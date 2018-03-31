/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionResult;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.emailpool.EmailInfor;
import com.vn.ntsc.usermanagementserver.server.emailpool.EmailSender;
import com.vn.ntsc.usermanagementserver.server.emailpool.QueueEmailManager;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.NotificationUserData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;

/**
 *
 * @author RuAc0n
 */
public class NotificationLoginApi implements IApiAdapter {

    private static final int ALERT_ALL = 0;
    private static final int ALERT_NOTIFICATION = 1;
    private static final int ALERT_EMAIL = 2;
    
    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            String username = UserDAO.getUserName(userId);
            
            Map<String, Integer> onlineAlertMap = OnlineAlertDAO.getListAlertNotification(userId, time);
            List<String> onlineEmailAlertList = new ArrayList<>();
            List<String> onlineAlertList = new ArrayList<>();
            for (Map.Entry pairs : onlineAlertMap.entrySet()) {
                String alertId = (String) pairs.getKey();
                onlineAlertList.add(alertId);
            }
            Util.addDebugLog("onlineAlertList======================================"+onlineAlertList.size());
            List<String> listUserNoNoti = NotificationSettingDAO.listUserNotNotifcation(onlineAlertList, Constant.NOTIFICATION_TYPE_VALUE.ONLINE_ALERT_NOT);
             Util.addDebugLog("listUserNoNoti======================================"+listUserNoNoti.size());
//            List<String> blackList = Tool.getBlackList(userId);
//            blackList.addAll(listUserNoNoti);
            Tool.removeBlackList(onlineAlertList, userId);
            onlineAlertList.removeAll(listUserNoNoti);
            List<String> removeList = new ArrayList<>();
            for (String id : onlineAlertList) {
                ActionResult actionResult = ActionManager.doAction(ActionType.online_alert, id, userId, time, null, null, ip);
                if (actionResult.code != ErrorCode.SUCCESS) {
                    //remove check point noti online
                    //removeList.add(id);
                } else {
                    //add notification
                    Integer alertType = onlineAlertMap.get(id);
                    if (alertType == ALERT_ALL || alertType == ALERT_NOTIFICATION) {
                        Notification noti = new Notification();
                        noti.fromNotiUserId = userId;
                        noti.notiUserName = username;
                        String notificationId = NotificationDAO.addNotification(id, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.ONLINE_ALERT_NOT);
                        NotificationCleaner.put(notificationId, time.getTime());
                    }

                    if (alertType == ALERT_ALL || alertType == ALERT_EMAIL) {
                        onlineEmailAlertList.add(id);
                        if (alertType == ALERT_EMAIL) {
                            removeList.add(id);
                        }
                    }
                    Util.addDebugLog("alertType======================================"+alertType);
                    OnlineAlertDAO.updateAlert(userId, id, time);
                }
            }
            for (String id : removeList) {
                onlineAlertList.remove(id);
            }

            for (String id : onlineEmailAlertList) {
                String email = UserDAO.getEmail(id);
                EmailInfor emailInfor = new EmailInfor(email, null, Constant.EMAIL_TYPE_VALUE.ONLINE_ALERT_EMAIL, username);
                QueueEmailManager.addEmail(emailInfor);
            }
            Util.addDebugLog("onlineAlertList======================================"+onlineAlertList.size());
            EmailSender.startSendingEmail();
            result.code = ErrorCode.SUCCESS;
            result.data = new NotificationUserData(username, onlineAlertList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
