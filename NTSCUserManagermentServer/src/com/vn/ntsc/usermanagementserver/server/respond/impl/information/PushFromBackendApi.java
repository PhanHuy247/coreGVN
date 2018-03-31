/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListStringRespond;

/**
 *
 * @author duyetpt
 */
public class PushFromBackendApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond respond = new EntityRespond();
        try {
            List<String> listFriend = Util.getListString(obj, ParamKey.FRIEND_LIST);
            String content = Util.getStringParam(obj, ParamKey.CONTENT);
            String url = Util.getStringParam(obj, ParamKey.URL);
            String pushId = Util.getStringParam(obj, "push_id");
            long type = Util.getLongParam(obj, "type");
            NotificationSettingDAO.listUserNotification(listFriend, Constant.NOTIFICATION_TYPE_VALUE.FREE_PAGE_NOTI);
            Notification noti = new Notification();
            noti.content = content;
            noti.url = url;
            noti.pushId = pushId;
            int notiType = Constant.NOTIFICATION_TYPE_VALUE.FREE_PAGE_NOTI;
            Util.addDebugLog("hungcheck" + type);
            if(type==1) {
                
                notiType = Constant.NOTIFICATION_TYPE_VALUE.AUTO_NEWS;
            }
            List<String> notificationIds = NotificationDAO.addNotifications(listFriend, noti, time.getTime(), notiType);
            for(String notificationId : notificationIds){
                NotificationCleaner.put(notificationId, time.getTime());
            } 
            respond = new ListStringRespond(ErrorCode.SUCCESS, listFriend);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
