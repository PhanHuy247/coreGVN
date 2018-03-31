/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.dao.impl.LogNotificationDAO;
import com.vn.ntsc.jpns.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.jpns.entity.impl.notification.NotificationSetting;
import com.vn.ntsc.jpns.server.constant.Params;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;
import com.vn.ntsc.jpns.util.InterCommunicator;
import com.vn.ntsc.jpns.util.MsgUtil;

/**
 *
 * @author Administrator
 */
public class PushNotificationApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String userId = Util.getStringParam(obj, ParamKey.USERID);
            List<String> friendList = Util.getListString(obj, ParamKey.FRIEND_LIST);
            String content = Util.getStringParam(obj, ParamKey.CONTENT);
            String url = Util.getStringParam(obj, ParamKey.URL);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            String api = (String) obj.get(ParamKey.API_NAME);
            Long type = (Long) obj.get(ParamKey.TYPE);
            if (userId != null) {
                //ThanhDD: truong hop nay ko co on/off nen bo check trang thai on/off
//                JSONObject auto_push = MsgUtil.iosPushFromFreepage(API.PUSH_NOTIFICATION_FROM_NEWS, content, url, userId, null);
//                send(auto_push, userId);
//                LogNotificationDAO.addLog(userId, Params.getNotiType(API.PUSH_NOTIFICATION_FROM_NEWS), null, Util.getGMTTime(), ip);
                JSONObject msg = null;
                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(userId);
                if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
//                    msg = MsgUtil.iosPushFromFreepage_new(api, content, url, userId, null, true);
                    msg = MsgUtil.pushNewNotification(type, content, url, userId, null, true);
                } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
//                    msg = MsgUtil.iosPushFromFreepage_new(api, content, url, userId, null, false);
                    msg = MsgUtil.pushNewNotification(type, content, url, userId, null, false);
                }
                InterCommunicator.send(msg, userId);
                LogNotificationDAO.addLog(userId, type.intValue(), null, Util.getGMTTime(), ip);
            } else {
                String pushId = Util.getStringParam(obj, "push_id");
                for (String friend : friendList) {
                    //ThanhDD: truong hop nay ko co on/off nen bo check trang thai on/off
//                    JSONObject auto_push = MsgUtil.iosPushFromFreepage(API.PUSH_NOTIFICATION_FROM_NEWS, content, url, friend, pushId);
//                    send(auto_push, friend);
//                    LogNotificationDAO.addLog(friend, Params.getNotiType(API.PUSH_NOTIFICATION_FROM_NEWS), null, Util.getGMTTime(), ip);
                    JSONObject msg = null;
                    // EDIT LongLT 
                    NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(friend);
                    if (notification.andgAlert == 0) { //OFF push noti
//                        msg = MsgUtil.iosPushFromFreepage_new(api, content, url, friend, pushId, true);
                        msg = MsgUtil.pushNewNotification(type, content, url, friend, pushId, true);
                    } else if (notification.andgAlert == 1) {//ON push noti
//                        msg = MsgUtil.iosPushFromFreepage_new(api, content, url, friend, pushId, false);
                        msg = MsgUtil.pushNewNotification(type, content, url, friend, pushId, false);
                    }
                    InterCommunicator.send(msg, friend);
                    LogNotificationDAO.addLog(friend, type.intValue(), null, Util.getGMTTime(), ip);
                }
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
}
