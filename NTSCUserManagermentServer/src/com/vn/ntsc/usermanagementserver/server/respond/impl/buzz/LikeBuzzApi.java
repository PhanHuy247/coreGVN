/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
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
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class LikeBuzzApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String userName = UserDAO.getUserName(userId);
            String buzzOwner = Util.getStringParam(obj, ParamKey.BUZZ_OWNER_ID);
            String buzzOwnerName = UserDAO.getUserName(buzzOwner);
            Long activityTime = Util.getLongParam(obj, ParamKey.TIME);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            UserActivityDAO.updateLastActivity(buzzOwner, activityTime);
            Long isNoti = Util.getLongParam(obj, ParamKey.IS_NOTI);
            //ThanhDD edit #3766
            if (1 == 1) {
                List<String> blackList = BlockUserManager.getBlackList(userId);
                blackList.add(userId);
//                Collection<String> deactiveUsers = BlackListManager.toList();
//                if (!NotificationSettingDAO.checkUserNotification(buzzOwner, Constant.LIKE_MY_BUZZ_NOTI)) {
//                    blackList.add(buzzOwner);
//                }
                // add notification for buzzOwner
                if (!blackList.contains(buzzOwner) && !DeactivateUserManager.isDeactivateUser(buzzOwner)) {
                    String notificationId = NotificationDAO.getLikeNotification(userId, buzzOwner, buzzId, Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI);
                    Util.addDebugLog("++++++++++++++++++++++++++++++++++++++ "+notificationId);
                    if (notificationId == null) {
                        Notification ownerNoti = new Notification();
                        ownerNoti.notiBuzzId = buzzId;
                        ownerNoti.fromNotiUserId = userId;
                        ownerNoti.notiUserName = userName;
                        notificationId = NotificationDAO.addNotification(buzzOwner, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI);
                        NotificationCleaner.put(notificationId, time.getTime());
                    } else {
                        NotificationDAO.remakeLikeNotification(notificationId, time.getTime());
                    }
                }

                BuzzData data = new BuzzData(buzzOwnerName, isNoti.intValue(), null);
                result = new EntityRespond(ErrorCode.SUCCESS, data);
            } else {
                BuzzData data = new BuzzData(null, isNoti.intValue(), null);
                result = new EntityRespond(ErrorCode.SUCCESS, data);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
