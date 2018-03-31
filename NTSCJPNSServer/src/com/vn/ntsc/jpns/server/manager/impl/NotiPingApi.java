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
import com.vn.ntsc.jpns.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.jpns.entity.impl.notification.NotificationSetting;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;
import com.vn.ntsc.jpns.util.InterCommunicator;
import com.vn.ntsc.jpns.util.MsgUtil;

/**
 *
 * @author Administrator
 */
public class NotiPingApi implements IApiAdapter {

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            String fromUsername = Core.dao.getUsername(fromUserid);
            String toUser = (String) obj.get(ParamKey.TOUSERID);
            Long typecall = (Long) obj.get("typecall");
            int typecallInt = 0;
            if (typecall != null) {
                typecallInt = typecall.intValue();
            }
            if (fromUserid != null && fromUsername != null && toUser != null) {
                JSONObject msg = null;
                //Check user off ko?
                //neu off -> sua key cua msg
                //neu on thi giu nguyen
                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUser);
                if (notification != null) { //OFF push noti
                    //Util.addInfoLog("NotiPingApi========notification"+ notification);
                    if (notification.notiLike != null && notification.notiLike == 0) {
                        msg = MsgUtil.iosPayload_ping(api, fromUsername, fromUserid, toUser, true, typecallInt);
                    } else if (notification.notiLike != null && notification.notiLike == 1) {//ON push noti
                        msg = MsgUtil.iosPayload_ping(api, fromUsername, fromUserid, toUser, false, typecallInt);
                    }
                }
                //JSONObject msg = MsgUtil.iosPayload(api, fromUsername, fromUserid, toUser);
                InterCommunicator.send(msg, toUser);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

}
