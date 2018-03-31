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
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Administrator
 */
public class NotiLikeYourBuzzApi implements IApiAdapter {

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            String fromUsername = UserDAO.getUserName(fromUserid);
            String toUser = (String) obj.get(ParamKey.TOUSERID);
            String buzzid = (String) obj.get(ParamKey.NOTI_BUZZ_ID);
            String ip = (String) obj.get(ParamKey.IP);
            
            String avaUrl = getUrlAvatar(fromUserid);
            String urlImageBuzz = getUrlImageBuzz(buzzid);
            String textBuzz = getTextBuzz(buzzid);
            
            Integer gender = Helper.getGender(fromUserid);
            
            //send service Chat check socket
            String result = sendNotiWebSocket(toUser, fromUserid, buzzid, fromUsername, avaUrl, textBuzz, urlImageBuzz, gender, api);
            if(result == null){
                sendNotiFCM(toUser, fromUserid, buzzid, fromUsername, avaUrl, textBuzz, urlImageBuzz, gender, api, ip);
            }else{
                JSONParser parse = new JSONParser();
                JSONObject jsonResult = (JSONObject)parse.parse(result);
                //listDevice get from Service Chat
                JSONArray listDeviceId = (JSONArray)jsonResult.get("lst_device_id");
                //listDevice get from Database
                List<String> devices = Core.dao.getListDeviceId(toUser);
                for(String deviceId: devices){
                    if(listDeviceId.isEmpty() || !listDeviceId.contains(deviceId)){
                        sendNotiFCMbyDeviceId(deviceId,toUser, fromUserid, buzzid, fromUsername, avaUrl, textBuzz, urlImageBuzz, gender, api, ip);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
     private void sendNotiFCM(String toUser, String fromUserid, String buzzid, String fromUsername, String avaUrl, String textBuzz, String urlImageBuzz, Integer gender, String api,
            String ip) throws EazyException {
        if (fromUserid != null && fromUsername != null && toUser != null && buzzid != null) {
            JSONObject msg = null;
            NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUser);
            if (notification != null) { //OFF push noti
                if (notification.notiLike != null && notification.notiLike == 0) {
                    msg = MsgUtil.iosPayloadForLikeBuzz(api, fromUsername, buzzid, null, toUser, true, avaUrl, textBuzz, urlImageBuzz, gender);
                } else if (notification.notiLike != null && notification.notiLike == 1) {//ON push noti
                    msg = MsgUtil.iosPayloadForLikeBuzz(api, fromUsername, buzzid, null, toUser, false, avaUrl, textBuzz, urlImageBuzz, gender);
                }
            }
            InterCommunicator.send(msg, toUser);
            LogNotificationDAO.addLog(toUser, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
        }
    }
     
     private void sendNotiFCMbyDeviceId(String deviceId,String toUser, String fromUserid, String buzzid, String fromUsername, String avaUrl, String textBuzz, String urlImageBuzz, Integer gender, String api,
            String ip) throws EazyException {
        if (fromUserid != null && fromUsername != null && toUser != null && buzzid != null) {
            JSONObject msg = null;
            NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUser);
            if (notification != null) { //OFF push noti
                if (notification.notiLike != null && notification.notiLike == 0) {
                    msg = MsgUtil.iosPayloadForLikeBuzz(api, fromUsername, buzzid, null, toUser, true, avaUrl, textBuzz, urlImageBuzz, gender);
                } else if (notification.notiLike != null && notification.notiLike == 1) {//ON push noti
                    msg = MsgUtil.iosPayloadForLikeBuzz(api, fromUsername, buzzid, null, toUser, false, avaUrl, textBuzz, urlImageBuzz, gender);
                }
            }
            InterCommunicator.sendByDeviceId(msg, deviceId);
            LogNotificationDAO.addLog(toUser, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
        }
    }
    
    private String sendNotiWebSocket(String toUser,String fromUserid, String buzzid, String fromUsername, String avaUrl, String textBuzz, String urlImageBuzz, Integer gender, String api) throws EazyException {
        JSONObject jsonSendSocket = new JSONObject();
        JSONObject listUserSendNotiSocket = new JSONObject();
        if (fromUserid != null && toUser != null && buzzid != null) {
            JSONObject msg = MsgUtil.iosPayloadForLikeBuzz(api, fromUsername, buzzid, null, toUser, true, avaUrl, textBuzz, urlImageBuzz, gender);
            jsonSendSocket.put("message_content", msg.toJSONString());
            jsonSendSocket.put("to", toUser);
            jsonSendSocket.put("from", fromUserid);
            jsonSendSocket.put("msg_type", "NOTIBUZZ");
            if (toUser.equals(fromUserid)) {
                return null;
            }
        }
        if (!jsonSendSocket.isEmpty()) {
            listUserSendNotiSocket.put("msg_noti", jsonSendSocket);
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
