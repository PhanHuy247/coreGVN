/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.inspection.version.InspectionVersionDAO;
import eazycommon.util.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.FileUrl;
import com.vn.ntsc.otherservice.entity.impl.User;

/**
 *
 * @author tuannxv00804
 */
public class MeetPeople implements IServiceAdapter {

    public static final GetMeetPeople getMeetPeople = new GetMeetPeople();

    @Override
    public String callService(Request request) {
        try {
            String requestString = request.toJson();
            String result = InterCommunicator.sendRequest(requestString, Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
            return result;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return ResponseMessage.UnknownError;
        }
    }

    private static final String EMPTY_LIST_RESPONSE;

    static {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
        obj.put(ParamKey.DATA, new JSONArray());
        EMPTY_LIST_RESPONSE = obj.toJSONString();
    }

    private static class GetMeetPeople implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String resultMeet;
            String requestString = "";
            JSONParser parser = new JSONParser();
            String safaryVersion;

            try {
                safaryVersion = InspectionVersionDAO.getIOSTurnOffSafaryVersion();
                String token = request.token;
//                if (isCheck(token, safaryVersion)) {
//                    request.reqObj.put("isCheck", true);
//                } else {
//                    request.reqObj.put("isCheck", false);
//                }
//                
//                if (isCheckFemale(token, safaryVersion)) {                    
//                    request.reqObj.put("isCheckFemale", true);
////                    String userID = UserSessionDAO.getUserIdFromToken(token);
////                    request.reqObj.put("userid", userID);
//                } else {
//                    request.reqObj.put("isCheckFemale", false);
//                }                

            } catch (EazyException ex) {
                Logger.getLogger(MeetPeople.class.getName()).log(Level.SEVERE, null, ex);
            }
            requestString = request.toJson();

            try {

                resultMeet = InterCommunicator.sendRequest(requestString, Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return EMPTY_LIST_RESPONSE;
            }

            JSONObject meetJson;
            try {
                meetJson = (JSONObject) parser.parse(resultMeet);
                
                Long code = (Long) meetJson.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    JSONArray meetJsonArray = (JSONArray) meetJson.get(ParamKey.DATA);
                    List<MeetObject> lMeet = new ArrayList<>();
                    List<String> lEmail = new ArrayList<>();
                    List<String> lImgId = new ArrayList<>();
                    Util.addDebugLog("GET MEET PEOPLE REQUEST STRING MEET ARRAY START" + meetJsonArray.size());
                    Util.addDebugLog("GET MEET PEOPLE REQUEST STRING " + requestString);

                    
                    for (int i = 0; i < meetJsonArray.size(); i++) {
                        JSONObject obj = (JSONObject) meetJsonArray.get(i);
                        String userId = (String) obj.get(ParamKey.USER_ID);
                        String avaId = (String) obj.get(ParamKey.AVATAR_ID);

                        MeetObject meet = new MeetObject(userId, i);
                        lMeet.add(meet);
                        lEmail.add(userId);
                        
                        if (avaId != null)
                            lImgId.add(avaId);
                    }

                    Collections.sort(lMeet);
                    
                    getUnReadMessageNumber(request, lMeet, meetJsonArray, lEmail);
                    getConnectionInfor(request, meetJsonArray, lEmail);
                    
                    // Linh #10180 2017/09/21
                    getImageUrl(meetJsonArray, lImgId);
                    
                    long statusStart = System.currentTimeMillis();
                    getStatus(request, lEmail, lMeet, meetJsonArray);
                    long statusEnd = System.currentTimeMillis();
                    statusEnd -= statusStart;
                    if (statusEnd >= 1500) {
                        Util.addInfoLog("Meetpeople : get status slow : " + statusEnd);
                    }

                    long removeStart = System.currentTimeMillis();
                    removeBlockUser(request, meetJsonArray);
                    long removeEnd = System.currentTimeMillis();
                    removeEnd -= removeStart;
                    if (removeEnd >= 1500) {
                        Util.addInfoLog("Meetpeople : remove block slow : " + removeEnd);
                    }
                    Util.addDebugLog("GET MEET PEOPLE REQUEST STRING MEET ARRAY AFTER " + meetJsonArray.size());
                } else {
                    return resultMeet;
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return EMPTY_LIST_RESPONSE;
            }
            return meetJson.toJSONString();
        }

        private boolean isSafeUser(String userId, String safaryVersion) {
            try {
                DBObject dbObject = UserDAO.getUserInforJSON(userId);

                if (dbObject == null) {
                    Util.addDebugLog("GET MEET PEOPLE isSafeUser FALSE ");
                    return false;
                }
                Util.addDebugLog("GET MEET PEOPLE isSafeUser " + dbObject.toString());
                String appVersion = (String) dbObject.get(UserdbKey.USER.APP_VERSION);
                Integer safetyUser = (Integer) dbObject.get(UserdbKey.USER.SAFE_USER);
                Util.addDebugLog("GET MEET PEOPLE isSafeUser safetyUser " + safetyUser);
                if (safetyUser != null) {
                    if (safetyUser == 1) {
                        Util.addDebugLog("GET MEET PEOPLE isSafeUser TRUE " + safetyUser);
                        return true;
                    }
                }
                if (appVersion.equals(safaryVersion)) {
                    return true;
                }

            } catch (EazyException ex) {
                Logger.getLogger(MeetPeople.class.getName()).log(Level.SEVERE, null, ex);
            }
            Util.addDebugLog("GET MEET PEOPLE isSafeUser FALSE ");
            return false;
        }

        private boolean isCheck(String token, String safaryVersion) {
            try {
                String userIdfromToken = UserSessionDAO.getUserIdFromToken(token);
                DBObject dbObject = UserDAO.getUserInforJSON(userIdfromToken);

                if (dbObject == null) {
                    Util.addDebugLog("GET MEET PEOPLE isSafeUser FALSE ");
                    return false;
                }

                Integer gender = (Integer) dbObject.get(UserdbKey.USER.GENDER);
                Integer deviceType = (Integer) dbObject.get(UserdbKey.USER.DEVICE_TYPE);
                if (UserSessionDAO.getAppVersionFromToken(token).equals(safaryVersion) && gender == 0 && deviceType == 0) {
                    return true;
                }

            } catch (EazyException ex) {
                Logger.getLogger(MeetPeople.class.getName()).log(Level.SEVERE, null, ex);
            }
            Util.addDebugLog("GET MEET PEOPLE isSafeUser FALSE ");
            return false;
        }
        
        private boolean isCheckFemale(String token, String safaryVersion) {
            try {
                String userIdfromToken = UserSessionDAO.getUserIdFromToken(token);
                DBObject dbObject = UserDAO.getUserInforJSON(userIdfromToken);

                if (dbObject == null) {
                    Util.addDebugLog("GET MEET PEOPLE isSafeUser FALSE ");
                    return false;
                }

                Integer gender = (Integer) dbObject.get(UserdbKey.USER.GENDER);
                Integer deviceType = (Integer) dbObject.get(UserdbKey.USER.DEVICE_TYPE);
                if (UserSessionDAO.getAppVersionFromToken(token).equals(safaryVersion) && gender == 1 && deviceType == 0) {
                    return true;
                }

            } catch (EazyException ex) {
                Logger.getLogger(MeetPeople.class.getName()).log(Level.SEVERE, null, ex);
            }
            Util.addDebugLog("GET MEET PEOPLE isSafeUser FALSE ");
            return false;
        }

        private void removeBlockUser(Request request, JSONArray meetJsonArray) throws ParseException {
            if (request.getParamValue(ParamKey.USER_ID) == null){
                return;
            }
            
            request.put(ParamKey.API_NAME, API.GET_BLOCK_LIST);

            long getRemoveListStart = System.currentTimeMillis();
            String umsString = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            JSONObject umsO = (JSONObject) new JSONParser().parse(umsString);
            JSONArray blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
//            JSONArray deactiveList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.DEACTIVE_LIST);
            long getRemoveListEnd = System.currentTimeMillis();
            getRemoveListEnd -= getRemoveListStart;
            if (getRemoveListEnd >= 1500) {
                Util.addInfoLog("Meetpeople --> removeBlockUser : get RemoveList slow: " + getRemoveListEnd);
            }

//            if (!blockList.isEmpty() || !deactiveList.isEmpty()) {
            if (!blockList.isEmpty()) {
                long addMapStart = System.currentTimeMillis();
                Map<String, JSONObject> map = new HashMap<>();
                for (Object meetJsonArray1 : meetJsonArray) {
                    JSONObject obj = (JSONObject) meetJsonArray1;
                    String userId = (String) obj.get(ParamKey.USER_ID);
                    map.put(userId, obj);
                }
                long addMapEnd = System.currentTimeMillis();
                addMapEnd -= addMapStart;
                if (addMapEnd >= 1500) {
                    Util.addInfoLog("Meetpeople --> removeBlockUser : add Map slow : " + addMapEnd);
                }

                long removeBlockListStart = System.currentTimeMillis();
                if (!blockList.isEmpty()) {
                    for (Object obj : blockList) {
                        String id = (String) obj;
                        if (map.containsKey(id)) {
                            meetJsonArray.remove(map.get(id));
                        }
                    }
                }
                long removeBlockListEnd = System.currentTimeMillis();
                removeBlockListEnd -= removeBlockListStart;
                if (removeBlockListEnd >= 1500) {
                    Util.addInfoLog("Meetpeople --> removeBlockUser : remove block list slow : " + removeBlockListEnd);
                }
            }
        }

        private void getStatus(Request request, List<String> lEmail, List<MeetObject> lMeet, JSONArray meetJsonArray) throws ParseException {
            long getListStatusStart = System.currentTimeMillis();
//            request.put(ParamKey.API_NAME, API.LIST_STATUS);
//            request.reqObj.put(ParamKey.LIST_USER, lEmail);
//            String resutlUMS = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
//            JSONObject UMSJson = (JSONObject) new JSONParser().parse(resutlUMS);
//            JSONArray UMSJsonArr = (JSONArray) UMSJson.get(ParamKey.DATA);
            Map<String, String> mapAbout = UserDAO.getListAboutbyListUserId(lEmail);
            long getListStatusEnd = System.currentTimeMillis();
            getListStatusEnd -= getListStatusStart;
            if (getListStatusEnd >= 1500) {
                Util.addInfoLog("Meetpeople : get Status : get List status slow : " + getListStatusEnd);
            }

//            if (UMSJsonArr != null && !UMSJsonArr.isEmpty()) {
            if (mapAbout != null && !mapAbout.isEmpty()) {
                List<UMSObject> lUms = new ArrayList<>();
                long addUMSListStart = System.currentTimeMillis();
//                for (Object UMSJsonArr1 : UMSJsonArr) {
//                    JSONObject obj = (JSONObject) UMSJsonArr1;
//                    String userId = (String) obj.get(ParamKey.USER_ID);
//                    String about = (String) obj.get(ParamKey.ABOUT);
//                    if (about != null) {
//                        UMSObject stt = new UMSObject(userId, about);
//                        lUms.add(stt);
//                    }
//                }
                for (Map.Entry<String, String> pairs : mapAbout.entrySet()) {
                    String userId = pairs.getKey();
                    String about = pairs.getValue();
                    if (about != null) {
                        UMSObject stt = new UMSObject(userId, about);
                        lUms.add(stt);
                    }
                }
                long addUMSListEnd = System.currentTimeMillis();
                addUMSListEnd -= addUMSListStart;
                if (addUMSListEnd >= 1500) {
                    Util.addInfoLog("Meetpeople : get Status : add UMS list slow : " + addUMSListEnd);
                }

                long addStatusStart = System.currentTimeMillis();
                if (!lUms.isEmpty()) {
                    Collections.sort(lUms);
                    int meetCount = 0;
                    int umsCount = 0;
                    while (meetCount < lMeet.size() && umsCount < lUms.size()) {
                        String meetUserId = lMeet.get(meetCount).userId;
                        String umsUserId = lUms.get(umsCount).userId;
                        if (meetUserId.compareTo(umsUserId) == 0) {
                            int index = lMeet.get(meetCount).index;
                            String about = lUms.get(umsCount).about;
                            //HUNGDT add
                            about = Util.replaceBannedWord(about);
                            JSONObject obj = (JSONObject) meetJsonArray.get(index);
                            obj.put("abt", about);
                            meetCount++;
                            umsCount++;
                        } else if (meetUserId.compareTo(umsUserId) > 0) {
                            umsCount++;
                        } else {
                            meetCount++;
                        }
                    }
                }
                long addStatusEnd = System.currentTimeMillis();
                addStatusEnd -= addStatusStart;
                if (addStatusEnd >= 1500) {
                    Util.addInfoLog("Meetpeople : get Status : add status slow : " + addStatusEnd);
                }
            }
        }

        private void getConnectionInfor(Request request, JSONArray meetJsonArray, List<String> listEmail) {
            JSONArray listConnectionInfor = InterCommunicator.getConnectionInfor(request, listEmail);
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
                        Long isAlert = (Long) connectionJson.get("is_alt");
                        jsonObj.put("is_alt", isAlert);
                        break;
                    }
                }
            }
        }
        
        private void getImageUrl(JSONArray meetJsonArray, List<String> listAvaId){
            HashMap<String, FileUrl> mapImg = InterCommunicator.getImage(listAvaId);
            for (Object obj : meetJsonArray) {
                JSONObject user = (JSONObject) obj;
                String ava = (String) user.get(ParamKey.AVATAR_ID);
                FileUrl url = mapImg.get(ava);
                if (url != null){
                    user.put(ParamKey.THUMBNAIL_URL, url.getThumbnail());
                    user.put(ParamKey.ORIGINAL_URL, url.getOriginalUrl());
                }
            }
        }

        private void getUnReadMessageNumber(Request request, List<MeetObject> lMeet, JSONArray meetJsonArray, List<String> listEmail) {
            request.reqObj.put(ParamKey.FRIEND_LIST, listEmail);
            request.put(ParamKey.API_NAME, API.GET_UNREAD_NUMBER);
            String resutlChat = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            JSONArray chatJsonArr = new JSONArray();
            try {
                chatJsonArr = (JSONArray) new JSONParser().parse(resutlChat);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            if (!chatJsonArr.isEmpty()) {
                List<ChatObject> lChat = new ArrayList<>();
                for (Object obj : chatJsonArr) {
                    JSONObject jsonObj = (JSONObject) obj;
                    String userId = (String) jsonObj.get(ParamKey.FRDID);
                    Long number = (Long) jsonObj.get(ParamKey.UNREAD_NUMBER);
                    if (number != 0) {
                        ChatObject chat = new ChatObject(userId, number.intValue());
                        lChat.add(chat);
                    }
                }
                if (!lChat.isEmpty()) {
                    Collections.sort(lChat);
                    int meetCount = 0;
                    int chatCount = 0;
                    while (meetCount < lMeet.size() && chatCount < lChat.size()) {
                        String meetUserId = lMeet.get(meetCount).userId;
                        String chatUserId = lChat.get(chatCount).userId;
                        if (meetUserId.compareTo(chatUserId) == 0) {
                            int index = lMeet.get(meetCount).index;
                            int num = lChat.get(chatCount).number;
                            JSONObject obj = (JSONObject) meetJsonArray.get(index);
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
        }

        private class MeetObject implements Comparable<MeetObject> {

            public String userId;
            public int index;

            public MeetObject(String userId, int index) {
                super();
                this.userId = userId;
                this.index = index;
            }

            @Override
            public int compareTo(MeetObject obj) {
                //ascending order
                return this.userId.compareTo(obj.userId);
            }
        }

        private class ChatObject implements Comparable<ChatObject> {

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

        private class UMSObject implements Comparable<UMSObject> {

            public String userId;
            public String about;

            public UMSObject(String userId, String about) {
                super();
                this.userId = userId;
                this.about = about;
            }

            @Override
            public int compareTo(UMSObject obj) {
                //ascending order
                return this.userId.compareTo(obj.userId);
            }
        }
    }
}
