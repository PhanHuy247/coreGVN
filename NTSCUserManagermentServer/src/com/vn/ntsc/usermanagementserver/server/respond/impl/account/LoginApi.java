/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.DeactivateDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ApplicationDAO;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LoginData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;

/**
 *
 * @author RuAc0n
 */
public class LoginApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {

            User inputUser = User.getLoginUser(obj);

//            String applicationId = "1";
//            if (obj.get("application") != null && Util.getLongParam(obj, "application") != null) {
//                applicationId = Util.getLongParam(obj, "application").toString();
//            }
//            Util.addDebugLog("applicationId " + applicationId);
//
//            String uniquename = ApplicationDAO.getUniqueNameById(applicationId);
//            if (uniquename == null) {
//                return new EntityRespond(ErrorCode.INVALID_APPLICATION_NAME);
//            }
//            inputUser.applicationId = applicationId;
//            String email = Util.getStringParam(obj, ParamKey.EMAIL);
//            user = userDAO.login(user);
            User respondUser = UserDAO.login(inputUser);
            Util.addDebugLog("respondUser----------------------------------------"+respondUser.toJsonObject());
            String ip = Util.getStringParam(obj, ParamKey.IP);
//            String countryCode = Util.GeoIPGetCountry(ip);
//
//            Util.addDebugLog("RegisterApi ======================" + countryCode);
//            if (countryCode != null) {
//                obj.put("countryCode", countryCode);
//                UserDAO.updateNotNullStringParam(respondUser.userId, obj, "countryCode", ParamKey.IP);
//            }
            Long deviceType = (Long) obj.get(ParamKey.DEVICE_TYPE);
            String deviceId = (String) obj.get(ParamKey.DEVICE_ID);
            if (deviceType != null) {
                UserDAO.upadateDeviceType(respondUser.userId, deviceType.intValue());
            }
//
//            String osVersion = obj.get(ParamKey.OS_VERSION) != null ? obj.get(ParamKey.OS_VERSION).toString() : null;
//            if (osVersion != null) {
//                UserDAO.updateStringField(respondUser.userId, ParamKey.OS_VERSION, osVersion);
//            }
//
//            String appVersion = "1.1";
//            if (obj.get(ParamKey.APP_VERSION) != null) {
//                appVersion = obj.get(ParamKey.APP_VERSION).toString();
//                Util.addDebugLog("update app_version1 " + appVersion);
//                UserDAO.updateStringField(respondUser.userId, ParamKey.APP_VERSION, appVersion);
//            }
//
//            String devName = obj.get(ParamKey.DEVICE_NAME) != null ? obj.get(ParamKey.DEVICE_NAME).toString() : null;
//            if (devName != null) {
//                UserDAO.updateStringField(respondUser.userId, ParamKey.DEVICE_NAME, devName);
//            }
//
//            String device_id = obj.get(ParamKey.DEVICE_ID) != null ? obj.get(ParamKey.DEVICE_ID).toString() : null;
//            if (device_id != null) {
//                UserDAO.updateStringField(respondUser.userId, ParamKey.DEVICE_ID, device_id);
//            }

            //Linh #9880: remove this device_id field of other user
            UserDAO.unsetThisDeviceId(deviceId);
            UserDAO.updateNotNullStringParam(respondUser.userId, obj,
                    ParamKey.OS_VERSION, ParamKey.APP_VERSION, ParamKey.DEVICE_NAME, ParamKey.IDFA, ParamKey.GPS_ADID, ParamKey.ADID, ParamKey.DEVICE_ID
            );

//            if(respondUser.deviceType == null){
//                Long deviceType = (Long) obj.get(ParamKey.DEVICE_TYPE);
//                if(deviceType != null){
//                    UserDAO.upadateDeviceType(respondUser.userId, deviceType.intValue());
//                }
//            }
            //HUNGDT #6360 call kit
            String voip_notify_token = obj.get(ParamKey.VOIP_NOTI_TOKEN) != null ? obj.get(ParamKey.VOIP_NOTI_TOKEN).toString() : null;
            if (voip_notify_token != null) {
                UserDAO.updateStringField(respondUser.userId, ParamKey.VOIP_NOTI_TOKEN, voip_notify_token);
            }

            List<String> blockList = BlockUserManager.getBlackList(respondUser.userId);
            LoginData data = new LoginData();
            data.user = respondUser;
//            if (applicationId != null) {
//                UserDAO.updateStringField(respondUser.userId, "application_id", applicationId);
//            }
//            data.user.applicationName = ApplicationDAO.getDisplayNameById(applicationId);
            data.blackList = blockList;
//            Collection<String> deactiveUser = BlackListManager.toList();

            data.setting = Helper.setSetting(respondUser.userId);

            String userId = respondUser.userId;
            // Helper.setUnlockSetting(userId, respondUser, blackList);
            // get checkoutNumber 
//            Helper.getAttentionNumber(data, time.getTime());
            data.checkoutNum = Helper.getCheckOutNumber(userId);
            data.user.favouritedNumber = (long) Helper.getFavoristedNumber(userId);
            Helper.setNotificationNumber(userId, blockList, data);
            //finishRegistFlag, verificationFlag
            data.isVerify = Helper.isVerify(respondUser);
            Long readTime = UserDAO.getNotificationReadTime(userId);
            data.notiLikeNumber = new Long(NotificationDAO.getNotificationNumberByType(userId, Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI, blockList, readTime));
            data.notiNewsNumber = new Long(NotificationDAO.getNotificationNumberByType(userId, Constant.NOTIFICATION_TYPE_VALUE.AUTO_NEWS, blockList, readTime));
            data.notiQANumber = new Long(NotificationDAO.getNotificationNumberByType(userId, Constant.NOTIFICATION_TYPE_VALUE.QA_NOTI, blockList, readTime));
            String loginTime = Util.getStringParam(obj, ParamKey.LOGIN_TIME);
            Date loginDate = DateFormat.parse(loginTime);
            if (data.isVerify) {
                //add login time and check daily bonus
                Date lastLogin = UserActivityDAO.getLastOnline(userId);
                UserDAO.updateLoginTime(userId, time);
                //String ip = Util.getStringParam(obj, ParamKey.IP);
                loginDate = Helper.addLoginTime(loginDate, time, lastLogin, userId, ip);
                Helper.checkDailyBonus(userId, loginDate, time, respondUser, data, lastLogin, ip);
            }
            if (respondUser.isActiveUser != null && respondUser.isActiveUser == Constant.FLAG.ON) {
                if (respondUser.flag != null && respondUser.flag == Constant.USER_STATUS_FLAG.DEACITIVE) {
                    UserDAO.updateFlag(userId, Constant.FLAG.ON);
                    DeactivateDAO.activate(userId);
                }
                Tool.active(userId, blockList);
//                if(respondUser.gender == Constant.MALE){
//                    Tool.active(userId, blockList);
//                }else{
//                    Female female = (Female) respondUser;
//                    if(female.verificationFlag == Constant.APPROVED){
//                        Tool.active(userId, blockList);
//                    }
//                }
            }

            data.notiNum = Helper.getNotificationNumber(userId) - data.notiQANumber.intValue();

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
