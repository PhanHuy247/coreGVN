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
import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.Setting;
import com.vn.ntsc.dao.impl.BuzzTagDAO;
import com.vn.ntsc.dao.impl.FavoristDAO;
import com.vn.ntsc.dao.impl.FileDAO;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.adapter.impl.util.ParseData;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.entity.impl.FileData;
import com.vn.ntsc.otherservice.entity.impl.FileUrl;
import com.vn.ntsc.otherservice.entity.impl.ListFileData;
import com.vn.ntsc.otherservice.entity.impl.ListFileUrl;
import com.vn.ntsc.otherservice.entity.impl.User;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;
import com.vn.ntsc.otherservice.statistic.CmCodeCollector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RuAc0n
 */
public class UserManagermentServiceAdapter implements IServiceAdapter {

    public static final AddFavourist addFavourist = new AddFavourist();
    public static final Unlock unlock = new Unlock();
    public static final NotificationSetting notiSetting = new NotificationSetting();
    public static final ConfirmUploadImage confirmUploadImage = new ConfirmUploadImage();
    public static final ConfirmUploadVideo confirmUploadVideo = new ConfirmUploadVideo();
    public static final ConfirmRecordingVideo confirmRecordingVideo = new ConfirmRecordingVideo();
    public static final ConfirmStreamingVideo confirmStreamingVideo = new ConfirmStreamingVideo();
    public static final ConfirmUploadAudio confirmUploadAudio = new ConfirmUploadAudio();
    public static final SettingCallWaiting settingCallWaiting = new SettingCallWaiting();
    public static final UpdateUserAgeFlag updateUserAgeFlag = new UpdateUserAgeFlag();
    public static final GetMyPageInfo getMyPageInfo = new GetMyPageInfo();
    public static final GetBackendSetting getBackendSetting = new GetBackendSetting();
    public static final GettAttentionNumber getAttentionNumber = new GettAttentionNumber();
    public static final SetCreaUserInfo setCreaUserInfo = new SetCreaUserInfo();
    public static final CheckCall checkCall = new CheckCall();
    public static final LoadAlbumImage loadAlbumImage = new LoadAlbumImage();
    public static final LoadAlbum loadAlbum = new LoadAlbum();
    public static final UpdateAlbum updateAlbum = new UpdateAlbum();
    public static final CreateAccountFromFBid createAccFromFbId = new CreateAccountFromFBid();
    public static final ListUserOnlineAlert listUserOnlineAlert = new ListUserOnlineAlert();
    
    //Linh #10080
    public static final ListPublicImage listPublicImage = new ListPublicImage();
    public static final ListPublicVideo listPublicVideo = new ListPublicVideo();
    public static final ListPublicFile listPublicFile = new ListPublicFile();

    @Override
    public String callService(Request request) {
        String requestStr = request.toJson();
        String result;
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(requestStr);
            if(json.get(ParamKey.API_NAME).equals(API.LIST_ONLINE_ALERT)){
                result = requestOnline(requestStr);
            }else{
                result = request(requestStr);
            }
        } catch (ParseException ex) {
            Util.addErrorLog(ex);
            result = ResponseMessage.UnknownError;
        }
        return result;
    }
    public String request(String inputString){
         String result;
          try {
            result = InterCommunicator.sendRequest(inputString, Config.UMSServerIP, Config.UMSPort);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = ResponseMessage.UnknownError;
        }
        return result;
    }
    public String requestOnline(String inputString) {
        String result;
        JSONObject json = null;
        try {
            result = InterCommunicator.sendRequest(inputString, Config.UMSServerIP, Config.UMSPort);
            json = (JSONObject) new JSONParser().parse(result);
            Long code = (Long) json.get(ParamKey.ERROR_CODE);
            if(code == ErrorCode.SUCCESS){
                Object objData = json.get(ParamKey.DATA);
                JSONArray arr = (JSONArray) json.get(ParamKey.DATA);
                List<String> listAvaId = new ArrayList<>();
                List<String> listUser = new ArrayList<>();
                for (Object obj : arr){
                    JSONObject user = (JSONObject) obj;
                    String avaId = (String) user.get(ParamKey.AVATAR_ID);
                    if (avaId != null && !avaId.isEmpty()){
                        listAvaId.add(avaId);
                    }
                    user.put(ParamKey.AVATAR, "");
                    
                    String friendId = (String) user.get(ParamKey.USER_ID);
                    listUser.add(friendId);
                }
                HashMap<String, FileUrl> mapUrl = InterCommunicator.getImage(listAvaId);
                for (Object obj : arr){
                    JSONObject user = (JSONObject) obj;
                    String avaId = (String) user.get(ParamKey.AVATAR_ID);
                    if (avaId != null && !avaId.isEmpty()){
                        FileUrl url = mapUrl.get(avaId);
                        if (url != null){
                            user.put(ParamKey.AVATAR, url.getThumbnail());
                        }
                    }
                }
                
                //HoangNH #11770
                //filter get user online only
                JSONArray psArr = InterCommunicator.getUserPresentList(null, listUser);
                Map<String, User> mUserInfo = ParseData.parseUserInfo(psArr);
                
                JSONArray returnData = new JSONArray();
                for (Object obj : arr){
                    JSONObject user = (JSONObject) obj;
                    String friendId = (String) user.get(ParamKey.USER_ID);
                    User userInfo = mUserInfo.get(friendId);
                    if(userInfo.isOnline){
                        returnData.add(user);
                    }
                }

                JSONObject returnObj = new JSONObject();
                returnObj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                returnObj.put(ParamKey.DATA, returnData);
                return returnObj.toJSONString();
            }
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = ResponseMessage.UnknownError;
        }
       return json.toJSONString();
    }

    public static void updateCmCode(String reciverCode, String cmCode, int type) {
        try {
            JSONObject umsRequest = new JSONObject();
            umsRequest.put(ParamKey.API_NAME, API.CM_CODE);
            umsRequest.put("rec_code", reciverCode);
            umsRequest.put(ParamKey.CM_CODE, cmCode);
            String result = InterCommunicator.sendRequest(umsRequest.toJSONString(), Config.UMSServerIP, Config.UMSPort);
            JSONObject obj = (JSONObject) new JSONParser().parse(result);
            Long code = (Long) obj.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                CmCodeCollector.registerLog(cmCode, type);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void updateInstallCmCode(String uniqueNumber, String cmCode, int type) {
        try {
            CmCodeCollector.installLog(cmCode, type, uniqueNumber);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
//        UserManagermentServiceAdapter adapter = new UserManagermentServiceAdapter();
//        adapter.request(umsRequest.toJSONString());
    }

    public static class ConfirmUploadImage implements IServiceAdapter {

        @Override
        public String callService(Request request) {
//            int auto = Constant.auto_approved_image;
            try {
                request.reqObj.put("auto_approved_img", Setting.auto_approved_image);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject obj = (JSONObject) new JSONParser().parse(umsStr);
                Long code = (Long) obj.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    Long isAva = (Long) ((JSONObject) obj.get(ParamKey.DATA)).get(ParamKey.IS_AVATAR);
                    String imageId = (String) request.getParamValue(ParamKey.IMAGE_ID);
                    if (isAva == Constant.FLAG.ON) {
                        request.put(ParamKey.API_NAME, API.UPDATE_USER_INFOR);
                        request.put(ParamKey.IMAGE_ID, imageId);
                        InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                    }
                    Long imagType = (Long) ((JSONObject) obj.get(ParamKey.DATA)).get(ParamKey.IMAGE_TYPE);
                    if (imagType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE && Setting.auto_approved_image == Constant.FLAG.ON) {
                        String userId = (String) request.getParamValue(ParamKey.USER_ID);
                        request.put(ParamKey.API_NAME, API.NOTI_BACKSTAGE_APPROVE);
                        request.put(ParamKey.TOUSERID, userId);
                        request.put(ParamKey.NOTI_IMAGE_ID, imageId);
                        InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                    }
                }
                return umsStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }
    
    public static class ConfirmUploadVideo implements IServiceAdapter {

        @Override
        public String callService(Request request) {
//            int auto = Constant.auto_approved_image;
            try {
                request.reqObj.put("auto_approved_video", Setting.auto_approved_video);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject obj = (JSONObject) new JSONParser().parse(umsStr);
                Long code = (Long) obj.get(ParamKey.ERROR_CODE);
                String buzzId  = (String) request.getParamValue(ParamKey.BUZZ_ID);
                if (code == ErrorCode.SUCCESS && buzzId != null) {
                    String userId = (String) request.getParamValue(ParamKey.USER_ID);
                    String videoId = (String) request.getParamValue(ParamKey.VIDEO_ID);
                    request.put(ParamKey.API_NAME, API.NOTI_BUZZ_APPROVED);
                    request.put(ParamKey.TOUSERID, userId);
                    request.put(ParamKey.NOTI_IMAGE_ID, videoId);
                    request.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                    InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                    
                    JSONObject data = (JSONObject) obj.get(ParamKey.DATA);
                    JSONArray listFvt = (JSONArray) data.get(ParamKey.FAVORITED_LIST);
                    if(listFvt != null && !listFvt.isEmpty()){
                        Util.addDebugLog("===========AddBuzz listFvt size========"+listFvt.size());
                        String userName = (String) data.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
                        Util.addDebugLog("========Username AddBuzz===:" +userName);
                        JSONObject objNoti = new JSONObject();
                        objNoti.put(ParamKey.API_NAME, API.NOTI_NEW_BUZZ_FROM_FAVORIST);
                        objNoti.put(ParamKey.USER_NAME, userName);
                        objNoti.put(ParamKey.FROM_USER_ID, userId);
                        objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
                        objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);

                        InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                    }
                }
                return umsStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }
    
    public static class ConfirmRecordingVideo implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            try{
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                String videoId = (String) request.getParamValue(ParamKey.VIDEO_ID);
                String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
                request.put(ParamKey.API_NAME, API.NOTI_RECORDING_FILE);
                request.put(ParamKey.TOUSERID, userId);
                request.put(ParamKey.NOTI_VIDEO_ID, videoId);
                request.put(ParamKey.BUZZ_ID, buzzId);
                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
            return ResponseMessage.SuccessMessage;
        }
        
    }
    
    public static class ConfirmStreamingVideo implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            try{
                Util.addDebugLog("========ConfirmStreamingVideo========");
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
                
                request.put(ParamKey.API_NAME, API.GET_NOTIFICATION_LIST);
                request.put(ParamKey.USER_ID, userId);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                Util.addDebugLog("========umsStr========"+umsStr);
                JSONObject obj = (JSONObject) new JSONParser().parse(umsStr);
                Long code = (Long) obj.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    JSONObject data = (JSONObject) obj.get(ParamKey.DATA);
                    JSONArray listFvt = (JSONArray) data.get(ParamKey.FAVORITED_LIST);
                    String userName = (String) data.get(ParamKey.USER_NAME);
                    String streamId = (String) data.get(ParamKey.STREAM_ID);
                    JSONArray listTo = BuzzTagDAO.getListTagUser(buzzId);
                    if(listFvt != null && !listFvt.isEmpty()){
                        
                        JSONObject objNoti = new JSONObject();;
//                        objNoti.put(ParamKey.API_NAME, API.NOTI_NEW_BUZZ_FROM_FAVORIST);
//                        objNoti.put(ParamKey.USER_NAME, userName);
//                        objNoti.put(ParamKey.FROM_USER_ID, userId);
//                        objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
//                        objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                        
                        objNoti.put(ParamKey.API_NAME, API.NOTI_LIVESTREAM_FROM_FAVOURIST);
                        objNoti.put(ParamKey.USER_NAME, userName);
                        objNoti.put(ParamKey.FROM_USER_ID, userId);
                        objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
                        objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                        objNoti.put(ParamKey.STREAM_ID, streamId);
                        objNoti.put(ParamKey.TAG_LIST, listTo);

                        InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                    }
                    
                }
                
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
            return ResponseMessage.SuccessMessage;
        }
        
    }
    
     public static class ConfirmUploadAudio implements IServiceAdapter {

        @Override
        public String callService(Request request) {
//            int auto = Constant.auto_approved_image;
            try {
                request.reqObj.put("auto_approved_video", Setting.auto_approved_video);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject obj = (JSONObject) new JSONParser().parse(umsStr);
                Long code = (Long) obj.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
//                    Long imagType = (Long) ((JSONObject) obj.get(ParamKey.DATA)).get(ParamKey.IMAGE_TYPE);
//                    if (imagType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE && Setting.auto_approved_image == Constant.FLAG.ON) {
                        String userId = (String) request.getParamValue(ParamKey.USER_ID);
                        String audioId = (String) request.getParamValue(ParamKey.AUDIO_ID);
                        request.put(ParamKey.API_NAME, API.NOTI_BACKSTAGE_APPROVE);
                        request.put(ParamKey.TOUSERID, userId);
                        request.put(ParamKey.NOTI_AUDIO_ID, audioId);
                        InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
//                    }
                }
                return umsStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }


    public static class NotificationSetting implements IServiceAdapter {

        @Override
        public String callService(Request request) {

            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
                return umsStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }

    public static class GetBackendSetting implements IServiceAdapter {

        @Override
        public String callService(Request request) {

            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    JSONObject joData = (JSONObject) jo.get(ParamKey.DATA);
                    joData.put("switch_safari_version", Setting.turnOffSafaryVersion);
                    joData.put("get_free_point", Setting.turnOffGetFreePoint);
                    joData.put("turn_off_user_info", Setting.turnOffExtendedUserInfo);
                    joData.put("turn_off_show_news", Setting.turnOffShowNews);
                    joData.put("switch_browser_android_version", Setting.turnOffBrowserAndroidVersion);
                    joData.put("get_free_point_android", Setting.turnOffGetFreePointAndroid);
                    joData.put("turn_off_user_info_android", Setting.turnOffExtendedUserInfoAndroid);
                    joData.put("turn_off_show_news_android", Setting.turnOffShowNewsAndroid);
                    SessionManager.updateChangeBackendSettingFlag(request.token, false);
                    return jo.toJSONString();
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }

    public static class Unlock implements IServiceAdapter {

        @Override
        public String callService(Request request) {
//            String result = "";
            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                //            Long unlockType = (Long) request.getParamValue(ParamKey.UNLOCK_TYPE);
                //            if (unlockType == Constant.BACKSTAGE_UNLOCK) {
                //                try {
                //                    JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                //                    //DuongLTD
                //                    Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                //                    if (code != ErrorCode.SUCCESS) {
                //                        return umsStr;
                //                    }
                //                    result = jo.toJSONString();
                //                    return result;
                //                } catch (Exception ex) {
                //                    Util.addErrorLog(ex);
                //
                //                }
                //            }
                return umsStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }

    public static class AddFavourist implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                JSONObject dataObj = (JSONObject) jo.get(ParamKey.DATA);
                if (dataObj == null) {
                    return jo.toJSONString();
                }
                // send to chat server

                InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
//                Long isNoti = (Long) ((JSONObject) jo.get(ParamKey.Data)).get(ParamKey.IS_NOTI);
//                if (isNoti != null && isNoti == Constant.YES) {
//                    JSONObject notiRequest = new JSONObject();
//                    String from = (String) request.getParamValue(ParamKey.UserID);
//                    String to = (String) request.getParamValue(ParamKey.REQUEST_USER_ID);
//                    String ip = (String) request.getParamValue(ParamKey.IP);
//                    notiRequest.put(ParamKey.API, API.NOTI_FAVORITED);
//                    notiRequest.put(ParamKey.NOTI_FROM_USER_ID, from);
//                    notiRequest.put(ParamKey.NOTI_TO_USER_ID, to);
//                    notiRequest.put(ParamKey.IP, ip);
//                    notiRequest.put(ParamKey.NOTI_FROM_USER_ID, from);
//                    notiRequest.put(ParamKey.NOTI_TO_USER_ID, to);
//                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
//                }
                jo.remove(ParamKey.DATA);
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;
            }
            return result;
        }
    }

    public static class GetMyPageInfo implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    // buzz number
                    JSONObject usersObj = (JSONObject) jo.get(ParamKey.DATA);
                    String userId = (String) request.getParamValue(ParamKey.USER_ID);
                    request.put(ParamKey.USER_ID, userId);
                    request.put(ParamKey.API_NAME, API.GET_BUZZ_NUMBER);
                    String buzzResult = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                    int buzzNumber = 0;
                    try {
                        JSONObject buzzJSON = (JSONObject) new JSONParser().parse(buzzResult);
                        JSONObject data = (JSONObject) buzzJSON.get(ParamKey.DATA);
                        if (data != null) {
                            Long buzzNumberL = (Long) data.get("buzz_number");
                            buzzNumber = buzzNumberL != null ? buzzNumberL.intValue() : 0;
                        }
                    } catch (ParseException ex) {
                        Util.addErrorLog(ex);
                    }
                    usersObj.put("buzz_number", buzzNumber);

                    return jo.toJSONString();
                }
                return umsStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }

    public static class GettAttentionNumber implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    // buzz number
                    request.put(ParamKey.API_NAME, API.TOTAL_UNREAD);
                    String line = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
                    int number = 0;
                    try {
                        number = Integer.parseInt(line);
                    } catch (NumberFormatException ex) {
                        Util.addErrorLog(ex);
                    }
                    JSONObject joData = (JSONObject) jo.get(ParamKey.DATA);
                    joData.put(ParamKey.UNREAD_NUMBER, number);
                    return jo.toJSONString();
                }
                return umsStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }

    public static class SettingCallWaiting implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                String token = request.token;
                List<Session> listSession = SessionManager.removeSessionsOfUserExcudeToken(token);
                try {
                    UserSessionDAO.remove(listSession);
                } catch (EazyException ex) {
                    Util.addErrorLog(ex);
                }
                request.put(ParamKey.API_NAME, API.UPDATE_USER_INFOR);
                InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                return umsStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }

    public static class SetCreaUserInfo implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            return umsStr;
        }
    }

    public static class UpdateUserAgeFlag implements IServiceAdapter {

        @Override
        public String callService(Request request) {

            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                String userId = (String) request.getParamValue(ParamKey.USERID);

                JSONObject obj = (JSONObject) (new JSONParser().parse(umsStr));
                JSONObject data = (JSONObject) obj.get(ParamKey.DATA);
                if (data != null) {
                    Boolean isRemoveSession = (Boolean) data.get(ParamKey.IS_REMOVE_SESSION);
                    if (isRemoveSession) {
                        //                    List<Session>  listSession = SessionManager.removeSessionsByUserId(userId);
                        SessionManager.removeSessionsByUserId(userId);
                        //                    try{
                        //                        UserSessionDAO.remove(listSession);
                        //                    }catch(Exception ex){
                        //                        Util.addErrorLog(ex);
                        //                    }
                    }
                    Long isActive = (Long) data.get(ParamKey.IS_ACTIVE_USER);
                    Long isFinishRegister = (Long) data.get(ParamKey.IS_FINISH_REGISTER);
                    Boolean isVerify = (Boolean) data.get(ParamKey.IS_VERIFY);
                    if (isVerify && isActive == Constant.FLAG.ON && isFinishRegister == Constant.FLAG.ON) {
                        data.put(ParamKey.EMAIL, userId);
                        data.put(ParamKey.API_NAME, API.REGISTER);
                    } else {
                        data.put(ParamKey.API_NAME, API.DEACTIVATE);
                    }
                    InterCommunicator.sendRequest(data.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                    obj.remove(ParamKey.DATA);
                    return obj.toJSONString();
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
    }

    public static class CheckCall implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject obj = (JSONObject) (new JSONParser().parse(umsStr));
                JSONObject data = (JSONObject) obj.get(ParamKey.DATA);
                
                String friendId = request.reqObj.get("rcv_id").toString();
                String uId = request.reqObj.get("user_id").toString();
                List<String> llEmail = new LinkedList<>();
                if (friendId != null) {
                    llEmail.add(friendId);
                }
                JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                if (psArr != null && !psArr.isEmpty()) {
                    JSONObject json = (JSONObject) psArr.get(0);
                    Boolean isOnline = (Boolean) json.get(ParamKey.IS_ONLINE);
                    String id = (String) json.get(ParamKey.USER_ID);
                    String userName = (String) json.get(ParamKey.USER_NAME);
                    Long gender = (Long) json.get(ParamKey.GENDER);
                    Boolean isVideo = (Boolean) json.get(ParamKey.VIDEO_CALL_WAITING);
                    Boolean isVoice = (Boolean) json.get(ParamKey.VOICE_CALL_WAITING);
                    
                    data.put(ParamKey.IS_ONLINE, isOnline);
                    data.put(ParamKey.USER_ID, id);
                    data.put(ParamKey.USER_NAME, userName);
                    data.put(ParamKey.GENDER, gender);
                    data.put(ParamKey.VIDEO_CALL_WAITING, isVideo);
                    data.put(ParamKey.VOICE_CALL_WAITING, isVoice);

                    return obj.toJSONString();
                } else {
                    JSONObject jo = new JSONObject();
                    jo.put(ParamKey.ERROR_CODE, ErrorCode.USER_NOT_EXIST);
                    return jo.toJSONString();
                }
            } catch (Exception e) {
                Util.addErrorLog(e);
            }
            return ResponseMessage.UnknownError;

        }

    }
     public static class ListPublicVideo implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                Util.addDebugLog("umsStr-=-------------------------------------"+umsStr);
                JSONObject jo = (JSONObject) (new JSONParser().parse(umsStr));
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                Long videoStatus = 0L;
                Long audioStatus = 0L;
                if (ErrorCode.SUCCESS == code){
                    JSONArray data = (JSONArray) jo.get(ParamKey.DATA);
                    List<String> listVideoId = new LinkedList<>();
                    List<String> listAudioId = new LinkedList<>();
                    List<String> listCoverId = new LinkedList<>();
                    for (int i = 0; i < data.size() ; i++){
                        Object obj = data.get(i);
                        String videoId = (String) ((JSONObject) obj).get(ParamKey.VIDEO_ID);
                        String audioId = (String) ((JSONObject) obj).get(ParamKey.AUDIO_ID);
                        String coverId = (String) ((JSONObject) obj).get(ParamKey.COVER_ID);
                        if(videoId != null){
                            videoStatus = FileDAO.getFileStatus(videoId);
                             if (videoStatus.intValue() == 1) {
                                listVideoId.add(videoId);
                            }else{
                                data.remove(i);
                                i--;
                                continue;
                            }
                        }
                        if(audioId != null && coverId != null){
//                            audioStatus = FileDAO.getFileStatus(audioId);
//                             if ( audioStatus.intValue() == 1) {
                                listAudioId.add(audioId);
                                listCoverId.add(coverId);
//                            }else{
//                                data.remove(i);
//                                i--;
//                                continue;
//                            }
                        }
                    }
                    HashMap<String, FileUrl> videoMap = InterCommunicator.getVideo(listVideoId);
                    HashMap<String, FileUrl> audioMap = InterCommunicator.getAudio(listAudioId,listCoverId);
                    for (Object obj : data){
                        JSONObject videoObj = (JSONObject) obj;
                        String videoId = (String) videoObj.get(ParamKey.VIDEO_ID);
                        String audioId = (String) videoObj.get(ParamKey.AUDIO_ID);
                        FileUrl urlVideo = videoMap.get(videoId);
                        FileUrl urlAudio = audioMap.get(audioId);
                        if (urlVideo != null && urlVideo.getFileUrl() != null) {
                            videoObj.put(ParamKey.THUMBNAIL_URL, urlVideo.getThumbnail());
                            videoObj.put(ParamKey.ORIGINAL_URL, urlVideo.getFileUrl());
                            videoObj.put("buzz_type", "video");
                        }
                        if (urlAudio != null && urlAudio.getOriginalUrl() != null) {
                            videoObj.put(ParamKey.THUMBNAIL_URL, urlAudio.getThumbnail());
                            videoObj.put(ParamKey.ORIGINAL_URL, urlAudio.getOriginalUrl());
                            videoObj.put("buzz_type", "audio");
                            videoObj.remove(ParamKey.COVER_ID);
                        }
                    }
                }
                
                return jo.toJSONString();
            } catch (Exception e) {
                Util.addErrorLog(e);
            }
            return ResponseMessage.UnknownError;

        }

    }
    //Linh #10080
    public static class ListPublicImage implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) (new JSONParser().parse(umsStr));
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (ErrorCode.SUCCESS == code){
                    JSONArray data = (JSONArray) jo.get(ParamKey.DATA);
                    
                    Util.addDebugLog("ListPublicImage==================================="+data);
                    
                    Iterator<Object> iterator = data.iterator();
                    while(iterator.hasNext()){
                        JSONObject imgObj = (JSONObject) iterator.next();
                        String imgId = (String) imgObj.get(ParamKey.IMAGE_ID);
                        String buzzId = (String) imgObj.get(ParamKey.BUZZ_ID);
                        request.put("buzz_id", buzzId);
                        request.put("api", "get_buzz_detail");
                        request.put("img_id", imgId);
                        
                        String jsonBuzzDetal = BuzzAdapter.getBuzzDetail.callService(request);
                        JSONObject joBuzzDetail = (JSONObject) (new JSONParser().parse(jsonBuzzDetal));
                        Long codeBuzzDetail = (Long) joBuzzDetail.get(ParamKey.ERROR_CODE);
                        if(ErrorCode.SUCCESS == codeBuzzDetail){
                            
                            JSONObject jsonObjectBuzzDetail = (JSONObject) joBuzzDetail.get(ParamKey.DATA);
                            JSONArray dataListChild = (JSONArray) jsonObjectBuzzDetail.get("list_child");
                            if(dataListChild.size()!= 0){
                                JSONObject imgObjChild = (JSONObject) dataListChild.get(0);
                                imgObj.put("thumbnail_height", imgObjChild.get("thumbnail_height"));
                                imgObj.put("cmt_num", imgObjChild.get("cmt_num"));
                                imgObj.put("buzz_id", imgObjChild.get("buzz_id"));
                                imgObj.put("original_width", imgObjChild.get("original_width"));
                                imgObj.put("buzz_time", imgObjChild.get("buzz_time"));
                                imgObj.put("thumbnail_width", imgObjChild.get("thumbnail_width"));
                                imgObj.put("thumbnail_url", imgObjChild.get("thumbnail_url"));
                                imgObj.put("buzz_type", imgObjChild.get("buzz_type"));
                                imgObj.put("original_height", imgObjChild.get("original_height"));
                                imgObj.put("is_app", imgObjChild.get("is_app"));
                                imgObj.put("user_id", imgObjChild.get("user_id"));
                                imgObj.put("buzz_val", imgObjChild.get("buzz_val"));
                                JSONObject like = (JSONObject) imgObjChild.get("like");
                                JSONObject likeBuzz = new JSONObject();
                                likeBuzz.put("is_like", like.get("is_like"));
                                likeBuzz.put("like_num", like.get("like_num"));
                                imgObj.put("like", likeBuzz);
                                JSONArray tagList = (JSONArray) jsonObjectBuzzDetail.get("tag_list");
                                JSONArray tagListBuzzArray = new JSONArray();
                                JSONObject tagListObject = new JSONObject();
                                for(Object tagListBuzz : tagList){
                                    JSONObject imgTagList = (JSONObject) tagListBuzz;
                                    tagListObject.put("user_name", imgTagList.get("user_name"));
                                    tagListObject.put("flag", imgTagList.get("flag"));
                                    tagListObject.put("user_id", imgTagList.get("user_id"));
                                }
                                tagListBuzzArray.add(tagListObject);
                                imgObj.put("tag_list", tagListBuzzArray);
                                imgObj.remove("img_id");
                                imgObj.put("file_id",imgId);
                            }else{
                                iterator.remove();
                            }
                        }else{
                            iterator.remove();
                        }
                              
                    }
                }
                
                
                return jo.toJSONString();
            } catch (Exception e) {
                Util.addErrorLog(e);
            }
            return ResponseMessage.UnknownError;

        }

    }
    
    public static class ListPublicFile implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            try{
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                String reqUserId = (String) request.getParamValue(ParamKey.REQUEST_USER_ID);
                
                long isMine = Constant.FLAG.OFF;
                long isFav = Constant.FLAG.OFF;
                if(userId != null && reqUserId != null){
                    isFav = FavoristDAO.checkFavourist(userId, reqUserId);
                }
                if(userId != null){
                    if(reqUserId != null && userId.equals(reqUserId)){
                        isMine = Constant.FLAG.ON;
                    }
                    if(reqUserId == null){
                        isMine = Constant.FLAG.ON;
                    }
                }
                
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) (new JSONParser().parse(umsStr));
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (ErrorCode.SUCCESS == code){
                    JSONArray data = (JSONArray) jo.get(ParamKey.DATA);
                    
                    Util.addDebugLog("ListPublicFile==================================="+data);
                    
                    Set<String> setBuzz = new HashSet<>();
                    List<String> listFile = new ArrayList<>();
                    
                    Iterator<Object> iterator = data.iterator();
                    while(iterator.hasNext()){
                        JSONObject imgObj = (JSONObject) iterator.next();
                        String fileId = (String) imgObj.get(ParamKey.FILE_ID);
                        String buzzId = (String) imgObj.get(ParamKey.BUZZ_ID);
                        
                        setBuzz.add(buzzId);
                        listFile.add(fileId);
                    }
                    Util.addDebugLog("setBuzz==================================="+setBuzz);
                    Util.addDebugLog("listFile==================================="+listFile);
                    
                    JSONArray arr = new JSONArray();
                    for (String item : listFile) {
                        arr.add(item);
                    }
                    request.reqObj.put(ParamKey.LIST_ID, listFile);
                    request.put(ParamKey.API_NAME, API.GET_MEDIA_BUZZ_DATA);
                    request.put(ParamKey.USER_ID, userId);
                    String jsonBuzzDetal = BuzzAdapter.getMediaBuzzData.callService(request);
                    JSONArray buzzsArr = (JSONArray) (new JSONParser().parse(jsonBuzzDetal));
                    
                    List<String> listImgId = new LinkedList<>();
                    List<String> listGiftId = new LinkedList<>();
                    List<String> listVideoId = new LinkedList<>();
                    List<String> listCoverId = new LinkedList<>();
                    List<String> listAudioId = new LinkedList<>();
                    List<String> listStreamId = new LinkedList<>();
                    
                    for (Object item : buzzsArr) {
                        JSONObject buzz = (JSONObject) item;
                        
                        Long buzzType = (Long) buzz.get(ParamKey.BUZZ_TYPE);
                        if (buzzType != null){
                            String fileId = (String) buzz.get(ParamKey.FILE_ID);
                            String coverId = (String) buzz.get(ParamKey.COVER_ID);
                            if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS){
                                if (fileId != null && !fileId.equals(""))
                                    listImgId.add(fileId);
                            }
                            else if (buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                                if (fileId != null && !fileId.equals(""))
                                    listVideoId.add(fileId);
                            }
                            else if (buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                                if (coverId != null && !coverId.equals("")){
                                    listCoverId.add(coverId);
                                }
                                if (fileId != null && !fileId.equals(""))
                                    listAudioId.add(fileId);
                            }
                        }
                    }
                    
                    ListFileData map = InterCommunicator.getFileData(listImgId, listVideoId, listCoverId, listAudioId);
//                    ListFileUrl map = InterCommunicator.getFile(listImgId, listGiftId, listVideoId, listCoverId, listAudioId, listStreamId);
                    
                    JSONArray returnData = new JSONArray();
                    
                    for (Object buzzObj : buzzsArr) {
                        JSONObject buzz = (JSONObject) buzzObj;
                        Long privacy = (Long) buzz.get(ParamKey.PRIVACY);
                        Long buzzType = (Long) buzz.get(ParamKey.BUZZ_TYPE);
                        String fileId = (String) buzz.get(ParamKey.FILE_ID);
                        String coverId = (String) buzz.get(ParamKey.COVER_ID);
                        if (buzzType != null){
                            if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS){
                                if(fileId != null){
                                    FileData imgData = map.mapImg.get(fileId);
                                    if (imgData != null){
                                        JSONObject like = new JSONObject();
                                        like.put(ParamKey.IS_LIKE, buzz.get(ParamKey.IS_LIKE));
                                        like.put(ParamKey.LIKE_NUM, buzz.get(ParamKey.LIKE_NUM));

                                        JSONObject val = new JSONObject();
                                        val.put(ParamKey.BUZZ_VALUE, imgData.originalUrl);
                                        val.put(ParamKey.ORIGINAL_WIDTH, imgData.originalWidth);
                                        val.put(ParamKey.ORIGINAL_HEIGHT, imgData.originalHeight);
                                        val.put(ParamKey.THUMBNAIL_URL, imgData.thumbnailUrl);
                                        val.put(ParamKey.THUMBNAIL_WIDTH, imgData.thumbnailWidth);
                                        val.put(ParamKey.THUMBNAIL_HEIGHT, imgData.thumbnailHeight);

                                        val.put(ParamKey.BUZZ_ID, buzz.get(ParamKey.BUZZ_ID));
                                        val.put(ParamKey.BUZZ_TYPE, "image");
                                        val.put(ParamKey.COMMENT_NUM, buzz.get(ParamKey.COMMENT_NUM));
                                        val.put(ParamKey.BUZZ_TIME, buzz.get(ParamKey.BUZZ_TIME));
                                        val.put(ParamKey.USER_ID, buzz.get(ParamKey.USER_ID));
                                        val.put(ParamKey.IS_APPROVED_IMAGE, buzz.get(ParamKey.IS_APPROVED_IMAGE));
                                        val.put(ParamKey.LIKE_INFO, like);
                                        val.put(ParamKey.FILE_ID, fileId);
                                        val.put(ParamKey.IS_FAV, isFav);
                                        val.put(ParamKey.IS_MINE, isMine);
                                        val.put(ParamKey.PRIVACY, privacy);
                                        returnData.add(val);
                                    }
                                }
                            }
                            else if (buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                                if(fileId != null){
                                    FileData videoData = map.mapVideo.get(fileId);
                                    if (videoData != null){
                                        JSONObject like = new JSONObject();
                                        like.put(ParamKey.IS_LIKE, buzz.get(ParamKey.IS_LIKE));
                                        like.put(ParamKey.LIKE_NUM, buzz.get(ParamKey.LIKE_NUM));

                                        JSONObject val = new JSONObject();
                                        val.put(ParamKey.URL, videoData.fileUrl);
                                        val.put(ParamKey.BUZZ_VALUE, videoData.originalUrl);
                                        val.put(ParamKey.ORIGINAL_WIDTH, videoData.originalWidth);
                                        val.put(ParamKey.ORIGINAL_HEIGHT, videoData.originalHeight);
                                        val.put(ParamKey.THUMBNAIL_URL, videoData.thumbnailUrl);
                                        val.put(ParamKey.THUMBNAIL_WIDTH, videoData.thumbnailWidth);
                                        val.put(ParamKey.THUMBNAIL_HEIGHT, videoData.thumbnailHeight);
                                        if(videoData.fileDuration != null){
                                            val.put(ParamKey.FILE_DURATION, videoData.fileDuration);
                                        }else{
                                            val.put(ParamKey.FILE_DURATION, 0);
                                        }

                                        val.put(ParamKey.BUZZ_ID, buzz.get(ParamKey.BUZZ_ID));
                                        val.put(ParamKey.BUZZ_TYPE, "video");
                                        val.put(ParamKey.COMMENT_NUM, buzz.get(ParamKey.COMMENT_NUM));
                                        val.put(ParamKey.BUZZ_TIME, buzz.get(ParamKey.BUZZ_TIME));
                                        val.put(ParamKey.USER_ID, buzz.get(ParamKey.USER_ID));
                                        val.put(ParamKey.IS_APPROVED_IMAGE, buzz.get(ParamKey.IS_APPROVED_IMAGE));
                                        val.put(ParamKey.LIKE_INFO, like);
                                        val.put(ParamKey.FILE_ID, fileId);
                                        val.put(ParamKey.IS_FAV, isFav);
                                        val.put(ParamKey.IS_MINE, isMine);
                                        val.put(ParamKey.PRIVACY, privacy);
                                        returnData.add(val);
                                    }
                                }
                            }
                            else if (buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                                if(coverId != null && fileId != null){
                                    FileData cover = map.mapCover.get(coverId);
                                    FileData audio = map.mapAudio.get(fileId);
                                    if (audio != null){
                                        JSONObject like = new JSONObject();
                                        like.put(ParamKey.IS_LIKE, buzz.get(ParamKey.IS_LIKE));
                                        like.put(ParamKey.LIKE_NUM, buzz.get(ParamKey.LIKE_NUM));



                                        JSONObject val = new JSONObject();
                                        if(cover != null){
                                            val.put(ParamKey.BUZZ_VALUE, cover.originalUrl);
                                            val.put(ParamKey.THUMBNAIL_URL, cover.originalUrl);
                                            val.put(ParamKey.ORIGINAL_WIDTH, cover.originalWidth);
                                            val.put(ParamKey.ORIGINAL_HEIGHT, cover.originalHeight);
                                            val.put(ParamKey.THUMBNAIL_WIDTH, cover.originalWidth);
                                            val.put(ParamKey.THUMBNAIL_HEIGHT, cover.originalHeight);
                                        }
                                        if(audio.fileDuration != null){
                                            val.put(ParamKey.FILE_DURATION, audio.fileDuration);
                                        }else{
                                            val.put(ParamKey.FILE_DURATION, 0);
                                        }
                                        
                                        val.put(ParamKey.URL, audio.fileUrl);
                                        
                                        val.put(ParamKey.BUZZ_ID, buzz.get(ParamKey.BUZZ_ID));
                                        val.put(ParamKey.BUZZ_TYPE, "audio");
                                        val.put(ParamKey.COMMENT_NUM, buzz.get(ParamKey.COMMENT_NUM));
                                        val.put(ParamKey.BUZZ_TIME, buzz.get(ParamKey.BUZZ_TIME));
                                        val.put(ParamKey.USER_ID, buzz.get(ParamKey.USER_ID));
                                        val.put(ParamKey.IS_APPROVED_IMAGE, buzz.get(ParamKey.IS_APPROVED_IMAGE));
                                        val.put(ParamKey.LIKE_INFO, like);
                                        val.put(ParamKey.FILE_ID, fileId);
                                        val.put(ParamKey.IS_FAV, isFav);
                                        val.put(ParamKey.IS_MINE, isMine);
                                        val.put(ParamKey.PRIVACY, privacy);
                                        returnData.add(val);
                                    }
                                }
                            }
                        }
                    }
                    
                    JSONObject result = new JSONObject();
                    result.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                    result.put(ParamKey.DATA, returnData);
                    return result.toJSONString();
                }
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
            return ResponseMessage.UnknownError;
        }
        
    }
    
    public static class LoadAlbum implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) (new JSONParser().parse(umsStr));
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (ErrorCode.SUCCESS == code){
                    JSONArray data = (JSONArray) jo.get(ParamKey.DATA);
                    
                    List<String> listImage = new ArrayList<>();
                    List<String> listVideo = new ArrayList<>();
                    List<String> listAudio = new ArrayList<>();
                    
                    Iterator<Object> iterator = data.iterator();
                    while(iterator.hasNext()){
                        JSONObject albumObj = (JSONObject) iterator.next();
                        JSONArray imgArr = (JSONArray) albumObj.get("image_list");
                        if(imgArr.size() > 0){
                            JSONObject imgObj = (JSONObject) imgArr.get(0);
                            String imgId = (String) imgObj.get("image_id");
                            listImage.add(imgId);
                        }
                        
                    }
                    ListFileData map = InterCommunicator.getFileData(listImage);
                    
                    Iterator<Object> iterator2 = data.iterator();
                    while(iterator2.hasNext()){
                        JSONObject albumObj = (JSONObject) iterator2.next();
                        JSONArray imgArr = (JSONArray) albumObj.get("image_list");
                        JSONObject temp = new JSONObject();
                        if(imgArr.size() > 0){
                            JSONObject imgObj = (JSONObject) imgArr.get(0);
                            String imgId = (String) imgObj.get("image_id");
                            Long time = (Long) imgObj.get(ParamKey.TIME);

                            FileData img = map.mapImg.get(imgId);
                            
                            temp = img.toJsonObject();
                            temp.put(ParamKey.TIME, time);
                            Util.addDebugLog("temp=============="+temp);
                            
                        }
                        albumObj.put("image_list", temp);
                    }
                }
                return jo.toJSONString();
            } catch (Exception e) {
                Util.addErrorLog(e);
            }
            return ResponseMessage.UnknownError;
        }
        
    }
    
    public static class LoadAlbumImage implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) (new JSONParser().parse(umsStr));
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (ErrorCode.SUCCESS == code){
                    JSONArray data = (JSONArray) jo.get(ParamKey.DATA);
                    
                    List<String> listImage = new ArrayList<>();
                    List<String> listVideo = new ArrayList<>();
                    List<String> listAudio = new ArrayList<>();
                    
                    Iterator<Object> iterator = data.iterator();
                    while(iterator.hasNext()){
                        JSONObject imgObj = (JSONObject) iterator.next();
                        String imgId = (String) imgObj.get("image_id");
                        listImage.add(imgId);
                    }
                    ListFileData map = InterCommunicator.getFileData(listImage);
                    
                    
                    JSONArray listImageData = new JSONArray();
                     JSONObject joImg = new JSONObject();
                    Iterator<Object> iterator2 = data.iterator();
                    while(iterator2.hasNext()){
                        JSONObject imgObj = (JSONObject) iterator2.next();
                        String imgId = (String) imgObj.get("image_id");
                        Long time = (Long) imgObj.get(ParamKey.TIME);
                        String albumName = (String) imgObj.get(ParamKey.ALBUM_NAME);
                        String albumDes = (String) imgObj.get(ParamKey.ALBUM_DES);
                        Long privacy = (Long) imgObj.get(ParamKey.PRIVACY);
                        Long numberImage = (Long) imgObj.get("number_image");
                        joImg.put(ParamKey.ALBUM_NAME, albumName);
                        joImg.put(ParamKey.ALBUM_DES, albumDes);
                        joImg.put(ParamKey.PRIVACY, privacy);
                        joImg.put("number_image", numberImage);
                        FileData img = map.mapImg.get(imgId);
                        if(img != null){
                            JSONObject temp = new JSONObject();
                            temp = img.toJsonObject();
                            temp.put(ParamKey.TIME, time);
                            listImageData.add(temp);
                        }
                        joImg.put("list_image", listImageData);
                    }
                    jo.put(ParamKey.DATA, joImg);
                          
                }
                return jo.toJSONString();
            } catch (Exception e) {
                Util.addErrorLog(e);
            }
            return ResponseMessage.UnknownError;

        }

    }

    private static class UpdateAlbum implements IServiceAdapter{

        @Override
        public String callService(Request request) {
              try {
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) (new JSONParser().parse(umsStr));
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (ErrorCode.SUCCESS == code){
                    JSONObject albumObj = (JSONObject) jo.get(ParamKey.DATA);
                    
                    List<String> listImage = new ArrayList<>();
                    List<String> listVideo = new ArrayList<>();
                    List<String> listAudio = new ArrayList<>();
                    
                    JSONArray imgArr1 = (JSONArray) albumObj.get("image_list");
                        if(imgArr1.size() > 0){
                            JSONObject imgObj = (JSONObject) imgArr1.get(0);
                            String imgId = (String) imgObj.get("image_id");
                            listImage.add(imgId);
                        }
                        
                    ListFileData map = InterCommunicator.getFileData(listImage);
                    
                        JSONArray imgArr = (JSONArray) albumObj.get("image_list");
                        JSONObject temp = new JSONObject();
                        if(imgArr.size() > 0){
                            JSONObject imgObj = (JSONObject) imgArr.get(0);
                            String imgId = (String) imgObj.get("image_id");
                            Long time = (Long) imgObj.get(ParamKey.TIME);

                            FileData img = map.mapImg.get(imgId);
                            
                            temp = img.toJsonObject();
                            temp.put(ParamKey.TIME, time);
                            Util.addDebugLog("temp=============="+temp);
                            
                        }
                        albumObj.put("image_list", temp);
                }
                return jo.toJSONString();
            } catch (Exception e) {
                Util.addErrorLog(e);
            }
            return ResponseMessage.UnknownError;
        }
    }
    
    private static class CreateAccountFromFBid implements IServiceAdapter{
        @Override
        public String callService(Request request) {
            String result = null;
            try {
                result = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            } catch (Exception e) {
                Util.addErrorLog(e);
            }
            return result;
        }
    }
    
    private static class ListUserOnlineAlert implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            String result = null;
            try {
                request.put(ParamKey.API_NAME, API.LIST_ONLINE_ALERT);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject jo = (JSONObject) (new JSONParser().parse(umsStr));
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (ErrorCode.SUCCESS == code){
                    String userId = (String) request.getParamValue(ParamKey.USER_ID);
                    List<String> listUser = new ArrayList<>();
//                    Util.addDebugLog("jo.get(ParamKey.DATA)===="+jo.get(ParamKey.DATA));
                    JSONArray userArr = (JSONArray) jo.get(ParamKey.DATA);
                    for (Object obj : userArr){
                        JSONObject user = (JSONObject) obj;
                        String friendId = (String) user.get(ParamKey.USER_ID);
                        listUser.add(friendId);
                    }
                    JSONArray psArr = InterCommunicator.getUserPresentList(userId, listUser);
                    Map<String, User> mUserInfo = ParseData.parseUserInfo(psArr);
                    
                    JSONArray returnData = new JSONArray();
                    for (Object obj : userArr){
                        JSONObject user = (JSONObject) obj;
                        String friendId = (String) user.get(ParamKey.USER_ID);
                        User userInfo = mUserInfo.get(friendId);
                        if(userInfo.isOnline){
                            returnData.add(user);
                        }
                    }
                    
                    JSONObject returnObj = new JSONObject();
                    returnObj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
//                    returnObj.put(ParamKey.DATA, returnData);
                    returnObj.put("count", returnData.size());
                    return returnObj.toJSONString();
                }
            } catch (Exception e) {
                Util.addErrorLog(e);
            }
            return result;
        }
        
    }
}
