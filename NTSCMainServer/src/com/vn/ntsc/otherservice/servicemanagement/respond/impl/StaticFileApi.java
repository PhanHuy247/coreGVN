/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import com.vn.ntsc.Config;
import com.vn.ntsc.Setting;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.User;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.ListEntityRespond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.StringRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class StaticFileApi implements IServiceAdapter {
    
    public static final GetUploadSetting getUploadSetting = new GetUploadSetting();
    public static final GetStickerUrl getStickerUrl = new GetStickerUrl();
    public static final GetEmojiUrl getEmojiUrl = new GetEmojiUrl();
    
    @Override
    public String callService(Request request) {
         try {
            StringRespond result = new StringRespond();
            
            String userId = (String) request.getParamValue(ParamKey.USER_ID);
            User user = null;
            
            user = UserDAO.getUserInfor(userId);
            
            request.put(ParamKey.USER_NAME, user.username);
            request.put(ParamKey.EMAIL, user.email);
            request.put(ParamKey.VIDEO_STATUS, Setting.auto_approved_video+"");
            Util.addDebugLog("user.username---------------------------" + user.username);
            Util.addDebugLog("user.username---------------------------" + request.toString());
            
            
        } catch (EazyException ex) {
            Logger.getLogger(StaticFileApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return request.toString();
    }
    
    public static class GetUploadSetting implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            return InterCommunicator.sendRequest(request.toJson(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        }
    }
    
    public static class GetStickerUrl implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            return InterCommunicator.sendRequest(request.toJson(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        }
    }
    
    public static class GetEmojiUrl implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            return InterCommunicator.sendRequest(request.toJson(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        }
        
    }
}
