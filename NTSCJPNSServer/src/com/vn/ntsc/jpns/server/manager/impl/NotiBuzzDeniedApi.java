/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import com.vn.ntsc.jpns.Config;
import com.vn.ntsc.jpns.dao.impl.BuzzDetailDAO;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
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
import eazycommon.constant.FilesAndFolders;
import eazycommon.exception.EazyException;

/**
 *
 * @author Administrator
 */
public class NotiBuzzDeniedApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String toUserid = (String) obj.get(ParamKey.TOUSERID);
            String imageId = (String) obj.get(ParamKey.NOTI_IMAGE_ID);
            String buzzId = (String) obj.get(ParamKey.NOTI_BUZZ_ID);
            String ip = (String) obj.get(ParamKey.IP);
            
            String avaUrl = getUrlAvatar(toUserid);
            String urlImageBuzz = getUrlImageBuzz(buzzId);
            String textBuzz = getTextBuzz(buzzId);
            Integer gender = Helper.getGender(toUserid);

            if (imageId != null && toUserid != null && buzzId != null) {
                JSONObject msg = null;
                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
                if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                    msg = MsgUtil.iosPayload_image(api, toUserid, buzzId, imageId, true,avaUrl,textBuzz,urlImageBuzz,gender);
                } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                    msg = MsgUtil.iosPayload_image(api, toUserid, buzzId, imageId, false,avaUrl,textBuzz,urlImageBuzz,gender);
                }
                InterCommunicator.send(msg, toUserid);
                LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), null, Util.getGMTTime(), ip);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
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
