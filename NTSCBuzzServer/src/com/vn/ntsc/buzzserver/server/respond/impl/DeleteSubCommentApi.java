/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

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
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Comment;
import com.vn.ntsc.buzzserver.entity.impl.buzz.SubComment;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class DeleteSubCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String commentId = Util.getStringParam(obj, ParamKey.COMMENT_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String subCommentId = Util.getStringParam(obj, "sub_comment_id");
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            Integer numberCmt = 0;
            Integer numberSubCmt = 0;
//            List<String> deactiveList = Util.getListString(obj, ParamKey.DEACTIVE_LIST);
            if (commentId == null || commentId.isEmpty() || buzzId == null || buzzId.isEmpty() 
                    || subCommentId == null  || subCommentId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            Buzz buzz = BuzzDetailDAO.getBuzzDetail(buzzId);
            String buzzOwner = buzz.userId;
//            if (buzz.isApp == Constant.FLAG.OFF) {
//                result.code = ErrorCode.WATTING_APPROVE;
//                return result;
//            }
            if (blockList.contains(buzzOwner)) {
                result.code = ErrorCode.BLOCK_USER;
                return result;
            }
            if (BlackListManager.isDeactivateUser(buzzOwner)) {
                result.code = ErrorCode.USER_NOT_EXIST;
                return result;
            }
//            if(buzz.parentId != null && !buzz.parentId.isEmpty()){
//                int approvedFlag = UserBuzzDAO.getApprovedFlag(buzz.userId, buzz.parentId);
//                if (approvedFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
//                    result.code = ErrorCode.WATTING_APPROVE;
//                    return result;
//                }
//            }else{
//                int approvedFlag = UserBuzzDAO.getApprovedFlag(buzz.userId, buzzId);
//                if (approvedFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
//                    result.code = ErrorCode.WATTING_APPROVE;
//                    return result;
//                }
//            }
            
            Comment comment = CommentDAO.get(commentId);
            if(comment.flag != Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                result.code = ErrorCode.COMMENT_NOT_FOUND;
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
            
            SubComment subComment = SubCommentDAO.get(subCommentId);
//            if(subComment.isApp == Constant.FLAG.ON){
                if(userId.equals(buzzOwner) || userId.equals(subComment.userId)){
                    if (subComment.flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG) {
                        SubCommentDAO.updateFlag(subCommentId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                        CommentDAO.removeSubComment(commentId);
                        SubCommentListDAO.delSubComment(commentId, subCommentId);
                        
                        List<String> lCommnet = BuzzCommentDAO.getListComment(buzzId, userId);
                
                        if (!lCommnet.isEmpty()) {
                            numberCmt = CommentDAO.getNumberComment(lCommnet, blockList);
                            numberSubCmt = CommentDAO.getNumberSubComment(lCommnet, blockList);
                            Util.addDebugLog("numberSubCmt============================="+numberSubCmt);
                        }
                        Comment commentDel = new Comment();
                        commentDel.commentNumber = numberCmt;
                        commentDel.allSubCommentNumber = numberSubCmt;
                        Util.addDebugLog("numberCmt============================="+numberCmt);
                        result.code = ErrorCode.SUCCESS;
                        result.data = commentDel;
                    }
                    result.code = ErrorCode.SUCCESS;
                }else{
                    result.code = ErrorCode.ACCESS_DENIED;
                }
//            }else{
//                result.code = ErrorCode.WATTING_APPROVE;
//            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
