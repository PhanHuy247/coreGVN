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
import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.Core;
import com.vn.ntsc.jpns.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.jpns.dao.impl.LogNotificationDAO;
import com.vn.ntsc.jpns.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.jpns.dao.impl.ThumbnailDAO;
import com.vn.ntsc.jpns.dao.impl.UserDAO;
import com.vn.ntsc.jpns.entity.impl.notification.NotificationSetting;
import com.vn.ntsc.jpns.server.Helper;
import com.vn.ntsc.jpns.server.constant.Params;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;
import com.vn.ntsc.jpns.util.InterCommunicator;
import com.vn.ntsc.jpns.util.MsgUtil;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.FilesAndFolders;
import eazycommon.exception.EazyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;

/**
 *
 * @author Administrator
 */
public class NotiCommentYourBuzzApi implements IApiAdapter {

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            String fromUsername = UserDAO.getUserName(fromUserid);
//            String toUserid = (String) obj.get(ParamKey.TOUSERID);
            String buzzid = (String) obj.get(ParamKey.NOTI_BUZZ_ID);
            String ip = (String) obj.get(ParamKey.IP);

            //list user send noti by FCM
            JSONArray listUserCmt = (JSONArray) obj.get("list_user_comment");

            //list deviceId send noti by FCM
            Map<String, List<String>> listDeviceIdSendFCM = new HashMap<>();

            //list user send noti by websocket
            JSONArray listUserNotiSocketCmt = (JSONArray) obj.get("list_user_noti_socket");

            //list deviceId send noti by websocket
            JSONArray listDeviceIdNotiSocket = (JSONArray) obj.get("list_device_id_noti_socket");

            //check deviceId websocket not alive ( send noti case 1 account login multi device)
            if(listDeviceIdNotiSocket != null){
                checkDeviceIdToSendNotiFCM(listDeviceIdNotiSocket, listDeviceIdSendFCM);
            }
            String urlAvatar = getAvatar(fromUserid);
            String text = BuzzDetailDAO.getText(buzzid);
            String urlImage = getImageBuzz(buzzid);

            Integer gender = Helper.getGender(fromUserid);

            //send noti FCM
            sendNotiFCM(listUserCmt, fromUserid, buzzid, fromUsername, urlAvatar, text, urlImage, gender, api, ip);
            //send noti websocket
            sendNotiWebSocket(listUserNotiSocketCmt, fromUserid, buzzid, fromUsername, urlAvatar, text, urlImage, gender, api, ip);

            //send noti FCM by DeviceId
            sendNotiFCMbyDeviceId(listDeviceIdSendFCM, fromUserid, buzzid, fromUsername, urlAvatar, text, urlImage, gender, api, ip);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    private void sendNotiFCMbyDeviceId(Map<String, List<String>> listDeviceIdSendFCM, String fromUserid, String buzzid, String fromUsername, String urlAvatar, String text, String urlImage, Integer gender, String api, String ip) throws EazyException {
        for (String toUserid : listDeviceIdSendFCM.keySet()) {
            if (listDeviceIdSendFCM.get(toUserid).size() > 0) {
                for (String deviceId : listDeviceIdSendFCM.get(toUserid)) {
                    if (!NotificationSettingDAO.checkUserNotification(toUserid, Constant.NOTIFICATION_TYPE_VALUE.COMMENT_MY_BUZZ_NOTI)) {
                        continue;
                    }
                    if (toUserid.equals(fromUserid)) {
                        continue;
                    }
                    if (fromUserid != null && toUserid != null && buzzid != null && fromUsername != null) {
                        JSONObject msg = null;

                        NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);

                        if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                            msg = MsgUtil.iosPayload_noti_comment_your_buzz(api, fromUsername, buzzid, null, toUserid, true, urlAvatar, text, urlImage, gender);
                        } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                            msg = MsgUtil.iosPayload_noti_comment_your_buzz(api, fromUsername, buzzid, null, toUserid, false, urlAvatar, text, urlImage, gender);
                        }
                        InterCommunicator.sendByDeviceId(msg, deviceId);
                        LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
                    }
                }

            }
        }

    }

    private void sendNotiFCM(List<String> listUserCmt, String fromUserid, String buzzid, String fromUsername, String urlAvatar, String text, String urlImage, Integer gender, String api, String ip) throws EazyException {
        for (Object toUseridObj : listUserCmt) {
            String toUserid = (String) toUseridObj;
            if (!NotificationSettingDAO.checkUserNotification(toUserid, Constant.NOTIFICATION_TYPE_VALUE.COMMENT_MY_BUZZ_NOTI)) {
                continue;
            }
            if (toUserid.equals(fromUserid)) {
                continue;
            }
            if (fromUserid != null && toUserid != null && buzzid != null && fromUsername != null) {
                JSONObject msg = null;

                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);

                if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                    msg = MsgUtil.iosPayload_noti_comment_your_buzz(api, fromUsername, buzzid, null, toUserid, true, urlAvatar, text, urlImage, gender);
                } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                    msg = MsgUtil.iosPayload_noti_comment_your_buzz(api, fromUsername, buzzid, null, toUserid, false, urlAvatar, text, urlImage, gender);
                }
                InterCommunicator.send(msg, toUserid);
                LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
            }
        }
    }
    
    private void sendNotiWebSocket(List<String> listUserNotiSocketCmt, String fromUserid, String buzzid, String fromUsername, String urlAvatar, String text, String urlImage, Integer gender, String api, String ip) throws EazyException {
        JSONObject listUserSendNotiSocket = new JSONObject();
        JSONArray listUser = new JSONArray();
        JSONObject jsonSendSocket = new JSONObject();
        for (Object toUseridObj : listUserNotiSocketCmt) {
            String toUserid = (String) toUseridObj;
            if (fromUserid != null && toUserid != null && buzzid != null) {
                JSONObject msg = MsgUtil.iosPayload_noti_comment_your_buzz(api, fromUsername, buzzid, null, toUserid, false, urlAvatar, text, urlImage, gender);
                jsonSendSocket.put("message_content", msg.toJSONString());
                jsonSendSocket.put("to", toUserid);
                jsonSendSocket.put("from", fromUserid);
                jsonSendSocket.put("msg_type", "NOTIBUZZ");
                if (toUserid.equals(fromUserid)) {
                    continue;
                }
                listUser.add(jsonSendSocket.toJSONString());
            }

        }
        if (!listUser.isEmpty()) {
            listUserSendNotiSocket.put("list_user", listUser);
            listUserSendNotiSocket.put("api", API.NOTI_BUZZ_SOCKET);
            InterCommunicator.sendRequest(listUserSendNotiSocket.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
        }
    }

    private void checkDeviceIdToSendNotiFCM(JSONArray listDeviceIdNotiSocket, Map<String, List<String>> listDeviceIdSendFCM) {
        for (Object listDeviceObject : listDeviceIdNotiSocket) {
            JSONObject listDeviceJson = (JSONObject) listDeviceObject;
            String userId = (String) listDeviceJson.get("user_id");
            JSONArray listDeviceId = (JSONArray) listDeviceJson.get("lst_device_id");
            List<String> devices = Core.dao.getListDeviceId(userId);
            for (Object devicesObject : listDeviceId) {
                String deviceId = (String) devicesObject;
                if (devices.contains(deviceId)) {
                    devices.remove(deviceId);
                }
            }
            if (!devices.isEmpty()) {
                listDeviceIdSendFCM.put(userId, devices);
            }
        }
    }

    private String getImageBuzz(String buzzid) throws EazyException {
        String imageId = BuzzDetailDAO.getImageId(buzzid);
        if (imageId != null && !imageId.isEmpty()) {
            String imageThumbnail = ThumbnailDAO.getImageUrl(imageId);
            String url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + imageThumbnail;
            if (imageThumbnail != null && imageThumbnail != "") {
                return url;
            }
        }
        return null;
    }

    private String getAvatar(String fromUserid) throws EazyException {
        String avatarId = UserDAO.getAvatarId(fromUserid);
        if (avatarId != null && !avatarId.isEmpty()) {
            String imageThumbnail = ThumbnailDAO.getImageUrl(avatarId);
            if (imageThumbnail != null && imageThumbnail != "") {
                String url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + imageThumbnail;
                return url;
            }
        }
        return null;
    }
}
