/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.buzz.BuzzCommentDAO;
import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.buzz.CommentDetailDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingCommentDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.dao.buzz.UserInteractionDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.buzz.Buzz;
import com.vn.ntsc.backend.entity.impl.buzz.ReviewingComment;
import com.vn.ntsc.backend.entity.impl.datarespond.ReplacedReviewCommentData;
import com.vn.ntsc.backend.entity.impl.datarespond.ReviewCommentData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ReviewCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond<IEntity> respond = new ListEntityRespond<IEntity>();// LongLT 8/2016
        try {
            String commentId = Util.getStringParam(obj, "comment_id");
            String buzzId = Util.getStringParam(obj, "buzz_id");
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            String userDeny = Util.getStringParam(obj, "admin_name");
            Long flag_func = Util.getLongParam(obj, "flag_func");
            

            if (commentId == null || type == null) {
                return null;
            }
            ReviewingComment comment = ReviewingCommentDAO.get(commentId);
//            if(comment != null && comment.reviewFlag != Constant.YES){
            if (flag_func != null) {
                CommentDetailDAO.updateApprovedFlag(commentId, type.intValue(), userDeny);
                BuzzCommentDAO.updateApproveFlag(buzzId, commentId, type.intValue());
            } else {
                if (comment != null) {
                    Buzz buzz = BuzzDetailDAO.getBuzzDetail(comment.buzzId);
                    boolean isAppearBuzz = UserBuzzDAO.isAppearBuzz(buzz.userId, comment.buzzId);
                    if (isAppearBuzz) {
                        if (type == Constant.REVIEW_STATUS_FLAG.APPROVED) {
//                        ReviewingCommentDAO.updateReveiwFlag(commentId, Constant.YES);
                            CommentDetailDAO.updateApprovedFlag(commentId, Constant.REVIEW_STATUS_FLAG.APPROVED, userDeny);
                            BuzzCommentDAO.updateApproveFlag(comment.buzzId, comment.commentId, Constant.REVIEW_STATUS_FLAG.APPROVED);
                            UserBuzzDAO.updateBuzzActivity(comment.buzzId, buzz.userId, Util.currentTime());
                            ReviewingCommentDAO.remove(commentId);
                            Util.addDebugLog("===================" + buzz.userId);
                            Util.addDebugLog("===================" + comment.userId);
                            if (!buzz.userId.equals(comment.userId)) {
                                addInteraction(buzz.userId, comment.userId);
                            }
                        } else {
                            ReviewingCommentDAO.remove(commentId);
                            CommentDetailDAO.updateApprovedFlag(commentId, Constant.REVIEW_STATUS_FLAG.DENIED, userDeny);
                            BuzzCommentDAO.updateApproveFlag(comment.buzzId, comment.commentId, Constant.REVIEW_STATUS_FLAG.DENIED);
                        }

                        // LongLT 8/2016
                        ReplacedReviewCommentData rrcd
                                = new ReplacedReviewCommentData(commentId, Util.replaceBannedWord(comment.commentValue));
                        respond.data.add(rrcd);

                        String buzzOwnerId = BuzzDetailDAO.getUserId(comment.buzzId);
                        ReviewCommentData data = new ReviewCommentData(comment.userId, buzzOwnerId, comment.buzzId);
                        respond.data.add(data);

                    }
                }
            }
            respond.code = ErrorCode.SUCCESS;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            return new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    public static void addInteraction(String userId, String friendId) {

        boolean isExistUserInteractionList = UserInteractionDAO.checkExistInteractionList(userId);
        boolean isExistUserInteraction = UserInteractionDAO.checkExistInteraction(userId, friendId);

        boolean isExistFriendInteractionList = UserInteractionDAO.checkExistInteractionList(friendId);
        boolean isExistFriendInteraction = UserInteractionDAO.checkExistInteraction(friendId, userId);

        if (isExistUserInteractionList && !isExistUserInteraction) {
            UserInteractionDAO.updateInteraction(userId, friendId);
        } else if (!isExistUserInteractionList) {
            UserInteractionDAO.addInteraction(userId, friendId);
        }

        if (isExistFriendInteractionList && !isExistFriendInteraction) {
            UserInteractionDAO.updateInteraction(friendId, userId);
        } else if (!isExistFriendInteractionList) {
            UserInteractionDAO.addInteraction(friendId, userId);
        }
    }
}
