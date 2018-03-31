/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.ReviewCommentData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ReviewCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            //commentor
            String commentor = Util.getStringParam(obj, "commentor");
            String userName = UserDAO.getUserName(commentor);
            //BUZZ OF THIS USER
            String buzzOwner = Util.getStringParam(obj, ParamKey.BUZZ_OWNER_ID);
            String buzzOwnerName = UserDAO.getUserName(buzzOwner);
//            Long activityTime = Util.getLongParam(obj, ParamKey.TIME);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
//            String ip = Util.getStringParam(obj, ParamKey.IP);
            UserActivityDAO.updateLastActivity(buzzOwner, Util.currentTime());
            // add notification for buzzOwner
            List<String> blackList = BlockUserManager.getBlackList(commentor);
            blackList.add(commentor);
            Long type = Util.getLongParam(obj, "type");
            
            int isNotiReviewComment = Constant.FLAG.OFF;
            int isNotiCommentBuzz = Constant.FLAG.OFF;
            String notificationId = null;
            if(type == Constant.REVIEW_STATUS_FLAG.APPROVED){
                ActionManager.doIncreasePointVer2(commentor, buzzOwner, ActionType.comment_buzz, ActionType.comment_bonus, time, null);
                if (NotificationSettingDAO.checkUserNotification(commentor, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_COMMENT_NOTI)) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    notificationId = NotificationDAO.addNotification(commentor, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_COMMENT_NOTI);
                    isNotiReviewComment = Constant.FLAG.ON;
                }
                if (!blackList.contains(buzzOwner) && !DeactivateUserManager.isDeactivateUser(buzzOwner)) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    ownerNoti.fromNotiUserId = commentor;
                    ownerNoti.notiUserName = userName;
                    notificationId = NotificationDAO.addNotification(buzzOwner, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.COMMENT_MY_BUZZ_NOTI);
                    isNotiCommentBuzz = Constant.FLAG.ON;
                }
            }else{
                ActionManager.doRefund(commentor, buzzOwner, ActionType.refund_comment, time, null);
                if (NotificationSettingDAO.checkUserNotification(commentor, Constant.NOTIFICATION_TYPE_VALUE.DENIED_COMMENT_NOTI)) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    notificationId = NotificationDAO.addNotification(commentor, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DENIED_COMMENT_NOTI);
                    isNotiReviewComment = Constant.FLAG.ON;
                }
            }
            if(notificationId != null){
                NotificationCleaner.put(notificationId, time.getTime());
            }
            ReviewCommentData data = new ReviewCommentData(buzzOwnerName, isNotiCommentBuzz, isNotiReviewComment);
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
