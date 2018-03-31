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
import com.vn.ntsc.jpns.dao.impl.User;
import com.vn.ntsc.jpns.dao.impl.UserDAO;
import com.vn.ntsc.jpns.server.constant.Params;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;
import com.vn.ntsc.jpns.server.workers.MsgContainer;
import com.vn.ntsc.jpns.util.InterCommunicator;
import com.vn.ntsc.jpns.util.MsgUtil;

/**
 *
 * @author Administrator
 */
public class NotiMissCallApi implements IApiAdapter {

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            User fromUser = UserDAO.getUserInfor(fromUserid);

            String fromUsername = Core.dao.getUsername(fromUserid);
            String toUserid = (String) obj.get(ParamKey.TOUSERID);
            String ip = (String) obj.get(ParamKey.IP);
            String push_content = fromUser.username + "： 不在着信";
            int badge = 0;
            try {
                Long badgeStr = (Long) obj.get(ParamKey.BADGE);
                badge = badgeStr.intValue();
            } catch (Exception ex) {

            }
            if (fromUserid != null && fromUsername != null && toUserid != null) {
                JSONObject msg = MsgUtil.iosPayload_miss_call(api, fromUsername, fromUserid, badge, toUserid, push_content);
                //JSONObject msg = MsgUtil.iosPayload_miss_call(api , fromUsername, fromUserid, badge, toUserid);
                User u = UserDAO.getUserInfor(toUserid);
                if (u.deviceType == 0) {
                    InterCommunicator.send(msg, toUserid);
                    LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
                }
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

}
