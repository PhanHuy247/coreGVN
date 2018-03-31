/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.DeactivateDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ReviewingUserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogUserInfoDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.UpdateUserData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.entity.impl.user.ReviewingUser;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Female;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Male;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.common.UserInforValidator;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;
import com.vn.ntsc.usermanagementserver.setting.Setting;
import eazycommon.constant.mongokey.UserdbKey;

/**
 *
 * @author RuAc0n
 */
public class UpdateUserApi implements IApiAdapter {


    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String id = Util.getStringParam(obj, ParamKey.USER_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            int userType = Util.getIntParam(obj, ParamKey.TYPE);
            User user;

            int gender = UserInforManager.getGender(id);
            if (gender == Constant.GENDER.FEMALE) {
                user = new Female();
            } else {
                user = new Male();
            }

            // LongLT 20Sep2016 ///////////////////////////  #4483
            Object isPurchaseTemp = obj.get(ParamKey.IS_PURCHASE);
            if ((isPurchaseTemp != null) && ("".equals(isPurchaseTemp.toString()))) {
                Integer isPurchase = new Integer(isPurchaseTemp.toString());
                user.isPurchased = isPurchase;
            }
            //Thanhddd #5130
            Object siteIdTemp = obj.get(ParamKey.SITE_ID);
            if ((siteIdTemp != null) && ("".equals(siteIdTemp.toString()))) {
                Integer site_id = new Integer(siteIdTemp.toString());
                user.site_id = site_id;
            }
            user.gender = Long.valueOf(gender);
            user.getUpdateUser(obj);

            user.lastUpdate = DateFormat.format(time);

            //long deviceType = (Long) obj.get("device_type");
            User u = UserDAO.getUserInfor(id);
            long deviceType = u.deviceType;

            String api = (String) obj.get(ParamKey.API_NAME);
            ReviewingUser reviewingUser = ReviewingUserDAO.get(id);
            if (api.equals(API.UPDATE_USER_INFOR)) {
                Util.addDebugLog("id " + id);
                Util.addDebugLog("deviceType " + deviceType);
                Util.addDebugLog("user.job " + user.job);
                Util.addDebugLog("user.appVersion " + user.appVersion);
                Util.addDebugLog("u.appVersion " + u.appVersion);

//            String api = (String) obj.get(ParamKey.API_NAME);
//            if (api.equals(API.UPDATE_USER_INFOR)) {
                //String app_ver = Util.getStringParam(obj, ParamKey.APP_VERSION);
                if (deviceType == 1 && user.gender == 0) {
                    if (u.appVersion != null && u.appVersion.equals("1.1")) {
                        if (user.job != null) {
                            user.job = user.job - 17;
                        }
                        Util.addDebugLog("user.job -17 " + user.job);
                    }
                }
                // }

                return updateByUser(user, id, time, ip, reviewingUser);
            } else {
                if (userType == 0) {
                    return updateByAdminChangeFacebookToEmail(user, obj, time, userType);
                }
                return updateByAdmin(user, obj, time);
            }

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

        return result;
    }

    private EntityRespond updateByUser(User user, String id, Date time, String ip, ReviewingUser reviewingUser) throws EazyException {
        ReviewingUserDAO.remove(id);
        EntityRespond result = new EntityRespond();
        if (!Tool.checkUserName(id, user.username)) {
            result.code = ErrorCode.INVALID_USER_NAME;
            return result;
        }

       
        
        if (user instanceof Female) {
//            if (!Tool.checkFemaleName(id, user.username)) {
//                result.code = ErrorCode.DUPLICATE_USER_NAME;
//                return result;
//            }
            ((Female) user).verificationFlag = null;
        }
//        if (!UserInforValidator.validateAutoRegion(user) || !UserInforValidator.validateRegion(user, 0L)) {
        if (!UserInforValidator.validateRegion(user, 0L)) {
            result.code = ErrorCode.WRONG_DATA_FORMAT;
            return result;
        }
        if (user.username == null) {
            result.code = ErrorCode.WRONG_DATA_FORMAT;
            return result;
        }
        User beforeUser = UserDAO.getUserInfor(id);

        int birFlag = beforeUser.finishRegisterFlag.intValue();
        int userType = beforeUser.userType.intValue();
        
        if(userType != 1){
            if(UserDAO.isexitsedEmail(user.email, id)){
                result.code = ErrorCode.EMAIL_REGISTED;
                return result;
            }

            if (!Util.validateEmail(user.email)) {
                result.code = ErrorCode.INVALID_EMAIL;
                return result;
            }
        }
        
        
        if (!UserInforValidator.validateBirthday(user, birFlag)) {
            result.code = ErrorCode.INVALID_BIRTHDAY;
            return result;
        }
//        if (!UserInforValidator.validateJob(user, Constant.FLAG.OFF, beforeUser)) {
//            result.code = ErrorCode.WRONG_DATA_FORMAT;
//            return result;
//        }
        if (!UserInforValidator.validateName(user, Constant.FLAG.ON)) {
            result.code = ErrorCode.WRONG_DATA_FORMAT;
            return result;
        }
        if (!UserInforValidator.validate(user, Constant.FLAG.OFF, beforeUser)) {
            result.code = ErrorCode.WRONG_DATA_FORMAT;
            return result;
        }

        String reviewId = null;
        if (Setting.AUTO_APPROVE_REVIEW_USER == Constant.FLAG.OFF) {
            reviewingUser = addReviewingUser(user, beforeUser, id);
        }

        user.gender = null;
        user.flag = null;
//        user.email = null;
        user.password = null;
        user.originalPassword = null;
        user.fbId = null;
        user.cmCode = null;

        int isFinishRegister = Constant.FLAG.OFF;
        if (beforeUser.finishRegisterFlag != Constant.FLAG.ON) {
            isFinishRegister = Constant.FLAG.ON;
            user.finishRegisterFlag = (long) Constant.FLAG.ON;
        }

        int isNoti = Constant.FLAG.OFF;
        if (Setting.AUTO_APPROVE_REVIEW_USER == Constant.FLAG.ON) {
            if (NotificationSettingDAO.checkUserNotification(id, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_USER_INFOR_NOTI)) {
                isNoti = getNotification(beforeUser, user);
                // thanhdd edit 20/10
                boolean pushAbt = isNotiStatus(beforeUser, user);
                Util.addDebugLog("==========pushAbt:" + pushAbt);
                if (pushAbt) {
                    isNoti = Constant.FLAG.ON;
                } else {
                    isNoti = Constant.FLAG.OFF;
                }

                if (isNoti == Constant.FLAG.ON) {
                    Notification ownerNoti = new Notification();
                    String notificationId = NotificationDAO.addNotification(id, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_USER_INFOR_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                }
            }
        }

        User respond = null;
        UserDAO.updateBlankTextField(id, user, beforeUser.gender.intValue());
        if (Setting.AUTO_APPROVE_REVIEW_USER == Constant.FLAG.OFF) {
            //Linh add
            LogUserInfoDAO.insert(reviewId, user, beforeUser, time.getTime(), Constant.REVIEW_STATUS_FLAG.PENDING);

            user.about = beforeUser.about;
            respond = UserDAO.updateUser(id, user);
//            respond.about = beforeUser.about;
            getOldData(respond, beforeUser, reviewingUser);

        } else {
            //Linh add 
            LogUserInfoDAO.insert(reviewId, user, beforeUser, time.getTime(), Constant.REVIEW_STATUS_FLAG.APPROVED);

            respond = UserDAO.updateUser(id, user);
            Util.addDebugLog("updateTextField  " + Setting.AUTO_APPROVE_REVIEW_USER);
            UserDAO.updateTextField(id, user, beforeUser.gender.intValue());
            respond.about = user.about;
            if (respond.gender == Constant.GENDER.FEMALE) {
                Female female = (Female) respond;
                Female oldFemale = (Female) user;
                female.typeOfMan = oldFemale.typeOfMan;
                female.fetishs = oldFemale.fetishs;
//                female.hobby = oldFemale.hobby;
            } else {
                Male male = (Male) respond;
                Male oldMale = (Male) user;
//                male.hobby = oldMale.hobby;
            }
        }
        UpdateUserData data = new UpdateUserData(isFinishRegister, respond, isNoti);
        data.isVerify = Helper.isVerify(respond);
        data.isReviewed = false;
        if (Setting.AUTO_APPROVE_REVIEW_USER == Constant.FLAG.OFF) {
            ReviewingUser reviewUser = ReviewingUserDAO.get(id);
            data.isReviewed = isReviewed(beforeUser, reviewUser);
        } else {
            data.isReviewed = false;
        }

        if (data.isVerify && beforeUser.finishRegisterFlag != Constant.FLAG.ON) {
            Helper.addLoginTime(time, time, null, id, ip);
            UserDAO.updateLoginTime(id, time);
            int curPoint = UserInforManager.getPoint(id);
            ActionManager.doAction(ActionType.daily_bonus, id, null, Util.getGMTTime(), null, null, ip);
            int bonusPoint = UserInforManager.getPoint(id) - curPoint;
            if (bonusPoint > 0) {
                data.addPoint = bonusPoint;
                Notification noti = new Notification();
                noti.point = bonusPoint;
                String notificationId = NotificationDAO.addNotification(id, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DAILY_BONUS_NOTI);
                NotificationCleaner.put(notificationId, time.getTime());
            }
        }
        result = new EntityRespond(ErrorCode.SUCCESS, data);
        Util.addDebugLog("============result:" + result.toString());
        return result;
    }

    private int getNotification(User before, User after) {
        if (canNotification(before.about, after.about)) {
            return Constant.FLAG.ON;
        } else {
            if (before.gender.intValue() == Constant.GENDER.FEMALE) {
                Female female = (Female) after;
                Female oldFemale = (Female) before;
                if (canNotification(oldFemale.fetishs, female.fetishs)) {
                    return Constant.FLAG.ON;
                } else if (canNotification(oldFemale.typeOfMan, female.typeOfMan)) {
                    return Constant.FLAG.ON;
                }
            } else {
                Male male = (Male) after;
                Male oldMale = (Male) before;
                if (canNotification(oldMale.hobby, male.hobby)) {
                    return Constant.FLAG.ON;
                }
            }
            return Constant.FLAG.OFF;
        }
    }

    private boolean canNotification(String before, String after) {
        if (after != null && !after.isEmpty()) {
            if (before == null) {
                return true;
            }
            if (!before.trim().equals(after.trim())) {
                return true;
            }
        }
        return false;
    }

    private void getOldData(User respond, User beforeUser, ReviewingUser reviewingUser) {
        // return old data
        respond.about = beforeUser.about;
        if (respond.gender == Constant.GENDER.FEMALE) {
            Female female = (Female) respond;
            Female oldFemale = (Female) beforeUser;
            female.typeOfMan = oldFemale.typeOfMan;
            female.fetishs = oldFemale.fetishs;
//            female.hobby = oldFemale.hobby;
        } else {
            Male male = (Male) respond;
            Male oldMale = (Male) beforeUser;
//            male.hobby = oldMale.hobby;
        }
        if (reviewingUser != null) {
            if (reviewingUser.about != null) {
                respond.about = reviewingUser.about;
            }
            if (respond.gender == Constant.GENDER.FEMALE) {
                Female female = (Female) respond;
                if (reviewingUser.fetish != null) {
                    female.fetishs = reviewingUser.fetish;
                }
                if (reviewingUser.typeOfMan != null) {
                    female.typeOfMan = reviewingUser.typeOfMan;
                }
//                if (reviewingUser.hobby != null) {
//                    female.hobby = reviewingUser.hobby;
//                }
            } else {
                Male male = (Male) respond;
//                if (reviewingUser.hobby != null) {
//                    male.hobby = reviewingUser.hobby;
//                }
            }
        }
    }

    private ReviewingUser addReviewingUser(User user, User beforeUser, String id) throws EazyException {
        // get all updated infor to reviewing
        ReviewingUser reviewingUser = new ReviewingUser();
        reviewingUser.about = getReviewingValue(user.about, beforeUser.about);
        if (beforeUser.gender == Constant.GENDER.FEMALE) {
            Female female = (Female) user;
            Female oldFemale = (Female) beforeUser;
            reviewingUser.typeOfMan = getReviewingValue(female.typeOfMan, oldFemale.typeOfMan);
            reviewingUser.fetish = getReviewingValue(female.fetishs, oldFemale.fetishs);
//             reviewingUser.hobby = getReviewingValue(female.hobby, oldFemale.hobby);
        } else {
            Male male = (Male) user;
            Male oldMale = (Male) beforeUser;
//            reviewingUser.hobby = getReviewingValue(male.hobby, oldMale.hobby);
        }
        String reviewId = ReviewingUserDAO.addReviewUser(id, beforeUser.gender.intValue(), reviewingUser);
        return reviewingUser;
    }

    private EntityRespond updateByAdminChangeFacebookToEmail(User user, JSONObject obj, Date time, int userType) throws EazyException {

        EntityRespond result = new EntityRespond();
        String id = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        ReviewingUserDAO.remove(id);
        User beforeUser = UserDAO.getUserInfor(id);

        Long delAbout = Util.getLongParam(obj, "del_abt");

        if (delAbout == null || delAbout < 0 || delAbout > 1) {
            result.code = ErrorCode.UNKNOWN_ERROR;
            return result;
        }
        if (!Tool.checkUserName(id, user.username)) {
            result.code = ErrorCode.INVALID_USER_NAME;
            return result;
        }

        if (!UserInforValidator.validateBirthday(user, Constant.FLAG.ON)) {
            result.code = 4;
            return result;
        }
        if (!UserInforValidator.validateName(user, beforeUser.finishRegisterFlag.intValue())) {
            result.code = 6;
            return result;
        }
        if (delAbout == 1) {
            user.about = "";
        }

        if (user.email != null) {
            if (UserDAO.checkEmail(user.email, id)) {
                result.code = ErrorCode.EMAIL_REGISTED;
                return result;
            }

            if (!user.email.trim().equals(beforeUser.email)) {

                if ((beforeUser.updateEmailFlag != null && beforeUser.updateEmailFlag != Constant.FLAG.ON) || beforeUser.updateEmailFlag == null) {
                    user.updateEmailFlag = (long) Constant.FLAG.ON;
                    user.userType = 0L;
                }
            }

        }
//        if (!UserInforValidator.validateJob(user, beforeUser.finishRegisterFlag.intValue(), beforeUser)) {
//            result.code = ErrorCode.WRONG_DATA_FORMAT;
//            return result;
//        }
        if (!UserInforValidator.validate(user, beforeUser.finishRegisterFlag.intValue(), beforeUser)) {
            result.code = ErrorCode.WRONG_DATA_FORMAT;
            return result;
        }
        // validate region
        int isFinishRegister = beforeUser.finishRegisterFlag.intValue();
        if (beforeUser.finishRegisterFlag == Constant.FLAG.OFF) {
            if (beforeUser.region == null) {
                if (user.region != null) {
                    user.autoRegion = (long) 0;
                }
            }

        }
        // validate finishFlag
        Integer flag = UserDAO.getFlag(id);
        user.password = null;
        user.originalPassword = null;
        user.point = null;
        User respond = UserDAO.updateUserChangeType(id, user, userType);

        UserDAO.updateBlankTextField(id, user, beforeUser.gender.intValue());
        UserDAO.updateTextField(id, user, beforeUser.gender.intValue());
        User afterUser = UserDAO.getUserInfor(id);

        if (beforeUser.finishRegisterFlag == Constant.FLAG.OFF) {
            if (afterUser.isCompleteUser()) {
                isFinishRegister = Constant.FLAG.ON;
                UserDAO.updateFinishRegister(id, Constant.FLAG.ON);
            }
        }
        if (isActiveToNotActiveUser(flag, user.flag)) {
            if (user.flag == Constant.USER_STATUS_FLAG.DEACITIVE) {
                DeactivateDAO.addDeactiveUser(id, null, time.getTime());
            }
            List<String> blackList = BlockUserManager.getBlackList(id);
            Tool.deactive(id, blackList);
            respond.isActiveUser = Constant.FLAG.OFF;
        } else if (isNotActiveToActiveUser(flag, user.flag)) {
            UserDAO.updateFlag(id, Constant.FLAG.ON);
            if (flag == Constant.USER_STATUS_FLAG.DEACITIVE) {
                DeactivateDAO.activate(id);
            }
            List<String> blackList = BlockUserManager.getBlackList(id);
            Tool.active(id, blackList);
            respond.isActiveUser = Constant.FLAG.ON;
        }
        boolean isVerify = true;
//        UserPointManager.add(respond);
        result = new EntityRespond(ErrorCode.SUCCESS, new UpdateUserData(isFinishRegister, isVerify, respond));
        return result;
    }

    private EntityRespond updateByAdmin(User user, JSONObject obj, Date time) throws EazyException {

        EntityRespond result = new EntityRespond();
        String id = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        ReviewingUserDAO.remove(id);
        User beforeUser = UserDAO.getUserInfor(id);
//        if(beforeUser instanceof Female){
//            Female f= (Female) beforeUser;
//        }
        Long delAbout = Util.getLongParam(obj, "del_abt");
//        if (beforeUser.finishRegisterFlag != Constant.YES || beforeUser.verificationFlag != Constant.YES) {
//            result.code = ErrorCode.UNKNOWN_ERROR;
//            return result;
//        }
        String applicationId = (String) obj.get("application");

        if (delAbout == null || delAbout < 0 || delAbout > 1) {
            result.code = ErrorCode.UNKNOWN_ERROR;
            return result;
        }
        if (!Tool.checkUserName(id, user.username)) {
            result.code = ErrorCode.INVALID_USER_NAME;
            return result;
        }
//        if (user instanceof Female) {
//            if (!Tool.checkFemaleName(id, user.username)) {
//                result.code = ErrorCode.INVALID_USER_NAME;
//                return result;
//            }
//        }
        if (!UserInforValidator.validateBirthday(user, Constant.FLAG.ON)) {
            result.code = 4;
            return result;
        }
        if (!UserInforValidator.validateName(user, beforeUser.finishRegisterFlag.intValue())) {
            result.code = 6;
            return result;
        }
        if (delAbout == 1) {
            user.about = "";
        }

        if (user.email != null) {
            int accountType = beforeUser.userType.intValue();
            if (UserDAO.checkEmail(user.email, id)) {
                result.code = ErrorCode.EMAIL_REGISTED;
                return result;
            }
            if (accountType == Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE) {
                if (!user.email.trim().equals(beforeUser.email)) {
                    // LongLT 25 Oct2016 ///////////////////////////  #5045 START
//                    if (!Util.validateEmail(user.email)) {
//                        result.code = ErrorCode.INVALID_EMAIL;
//                        return result;
//                    }
                    // update flag :updateEmailFlag
                    if ((beforeUser.updateEmailFlag != null && beforeUser.updateEmailFlag != Constant.FLAG.ON) || beforeUser.updateEmailFlag == null) {
                        user.updateEmailFlag = (long) Constant.FLAG.ON;
                    }
                }
            } else if (accountType == Constant.ACCOUNT_TYPE_VALUE.FB_TYPE) {
                if (user.fbId == null) {
                    user.email = null;
                }
            }
        }
//        if (!UserInforValidator.validateJob(user, beforeUser.finishRegisterFlag.intValue(), beforeUser)) {
//            result.code = ErrorCode.WRONG_DATA_FORMAT;
//            return result;
//        }
        if (!UserInforValidator.validate(user, beforeUser.finishRegisterFlag.intValue(), beforeUser)) {
            result.code = ErrorCode.WRONG_DATA_FORMAT;
            return result;
        }
        // validate region
        int isFinishRegister = beforeUser.finishRegisterFlag.intValue();
        if (beforeUser.finishRegisterFlag == Constant.FLAG.OFF) {
            if (beforeUser.region == null) {
                if (user.region != null) {
                    user.autoRegion = (long) 0;
                }
            }
//            if (user.region != null && user.username != null) {
//                user.finishRegisterFlag = (long) Constant.YES;
//                isFinishRegister = Constant.YES;
//            }
        }
        // validate finishFlag
        Integer flag = UserDAO.getFlag(id);
        user.password = null;
        user.originalPassword = null;
        user.point = null;
        User respond = UserDAO.updateUser(id, user);

        UserDAO.updateBlankTextField(id, user, beforeUser.gender.intValue());
        UserDAO.updateTextField(id, user, beforeUser.gender.intValue());
        //UserDAO.updateIntField(id, ParamKey.SITE_ID, user.site_id);
        User afterUser = UserDAO.getUserInfor(id);

        if (beforeUser.finishRegisterFlag == Constant.FLAG.OFF) {
            if (afterUser.isCompleteUser()) {
                isFinishRegister = Constant.FLAG.ON;
                UserDAO.updateFinishRegister(id, Constant.FLAG.ON);
            }
        }
        if (isActiveToNotActiveUser(flag, user.flag)) {
            if (user.flag == Constant.USER_STATUS_FLAG.DEACITIVE) {
                DeactivateDAO.addDeactiveUser(id, null, time.getTime());
            }
            List<String> blackList = BlockUserManager.getBlackList(id);
            Tool.deactive(id, blackList);
            respond.isActiveUser = Constant.FLAG.OFF;
        } else if (isNotActiveToActiveUser(flag, user.flag)) {
            UserDAO.updateFlag(id, Constant.FLAG.ON);
            if (flag == Constant.USER_STATUS_FLAG.DEACITIVE) {
                DeactivateDAO.activate(id);
            }
            List<String> blackList = BlockUserManager.getBlackList(id);
            Tool.active(id, blackList);
            respond.isActiveUser = Constant.FLAG.ON;
        }
//        if(isNormalToNotNormalUser(flag, beforeUser, user)){
//            List<String> blackList = BlockDAO.getBlackList(id);
//            Tool.deactive(id, blackList);
//        }else if(isNotNormalToNormalUser(flag, beforeUser, user)){
//            List<String> blackList = BlockDAO.getBlackList(id);
//            Tool.active(id, blackList);
//        }
        boolean isVerify = true;
//        if(afterUser instanceof Female){
//            Female after = (Female) afterUser;
//            if(after.verificationFlag == Constant.APPROVED){
//                isVerify = true;
//            }else {
//                isVerify = false;
//            }
//        }

//        UserPointManager.add(respond);
        result = new EntityRespond(ErrorCode.SUCCESS, new UpdateUserData(isFinishRegister, isVerify, respond));
        return result;
    }

    private boolean isActiveToNotActiveUser(Integer beforeFlag, Long afterFlag) {
        return (beforeFlag == null || beforeFlag == Constant.USER_STATUS_FLAG.ACTIVE) && (afterFlag != null && afterFlag != Constant.USER_STATUS_FLAG.ACTIVE);
    }

    private boolean isNotActiveToActiveUser(Integer beforeFlag, Long afterFlag) {
        return (beforeFlag != null && beforeFlag != Constant.USER_STATUS_FLAG.ACTIVE) && (afterFlag != null && afterFlag == Constant.USER_STATUS_FLAG.ACTIVE);
    }

    private boolean isNotNormalToNormalUser(Integer beforeFlag, User beforeUser, User afterUser) {
        if (beforeUser.gender == Constant.GENDER.MALE) {
            return isActiveToNotActiveUser(beforeFlag, afterUser.flag);
        } else {
            Female afterFemale = (Female) afterUser;
            Female beforeFemale = (Female) beforeUser;
            if (afterFemale.verificationFlag == Constant.REVIEW_STATUS_FLAG.APPROVED && isNotActiveToActiveUser(beforeFlag, afterFemale.flag)) {
                return true;
            }
            if (afterFemale.flag == Constant.USER_STATUS_FLAG.ACTIVE && (beforeFemale.verificationFlag != Constant.REVIEW_STATUS_FLAG.APPROVED && afterFemale.verificationFlag == Constant.REVIEW_STATUS_FLAG.APPROVED)) {
                return true;
            }
            return false;
        }
    }

    private boolean isNormalToNotNormalUser(Integer beforeFlag, User beforeUser, User afterUser) {
        if (beforeUser.gender == Constant.GENDER.MALE) {
            return isActiveToNotActiveUser(beforeFlag, afterUser.flag);
        } else {
            Female afterFemale = (Female) afterUser;
            Female beforeFemale = (Female) beforeUser;
            if (beforeFlag == Constant.USER_STATUS_FLAG.ACTIVE && beforeFemale.verificationFlag == Constant.REVIEW_STATUS_FLAG.APPROVED) {
                if ((afterFemale.flag != null && afterFemale.flag != Constant.USER_STATUS_FLAG.ACTIVE) || afterFemale.verificationFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    return true;
                }
            }
            return false;
        }
    }

    private String getReviewingValue(String after, String before) {
        if (after == null || after.isEmpty()) {
            return null;
        }
        if (before == null || before.isEmpty()) {
            return after;
        }
        if (after.equals(before)) {
            return null;
        } else {
            return after;
        }
    }

    private boolean isNotiStatus(User before, User after) {
        if (after.about == null || before.about == null) {
            return false;
        }
        return !after.about.equals(before.about) ? true : false;
    }

    private boolean isReviewed(User before, ReviewingUser after) {
        if (after == null || before == null) {
            return false;
        }

        if (after.about != null && !after.about.isEmpty()) {
            if (!after.about.equals(before.about)) {
                return true;
            }
        }
//        if (before.gender == Constant.GENDER.FEMALE) {
//            Female beforeFemale = (Female) before;
//            Female afterFemale = (Female) after;
//            if (afterFemale.fetishs != null && !afterFemale.fetishs.isEmpty() && !afterFemale.fetishs.equals(beforeFemale.fetishs)) {
//                return true;
//            }
//            if (afterFemale.typeOfMan != null && !afterFemale.typeOfMan.isEmpty() && !afterFemale.typeOfMan.equals(beforeFemale.typeOfMan)) {
//                return true;
//            }
//            if (afterFemale.hobby != null && !afterFemale.hobby.isEmpty() && !afterFemale.hobby.equals(beforeFemale.hobby)) {
//                return true;
//            }
//        } else {
//            Male beforeMale = (Male) before;
//            Male afterMale = (Male) after;
//            if (afterMale.hobby != null && !afterMale.hobby.isEmpty() && !afterMale.hobby.equals(beforeMale.hobby)) {
//                return true;
//            }
//        }
        return false;
    }
}
