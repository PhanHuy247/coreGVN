/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.eazyserver.adapter.impl;


import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.servicemanagement.MixService;


/**
 *
 * @author HuyDX
 */
abstract class AbstractCall implements IServiceAdapter{

    @Override
    public String callService(Request request) {
        String respond;
        String IP = (String) request.getParamValue(ParamKey.IP);
        if (testSipIp(IP)){
            respond = doService(request);
        }else
            respond = ResponseMessage.BadResquestMessage;
        return respond;
    }
    
    public boolean testSipIp(String ip){
        return Config.SIP_SERVER_IP.contains(ip);
    }
    
    public abstract String doService(Request request);
    
}

public class SipAdapter implements IServiceAdapter {

    public static final MakeCallAdapter makeCallAdapter = new MakeCallAdapter();
    public static final CallPaymentAdapter callPaymentAdapter = new CallPaymentAdapter();
    public static final StartCallAdapter startCallAdapter = new StartCallAdapter();
    public static final EndCallAdapter endCallAdapter = new EndCallAdapter();

    @Override
    public String callService(Request request) {
        MixService ms = new MixService();
        String result = ms.callApi(request);
        return result;        
    }
    
    private static class MakeCallAdapter extends AbstractCall{

        @Override
        public String doService(Request request) {
            MixService ms = new MixService();            
            try{
                String result = ms.callApi(request);
                try {
                    JSONObject jo = (JSONObject) new JSONParser().parse(result);
                    //DuongLTD
                    Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                    if (code != ErrorCode.SUCCESS) {
                        return result;
                    }
                    InterCommunicator.sendRequest(request.toString(), Config.ChatServerIP, Config.ChatServerPort);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    result = ResponseMessage.UnknownError;
                }
                return result;
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
        
    }
    
    private static class CallPaymentAdapter extends AbstractCall {

        @Override
        public String doService(Request request) {
            
            String requestStr = request.toJson();
            String result;
            try {
                result = InterCommunicator.sendRequest(requestStr, Config.UMSServerIP, Config.UMSPort);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;
            }
            return result;
        }
        
    }
    
    private static class StartCallAdapter extends AbstractCall {
        
        @Override
        public String doService(Request request) {
            MixService ms = new MixService();            
            try{
                String result = ms.callApi(request);
                try {
                    JSONObject jo = (JSONObject) new JSONParser().parse(result);
                    //DuongLTD
                    Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                    if (code != ErrorCode.SUCCESS) {
                        return result;
                    }
                    InterCommunicator.sendRequest(request.toString(), Config.ChatServerIP, Config.ChatServerPort);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    result = ResponseMessage.UnknownError;
                }
                return result;
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
        
    }
    
    private static class EndCallAdapter extends AbstractCall{

        @Override
        public String doService(Request request) {
            
            MixService ms = new MixService();            
            try{
                String result = ms.callApi(request);
                try {
                    JSONObject jo = (JSONObject) new JSONParser().parse(result);
                    //DuongLTD
                    Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                    if (code != ErrorCode.SUCCESS) {
                        return result;
                    }
//                    InterCommunicator.sendRequest(request.toString(), ServerConfig.UMS_SERVER_IP, ServerConfig.UMS_SERVER_PORT);
                    InterCommunicator.sendRequest(request.toString(), Config.ChatServerIP, Config.ChatServerPort);
                    
                    String toUser = (String) request.reqObj.get("to");
                    String fromUser = (String) request.reqObj.get("from");
                    String ip = (String) request.reqObj.get("ip");
                    Long partnerRespondStr = (Long) request.getParamValue("finish_type");
                    request.reqObj.put(ParamKey.API_NAME, API.GET_MY_PAGE_INFOR);
                    request.reqObj.put(ParamKey.USER_ID, toUser);
                    String umsResult = InterCommunicator.sendRequest(request.toString(), Config.UMSServerIP, Config.UMSPort);
                    Util.addDebugLog("========== umsResult: "+umsResult);
                    JSONObject unreadNotiObj = Util.toJSONObject(umsResult);
                    JSONObject unreadNoti = (JSONObject) unreadNotiObj.get("data");
                    long notiLikeNum = (Long) unreadNoti.get("noti_like_num");
                    long notiNum = (Long) unreadNoti.get("noti_num");
                    long notiQANum = (Long) unreadNoti.get("noti_qa_num");
                    long notiNewsNum = (Long) unreadNoti.get("noti_news_num");
                    request.reqObj.put(ParamKey.API_NAME, API.TOTAL_UNREAD);
                    String chatResult = InterCommunicator.sendRequest(request.toString(), Config.ChatServerIP, Config.ChatServerPort);
                    Util.addDebugLog("========== chatResult: "+chatResult);
                    int unreadMessage = new Integer(chatResult);
                    
                    long badge = notiLikeNum + notiNum + notiQANum + notiNewsNum + unreadMessage;
                    Util.addDebugLog("========== total badge: "+badge);
                    
                    if (partnerRespondStr == Constant.END_CALL_VALUE.CANCEL || partnerRespondStr == Constant.END_CALL_VALUE.BUSY){
                    sendJPNSWithEndCall(fromUser, toUser, API.NOTI_MISS_CALL,(int) badge, ip);
                    }
                    
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    result = ResponseMessage.UnknownError;
                }
                return result;
                
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
       
        
        private static void sendJPNSWithEndCall(String fromUserid, String toUserid, String api, int badge,String ip) {

        JSONObject jo = new JSONObject();
        jo.put(ParamKey.FROM_USER_ID, fromUserid);
        jo.put(ParamKey.TOUSERID, toUserid);
        jo.put(ParamKey.API_NAME, api);
//        jo.put(ParamKey.BADGE, badge + 1);
        jo.put(ParamKey.BADGE, badge);
        jo.put(ParamKey.IP, ip);

        String msg = jo.toJSONString();
        Util.sendRequest(msg, Config.NotificationServerIP, Config.NotificationPort);
        }
    }
}