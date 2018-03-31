/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import java.util.ArrayList;
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
import com.vn.ntsc.buzzserver.dao.impl.BuzzTagDAO;
import com.vn.ntsc.buzzserver.dao.impl.BuzzViewDAO;
import com.vn.ntsc.buzzserver.dao.impl.CommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.LikeDAO;
import com.vn.ntsc.buzzserver.dao.impl.SubCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.SubCommentListDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Comment;
import com.vn.ntsc.buzzserver.entity.impl.buzz.SubComment;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Tag;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.json.simple.JSONArray;

/**
 *
 * @author RuAc0n
 */
public class GetBuzzDetailApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        Util.addDebugLog("============GetBuzzDetailApi==============");
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            List<String> friendList = Util.getListString(obj, ParamKey.FRIEND_LIST);
//            List<String> deactiveList = Util.getListString(obj, ParamKey.DEACTIVE_LIST);
//            List<String> blackList = new ArrayList<>();
//            blackList.addAll(blockList);
//            blackList.addAll(deactiveList);
            if (buzzId == null || buzzId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            Buzz buzz = BuzzDetailDAO.getBuzzDetail(buzzId);
          
            Boolean isFriend = friendList.contains(userId);
            Boolean isCurrentUser = (buzz.userId.equals(userId));
            if (blockList.contains(buzz.userId)) {
//                System.out.println("vao block");
                result.code = ErrorCode.BLOCK_USER;
                return result;
            }
            if (BlackListManager.isDeactivateUser(buzz.userId)) {
//                System.out.println("vao deactive");
                result.code = ErrorCode.USER_NOT_EXIST;
                return result;
            }
            if (buzz.isApp == Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG) {
                result.code = ErrorCode.ACCESS_DENIED;
                return result;
            }
            if (buzz.isApp == Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG && !buzz.userId.equals(userId)) {
                result.code = ErrorCode.WATTING_APPROVE;
                return result;
            }
            if ( (buzz.privacy == Constant.POST_MODE.FRIEND && !isFriend && !isCurrentUser) || (buzz.privacy == Constant.POST_MODE.ONLY_ME && !isCurrentUser) ){
                result.code = ErrorCode.BLOCK_USER;
                return result;
            }
//            BuzzDetailDAO.addSeen(buzzId);
            buzz.isLike = LikeDAO.checkLike(buzzId, userId);
            List<String> lCommnet = BuzzCommentDAO.getListComment(buzzId, userId);
            List<Comment> lCommentDetail = new ArrayList<>();
            if (!lCommnet.isEmpty()) {
                lCommentDetail = CommentDAO.getListComment(lCommnet, blockList);
            }
            buzz.cmtNum = (long)lCommentDetail.size();
            List<Comment> respondList = new ArrayList<>();
            if (!lCommentDetail.isEmpty()) {
                long startIndex = skip;
                long endIndex = startIndex + take;
                if (endIndex > lCommentDetail.size()) {
                    endIndex = lCommentDetail.size();
                }
                respondList = lCommentDetail.subList((int) startIndex, (int) endIndex);
            }
            buzz.comment = respondList;
            for(Comment comment : buzz.comment){
                if (userId!=null) {
                    comment.isDel =  comment.isDel == Constant.FLAG.ON 
                        &&(userId.equals(comment.userId) || userId.equals(buzz.userId))? Constant.FLAG.ON : Constant.FLAG.OFF;
                }else{
                    comment.isDel = Constant.FLAG.ON;
                }
               
                if(comment.subCommentNumber != null && comment.subCommentNumber > 0){
//                    List<String> lSubCommnet = SubCommentListDAO.getListSubComment(comment.cmtId);
                    List<SubComment> l4SubCommentDetail = new ArrayList<>();
                    List<String> subCommentIds = SubCommentListDAO.getListSubComment(comment.cmtId, userId);
                    List<SubComment> lSubCommentDetail = SubCommentDAO.getDetailByIds(subCommentIds, blockList);
//                    lSubCommentDetail = SubCommentDAO.getDetailByCommentId(comment.cmtId, blockList, deactiveList);
                    if (!lSubCommentDetail.isEmpty()) {

                        int endIndex = lSubCommentDetail.size();
                        int startIndex = lSubCommentDetail.size() - 3;
                        if (startIndex < 0) {
                            startIndex = 0;
                        }
                        l4SubCommentDetail = lSubCommentDetail.subList( startIndex, endIndex);
//                        Collections.sort(l4SubCommentDetail);
                    }
                    for(SubComment subComment : lSubCommentDetail){
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
            }
            
            buzz.subCmtNum = SubCommentDAO.getSubCommentNumber(buzzId, blockList, lCommnet);
            
            buzz.listChild = BuzzDetailDAO.getChildBuzz(buzzId, userId);
            if(imageId != null){
                buzz.listChild = BuzzDetailDAO.getChildBuzzWithImageId(buzzId,imageId);
            }
            buzz.childNum = buzz.listChild.size();
            
            if(buzz.childNum > 0){
                for(Buzz item: buzz.listChild){
                    List<String> lcComment = BuzzCommentDAO.getListComment(item.buzzId, userId);
                    Integer num = SubCommentDAO.getSubCommentNumber(item.buzzId, blockList, lcComment);
                    item.subCmtNum = num;
                }
            }
            
            Integer count = BuzzViewDAO.count(buzzId);
            if(buzz.viewNumber != null){
                buzz.viewNumber = buzz.viewNumber + count;
            }else{
                buzz.viewNumber = count;
            }
            
            List<Tag> tagBuzz = BuzzTagDAO.getTag(buzzId);
                        
            List<String> tagUserList = new ArrayList<>();
            for(Tag item : tagBuzz){
                tagUserList.add(item.userId);
            }
            Map<String, String> mUserName = UserDAO.getListUserName(tagUserList);
            for(Tag item: tagBuzz){
                item.userName = mUserName.get(item.userId);
            }
            buzz.listTag = tagBuzz;
            buzz.tagNum = tagBuzz.size();
            
            if(buzz.shareDetail != null){
                String shareBuzzId = buzz.shareDetail.buzzId;
                
                List<Comment> lCommentId = BuzzCommentDAO.getListShareComment(shareBuzzId, userId);
                long commentNumber = 0;
                for (Comment comment : lCommentId) {
                    String commenter = comment.userId;
                    if (!BlackListManager.isDeactivateUser(commenter) && !blockList.contains(commenter)) {
                        commentNumber++;
                    }
                }
                buzz.shareDetail.cmtNum = commentNumber;
                
                List<String> lcComment = BuzzCommentDAO.getListComment(shareBuzzId, userId);
                Integer num = SubCommentDAO.getSubCommentNumber(shareBuzzId, blockList, lcComment);
                buzz.shareDetail.subCmtNum = num;
                
                List<Tag> tagShareBuzz = BuzzTagDAO.getTag(shareBuzzId);
                List<String> tagShareUserList = new ArrayList<>();
                for(Tag item : tagShareBuzz){
                    tagShareUserList.add(item.userId);
                }
                Map<String, String> mShareUserName = UserDAO.getListUserName(tagShareUserList);
                for(Tag item: tagShareBuzz){
                    item.userName = mShareUserName.get(item.userId);
                }
                buzz.shareDetail.listTag = tagShareBuzz;
                buzz.shareDetail.tagNum = tagShareBuzz.size();
            }
            
            result = new EntityRespond(ErrorCode.SUCCESS, buzz);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }

}
