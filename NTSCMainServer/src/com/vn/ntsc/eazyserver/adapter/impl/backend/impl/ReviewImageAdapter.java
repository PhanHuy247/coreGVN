/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl.backend.impl;

import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.impl.backend.IServiceBackendAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author RuAc0n
 */
public class ReviewImageAdapter implements IServiceBackendAdapter {

    public static final DeleteImage deleteImage = new DeleteImage();
    public static final ProcessReportFile processReportFile = new ProcessReportFile();

    @Override
    public String callService(Request request) {
        String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
        try {
            JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
            Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
            JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
            if (code == ErrorCode.SUCCESS) {
                String buzzId = (String) joData.get(ParamKey.BUZZ_ID);
                String imageId = (String) joData.get(ParamKey.IMAGE_ID);
                String userId = (String) joData.get(ParamKey.USER_ID);
                String ip = (String) joData.get(ParamKey.IP);
                Long avatar = (Long) joData.get(ParamKey.AVATAR);
                if (avatar != null && avatar != 0) {
                    imageId = Constant.NO_AVATAR_STRING_VALUE;
                    if (avatar == 1) {
                        imageId = (String) joData.get(ParamKey.IMAGE_ID);
                    }
                    request.put(ParamKey.USER_ID, userId);
                    request.put(ParamKey.API_NAME, API.UPDATE_USER_INFOR);
                    request.put(ParamKey.IMAGE_ID, imageId);
                    InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                }
                Long notify = (Long) joData.get("notify");
                if (notify != null && notify == Constant.FLAG.ON) {
                    Long type = (Long) request.getParamValue(ParamKey.TYPE);
                    if(type == Constant.REVIEW_STATUS_FLAG.APPROVED){
                        request.put(ParamKey.BUZZ_ID, buzzId);
                        request.put(ParamKey.IMAGE_ID, imageId);
                        request.put(ParamKey.USER_ID, userId);
                        request.put(ParamKey.API_NAME, API.APPROVED_IMAGE);
                        request.reqObj.put(ParamKey.IS_AVATAR, avatar);
                        request.put(ParamKey.IP, ip);
                        String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                        JSONObject umsObj = (JSONObject) new JSONParser().parse(ums);
                        Long codeUms = (Long) umsObj.get(ParamKey.ERROR_CODE);
                        if (codeUms == ErrorCode.SUCCESS) {
                            JSONObject data = (JSONObject) umsObj.get(ParamKey.DATA);
                            Long isNoti = (Long) data.get(ParamKey.IS_NOTI);
                            if (isNoti != null && isNoti == Constant.FLAG.ON) {
                                if (buzzId == null) {
                                    request.put(ParamKey.API_NAME, API.NOTI_BACKSTAGE_APPROVE);
                                } else {
                                    request.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    request.put(ParamKey.API_NAME, API.NOTI_BUZZ_APPROVED);
                                }
                                request.put(ParamKey.TOUSERID, userId);
                                request.put(ParamKey.NOTI_IMAGE_ID, imageId);
                                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                            }
                            JSONArray listFvt = (JSONArray) data.get(ParamKey.FAVORITED_LIST);
                            if(listFvt != null && !listFvt.isEmpty()){
                                String userName = (String) data.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
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
                    }else if(type == Constant.REVIEW_STATUS_FLAG.DENIED){
                        request.put(ParamKey.BUZZ_ID, buzzId);
                        request.put(ParamKey.IMAGE_ID, imageId);
                        request.put(ParamKey.USER_ID, userId);
                        request.put(ParamKey.API_NAME, API.DENIED_IMAGE);
                        request.reqObj.put(ParamKey.IS_AVATAR, avatar);
                        request.put(ParamKey.IP, ip);
                        String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                        JSONObject umsObj = (JSONObject) new JSONParser().parse(ums);
                        Long codeUms = (Long) umsObj.get(ParamKey.ERROR_CODE);
                        if (codeUms == ErrorCode.SUCCESS) {
                            JSONObject data = (JSONObject) umsObj.get(ParamKey.DATA);
                            Long isNoti = (Long) data.get(ParamKey.IS_NOTI);
                            if (isNoti != null && isNoti == Constant.FLAG.ON) {
                                if (buzzId == null) {
                                    request.put(ParamKey.API_NAME, API.NOTI_BACKSTAGE_DENIED);
                                } else {
                                    request.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    request.put(ParamKey.API_NAME, API.NOTI_BUZZ_DENIED);
                                }
                                request.put(ParamKey.TOUSERID, userId);
                                request.put(ParamKey.NOTI_IMAGE_ID, imageId);
                                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                            }
                        }
                    }
                }
                jsonResult.remove(ParamKey.DATA);
                result = jsonResult.toJSONString();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
        }
        return result;
    }

    public static class DeleteImage implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
                if (code == ErrorCode.SUCCESS) {
                    Long avatar = (Long) joData.get(ParamKey.AVATAR);
                    if (avatar != null && avatar == -1) {
                        String imageId = Constant.NO_AVATAR_STRING_VALUE;
                        String userId = (String) joData.get(ParamKey.USER_ID);
                        request.put(ParamKey.USER_ID, userId);
                        request.put(ParamKey.API_NAME, API.UPDATE_USER_INFOR);
                        request.put(ParamKey.IMAGE_ID, imageId);
                        InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                    }
                    jsonResult.remove(ParamKey.DATA);
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);                 
               
            }
            return result;
        }
    }
    
    public static class ProcessReportFile implements IServiceBackendAdapter{

        @Override
        public String callService(Request request) {
            return InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
        }
        
    }

}
