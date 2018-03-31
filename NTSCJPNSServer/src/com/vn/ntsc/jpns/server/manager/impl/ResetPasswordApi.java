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
import com.vn.ntsc.jpns.Core;
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
public class ResetPasswordApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            String fromUsername = Core.dao.getUsername(fromUserid);
            String toUser = (String) obj.get(ParamKey.TOUSERID);
            String ip = (String) obj.get(ParamKey.IP);
            if (fromUserid != null && fromUsername != null && toUser != null) {
                JSONObject msg = null;

                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUser);
                if (notification.resetPassword != null && notification.resetPassword == 0) { //OFF push noti
                    msg = MsgUtil.iosPayload_reset_pwd(api, fromUsername, null, toUser, true);
                } else if (notification.resetPassword != null && notification.resetPassword == 1) {//ON push noti
                    msg = MsgUtil.iosPayload_reset_pwd(api, fromUsername, null, toUser, false);
                }
                InterCommunicator.send(msg, toUser);
                LogNotificationDAO.addLog(toUser, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);

            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
}
