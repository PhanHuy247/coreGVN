/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.Setting;
import com.vn.ntsc.buzzserver.blacklist.BlackListManager;
import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.CommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.ReviewingSubCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.SubCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.SubCommentListDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Comment;
import com.vn.ntsc.buzzserver.entity.impl.datarespond.AddSubCommentData;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class AddSubCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            String commentId = Util.getStringParam(obj, ParamKey.COMMENT_ID);
            String value = Util.getStringParam(obj, "value");
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
//            List<String> deactiveList = Util.getListString(obj, ParamKey.DEACTIVE_LIST);
            if (buzzId == null || buzzId.isEmpty() || value == null || value.isEmpty()
                    || commentId == null || commentId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            
//            boolean check = BuzzDetailDAO.checkBuzzExist(buzzId);
//            if (!check) {
//                result.code = ErrorCode.BUZZ_NOT_FOUND;
//                return result;
//            }
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

            if(buzz.parentId != null && !buzz.parentId.isEmpty()){
                int approvedFlag = UserBuzzDAO.getApprovedFlag(buzz.userId, buzz.parentId);
                if (approvedFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    result.code = ErrorCode.WATTING_APPROVE;
                    return result;
                }
            }else{
                int approvedFlag = UserBuzzDAO.getApprovedFlag(buzz.userId, buzzId);
                if (approvedFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    result.code = ErrorCode.WATTING_APPROVE;
                    return result;
                }
            }

            
            Comment comment = CommentDAO.get(commentId);
            if(comment.flag != Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG || comment.isApp != Constant.FLAG.ON){
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
            
            int approveFlag = Setting.auto_approve_comment;
            String id = SubCommentDAO.addSubComment(userId, buzzId, commentId, value, approveFlag, time);
            SubCommentListDAO.addSubComment(commentId, id, userId, approveFlag);
            CommentDAO.addSubComment(commentId);
            Set<String> notificationList = new HashSet<>();
            if(approveFlag == Constant.FLAG.OFF){
                ReviewingSubCommentDAO.addSubComment(buzzId, userId, commentId, id, value, time, ip);
//                ReviewingCommentDAO.addSubComment(commentId, buzzId, comment.userId, comment.cmtVal,DateFormat.parse(comment.cmtTime).getTime() ,ip);
            }else{
                UserBuzzDAO.updateBuzzActivity(buzzId, buzzOwner, time, null, null, null, null);
                notificationList = SubCommentListDAO.getAllUserAddSubComment(commentId);
                notificationList.add(buzzOwner);
                notificationList.add(comment.userId);
            }

            AddSubCommentData data = new AddSubCommentData(buzzId, time, commentId, id, notificationList, buzzOwner, comment.userId, approveFlag,comment.cmtVal,comment.userId,comment.cmtTime);
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
