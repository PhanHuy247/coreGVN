/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.NotificationData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;

/**
 *
 * @author RuAc0n
 */
public class ReviewBuzzApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            //BUZZ OF THIS USER
            String buzzOwner = Util.getStringParam(obj, ParamKey.BUZZ_OWNER_ID);
            String buzzOwnerName = UserDAO.getUserName(buzzOwner);
//            Long activityTime = Util.getLongParam(obj, ParamKey.TIME);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
//            String ip = Util.getStringParam(obj, ParamKey.IP);
            Long type = Util.getLongParam(obj, "type");
            int isNoti = Constant.FLAG.OFF;
            List<String> listFavourited = new ArrayList<>();
            if(type == Constant.REVIEW_STATUS_FLAG.APPROVED){
                if (NotificationSettingDAO.checkUserNotification(buzzOwner, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_TEXT_BUZZ_NOTI)) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    String notificationId = NotificationDAO.addNotification(buzzOwner, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_TEXT_BUZZ_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                    isNoti = Constant.FLAG.ON;
                }
                listFavourited = FavoritedDAO.getFavoristIdList(buzzOwner);
                Tool.removeBlackList(listFavourited, buzzOwner);
                // ADD NOTIFY TO FAVORISTED LIST
                NotificationSettingDAO.listUserNotification(listFavourited, Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI);
                Util.addDebugLog("=====listFavourite:" +listFavourited);
                Notification noti = new Notification();
                noti.fromNotiUserId = buzzOwner;
                noti.notiType = Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI;
                noti.notiBuzzId = buzzId;
                noti.notiUserName = buzzOwnerName;
                List<String> notificationIds = NotificationDAO.addNotifications(listFavourited, noti, time.getTime(), noti.notiType);
                for(String notificationId : notificationIds){
                    NotificationCleaner.put(notificationId, time.getTime());
                }  
            }else{
                if (NotificationSettingDAO.checkUserNotification(buzzOwner, Constant.NOTIFICATION_TYPE_VALUE.DENIED_TEXT_BUZZ_NOTI)) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    String notificationId = NotificationDAO.addNotification(buzzOwner, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DENIED_TEXT_BUZZ_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                    isNoti = Constant.FLAG.ON;
                }
            }
            NotificationData data = new NotificationData(isNoti, listFavourited, buzzOwnerName);
            result = new EntityRespond(ErrorCode.SUCCESS, data);
            Util.addDebugLog("=================result:" +result.toString());
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }

}
