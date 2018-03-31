/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.NotificationData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class PushFromFreePageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond respond = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USERID);
            String content = Util.getStringParam(obj, ParamKey.CONTENT);
            String url = Util.getStringParam(obj, ParamKey.URL);
            int isNoti = Constant.FLAG.OFF;
            if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.FREE_PAGE_NOTI)) {
                Notification noti = new Notification();
                noti.content = content;
                noti.url = url;
                String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.FREE_PAGE_NOTI);
                NotificationCleaner.put(notificationId, time.getTime());
                isNoti = Constant.FLAG.ON;
            } else {
                Notification noti = new Notification();
                noti.content = content;
                noti.url = url;
                String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.FREE_PAGE_NOTI);
                NotificationCleaner.put(notificationId, time.getTime());

            }

            respond = new EntityRespond(ErrorCode.SUCCESS, new NotificationData(isNoti));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
