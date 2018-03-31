/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.blacklist.BlackListManager;
import com.vn.ntsc.buzzserver.dao.impl.BuzzCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.CommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.SubCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.SubCommentListDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Comment;
import com.vn.ntsc.buzzserver.entity.impl.buzz.SubComment;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long sortBy = Util.getLongParam(obj, "sort_by");
            String cmtId = Util.getStringParam(obj, "cmt_id");
            Integer numberCmt = 0;
            Integer numberSubCmt = 0;
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            
//            List<String> deactiveList = Util.getListString(obj, ParamKey.DEACTIVE_LIST);
//            List<String> blackList = new ArrayList<>();
//            blackList.addAll(blockList);
//            blackList.addAll(deactiveList);
            Buzz buzz = BuzzDetailDAO.getBuzzDetail(buzzId);
            String buzzOwner = buzz.userId;
            if (blockList.contains(buzzOwner)) {
                result.code = ErrorCode.BLOCK_USER;
                return result;
            }
            if (BlackListManager.isDeactivateUser(buzzOwner)) {
                result.code = ErrorCode.USER_NOT_EXIST;
                return result;
            }
            if (buzzId == null || buzzId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            if (buzz.isApp == Constant.FLAG.OFF) {
                result.code = ErrorCode.WATTING_APPROVE;
                return result;
            }
            List<String> lCommnet = BuzzCommentDAO.getListComment(buzzId, userId);
            
            //List User Comment in Buzz
            List<String> lUserComment = BuzzCommentDAO.getUserCommentBuzz(buzzId);
            
            //cmtID, get 1 cmt in lst_cmt with cmt_id
            if(cmtId != null){
                
                if (!lCommnet.isEmpty()) {
                    numberCmt = CommentDAO.getNumberComment(lCommnet, blockList);
                    numberSubCmt = CommentDAO.getNumberSubComment(lCommnet, blockList);
                    Util.addDebugLog("numberSubCmt============================="+numberSubCmt);
                }
                Util.addDebugLog("numberCmt============================="+numberCmt);
                lCommnet.clear();
                lCommnet.add(cmtId);
                if(take == null && skip == null){
                    take = 1L;
                    skip = 0L;
                }
            }
            List<Comment> lCommentDetail = new ArrayList<>();
            if (!lCommnet.isEmpty()) {
                lCommentDetail = CommentDAO.getListComment(lCommnet, blockList,sortBy);
            }
            
           
            List<Comment> respondList = new ArrayList<>();
            if (skip < lCommentDetail.size()) {
                long startIndex = skip;
                long endIndex = startIndex + take;
                if (endIndex > lCommentDetail.size()) {
                    endIndex = lCommentDetail.size();
                }
                respondList = lCommentDetail.subList((int) startIndex, (int) endIndex);
            }
            
            for(Comment comment : respondList){
                if (userId!=null) {
                    comment.isDel = (userId.equals(comment.userId) || userId.equals(buzz.userId))? Constant.FLAG.ON : Constant.FLAG.OFF;
                }else{
                    comment.isDel = Constant.FLAG.OFF;
                }
              
                if(comment.subCommentNumber != null && comment.subCommentNumber > 0){
//                    List<String> lSubCommnet = SubCommentListDAO.getListSubComment(comment.cmtId);
                    List<SubComment> l4SubCommentDetail = new ArrayList<>();
                    List<String> subCommentIds = SubCommentListDAO.getListSubComment(comment.cmtId, userId);
                    List<SubComment> lSubCommentDetail = SubCommentDAO.getDetailByIds(subCommentIds, blockList);
//                    lSubCommentDetail = SubCommentDAO.getDetailByCommentId(comment.cmtId, blockList, deactiveList);
                    if (!lSubCommentDetail.isEmpty()) {
                        int endIndex = lSubCommentDetail.size();
                        int startIndex = lSubCommentDetail.size() - 4;
                        if (startIndex < 0) {
                            startIndex = 0;
                        }
                        l4SubCommentDetail = lSubCommentDetail.subList( startIndex, endIndex);
                    }
                    for(SubComment subComment : lSubCommentDetail){
                            
//                            if(userId.equals(buzz.userId) || userId.equals(comment.userId) || userId.equals(subComment.userId))
//                                subComment.canDelete = true;
                            if(userId!=null){
                            if(userId.equals(buzz.userId) || userId.equals(comment.userId) || userId.equals(subComment.userId))
                                subComment.canDelete = 1;
                            }else{
                                 subComment.canDelete = 0;
                            }
                    }
//                    comment.subCommentNumber = comment.subCommentNumber - (lSubCommnet.size() - lSubCommentDetail.size());
                    comment.subCommentNumber = lSubCommentDetail.size();
                    comment.subComment = l4SubCommentDetail;
                }
                 //cmtID, get 1 cmt in lst_cmt with cmt_id
                if(cmtId != null){
                    comment.commentNumber = numberCmt;
                    comment.allSubCommentNumber = numberSubCmt;
                    if(buzz.parentId != null){
                        comment.buzzParId = buzz.parentId;
                    }
                        //get list UserID comment in buzzId
                   for (String userIdComment : lUserComment) {
                       if(userIdComment == null){
                           continue;
                       }
                       comment.listUserCmt.add(userIdComment);
                   }
                   if(!comment.listUserCmt.contains(buzzOwner)){
                       comment.listUserCmt.add(buzzOwner);
                   }
                }
               
            }
            
            result = new ListEntityRespond(ErrorCode.SUCCESS, respondList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }

}
