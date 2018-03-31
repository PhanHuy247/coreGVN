/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;

/**
 *
 * @author tuannxv00804
 */
public class LogoutAdapter implements IServiceAdapter {

    @Override
    public String callService(Request request) {
        try {
            if (request.token == null) {
                return ResponseMessage.BadResquestMessage;
            }

            Session session = SessionManager.getSession(request.token);
//        if( session == null ){
//            return Constant.InvailidTokenMessage;
//        }
            if (session != null) {
                JSONObject requestPre = new JSONObject();
                requestPre.put(ParamKey.API_NAME, API.LOG_OUT);
                requestPre.put(ParamKey.USER_ID, session.userID);
                InterCommunicator.sendRequest(requestPre.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
//                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                String userId = session.userID;
                request.put(ParamKey.NOTI_USER_ID, userId);
                String deviceToken = (String) request.getParamValue(ParamKey.NOTIFY_TOKEN);
                request.put(ParamKey.NOFI_DEVICE_TOKEN, deviceToken);
                Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
                request.reqObj.put(ParamKey.NOFI_DEVICE_TYPE, deviceType);
                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                
                request.reqObj.put("user_id", userId);
                request.reqObj.put("api", API.REMOVE_WEBSOCKET);
                InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
                UserSessionDAO.remove(session);
                SessionManager.removeSession(request.token);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return ResponseMessage.SuccessMessage;
    }

}
