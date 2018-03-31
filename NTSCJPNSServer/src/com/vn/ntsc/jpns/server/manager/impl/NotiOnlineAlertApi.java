/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import com.vn.ntsc.jpns.Config;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.Core;
import com.vn.ntsc.jpns.dao.impl.LogNotificationDAO;
import com.vn.ntsc.jpns.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.jpns.dao.impl.ThumbnailDAO;
import com.vn.ntsc.jpns.dao.impl.UserDAO;
import com.vn.ntsc.jpns.entity.impl.notification.NotificationSetting;
import com.vn.ntsc.jpns.server.Helper;
import com.vn.ntsc.jpns.server.constant.Params;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;
import com.vn.ntsc.jpns.server.workers.BuzzNotificationContainer;
import com.vn.ntsc.jpns.server.workers.BuzzNotificationPackage;
import com.vn.ntsc.jpns.util.InterCommunicator;
import com.vn.ntsc.jpns.util.MsgUtil;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.FilesAndFolders;
import eazycommon.exception.EazyException;
import java.util.List;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Administrator
 */
public class NotiOnlineAlertApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            String fromUsername = Core.dao.getUsername(fromUserid);
            JSONArray toUserList = (JSONArray) obj.get(ParamKey.TO_LIST_USER_ID);
            String ip = (String) obj.get(ParamKey.IP);
            
            String avatarId = UserDAO.getAvatarId(fromUserid);
            String urlAvatar = null;
            if(avatarId != null && !avatarId.isEmpty()){
                String url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + ThumbnailDAO.getImageUrl(avatarId);
                if(!url.contains("null")){
                    urlAvatar = url;
                }
            }
            Integer gender = Helper.getGender(fromUserid);
            
            String result = sendNotiWebSocket(toUserList, fromUserid, fromUsername, urlAvatar, gender, api);
            
             if(result == null){
                sendNotiFCM(toUserList, fromUserid,fromUsername, urlAvatar,gender, api, ip);
            }else{
                JSONParser parse = new JSONParser();
                JSONObject jsonResult = (JSONObject)parse.parse(result);
                //listDevice get from Service Chat
                JSONArray listDeviceId = (JSONArray)jsonResult.get("list_device_id");
                JSONArray listUserId = (JSONArray)jsonResult.get("list_user_id");
                
                if(listUserId != null && !listUserId.isEmpty()){
                    sendNotiFCM(listUserId, fromUserid, fromUsername, urlAvatar, gender, api, ip);
                }
                    //listDevice get from Database
                for (Object deviceId : listDeviceId) {
                    JSONObject jsonDeviceId = (JSONObject) deviceId;
                    String userId = (String) jsonDeviceId.get("user_id");
                    JSONArray listDevice = (JSONArray) jsonDeviceId.get("lst_device_id");
                    List<String> devices = Core.dao.getListDeviceId(userId);
                    for(String device : devices){
                        if(listDevice != null && !listDevice.contains(device)){
                            sendNotiFCMbyDeviceId(userId, device, fromUserid,  fromUsername, urlAvatar, gender, api, ip);
                        }
                    }
                }
            }
            
            if (fromUserid != null && fromUsername != null && toUserList != null) {
                JSONObject msg = null;
                for (Object toUserList1 : toUserList) {
                    String toUserId = (String) toUserList1;
                    NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserId);
                    if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                        Util.addDebugLog("=================online alert status on: ");
                        msg = MsgUtil.iosPayload_alert_online(api, fromUsername, fromUserid, toUserId, true,urlAvatar, gender);
                    } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                        Util.addDebugLog("=================online alert status off: ");
                        msg = MsgUtil.iosPayload_alert_online(api, fromUsername, fromUserid, toUserId, false,urlAvatar, gender);
                    }
                    //JSONObject msg = MsgUtil.iosPayload(api, fromUsername, fromUserid, toUserId);
                    InterCommunicator.send(msg, toUserId);
                    LogNotificationDAO.addLog(toUserId, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
                }
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    private void sendNotiFCMbyDeviceId(String toUserid, String deviceId, String fromUserid, String fromUserName, String urlAvatar, Integer gender, String api,
            String ip) throws EazyException {
        if (!NotificationSettingDAO.checkUserNotification(toUserid, Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT)) {
            return;
        }
        JSONObject msg = null;
        NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
        if (toUserid.equals(fromUserid)) {
            return;
        }
        if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
            msg = MsgUtil.iosPayload_alert_online(api, fromUserName, fromUserid, toUserid, true,urlAvatar, gender);
        } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
            msg = MsgUtil.iosPayload_alert_online(api, fromUserName, fromUserid, toUserid, false,urlAvatar, gender);
        }
        InterCommunicator.sendByDeviceId(msg, deviceId);
        LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
    }
    private void sendNotiFCM(List<String> listToUser, String fromUserid, String fromUserName, String avaUrl,Integer gender,
            String api, String ip) throws EazyException {
        if (fromUserid != null && fromUserName != null && listToUser != null) {
            JSONObject msg = null;
            for (Object toUserList1 : listToUser) {
                String toUserId = (String) toUserList1;
                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserId);
                if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                    Util.addDebugLog("=================online alert status on: ");
                    msg = MsgUtil.iosPayload_alert_online(api, fromUserName, fromUserid, toUserId, true, avaUrl, gender);
                } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                    Util.addDebugLog("=================online alert status off: ");
                    msg = MsgUtil.iosPayload_alert_online(api, fromUserName, fromUserid, toUserId, false, avaUrl, gender);
                }
                //JSONObject msg = MsgUtil.iosPayload(api, fromUsername, fromUserid, toUserId);
                InterCommunicator.send(msg, toUserId);
                LogNotificationDAO.addLog(toUserId, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
            }
        }
    }
    
    private String sendNotiWebSocket(JSONArray listToUser,String fromUserid, String fromUserName, String urlAvatar, Integer gender, String api) throws EazyException {
        
        JSONObject listUserSendNotiSocket = new JSONObject();
        
        JSONArray listUser = new JSONArray();
        for (Object toUseridObject : listToUser) {
            JSONObject jsonSendSocket = new JSONObject();
            String toUserid = (String)toUseridObject;
            if (fromUserid != null && toUserid != null) {
                JSONObject msg = MsgUtil.iosPayload_alert_online(api, fromUserName, fromUserid, toUserid, true,urlAvatar, gender);
                jsonSendSocket.put("message_content", msg.toJSONString());
                jsonSendSocket.put("to", toUserid);
                jsonSendSocket.put("from", fromUserid);
                jsonSendSocket.put("msg_type", "NOTIBUZZ");
                if (toUserid.equals(fromUserid)) {
                    return null;
                }
            }
            listUser.add(jsonSendSocket.toJSONString());
        }
        if (!listUser.isEmpty()) {
            listUserSendNotiSocket.put("list_user", listUser);
            listUserSendNotiSocket.put("api", API.NOTI_BUZZ_SOCKET);
            return InterCommunicator.sendRequest(listUserSendNotiSocket.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
        }
        return null;
    }
    
}
