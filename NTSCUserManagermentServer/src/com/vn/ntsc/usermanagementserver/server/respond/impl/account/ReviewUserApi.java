/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.ReviewUserData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ReviewUserApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, "user_id");
            Long reviewResult = Util.getLongParam(obj, "review_result");
            int isNoti = Constant.FLAG.OFF;
            String notificationId = null;
            if(reviewResult == -1){ // denied all
                if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.DENIED_USER_INFO_NOTI)) {
                    Notification ownerNoti = new Notification();
                    notificationId = NotificationDAO.addNotification(userId, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DENIED_USER_INFO_NOTI);
                    isNoti = Constant.FLAG.ON;
                }
            }else if(reviewResult == 0){ // some infor was denied
                if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.DENIED_A_PART_OF_USER_INFO_NOTI)) {
                    Notification ownerNoti = new Notification();
                    notificationId = NotificationDAO.addNotification(userId, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DENIED_A_PART_OF_USER_INFO_NOTI);
                    isNoti = Constant.FLAG.ON;
                }
            }else{ // approve
                if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_USER_INFOR_NOTI)) {
                    Notification ownerNoti = new Notification();
                    notificationId = NotificationDAO.addNotification(userId, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_USER_INFOR_NOTI);
                    isNoti = Constant.FLAG.ON;
                }
            }
            if(notificationId != null){
                NotificationCleaner.put(notificationId, time.getTime());
            }
            result = new EntityRespond(ErrorCode.SUCCESS, new ReviewUserData(isNoti));
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }

}
