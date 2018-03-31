/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
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
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AddSubCommentData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class AddSubCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            //sub commentor
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);

            
            //BUZZ OF THIS USER
            String buzzOwner = Util.getStringParam(obj, ParamKey.BUZZ_OWNER_ID);
            String buzzOwnerName = UserDAO.getUserName(buzzOwner);
            String commentOwner = Util.getStringParam(obj, "comment_owner_id");
            String commentOwnerName = UserDAO.getUserName(commentOwner);
            String commentId = Util.getStringParam(obj, ParamKey.COMMENT_ID);
            
            int isApp = Util.getLongParam(obj, "is_app").intValue();
            Long activityTime = Util.getLongParam(obj, ParamKey.TIME);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);

            
            List<String> notificationList =  (List<String>) obj.get("notification_list");
            notificationList = notificationList == null ? new ArrayList<String>() : notificationList;
            int isNoti = Constant.FLAG.OFF;
            if(isApp == Constant.FLAG.ON){
                
                
                UserActivityDAO.updateLastActivity(buzzOwner, activityTime);
                
                String userName = UserDAO.getUserName(userId);
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
                if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_SUB_COMMENT_NOTI)) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    String notificationId = NotificationDAO.addNotification(userId, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_SUB_COMMENT_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                    isNoti = Constant.FLAG.ON;
                } 
                
                if (!userId.equals(buzzOwner)){
                    if (!userId.equals(commentOwner)){
                        ActionManager.doAction(ActionType.reply_comment, userId, commentOwner, time, null, null, ip);
                    } else {
                        ActionManager.doDecreasePointReplyComment(userId, ActionType.reply_comment, commentOwner, ActionType.reply_comment_bonus, time, ip);
                    }
                }
            }
            else {
                if (!userId.equals(buzzOwner)){
                    if (!userId.equals(commentOwner)){
                        ActionManager.doDecreasePointVer2(userId, commentOwner, ActionType.reply_comment, time, ip);
                    }
                }
            }
            //decrease point of commentor --DuyetPT
            //if(!userId.equals(buzzOwner) && !userId.equals(commentOwner))
//            if (!userId.equals(buzzOwner)){
//                if (!userId.equals(commentOwner)){
//                    ActionManager.doDecreasePointVer2(userId, commentOwner, ActionType.reply_comment, time, ip);
//                }
//            }
            //----------
            AddSubCommentData data = new AddSubCommentData(buzzOwnerName, commentOwnerName, notificationList, (long)UserInforManager.getPoint(userId), isNoti);
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
