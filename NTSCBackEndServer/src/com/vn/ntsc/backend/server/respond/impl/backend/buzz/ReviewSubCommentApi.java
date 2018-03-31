/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import java.util.HashSet;
import java.util.Set;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.buzz.CommentDetailDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingSubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.SubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.SubCommentListDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.entity.impl.buzz.Buzz;
import com.vn.ntsc.backend.entity.impl.buzz.ReviewingSubComment;
import com.vn.ntsc.backend.entity.impl.datarespond.ReviewSubCommentData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import static com.vn.ntsc.backend.server.respond.impl.backend.buzz.ReviewCommentApi.addInteraction;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ReviewSubCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String subCommentId = Util.getStringParam(obj, "sub_comment_id");
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            if (subCommentId == null || type == null) {
                return null;
            }
            ReviewingSubComment subComment = ReviewingSubCommentDAO.get(subCommentId);
            if(subComment != null){
                Buzz buzz = BuzzDetailDAO.getBuzzDetail(subComment.buzzId);
                boolean isAppearBuzz = UserBuzzDAO.isAppearBuzz(buzz.userId, subComment.buzzId);
                if(isAppearBuzz){
                    ReviewingSubCommentDAO.remove(subCommentId);
//                    ReviewingCommentDAO.reviewSubComment(subComment.commentId);
                    if(type == Constant.REVIEW_STATUS_FLAG.APPROVED){
                        SubCommentDAO.updateApprovedFlag(subCommentId, Constant.REVIEW_STATUS_FLAG.APPROVED);
                        SubCommentListDAO.updateApproveFlag(subComment.commentId, subComment.subCommentId, Constant.REVIEW_STATUS_FLAG.APPROVED);

                        UserBuzzDAO.updateBuzzActivity(subComment.buzzId, buzz.userId, Util.currentTime());
                        String commentOwnerId = CommentDetailDAO.getUserId(subComment.commentId);
                        Set<String> notificationList = SubCommentListDAO.getAllUserAddSubComment(subCommentId);
                        notificationList.add(buzz.userId);
                        notificationList.add(commentOwnerId);
                        ReviewSubCommentData data = new ReviewSubCommentData(subComment.userId, buzz.userId, subComment.buzzId, subComment.commentId, commentOwnerId, notificationList);
                        respond.data = data;
                        
                        if (!buzz.userId.equals(subComment.userId)) {
                            addInteraction(buzz.userId, subComment.userId);
                        }
                        if (!commentOwnerId.equals(subComment.userId)) {
                            addInteraction(commentOwnerId, subComment.userId);
                        }
                        
                    }else{
                        SubCommentDAO.updateApprovedFlag(subCommentId, Constant.FLAG.OFF);
                        SubCommentListDAO.updateApproveFlag(subComment.commentId, subComment.subCommentId, Constant.REVIEW_STATUS_FLAG.DENIED);
                        String commentOwnerId = CommentDetailDAO.getUserId(subComment.commentId);
                        ReviewSubCommentData data = new ReviewSubCommentData(subComment.userId, buzz.userId, subComment.buzzId, subComment.commentId, commentOwnerId, new HashSet<String>());
                        respond.data = data;
                    }
                }
            }
            respond.code = ErrorCode.SUCCESS;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
