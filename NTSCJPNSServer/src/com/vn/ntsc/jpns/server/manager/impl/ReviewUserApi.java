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
public class ReviewUserApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String toUserid = (String) obj.get(ParamKey.TOUSERID);
            Long reviewResult = (Long) obj.get("review_result");
            String ip = (String) obj.get(ParamKey.IP);
            String api;
            
            String avaUrl = getUrlAvatar(toUserid);
            Integer gender = Helper.getGender(toUserid);

            if (toUserid != null && reviewResult != null) {
                JSONObject msg = null;
                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
                if (reviewResult == -1) { // denied all
                    api = "denied_user_infor_noti";
                    if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                        msg = MsgUtil.iosPayload_update_user(api, null, null, toUserid, true,avaUrl,gender);
                    } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                        msg = MsgUtil.iosPayload_update_user(api, null, null, toUserid, false,avaUrl,gender);
                    }
                } else if (reviewResult == 0) { // something was denied
                    api = "denied_apart_of_user_info";
                } else { // approve all
                    api = "approve_user_infor_noti";
                    if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                        msg = MsgUtil.iosPayload_update_user(api, null, null, toUserid, true,avaUrl,gender);
                    } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                        msg = MsgUtil.iosPayload_update_user(api, null, null, toUserid, false,avaUrl,gender);
                    }
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
}
