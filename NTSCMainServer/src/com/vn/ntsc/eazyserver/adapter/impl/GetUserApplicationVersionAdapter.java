/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.Setting;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;

/**
 *
 * @author RuAc0n
 */
public class GetUserApplicationVersionAdapter implements IServiceAdapter{

    @Override
    public String callService(Request request) {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
        Session session = SessionManager.getSession(request.token);
        JSONObject data = new JSONObject();
        data.put("turn_off_safary_version", Setting.turnOffSafaryVersion);
        data.put("turn_off_browser_android_version", Setting.turnOffBrowserAndroidVersion);
//        data.put("turn_off_safary_version_for_enterprise", Setting.enterpriseturnOffSafaryVersion);
        if(session != null ){
            if(session.applicationVersion != null)
                data.put("user_application_version", session.applicationVersion);
            if(session.applicationType != null)
                data.put("user_application_type", session.applicationType);
        }
        obj.put(ParamKey.DATA, data);
        return obj.toJSONString();
    }
    
}
