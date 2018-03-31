/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.Config;
import com.vn.ntsc.usermanagementserver.dao.impl.InvitationCodeDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.LoginTrackingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LoginData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class RegisterVersion2Api implements IApiAdapter{
    
    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            User inputUser = User.getRegisterUser(obj);

            inputUser.lastUpdate = DateFormat.format(time);
            inputUser.registerDate = DateFormat.format(time);
            inputUser.finishRegisterFlag = 1L;

            Long deviceType = (Long) obj.get(ParamKey.DEVICE_TYPE);
            String deviceId = (String) obj.get(ParamKey.DEVICE_ID);
            if (deviceType != null) {
                inputUser.deviceType = deviceType;

            }
            
            User respondData = UserDAO.insertUser(inputUser);
            //Linh #9880: remove this device_id field of other user
            UserDAO.unsetThisDeviceId(deviceId);
            UserDAO.updateNotNullStringParam(respondData.userId, obj,
                    ParamKey.OS_VERSION, ParamKey.APP_VERSION, ParamKey.DEVICE_NAME, ParamKey.IDFA, ParamKey.GPS_ADID, ParamKey.ADID, ParamKey.DEVICE_ID
            );
            LoginData data = new LoginData();
            data.user = respondData;
            UserInforManager.add(respondData.userId, respondData.gender.intValue());

            ActionManager.doAction(ActionType.register, respondData.userId, null, Util.getGMTTime(), null, null, inputUser.ip);
//            data.isVerify = Helper.isVerify(respondData);
            String loginTime = Util.getStringParam(obj, ParamKey.LOGIN_TIME);
            Date loginDate = time;
            if (loginTime != null) {
                loginDate = DateFormat.parse(loginTime);
            }
//            if (data.isVerify) {
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
//            }
            LoginTrackingDAO.initialize(respondData.userId, respondData.gender.intValue());
            respondData.point = new Long(UserInforManager.getPoint(respondData.userId));
            data.notiNum = 0;
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

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
}
