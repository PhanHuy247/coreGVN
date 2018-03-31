/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONArray;
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
public class AutoMessageApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
//            String fromUsername = Core.dao.getUsername(fromUserid);
            JSONArray listToUser = (JSONArray) obj.get(ParamKey.TO_LIST_USER_ID);
            String ip = (String) obj.get(ParamKey.IP);
            for (Object o : listToUser) {
                JSONObject toUser = (JSONObject) o;
                String toUserid = (String) toUser.get(ParamKey.TOUSERID);
                Long badge = (Long) toUser.get(ParamKey.BADGE);
                if (fromUserid != null && toUserid != null) {
                    JSONObject msg = null;
                    NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
                    if (notification.andgAlert == 0) { //OFF push noti
                        msg = MsgUtil.iosPayload_auto_message("noti_new_chat_msg_text", null, fromUserid, badge.intValue(), toUserid, true);
                    } else if (notification.andgAlert == 1) {//ON push noti
                        msg = MsgUtil.iosPayload_auto_message("noti_new_chat_msg_text", null, fromUserid, badge.intValue(), toUserid, false);
                    }
                    //JSONObject msg = MsgUtil.iosPayload("noti_new_chat_msg_text", null, fromUserid, badge.intValue(), toUserid);
                    InterCommunicator.send(msg, toUserid);
                    LogNotificationDAO.addLog(toUserid, Params.getNotiType("noti_new_chat_msg_text"), fromUserid, Util.getGMTTime(), ip);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
}
