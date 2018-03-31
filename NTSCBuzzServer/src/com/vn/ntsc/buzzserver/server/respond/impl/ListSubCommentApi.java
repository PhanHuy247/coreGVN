/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
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
public class ListSubCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String commentId = Util.getStringParam(obj, ParamKey.COMMENT_ID);
            String subCmtId = Util.getStringParam(obj, "sub_comment_id");
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long sortby = Util.getLongParam(obj, "sort_by");
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            Integer numberCmt = 0;
            Integer numberSubCmt = 0;
//            List<String> deactiveList = Util.getListString(obj, ParamKey.DEACTIVE_LIST);
            if (buzzId == null || buzzId.isEmpty() || commentId == null  || commentId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            
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
            if (buzz.isApp == Constant.FLAG.OFF) {
                result.code = ErrorCode.WATTING_APPROVE;
                return result;
            }

            Comment comment = CommentDAO.get(commentId);
            if(comment.flag != Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                result.code = ErrorCode.ACCESS_DENIED;
                return result;
            }
            if (blockList.contains(comment.userId)) {
                result.code = ErrorCode.ACCESS_DENIED;
                return result;
            }
            if (BlackListManager.isDeactivateUser(comment.userId)) {
                result.code = ErrorCode.ACCESS_DENIED;
                return result;
            }
            
            List<String> lSubCommnet = SubCommentListDAO.getListSubComment(commentId, userId);
            
            //list User SubCmt in Cmt
            List<String> lUserSubCmt = SubCommentListDAO.getListUserSubCmt(commentId);
            if(subCmtId != null){
                List<String> lCommnet = BuzzCommentDAO.getListComment(buzzId, userId);
                if (!lCommnet.isEmpty()) {
                    numberCmt = CommentDAO.getNumberComment(lCommnet, blockList);
                    numberSubCmt = CommentDAO.getNumberSubComment(lCommnet, blockList);
                    Util.addDebugLog("numberSubCmt============================="+numberSubCmt);
                }
                Util.addDebugLog("numberCmt============================="+numberCmt);
                lSubCommnet.clear();
                lSubCommnet.add(subCmtId);
                skip = 0L;
                take = 1L;
            }
            List<SubComment> lSubCommentDetail = new ArrayList<>();
            if (!lSubCommnet.isEmpty()) {
                lSubCommentDetail = SubCommentDAO.getDetailByIdsSortBy(lSubCommnet, blockList,sortby);
            }
//            Collections.sort(lSubCommentDetail);
            List<SubComment> respondList = new ArrayList<>();
            if (skip < lSubCommentDetail.size()) {
                long startIndex = skip;
                long endIndex = startIndex + take;
                if (endIndex > lSubCommentDetail.size()) {
                    endIndex = lSubCommentDetail.size();
                }
                respondList = lSubCommentDetail.subList((int) startIndex, (int) endIndex);
            }
            for(SubComment subComment : respondList){
//                if(subComment.isApp == Constant.FLAG.ON){
                    if(userId!=null){
                        if(userId.equals(buzzOwner) || userId.equals(comment.userId) || userId.equals(subComment.userId))
                            subComment.canDelete = 1;
                    }else {
                        subComment.canDelete = 0;
                    }
//                }
                if(subCmtId != null){
                    subComment.allSubCommentNumber = numberSubCmt;
                    subComment.commentNumber = numberCmt;
                    if(buzz.parentId != null){
                        subComment.buzzParId = buzz.parentId;
                    }
                         
                    //get list UserID comment in buzzId
                   for (String userSubCmt : lUserSubCmt) {
                       if(userSubCmt == null){
                           continue;
                       }
                       subComment.listUserCmt.add(userSubCmt);
                   }
                   if(!subComment.listUserCmt.contains(comment.userId)){
                       subComment.listUserCmt.add(comment.userId);
                   }
                }
            }
//            Collections.sort(respondList, new SortSubCommentByTimeAsc());
//            Collections.sort(respondList);
            result = new ListEntityRespond(ErrorCode.SUCCESS, respondList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }

    private class SortSubCommentByTimeAsc implements Comparator<SubComment>{

        @Override
        public int compare(SubComment o1, SubComment o2) {
            return o1.time.compareTo(o2.time);
        }
    
    }
    
}
