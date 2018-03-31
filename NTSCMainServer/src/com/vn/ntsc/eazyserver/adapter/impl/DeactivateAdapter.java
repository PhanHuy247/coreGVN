/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.eazyserver.adapter.impl;

import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;

/**
 *
 * @author DUONGLTD
 */
public class DeactivateAdapter implements IServiceAdapter {

    @Override
    public String callService(Request request) {
        try {
            String umsString = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            JSONObject umsJson = (JSONObject) new JSONParser().parse(umsString);
            String token = (String) request.token;
            Long code = (Long) umsJson.get(ParamKey.ERROR_CODE);
            if(code == ErrorCode.SUCCESS){
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);                    
                InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
//                InterCommunicator.sendRequest(request.toJson(), Config.FreeSwitchServerIp, Config.FreeSwitchServerPort);                                
                List<Session> listSession = SessionManager.clearSessionOfUser(token);
                try {
                    UserSessionDAO.remove(listSession);
                } catch (EazyException ex) {
                    Util.addErrorLog(ex);
                }
                List<String> list = new ArrayList<>();
                list.add(userId);
                InterCommunicator.resetOrDeletPresentation(list);                  
            }
            return umsString;
        } catch (ParseException ex) {
            Util.addErrorLog(ex);             
            return ResponseMessage.UnknownError;
        }
    }

}
