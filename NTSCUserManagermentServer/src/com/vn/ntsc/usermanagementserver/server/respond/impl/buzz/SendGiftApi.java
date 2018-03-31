/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserGiftDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.GiftUserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
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
public class SendGiftApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String sender = Util.getStringParam(obj, ParamKey.SENDER);
            String giftId = Util.getStringParam(obj, ParamKey.GIFT_ID);
            //String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            GiftUserDAO.addGift(userId, sender, giftId);
            UserDAO.addGift(userId);
            Long activityTime = Util.getLongParam(obj, ParamKey.TIME);
            UserActivityDAO.updateLastActivity(userId, activityTime);
            UserGiftDAO.addGift(userId, giftId);
            //UserBuzzDAO.add(buzzId, userId);
            //String buzzOwner = UserDAO.getUserName(userId);
            //List<String> listFavourited = FavoritedDAO.getFavoristIdList(userId);
            //Tool.removeBlackList(listFavourited, userId);
            // ADD NOTIFY TO FAVORISTED LIST
            //NotificationSettingDAO.listUserNotification(listFavourited, Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI);
            //Notification noti = new Notification();
            //noti.fromNotiUserId = userId;
            //noti.notiType = Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI;
            //noti.notiBuzzId = buzzId;
            //noti.notiUserName = buzzOwner;
            //List<String> notificationIds = NotificationDAO.addNotifications(listFavourited, noti, time.getTime(), noti.notiType);
//            for(String notificationId : notificationIds){
//                NotificationCleaner.put(notificationId, time.getTime());
//            } 
            //result = new EntityRespond(ErrorCode.SUCCESS, new NotificationData(null, listFavourited, buzzOwner));
            result = new EntityRespond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
