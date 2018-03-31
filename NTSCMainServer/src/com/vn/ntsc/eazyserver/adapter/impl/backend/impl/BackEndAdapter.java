/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl.backend.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.Setting;
import com.vn.ntsc.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.eazyserver.adapter.impl.backend.IServiceBackendAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.servicemanagement.MixService;
import com.vn.ntsc.otherservice.statistic.UserCounter;

/**
 *
 * @author RuAc0n
 */
public class BackEndAdapter implements IServiceBackendAdapter {

    public static final LoginAdmin loginAdmin = new LoginAdmin();
    public static final ChangePass changePass = new ChangePass();
    public static final UpdateAdmin updateAdmin = new UpdateAdmin();
    public static final UpdateRole updateRole = new UpdateRole();
    public static final GetUserOnline getUserOnline = new GetUserOnline();
    public static final SetDistanceSetting setDistanceSetting = new SetDistanceSetting();
    public static final SetPointSetting setPointSetting = new SetPointSetting();
    public static final SetCommunicationSetting setCommunicationSetting = new SetCommunicationSetting();
    public static final SetConnectionPointSetting setConnectionPointSetting = new SetConnectionPointSetting();
    public static final SetOtherSetting setOtherSetting = new SetOtherSetting();
    public static final SetOtherSettingApp setOtherSettingApp = new SetOtherSettingApp();
    public static final SetVersionSetting setVersionSetting = new SetVersionSetting();
    public static final StaticPage staticPage = new StaticPage();
    public static final UpdateUserInfor updateUserInfor = new UpdateUserInfor();
    public static final RegisterByAdmin registerByAdmin = new RegisterByAdmin();
    public static final DeleteBuzz deleteBuzz = new DeleteBuzz();
    public static final InsertStamp insertStamp = new InsertStamp();
    public static final UpdateStampImage updateStampImage = new UpdateStampImage();
    public static final AddPoint addPoint = new AddPoint();
    public static final AddPointByList addPointByList = new AddPointByList();
    public static final ReviewBuzz reviewBuzz = new ReviewBuzz();
    public static final ReviewComment reviewComment = new ReviewComment();
    public static final ReviewSubComment reviewSubComment = new ReviewSubComment();
    public static final ReviewUser reviewUser = new ReviewUser();
    public static final AddPurchaseByAdmin addPurchaseByAdmin = new AddPurchaseByAdmin();//LongLT 8/8/2016
    public static final SetUploadSetting setUploadSetting = new SetUploadSetting();
    public static final SetPrioritizeUserBuzzSetting setPrioritizeUserBuzzSetting = new SetPrioritizeUserBuzzSetting();

    @Override
    public String callService(Request request) {
        return InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
    }
    
    // LongLT 8/8/2016
    public static class AddPurchaseByAdmin implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            JSONObject json;
            try {
                json = (JSONObject) new JSONParser().parse(result);
                Long code = (Long) json.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    return InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }

    public static class ReviewUser implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);

                    if(joData != null){
                        joData.put(ParamKey.API_NAME, API.REVIEW_USER);
                        String umsResult = InterCommunicator.sendRequest(joData.toJSONString(), Config.UMSServerIP, Config.UMSPort);
                        JSONObject umsJson = (JSONObject) (new JSONParser().parse(umsResult));
                        code = (Long) umsJson.get(ParamKey.ERROR_CODE);
                        if(code == ErrorCode.SUCCESS){
                            String from = (String) joData.get("user_id");
                            Long reviewResult = (Long) joData.get("review_result");
                            String ip = (String) request.getParamValue(ParamKey.IP);
                            JSONObject umsData = (JSONObject) umsJson.get(ParamKey.DATA);
                            Long isNoti = (Long) umsData.get("is_noti");
                           // if(isNoti == Constant.FLAG.ON){
                            if(1 == 1){
                                JSONObject notiRequest = new JSONObject();
                                notiRequest.put(ParamKey.API_NAME, API.REVIEW_USER);
                                notiRequest.put(ParamKey.TOUSERID, from);
                                notiRequest.put("review_result", reviewResult);
                                notiRequest.put(ParamKey.IP, ip);
                                InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                            }
                        }
                        jsonResult.remove(ParamKey.DATA);
                        result = jsonResult.toJSONString();
                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }
    
    public static class ReviewSubComment implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                Long type = (Long) request.getParamValue(ParamKey.TYPE);
                if (code == ErrorCode.SUCCESS) {
                    JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);

                    if(joData != null){
                        joData.put(ParamKey.API_NAME, API.REVIEW_SUB_COMMENT);
                        joData.put(ParamKey.TYPE, type);
                        String umsResult = InterCommunicator.sendRequest(joData.toJSONString(), Config.UMSServerIP, Config.UMSPort);
                        JSONObject umsJson = (JSONObject) (new JSONParser().parse(umsResult));
                        code = (Long) umsJson.get(ParamKey.ERROR_CODE);
                        if(code == ErrorCode.SUCCESS){
                            String from = (String) joData.get("sub_commentor");
                            String buzzId = (String) joData.get("buzz_id");
                            String ip = (String) request.getParamValue(ParamKey.IP);
                            JSONObject umsData = (JSONObject) umsJson.get(ParamKey.DATA);
                            Long isNotiReviewSubComment = (Long) umsData.get("is_noti_review_sub_comment");
                            //if(type == Constant.FLAG.ON){
                            if(1 == 1){    
                                JSONObject notiRequest = new JSONObject();
                                if(isNotiReviewSubComment == Constant.FLAG.ON && NotificationSettingDAO.checkUserNotification(from, Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT)){
                                    notiRequest.put(ParamKey.API_NAME, API.NOTI_SUB_COMMENT_APPROVED);
                                    notiRequest.put(ParamKey.TOUSERID, from);
                                    notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    notiRequest.put(ParamKey.IP, ip);
                                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                }
                                
                                JSONArray notificationList = (JSONArray) umsData.get("notification_list");
                                if(notificationList != null && !notificationList.isEmpty()){
                                    String buzzOwnerName = (String) umsData.get("buzz_owner_name");
                                    notiRequest.put(ParamKey.API_NAME, API.NOTI_REPLY_YOUR_COMMENT);
                                    notiRequest.put(ParamKey.FROM_USER_ID, from);
                                    notiRequest.put("buzz_owner_name", buzzOwnerName);
                                    notiRequest.put(ParamKey.TO_LIST_USER_ID, notificationList);
                                    notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    notiRequest.put(ParamKey.IP, ip);
                                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                }
                            }else{
                                //if(isNotiReviewSubComment == Constant.FLAG.ON){
                                if(1 == 1){
                                    JSONObject notiRequest = new JSONObject();
                                    notiRequest.put(ParamKey.API_NAME, API.NOTI_SUB_COMMENT_DENIED);
                                    notiRequest.put(ParamKey.TOUSERID, from);
                                    notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    notiRequest.put(ParamKey.IP, ip);
                                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                }
                            }
                        }
                        jsonResult.remove(ParamKey.DATA);
                        result = jsonResult.toJSONString();
                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }
    
    public static class ReviewComment implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                Long type = (Long) request.getParamValue(ParamKey.TYPE);
                if (code == ErrorCode.SUCCESS) {
                    JSONArray joArrayData = (JSONArray) jsonResult.get(ParamKey.DATA);
                    JSONObject joData =null;
                    if (joArrayData.size() >0){
                        joData = (JSONObject) joArrayData.get(1);
                    }
                    
                    if(joData != null){
                        joData.put(ParamKey.API_NAME, API.REVIEW_COMMENT);
                        joData.put(ParamKey.TYPE, type);
                        String umsResult = InterCommunicator.sendRequest(joData.toJSONString(), Config.UMSServerIP, Config.UMSPort);
                        JSONObject umsJson = (JSONObject) (new JSONParser().parse(umsResult));
                        code = (Long) umsJson.get(ParamKey.ERROR_CODE);
                        if(code == ErrorCode.SUCCESS){
                            JSONObject umsData = (JSONObject) umsJson.get(ParamKey.DATA);
                            Long isNotiReviewComment = (Long) umsData.get("is_noti_review_comment");
                            String from = (String) joData.get("commentor");
                            String buzzOwner = (String) joData.get("buzz_owner_id");
                            String buzzId = (String) joData.get("buzz_id");
                            String ip = (String) request.getParamValue(ParamKey.IP);
                            if(type == Constant.FLAG.ON){
                            //if(1 == 1){
                                JSONObject notiRequest = new JSONObject();
                                if(isNotiReviewComment == Constant.FLAG.ON){
                                    notiRequest.put(ParamKey.API_NAME, API.NOTI_COMMENT_APPROVED);
                                    notiRequest.put(ParamKey.TOUSERID, from);
                                    notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    notiRequest.put(ParamKey.IP, ip);
                                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                }
                                Long isNotiCommentBuzz = (Long) umsData.get("is_noti_comment_buzz");
                                if(isNotiCommentBuzz == Constant.FLAG.ON){
                                    notiRequest = new JSONObject();
                                    notiRequest.put(ParamKey.API_NAME, API.NOTI_COMMENT_YOUR_BUZZ);
                                    notiRequest.put(ParamKey.FROM_USER_ID, from);
                                    notiRequest.put(ParamKey.TOUSERID, buzzOwner);
                                    notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    notiRequest.put(ParamKey.IP, ip);
                                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                }
                            }else{
                                if(isNotiReviewComment == Constant.FLAG.ON){
                                //if(1 == 1){    
                                    JSONObject notiRequest = new JSONObject();
                                    notiRequest.put(ParamKey.API_NAME, API.NOTI_COMMENT_DENIED);
                                    notiRequest.put(ParamKey.TOUSERID, from);
                                    notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    notiRequest.put(ParamKey.IP, ip);
                                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                }
                            }
                            jsonResult.remove(ParamKey.DATA);
                            result = jsonResult.toJSONString();
                        }
                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }
    
    public static class ReviewBuzz implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                Long type = (Long) request.getParamValue(ParamKey.TYPE);
                //thanhdd edit 20/10
                if (code == ErrorCode.SUCCESS) {
                    JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
                    Util.addDebugLog("===========joData============"+joData);
                    if(joData != null){
                        joData.put(ParamKey.API_NAME, API.REVIEW_BUZZ);
                        joData.put(ParamKey.TYPE, type);
                        String umsResult = InterCommunicator.sendRequest(joData.toJSONString(), Config.UMSServerIP, Config.UMSPort);
                        JSONObject umsJson = (JSONObject) (new JSONParser().parse(umsResult));
                        code = (Long) umsJson.get(ParamKey.ERROR_CODE);
                        if(code == ErrorCode.SUCCESS){
                            JSONObject umsData = (JSONObject) umsJson.get(ParamKey.DATA);
                            Long isNoti = (Long) umsData.get("is_noti");
                            String buzzOwner = (String) joData.get("buzz_owner_id");
                            String buzzId = (String) joData.get("buzz_id");
                            String ip = (String) request.getParamValue(ParamKey.IP);
                            String userName = (String) umsData.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
                            
                            if(type == Constant.REVIEW_STATUS_FLAG.APPROVED){
                                JSONObject notiRequest = new JSONObject();
                                if(isNoti == Constant.FLAG.ON){
                                    Util.addDebugLog("===========isNoti============"+isNoti);
                                    notiRequest.put(ParamKey.API_NAME, API.NOTI_TEXT_BUZZ_APPROVED);
                                    notiRequest.put(ParamKey.TOUSERID, buzzOwner);
                                    notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    notiRequest.put(ParamKey.IP, ip);
                                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                    Util.addDebugLog("===========Config.NotificationServerIP11111111111============ SUCCESS");
                                }
                                JSONArray listFvt = (JSONArray) umsData.get(ParamKey.FAVORITED_LIST);
                                if(listFvt != null){
                                    JSONObject objNoti = new JSONObject();
                                    objNoti.put(ParamKey.API_NAME, API.NOTI_NEW_BUZZ_FROM_FAVORIST);
                                    objNoti.put(ParamKey.USER_NAME, userName);
                                    objNoti.put(ParamKey.FROM_USER_ID, buzzOwner);
                                    objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
                                    objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    objNoti.put(ParamKey.IP, ip);
                                    InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                    Util.addDebugLog("===========Config.NotificationServerIP222222222222222222222222============ SUCCESS");
                                }
                            }else{
                                if(isNoti == Constant.FLAG.ON){
                                    JSONObject notiRequest = new JSONObject();
                                    notiRequest.put(ParamKey.API_NAME, API.NOTI_TEXT_BUZZ_DENIED);
                                    notiRequest.put(ParamKey.TOUSERID, buzzOwner);
                                    notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    notiRequest.put(ParamKey.IP, ip);
                                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                    Util.addDebugLog("===========Config.NotificationServerIP333333333333============ SUCCESS");
                                }
                            }

                        }
                        jsonResult.remove(ParamKey.DATA);
                        result = jsonResult.toJSONString();
                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }
    
    public static class LoginAdmin implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
                if (code == ErrorCode.SUCCESS) {
                    String userId = (String) joData.get(ParamKey.USER_ID);
                    String type = (String) joData.get(ParamKey.ROLE_ID);
                    Session session = new Session(userId, type);
                    SessionManager.putSession(session);
                    joData.put(ParamKey.TOKEN_STRING, session.token);
                    joData.remove(ParamKey.USER_ID);
                    result = jsonResult.toJSONString();
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }

    public static class ChangePass implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String token = request.token;
            String userId = (String) request.getParamValue(ParamKey.USER_ID);
            
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            JSONObject json;
            try {
                json = (JSONObject) new JSONParser().parse(result);
                Long code = (Long) json.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    SessionManager.removeSessionsOfUserExcudeToken(token);
                    SessionManager.removeSessionsByUserId(userId);

                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }

    public static class AddPoint implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            JSONObject json;
            try {
                json = (JSONObject) new JSONParser().parse(result);
                Long code = (Long) json.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    return InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }
    
    public static class AddPointByList implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            JSONObject json;
            try {
                json = (JSONObject) new JSONParser().parse(result);
                Long code = (Long) json.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    JSONArray listIds = (JSONArray) json.get(ParamKey.DATA);
                    request.reqObj.put("list_ids", listIds);
                    return InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }

    public static class UpdateRole implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            JSONObject json;
            try {
                json = (JSONObject) new JSONParser().parse(result);
                Long code = (Long) json.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    String type = (String) request.getParamValue(ParamKey.ID);
                    SessionManager.removeSessionsOfGroupUser(type);
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
            return result;
        }
    }

    public static class UpdateAdmin implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            JSONObject json;
            try {
                json = (JSONObject) new JSONParser().parse(result);
                Long code = (Long) json.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    String userId = (String) request.getParamValue(ParamKey.ID);
                    List<Session> listSession = SessionManager.removeSessionsByUserId(userId);
                    try {
                        UserSessionDAO.remove(listSession);
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
            return result;
        }
    }

    public static class GetUserOnline implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result;
            try {
                String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
                Long skip = (Long) request.getParamValue(ParamKey.SKIP);
                Long take = (Long) request.getParamValue(ParamKey.TAKE);
                if (skip == null || take == null) {
                    return ResponseMessage.BadResquestMessage;
                }
                String meetResult = InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                JSONObject json = (JSONObject) new JSONParser().parse(meetResult);
                json.put(ParamKey.API_NAME, API.GET_USER_ONLINE);
                json.put(ParamKey.USER_ID, userId);
                result = InterCommunicator.sendRequest(json.toJSONString(), Config.BackEndServerIp, Config.BackEndServerPort);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
            return result;
        }
    }

    public static class SetDistanceSetting implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    request.put(ParamKey.API_NAME, API.UPDATE_DISTANCE);
                    InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                    JSONObject requestPre = new JSONObject();
                    List<Double> dis = new ArrayList<>();
                    Double near = Double.parseDouble(request.getParamValue("near").toString());
                    dis.add(near);
                    Double city = Double.parseDouble(request.getParamValue("city").toString());
                    dis.add(city);
                    Double stage = (double) 200;
                    dis.add(stage);
                    Double country = Double.parseDouble(request.getParamValue("country").toString());
                    dis.add(country);
                    Double localBuzz = Double.parseDouble(request.getParamValue("local_buzz").toString());
                    requestPre.put(ParamKey.API_NAME, API.UPDATE_DISTANCE);
                    requestPre.put(ParamKey.DISTANCE, dis);
                    requestPre.put("local_buzz", localBuzz);
                    InterCommunicator.sendRequest(requestPre.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
//                    SessionManager.changeSettingAllUser(true);
                    SessionManager.updateInUseAllUser(false);
//                    try {
//                        UserSessionDAO.remove(listSession);
//                    } catch (DaoException ex) {
//                        Util.addErrorLog(ex);
//                    }
//                    List<String> list = new ArrayList<String>();
//                    for (Session session : listSession) {
//                        list.add(session.userID);
//                    }
//                    InterCommunicator.resetOrDeletPresentation(list);
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

                result = ResponseMessage.UnknownError;
            }
            return result;
        }
    }
    
    public static class SetOtherSettingApp implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            return result;
        }
    }

    public static class SetPointSetting implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
//                    List<Session> listSession = SessionManager.removeSessionsOfGroupUser(null);
//                    SessionManager.changeSettingAllUser(true);
                    SessionManager.updateInUseAllUser(false);
//                    try {
//                        UserSessionDAO.remove(listSession);
//                    } catch (DaoException ex) {
//                        Util.addErrorLog(ex);
//                    }
//                    List<String> list = new ArrayList<String>();
//                    for (Session session : listSession) {
//                        list.add(session.userID);
//                    }
//                    InterCommunicator.resetOrDeletPresentation(list);
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

                result = ResponseMessage.UnknownError;
            }
            return result;
        }
    }

    public static class SetCommunicationSetting implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
//                    List<Session> listSession = SessionManager.removeSessionsOfGroupUser(null);
//                    SessionManager.changeSettingAllUser(true);
                    SessionManager.updateInUseAllUser(false);
//                    try {
//                        UserSessionDAO.remove(listSession);
//                    } catch (DaoException ex) {
//                        Util.addErrorLog(ex);
//                    }
//                    List<String> list = new ArrayList<String>();
//                    for (Session session : listSession) {
//                        list.add(session.userID);
//                    }
//                    InterCommunicator.resetOrDeletPresentation(list);
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

                result = ResponseMessage.UnknownError;
            }
            return result;
        }
    }

    public static class SetConnectionPointSetting implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
//                    List<Session> listSession = SessionManager.removeSessionsOfGroupUser(null);
//                    SessionManager.changeSettingAllUser(true);
                    SessionManager.updateInUseAllUser(false);
//                    try {
//                        UserSessionDAO.remove(listSession);
//                    } catch (DaoException ex) {
//                        Util.addErrorLog(ex);
//                    }
//                    List<String> list = new ArrayList<String>();
//                    for (Session session : listSession) {
//                        list.add(session.userID);
//                    }
//                    InterCommunicator.resetOrDeletPresentation(list);
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

                result = ResponseMessage.UnknownError;
            }
            return result;
        }
    }

    public static class UpdateStampImage implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    String api = request.api;
                    String imageString = (String) request.getParamValue(ParamKey.IMAGE);
                    return sendRequest(createUrl(api, request), imageString, Config.StaticFileServerIp, Config.StaticFileServerPort);
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

            }
            return result;
        }

        private String createUrl(String api, Request request) {
            StringBuilder url = new StringBuilder(ParamKey.API_NAME);
            url.append("=").append(api);
            if (api.equals(API.UPDATE_GIFT_IMAGE)) {
                String giftId = (String) request.getParamValue(ParamKey.GIFT_ID);
                url.append("&").append(ParamKey.GIFT_ID).append("=").append(giftId);
            } else if (api.equals(API.UPDATE_STICKER_CATEGORY_IMAGE)) {
                String catId = (String) request.getParamValue(ParamKey.ID);
                url.append("&").append("sticker_cat_id").append("=").append(catId);
            } else if (api.equals(API.UPDATE_STICKER_IMAGE)) {
                String stickerIdId = (String) request.getParamValue(ParamKey.ID);
                url.append("&").append("stk_id").append("=").append(stickerIdId);
            }
            return url.toString();
        }

    }

    public static class InsertStamp implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String api = request.api;
            String imageString = (String) request.getParamValue(ParamKey.IMAGE);
            if (imageString != null) {
                String result = sendRequest(createUrl(api), imageString, Config.StaticFileServerIp, Config.StaticFileServerPort);
                try {
                    JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                    Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                    if (code == ErrorCode.SUCCESS) {
                        JSONObject dataObj = (JSONObject) jsonResult.get(ParamKey.DATA);
                        if (dataObj != null) {
                            String id = (String) (dataObj).get(ParamKey.ID);
                            request.put(ParamKey.ID, id);
                            Long stickerCode = (Long) (dataObj).get(ParamKey.ERROR_CODE);
                            if (stickerCode != null) {
                                request.reqObj.put("stk_code", stickerCode);
                            }
                        }
                        return InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
                    }
                } catch (ParseException ex) {
                    Util.addErrorLog(ex);
                }
                return result;
            } else {
                return InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            }
        }

        private String createUrl(String api) {
//            if(api.equals(API.insert_sticker_category_ver_2))
//                api = API.insert_sticker_category;
//            if(api.equals(API.insert_sticker_ver_2))
//                api = API.insert_sticker;
            StringBuilder url = new StringBuilder(ParamKey.API_NAME);
            url.append("=").append(api);
            return url.toString();
        }

    }   
    
    public static class SetOtherSetting implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    Long auto = (Long) request.getParamValue(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_IMAGE);
                    Setting.auto_approved_image = auto.intValue();
                    Long autoApprovedBuzz = (Long) request.getParamValue(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_BUZZ);
                    Setting.auto_approved_buzz = autoApprovedBuzz.intValue();
                    Long autoApprovedComment = (Long) request.getParamValue(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_COMMENT);
                    Setting.auto_approved_comment = autoApprovedComment.intValue();
                    String turnOffSafaryVersion = (String) request.getParamValue(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY_VERSION);
                    Long autoApprovedVideo = (Long) request.getParamValue(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_VIDEO);
                    if (autoApprovedVideo != null) {
                        Setting.auto_approved_video = autoApprovedVideo.intValue();
                    }

                    if (turnOffSafaryVersion != null) {
                        Setting.turnOffSafaryVersion = turnOffSafaryVersion;
                    }
                    Boolean turnOffSafary = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY);
                    if (turnOffSafary != null) {
                        Setting.turnOffSafary = turnOffSafary;
                    }
                    Boolean loginByMocom = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.LOGIN_BY_MOCOM);
                    if (loginByMocom != null) {
                        Setting.turnOffLoginByMocom = loginByMocom;
                    }
                    Boolean extendedUserInfo = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.EXTENDED_USER_INFO);
                    if (extendedUserInfo != null) {
                        Setting.turnOffExtendedUserInfo = extendedUserInfo;
                    }
                    Boolean showNews = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.SHOW_NEWS);
                    if (showNews != null) {
                        Setting.turnOffShowNews = showNews;
                    }
                    Boolean getFreePoint = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.GET_FREE_POINT);
                    if (getFreePoint != null) {
                        Setting.turnOffGetFreePoint = getFreePoint;
                    }
                    
                    String turnOffBrowserVersion = (String) request.getParamValue(SettingdbKey.OTHER_SETTING.TURN_OFF_BROWSER_ANDROID_VERSION);
                    if (turnOffBrowserVersion != null) {
                        Setting.turnOffBrowserAndroidVersion = turnOffBrowserVersion;
                    }
                    Boolean turnOffBrowser = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.TURN_OFF_BROWSER_ANDROID);
                    if (turnOffBrowser != null) {
                        Setting.turnOffBrowserAndroid = turnOffBrowser;
                    }
                    Boolean loginByMocomAndroid = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.LOGIN_BY_MOCOM_ANDROID);
                    if (loginByMocomAndroid != null) {
                        Setting.turnOffLoginByMocomAndroid = loginByMocomAndroid;
                    }
                    Boolean extendedUserInfoAndroid = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.EXTENDED_USER_INFO_ANDROID);
                    if (extendedUserInfoAndroid != null) {
                        Setting.turnOffExtendedUserInfoAndroid = extendedUserInfoAndroid;
                    }
                    Boolean showNewsAndroid = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.SHOW_NEWS_ANDROID);
                    if (showNewsAndroid != null) {
                        Setting.turnOffShowNewsAndroid = showNewsAndroid;
                    }
                    Boolean getFreePointAndroid = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.GET_FREE_POINT_ANDROID);
                    if (getFreePointAndroid != null) {
                        Setting.turnOffGetFreePointAndroid = getFreePointAndroid;
                    }
//                    Setting.android_usable_version = (String) request.getParamValue(SettingdbKey.OTHER_SETTING.ANDROID_USABLE_VERSION);
//                    Setting.ios_non_enterprise_usable_version = (String) request.getParamValue(SettingdbKey.OTHER_SETTING.IOS_NON_ENTERPRISE_USABLE_VERSION);
//                    Setting.ios_enterprise_usable_version = (String) request.getParamValue(SettingdbKey.OTHER_SETTING.IOS_ENTERPRISE_USABLE_VERSION);
//                    String enterpriseTurnOffSafaryVersion = (String) request.getParamValue(SettingdbKey.OTHER_SETTING.ENTERPRISE_TURN_OFF_SAFARY_VERSION);
//                    if (enterpriseTurnOffSafaryVersion != null) {
//                        Setting.enterpriseturnOffSafaryVersion = enterpriseTurnOffSafaryVersion;
//                    }
//                    Boolean enterpriseTurnOffSafary = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.ENTERPRISE_TURN_OFF_SAFARY);
//                    if (enterpriseTurnOffSafary != null) {
//                        Setting.enterpriseTurnOffSafary = enterpriseTurnOffSafary;
//                    }
//                    Boolean enterpriseLoginByMocom = (Boolean) request.getParamValue(SettingdbKey.OTHER_SETTING.ENTERPRISE_LOGIN_BY_MOCOM);
//                    if (enterpriseLoginByMocom != null) {
//                        Setting.enterpriseTurnOffLoginByMocom = enterpriseLoginByMocom;
//                    }
                    
//                    InterCommunicator.sendRequest(request.toJson(), Config.StaticFileServerIp, Config.StaticFileServerPort);
                    InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                    String buzz = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                    System.out.println("buzz : " + buzz);
//                    List<Session> listSession = SessionManager.removeSessionsOfGroupUser(null);
//                    SessionManager.changeSettingAllUser(true);
                    SessionManager.updateInUseAllUser(true);
//                    try {
//                        UserSessionDAO.remove(listSession);
//                    } catch (DaoException ex) {
//                        Util.addErrorLog(ex);
//                    }
//                    List<String> list = new ArrayList<String>();
//                    for (Session session : listSession) {
//                        list.add(session.userID);
//                    }
//                    InterCommunicator.resetOrDeletPresentation(list);
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

            }
            return result;
        }
    }
    
    public static class SetVersionSetting implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    Setting.android_usable_version = (String) request.getParamValue(SettingdbKey.OTHER_SETTING.ANDROID_USABLE_VERSION);
                    Setting.ios_non_enterprise_usable_version = (String) request.getParamValue(SettingdbKey.OTHER_SETTING.IOS_NON_ENTERPRISE_USABLE_VERSION);
                    Setting.ios_enterprise_usable_version = (String) request.getParamValue(SettingdbKey.OTHER_SETTING.IOS_ENTERPRISE_USABLE_VERSION);

//                    List<Session> listSession = SessionManager.removeSessionsOfGroupUser(null);
//                    SessionManager.changeSettingAllUser(true);
                    SessionManager.updateInUseAllUser(false);
//                    try {
//                        UserSessionDAO.remove(listSession);
//                    } catch (DaoException ex) {
//                        Util.addErrorLog(ex);
//                    }
//                    List<String> list = new ArrayList<String>();
//                    for (Session session : listSession) {
//                        list.add(session.userID);
//                    }
//                    InterCommunicator.resetOrDeletPresentation(list);
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

            }
            return result;
        }
    }

    public static class StaticPage implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result;
            result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    MixService ss = new MixService();
                    result = ss.callApi(request);
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }

    public static class RegisterByAdmin implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = null;
            result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {

                    result = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(result);
                        JSONObject data = (JSONObject) json.get(ParamKey.DATA);
                        if (data != null) {
                            String userId = (String) data.get(ParamKey.USER_ID);
                            Long gender = (Long) data.get(ParamKey.GENDER);
                            UserCounter.count(userId, Constant.DEVICE_TYPE.WEB, gender.intValue(), true);
                            data.put(ParamKey.EMAIL, userId);
                            data.put(ParamKey.API_NAME, API.REGISTER);
                            InterCommunicator.sendRequest(data.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                        }
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

            }
            return result;
        }

    }
    
    public static class UpdateUserInfor implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = null;
            result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    String userId = (String) request.getParamValue(ParamKey.REQUEST_USER_ID);
                    request.put(ParamKey.USER_ID, userId);
                    result = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(result);
                        JSONObject data = (JSONObject) json.get(ParamKey.DATA);
                        if (data != null) {
                            updateInfor(data, userId);
                        }
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

            }
            return result;
        }

        private static void updateInfor(JSONObject joData, String userId) {
            Long isActive = (Long) joData.get(ParamKey.IS_ACTIVE_USER);
            Long isFinishRegister = (Long) joData.get(ParamKey.IS_FINISH_REGISTER);
            Boolean isVerify = (Boolean) joData.get(ParamKey.IS_VERIFY);

            if (isFinishRegister != null && isFinishRegister == Constant.FLAG.ON) {
                if (isActive == null) {
                    if (isVerify) {
                        joData.put(ParamKey.EMAIL, userId);
        //                joData.put(ParamKeys.API, API.DEACTIVATE);
                        //                InterCommunicator.sendRequest(joData.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                        joData.put(ParamKey.API_NAME, API.REGISTER);
                        InterCommunicator.sendRequest(joData.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                    } else {
                        joData.put(ParamKey.API_NAME, API.DEACTIVATE);
                        InterCommunicator.sendRequest(joData.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                        InterCommunicator.sendRequest(joData.toJSONString(), Config.BuzzServerIP, Config.BuzzServerPort);
//                        List<Session> listSession = SessionManager.removeSessionsByUserId(userId);
                        SessionManager.updateInUseByUserId(userId, false);
//                        try {
//                            UserSessionDAO.remove(listSession);
//                        } catch (DaoException ex) {
//                            Util.addErrorLog(ex);
//                        }
//                        List<String> list = new ArrayList<String>();
//                        list.add(userId);
//                        InterCommunicator.resetOrDeletPresentation(list);
                    }

                } else if (isActive == Constant.FLAG.ON) {
                    if (isVerify) {
                        joData.put(ParamKey.EMAIL, userId);
                        joData.put(ParamKey.API_NAME, API.REGISTER);
                        InterCommunicator.sendRequest(joData.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                    }
                    joData.put(ParamKey.API_NAME, API.ACTIVATE);
                    //                InterCommunicator.sendRequest(joData.toJSONString(), Config.FreeSwitchServerIp, Config.FreeSwitchServerPort);
                    InterCommunicator.sendRequest(joData.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
                    InterCommunicator.sendRequest(joData.toJSONString(), Config.BuzzServerIP, Config.BuzzServerPort);
                } else {
                    joData.put(ParamKey.API_NAME, API.DEACTIVATE);
                    InterCommunicator.sendRequest(joData.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                    InterCommunicator.sendRequest(joData.toJSONString(), Config.BuzzServerIP, Config.BuzzServerPort);
                    InterCommunicator.sendRequest(joData.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                    InterCommunicator.sendRequest(joData.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
                    //                InterCommunicator.sendRequest(joData.toJSONString(), Config.FreeSwitchServerIp, Config.FreeSwitchServerPort);  
//                    List<Session> listSession = SessionManager.removeSessionsByUserId(userId);
                    SessionManager.updateInUseByUserId(userId, false);
//                    try {
//                        UserSessionDAO.remove(listSession);
//                    } catch (DaoException ex) {
//                        Util.addErrorLog(ex);
//                    }
//                    List<String> list = new ArrayList<String>();
//                    list.add(userId);
//                    InterCommunicator.resetOrDeletPresentation(list);
                }
            }
//            List<Session>  listSession = SessionManager.removeSessionsByUserId(userId);
//            try{
//                UserSessionDAO.remove(listSession);
//            }catch(Exception ex){
//                Util.addErrorLog(ex);
//            }
            joData.clear();
        }
    }

    public static class DeleteBuzz implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
                if (code == ErrorCode.SUCCESS) {
                    Long avatar = (Long) joData.get(ParamKey.AVATAR);
                    String userId = (String) joData.get(ParamKey.USER_ID);
                    if (avatar != null && avatar == -1) {
                        String imageId = Constant.NO_AVATAR_STRING_VALUE;

                        request.put(ParamKey.USER_ID, userId);
                        request.put(ParamKey.API_NAME, API.UPDATE_USER_INFOR);
                        request.put(ParamKey.IMAGE_ID, imageId);
                        InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                    }
//                    String buzzId = Util.getStringParam(request, ParamKeys.BUZZ_ID);
//                    request.put(ParamKeys.BUZZ_ID, buzzId);
//                    request.put(ParamKeys.UserID, userId);
//                    request.put(ParamKeys.API, API.PROCESS_DELETE_BUZZ);
//                    request.reqObj.put(ParamKeys.FLAG, -1);
//                    InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort); 
                    jsonResult.remove(ParamKey.DATA);
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }
    
    public static class SetUploadSetting implements IServiceBackendAdapter {

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    InterCommunicator.sendRequest(request.toJson(), Config.StaticFileServerIp, Config.StaticFileServerPort);
                    InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

            }
            return result;
        }
    }

    private static String sendRequest(String url, String requestString, String serverIP, int serverPort) {
        String result = " ";
        try {

            StringBuilder postData = new StringBuilder();
            String urlStr = "http://" + serverIP + ":" + serverPort + "/";
            if (url != null) {
                urlStr += url;
            }
            URL u = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            if (requestString != null) {
            //post method
                //data to send
                postData.append(requestString);
                String encodedData = postData.toString();
                // send data by byte
                conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());
                byte[] postDataByte = postData.toString().getBytes("UTF-8");
                try {
                    OutputStream out = conn.getOutputStream();
                    out.write(postDataByte);
                    out.close();
                } catch (IOException ex) {
                    Util.addErrorLog(ex);

                }
            }
            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            result = buf.readLine();

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }

    private static class SetPrioritizeUserBuzzSetting implements IServiceBackendAdapter{

        @Override
        public String callService(Request request) {
            String result = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
            return result;
        }
    }
}
