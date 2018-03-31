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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class NotiReplyYourCommentApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            String fromUsername = UserDAO.getUserName(fromUserid);
            String buzzid = (String) obj.get(ParamKey.NOTI_BUZZ_ID);
            String ownerName = (String) obj.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
            String ip = (String) obj.get(ParamKey.IP);
            String cmtValue = (String) obj.get("cmt_value");
            String cmtUserId = (String) obj.get("cmt_user_id");
            String cmtTime = (String) obj.get("cmt_time");
            String cmtId = (String) obj.get(ParamKey.COMMENT_ID);
            
             //list deviceId send noti by FCM
            Map<String, List<String>> listDeviceIdSendFCM = new HashMap<>();
            
            //list user send noti by websocket
            JSONArray listUserNotiSocketSubCmt = (JSONArray) obj.get("list_user_noti_socket");
            
            //list deviceId send noti by websocket
            JSONArray listDeviceIdNotiSocket = (JSONArray) obj.get("list_device_id_noti_socket");
            
            //check deviceId websocket not alive ( send noti case 1 account login multi device)
            checkDeviceIdToSendNotiFCM(listDeviceIdNotiSocket, listDeviceIdSendFCM);
            
            String urlAvatar = getAvatar(fromUserid);
            String text = BuzzDetailDAO.getText(buzzid);
            String urlImage = getImageBuzz(buzzid);
            Integer gender = Helper.getGender(fromUserid);
            
            //parent comment
            String avatarIdCmt = UserDAO.getAvatarId(cmtUserId);
            String userNameCmt = UserDAO.getUserName(cmtUserId);
            
            //send noti FCM
            if (buzzid != null && !buzzid.isEmpty()) {
                JSONArray toUserList = (JSONArray) obj.get(ParamKey.TO_LIST_USER_ID);
                if (toUserList != null) {
                    sendNotiFCM(toUserList, fromUserid, buzzid, fromUsername, urlAvatar, text, urlImage, gender, api, ip, ownerName, cmtUserId, cmtId, avatarIdCmt, cmtValue, userNameCmt);
                }
            }
            
            //send noti websocket
            sendNotiWebSocket(listUserNotiSocketSubCmt, fromUserid, buzzid, fromUsername, urlAvatar, text, urlImage, gender, api, ip, ownerName, cmtUserId, cmtId, avatarIdCmt, cmtValue, userNameCmt);
        
            //send noti FCM by DeviceId
            sendNotiFCMbyDeviceId(listDeviceIdSendFCM, fromUserid, buzzid, fromUsername, urlAvatar, text, urlImage, gender, api, ip, ownerName, cmtUserId, cmtId, avatarIdCmt, cmtValue, userNameCmt);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
     private void sendNotiFCMbyDeviceId(Map<String, List<String>> listDeviceIdSendFCM, String fromUserid, String buzzid, String fromUsername, String urlAvatar, String text, String urlImage, Integer gender, String api, 
                            String ip,String ownerName,String cmtUserId,String cmtId,String avatarIdCmt,String cmtValue,String userNameCmt) throws EazyException {
         for (String toUserid : listDeviceIdSendFCM.keySet()) {
             if (listDeviceIdSendFCM.get(toUserid).size() > 0) {
                 for (String deviceId : listDeviceIdSendFCM.get(toUserid)) {
                     if (!NotificationSettingDAO.checkUserNotification(toUserid, Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT)) {
                         continue;
                     }
                     JSONObject msg = null;
                     NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
                     if (toUserid.equals(fromUserid)) {
                         continue;
                     }
                     if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                         msg = MsgUtil.iosPayload_noti_sub_comment_your_buzz(api, fromUsername, buzzid, ownerName, toUserid, true, urlAvatar, text, urlImage, cmtUserId, cmtId, avatarIdCmt, cmtValue, userNameCmt, gender);
                     } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                         msg = MsgUtil.iosPayload_noti_sub_comment_your_buzz(api, fromUsername, buzzid, ownerName, toUserid, false, urlAvatar, text, urlImage, cmtUserId, cmtId, avatarIdCmt, cmtValue, userNameCmt, gender);
                     }
                     InterCommunicator.sendByDeviceId(msg, deviceId);
                     LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
                 }
             }
         }
    }
    
    private void sendNotiWebSocket(List<String> listUserNotiSocketSubCmt, String fromUserid, String buzzid, String fromUsername, String urlAvatar, String text, String urlImage, Integer gender, String api, 
                            String ip,String ownerName,String cmtUserId,String cmtId,String avatarIdCmt,String cmtValue,String userNameCmt)throws EazyException {
        JSONObject listUserSendNotiSocket = new JSONObject();
        JSONArray listUser = new JSONArray();
        JSONObject jsonSendSocket = new JSONObject();
        for (Object toUseridObj : listUserNotiSocketSubCmt) {
            String toUserid = (String) toUseridObj;
            if (fromUserid != null && toUserid != null && buzzid != null) {
                JSONObject msg = MsgUtil.iosPayload_noti_sub_comment_your_buzz(api, fromUsername, buzzid, ownerName, toUserid, false, urlAvatar, text, urlImage, cmtUserId, cmtId, avatarIdCmt, cmtValue, userNameCmt, gender);
                jsonSendSocket.put("message_content", msg.toJSONString());
                jsonSendSocket.put("to", toUserid);
                jsonSendSocket.put("from", fromUserid);
                jsonSendSocket.put("msg_type", "NOTIBUZZ");
                listUser.add(jsonSendSocket.toJSONString());
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
    
    private void sendNotiFCM(List<String> toUserList, String fromUserid, String buzzid, String fromUsername, String urlAvatar, String text, String urlImage, Integer gender, String api, 
                            String ip,String ownerName,String cmtUserId,String cmtId,String avatarIdCmt,String cmtValue,String userNameCmt) throws EazyException {
        for (Object toUserList1 : toUserList) {
            String toUserid = (String) toUserList1;
            if (!NotificationSettingDAO.checkUserNotification(toUserid, Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT)) {
                continue;
            }
            JSONObject msg = null;
            NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);

            if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                msg = MsgUtil.iosPayload_noti_sub_comment_your_buzz(api, fromUsername, buzzid, ownerName, toUserid, true, urlAvatar, text, urlImage, cmtUserId, cmtId, avatarIdCmt, cmtValue, userNameCmt, gender);
            } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                msg = MsgUtil.iosPayload_noti_sub_comment_your_buzz(api, fromUsername, buzzid, ownerName, toUserid, false, urlAvatar, text, urlImage, cmtUserId, cmtId, avatarIdCmt, cmtValue, userNameCmt, gender);
            }
            InterCommunicator.send(msg, toUserid);
            LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
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
}
