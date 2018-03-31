/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.ReviewSubCommentData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;

/**
 *
 * @author RuAc0n
 */
public class ReviewSubCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            
            //sub commentor
            String userId = Util.getStringParam(obj, "sub_commentor");
            String userName = UserDAO.getUserName(userId);
            
            //BUZZ OF THIS USER
            String buzzOwner = Util.getStringParam(obj, ParamKey.BUZZ_OWNER_ID);
            String buzzOwnerName = UserDAO.getUserName(buzzOwner);
            String commentOwner = Util.getStringParam(obj, "comment_owner_id");
            String commentOwnerName = UserDAO.getUserName(commentOwner);
            String commentId = Util.getStringParam(obj, ParamKey.COMMENT_ID);
            
            
//            Long activityTime = Util.getLongParam(obj, ParamKey.TIME);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            UserActivityDAO.updateLastActivity(buzzOwner, Util.currentTime());
            
            Long type = Util.getLongParam(obj, "type");
            int isNotiReviewSubComment = Constant.FLAG.OFF;
            List<String> notificationList =  (List<String>) obj.get("notification_list");
            
            if(type == Constant.FLAG.ON){
                ActionManager.doIncreasePointVer2(userId, commentOwner, ActionType.reply_comment, ActionType.reply_comment_bonus, time, null);
                if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_SUB_COMMENT_NOTI)) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    String notificationId = NotificationDAO.addNotification(userId, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_SUB_COMMENT_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                    isNotiReviewSubComment = Constant.FLAG.ON;
                }
                
                notificationList = notificationList == null ? new ArrayList<String>() : notificationList;
                notificationList.remove(userId);

                Tool.removeBlackList(notificationList, userId);
                // ADD NOTIFY TO NOTIFICATION LIST
                NotificationSettingDAO.listUserNotification(notificationList, Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT);
                Notification noti = new Notification();
                noti.fromNotiUserId = userId;
                noti.notiType = Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT;
                noti.notiBuzzId = buzzId;
                noti.notiCommentId = buzzId;
                noti.notiUserName = userName;
                noti.notiCommentId = commentId;
                noti.notiCommentOwnerId = commentOwner;
                noti.notiCommentOwnerName = commentOwnerName;
                noti.notiBuzzOwnerName = buzzOwnerName;
                List<String> notificationIds = NotificationDAO.addNotifications(notificationList, noti, time.getTime(), noti.notiType);
                for(String notificationId : notificationIds){
                    NotificationCleaner.put(notificationId, time.getTime());
                } 
            }else{
                ActionManager.doRefund(userId, commentOwner, ActionType.refund_reply_comment, time, null);
                if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.DENIED_SUB_COMMENT_NOTI)) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    String notificationId = NotificationDAO.addNotification(userId, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DENIED_SUB_COMMENT_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                    isNotiReviewSubComment = Constant.FLAG.ON;
                }
            }
            ReviewSubCommentData data = new ReviewSubCommentData(buzzOwnerName, commentOwnerName, notificationList, isNotiReviewSubComment);
            result.code = ErrorCode.SUCCESS;
            result.data = data;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }

}
