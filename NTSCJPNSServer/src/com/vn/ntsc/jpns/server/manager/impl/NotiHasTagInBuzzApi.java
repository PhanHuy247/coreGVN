/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import com.vn.ntsc.jpns.Config;
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
import com.vn.ntsc.jpns.server.workers.BuzzNotificationContainer;
import com.vn.ntsc.jpns.server.workers.BuzzNotificationPackage;
import com.vn.ntsc.jpns.util.InterCommunicator;
import com.vn.ntsc.jpns.util.MsgUtil;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author hoangnh
 */
public class NotiHasTagInBuzzApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try{
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            JSONArray listToUser = (JSONArray) obj.get(ParamKey.TO_LIST_USER_ID);
            String ip = (String) obj.get(ParamKey.IP);
            String fromUserName = (String) obj.get(ParamKey.USER_NAME);
            String buzzId = (String) obj.get(ParamKey.NOTI_BUZZ_ID);
            String streamId = (String) obj.get(ParamKey.STREAM_ID);
            
            String avaUrl = getUrlAvatar(fromUserid);
            String urlImageBuzz = getUrlImageBuzz(buzzId);
            String textBuzz = getTextBuzz(buzzId);
            Integer gender = Helper.getGender(fromUserid);
            
            String result = sendNotiWebSocket(listToUser, fromUserid, buzzId, fromUserName, avaUrl, textBuzz, urlImageBuzz, gender, api,streamId);
            
            if(result == null){
                sendNotiFCM(listToUser, fromUserid, buzzId, fromUserName, avaUrl, textBuzz, urlImageBuzz, gender, api, ip,streamId);
            }else{
                JSONParser parse = new JSONParser();
                JSONObject jsonResult = (JSONObject)parse.parse(result);
                //listDevice get from Service Chat
                JSONArray listDeviceId = (JSONArray)jsonResult.get("list_device_id");
                JSONArray listUserId = (JSONArray)jsonResult.get("list_user_id");
                
                if(listUserId != null && !listUserId.isEmpty()){
                    sendNotiFCM(listUserId, fromUserid, buzzId, fromUserName, avaUrl, textBuzz, urlImageBuzz, gender, api, ip,streamId);
                }
                    //listDevice get from Database
                for (Object deviceId : listDeviceId) {
                    JSONObject jsonDeviceId = (JSONObject) deviceId;
                    String userId = (String) jsonDeviceId.get("user_id");
                    JSONArray listDevice = (JSONArray) jsonDeviceId.get("lst_device_id");
                    List<String> devices = Core.dao.getListDeviceId(userId);
                    for(String device : devices){
                        if(listDevice != null && !listDevice.contains(device)){
                            sendNotiFCMbyDeviceId(userId, device, fromUserid, buzzId, fromUserName, result, result, urlImageBuzz, gender, api, ip,streamId);
                        }
                    }
                }
            }
            
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
     private void sendNotiFCMbyDeviceId(String toUserid, String deviceId, String fromUserid, String buzzId, String fromUserName, String urlAvatar, String text, String urlImage, Integer gender, String api,
            String ip,String streamId) throws EazyException {
        if (!NotificationSettingDAO.checkUserNotification(toUserid, Constant.NOTIFICATION_TYPE_VALUE.TAG_FROM_BUZZ_NOTI)) {
            return;
        }
        JSONObject msg = null;
        NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
        if (toUserid.equals(fromUserid)) {
            return;
        }
        if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
            msg = MsgUtil.iosPayloadNotiNewBuzzForFavorited(api, fromUserName, fromUserid, toUserid, buzzId, true,urlAvatar,text,urlImage,gender,streamId);
        } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
            msg = MsgUtil.iosPayloadNotiNewBuzzForFavorited(api, fromUserName, fromUserid, toUserid, buzzId, false,urlAvatar,text,urlImage,gender,streamId);
        }
        InterCommunicator.sendByDeviceId(msg, deviceId);
        LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
    }
    private void sendNotiFCM(List<String> listToUser, String fromUserid, String buzzId, String fromUserName, String urlAvatar, String text, String urlImage, Integer gender,
            String api, String ip,String streamId) throws EazyException {
         for (Object o : listToUser) {
                String toUserid = (String) o;
                if(!NotificationSettingDAO.checkUserNotification(toUserid, Constant.NOTIFICATION_TYPE_VALUE.TAG_FROM_BUZZ_NOTI)){
                    continue;
                }
                if (fromUserid != null && toUserid != null) {
                    JSONObject msg = null;
                    NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
                    if (notification.notiBuzz != null && notification.notiBuzz == 0) { //OFF push noti
                        msg = MsgUtil.iosPayloadNotiNewBuzzForFavorited(api, fromUserName, fromUserid, toUserid, buzzId, true,urlAvatar,text,urlImage,gender,streamId);
                    } else if (notification.notiBuzz != null && notification.notiBuzz == 1) {//ON push noti
                        msg = MsgUtil.iosPayloadNotiNewBuzzForFavorited(api, fromUserName, fromUserid, toUserid, buzzId, false,urlAvatar,text,urlImage,gender,streamId);
                    }
                    InterCommunicator.send(msg, toUserid);
                    LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
                }
            }
    }
    
    private String sendNotiWebSocket(JSONArray listToUser,String fromUserid, String buzzId, String fromUserName, String urlAvatar, String text, String urlImage, Integer gender, String api,String streamId) throws EazyException {
        
        JSONObject listUserSendNotiSocket = new JSONObject();
        
        JSONArray listUser = new JSONArray();
        for (Object toUseridObject : listToUser) {
            JSONObject jsonSendSocket = new JSONObject();
            String toUserid = (String)toUseridObject;
            if (fromUserid != null && toUserid != null) {
                JSONObject msg = MsgUtil.iosPayloadNotiNewBuzzForFavorited(api, fromUserName, fromUserid, toUserid, buzzId, true,urlAvatar,text,urlImage,gender,streamId);
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
    
    private String getUrlAvatar(String fromUserid) throws EazyException{
        String avatarId = UserDAO.getAvatarId(fromUserid);
        String urlAvatar = null;
        if (avatarId != null && avatarId != "") {
            String thumbnailUrl = ThumbnailDAO.getImageUrl(avatarId);
            if (thumbnailUrl != null) {
                urlAvatar = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + thumbnailUrl;
            }
        }
        return urlAvatar;
    }
    
    private String getUrlImageBuzz(String buzzid) throws EazyException{
        String urlImage = null;
        String imageId = BuzzDetailDAO.getImageId(buzzid);
        if (imageId != null && !imageId.isEmpty()) {
            String thumbnailUrl = ThumbnailDAO.getImageUrl(imageId);
            if (thumbnailUrl != null) {
                urlImage = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + thumbnailUrl;
            }
        }
        return urlImage; 
    }
    public String getTextBuzz(String buzzid) throws EazyException{
        String text = BuzzDetailDAO.getText(buzzid);
        return text;
    }
}
