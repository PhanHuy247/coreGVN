/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import com.vn.ntsc.jpns.Config;
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
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class NotiRecordingFileApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String toUserid = (String) obj.get(ParamKey.TOUSERID);
            String videoId = (String) obj.get(ParamKey.NOTI_VIDEO_ID);
            String buzzId = (String) obj.get(ParamKey.BUZZ_ID);
            String ip = (String) obj.get(ParamKey.IP);
            
            String avatarId = UserDAO.getAvatarId(toUserid);
            String urlAvatar = null;
            if(avatarId != null && avatarId != ""){
                String url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + ThumbnailDAO.getImageUrl(avatarId);
                if(!url.contains("null")){
                    urlAvatar = url;
                }
            }
            Integer gender = Helper.getGender(toUserid);

            if (videoId != null && toUserid != null) {
                //JSONObject msg = MsgUtil.iosPayload_backstage(api, toUserid, null, imageId);
                JSONObject msg = null;
                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
                if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                    msg = MsgUtil.iosPayloadForRecordingFile(api, toUserid, urlAvatar, buzzId, videoId, true, gender);
                } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                    msg = MsgUtil.iosPayloadForRecordingFile(api, toUserid, urlAvatar, buzzId, videoId, false, gender);
                }
                InterCommunicator.send(msg, toUserid);
                LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), null, Util.getGMTTime(), ip);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
}
