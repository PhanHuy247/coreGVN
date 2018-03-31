/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.NotificationData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class DeniedFileApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String fileId = Util.getStringParam(obj, ParamKey.FILE_ID);            
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Integer isNoti;
            if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.DENIED_BACKSTAGE_NOTI)) {
                Notification noti = new Notification();
                noti.notiFileId = fileId;
                if (buzzId != null) {
                    noti.notiBuzzId = buzzId;
                    String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DENIED_BUZZ_NOTI);
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
