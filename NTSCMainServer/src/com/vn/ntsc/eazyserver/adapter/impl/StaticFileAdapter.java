/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.User;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Phan Huy
 */
public class StaticFileAdapter implements IServiceAdapter{

    @Override
    public String callService(Request request) {
        
        try {
            String userId = (String)request.getParamValue(ParamKey.USER_ID);
            User user = UserDAO.getUserInfor(userId);
            request.put(ParamKey.USER_NAME, user.username);
            request.put(ParamKey.EMAIL, user.email);
            Util.addDebugLog("user.username---------------------------"+user.username);
            Util.addDebugLog("user.username---------------------------"+request.toString());
            Util.addDebugLog("user.username---------------------------"+InterCommunicator.sendRequest(request.toString(), Config.StaticFileServerIp, Config.StaticFileServerPort));
            
        } catch (EazyException ex) {
            Logger.getLogger(StaticFileAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return InterCommunicator.sendRequest(request.toString(), Config.StaticFileServerIp, Config.StaticFileServerPort);
    }

    
    
}