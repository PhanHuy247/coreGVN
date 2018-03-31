/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl.backend.impl;

import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.impl.backend.IServiceBackendAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Phan Huy
 */
public class ReviewVideoAdapter implements IServiceBackendAdapter{

    @Override
    public String callService(Request request) {
        String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
        try{
            JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
            Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
            JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
            if (code == ErrorCode.SUCCESS) {
                String data = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject json = (JSONObject) (new JSONParser().parse(data));
                JSONObject jsonData = (JSONObject) json.get(ParamKey.DATA);
                Long isNoti = (Long) jsonData.get(ParamKey.IS_NOTI);
                String buzzId = (String) jsonData.get(ParamKey.BUZZ_ID);
                String userId = (String) jsonData.get(ParamKey.BUZZ_OWNER_ID);
                String fileId = (String) jsonData.get(ParamKey.VIDEO_ID);
                String ip = (String) joData.get(ParamKey.IP);
                Util.addDebugLog("isNoti======"+isNoti);
                if(isNoti != null && isNoti == Constant.FLAG.ON){
                    Long videoStatus = (Long) request.getParamValue(ParamKey.VIDEO_STATUS);
                    Util.addDebugLog("videoStatus======"+videoStatus);
                    if(videoStatus == Constant.REVIEW_STATUS_FLAG.APPROVED){
                        request.put(ParamKey.BUZZ_ID, buzzId);
                        request.put(ParamKey.FILE_ID, fileId);
                        request.put(ParamKey.USER_ID, userId);
                        request.put(ParamKey.API_NAME, API.APPROVED_FILE);
                        request.put(ParamKey.IP, ip);
                        String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                        Util.addDebugLog("ums======"+ums);
                        JSONObject umsObj = (JSONObject) new JSONParser().parse(ums);
                        Long codeUms = (Long) umsObj.get(ParamKey.ERROR_CODE);
                        if (codeUms == ErrorCode.SUCCESS) {
                            JSONObject dataUms = (JSONObject) umsObj.get(ParamKey.DATA);
                            Long isNotification = (Long) dataUms.get(ParamKey.IS_NOTI);
                            Util.addDebugLog("isNotification======"+isNotification);
                            if (isNotification != null && isNotification == Constant.FLAG.ON) {
                                request.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                request.put(ParamKey.API_NAME, API.NOTI_BUZZ_APPROVED);
                                request.put(ParamKey.TOUSERID, userId);
                                request.put(ParamKey.NOTI_IMAGE_ID, fileId);
                                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                            }
                            JSONArray listFvt = (JSONArray) dataUms.get(ParamKey.FAVORITED_LIST);
                            if(listFvt != null && !listFvt.isEmpty()){
                                String userName = (String) dataUms.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
                                Util.addDebugLog("========Username ReviewImageAdapter===:" +userName);
                                JSONObject objNoti = new JSONObject();
                                objNoti.put(ParamKey.API_NAME, API.NOTI_NEW_BUZZ_FROM_FAVORIST);
                                objNoti.put(ParamKey.USER_NAME, userName);
                                objNoti.put(ParamKey.FROM_USER_ID, userId);
                                objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
                                objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);

                                objNoti.put(ParamKey.IP, ip);

                                InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                            }
                        }
                    }else if(videoStatus == Constant.REVIEW_STATUS_FLAG.DENIED){
                        request.put(ParamKey.BUZZ_ID, buzzId);
                        request.put(ParamKey.FILE_ID, fileId);
                        request.put(ParamKey.USER_ID, userId);
                        request.put(ParamKey.API_NAME, API.DENIED_FILE);
                        request.put(ParamKey.IP, ip);
                        String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                        JSONObject umsObj = (JSONObject) new JSONParser().parse(ums);
                        Long codeUms = (Long) umsObj.get(ParamKey.ERROR_CODE);
                        if (codeUms == ErrorCode.SUCCESS) {
                            JSONObject dataUms = (JSONObject) umsObj.get(ParamKey.DATA);
                            Long isNotification = (Long) dataUms.get(ParamKey.IS_NOTI);
                            Util.addDebugLog("isNotification======"+isNotification);
                            if (isNotification != null && isNotification == Constant.FLAG.ON) {
                                request.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                request.put(ParamKey.API_NAME, API.NOTI_BUZZ_DENIED);
                                request.put(ParamKey.TOUSERID, userId);
                                request.put(ParamKey.NOTI_IMAGE_ID, fileId);
                                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                            }
                        }
                    }
                }
                
                result = data;
            }
        }catch(ParseException ex){
            Util.addErrorLog(ex);
        }
       return result;
    }
    
    
}
