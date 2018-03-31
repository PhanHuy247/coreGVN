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
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.BuzzData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class AddCommentVersion2Api implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            //commentor
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            //BUZZ OF THIS USER
            String buzzOwner = Util.getStringParam(obj, ParamKey.BUZZ_OWNER_ID);
            String buzzOwnerName = UserDAO.getUserName(buzzOwner);
            Long activityTime = Util.getLongParam(obj, ParamKey.TIME);
            int isApp = Util.getLongParam(obj, "is_app").intValue();
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);

            // add notification for buzzOwner
            List<String> blackList = BlockUserManager.getBlackList(userId);
            blackList.add(userId);
            int isNoti = Constant.FLAG.OFF;
            if(isApp == Constant.FLAG.ON){
                if(!userId.equals(buzzOwner))
                    ActionManager.doAction(ActionType.comment_buzz, userId, buzzOwner, time, null, null, ip);
                
                UserActivityDAO.updateLastActivity(buzzOwner, activityTime);
                if (!blackList.contains(buzzOwner) && !DeactivateUserManager.isDeactivateUser(buzzOwner)) {
                    String userName = UserDAO.getUserName(userId);
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    ownerNoti.fromNotiUserId = userId;
                    ownerNoti.notiUserName = userName;
                    String notificationId = NotificationDAO.addNotification(buzzOwner, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.COMMENT_MY_BUZZ_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                }
                if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_COMMENT_NOTI)) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    String notificationId =  NotificationDAO.addNotification(userId, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_COMMENT_NOTI);
                    NotificationCleaner.put(notificationId, time.getTime());
                    isNoti = Constant.FLAG.ON;
                }
            }
            else {
                //Linh edit
                ActionManager.doDecreasePointVer2(userId, buzzOwner, ActionType.comment_buzz, time, ip);
            }
            //decrease point of commentor --DuyetPT
//            if(!userId.equals(buzzOwner))
//                ActionManager.doAction(ActionType.comment_buzz, userId, buzzOwner, time, null, null, ip);
            //----------
            
            BuzzData data = new BuzzData(buzzOwnerName, isNoti, (long)UserInforManager.getPoint(userId));
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
