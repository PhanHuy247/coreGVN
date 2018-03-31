/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.user;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.log.LogUserInfoDAO;
import com.vn.ntsc.backend.dao.user.ReviewingUserDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.ReviewUserData;
import com.vn.ntsc.backend.entity.impl.user.ReviewingUser;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ReviewUserApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.ID);
            Util.addDebugLog("=======userId===" + userId);
            if (userId == null) {
                return null;
            }
            Long type = Util.getLongParam(obj, "about");//thuc ra day la thong tin type de phan biet approved hay deny
            //String userDeny = Util.getStringParam(obj, ParamKey.USER_ID);
            String userDeny = Util.getStringParam(obj, "admin_name");
            Long hobby = Util.getLongParam(obj, "hobby");
            Long typeOfMan = Util.getLongParam(obj, "type_of_man");
            Long fetish = Util.getLongParam(obj, "fetish");
            Long about = Util.getLongParam(obj, "about");
            ReviewingUser user = ReviewingUserDAO.get(userId);
            if (user != null) {
                ReviewingUser reviewingUser = new ReviewingUser();
                if (about != null && about == Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    String fieldValue = user.about;
                    reviewingUser.setField("about", fieldValue);
                }
                if (hobby != null && hobby == Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    String fieldValue = user.hobby;
                    reviewingUser.setField("hobby", fieldValue);
                }
                if (typeOfMan != null && typeOfMan == Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    String fieldValue = user.typeOfMan;
                    reviewingUser.setField("type_of_man", fieldValue);
                }
                if (fetish != null && fetish == Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    String fieldValue = user.fetish;
                    reviewingUser.setField("fetish", fieldValue);
                }
                ReviewingUserDAO.remove(userId);
                Util.addDebugLog("ReviewUserApi ===="+ getReviewResult(user, obj));
                Util.addDebugLog("ReviewUserApi type===="+ type);
                if (type == 1) {
                    UserDAO.updateReviewField(userId, reviewingUser);
                }

                Util.addDebugLog("=======ReviewingUser user userId===" + user.userId);
                Util.addDebugLog("=======ReviewingUser user Id===" + user.id);
                Util.addDebugLog("=======ReviewingUser userDeny ===" + userDeny);

                LogUserInfoDAO.insert(user.userId, type, Long.parseLong(user.time), user.gender, user.about, userDeny);

                ReviewUserData data = new ReviewUserData(userId, getReviewResult(user, obj));
                respond.data = data;
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

    private int getReviewResult(ReviewingUser user, JSONObject obj) {
        Long about = Util.getLongParam(obj, "about");
        about = about == null ? (long) Constant.REVIEW_STATUS_FLAG.DENIED : about;
        Long hobby = Util.getLongParam(obj, "hobby");
        hobby = hobby == null ? (long) Constant.REVIEW_STATUS_FLAG.DENIED : hobby;
        Long typeOfMan = Util.getLongParam(obj, "type_of_man");
        typeOfMan = typeOfMan == null ? (long) Constant.REVIEW_STATUS_FLAG.DENIED : typeOfMan;
        Long fetish = Util.getLongParam(obj, "fetish");
        fetish = fetish == null ? (long) Constant.REVIEW_STATUS_FLAG.DENIED : fetish;

        if (about == Constant.REVIEW_STATUS_FLAG.DENIED
                && hobby == Constant.REVIEW_STATUS_FLAG.DENIED
                && typeOfMan == Constant.REVIEW_STATUS_FLAG.DENIED
                && fetish == Constant.REVIEW_STATUS_FLAG.DENIED) {
            return -1;
        } else {
            if (about == Constant.REVIEW_STATUS_FLAG.DENIED && user.about != null) {
                return 0;
            }
            if (hobby == Constant.REVIEW_STATUS_FLAG.DENIED && user.hobby != null) {
                return 0;
            }
            if (typeOfMan == Constant.REVIEW_STATUS_FLAG.DENIED && user.typeOfMan != null) {
                return 0;
            }
            if (fetish == Constant.REVIEW_STATUS_FLAG.DENIED && user.fetish != null) {
                return 0;
            }
            return 1;
        }
    }
}
