/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
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
public class NotiBackStageDeniedApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String toUserid = (String) obj.get(ParamKey.TOUSERID);
            String imageId = (String) obj.get(ParamKey.NOTI_IMAGE_ID);
            String ip = (String) obj.get(ParamKey.IP);

            if (imageId != null && toUserid != null) {
                //JSONObject msg = MsgUtil.iosPayload_backstage(api, toUserid, null, imageId);
                JSONObject msg = null;
                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
                if (notification.andgAlert != null && notification.andgAlert == 0) { //OFF push noti
                    msg = MsgUtil.iosPayload_backstage(api, toUserid, null, imageId, true);
                } else if (notification.andgAlert != null && notification.andgAlert == 1) {//ON push noti
                    msg = MsgUtil.iosPayload_backstage(api, toUserid, null, imageId, false);
                }
                InterCommunicator.send(msg, toUserid);
                LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), null, Util.getGMTTime(), ip);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
}
