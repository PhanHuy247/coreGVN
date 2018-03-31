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
import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingBuzzDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingCommentDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingSubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.dao.user.UserActivityDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.buzz.ReviewingBuzz;
import com.vn.ntsc.backend.entity.impl.datarespond.ReviewBuzzData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ReviewBuzzApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long flag_func = Util.getLongParam(obj, "flag_func");
            String userDeny = Util.getStringParam(obj, "admin_name");
            if (buzzId == null || type == null) {
                return null;
            }
            ReviewingBuzz buzz = ReviewingBuzzDAO.get(buzzId);
            if (flag_func != null) {
                BuzzDetailDAO.updateApprovedFlag(buzzId, type.intValue(), userDeny);
                UserBuzzDAO.updateApproveFlag(userId, buzzId, type.intValue());
            } else {
                if (buzz != null) {
                    if (type == Constant.REVIEW_STATUS_FLAG.APPROVED) {
                        ReviewingBuzzDAO.remove(buzzId);
                        BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED, userDeny);
                        UserBuzzDAO.updateApproveFlag(buzz.userId, buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED);
                        UserActivityDAO.updateStatus(buzz.userId, buzz.buzzVal);
                        UserDAO.addBuzz(buzz.userId);
                        com.vn.ntsc.backend.dao.user.UserBuzzDAO.add(buzzId, buzz.userId);
                        ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                        ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                    } else {
                        ReviewingBuzzDAO.remove(buzzId);
                        BuzzDetailDAO.updateApprovedFlag2(buzzId, Constant.REVIEW_STATUS_FLAG.DENIED, userDeny);
                        UserBuzzDAO.updateApproveFlag(buzz.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
                    }
                    ReviewBuzzData data = new ReviewBuzzData(buzz.userId, buzzId);
                    respond.data = data;
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
