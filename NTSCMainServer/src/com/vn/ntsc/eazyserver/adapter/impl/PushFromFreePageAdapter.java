/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.dao.impl.NotificationSettingDAO;

/**
 *
 * @author duyetpt
 */
public class PushFromFreePageAdapter implements IServiceAdapter {


    public static final PushFromBackEndAdapter pushFromBackend = new PushFromBackEndAdapter();
    public static final PushFromBackEndNewsAdapter pushFromBackendNews = new PushFromBackEndNewsAdapter();
    
    @Override
    public String callService(Request request) {
        try{
            Util.addDebugLog("vao PushFromFreePageAdapter");
            String umsString = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            JSONObject obj  = (JSONObject) new JSONParser().parse(umsString);
            Long code = (Long) obj.get(ParamKey.ERROR_CODE);
            if(code == ErrorCode.SUCCESS){
                JSONObject data = (JSONObject) obj.get(ParamKey.DATA);
                Long isNoti = (Long) data.get(ParamKey.IS_NOTI);
                if(isNoti == Constant.FLAG.ON){
                    
                    InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return ResponseMessage.SuccessMessage;
    }
   
    private static class PushFromBackEndAdapter implements IServiceAdapter {

    @Override
        public String callService(Request request) {
            try {
                String umsString = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject obj  = (JSONObject) new JSONParser().parse(umsString);
                Long code = (Long) obj.get(ParamKey.ERROR_CODE);
                if(code == ErrorCode.SUCCESS){
                    
                    JSONArray friendList = (JSONArray) obj.get(ParamKey.DATA);
                    if(friendList != null && !friendList.isEmpty()){
                        for (Object toUserId : friendList) {
                            if(NotificationSettingDAO.checkUserNotification((String)toUserId , Constant.NOTIFICATION_TYPE_VALUE.DENIED_USER_INFO_NOTI)){
                                Util.addDebugLog("=============vao if: "+toUserId);
                                JSONArray sendFriendList = new JSONArray();
                                sendFriendList.add(toUserId);
                                request.reqObj.put(ParamKey.FRIEND_LIST, sendFriendList);
                                request.reqObj.put(ParamKey.PUSH_OFFLINE, 0);
                                request.put(ParamKey.API_NAME, API.PUSH_NOTIFICATION_FROM_FREE_PAGE);
                                 Util.addDebugLog("=============Data json: "+request.toJson());
                                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                            } else {
                                Util.addDebugLog("=============vao else: "+toUserId);
                                JSONArray sendFriendList = new JSONArray();
                                sendFriendList.add(toUserId);
                                request.reqObj.put(ParamKey.FRIEND_LIST, sendFriendList);
                                request.reqObj.put(ParamKey.PUSH_OFFLINE, 1);
                                request.put(ParamKey.API_NAME, API.PUSH_NOTIFICATION_FROM_FREE_PAGE);
                                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                            }
                        }
                    }
                }
                Util.addDebugLog("=============PushFromBackEndAdapter OOOOOOOOOOOOOOOKKKKKKKKKKKKKKKKKKKKKK: ");
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.SuccessMessage;
        }
        
    }    
    
    private static class PushFromBackEndNewsAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                 Util.addDebugLog("vao PushFromBackEndNewsAdapter " + request.toJson());
                String umsString = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject obj  = (JSONObject) new JSONParser().parse(umsString);
                Long code = (Long) obj.get(ParamKey.ERROR_CODE);
                 Util.addDebugLog("vao PushFromBackEndNewsAdapter " + code);
                if(code == ErrorCode.SUCCESS){
                    JSONArray friendList = (JSONArray) obj.get(ParamKey.DATA);
                    if(friendList != null && !friendList.isEmpty()){
                        request.reqObj.put(ParamKey.FRIEND_LIST, friendList);
                        request.put(ParamKey.API_NAME, API.PUSH_NOTIFICATION);
                        InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.SuccessMessage;
        }
    } 
    
}


