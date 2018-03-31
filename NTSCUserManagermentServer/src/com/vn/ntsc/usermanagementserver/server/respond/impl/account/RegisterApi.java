/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.dao.impl.LoginTrackingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.InvitationCodeDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ApplicationDAO;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.Config;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LoginData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.common.UserInforValidator;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class RegisterApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            User inputUser = User.getRegisterUser(obj);

//            if (!UserInforValidator.validateGender(inputUser)) {
//                return new EntityRespond(ErrorCode.WRONG_DATA_FORMAT);
//            }
//            if (!UserInforValidator.validateBirthday(inputUser, Constant.FLAG.ON)) {
//                return new EntityRespond(ErrorCode.INVALID_BIRTHDAY);
//            }

//            String applicationId = "1";
//            if (obj.get("application") != null && Util.getLongParam(obj, "application") != null) {
//                applicationId = Util.getLongParam(obj, "application").toString();
//            }
//            Util.addDebugLog("applicationId " + applicationId);

            inputUser.lastUpdate = DateFormat.format(time);
            inputUser.registerDate = DateFormat.format(time);
//            inputUser.point = new Long(regPoint);

            Long deviceType = (Long) obj.get(ParamKey.DEVICE_TYPE);
            String deviceId = (String) obj.get(ParamKey.DEVICE_ID);
            String emailRegister = (String) obj.get(ParamKey.EMAIL);
            if (deviceType != null) {
                inputUser.deviceType = deviceType;

            }

//            String uniquename = ApplicationDAO.getUniqueNameById(applicationId);
//            Util.addDebugLog("uniquename " + uniquename);
//            if (uniquename == null) {
//                return new EntityRespond(ErrorCode.INVALID_APPLICATION_NAME);
//            }
//            inputUser.applicationId = applicationId;
//            String osVersion = obj.get(ParamKey.OS_VERSION) != null ? obj.get(ParamKey.OS_VERSION).toString() : null;
//            if (osVersion != null) {
//                Util.addDebugLog("osVersion " + osVersion);
//                inputUser.osVersion = osVersion;
//            }
//
//            String appVersion = "1.1";
//            if (obj.get(ParamKey.APP_VERSION) != null) {
//                appVersion = obj.get(ParamKey.APP_VERSION).toString();
//                Util.addDebugLog("update app_version1 " + appVersion); 
//            } 
//
//            inputUser.appVersion = appVersion;
//            String devName = obj.get(ParamKey.DEVICE_NAME) != null ? obj.get(ParamKey.DEVICE_NAME).toString() : null;
//            if (devName != null) {
//                inputUser.deviceName = devName;
//            }
//            String device_id = obj.get(ParamKey.DEVICE_ID) != null ? obj.get(ParamKey.DEVICE_ID).toString() : null;
//            if (device_id != null) {
//                inputUser.device_id = device_id;
//            }
//            String ip = Util.getStringParam(obj, ParamKey.IP);
//            String countryCode = Util.GeoIPGetCountry(ip);
//            inputUser.countryCode = countryCode;
//            Util.addDebugLog("RegisterApi ======================" + countryCode);
            
            //HUNGDT #6360 call kit
            String voip_notify_token = obj.get(ParamKey.VOIP_NOTI_TOKEN) != null ? obj.get(ParamKey.VOIP_NOTI_TOKEN).toString() : null;
            Util.addDebugLog("voip_notify_token " + voip_notify_token);
            if (voip_notify_token != null) {
                Util.addDebugLog("voip_notify_token1 " + voip_notify_token);
                inputUser.voip_notify_token = voip_notify_token;
            }

            User respondData = UserDAO.insertUser(inputUser);
            //Linh #9880: remove this device_id field of other user
            UserDAO.unsetThisDeviceId(deviceId);
            UserDAO.updateNotNullStringParam(respondData.userId, obj,
                    ParamKey.OS_VERSION, ParamKey.APP_VERSION, ParamKey.DEVICE_NAME, ParamKey.IDFA, ParamKey.GPS_ADID, ParamKey.ADID, ParamKey.DEVICE_ID
            );
//            ApplicationDAO.insertUser(applicationId);
            LoginData data = new LoginData();
            data.user = respondData;
//            data.user.applicationName = ApplicationDAO.getDisplayNameById(applicationId);
            //add user point and user infor
            UserInforManager.add(respondData.userId, respondData.gender.intValue());
//            UserInforManager.add(respondData);

            ActionManager.doAction(ActionType.register, respondData.userId, null, Util.getGMTTime(), null, null, inputUser.ip);
            data.isVerify = Helper.isVerify(respondData);
            String loginTime = Util.getStringParam(obj, ParamKey.LOGIN_TIME);
            Date loginDate = time;
            if (loginTime != null) {
                loginDate = DateFormat.parse(loginTime);
            }
            if (data.isVerify) {
                Helper.addLoginTime(loginDate, time, null, respondData.userId, inputUser.ip);
                UserDAO.updateLoginTime(respondData.userId, time);
                int curPoint = UserInforManager.getPoint(respondData.userId);
                ActionManager.doAction(ActionType.daily_bonus, respondData.userId, null, Util.getGMTTime(), null, null, inputUser.ip);
                int bonusPoint = UserInforManager.getPoint(respondData.userId) - curPoint;
                if (bonusPoint > 0) {
                    data.addPoint = bonusPoint;
                    Notification noti = new Notification();
                    noti.point = bonusPoint;
                    String notificationId = NotificationDAO.addNotification(respondData.userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DAILY_BONUS_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                }
            }
            LoginTrackingDAO.initialize(respondData.userId, respondData.gender.intValue());
            respondData.point = new Long(UserInforManager.getPoint(respondData.userId));
            data.notiNum = 0;
            // List<String> backList = blockDAO.getBlackList(respondData.getUserId());
            data.blackList = new ArrayList<>();

            // cretate invitation code
            String invitationCode = InvitationCodeDAO.addInvitationCode(respondData.userId);

            int applicationType = Constant.APPLICATION_TYPE.IOS_ENTERPRISE_APPLICATION;
            Long applicationTypeL = (Long) obj.get("applicaton_type");
            if (applicationTypeL != null) {
                applicationType = applicationTypeL.intValue();
            }
            String homeUrl = Config.HOME_PAGE_URL + invitationCode + "&application_type=" + applicationType;
            data.homePageUrl = homeUrl;

            data.setting = Helper.setSetting(respondData.userId);
            Helper.getAttentionNumber(data);

            result = new EntityRespond(ErrorCode.SUCCESS, data);

            // Add LongLT 8/2016
            //addDeviceInfoToJPNS(obj);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
