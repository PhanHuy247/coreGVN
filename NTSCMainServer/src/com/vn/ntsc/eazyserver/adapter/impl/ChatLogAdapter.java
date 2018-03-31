/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.API;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
import com.vn.ntsc.dao.impl.FavoristDAO;
import com.vn.ntsc.dao.impl.FileDAO;
import com.vn.ntsc.dao.impl.ImageStfDAO;
import com.vn.ntsc.dao.impl.UnlockDAO;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.FileData;
import com.vn.ntsc.otherservice.entity.impl.ListFileData;
import com.vn.ntsc.otherservice.entity.impl.User;
import eazycommon.constant.FilesAndFolders;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

/**
 *
 *
 * @author tuannxv00804
 */
public class ChatLogAdapter implements IServiceAdapter {

    public static final ListConverstation listConversation = new ListConverstation();
    public static final GetHistory getHistory = new GetHistory();
    public static final MarkReads markReads = new MarkReads();
    public static final DelConversations delConversation = new DelConversations();
    public static final UnRead unRead = new UnRead();
    public static final CheckStateWebsocket checkStateWebsocket = new CheckStateWebsocket();
    public static final RemoveWebsocket removeWebsocket = new RemoveWebsocket();
    public static final GetFileChat getFileChat = new GetFileChat();
    
    @Override
    public String callService(Request request) {
        String result = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
        return result;
    }

    public static class GetHistory implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                String line = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
                JSONArray chatObj = (JSONArray) new JSONParser().parse(line);
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                String friendId = (String) request.getParamValue("frd_id");
                String userName = (String)UserDAO.getUserInfor(friendId,"user_name");
                
                //check userId favorist friendId 
                Long isFav = FavoristDAO.checkFavourist(userId, friendId);
                
                Map<String, JSONObject> videoMap = new HashMap<>();
                Map<String, JSONObject> imageMap = new HashMap<>();
                for (Object obj : chatObj) {
                    JSONObject json = (JSONObject) obj;
                    String messageType = (String) json.get(ParamKey.MSG_TYPE);
                    String msg_id = (String) json.get("msg_id");
                    
                    if (messageType != null && messageType.equals("FILE")) {

                    } else {
                        String content = (String) json.get(ParamKey.CONTENT);
                        json.put("content", Util.replaceWordChat(content));

                    }

                }
                JSONObject obj = new JSONObject();
                obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                obj.put(ParamKey.DATA, chatObj);
                obj.put(ParamKey.USER_NAME, userName);
                obj.put(ParamKey.IS_FAV,isFav);
                return obj.toJSONString();
            } catch (Exception ex) {
                return ResponseMessage.UnknownError;
            }
        }

    }

    public static class ListConverstation implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            JSONObject resultJson = new JSONObject();
            try {
                String line = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
                JSONArray usersArr = new JSONArray();
                try {
                    usersArr = (JSONArray) new JSONParser().parse(line);
                    if (usersArr.isEmpty()) {
                        resultJson.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                        resultJson.put(ParamKey.DATA, usersArr);
                        Util.addDebugLog("resultJson1 " + resultJson);
                        return resultJson.toJSONString();
                    }
                } catch (ParseException ex) {
                    Util.addErrorLog(ex);
                    resultJson.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                    resultJson.put(ParamKey.DATA, usersArr);
                    Util.addDebugLog("resultJson2 " + resultJson);
                    return resultJson.toJSONString();
                }

                LinkedList<String> llEmail = new LinkedList<>();
                for (Object usersArr1 : usersArr) {
                    JSONObject user = (JSONObject) usersArr1;
                    String userID = (String) user.get(ParamKey.FRDID);
                    llEmail.add(userID);
                }

                String uId = (String) request.getParamValue(ParamKey.USER_ID);
                JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                if (psArr == null) {
                    resultJson.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                    resultJson.put(ParamKey.DATA, usersArr);
                    Util.addDebugLog("resultJson3 " + resultJson);
                    return resultJson.toJSONString();
                }
                Queue<String> removeList = new LinkedList<>();
                for (int i = 0; i < psArr.size(); i++) {
                    JSONObject ps = (JSONObject) psArr.get(i);
                    if (ps != null) {
                        try {
                            Boolean isOnline = (Boolean) ps.get(ParamKey.IS_ONLINE);
                            Double lon = (Double) ps.get(ParamKey.LONGITUDE);
                            Double lat = (Double) ps.get(ParamKey.LATITUDE);
                            String userName = (String) ps.get(ParamKey.USER_NAME);
                            String avaId = (String) ps.get(ParamKey.AVATAR_ID);
                            Long gender = (Long) ps.get(ParamKey.GENDER);
                            Double dist = (Double) ps.get(ParamKey.DIST);
                            String lastLogin = (String) ps.get(ParamKey.LAST_LOGIN);
                            Boolean isVideo = (Boolean) ps.get(ParamKey.VIDEO_CALL_WAITING);
                            Boolean isVoice = (Boolean) ps.get(ParamKey.VOICE_CALL_WAITING);
                            JSONObject user = (JSONObject) usersArr.get(i);
                            user.put(ParamKey.IS_ONLINE, isOnline);
                            user.put(ParamKey.LONGITUDE, lon);
                            user.put(ParamKey.LATITUDE, lat);
                            user.put("frd_name", userName);
                            user.put(ParamKey.DIST, dist);
                            user.put(ParamKey.LAST_LOGIN, lastLogin);
                            user.put(ParamKey.VIDEO_CALL_WAITING, isVideo);
                            user.put(ParamKey.VOICE_CALL_WAITING, isVoice);
                            List<String> listImgId = new ArrayList<String>();
                            if (avaId != null) {
                                user.put(ParamKey.AVATAR_ID, avaId);
                                Util.addDebugLog("AVaid==========================="+avaId);
                                listImgId.add(avaId);
                                ListFileData avaFile = InterCommunicator.getFileData(listImgId);
                                FileData listFile = avaFile.mapImg.get(avaId);
                                if(listFile != null){
                                    String avaUrl = listFile.thumbnailUrl;
                                    Util.addDebugLog("avaUrl==========================="+avaUrl);
                                    user.put(ParamKey.THUMBNAIL_URL, avaUrl);
                                }
                            }
                            user.put(ParamKey.GENDER, gender);
                        } catch (Exception ex) {
                            Util.addErrorLog(ex);

                            resultJson.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                            resultJson.put(ParamKey.DATA, usersArr);
                            Util.addDebugLog("resultJson4 " + resultJson);
                            return resultJson.toJSONString();
                        }
                    } else {
                        JSONObject user = (JSONObject) usersArr.get(i);
                        String userID = (String) user.get(ParamKey.FRDID);
                        //JSONObject user = (JSONObject) usersArr.get(i);
                        //user.put("frd_name", "退会者");
                        //usersArr.add(user);
                        //removeList.add(userID);
                        Util.addDebugLog("resultJson5 " + resultJson);
                    }
                }
                if (!removeList.isEmpty()) {
                    while (!removeList.isEmpty()) {
                        String userId = removeList.poll();
                        for (int i = 0; i < usersArr.size(); i++) {
                            JSONObject user = (JSONObject) usersArr.get(i);
                            String id = (String) user.get(ParamKey.FRDID);
                            if (userId.equals(id)) {
                                usersArr.remove(i);
                                break;
                            }
                        }
                    }
                }

                resultJson.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                resultJson.put(ParamKey.DATA, usersArr);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            Util.addDebugLog("resultJson " + resultJson);
            return resultJson.toJSONString();
        }

    }

    public static class MarkReads implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String line = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            return line;
        }

    }

    public static class UnRead implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            JSONObject respond = new JSONObject();
            String line = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            respond.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
            int number;
            try {
                number = Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                Util.addErrorLog(ex);
                number = 0;
            }
            JSONObject data = new JSONObject();
            data.put(ParamKey.UNREAD_NUMBER, number);
            respond.put(ParamKey.DATA, data);
            return respond.toJSONString();
        }

    }

    public static class DelConversations implements IServiceAdapter {

        @Override
        public String callService(Request request) {
//            Request newRequest = new Request();
//            newRequest.reqObj = new JSONObject();
//            newRequest.reqObj.put("api", "lst_fav");
//            newRequest.reqObj.put("token", request.token);
//            newRequest.reqObj.put("user_id", request.reqObj.get("user_id"));
//            String umsString = InterCommunicator.sendRequest(newRequest.toJson(), Config.UMSServerIP, Config.UMSPort);
//            JSONObject umsJson = Util.toJSONObject(umsString);
//            JSONArray umsArr = (JSONArray) umsJson.get("data");
//            List<String> listFavorite = new LinkedList<>();
//            for (Object user : umsArr) {
//                String userId = (String) ((JSONObject) user).get("user_id");
//                listFavorite.add(userId);
//            }
            String id = (String) request.getParamValue(ParamKey.USER_ID);
            List<String> listFavorite = FavoristDAO.getFavouristList(id);
            listFavorite.remove(id);
            
            JSONArray userArr = (JSONArray) request.reqObj.get("frd_id");
            List<Object> favoriteFriends = new LinkedList<>();
            if (!listFavorite.isEmpty()) {
                for (Object user : userArr) {
                    String userId = user.toString();
                    for (String uId : listFavorite) {
                        if (uId.equalsIgnoreCase(userId)) {
                            favoriteFriends.add(user);
                            break;
                        }
                    }
                }
            }
            if (userArr.size() > 1){
                request.reqObj.put("del_all", true);
                request.reqObj.put("list_fav", listFavorite);
            }

//            for (Object user : favoriteFriends) {
//                userArr.remove(user);
//            }

            String line = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            return line;
        }

    }

    private static class CheckStateWebsocket implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String line = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            return line;
        }
    }
    
    private static class GetFileChat implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String line = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            JSONObject obj = null;
            try {
                JSONObject chatObj = (JSONObject) new JSONParser().parse(line);
                obj = new JSONObject();
                obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                obj.put(ParamKey.DATA, chatObj);
            } catch (ParseException ex) {
                return ResponseMessage.UnknownError;
            }
            return obj.toJSONString();
        }
    }

    private static class RemoveWebsocket implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            String line = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            return line;
        }
    }

}
