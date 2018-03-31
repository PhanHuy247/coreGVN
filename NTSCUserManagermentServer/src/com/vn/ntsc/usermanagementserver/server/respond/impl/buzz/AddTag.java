/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.mongodb.ErrorCategory;
import com.vn.ntsc.usermanagementserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.respond.result.JSONRespond;
import com.vn.ntsc.usermanagementserver.server.respond.result.StringRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class AddTag implements IApiAdapter{
    JSONRespond result = new JSONRespond();
    @Override
    public Respond execute(JSONObject obj, Date time) {
        try {
            String buzzOwner = Util.getStringParam(obj, ParamKey.USER_ID);
            String userName = UserDAO.getUserName(buzzOwner);

            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);

            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Integer buzzType = BuzzDetailDAO.getType(buzzId);
            String streamId = BuzzDetailDAO.getStreamId(buzzId);
            List<String> blackList = BlockUserManager.getBlackList(friendId);
            blackList.add(friendId);

            if (!blackList.contains(buzzOwner) && !DeactivateUserManager.isDeactivateUser(buzzOwner)) {
                Integer notificationType = Constant.NOTIFICATION_TYPE_VALUE.TAG_FROM_BUZZ_NOTI;
                if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                    notificationType = Constant.NOTIFICATION_TYPE_VALUE.NOTI_TAG_LIVESTREAM_FROM_FAVOURIST;
                }
                String notificationId = NotificationDAO.getLikeNotification(friendId, buzzOwner, buzzId, notificationType);
                if (notificationId == null) {
                    Notification ownerNoti = new Notification();
                    ownerNoti.notiBuzzId = buzzId;
                    ownerNoti.fromNotiUserId = buzzOwner;
                    ownerNoti.notiUserName = userName;
                    notificationId = NotificationDAO.addNotification(friendId, ownerNoti, time.getTime(), notificationType);
                    NotificationCleaner.put(notificationId, time.getTime());
                } else {
                    NotificationDAO.remakeLikeNotification(notificationId, time.getTime());
                }
            }
            JSONObject jsonBuzz = new JSONObject();
            jsonBuzz.put("buzz_type", buzzType);
            jsonBuzz.put("stream_id", streamId);
            result.jsonData = jsonBuzz;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        }
        return result;
    }
    
}
