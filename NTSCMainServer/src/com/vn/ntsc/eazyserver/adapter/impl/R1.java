/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.FileUrl;
import com.vn.ntsc.otherservice.entity.impl.User;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ClickPushNotificationApi;
import java.util.HashMap;

/**
 *
 * @author tuannxv00804
 */
public class R1 {

    public static final ListUserAdapter listUserAdapter = new ListUserAdapter();
    public static final UserInforAdapter userInforAdapter = new UserInforAdapter();
    public static final BasicInforAdapter basicInforAdapter = new BasicInforAdapter();
    public static final ListNotificationAdapter listNotification = new ListNotificationAdapter();
    public static final ClickNewsNotificationAdapter clickNewsNotification = new ClickNewsNotificationAdapter();
    public static final ListCallLogdapter listCallLog = new ListCallLogdapter();
    public static final ClickLikeNotificationAdapter clickLikeNotification = new ClickLikeNotificationAdapter();
    public static final ClickNotiNotificationAdapter clickNotiNotification = new ClickNotiNotificationAdapter();
    public static final DeleteNotificationAdapter deleteNotification = new DeleteNotificationAdapter();
    public static final DeleteFootPrintAdapter deleteFootprint = new DeleteFootPrintAdapter();
    public static final DeleteCheckOutFootPrintAdapter deleteCheckOutFootprint = new DeleteCheckOutFootPrintAdapter();

    static class DeleteCheckOutFootPrintAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            Util.addDebugLog("Test request DeleteCheckoutFootprint " + request);
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            Util.addDebugLog("Test for umsStr " + umsStr);
            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                Util.addDebugLog("Test for jo umsStr " + jo.toJSONString());
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                //
                JSONArray usersArr = (JSONArray) jo.get(ParamKey.DATA);
                if (usersArr != null && !usersArr.isEmpty()) {
                    addInfor(request, usersArr);
                }
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;

            }
            return result;
        }

    }

    static class DeleteFootPrintAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            Util.addDebugLog("Test request DeleteFootprint " + request);
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            Util.addDebugLog("Test for umsStr " + umsStr);
            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                Util.addDebugLog("Test for jo umsStr " + jo.toJSONString());
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                //
                JSONArray usersArr = (JSONArray) jo.get(ParamKey.DATA);
                if (usersArr != null && !usersArr.isEmpty()) {
                    addInfor(request, usersArr);
                }
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;

            }
            return result;
        }

    }

    static class DeleteNotificationAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            Util.addDebugLog("Test request DeleteNotification " + request);
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            Util.addDebugLog("Test for umsStr " + umsStr);
            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                Util.addDebugLog("Test for jo umsStr " + jo.toJSONString());
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                //
                JSONArray usersArr = (JSONArray) jo.get(ParamKey.DATA);
                if (usersArr != null && !usersArr.isEmpty()) {
                    addInfor(request, usersArr);
                }
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;

            }
            return result;
        }

    }

    static class ClickNotiNotificationAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            Util.addDebugLog("Test request ClickNotiNotification " + request);
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            Util.addDebugLog("Test for umsStr " + umsStr);
            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                Util.addDebugLog("Test for jo umsStr " + jo.toJSONString());
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                //
                JSONArray usersArr = (JSONArray) jo.get(ParamKey.DATA);
                if (usersArr != null && !usersArr.isEmpty()) {
                    addInfor(request, usersArr);
                }
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;

            }
            return result;
        }

    }

    static class ClickLikeNotificationAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            Util.addDebugLog("Test request ClickLikeLogin " + request);
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            Util.addDebugLog("Test for umsStr " + umsStr);
            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                Util.addDebugLog("Test for jo umsStr " + jo.toJSONString());
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                //
                JSONArray usersArr = (JSONArray) jo.get(ParamKey.DATA);
                if (usersArr != null && !usersArr.isEmpty()) {
                    addInfor(request, usersArr);
                }
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;

            }
            return result;
        }

    }

    static class ClickNewsNotificationAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            Util.addDebugLog("Test request ClickNewsLogin " + request);
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            Util.addDebugLog("Test for umsStr " + umsStr);
            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                Util.addDebugLog("Test for jo umsStr " + jo.toJSONString());
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                //
                JSONArray usersArr = (JSONArray) jo.get(ParamKey.DATA);
                if (usersArr != null && !usersArr.isEmpty()) {
                    addInfor(request, usersArr);
                }
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;

            }
            return result;
        }

    }

    static class ListUserAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;

            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            Util.addDebugLog("search by name response----------------------------------------" + umsStr);
            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                //
                Util.addDebugLog("request----------------------------------------" + request);
                Util.addDebugLog("jo before----------------------------------------" + jo);
                JSONArray usersArr = (JSONArray) jo.get(ParamKey.DATA);
                if (usersArr != null && !usersArr.isEmpty()) {
                    addInfor(request, usersArr);

                }
                Util.addDebugLog("jo after----------------------------------------" + jo);
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;

            }
            return result;
        }
    }

    static class ListCallLogdapter implements IServiceAdapter {

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
                //
                JSONArray usersArr = (JSONArray) jo.get(ParamKey.DATA);
                if (usersArr != null && !usersArr.isEmpty()) {
                    LinkedList<String> llEmail = new LinkedList<>();
                    for (Object usersArr1 : usersArr) {
                        JSONObject user = (JSONObject) usersArr1;
                        String userID = (String) user.get(ParamKey.PARTNER_ID);
                        llEmail.add(userID);
                    }

                    String uId = (String) request.getParamValue(ParamKey.USER_ID);
                    JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                    Queue<String> removeList = new LinkedList<>();
                    for (int i = 0; i < psArr.size(); i++) {
                        JSONObject ps = (JSONObject) psArr.get(i);
                        if (ps != null) {
                            try {

                                Double dist = (Double) ps.get(ParamKey.DIST);
                                String name = (String) ps.get(ParamKey.USER_NAME);
                                String lastLogin = (String) ps.get(ParamKey.LAST_LOGIN);
                                Boolean isVideo = (Boolean) ps.get(ParamKey.VIDEO_CALL_WAITING);
                                Boolean isVoice = (Boolean) ps.get(ParamKey.VOICE_CALL_WAITING);
                                Boolean isOnline = (Boolean) ps.get(ParamKey.IS_ONLINE);
                                String ava = (String) ps.get(ParamKey.AVATAR_ID);
                                JSONObject user = (JSONObject) usersArr.get(i);
                                user.put(ParamKey.DIST, dist);
                                user.put("partner_name", name);
                                user.put(ParamKey.LAST_LOGIN, lastLogin);
                                user.put(ParamKey.VIDEO_CALL_WAITING, isVideo);
                                user.put(ParamKey.VOICE_CALL_WAITING, isVoice);
                                user.put(ParamKey.IS_ONLINE, isOnline);
                                if (ava != null) {
                                    user.put(ParamKey.AVATAR_ID, ava);
                                }
                            } catch (Exception ex) {
                                Util.addErrorLog(ex);

                                return jo.toJSONString();
                            }
                        } else {
                            JSONObject user = (JSONObject) usersArr.get(i);
                            String userID = (String) user.get(ParamKey.NOTIFICATION_USER_ID);
                            removeList.add(userID);
                        }
                    }
                    if (!removeList.isEmpty()) {
                        while (!removeList.isEmpty()) {
                            String userId = removeList.poll();
                            for (int i = 0; i < usersArr.size(); i++) {
                                JSONObject user = (JSONObject) usersArr.get(i);
                                String id = (String) user.get(ParamKey.NOTIFICATION_USER_ID);
                                if (userId.equals(id)) {
                                    usersArr.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                }
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;
            }
            return result;
        }
    }

    static class ListNotificationAdapter implements IServiceAdapter {

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
                //
                JSONArray usersArr = (JSONArray) jo.get(ParamKey.DATA);
                if (usersArr != null && !usersArr.isEmpty()) {
                    LinkedList<String> llEmail = new LinkedList<>();
                    for (Object usersArr1 : usersArr) {
                        JSONObject user = (JSONObject) usersArr1;
                        String userID = (String) user.get(ParamKey.NOTIFICATION_USER_ID);
                        llEmail.add(userID);
                    }

                    String uId = (String) request.getParamValue(ParamKey.USER_ID);
                    JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                    Queue<String> removeList = new LinkedList<>();
                    if (psArr != null) {
                        for (int i = 0; i < psArr.size(); i++) {
                            JSONObject ps = (JSONObject) psArr.get(i);
                            if (ps != null) {
                                try {

                                    Double dist = (Double) ps.get(ParamKey.DIST);
                                    String lastLogin = (String) ps.get(ParamKey.LAST_LOGIN);
                                    Boolean isVideo = (Boolean) ps.get(ParamKey.VIDEO_CALL_WAITING);
                                    Boolean isVoice = (Boolean) ps.get(ParamKey.VOICE_CALL_WAITING);
                                    JSONObject user = (JSONObject) usersArr.get(i);
                                    user.put(ParamKey.DIST, dist);
                                    user.put(ParamKey.LAST_LOGIN, lastLogin);
                                    user.put(ParamKey.VOICE_CALL_WAITING, isVoice);
                                    user.put(ParamKey.VIDEO_CALL_WAITING, isVideo);
                                } catch (Exception ex) {
                                    Util.addErrorLog(ex);
                                    return jo.toJSONString();
                                }
                            } else {
                                JSONObject user = (JSONObject) usersArr.get(i);
                                String userID = (String) user.get(ParamKey.NOTIFICATION_USER_ID);
                                removeList.add(userID);
                            }
                        }
                    }
                    if (!removeList.isEmpty()) {
                        while (!removeList.isEmpty()) {
                            String userId = removeList.poll();
                            for (int i = 0; i < usersArr.size(); i++) {
                                JSONObject user = (JSONObject) usersArr.get(i);
                                String id = (String) user.get(ParamKey.NOTIFICATION_USER_ID);
                                if (userId.equals(id)) {
                                    usersArr.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                }
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;
            }
            return result;
        }
    }

    static class UserInforAdapter implements IServiceAdapter {

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
                //
                JSONObject usersObj = (JSONObject) jo.get(ParamKey.DATA);

                LinkedList<String> llEmail = new LinkedList<>();
                String userID = (String) request.getParamValue(ParamKey.REQUEST_USER_ID);
                String uId = (String) request.getParamValue(ParamKey.USER_ID);
                if (userID == null) {
                    llEmail.add(uId);
                } else {
                    llEmail.add(userID);
                }

                JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                if (psArr == null) {
                    return umsStr;
                }

                for (Object psArr1 : psArr) {
                    JSONObject ps = (JSONObject) psArr1;
                    if (ps != null) {
                        try {
                            Boolean isOnline = (Boolean) ps.get(ParamKey.IS_ONLINE);
                            Double lon = (Double) ps.get(ParamKey.LONGITUDE);
                            Double lat = (Double) ps.get(ParamKey.LATITUDE);
                            Double dist = (Double) ps.get(ParamKey.DIST);
                            String lastLogin = (String) ps.get(ParamKey.LAST_LOGIN);
                            Boolean isVideo = (Boolean) ps.get(ParamKey.VIDEO_CALL_WAITING);
                            Boolean isVoice = (Boolean) ps.get(ParamKey.VOICE_CALL_WAITING);
                            usersObj.put(ParamKey.IS_ONLINE, isOnline);
                            usersObj.put(ParamKey.LONGITUDE, lon);
                            usersObj.put(ParamKey.LATITUDE, lat);
                            usersObj.put(ParamKey.DIST, dist);
                            usersObj.put(ParamKey.LAST_LOGIN, lastLogin);
                            usersObj.put(ParamKey.VIDEO_CALL_WAITING, isVideo);
                            usersObj.put(ParamKey.VOICE_CALL_WAITING, isVoice);
                        } catch (Exception ex) {
                            Util.addErrorLog(ex);

                        }

                    } else {
                        usersObj.put(ParamKey.IS_ONLINE, false);
                        usersObj.put(ParamKey.LONGITUDE, 0);
                        usersObj.put(ParamKey.LATITUDE, 0);
                    }
                }
                
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                
                String avaId = (String) usersObj.get(ParamKey.AVATAR_ID);
                if (avaId != null){
                    List<String> listImgId = new LinkedList<>();
                    listImgId.add(avaId);
                    HashMap<String, FileUrl> imgMap = InterCommunicator.getImage(listImgId);
                    FileUrl url = imgMap.get(avaId);
                    if (url != null){
                        usersObj.put(ParamKey.THUMBNAIL_URL, url.getThumbnail());
                        usersObj.put(ParamKey.ORIGINAL_URL, url.getOriginalUrl());
                    }
                }
                String requestId = (String) request.getParamValue(ParamKey.REQUEST_USER_ID);

                // chat number
                if (requestId != null && !requestId.isEmpty() && userId != null) {
//                    String from = (String) request.getParamValue(ParamKey.USER_ID);
//                     get unread number
                    request.reqObj.put(ParamKey.FRIEND_LIST, llEmail);
                    request.put(ParamKey.API_NAME, API.GET_UNREAD_NUMBER);
                    String resutlChat = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
                    JSONArray chatJsonArr = new JSONArray();
                    try {
                        chatJsonArr = (JSONArray) new JSONParser().parse(resutlChat);
                    } catch (ParseException ex) {
                        Util.addErrorLog(ex);
                    }
                    usersObj.put(ParamKey.UNREAD_NUMBER, 0);
                    for (Object chatJsonArr1 : chatJsonArr) {
                        JSONObject chat = (JSONObject) chatJsonArr1;
                        if (chat != null) {
                            try {
                                Long unread = (Long) chat.get(ParamKey.UNREAD_NUMBER);
                                usersObj.put(ParamKey.UNREAD_NUMBER, unread);
                            } catch (Exception ex) {
                                Util.addErrorLog(ex);
                            }

                        }
                    }
//                    if (isNoti != null && isNoti == Constant.YES && !requestId.equals(from)) {
//                        String ip = (String) request.getParamValue(ParamKey.IP);
//                        JSONObject notiRequest = new JSONObject();
//                        notiRequest.put(ParamKey.API, API.NOTI_CHECK_PROFILE);
//                        notiRequest.put(ParamKey.IP, ip);
//                        notiRequest.put(ParamKey.NOTI_TO_USER_ID, requestId);
//                        notiRequest.put(ParamKey.NOTI_FROM_USER_ID, from);
//                        InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
//                    }
                    usersObj.remove(ParamKey.IS_NOTI);
                    usersObj.remove("unlck_chk_out");
                }

                // buzz number
                String buzzNumberUserId = requestId != null && !requestId.equals(uId) ? requestId : uId;
                request.put(ParamKey.USER_ID, buzzNumberUserId);
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

                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;
            }
            return result;
        }
    }

    static class BasicInforAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            JSONObject jo = new JSONObject();
//            String umsStr = InterCommunicator.sendRequest( request.toJson(), Config.UMSServerIP, Config.UMSPort );
            try {
                LinkedList<String> llEmail = new LinkedList<>();
                String userID = (String) request.getParamValue(ParamKey.REQUEST_USER_ID);
                String uId = (String) request.getParamValue(ParamKey.USER_ID);
                if (userID == null) {
                    llEmail.add(uId);
                } else {
                    llEmail.add(userID);
                }

                //get favourist infor
                JSONObject dataObj = new JSONObject();

                String memo = null; //khanhdd

                int isFav = Constant.FLAG.OFF;
                int chatPoint = 0;
                Long viewImagePoint = 0L;
                Long watchVideoPoint = 0L;
                Long listenAudioPoint = 0L;
                Long viewImageTime = 0L;
                Long watchVideoTime = 0L;
                Long listenAudioTime = 0L;
                Long job = null;
                Boolean isContacted = false;

                Integer isPurchase = 0;
                User user = UserDAO.getUserInfor(uId);
                if (user.lastPurchaseTime != null || user.gender == 1) {
                    isPurchase = 1;
                }

                if (userID != null && !userID.equals(uId)) {
                    JSONArray listConnectionInfor = InterCommunicator.getConnectionInfor(request, llEmail);
                    if (listConnectionInfor != null && !listConnectionInfor.isEmpty()) {
                        JSONObject json = (JSONObject) listConnectionInfor.get(0);
                        isFav = Util.getLongParam(json, ParamKey.IS_FAV).intValue();
                        chatPoint = Util.getLongParam(json, "chat_point").intValue();
                        viewImagePoint = Util.getLongParam(json, "view_image_point");
                        job = Util.getLongParam(json, "job");
                        if (json.get("is_contacted") != null) {
                            isContacted = (Boolean) json.get("is_contacted");
                        }
                        viewImageTime = Util.getLongParam(json, "view_image_time");
                        watchVideoPoint = Util.getLongParam(json, "watch_video_point");
                        watchVideoTime = Util.getLongParam(json, "watch_video_time");
                        listenAudioPoint = Util.getLongParam(json, "listen_audio_point");
                        listenAudioTime = Util.getLongParam(json, "listen_audio_time");
                        memo = Util.getStringParam(json, "memo");// khanhdd 06/1/2017

                    } else {
                        jo.put(ParamKey.ERROR_CODE, ErrorCode.USER_NOT_EXIST);
                        return jo.toJSONString();
                    }
                }
                dataObj.put(ParamKey.IS_FAV, isFav);
                if (job != null) {
                    dataObj.put("job", job);
                }
                dataObj.put("is_contacted", isContacted);
                dataObj.put("chat_point", chatPoint);
                dataObj.put("view_image_point", viewImagePoint);
                dataObj.put("view_image_time", viewImageTime);
                dataObj.put("watch_video_point", watchVideoPoint);
                dataObj.put("watch_video_time", watchVideoTime);
                dataObj.put("listen_audio_point", listenAudioPoint);
                dataObj.put("listen_audio_time", listenAudioTime);
                dataObj.put("is_purchase", isPurchase);

                // khanhdd 06/01/2017
                if (memo != null) {
                    dataObj.put("memo", memo);
                }

                // get user presentation infor
                JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                if (psArr == null) {
                    return ResponseMessage.UnknownError;
                } else if (psArr.isEmpty()) {
                    jo.put(ParamKey.ERROR_CODE, ErrorCode.USER_NOT_EXIST);
                    return jo.toJSONString();
                }

                for (Object psArr1 : psArr) {
                    JSONObject ps = (JSONObject) psArr1;
                    if (ps != null) {
                        try {
                            Boolean isOnline = (Boolean) ps.get(ParamKey.IS_ONLINE);
                            String id = (String) ps.get(ParamKey.USER_ID);
                            Double lon = (Double) ps.get(ParamKey.LONGITUDE);
                            Double lat = (Double) ps.get(ParamKey.LATITUDE);
                            String userName = (String) ps.get(ParamKey.USER_NAME);
                            String avaId = (String) ps.get(ParamKey.AVATAR_ID);
                            Long gender = (Long) ps.get(ParamKey.GENDER);
                            Double dist = (Double) ps.get(ParamKey.DIST);
                            Boolean isVideo = (Boolean) ps.get(ParamKey.VIDEO_CALL_WAITING);
                            Boolean isVoice = (Boolean) ps.get(ParamKey.VOICE_CALL_WAITING);
                            Long age = (Long) ps.get("age");
                            Long region = (Long) ps.get(ParamKey.REGION);

                            dataObj.put(ParamKey.IS_ONLINE, isOnline);
                            dataObj.put(ParamKey.USER_ID, id);
                            dataObj.put(ParamKey.LONGITUDE, lon);
                            dataObj.put(ParamKey.LATITUDE, lat);
                            dataObj.put(ParamKey.USER_NAME, userName);
                            dataObj.put(ParamKey.DIST, dist);
                            dataObj.put("age", age);
                            dataObj.put(ParamKey.REGION, region);
                            if (avaId != null) {
                                dataObj.put(ParamKey.AVATAR_ID, avaId);
                            }
                            dataObj.put(ParamKey.GENDER, gender);
                            dataObj.put(ParamKey.VIDEO_CALL_WAITING, isVideo);
                            dataObj.put(ParamKey.VOICE_CALL_WAITING, isVoice);

                        } catch (Exception ex) {
                            Util.addErrorLog(ex);
                        }
                    }
                }
//                dataObj.put(ParamKey.USER_ID, userID);
                jo.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                jo.put(ParamKey.DATA, dataObj);
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;
            }
            return result;
        }
    }

    public static void addInfor(Request request, JSONArray usersArr) {
        LinkedList<String> listImgId = new LinkedList<>();
        LinkedList<String> llEmail = new LinkedList<>();
        List<UMSObject> lUMS = new ArrayList<>();
        for (int i = 0; i < usersArr.size(); i++) {
            JSONObject obj = (JSONObject) usersArr.get(i);
            String userId = (String) obj.get(ParamKey.USER_ID);
            String avaId = (String) obj.get(ParamKey.AVATAR_ID);
            UMSObject ums = new UMSObject(userId, i);
            lUMS.add(ums);
            llEmail.add(userId);
            if (avaId != null)
                listImgId.add(avaId);
        }
        request.reqObj.put(ParamKey.FRIEND_LIST, llEmail);
        request.put(ParamKey.API_NAME, API.GET_UNREAD_NUMBER);
        Util.addDebugLog("request----------------------------------------" + request);
        String resutlChat = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
        Util.addDebugLog("resutlChat----------------------------------------" + resutlChat);
        JSONArray chatJsonArr = new JSONArray();
        if (resutlChat != null){
            try {
                chatJsonArr = (JSONArray) new JSONParser().parse(resutlChat);
            } catch (ParseException ex) {
                Util.addErrorLog(ex);
            }
        }
        
        if (!chatJsonArr.isEmpty()) {
            Collections.sort(lUMS);
            List<ChatObject> lChat = new ArrayList<>();
            for (Object chatJsonArr1 : chatJsonArr) {
                JSONObject obj = (JSONObject) chatJsonArr1;
                String userId = (String) obj.get(ParamKey.FRDID);
                Long number = (Long) obj.get(ParamKey.UNREAD_NUMBER);
                if (number != 0) {
                    ChatObject chat = new ChatObject(userId, number.intValue());
                    lChat.add(chat);
                }
            }
            if (!lChat.isEmpty()) {
                Collections.sort(lChat);
                int meetCount = 0;
                int chatCount = 0;
                while (meetCount < lUMS.size() && chatCount < lChat.size()) {
                    String meetUserId = lUMS.get(meetCount).userId;
                    String chatUserId = lChat.get(chatCount).userId;
                    if (meetUserId.compareTo(chatUserId) == 0) {
                        int index = lUMS.get(meetCount).index;
                        int num = lChat.get(chatCount).number;
                        JSONObject obj = (JSONObject) usersArr.get(index);
                        obj.put(ParamKey.UNREAD_NUMBER, num);
                        meetCount++;
                        chatCount++;
                    } else if (meetUserId.compareTo(chatUserId) > 0) {
                        chatCount++;
                    } else {
                        meetCount++;
                    }
                }
            }
        }
        //add by Huy 201721Sep
        
        String uId = (String) request.getParamValue(ParamKey.USER_ID);
        JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
        if (psArr == null) {
            return;
        }
        
        //Linh add 10080
        HashMap<String, FileUrl> imgMap = InterCommunicator.getImage(listImgId);
        
        LinkedList<String> removeList = new LinkedList<>();
        for (int i = 0; i < psArr.size(); i++) {
            JSONObject ps = (JSONObject) psArr.get(i);
            if (ps != null) {
                try {
                    String userName = (String) ps.get(ParamKey.USER_NAME);
                    Boolean isOnline = (Boolean) ps.get(ParamKey.IS_ONLINE);
                    Double lon = (Double) ps.get(ParamKey.LONGITUDE);
                    Double lat = (Double) ps.get(ParamKey.LATITUDE);
                    Double dist = (Double) ps.get(ParamKey.DIST);
                    String lastLogin = (String) ps.get(ParamKey.LAST_LOGIN);
                    Boolean isVideo = (Boolean) ps.get(ParamKey.VIDEO_CALL_WAITING);
                    Boolean isVoice = (Boolean) ps.get(ParamKey.VOICE_CALL_WAITING);
                    String avaId = (String) ps.get(ParamKey.AVATAR_ID);
                    JSONObject user = (JSONObject) usersArr.get(i);
                    user.put(ParamKey.USER_NAME, userName);
                    user.put(ParamKey.IS_ONLINE, isOnline);
                    user.put(ParamKey.LONGITUDE, lon);
                    user.put(ParamKey.LATITUDE, lat);
                    user.put(ParamKey.DIST, dist);
                    user.put(ParamKey.LAST_LOGIN, lastLogin);
                    user.put(ParamKey.VIDEO_CALL_WAITING, isVideo);
                    user.put(ParamKey.VOICE_CALL_WAITING, isVoice);
                    
                    FileUrl url = imgMap.get(avaId);
                    if (url != null){
                        user.put(ParamKey.THUMBNAIL_URL, url.getThumbnail());
                        user.put(ParamKey.ORIGINAL_URL, url.getOriginalUrl());
                    }
                    
                } catch (Exception ex) {
                    Util.addErrorLog(ex);

                }
            } else {
                JSONObject user = (JSONObject) usersArr.get(i);
                String userID = (String) user.get(ParamKey.USER_ID);
                removeList.add(userID);
            }
        }
        
        if (!removeList.isEmpty() && !(request.getParamValue(ParamKey.API_NAME).equals(API.GET_FRIEND))) {
            while (!removeList.isEmpty()) {
                String userId = removeList.poll();
                for (int i = 0; i < usersArr.size(); i++) {
                    JSONObject user = (JSONObject) usersArr.get(i);
                    String id = (String) user.get(ParamKey.USER_ID);
                    if (userId.equals(id)) {
                        usersArr.remove(i);
                        break;
                    }
                }
            }
        }

        getConnectionInfor(request, usersArr, llEmail);
    }

    static void getConnectionInfor(Request request, JSONArray meetJsonArray, List<String> listEmail) {
        JSONArray listConnectionInfor = InterCommunicator.getConnectionInfor(request, listEmail);
        Util.addDebugLog("listConnectionInfor----------------------------------------" + listConnectionInfor);
        for (Object obj : meetJsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            String ownerId = (String) jsonObj.get(ParamKey.USER_ID);
            for (Object connectionObj : listConnectionInfor) {
                JSONObject connectionJson = (JSONObject) connectionObj;
                String id = Util.getStringParam(connectionJson, "rqt_id");
                if (ownerId.equals(id)) {
                    int isFav = Util.getLongParam(connectionJson, "is_fav").intValue();
                    jsonObj.put(ParamKey.IS_FAV, isFav);
                    Boolean isContacted = (Boolean) connectionJson.get("is_contacted");
                    jsonObj.put("is_contacted", isContacted);
                    break;
                }
            }
        }
    }

    private static class UMSObject implements Comparable<UMSObject> {

        public String userId;
        public int index;

        public UMSObject(String userId, int index) {
            super();
            this.userId = userId;
            this.index = index;
        }

        @Override
        public int compareTo(UMSObject obj) {
            //ascending order
            return this.userId.compareTo(obj.userId);
        }
    }

    private static class ChatObject implements Comparable<ChatObject> {

        public String userId;
        public int number;

        public ChatObject(String userId, int number) {
            super();
            this.userId = userId;
            this.number = number;
        }

        @Override
        public int compareTo(ChatObject obj) {
            //ascending order
            return this.userId.compareTo(obj.userId);
        }
    }
}
