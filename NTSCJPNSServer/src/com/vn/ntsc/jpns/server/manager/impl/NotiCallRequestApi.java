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
import com.vn.ntsc.jpns.server.manager.IApiAdapter;
import com.vn.ntsc.jpns.util.InterCommunicator;
import com.vn.ntsc.jpns.util.MsgUtil;

/**
 *
 * @author Administrator
 */
public class NotiCallRequestApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String api = (String) obj.get(ParamKey.API_NAME);
            String fromUserid = (String) obj.get(ParamKey.FROM_USER_ID);
            String fromUsername = Core.dao.getUsername(fromUserid);
            String toUser = (String) obj.get(ParamKey.TOUSERID);
            if (fromUserid != null && toUser != null) {
                JSONObject msg = MsgUtil.iosPayload(api, fromUsername, fromUserid, toUser);
                InterCommunicator.send(msg, toUser);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
}
