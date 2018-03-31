/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import com.vn.ntsc.jpns.Config;
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
import eazycommon.constant.Constant;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class NotiShareMusic implements IApiAdapter{
    
    @Override
    public void execute(JSONObject obj, Date time) {
        try{
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            JSONArray listToUser = (JSONArray) obj.get(ParamKey.TO_LIST_USER_ID);
            String ip = (String) obj.get(ParamKey.IP);
            String fromUserName = (String) obj.get(ParamKey.USER_NAME);
            String buzzId = (String) obj.get(ParamKey.NOTI_BUZZ_ID);
            
            String avatarId = UserDAO.getAvatarId(fromUserid);
            String urlAvatar = null;
            String urlImage = null;
            Util.addDebugLog("avatarId===================================="+avatarId);
            if(avatarId != null && avatarId != "" && ObjectId.isValid(avatarId)){
                String url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + ThumbnailDAO.getImageUrl(avatarId);
                if(!url.contains("null")){
                    urlAvatar = url;
                }
            }
            String text = BuzzDetailDAO.getText(buzzId);
            String imageId = BuzzDetailDAO.getImageId(buzzId);
            if(imageId != "" && imageId != null){
                String url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + ThumbnailDAO.getImageUrl(imageId);
                if(!url.contains("null"))
                    urlImage = url;
            }
            Integer gender = Helper.getGender(fromUserid);
            
            for (Object o : listToUser) {
                String toUserid = (String) o;
                if(!NotificationSettingDAO.checkUserNotification(toUserid, Constant.NOTIFICATION_TYPE_VALUE.SHARE_MUSIC)){
                    continue;
                }
                if (fromUserid != null && toUserid != null) {
                    JSONObject msg = null;
                    NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
                    if (notification.notiBuzz != null && notification.notiBuzz == 0) { //OFF push noti
                        msg = MsgUtil.iosPayloadNotiNewBuzzForFavorited(api, fromUserName, fromUserid, toUserid, buzzId, true,urlAvatar,text,urlImage,gender,null);
                    } else if (notification.notiBuzz != null && notification.notiBuzz == 1) {//ON push noti
                        msg = MsgUtil.iosPayloadNotiNewBuzzForFavorited(api, fromUserName, fromUserid, toUserid, buzzId, false,urlAvatar,text,urlImage,gender,null);
                    }
                    InterCommunicator.send(msg, toUserid);
                    LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
}
