/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.NotificationData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;

/**
 *
 * @author RuAc0n
 */
public class ApproveImageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);            
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String buzzOwner = null;
            Integer isNoti;
            List<String> listFavourited = new ArrayList<>();
            if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_BACKSTAGE_NOTI)) {
                Notification noti = new Notification();
                noti.notiImageId = imageId;
                if (buzzId != null) {
                    noti.notiBuzzId = buzzId;
                    String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_BUZZ_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                } else {
                    String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_BACKSTAGE_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                }
                isNoti = Constant.FLAG.ON;
            } else {
                isNoti = Constant.FLAG.OFF;
            }
            if(buzzId != null){
                buzzOwner = UserDAO.getUserName(userId);
                listFavourited = FavoritedDAO.getFavoristIdList(userId);
                Tool.removeBlackList(listFavourited, userId);
                // ADD NOTIFY TO FAVORISTED LIST
                NotificationSettingDAO.listUserNotification(listFavourited, Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI);
                Notification noti = new Notification();
                noti.fromNotiUserId = userId;
                noti.notiType = Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI;
                noti.notiBuzzId = buzzId;
                noti.notiUserName = buzzOwner;
                List<String> notificationIds = NotificationDAO.addNotifications(listFavourited, noti, time.getTime(), noti.notiType);
                for(String notificationId : notificationIds){
                    NotificationCleaner.put(notificationId, time.getTime());
                }    
            }
            result = new EntityRespond(ErrorCode.SUCCESS, new NotificationData(isNoti, listFavourited, buzzOwner));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
