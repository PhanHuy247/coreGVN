/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.NotificationData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class DeniedImageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);            
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Integer isNoti;
            if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.DENIED_BACKSTAGE_NOTI)) {
                Notification noti = new Notification();
                noti.notiImageId = imageId;
                if (buzzId != null) {
                    noti.notiBuzzId = buzzId;
                    String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DENIED_BUZZ_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                } else {
                    String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DENIED_BACKSTAGE_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                }
                isNoti = Constant.FLAG.ON;
            } else {
                isNoti = Constant.FLAG.OFF;
            }
            result = new EntityRespond(ErrorCode.SUCCESS, new NotificationData(isNoti));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
