
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatlogserver;

import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.Core;
import com.vn.ntsc.blacklist.BlockUserManager;
import com.vn.ntsc.blacklist.DeactivateUserManager;
import com.vn.ntsc.chatlogserver.pojos.CaiThia;
import com.vn.ntsc.chatserver.logging.impl.mongo.UnreadMessageUpdater;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;
import com.vn.ntsc.chatserver.pojos.user.LastMessageManager;
import com.vn.ntsc.chatserver.pojos.user.UnreadMessageManager;
import com.vn.ntsc.chatserver.pojos.user.User;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;
import com.vn.ntsc.chatserver.pojos.user.UserInfo;
import com.vn.ntsc.dao.impl.FileChatDAO;
import com.vn.ntsc.dao.impl.LastChatDAO;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.utils.Filer;
import com.vn.ntsc.utils.Validator;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

/**
 *
 * @author tuannxv00804
 */
public class ChatLogHandler extends AbstractHandler {

    @Override
    public void handle(String string, Request rqst, HttpServletRequest hsr, HttpServletResponse response) throws IOException, ServletException {
        rqst.setHandled(true);
        response.setContentType("text/plain;charset=UTF-8");
        InputStreamReader isr = new InputStreamReader(rqst.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        string = reader.readLine();
        Date time = new Date(Util.currentTime());
        JSONParser parser = new JSONParser();
        JSONObject jo = null;
        if (!string.isEmpty()) {
            try {
                jo = (JSONObject) parser.parse(string);
            } catch (ParseException ex) {
                Util.addErrorLog(ex);
                return;
            }
        }
        if (jo == null) {
            return;
        }
        OutputStream out = response.getOutputStream();
        String api = (String) jo.get(ParamKey.API_NAME);
        switch (api) {
            case API.RESET_CONFIG:
                resetConfig();
                out.write(ResponseMessage.SuccessMessage.getBytes());
                break;
            case API.IS_ALIVE:
                isAlive(jo, out);
                break;
            case API.LIST_CONVERSATION:
                listConversation(jo, out, time);
                break;
            case API.GET_UNREAD_NUMBER:
                getUnreadNumber(jo, out, time);
                break;
            case API.GET_CHAT_HISTORY:
                getHistory(jo, out, time);
                break;
            case API.GET_NEW_CHAT_MESSAGE:
                getNewMessage(jo, out, time);
                break;
            case API.MARKREADS:
                out.write(ResponseMessage.SuccessMessage.getBytes());
                markReads(jo, time);
                break;
            case API.DELCOVERSATION:
                Util.addInfoLog("DELETE CONVERSATION: " + jo.toJSONString());
                out.write(ResponseMessage.SuccessMessage.getBytes());
                delConversations(jo, time);
                break;
            case API.DEL_MESSAGE:
                Util.addInfoLog("DELETE MESSAGE: " + jo.toJSONString());
                out.write(ResponseMessage.SuccessMessage.getBytes());
                delMessage(jo, time);
                break;
            case API.TOTAL_UNREAD:
                totalUnread(jo, out, time);
                break;
            case API.CHECK_STATE_WEBSOCKET:
                checkStateWebsocket(jo, out, time);
                break;
            case API.ADD_BLOCK:
                addBlock(jo, out, time);
                break;
            case API.REMOVE_BLOCK:
                removeBlock(jo, out, time);
                break;
            case API.END_CALL:
                endCall(jo, out, time);
                out.write(ResponseMessage.SuccessMessage.getBytes());
                break;
            case API.MAKE_CALL:
                makeCall(jo, out, time);
                out.write(ResponseMessage.SuccessMessage.getBytes());
                break;
            case API.START_CALL:
                startCall(jo, out, time);
                out.write(ResponseMessage.SuccessMessage.getBytes());
                break;

            case API.ADD_FAVOURIST:
                try {
                    String userid = (String) jo.get(ParamKey.USER_ID);
                    String friendid = (String) jo.get("req_user_id");

                    List<UserConnection> l = Core.getStoreEngine().gets(userid);
                    if (l != null && !l.isEmpty()) {
                        for (UserConnection uc : l) {
                            uc.user.favouristList.add(friendid);
                        }
                    }
                    Util.addDebugLog("ChatLogHadler() --> AddFavourist : Successful");
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.REMOVE_FAVOURIST:
                try {
                    String userid = (String) jo.get(ParamKey.USER_ID);
                    String friendid = (String) jo.get("fav_id");

                    List<UserConnection> l = Core.getStoreEngine().gets(userid);
                    if (l != null && !l.isEmpty()) {
                        for (UserConnection uc : l) {
                            uc.user.favouristList.remove(friendid);
                        }
                    }
                    Util.addDebugLog("ChatLogHadler() --> RemoveFavourist : Successful");
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.ADDFRIEND:
                try {
                    String userid = (String) jo.get(ParamKey.USER_ID);
                    String friendid = (String) jo.get("req_user_id");
                    List<UserConnection> l = Core.getStoreEngine().gets(userid);
                    if (l != null && !l.isEmpty()) {
                        for (UserConnection uc : l) {
                            uc.user.friendList.add(friendid);
                        }
                    }
                    Util.addDebugLog("ChatLogHadler() --> AddFriend : Successful");
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.REMOVEFRIEND:
                try {
                    String userid = (String) jo.get(ParamKey.USER_ID);
                    String friendid = (String) jo.get("frd_id");
                    List<UserConnection> l = Core.getStoreEngine().gets(userid);
                    if (l != null && !l.isEmpty()) {
                        for (UserConnection uc : l) {
                            uc.user.friendList.remove(friendid);
                        }
                    }
                    Util.addDebugLog("ChatLogHadler() --> RemoveFriend : Successful");
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.NOTIFICATION_SETTING:
                try {
                    Long chat = (Long) jo.get("chat");
                    String userid = (String) jo.get(ParamKey.USER_ID);
                    if (chat != null) {
                        List<UserConnection> l = Core.getStoreEngine().gets(userid);
                        if (l != null && !l.isEmpty()) {
                            for (UserConnection uc : l) {
                                uc.user.sendChatNotificationSetting = chat.intValue();
                            }
                        }
                    }
                    Util.addDebugLog("ChatLogHadler() --> NotificationSetting : Successful");
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.AUTO_MESSAGE:
                try {
                    String userid = (String) jo.get(ParamKey.USER_ID);
                    JSONArray friendArr = (JSONArray) jo.get(ParamKey.FRIEND_LIST);
                    String content = (String) jo.get(ParamKey.CONTENT);
                    String ip = (String) jo.get(ParamKey.IP);

                    JSONArray toUsers = new JSONArray();
                    if (userid != null && !userid.isEmpty()
                            && friendArr != null && !friendArr.isEmpty()) {
                        for (Object friend : friendArr) {
                            String destUser = (String) friend;
                            Message msg = new Message(userid, destUser, MessageType.PP, content);
                            msg.ip = ip;
                            UserConnection destUC = Core.getStoreEngine().get(destUser);
                            User user;
                            boolean isFriendOffline = false;
                            if (destUC != null) {
                                destUC.user.inbox.add(msg);
                                user = destUC.user;
                            } else {
                                isFriendOffline = true;
                                user = User.getInstance(userid);
                            }
                            user.putLastChat(userid, msg);
                            user.increaseUnreadMessage(userid);
                            UnreadMessageUpdater.add(destUser);
                            Core.getLogger().log(msg);

                            if (isFriendOffline || destUC == null || destUC.soc == null) {
                                JSONObject obj = new JSONObject();
                                obj.put("to_userid", destUser);
                                obj.put("badge", UnreadMessageManager.getAllUnreadMessage(user.username));
                                toUsers.add(obj);
                            }

                            if (toUsers.size() == 200) {
                                sendJPNS(userid, toUsers, ip);
                                toUsers.clear();
                            }
                        }
                        if (!toUsers.isEmpty()) {
                            sendJPNS(userid, toUsers, ip);
                        }
                    }
                    Util.addDebugLog("ChatLogHadler() --> Automessage : Successful");
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.AUTO_MESSAGE_VAR:
                try {
                    String userid = (String) jo.get(ParamKey.USER_ID);
                    JSONArray friendArr = (JSONArray) jo.get(ParamKey.FRIEND_LIST);
                    String ip = (String) jo.get(ParamKey.IP);

                    JSONArray toUsers = new JSONArray();
                    if (userid != null && !userid.isEmpty()
                            && friendArr != null && !friendArr.isEmpty()) {
                        for (Object friend : friendArr) {
                            String destUser = (String) ((JSONObject) friend).get("user_id");
                            String content = (String) ((JSONObject) friend).get("content");
                            Message msg = new Message(userid, destUser, MessageType.PP, content);
                            msg.ip = ip;
                            UserConnection destUC = Core.getStoreEngine().get(destUser);
                            User user;
                            boolean isFriendOffline = false;
                            if (destUC != null) {
                                destUC.user.inbox.add(msg);
                                user = destUC.user;
                            } else {
                                isFriendOffline = true;
                                user = User.getInstance(destUser);
                            }
                            user.putLastChat(userid, msg);
                            user.increaseUnreadMessage(userid);
                            UnreadMessageUpdater.add(destUser);
                            Core.getLogger().log(msg);

                            if (isFriendOffline || destUC == null || destUC.soc == null) {

                                //receive from all
                                JSONObject obj = new JSONObject();
                                obj.put("to_userid", destUser);
                                obj.put("badge", UnreadMessageManager.getAllUnreadMessage(user.username));
                                toUsers.add(obj);
                            }

                            if (toUsers.size() == 200) {
                                sendJPNS(userid, toUsers, ip);
                                toUsers.clear();
                            }
                        }
                        if (!toUsers.isEmpty()) {
                            sendJPNS(userid, toUsers, ip);
                        }
                    }
                    Util.addDebugLog("ChatLogHadler() --> Automessage : Successful");
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.DEACTIVATE:
                try {
                    String userID = (String) jo.get(ParamKey.USER_ID);
                    DeactivateUserManager.add(userID);
                    Util.addDebugLog("ChatLogHadler() --> Deactive : Successful");
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.ACTIVATE:
                try {
                    String userID = (String) jo.get(ParamKey.USER_ID);
                    DeactivateUserManager.remove(userID);
                    Util.addDebugLog("ChatLogHadler() --> Active : Successful");
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.REMOVE_FAILED_UPLOAD_MESSAGE:
                try {
                    String userId, partnerId;
                    String messageId = (String) jo.get(ParamKey.MESSAGE_ID);

                    String elements[] = messageId.split("&");
                    if (elements.length == 3) {
                        userId = elements[0];
                        partnerId = elements[1];
                        Core.getDAO().removeMessage(userId, messageId);
                        Core.getDAO().removeMessage(partnerId, messageId);
                        Util.addDebugLog("ChatLogHadler() --> Remove_Failed_Upload_Message : Successful");
                    }
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
                break;
            case API.COMMENT_WEBSOCKET:
                Util.addDebugLog("j0 COMMENT_WEBSOCKET==============================================" + jo.toJSONString());
                jo.remove("api");
                Message msg = null;
                msg = Message.parseFromJSONString(jo.toJSONString());
                List<String> listUserCmt = msg.listUserComment;
                List<String> listUserNotiSocket = new ArrayList<>();
                List<String> listUserNotiFCM = new ArrayList<>();
                
                // JsonObject, JsonArray for noti FCM
                JSONObject jsonDeviceIdNotiSocket = new JSONObject();
                JSONArray listDeviceIdNotiSocket = new JSONArray();
                
                JSONArray jsonListDeviceIdNotiSocket = new JSONArray();
                //get user push noti FCM or Socket
                if (!listUserCmt.isEmpty()) {
                    if (msg.buzzId != null) {
                        for (String userID : listUserCmt) {
                            if(Core.getStoreEngine().gets(userID) != null && !Core.getStoreEngine().gets(userID).isEmpty()){
                                //list user noti websocket
                                listUserNotiSocket.add(userID);
                                
                                //get list device Id listUserNotiSocket ( case 1 account login multi device)
                                List<UserConnection> users = Core.getStoreEngine().gets(userID);
                                
                                jsonDeviceIdNotiSocket.put("user_id", userID);
                                for (UserConnection uc : users) {
                                    if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                                        continue;
                                    }
                                    listDeviceIdNotiSocket.add(uc.user.deviceId);
                                }
                                jsonDeviceIdNotiSocket.put("lst_device_id", listDeviceIdNotiSocket);
                                jsonListDeviceIdNotiSocket.add(jsonDeviceIdNotiSocket);
                            }else{
                                //list user noti FCM
                                listUserNotiFCM.add(userID);
                            }
                        }
                    }
                }
                if (msg.code == ErrorCode.SUCCESS) {
                    List<UserConnection> users = Core.getStoreEngine().gets(msg.from);
                    for (UserConnection uc : users) {
                        if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                            continue;
                        }
                        uc.user.outbox.add(msg);
                        break;
                    }
                } else {
                    confirmMessageSent(Core.getStoreEngine().get(msg.from), msg);
                }
                
                JSONObject obj = new JSONObject();
                obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                obj.put("list_user_cmt", listUserNotiFCM); // list User send Noti FCM
                obj.put("list_user_noti_socket", listUserNotiSocket); //list User send Noti Socket
                obj.put("list_device_id_noti_socket", jsonListDeviceIdNotiSocket); //list DeviceId send Noti Socket
                out.write(obj.toJSONString().getBytes());
                break;
            case API.SUB_COMMENT_WEBSOCKET:
                Util.addDebugLog("j0 SUB_COMMENT_WEBSOCKET==============================================" + jo.toJSONString());
                jo.remove("api");
                Message msgSub = Message.parseFromJSONString(jo.toJSONString());
                  //get user push noti FCM or Socket
                List<String> listUserNoti = msgSub.notificationList;
                List<String> listUserNotiSocketSubCmt = new ArrayList<>();
                List<String> listUserNotiFCMSubCmt = new ArrayList<>();
                
                // JsonObject, JsonArray for noti FCM
                JSONObject jsonDeviceIdNotiSocketSubCmt = new JSONObject();
                JSONArray listDeviceIdNotiSocketSubCmt = new JSONArray();
                
                JSONArray jsonListDeviceIdNotiSocketSubCmt = new JSONArray();
                if (!listUserNoti.isEmpty()) {
                    for (String userId : listUserNoti) {
                        if (Core.getStoreEngine().gets(userId) != null && !Core.getStoreEngine().gets(userId).isEmpty()) {
                            listUserNotiSocketSubCmt.add(userId);
                            
                              //get list device Id listUserNotiSocket ( case 1 account login multi device)
                                List<UserConnection> users = Core.getStoreEngine().gets(userId);
                                
                                jsonDeviceIdNotiSocketSubCmt.put("user_id", userId);
                                for (UserConnection uc : users) {
                                    if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                                        continue;
                                    }
                                    listDeviceIdNotiSocketSubCmt.add(uc.user.deviceId);
                                }
                                jsonDeviceIdNotiSocketSubCmt.put("lst_device_id", listDeviceIdNotiSocketSubCmt);
                                jsonListDeviceIdNotiSocketSubCmt.add(jsonDeviceIdNotiSocketSubCmt);
                        }else{
                            listUserNotiFCMSubCmt.add(userId);
                        }
                    }
                }
                if(msgSub.code == ErrorCode.SUCCESS){
                    List<UserConnection> usersSub = Core.getStoreEngine().gets(msgSub.from);
                    for (UserConnection uc : usersSub) {
                        if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                            continue;
                        }
                        uc.user.outbox.add(msgSub);
                        break;
                    }
                }else{
                    confirmMessageSent(Core.getStoreEngine().get(msgSub.from), msgSub);
                }
               
                JSONObject objSubComment = new JSONObject();
                objSubComment.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                objSubComment.put("notification_list", listUserNotiFCMSubCmt);// list User send Noti FCM
                objSubComment.put("list_user_noti_socket", listUserNotiSocketSubCmt);//list User send Noti Socket
                objSubComment.put("list_device_id_noti_socket", jsonListDeviceIdNotiSocketSubCmt); //list DeviceId send Noti Socket
                out.write(objSubComment.toJSONString().getBytes());
                break;
            case API.ADD_TAG_WEBSOCKET:
                Util.addDebugLog("j0 ADD_TAG_WEBSOCKET==============================================" + jo.toJSONString());
                jo.remove("api");
                Message msgTag = Message.parseFromJSONString(jo.toJSONString());
                List<UserConnection> usersTag = Core.getStoreEngine().gets(msgTag.from);
                for (UserConnection uc : usersTag) {
                    if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                        continue;
                    }
                    if(msgTag.listTo != null && !msgTag.listTo.isEmpty()){
                            JSONParser parse = new JSONParser();
                            try {
                                JSONArray array = (JSONArray)parse.parse(msgTag.listTo);
                                for(Object to : array){
                                    Message msgTagTo = Message.parseFromJSONString(jo.toJSONString());
                                    String toUser = (String)to;
                                    msgTagTo.to = toUser;
                                    uc.user.outbox.add(msgTagTo);
                                }
                            } catch (ParseException ex) {
                                Util.addDebugLog("ERROR CONVERT LISTO==============================");
                                return;
                            }
                    }else{
                        if(msgTag.to != null){
                            uc.user.outbox.add(msgTag);
                        }
                    }
                    break;
                }
                break;    
            case API.BUZZ_JOIN_WEBSOCKET:
                Util.addDebugLog("j0 BUZZ_JOIN_WEBSOCKET==============================================" + jo.toJSONString());
                jo.remove("api");
                Message msgBuzzJoin = Message.parseFromJSONString(jo.toJSONString());
                if(msgBuzzJoin.code == ErrorCode.SUCCESS){
                    if(msgBuzzJoin.buzzId != null){
                        Core.getStoreEngine().add(msgBuzzJoin.buzzId, msgBuzzJoin.from);
//                        Core.getStoreEngine().add(msgBuzzJoin.buzzId, msgBuzzJoin.ownBuzzId);
                    }else{
                        JSONParser parse = new JSONParser();
                        if (msgBuzzJoin.listBuzzId != null && msgBuzzJoin.listOwnBuzzId != null && !msgBuzzJoin.listBuzzId.isEmpty() && !msgBuzzJoin.listOwnBuzzId.isEmpty()) {
                            for (int i = 0; i < msgBuzzJoin.listBuzzId.size(); i++) {
                                String buzzId = (String) msgBuzzJoin.listBuzzId.get(i);
                                String ownBuzzId = (String) msgBuzzJoin.listOwnBuzzId.get(i);
                                Core.getStoreEngine().add(buzzId, msgBuzzJoin.from);
                                Core.getStoreEngine().add(buzzId, ownBuzzId);
                            }
                        }
                    }
                }else{
                    confirmMessageSent(Core.getStoreEngine().get(msgBuzzJoin.from), msgBuzzJoin);
                }
                break;
            case API.BUZZ_LEAVE_WEBSOCKET:
                Util.addDebugLog("j0 BUZZ_LEAVE_WEBSOCKET==============================================" + jo.toJSONString());
                jo.remove("api");
                Message msgBuzzLeave = Message.parseFromJSONString(jo.toJSONString());
                if(msgBuzzLeave.code == ErrorCode.SUCCESS){
                    Core.getStoreEngine().remove(msgBuzzLeave.buzzId, msgBuzzLeave.from);
                }
                confirmMessageSent(Core.getStoreEngine().get(msgBuzzLeave.from), msgBuzzLeave);                
                break;    
            case API.CHECK_BUZZ_WEBSOCKET:
                Util.addDebugLog("j0 CHECK_BUZZ_WEBSOCKET==============================================" + jo.toJSONString());
                jo.remove("api");
                Message msgBuzzCheck = Message.parseFromJSONString(jo.toJSONString());
                if(msgBuzzCheck.code == ErrorCode.SUCCESS){
                    Core.getStoreEngine().add(msgBuzzCheck.buzzId, msgBuzzCheck.from);
                }
                confirmMessageSent(Core.getStoreEngine().get(msgBuzzCheck.from), msgBuzzCheck);
                break;    
            case API.DELETE_COMMENT:
                Util.addDebugLog("j0 DELETE_COMMENT==============================================" + jo.toJSONString());
                jo.remove("api");
                Message msgBuzzDelCmt = Message.parseFromJSONString(jo.toJSONString());
                if(msgBuzzDelCmt.code == ErrorCode.SUCCESS){
                    Core.getStoreEngine().add(msgBuzzDelCmt.buzzId, msgBuzzDelCmt.from);
                    confirmMessageSent(Core.getStoreEngine().get(msgBuzzDelCmt.from), msgBuzzDelCmt);
                }
                break;
            case API.DELETE_SUB_COMMENT:
                Util.addDebugLog("j0 DELETE_SUB_COMMENT==============================================" + jo.toJSONString());
                jo.remove("api");
                Message msgBuzzDelSubCmt = Message.parseFromJSONString(jo.toJSONString());
                if(msgBuzzDelSubCmt.code == ErrorCode.SUCCESS){
                    Core.getStoreEngine().add(msgBuzzDelSubCmt.buzzId, msgBuzzDelSubCmt.from);
                    confirmMessageSent(Core.getStoreEngine().get(msgBuzzDelSubCmt.from), msgBuzzDelSubCmt);
                }
                break;
            case API.SEND_GIFT:
                Util.addDebugLog("j0 SEND_GIFT==============================================" + jo.toJSONString());
                jo.remove("api");
                Message msgSendGift = Message.parseFromJSONString(jo.toJSONString());
                if(msgSendGift.originTime == null) msgSendGift.originTime = new Date(Util.currentTime());
                List<UserConnection> users = Core.getStoreEngine().gets(msgSendGift.from);
                for (UserConnection uc : users){
                    if(uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()){
                        continue;
                    }
                    if(uc.user.token.equals(msgSendGift.token)){
                        uc.user.outbox.add(msgSendGift);
                        break;
                    }
                }
                break;    
            case API.CHANGE_TOKEN_WEBSOCKET:
                Util.addDebugLog("j0 CHANGE_TOKEN_WEBSOCKET==============================================" + jo.toJSONString());
                Message msgChangeTokenWebsocket = Message.parseFromJSONString(jo.toJSONString());
                List<UserConnection> usersToken = Core.getStoreEngine().gets(msgChangeTokenWebsocket.userId);
                for (UserConnection uc : usersToken){
                    if(uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()){
                        continue;
                    }
                    if(uc.user.token.equals(msgChangeTokenWebsocket.token)){
                        uc.user.token = msgChangeTokenWebsocket.newToken;
                    }
                }
                break;
            case API.GET_FILE_CHAT:
                Util.addDebugLog("GET_FILE_CHAT ==============================================" + jo.toJSONString());
                String to = (String) jo.get("friend_id");
                String from = (String) jo.get("user_id");
                Long type = (Long) jo.get("type");
                Long take = (Long) jo.get("take");
                Long skip = (Long) jo.get("skip");
                Long sort = (Long) jo.get("sort");
                String result = null;
                result = Core.getDAO().getFileChat(from, to, take, skip, type, sort);
                
                out.write(result.getBytes());
                out.flush();
                break;
            case API.REMOVE_WEBSOCKET:
                Util.addDebugLog("REMOVE WEBSOCKET ==============================================" + jo.toJSONString());
                result = ResponseMessage.UnknownError;
                String token = (String) jo.get("token");
                String userId = (String) jo.get("user_id");
                Boolean isRemove = Core.getStoreEngine().removeSessionTokenFromClient(userId,token);
                if(isRemove){
                    result = ResponseMessage.SuccessMessage;
                    out.write(result.getBytes());
                    out.flush();
                }
                break;
            case API.NOTI_BUZZ_SOCKET:
                Util.addDebugLog("NOTI_BUZZ_SOCKET ==============================================" + jo.toJSONString());
                // JsonObject, JsonArray for noti FCM
                JSONObject jsonDeviceIdNoti = new JSONObject();
                JSONArray listDeviceIdNoti = new JSONArray();
                JSONArray jsonListDeviceIdNoti = new JSONArray();
                
                List<String> listUserNotiFcm = new ArrayList<>();
                JSONObject jsonResutl = new JSONObject();
                
                //case noti cmt , sub_cmt with user is List(much user)
                JSONArray listUser = (JSONArray) jo.get("list_user");
                if(listUser != null && !listUser.isEmpty()){
                    for (Object user : listUser) {
                        String userCommentBuzz = (String) user;
                        Message msgNotiBuzz = Message.parseFromJSONString(userCommentBuzz);
                        List<UserConnection> usersNotiBuzz = Core.getStoreEngine().gets(msgNotiBuzz.from);
                        for (UserConnection uc : usersNotiBuzz) {
                            if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                                continue;
                            }
                            uc.user.outbox.add(msgNotiBuzz);
                            break;
                        }
                        List<UserConnection> usersNoti = Core.getStoreEngine().gets(msgNotiBuzz.to);
                        if(usersNoti == null || usersNoti.isEmpty()){
                            listUserNotiFcm.add(msgNotiBuzz.to);
                            continue;
                        }
                        jsonDeviceIdNoti.put("user_id", msgNotiBuzz.to);
                        for (UserConnection uc : usersNoti) {
                            if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                                continue;
                            }
                            listDeviceIdNoti.add(uc.user.deviceId);
                        }
                        jsonDeviceIdNoti.put("lst_device_id", listDeviceIdNoti);
                        jsonListDeviceIdNoti.add(jsonDeviceIdNoti);
                    }
                    Util.addDebugLog("jsonListDeviceIdNoti============================="+jsonListDeviceIdNoti);
                    Util.addDebugLog("listUserNotiFcm============================="+listUserNotiFcm);
                    jsonResutl.put("list_device_id", jsonListDeviceIdNoti);
                    jsonResutl.put("list_user_id", listUserNotiFcm);
                    out.write(jsonResutl.toJSONString().getBytes());
                    out.flush();
                }
                
                //case noti like with user is own buzz ( 1 user)
                JSONObject jsonUserId = (JSONObject)jo.get("msg_noti");
                
                
                if(jsonUserId != null){
                    Message msgNoti = Message.parseFromJSONString(jsonUserId.toJSONString());
                    if (Core.getStoreEngine().gets(msgNoti.to) != null && !Core.getStoreEngine().gets(msgNoti.to).isEmpty()) {
                        List<UserConnection> usersNotiBuzz = Core.getStoreEngine().gets(msgNoti.from);
                        for (UserConnection uc : usersNotiBuzz) {
                            if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                                continue;
                            }
                            uc.user.outbox.add(msgNoti);
                            break;
                        }
                        List<UserConnection> usersNoti = Core.getStoreEngine().gets(msgNoti.to);
                        for (UserConnection uc : usersNoti) {
                            if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                                continue;
                            }
                            listDeviceIdNoti.add(uc.user.deviceId);
                        }
                    }
                    jsonDeviceIdNoti.put("lst_device_id", listDeviceIdNoti);
                     Util.addDebugLog("jsonListDeviceIdNoti============================="+jsonDeviceIdNoti.toJSONString());
                    out.write(jsonDeviceIdNoti.toJSONString().getBytes());
                    out.flush();
                }
                break;
                
        }

        out.flush();
        out.close();
    }
    
    private void confirmMessageSent(UserConnection uc, Message msg) {
        if(uc != null && uc.webSocKet.isOpen())
            MessageIO.sendMessageWebSocket(uc, msg);
    }
    private static void sendJPNS(String fromUserid, JSONArray jsonArray, String ip) {
        JSONObject jo = new JSONObject();
        jo.put(ParamKey.FROM_USER_ID, fromUserid);
        jo.put("to_userid_list", jsonArray);
        jo.put(ParamKey.API_NAME, API.AUTO_MESSAGE);
        jo.put(ParamKey.IP, ip);

        String msg = jo.toJSONString();
        Util.sendRequest(msg, Config.JPNSServer, Config.JPNSPort);

    }
     
    //Linh clone from wlc
    private static void sendJPNSWithEndCall(String fromUserid, String toUserid, String api, int badge, String ip) {

        JSONObject jo = new JSONObject();
        jo.put(ParamKey.FROM_USER_ID, fromUserid);
        jo.put(ParamKey.TOUSERID, toUserid);
        jo.put(ParamKey.API_NAME, api);
//        jo.put(ParamKey.BADGE, badge + 1);
        jo.put(ParamKey.BADGE, badge);
        jo.put(ParamKey.IP, ip);

        String msg = jo.toJSONString();
        Util.sendRequest(msg, Config.JPNSServer, Config.JPNSPort);
    }

    private void markReads(JSONObject jo, Date time) {
        try {
            String userid = (String) jo.get(ParamKey.USER_ID);
            String ip = (String) jo.get(ParamKey.IP);
            JSONArray joArr = (JSONArray) jo.get(ParamKey.FRDID);

            User user;
            UserConnection uc = Core.getStoreEngine().get(userid);
            if (uc == null) {
                user = User.getInstance(userid);
            } else {
                user = uc.user;
            }
            Map<String, Message> mapLastChat = user.getLastChat();
            for (Object friend : joArr) {
                String friendid = (String) friend;
                user.readTimes.put(friendid, Util.currentTime());
                user.removeUnreadMessage(friendid);
                Core.getDAO().updateReadMessage(friendid, userid, friendid);
                Message lastMessage = mapLastChat.get(friendid);
                if (lastMessage != null) {
                    String content = lastMessage.id + "|" + MessageTypeValue.MsgStatus_Read_All;
                    Message sendMessage = new Message(userid, friendid, MessageType.MDS, content);
                    sendMessage.ip = ip;
                    UserConnection destUC = Core.getStoreEngine().get(friendid);
//                    if (destUC != null && destUC.isSendReadMessage) {
                    if (destUC != null) {
                        destUC.user.inbox.add(sendMessage);
                    }
                }
            }
            UnreadMessageUpdater.add(userid);
            Util.addDebugLog("ChatLogHadler() --> MarkReads : Successful");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private void delConversations(JSONObject jo, Date time) {
        try {
            String userid = (String) jo.get(ParamKey.USER_ID);
            JSONArray joArr = (JSONArray) jo.get(ParamKey.FRDID);
            Boolean isDelAll = (Boolean) jo.get("del_all");
            UserConnection uc = Core.getStoreEngine().get(userid);
            User user;
            user = uc == null ? User.getInstance(userid) : uc.user;
            Util.addDebugLog("=============== isDelAll: " + isDelAll);
            if (isDelAll == null || !isDelAll) {
                LinkedList<String> llFriends = new LinkedList<>();
                for (Object id : joArr) {
                    String friendID = (String) id;
                    llFriends.add(friendID);
                    //                user.lastChats.remove(friendID);
                    LastMessageManager.remove(userid, friendID);
                    user.readTimes.remove(friendID);
                    //                user.removeLastChat(friendID);
                    LastChatDAO.removeLastChatList(userid, friendID);
                    user.removeUnreadMessage(friendID);
                }
                Core.getDAO().del(userid, llFriends);
                Core.getDAO().delFileChat(userid, llFriends);
                UnreadMessageUpdater.add(userid);
            } else {
                JSONArray listFav = (JSONArray) jo.get("list_fav");
                List<String> llFav = new LinkedList<>();
//                llFav.addAll(listFav);
                Util.addDebugLog("=============== :: " + llFav);

                HashMap<String, String> lastMsgMap = LastMessageManager.getHashMapLastMessage(userid);
                lastMsgMap.keySet().retainAll(llFav);
                user.readTimes.keySet().retainAll(llFav);

                LastChatDAO.removeExceptList(userid, llFav);
                user.removeUnreadMessageExceptList(llFav);

                Core.getDAO().removeButKeepFavoristFriends(userid, llFav);
                Core.getDAO().removeAllFileChat(userid, llFav);
                UnreadMessageUpdater.add(userid);
            }
            Util.addDebugLog("ChatLogHadler() --> delConversations : Successful");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private void isAlive(JSONObject jo, OutputStream out) {
        Util.addDebugLog("ChatLogHadler() --> isAlive START ");
        try {
            String userID = (String) jo.get(ParamKey.USER_ID);// LongLT Edit 
            boolean isAlive = false;
            JSONObject result = new JSONObject();

            UserConnection uc = Core.getStoreEngine().get(userID);
            if (uc != null) {
                Util.addDebugLog("ChatLogHadler() --> isAlive PROCCESSING FOUND " + userID);
                isAlive = MessageIO.isClientAlive(uc);
            }
            if (isAlive) {
                result.put(ParamKey.DATA, 1);
                out.write(result.toJSONString().getBytes());
            } else {
                result.put(ParamKey.DATA, 0);
                out.write(result.toJSONString().getBytes());
            }

            Util.addDebugLog("ChatLogHadler() --> isAlive : " + result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private void getUnreadNumber(JSONObject jo, OutputStream out, Date time) {
        try {
            String userid = (String) jo.get(ParamKey.USER_ID);
            JSONArray joArr = (JSONArray) jo.get(ParamKey.FRIEND_LIST);

            ArrayList<CaiThia> arrThia = new ArrayList<>();
            for (Object id : joArr) {
                String friendID = (String) id;
                CaiThia thia = new CaiThia();
                thia.friendID = friendID;
                if (thia.friendID.equals(userid)) {
//                    User user;
//                    UserConnection uc = Core.getStoreEngine().get(userid);
//                    user = uc == null ? User.getInstance(userid) : uc.user;
//                    Map<String, Message> mapLastChat = user.getLastChat();
//                    Collection lastchat = mapLastChat != null? mapLastChat.values() : user.lastChats.values();
//                    uc = uc == null ? new UserConnection(userid) : uc;
//                    Map<String, Message> mapLastChat = uc.user.getLastChat();
//                    Collection lastchat = mapLastChat != null? mapLastChat.values() : uc.user.lastChats.values();
//                    Collection<String> lastChats = getListLastChatsFromLastMessages(userid, lastchat);
                    Collection<String> lastChats = LastMessageManager.getCollectionLastMessage(userid);
                    List<String> friends = new ArrayList<>();
                    for (String friend : lastChats) {
                        if (Validator.isUser(friend) && !BlockUserManager.isBlock(userid, friend)) {
                            friends.add(friend);
                        }
                    }
//                    List<String> friends = Core.getDAO().getListUserIds(lastChats);
//                    User.FFUMS.removeBlockUser(userid, friends);
                    thia.unreadNum = UnreadMessageManager.getAllUnreadByUsers(userid, friends);
                } else {
                    thia.unreadNum = UnreadMessageManager.getAllUnreadByUser(userid, friendID);
                }
                arrThia.add(thia);
            }

            String result = CaiThia.toJson(arrThia, false);
            try {
                out.write(result.getBytes());
                out.flush();
                Util.addDebugLog("ChatLogHadler() --> getUnreadNumber : " + result);
            } catch (IOException ex) {
                Util.addErrorLog(ex);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    //HUNGDT add 20425
    private void makeCall(JSONObject jo, OutputStream out, Date time) {
        try {
            String from = Util.getStringParam(jo, "from");
            String to = Util.getStringParam(jo, "to");
            String callSip = Util.getStringParam(jo, "call_sip");
            Integer type = Util.getLongParam(jo, "type").intValue();

            User fromUser = User.getInstance(from);
            User toUser = User.getInstance(to);

            MessageType messageType;
            if (!Util.validateString(from, to, callSip)) {
                return;
            }

            String partnerResponse;
            if (type == Constant.CALL_TYPE_VALUE.VIDEO_CALL) {
                messageType = MessageType.EVIDEO;
                partnerResponse = Constant.CALL_ANSWER_VALUE.VIDEO_CALL_REFUSE;
            } else {
                messageType = MessageType.EVOICE;
                partnerResponse = Constant.CALL_ANSWER_VALUE.VOICE_CALL_REFUSE;
            }

            sendJPNSPing(from, to, type);
            String value = partnerResponse + "|" + callSip + "|" + 0;
            Message msg = new Message(from, to, messageType, value);
            msg.callId = callSip;
            String callSipId = "";
            if (callSip != null) {
                if (callSip.length() >= 17) {
                    callSipId = callSip.replaceAll("-", "").substring(0, 17);
                }
            }
            msg.id = from + "&" + to + "&" + callSipId;
            Core.getDAO().save(msg);
            Util.addDebugLog("makeCall " + value);

            fromUser.putLastChat(to, msg);
            toUser.putLastChat(from, msg);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

    }

    private void startCall(JSONObject jo, OutputStream out, Date time) {
        try {
            String from = Util.getStringParam(jo, "from");
            String to = Util.getStringParam(jo, "to");
            String callSip = Util.getStringParam(jo, "call_sip");
            Integer type = Util.getLongParam(jo, "type").intValue();

            User fromUser = User.getInstance(from);
            User toUser = User.getInstance(to);

            MessageType messageType;
            if (!Util.validateString(from, to, callSip)) {
                return;
            }

            String partnerResponse;
            if (type == Constant.CALL_TYPE_VALUE.VIDEO_CALL) {
                messageType = MessageType.EVIDEO;
                partnerResponse = Constant.CALL_ANSWER_VALUE.VIDEO_CALL_ANSWER;
            } else {
                messageType = MessageType.EVOICE;
                partnerResponse = Constant.CALL_ANSWER_VALUE.VOICE_CALL_ANSWER;
            }

            String value = partnerResponse + "|" + callSip + "|" + 0;
            Message msg = new Message(from, to, messageType, value);
            String callSipId = "";
            if (callSip != null) {
                if (callSip.length() >= 17) {
                    callSipId = callSip.replaceAll("-", "").substring(0, 17);
                }
            }
            msg.id = from + "&" + to + "&" + callSipId;
            Core.getDAO().updateCallMessage(from, to, callSip, value, msg.id);

            fromUser.putLastChat(to, msg);
            toUser.putLastChat(from, msg);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

    }

    private void endCall(JSONObject jo, OutputStream out, Date time) {
        String from = Util.getStringParam(jo, "from");
        String to = Util.getStringParam(jo, "to");
        String callSip = Util.getStringParam(jo, "call_sip");
        Integer partnerRespondStr = Util.getLongParam(jo, "finish_type").intValue();
        Integer duration = Util.getLongParam(jo, "bill_seconds").intValue();
        Integer type = Util.getLongParam(jo, "type").intValue();
        String ip = Util.getStringParam(jo, "ip");

        User fromUser = User.getInstance(from);
        User toUser = User.getInstance(to);

        MessageType messageType;
        if (!Util.validateString(from, to, callSip)) {
            return;
        }
        int unreadNum = UnreadMessageManager.getAllUnreadMessage(to);

        String partnerResponse = null;
        if (type == Constant.CALL_TYPE_VALUE.VIDEO_CALL) {
            messageType = MessageType.EVIDEO;
            switch (partnerRespondStr) {
                case Constant.END_CALL_VALUE.BUSY:
                    partnerResponse = Constant.CALL_ANSWER_VALUE.VIDEO_CALL_BUSY;
//                    sendJPNSWithEndCall(from, to, API.NOTI_MISS_CALL, unreadNum, ip);
                    toUser.increaseUnreadMessage(from);
                    break;
                case Constant.END_CALL_VALUE.CANCEL:
                    partnerResponse = Constant.CALL_ANSWER_VALUE.VIDEO_CALL_REFUSE;
//                    sendJPNSWithEndCall(from, to, API.NOTI_MISS_CALL, unreadNum, ip);
                    toUser.increaseUnreadMessage(from);
                    break;
                case Constant.END_CALL_VALUE.END_CALL_BY_MALE:
                case Constant.END_CALL_VALUE.END_CALL_BY_FEMALE:
                case Constant.END_CALL_VALUE.END_CALL_BY_NOT_ENOUGH_POINT:
                case Constant.END_CALL_VALUE.OTHERS:
                    partnerResponse = Constant.CALL_ANSWER_VALUE.VIDEO_CALL_ANSWER;
                    break;
            }
        } else {
            messageType = MessageType.EVOICE;
            switch (partnerRespondStr) {
                case Constant.END_CALL_VALUE.BUSY:
                    partnerResponse = Constant.CALL_ANSWER_VALUE.VOICE_CALL_BUSY;
//                    sendJPNSWithEndCall(from, to, API.NOTI_MISS_CALL, unreadNum, ip);
                    toUser.increaseUnreadMessage(from);
                    break;
                case Constant.END_CALL_VALUE.CANCEL:
                    partnerResponse = Constant.CALL_ANSWER_VALUE.VOICE_CALL_REFUSE;
//                    sendJPNSWithEndCall(from, to, API.NOTI_MISS_CALL, unreadNum, ip);
                    toUser.increaseUnreadMessage(from);
                    break;
                case Constant.END_CALL_VALUE.END_CALL_BY_MALE:
                case Constant.END_CALL_VALUE.END_CALL_BY_FEMALE:
                case Constant.END_CALL_VALUE.END_CALL_BY_NOT_ENOUGH_POINT:
                case Constant.END_CALL_VALUE.OTHERS:
                    partnerResponse = Constant.CALL_ANSWER_VALUE.VOICE_CALL_ANSWER;
                    break;
            }
        }

        String value = partnerResponse + "|" + callSip + "|" + duration;
        Message msg = new Message(from, to, messageType, value);
        String callSipId = "";
        if (callSip != null) {
            if (callSip.length() >= 17) {
                callSipId = callSip.replaceAll("-", "").substring(0, 17);
            }
        }
        msg.id = from + "&" + to + "&" + callSipId;
        //Core.getDAO().save(msg);
        boolean checkUpdate = Core.getDAO().updateCallMessage(from, to, callSip, value, msg.id);
        Util.addDebugLog(" ===endCall checkUpdate===" + checkUpdate);
        if (!checkUpdate) {
            Core.getDAO().save(msg);
        }
        Util.addDebugLog("endCall " + value);

        List<UserConnection> fkers = Core.getStoreEngine().gets(to);
        if (fkers != null && !fkers.isEmpty()) {
            for (UserConnection fker : fkers) {
                //MessageIO.sendMessage( fker, msg );
                fker.user.inbox.add(msg);
                //fromUser.putLastChat(to, msg);
                fker.user.putLastChat(from, msg);
                Util.addDebugLog("endCall fker.user.inbox.add online");
            }
        } else {
            //for offline users.
            Util.addDebugLog("endCall fker.user.inbox.add offline");
            UserConnection fker = new UserConnection(to);
            fker.user.putLastChat(from, msg);

        }

        List<UserConnection> fkersFrom = Core.getStoreEngine().gets(from);
        if (fkersFrom != null && !fkersFrom.isEmpty()) {
            for (UserConnection fker : fkersFrom) {
                //MessageIO.sendMessage( fker, msg );
                fker.user.inbox.add(msg);
                fker.user.putLastChat(to, msg);
                //fromUser.putLastChat(to, msg);
//                    fker.user.readTimes.put( user, Util.currentTime() );
                Util.addDebugLog("endCall fkersFrom.user.inbox.add online");
            }
        } else {
            //for offline users.
            Util.addDebugLog("endCall fkersFrom.user.inbox.add offline");
            UserConnection fker = new UserConnection(from);
            fker.user.putLastChat(to, msg);
        }

//        fromUser.putLastChat(to, msg);
//        toUser.putLastChat(from, msg);
    }
    //END

    private void totalUnread(JSONObject jo, OutputStream out, Date time) {
        try {

            String userid = (String) jo.get(ParamKey.USER_ID);
//            UserConnection uc = Core.getStoreEngine().get(userid);
//            User user;
//            user = uc == null ? User.getInstance(userid) : uc.user;
//            Map<String, Message> mapLastChat = user.getLastChat();
//            Collection lastchat = mapLastChat != null? mapLastChat.values() : user.lastChats.values();
//            Collection<String> lastChats = getListLastChatsFromLastMessages(userid, lastchat);
            Collection<String> lastChats = LastMessageManager.getCollectionLastMessage(userid);
//            Map<String, Message> mapLastChat = uc.user.getLastChat();
//            Collection lastchat = mapLastChat != null? mapLastChat.values() : uc.user.lastChats.values();
//            Collection<String> lastChats = getListLastChatsFromLastMessages(userid, lastchat);
            // LongLT 07Dec2016 Edit 
            String[] ls = lastChats.toArray(new String[lastChats.size()]);
            List<String> friends = new ArrayList<>();
//            for (String friend : lastChats) {
            for (String friend : ls) {
                if (Validator.isUser(friend) && !BlockUserManager.isBlock(userid, friend)) {
                    friends.add(friend);
                }
            }
//            List<String> friends = Core.getDAO().getListUserIds(lastChats);
//            User.FFUMS.removeBlockUser(userid, friends);
            int unread = UnreadMessageManager.getAllUnreadByUsers(userid, friends);
            out.write(String.valueOf(unread).getBytes());
            Util.addDebugLog("ChatLogHadler() --> totalUnread : " + unread);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    private void checkStateWebsocket(JSONObject jo, OutputStream out, Date time){
         try {
            String result = Core.getStoreEngine().websocketSate();
            out.write(result.getBytes());
            out.flush();
        } catch (IOException ex) {
            Util.addErrorLog(ex);
        }
    }

    private void listConversation(JSONObject jo, OutputStream out, Date time) {
        ArrayList<CaiThia> arrThia = new ArrayList<>();
        try {
            String userid = (String) jo.get(ParamKey.USER_ID);
            String timeStamp = (String) jo.get(ParamKey.TIME_STAMP);
            Date sentTime = DateFormat.parse_yyyyMMddHHmmssSSS(timeStamp);

            Long takeL = (Long) jo.get(ParamKey.TAKE);
            int take = takeL.intValue();

            long serverTime = Util.currentTime();
            sentTime = sentTime == null ? new Date(serverTime) : sentTime;
            UserConnection uc = Core.getStoreEngine().get(userid);
            User user;
            user = uc == null ? User.getInstance(userid) : uc.user;

            Map<String, Message> mapLastChat = user.getLastChat();
            Collection lastchat = mapLastChat != null ? mapLastChat.values() : new HashSet<>();
//            Map<String, Message> mapLastChat = uc.user.getLastChat();
//            Collection lastchat = mapLastChat != null? mapLastChat.values() : uc.user.lastChats.values();
            Iterator iter = lastchat.iterator();
            ArrayList<Message> arLastChat = new ArrayList<>();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj != null) {
                    if (obj instanceof Message) {
                        Message msg = (Message) obj;
                        arLastChat.add(msg);
                    }
                }

            }
            Collections.sort(arLastChat);
            int count = 0;

            HashSet<String> friends = new HashSet<>();
            for (Message msg : arLastChat) {
                if (msg.serverTime < sentTime.getTime()) {
                    boolean isOwn = msg.from.equals(userid);
                    String friendID = isOwn ? msg.to : msg.from;
                    if (Validator.isUser2(friendID) && !BlockUserManager.isBlock(userid, friendID) && !friends.contains(friendID)) {
                        if (UserDAO.getUserInfor(friendID) != null && UserDAO.getUserInfor(friendID).flag != null && UserDAO.getUserInfor(friendID).flag > -1) {
                            Util.addDebugLog("check flag != " + UserDAO.getUserInfor(friendID).flag);
                            CaiThia thia = new CaiThia();
                            thia.isOwn = msg.from.equals(userid);
                            thia.friendID = thia.isOwn ? msg.to : msg.from;
                            thia.lastMsg = msg.value;
                            //HUNGDT add
                            thia.lastMsg = Util.replaceWordChat(thia.lastMsg);
                            if (msg != null && msg.msgType.equals("FILE")) {
                                UserInfo u = UserDAO.getUserInfor(userid);

                                Double dfrom = 1.1;
                                if (u.appVersion != null) {
                                    dfrom = Double.parseDouble(u.appVersion);
                                }
                                if (u.deviceType == 0) {

                                    if (dfrom <= 1.3) {
                                        String[] arr = thia.lastMsg.split("\\|");

                                        if (arr.length >= 3) {
                                            thia.lastMsg = arr[0] + "|" + arr[1] + "|" + arr[2] + "|" + arr[3];
                                        }
                                    }

                                }
                                if (u.deviceType == 1) {

                                    if (dfrom <= 1.2) {
                                        String[] arr = thia.lastMsg.split("\\|");
                                        if (arr.length >= 3) {
                                            thia.lastMsg = arr[0] + "|" + arr[1] + "|" + arr[2] + "|" + arr[3];
                                        }
                                    }

                                }
                            }

                            thia.sentTime = new Date(msg.serverTime);
                            thia.unreadNum = UnreadMessageManager.getAllUnreadByUser(userid, thia.friendID);
                            if (DeactivateUserManager.isDeactivateUser(thia.friendID)) {
                                thia.unreadNum = 0;
                            }
                            thia.msgType = msg.msgType.toString();
                            thia.msgID = msg.id;
                            arrThia.add(thia);
                        }
                        friends.add(friendID);
                        count++;
                    }
                    if (count >= take) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        try {
            String result = CaiThia.toJson(arrThia, true);
            out.write(result.getBytes());
            out.flush();
        } catch (IOException ex) {
            Util.addErrorLog(ex);
        }
    }

    private void getHistory(JSONObject jo, OutputStream out, Date time) {
        try {
            String userid = (String) jo.get(ParamKey.USER_ID);
            String frdid = (String) jo.get(ParamKey.FRDID);
            String timeStampStr = (String) jo.get(ParamKey.TIME_STAMP);
            String ip = (String) jo.get(ParamKey.IP);

            Date d = DateFormat.parse_yyyyMMddHHmmssSSS(timeStampStr);
            Long serverTime = d == null ? Util.currentTime() : d.getTime();
            Long takeL = (Long) jo.get(ParamKey.TAKE);
            int take = takeL.intValue();

            List<Message> ll = Core.getDAO().getHistory(userid, frdid, serverTime, take);
            dataBack(ll, out, userid, time);

            String destUser = (String) frdid;
            UserConnection destUC = Core.getStoreEngine().get(destUser);
            User user;
            UserConnection uc = Core.getStoreEngine().get(userid);
            if (uc == null) {
                user = User.getInstance(userid);
            } else {
                user = uc.user;
            }
            List<String> msgIds = new ArrayList<>();
            for (Message msg : ll) {
                if (msg != null) {
//                    String content = msg.id + "|" + MessageTypeValue.MsgStatus_Read;
//                    //HUNGDT add
//                    content = Util.replaceWordChat(content);
//
//                    Message sendMessage = new Message(userid, destUser, MessageType.MDS, content);
//                    sendMessage.ip = ip;
//
////                    if (destUC != null && destUC.isSendReadMessage) {
//                    if (destUC != null) {
//                        destUC.user.inbox.add(sendMessage);
//                    }
                    msgIds.add(msg.id);
                }
            }
    
            String content = MessageTypeValue.MsgStatus_Read_All;
            Message sendMessage = new Message(userid, frdid, MessageType.MDS, content);
            sendMessage.ip = ip;
//                    if (destUC != null && destUC.isSendReadMessage) {
            if (destUC != null) {
                destUC.user.inbox.add(sendMessage);
            }
            
            Core.getDAO().updateReadMessage(frdid, msgIds);

            updateReadTime(userid, frdid);

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private Collection<String> getListLastChatsFromLastMessages(String userid, Collection lateMessages) {
        Iterator ite = lateMessages.iterator();
        Collection<String> arLastChat = new ArrayList<>();
        while (ite.hasNext()) {
            Object obj = ite.next();
            if (obj != null) {
                if (obj instanceof Message) {
                    Message msg = (Message) obj;
                    boolean isOwn = msg.from.equals(userid);
                    String friendID = isOwn ? msg.to : msg.from;
                    arLastChat.add(friendID);
                }
            }

        }
        return arLastChat;
    }

    private void getNewMessage(JSONObject jo, OutputStream out, Date time) {
        try {
            String userid = (String) jo.get(ParamKey.USER_ID);
            String frdid = (String) jo.get(ParamKey.FRDID);
            String timeStampStr = (String) jo.get(ParamKey.TIME_STAMP);
            String ip = (String) jo.get(ParamKey.IP);

            Date d = DateFormat.parse_yyyyMMddHHmmssSSS(timeStampStr);
            Long serverTime = d == null ? null : d.getTime();

            List<Message> ll = Core.getDAO().getNewMessage(userid, frdid, serverTime);

            dataBack(ll, out, userid, time);

            String destUser = (String) frdid;
            UserConnection destUC = Core.getStoreEngine().get(destUser);
            List<String> msgIds = new ArrayList<>();
            for (Message msg : ll) {
                if (msg != null) {
//                    String content = msg.id + "|" + MessageTypeValue.MsgStatus_Read;
//                    //HUNGDT add
//                    //content = Util.replaceWordChat(content);
//                    Message sendMessage = new Message(userid, destUser, MessageType.MDS, content);
//                    sendMessage.ip = ip;
//
////                    if (destUC != null && destUC.isSendReadMessage) {
//                    if (destUC != null) {
//                        destUC.user.inbox.add(sendMessage);
//                    }
                    msgIds.add(msg.id);
                }
            }
            
            String content = MessageTypeValue.MsgStatus_Read_All;
            Message sendMessage = new Message(userid, frdid, MessageType.MDS, content);
            sendMessage.ip = ip;
//                    if (destUC != null && destUC.isSendReadMessage) {
            if (destUC != null) {
                destUC.user.inbox.add(sendMessage);
            }

            Core.getDAO().updateReadMessage(frdid, msgIds);
            updateReadTime(userid, frdid);

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private void updateReadTime(String userid, String friendid) {
        List<UserConnection> list = Core.getStoreEngine().gets(userid);
//        if (list == null || list.isEmpty()) {
//            UserConnection uc = new UserConnection(userid);
//            uc.user.readTimes.put(friendid, Util.currentTime());
//        } else {
//            for (UserConnection uc : list) {
//                uc.user.readTimes.put(friendid, Util.currentTime());
//            }
//        }
        if (list != null && !list.isEmpty()) {
            for (UserConnection uc : list) {
                uc.user.readTimes.put(friendid, Util.currentTime());
            }
        }
        User u = User.getInstance(userid);
        u.removeUnreadMessage(friendid);
        UnreadMessageUpdater.add(userid);
    }

    public void dataBack(List<Message> ll, OutputStream pw, String userid, Date time) {
        try {
            JSONArray arr = new JSONArray();
            if (ll != null && !ll.isEmpty()) {
                Iterator<Message> iter = ll.iterator();
                while (iter.hasNext()) {
                    Message msg = iter.next();
                    String readTime = msg.readTime != null ? DateFormat.format(msg.readTime) : "";
                    boolean isOwn = msg.from.equals(userid);
                    String timeStamp = DateFormat.format_yyyyMMddHHmmssSSS(msg.serverTime);
                    String content = msg.value;
                    //HUNGDT add
                    //content = Util.replaceWordChat(content);
                    JSONObject obj = new JSONObject();
                    obj.put(ParamKey.CONTENT, content);
                    obj.put(ParamKey.TIME_STAMP, timeStamp);
                    obj.put("is_own", isOwn);
                    obj.put(ParamKey.MSG_TYPE, msg.msgType.toString());
                    obj.put(ParamKey.MESSAGE_ID, msg.id);
                    obj.put(ParamKey.FROM, msg.from);
                    obj.put("to", msg.to);
                    if (msg.files != null) {
                        obj.put(ParamKey.FILES, msg.files);
                    }
                    if (msg.catId != null) {
                        obj.put("cat_id", msg.catId);
                    }
                    if (msg.stickerUrl != null) {
                        obj.put("sticker_url", msg.stickerUrl);
                    }
                    if (readTime != null) {
                        obj.put("read_time", readTime);
                    }
                    if(msg.msgFileId != null){
                        obj.put("msg_file_id", msg.msgFileId);
                    }
                    arr.add(obj);
                }
            }
            //
            pw.write(arr.toJSONString().getBytes());
            pw.flush();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private void addBlock(JSONObject jo, OutputStream out, Date time) {
        String userid;
        String blockedID;
        try {
            userid = (String) jo.get(ParamKey.USER_ID);
            blockedID = (String) jo.get(ParamKey.REQUEST_USER_ID);
            if (userid == null || blockedID == null) {
                return;
            }

            BlockUserManager.add(userid, blockedID);
            BlockUserManager.add(blockedID, userid);

            User user;
            UserConnection uc = Core.getStoreEngine().get(userid);
            user = uc == null ? User.getInstance(userid) : uc.user;
//            user.lastChats.remove(blockedID);
            LastMessageManager.remove(userid, blockedID);
//            user.removeLastChat(blockedID);
            LastChatDAO.removeLastChatList(userid, blockedID);

            long readtime = user.readTimes.get(blockedID) != null ? user.readTimes.get(blockedID) : Util.currentTime();
            Filer.putLastReadTimeBlockUser(userid, blockedID, readtime);
            user.readTimes.remove(blockedID);
            UnreadMessageUpdater.add(userid);

            User fUser;
            UserConnection f = Core.getStoreEngine().get(blockedID);
            fUser = f == null ? User.getInstance(blockedID) : f.user;
//            fUser.lastChats.remove(userid);
            LastMessageManager.remove(blockedID, userid);
//            fUser.removeLastChat(userid);
            LastChatDAO.removeLastChatList(blockedID, userid);
            readtime = fUser.readTimes.get(userid) != null ? fUser.readTimes.get(userid) : Util.currentTime();
            Filer.putLastReadTimeBlockUser(blockedID, userid, readtime);
            fUser.readTimes.remove(userid);
            UnreadMessageUpdater.add(blockedID);

            Util.addDebugLog("ChatLogHadler() --> listConversation : Successful");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private void removeBlock(JSONObject jo, OutputStream out, Date time) {
        String userid;
        String rmID;
        try {
            userid = (String) jo.get(ParamKey.USER_ID);
            rmID = (String) jo.get("blk_user_id");
            if (userid == null || rmID == null) {
                return;
            }

            BlockUserManager.remove(userid, rmID);
            BlockUserManager.remove(rmID, userid);

            User user1;
            UserConnection u1 = Core.getStoreEngine().get(userid);
            user1 = u1 == null ? User.getInstance(userid) : u1.user;
            if (user1.readTimes.get(rmID) == null) {
                user1.readTimes.put(rmID, Filer.getLastReadTimeBlockUser(userid, rmID));
            }
            List<Message> lm1 = Core.getDAO().getHistory(userid, rmID, System.currentTimeMillis(), 1);
            if (lm1 != null && !lm1.isEmpty()) {
                Message m1 = lm1.get(0);
                user1.putLastChat(rmID, m1);
            }
            UnreadMessageUpdater.add(userid);

            User user2;
            UserConnection u2 = Core.getStoreEngine().get(rmID);
            user2 = u2 == null ? User.getInstance(rmID) : u2.user;
            if (user2.readTimes.get(userid) == null) {
                user2.readTimes.put(userid, Filer.getLastReadTimeBlockUser(rmID, userid));
            }
            List<Message> lm2 = Core.getDAO().getHistory(rmID, userid, System.currentTimeMillis(), 1);
            if (lm2 != null && !lm2.isEmpty()) {
                Message m2 = lm2.get(0);
                user2.putLastChat(userid, m2);
            }
            UnreadMessageUpdater.add(rmID);

            Util.addDebugLog("ChatLogHadler() --> listConversation : Successful");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private static void sendJPNSPing(String fromUserid, String toUserid, int typecall) {

        int badge = UnreadMessageManager.getAllUnreadMessage(toUserid);
        JSONObject jo = new JSONObject();
        jo.put(ParamKey.FROM_USER_ID, fromUserid);
        jo.put(ParamKey.TOUSERID, toUserid);
        jo.put(ParamKey.API_NAME, API.NOTI_PING);
//        jo.put(ParamKey.BADGE, badge + 1);
        jo.put(ParamKey.BADGE, badge);
        //jo.put(ParamKey.IP, ip);

        jo.put("typecall", typecall);

        String msg = jo.toJSONString();

        Util.sendRequest(msg, Config.JPNSServer, Config.JPNSPort);

    }

    private static void resetConfig() {
        Config.initConfig();
    }

    private void delMessage(JSONObject jo, Date time) {
        String userId = (String) jo.get(ParamKey.USER_ID);
        String friendId = (String) jo.get(ParamKey.FRDID);
        JSONArray messageId = (JSONArray) jo.get(ParamKey.MESSAGE_ID);
        List<String> listMsgId = new LinkedList<>();
        if (messageId != null) {
            listMsgId.addAll(messageId);
        }

        UserConnection uc = Core.getStoreEngine().get(userId);

        if (!listMsgId.isEmpty()) {
            List<String> messageValueList = Core.getDAO().getFileValueById(userId, listMsgId);
            String lastChatMsg = LastMessageManager.getHashMapLastMessage(userId).get(friendId);
            Message lastChat = LastChatDAO.getLastChat(userId, friendId);

            Core.getDAO().delMsg(userId, listMsgId);
            Message lastMsg = Core.getDAO().getLastMessage(userId, friendId);
            if ((lastChatMsg != null && listMsgId.contains(lastChatMsg)) || (lastChat.msgType == MessageType.FILE && messageValueList.contains(lastChat.value))) {
                if (lastMsg != null) {
                    LastChatDAO.putLastChatList(userId, friendId, lastMsg);
                    LastMessageManager.update(userId, friendId, lastMsg);
                } else {
                    LastChatDAO.removeLastChatList(userId, friendId);
                    LastMessageManager.remove(userId, friendId);
                }
            }
        }

    }
}
