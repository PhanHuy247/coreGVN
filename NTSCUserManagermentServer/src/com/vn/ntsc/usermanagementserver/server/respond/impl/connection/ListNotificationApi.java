/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListNotificationApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
//            List<String> blackList = new ArrayList<>();
//            blackList.addAll(BlackListManager.toList());
//            blackList.addAll(BlockDAO.getBlackList(userId));
//            Collection<String> deactiveUsers = BlackListManager.toList();
            List<String> blockUsers = BlockUserManager.getBlackList(userId);
            String timeStamp = Util.getStringParam(obj, ParamKey.TIME_STAMP);
            Long versionL = Util.getLongParam(obj, "version");
            int version = versionL == null ? 1 : versionL.intValue();
           
            Date maxDate = time;
      
           
            long maxTime = maxDate.getTime();
            Long take = Util.getLongParam(obj, ParamKey.TAKE);

            List<Notification> list = NotificationDAO.getNotificationList(userId, maxTime, take.intValue(), blockUsers, version);
            UserDAO.updateNotificationReadTime(userId, time.getTime());
            result = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
