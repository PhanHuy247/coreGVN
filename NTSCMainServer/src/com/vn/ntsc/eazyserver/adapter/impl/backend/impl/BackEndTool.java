/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl.backend.impl;

import java.security.MessageDigest;
import java.util.UUID;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.backend.IServiceBackendAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;

/**
 *
 * @author RuAc0n
 */
public class BackEndTool implements IServiceBackendAdapter {

    private static final String username = "longlh";
    private static String password;

    static {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String pass = "1";
            md.update(pass.getBytes());
            //
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            password = sb.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
        }
    }
    private static final String userid = UUID.randomUUID().toString();

    public static final String BadRequest;
    static{
        JSONObject jo = new JSONObject();
        jo.put( ParamKey.ERROR_CODE, ErrorCode.UNKNOWN_ERROR );
        BadRequest = jo.toJSONString();
    }
    
    public static final LoginTool loginTool = new LoginTool();

    @Override
    public String callService(Request request) {    
        if(checkPermission(request))
            return InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
        else{
            return BadRequest;
        }
    }

    static class LoginTool implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            JSONObject obj = new JSONObject();
            try {
                String uName = (String) request.getParamValue(ParamKey.USER_NAME);
                String pwd = (String) request.getParamValue(ParamKey.PASSWORD);
                if (uName.equals(username) && pwd.equals(password)) {
                    Session ss = new Session(userid, password);
                    SessionManager.putSession(ss);
                    String token = ss.token;
                    obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                    JSONObject dataObj = new JSONObject();
                    dataObj.put(ParamKey.TOKEN_STRING, token);
                    obj.put(ParamKey.DATA, dataObj);
                }else{
                    return BadRequest;
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);                
               
                return BadRequest;
            }
            return obj.toJSONString();
        }
    }

    private static boolean checkPermission(Request request) {
        try {
            String userId = (String) request.getParamValue(ParamKey.USER_ID);
            if (userId.equals(userid)) {
                return true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            return false;
        }
        return false;
    }
}
