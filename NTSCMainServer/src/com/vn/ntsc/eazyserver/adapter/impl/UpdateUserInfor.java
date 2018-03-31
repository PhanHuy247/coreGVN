/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.entity.impl.FileUrl;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author RuAc0n
 */
public class UpdateUserInfor implements IServiceAdapter {

       @Override
        public String callService( Request request ) {
            String result;
            try {
                result = InterCommunicator.sendRequest( request.toJson(), Config.UMSServerIP, Config.UMSPort );
                JSONObject json = (JSONObject) new JSONParser().parse(result);
                Long code = (Long) json.get(ParamKey.ERROR_CODE);
                if(code == ErrorCode.SUCCESS){
                    updateUserInfor(request, json);
                    result = json.toJSONString();
                }
            } catch( Exception ex ) {
                Util.addErrorLog(ex);    
                result = ResponseMessage.UnknownError;
            }
            return result;
        }

        public static void updateUserInfor( Request request, JSONObject json) {
            try {
                String api = (String) request.getParamValue(ParamKey.API_NAME);
                String token = request.token;
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                String ip = (String) request.getParamValue(ParamKey.IP);
                request.put(ParamKey.API_NAME, API.UPDATE_USER_INFOR);
                JSONObject joData = (JSONObject) json.get(ParamKey.DATA);
                if(request.api.equals(API.UPDATE_USER_INFOR)){
                    JSONObject data = (JSONObject) json.get(ParamKey.DATA);
                    String email = (String) data.get(ParamKey.EMAIL);
                    //Linh #10080 get avatar url
                    String avaId = (String) data.get(ParamKey.AVATAR_ID);
                    if (avaId != null){
                        List<String> listImgId = new LinkedList<>();
                        listImgId.add(avaId);
                        HashMap<String, FileUrl> imgMap = InterCommunicator.getImage(listImgId);
                        FileUrl url = imgMap.get(avaId);
                        if (url != null){
                            data.put(ParamKey.THUMBNAIL_URL, url.getThumbnail());
                            data.put(ParamKey.ORIGINAL_URL, url.getOriginalUrl());
                        }
                    }
                    
                    Long isNoti = (Long) data.get(ParamKey.IS_NOTI);
                    if(isNoti != null && isNoti == Constant.FLAG.ON){
                        JSONObject notiRequest = new JSONObject();
                        notiRequest.put(ParamKey.API_NAME, API.REVIEW_USER);
                        notiRequest.put(ParamKey.TOUSERID, userId);
                        notiRequest.put("review_result", Constant.REVIEW_STATUS_FLAG.APPROVED);
                        notiRequest.put(ParamKey.IP, ip);
                        InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                    }
                    Long removeFlag = (Long) data.get(ParamKey.IS_FINISH_REGISTER);
                    Boolean isVerify = (Boolean) data.get(ParamKey.IS_VERIFY);
                    if(removeFlag != null && removeFlag == Constant.FLAG.ON && isVerify != null && isVerify){

                        List<Session> listSession = SessionManager.removeSessionsOfUserExcudeToken(token);
                        try{
                            UserSessionDAO.remove(listSession);
                        }catch(EazyException ex){
                            Util.addErrorLog(ex);
                        }
                        joData.put(ParamKey.EMAIL, userId);
                        joData.put(ParamKey.API_NAME, API.REGISTER);
                        InterCommunicator.sendRequest(joData.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort); 
//                        updateOnlineStatus(userId);
                        joData.put(ParamKey.EMAIL, email);
                        Session sess = SessionManager.getSession(token);
                        sess.normalUser = true;
                        UserSessionDAO.update(sess);
                        
                        //                     add daily bonus Notification
                        Long addPoint = (Long) joData.get(ParamKey.ADD_POINT);
                        if(addPoint != null && addPoint > 0){

                            JSONObject notiRequest = new JSONObject();
                            notiRequest.put(ParamKey.API_NAME, API.NOTI_DAILY_BONUS);
                            notiRequest.put(ParamKey.NOTI_USER_ID, userId);
                            notiRequest.put(ParamKey.POINT, addPoint.toString());
                            notiRequest.put(ParamKey.IP, ip);
                            InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                            joData.remove(ParamKey.ADD_POINT);
                        }
                    }
                    InterCommunicator.sendRequest( request.toJson(), Config.NotificationServerIP, Config.NotificationPort );
                }else{
                    if(!api.equals(API.UPDATE_AVATAR))
                        json.remove(ParamKey.DATA);
                }
                InterCommunicator.sendRequest( request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort );
                
            } catch( Exception ex ) {
                Util.addErrorLog(ex);                
            }
        }
           

}
