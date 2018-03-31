/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.workerfactory.impl;

import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.Core;
import com.vn.ntsc.blacklist.BlockUserManager;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;
import com.vn.ntsc.chatserver.pojos.user.UnreadMessageManager;
import com.vn.ntsc.chatserver.pojos.user.User;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;
import com.vn.ntsc.chatserver.pojos.user.UserInfo;
import com.vn.ntsc.chatserver.workerfactory.IWorker;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.dao.impl.UserInteractionDAO;
import com.vn.ntsc.utils.Validator;
import org.json.simple.JSONArray;

/**
 *
 * @author tuannxv00804
 */
public class Worker implements IWorker {

    @Override
    public void run() {
        while (true) {
            try {
                UserConnection uc = Core.getStoreEngine().poll();
                
                Core.getStoreEngine().removeSessionSameToken(uc);//remove session same token
                boolean isContinue = uc != null && MessageIO.isClientAlive(uc);
                try {
                    if (isContinue) {
                        processInbox(uc);
                        processOutbox(uc);

                        Core.getStoreEngine().put(uc);

                    } else if (uc != null) {
                        Core.getStoreEngine().remove(uc);
                        proccessDeletingConnection(uc);
                    }
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }

                if (!isContinue) {
                    sleep();
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }

        }
    }

    private void sleep() {
        try {
            Thread.sleep(Config.IdleThreadLatency);
        } catch (InterruptedException ex) {
            Util.addErrorLog(ex);
        }
    }

    private void processInbox(UserConnection uc) {
        try {
            for (int i = 0; i < uc.user.inbox.size(); i++) {
                Message msg = uc.user.inbox.poll();

                Util.addDebugLog("MSG SEND TO CLIENT=========================================== " + msg.toString());
                int sentResult = MessageIO.sendMessageWebSocket(uc, msg);

                //Try sending the second time if there are error on network connection.
                if (sentResult == 1) {
                    Util.addDebugLog("RESEND MESSEAGE======================================");
                    MessageIO.sendMessageWebSocket(uc, msg);
                }

                if (msg.msgType == MessageType.PP
                        || msg.msgType == MessageType.PE
                        || msg.msgType == MessageType.WINK
                        || (msg.msgType == MessageType.FILE)
                        || msg.msgType == MessageType.GIFT
                        || msg.msgType == MessageType.AUDIO
                        || msg.msgType == MessageType.VIDEO
                        || msg.msgType == MessageType.IMAGE
                        || msg.msgType == MessageType.STK
                        || msg.msgType == MessageType.LCT
                        || msg.msgType == MessageType.CALLREQ) {
                    uc.user.putLastChat(msg.from, msg);
//                    updateReadTime(uc, msg);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

    }

    private void processOutbox(UserConnection uc) {
        if(uc != null && uc.user.outbox.size() > 0){
            Util.addDebugLog("PROCESSOUTBOX===============================");
        }
        for (int i = 0; i < uc.user.outbox.size(); i++) {
            Message msg = uc.user.outbox.poll();
            
            if (msg == null ) {
                continue;
            }
            msg.serverTime = Util.currentTime();
            
            if(msg.msgType == MessageType.BUZZCMT || msg.msgType == MessageType.BUZZSUBCMT ){
                if(msg.api != null) msg.api = null;
                if (msg.isApp == 0) {
                    if (Validator.isUser(msg.from)) {
                        List<UserConnection> toUsers = Core.getStoreEngine().gets(msg.from);
                        if (toUsers != null && !toUsers.isEmpty()) {
                            for (UserConnection toUser : toUsers) {
                                toUser.user.inbox.add(msg);
                            }
                        }

                    }
                    continue;
                }
                List<String> listToStr = Core.getStoreEngine().getListUserTo(msg.buzzId);
                if(listToStr != null && !listToStr.isEmpty()){
                    for(String toStr : listToStr){
                        Util.addDebugLog("toStr------------------------------------------"+toStr);
    //                    confirmMessageSent(null,uc, msg, true);
                        if(Validator.isUser(toStr)){
                            boolean isFromUserBlocked = BlockUserManager.isBlock(msg.from, toStr);
                             if (!isFromUserBlocked) {
                                 List<UserConnection> toUsers = Core.getStoreEngine().gets(toStr);
                                 if (toUsers != null && !toUsers.isEmpty()) {
                                     for (UserConnection toUser : toUsers) {
    //                                     if(!toUser.webSocKet.equals(uc.webSocKet))
                                            toUser.user.inbox.add(msg);
                                     }
                                 }
                             }else{
                                 Util.addDebugLog("2 USER BLOCK=======================================");
//                                   Core.getStoreEngine().remove(msg.buzzId, toStr);
                             }
                        }
                    }
                }else{
                    Util.addDebugLog("Have not user join buzz");
                }
                continue;
            }
            String toStr = msg.to;
            if (msg.msgType == MessageType.PP
                    || msg.msgType == MessageType.WINK
                    || msg.msgType == MessageType.FILE
                    || msg.msgType == MessageType.STK
                    || msg.msgType == MessageType.LCT
                    || msg.msgType == MessageType.GIFT
                    || msg.msgType == MessageType.PE
                    || msg.msgType == MessageType.AUDIO
                    || msg.msgType == MessageType.VIDEO
                    || msg.msgType == MessageType.IMAGE
                    || msg.msgType == MessageType.MDS
                   
                    ) {
                int type = 1;
                if (msg.msgType == MessageType.WINK) {
                    type = 2;
                }
                List<UserConnection> users = Core.getStoreEngine().gets(msg.from);
                for (UserConnection ucFrom : users){
                    confirmMessageSent(uc,ucFrom, msg, true);
                }
            }else{
                if(msg.msgType == MessageType.AUTH){
                    continue;
                }
            }
            if (toStr.equals(Config.ServerName) || Validator.isUser(toStr)) {
                boolean isFromUserBlocked = BlockUserManager.isBlock(msg.from, msg.to);
//                boolean isFromUserBlocked = false;
                if (!isFromUserBlocked) {
                    if (!msg.to.equals(msg.from)) {
                       
                        if (msg.msgType == MessageType.PP
                                || msg.msgType == MessageType.PE
                                || msg.msgType == MessageType.WINK
                                || (msg.msgType == MessageType.FILE)
                                || msg.msgType == MessageType.STK
                                || msg.msgType == MessageType.GIFT
                                || msg.msgType == MessageType.AUDIO
                                || msg.msgType == MessageType.VIDEO
                                || msg.msgType == MessageType.IMAGE
                                || msg.msgType == MessageType.CALLREQ
                                || msg.msgType == MessageType.LCT) {
//                            UserConnection toUc = new UserConnection(msg.to);
//                            toUc.user.increaseUnreadMessage(msg.from);
                            User toUser = User.getInstance(msg.to);
                            toUser.increaseUnreadMessage(msg.from);

                        }
                        List<UserConnection> toUsers = Core.getStoreEngine().gets(toStr);

                        /**
                         * toUser is ONLINE.
                         */
                        if (toUsers != null && !toUsers.isEmpty()) {
                            Util.addDebugLog("=====================================================User Online");
                            for (UserConnection toUser : toUsers) {
                                if (msg.msgType == MessageType.MDS) {
                                    processMessageStatus(uc, msg, toUser);

                                } else if (msg.msgType == MessageType.PP || msg.msgType == MessageType.PE) {
                                    proccessChatMessage(uc, msg, toUser);

                                } else if (msg.msgType == MessageType.PRC) {
                                    processPresenceMessage(uc, msg, toUser);

                                } else if (msg.msgType == MessageType.WINK) {
                                    processWinkMessage(uc, msg, toUser);

                                } else if (msg.msgType == MessageType.FILE || msg.msgType == MessageType.AUDIO
                                                                            || msg.msgType == MessageType.VIDEO || msg.msgType == MessageType.IMAGE) {
                                    processFileMessage(uc, msg, toUser);
                                } else if (msg.msgType == MessageType.CMD) {
                                    proccessCMDMessage(uc, msg, toUser);

                                } else if (msg.msgType == MessageType.GIFT) {
                                    processGiftMessage(uc, msg, toUser);

                                } else if (msg.msgType == MessageType.STK) {
                                    processSticker(uc, msg, toUser);

                                } else if (msg.msgType == MessageType.LCT) {
                                    processLocation(uc, msg, toUser);

                                } else if (msg.msgType == MessageType.SVOICE
                                        || msg.msgType == MessageType.SVIDEO
                                        || msg.msgType == MessageType.EVOICE
                                        || msg.msgType == MessageType.EVIDEO) {
                                    //processVoiceAndVideo(uc, msg, toUser);
                                } else if (msg.msgType == MessageType.CALLREQ) {
                                    processCallRequest(uc, msg, toUser);
                                } else if (msg.msgType == MessageType.BUZZTAG){
                                    toUser.user.inbox.add(msg);
//                                    processTagMessage(uc,msg,null);
                                }else if (msg.msgType == MessageType.NOTIBUZZ){
                                    proccessNotiWebSocket(uc, msg, toUser);
                                }

                            }

                        } else {
                            /**
                             * toUser is OFFLINE or Message is intended to
                             * sending to server.
                             */
                            Util.addDebugLog("===============================================User Offline");
                            if (msg.msgType == MessageType.PP || msg.msgType == MessageType.PE) {
                                proccessChatMessage(uc, msg, null);

                            } else if (msg.msgType == MessageType.WINK) {
                                processWinkMessage(uc, msg, null);

                            } else if (msg.msgType == MessageType.PRC) {
                                processPresenceMessage(uc, msg, null);

                            } else if (msg.msgType == MessageType.FILE || msg.msgType == MessageType.AUDIO
                                                                            || msg.msgType == MessageType.VIDEO || msg.msgType == MessageType.IMAGE) {
                                processFileMessage(uc, msg, null);

                            } else if (msg.msgType == MessageType.CMD) {
                                proccessCMDMessage(uc, msg, null);

                            } else if (msg.msgType == MessageType.GIFT) {
                                processGiftMessage(uc, msg, null);

                            } else if (msg.msgType == MessageType.STK) {
                                processSticker(uc, msg, null);

                            } else if (msg.msgType == MessageType.LCT) {
                                processLocation(uc, msg, null);

                            } else if (msg.msgType == MessageType.SVOICE
                                    || msg.msgType == MessageType.SVIDEO
                                    || msg.msgType == MessageType.EVOICE
                                    || msg.msgType == MessageType.EVIDEO) {
                                //processVoiceAndVideo(uc, msg, null);
                            } else if (msg.msgType == MessageType.CALLREQ) {
                                processCallRequest(uc, msg, null);
                            } else if(msg.msgType == MessageType.BUZZTAG){
//                                processTagMessage(uc,msg,null);
                            }
                        }

                    }
                } else {
                    //Hai users đã block nhau:
                    List<UserConnection> toUsers = Core.getStoreEngine().gets(toStr);
                    if (toUsers == null || toUsers.isEmpty()) {
                        proccessCMDMessage(uc, msg, null);
                    } else {
                        for (UserConnection toUser : toUsers) {
                            proccessCMDMessage(uc, msg, toUser);
                        }
                    }
                }

            } else {
                //toUser is deactive
                Util.addDebugLog("=========================================User Deactive");
                if (msg.msgType == MessageType.PP
                        || msg.msgType == MessageType.PE
                        || msg.msgType == MessageType.WINK
                        || msg.msgType == MessageType.FILE
                        || msg.msgType == MessageType.AUDIO
                        || msg.msgType == MessageType.VIDEO 
                        || msg.msgType == MessageType.IMAGE
                        || msg.msgType == MessageType.GIFT
                        || msg.msgType == MessageType.STK
                        || msg.msgType == MessageType.LCT
                        || msg.msgType == MessageType.CALLREQ) {
                    uc.user.putLastChat(msg.to, msg);
                    updateReadTime(uc, msg);
                    boolean isFromUserBlocked = BlockUserManager.isBlock(msg.from, msg.to);
                    if (!isFromUserBlocked) {
                        if (!msg.to.equals(msg.from)) {
                            User toUser = User.getInstance(toStr);
                            toUser.putLastChat(msg.from, msg);
                        }
                    }
                } else if (msg.msgType == MessageType.MDS && msg.value.contains("call")) {
                    proccessCMDMessage(uc, msg, null);
                } else if (msg.msgType == MessageType.EVIDEO
                        || msg.msgType == MessageType.EVOICE
                        || msg.msgType == MessageType.SVIDEO
                        || msg.msgType == MessageType.SVOICE) {
                    //processVoiceAndVideo(uc, msg, null);
                }
            }
            if (msg.msgType == MessageType.PP
                    || msg.msgType == MessageType.PE
                    || msg.msgType == MessageType.WINK
                    || msg.msgType == MessageType.FILE
                    || msg.msgType == MessageType.AUDIO
                    || msg.msgType == MessageType.VIDEO
                    || msg.msgType == MessageType.IMAGE
                    || msg.msgType == MessageType.GIFT
                    || msg.msgType == MessageType.STK
                    || msg.msgType == MessageType.LCT
                    || msg.msgType == MessageType.CALLREQ
                    || (msg.msgType == MessageType.MDS && msg.value != null && !msg.value.contains("call"))) {
                Core.getLogger().log(msg);

                //Linh add #5747 - 20170227
                if (!msg.from.equalsIgnoreCase(msg.to) && !msg.from.equalsIgnoreCase(Config.ServerName) && !msg.to.equalsIgnoreCase(Config.ServerName)) {
                    addInteraction(msg.from, msg.to);
                }
            }

//            if(msg.msgType == MessageType.WINK){
//                sendMainServer("wink_log", msg, true);
//            }else if(msg.msgType == MessageType.SVOICE
//                    || msg.msgType == MessageType.SVIDEO
//                    || msg.msgType == MessageType.EVOICE
//                    || msg.msgType == MessageType.EVIDEO) {
//                sendMainServer("call_log", msg, true);
//                }else 
//            if (msg.msgType == MessageType.FILE) {
//                sendUMSServer("send_image_by_chat", msg);
//            }
//            }else if(msg.msgType == MessageType.CMD) {
//                
//                if(msg.value.contains("call")){
//                    sendMainServer("call_log", msg, true);
//                }
//            }
        }
    }
    private void processCallRequest(UserConnection uc, Message msg, UserConnection toUser) {
        if (toUser != null) {
            toUser.user.inbox.add(msg);
        } else {
//            toUser = new UserConnection(msg.to);
//            toUser.user.putLastChat(msg.from, msg);
            User toU = User.getInstance(msg.to);
            toU.putLastChat(msg.from, msg);
//            Filer.saveLastChats(toUser.user.lastChats, toUser.user.username);
        }
        uc.user.putLastChat(msg.to, msg);

        updateReadTime(uc, msg);
        if (toUser == null || toUser.webSocKet == null) {
            int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
            sendJPNS(toUser, msg.from, msg.to, "noti_call_request", unreadNum, msg.ip,msg.avaUrlFrom);
        }
    }

    private void processGiftMessage(UserConnection uc, Message msg, UserConnection toUser) {
        proccessChatMessage(uc, msg, toUser);

    }

    private boolean isSendCMDMessage(Message msg) {
        return msg.msgType == MessageType.CMD
                && !msg.value.contains("call")
                && !msg.value.contains("hearbeat");
    }

    private void proccessCMDMessage(UserConnection uc, Message msg, UserConnection toUser) {
        if (toUser != null && toUser.webSocKet != null) {
            if (isSendCMDMessage(msg)) {
                toUser.user.inbox.add(msg);
            }
        }
//        else {
//            if (msg.value.contains("ping")) {
//                toUser = new UserConnection(msg.to);
//                int unreadNum = UnreadMessageManager.getAllUnreadMessage(toUser.user.username);
//                sendJPNS(msg.from, msg.to, "noti_voip_calling", unreadNum - 1, msg.ip);
//            }
//        }
        if (msg.value.contains("ping")) {
            int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
            //HUNGDT #7185
            String typecall = "";
            if (msg.value != null && msg.value.length() > 0) {
                String[] newping = msg.value.split("-");
                if (newping.length > 1) {
                    typecall = newping[1];
                }
            }
            //sendJPNSPing(toUser, msg.from, msg.to, API.NOTI_PING, unreadNum, msg.ip,typecall);
        } else if (msg.value.contains("hearbeat")) {
            uc.resetExpiredDate();
        }
//        else if(msg.value.contains("call")){
//            User toU;
//            if (toUser == null || toUser.soc == null) {
//                toU = User.getInstance(msg.to);
//            } else {
//                toUser.user.inbox.add(msg);
//                toU = toUser.user;
//            }
//            String startMessageId = getMsgFromCountMessage(msg);
//            Util.addDebugLog("CMD call startId : " + startMessageId );
//            if(startMessageId != null){
//                Message startMessage = Core.getDAO().getMessages(msg.from, startMessageId);
//                if(startMessage != null){
//                    String answer = Constant.CALL_ANSWER_VALUE.VIDEO_CALL_ANSWER;
//                    MessageType messageType = MessageType.EVIDEO;
//                    if(startMessage.msgType == MessageType.EVOICE){
//                        messageType = MessageType.EVOICE;
//                        answer = Constant.CALL_ANSWER_VALUE.VOICE_CALL_ANSWER;
//                    }
//                    String value = msg.value.replace("call", answer);
//                    Message message = new Message(startMessageId, msg.from, msg.to, messageType, value, msg.originTime, Util.currentTime(), msg.destinationTime);
//                    toU.putLastChat(msg.from, message);
//                    Core.getLogger().log(message);
//                }
//            }
//        }
    }

    private String getMsgFromCountMessage(Message msg) {
        if (msg != null && msg.msgType == MessageType.CMD && msg.value.contains("call")) {
            String[] valueElements = msg.value.split("\\|");
            if (valueElements != null && valueElements.length == 3) {
                return valueElements[1];
            }
        }
        return null;
    }

//    private void confirmMessageSent(UserConnection uc, Message msg, ChatPayment chatPayment, boolean isSent) {
    private void confirmMessageSent(UserConnection uc,UserConnection ucFrom, Message msg, boolean isSent) {
        String messageTypeValue = MessageTypeValue.MsgStatus_Sent_Other;
        if (!isSent) {
            // not enough point
            messageTypeValue = MessageTypeValue.MsgStatus_Unsent;
        }
        if(uc != null && uc.webSocKet.equals(ucFrom.webSocKet)){
            messageTypeValue =  MessageTypeValue.MsgStatus_Sent;
        }
        
        Message confirmSentMsg = new Message(ucFrom.user.username, msg.to, MessageType.MDS, messageTypeValue, Util.currentTime(),msg.value,msg.msgId,msg.originTime);
        if(msg.msgType == MessageType.FILE){
            confirmSentMsg.files = msg.files;
            confirmSentMsg.typeMDS = MessageType.FILE.name();
            if(msg.msgFileId != null){
                confirmSentMsg.msgFileId = msg.msgFileId;
            }
        }
        if(msg.msgType == MessageType.STK){
            confirmSentMsg.catId = msg.catId;
            confirmSentMsg.stickerUrl = msg.stickerUrl;
            confirmSentMsg.typeMDS = MessageType.STK.name();
        }
        if(msg.msgType == MessageType.PP){
            confirmSentMsg.typeMDS = MessageType.PP.name();
        }
        if(msg.msgType == MessageType.PE){
            confirmSentMsg.typeMDS = MessageType.PE.name();
        }
        if(msg.msgType == MessageType.GIFT){
            confirmSentMsg.typeMDS = MessageType.GIFT.name();
            if(uc.user.token.equals(msg.token)){
                messageTypeValue =  MessageTypeValue.MsgStatus_Sent;
            }
        }
        if(msg.msgType == MessageType.MDS){
            confirmSentMsg.typeMDS = MessageType.MDS.name();
        }
        
        if (ucFrom != null && ucFrom.webSocKet != null && confirmSentMsg != null) {
            Util.addDebugLog("---------------- send msg to webSocket :" + confirmSentMsg);
            MessageIO.sendMessageWebSocket(ucFrom, confirmSentMsg);
        }
    }

    private void confirmUserOffline(UserConnection uc, Message msg) {
//        Util.addDebugLog(new Date(Util.currentTime()).toString() + "Offline message:  From : " + msg.from + " to :  " + msg.to + " msg : " + msg.value);                
        Message offlineConfirm = new Message(msg.to, uc.user.username, MessageType.PRC, MessageTypeValue.Presence_Offline, Util.currentTime(),msg.token);
        MessageIO.sendMessage(uc, offlineConfirm);
    }

    private void processMessageStatus(UserConnection uc, Message msg, UserConnection toUser) {
        String msgTypeValue = MessageTypeValue.MsgStatus_getStatus(msg);
        /**
         * Message confirm from client. Confirm received.
         */
        //confirm to origin user if he online
        if (msgTypeValue != null
                && (msgTypeValue.equals(MessageTypeValue.MsgStatus_Delivered))
                && toUser != null) {
            toUser.user.inbox.add(msg);
        }
        if (msgTypeValue != null
                && (msgTypeValue.equals(MessageTypeValue.MsgStatus_Read) || msgTypeValue.equals(MessageTypeValue.MsgStatus_Read_All)) && toUser != null) {
            toUser.user.inbox.add(msg);
            updateReadTime(uc, msg);
        }
//        Core.getLogger().log( msg );
    }
    
    //send Noti by Websocket
    private void proccessNotiWebSocket(UserConnection uc, Message msg, UserConnection toUser){
        Util.addDebugLog("proccessNotiWebSocket========================="+msg.to);
        toUser.user.inbox.add(msg);
    }

    private void proccessChatMessage(UserConnection uc, Message msg, UserConnection toUser) {
//        confirmMessageSent(uc, msg);
        if (toUser == null) {
            confirmUserOffline(uc, msg);

//            toUser = new UserConnection(msg.to);
//            toUser.user.putLastChat(msg.from, msg);
            User toU = User.getInstance(msg.to);
            toU.putLastChat(msg.from, msg);
//            Filer.saveLastChats(toUser.user.lastChats, toUser.user.username);

            //sendNotification( msg );
        } else {
            toUser.user.inbox.add(msg);
        }
        uc.user.putLastChat(msg.to, msg);

        updateReadTime(uc, msg);

        String api;
        if (msg.msgType == MessageType.GIFT) {
            api = "noti_gave_gift";
            if (toUser == null || toUser.webSocKet == null) {

                int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
                sendJPNS(toUser, msg.from, msg.to, api, unreadNum, msg.ip,msg.avaUrlFrom);
            }
        } else if ((msg.msgType == MessageType.PP || msg.msgType == MessageType.PE) && (toUser == null || toUser.webSocKet == null)) {
//        } else if (msg.msgType == MessageType.PP) {            
            api = "noti_new_chat_msg_text";
            int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
            sendJPNS(toUser, msg.from, msg.to, api, unreadNum, msg.ip,msg.avaUrlFrom);
        }
    }

//    public static void sendNotification( Message msg ) {
//        System.out.println( "sendNotification() -> msg = " ); msg.printStruct();
//        
//        String api = msg.msgType == MessageType.PP ? API.NotifyNewChatMessage
//                 : msg.msgType == MessageType.GIFT ? API.NotifyNewGift : "";
//        
//        JSONObject jo = new JSONObject();
//        jo.put( ParamKeys.API, api );
//        jo.put( ParamKeys.From, msg.from );
//        jo.put( ParamKeys.To, msg.to );
//
//        String str = jo.toJSONString();
//
//        try {
//            URL url = new URL( "http://" + Config.JPNSServer + ":" + Config.JPNSPort + "/" + str );
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.getInputStream();
//
//        } catch( Exception ex ) {
//            System.out.println( "Exception while SENDING NOTIFICATION: msg = " ); msg.printStruct();
//           
//        }
//    }
    private Message offlineMessage(String from, String to) {
        return new Message(from, to, MessageType.PRC, MessageTypeValue.Presence_Offline);
    }

    private Message onlineMessage(String from, String to) {
        return new Message(from, to, MessageType.PRC, MessageTypeValue.Presence_Online);
    }

    private void sendOfflineMsg(String friendName, UserConnection toUser) {
        if (toUser == null) {
//            String msg = "Worker.sendOfflineMsg() -> toUser is null. FriendName = " + friendName;
//            
            return;
        }
        Message offlineMsg = new Message(friendName, toUser.user.username, MessageType.PRC, MessageTypeValue.Presence_Offline);
        MessageIO.sendMessage(toUser, offlineMsg);
    }

    private void sendOnlineMsg(String friendName, UserConnection toUser) {
        Message onlineMsg = new Message(friendName, toUser.user.username, MessageType.PRC, MessageTypeValue.Presence_Online);
        MessageIO.sendMessage(toUser, onlineMsg);
    }

    private void sendStatusMsg(String friendName, String friendStatus, UserConnection toUser) {
        Message sttMsg = new Message(friendName, toUser.user.username, MessageType.PRC, friendStatus);
        MessageIO.sendMessage(toUser, sttMsg);
    }

    /**
     * Presence the following state of a user: 1. Online 2. Offline 3. Busy 4.
     * Away 5. User's custom 6. Writing 7. Stop Writing 8. Erasing The 3 last is
     * only apply for per-to-per message presence.
     *
     * @param uc
     * @param msg
     */
    private void processPresenceMessage(UserConnection uc, Message msg, UserConnection toUser) {
        String value = msg.value;
        if (toUser != null) {
//            if( value.equals( MessageTypeValue.Presence_Writing )
//                    || value.equals( MessageTypeValue.Presence_StopWriting )
//                    || value.equals( MessageTypeValue.Presence_Erasing ) ) {
            toUser.user.inbox.add(msg);
//            }

        }
//        else {
//            uc.status = value;
//            if (uc.user.lastChats == null) {
//                return;
//            }
//
//            Set<String> friendList = uc.user.lastChats.keySet();
//            Iterator<String> iter = friendList.iterator();
//            while (iter.hasNext()) {
//                String friendid = iter.next();
//                List<UserConnection> l = Core.getStoreEngine().gets(friendid);
//                if (l == null || l.isEmpty()) {
//                    return;
//                }
//                for (int i = 0; i < l.size(); i++) {
//                    UserConnection f = l.get(i);
//                    MessageIO.sendMessage(f, msg);
//                }
//            }
//
//        }
    }

    private void processWinkMessage(UserConnection uc, Message msg, UserConnection toUser) {
//        msg.serverTime = System.currentTimeMillis();
        if (toUser != null) {
            toUser.user.inbox.add(msg);
        } else {
//            toUser = new UserConnection(msg.to);
//            toUser.user.putLastChat(msg.from, msg);
            User toU = User.getInstance(msg.to);
            toU.putLastChat(msg.from, msg);
//            Filer.saveLastChats(toUser.user.lastChats, toUser.user.username);
        }
        uc.user.putLastChat(msg.to, msg);

        //updateReadTime( uc, msg );
        if (toUser == null || toUser.webSocKet == null) {
//            toUser = new UserConnection(msg.to);
            int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
            sendJPNS(toUser, msg.from, msg.to, "noti_new_chat_msg_text", unreadNum, msg.ip,msg.avaUrlFrom);
        }
//        sendMainServer("wink_log", msg, false);

    }

    private void processSticker(UserConnection uc, Message msg, UserConnection toUser) {
        if (toUser != null) {
            toUser.user.inbox.add(msg);
        } else {
//            toUser = new UserConnection(msg.to);
//            toUser.user.putLastChat(msg.from, msg);
            User toU = User.getInstance(msg.to);
            toU.putLastChat(msg.from, msg);
//            Filer.saveLastChats(toUser.user.lastChats, toUser.user.username);
        }
        uc.user.putLastChat(msg.to, msg);

        updateReadTime(uc, msg);
        if (toUser == null || toUser.webSocKet == null) {
//            toUser = new UserConnection(msg.to);
            int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
            sendJPNS(toUser, msg.from, msg.to, "noti_new_chat_msg_sticker", unreadNum, msg.ip,msg.avaUrlFrom);
        }
    }

    private void processLocation(UserConnection uc, Message msg, UserConnection toUser) {
        if (toUser != null) {
            toUser.user.inbox.add(msg);
        } else {
//            toUser = new UserConnection(msg.to);
//            toUser.user.putLastChat(msg.from, msg);
            User toU = User.getInstance(msg.to);
            toU.putLastChat(msg.from, msg);
//            Filer.saveLastChats(toUser.user.lastChats, toUser.user.username);
        }
        uc.user.putLastChat(msg.to, msg);

        updateReadTime(uc, msg);
        if (toUser == null || toUser.webSocKet == null) {
//            toUser = new UserConnection(msg.to);
            int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
            sendJPNS(toUser, msg.from, msg.to, "noti_new_chat_msg_location", unreadNum, msg.ip,msg.avaUrlFrom);
        }
    }

    private static void sendJPNS(UserConnection toUc, String fromUserid, String toUserid, String api, int badge, String ip, String avaUrlFrom) {

        JSONObject jo = new JSONObject();
        jo.put(ParamKey.FROM_USER_ID, fromUserid);
        jo.put(ParamKey.TOUSERID, toUserid);
        jo.put(ParamKey.AVATAR, avaUrlFrom);
        jo.put(ParamKey.API_NAME, api);
//        jo.put(ParamKey.BADGE, badge + 1);
        jo.put(ParamKey.BADGE, badge);
        jo.put(ParamKey.IP, ip);

        String msg = jo.toJSONString();
        int ntfSetting = 0;
        //when api = noti_ping, send from all
        if (!api.equals(API.NOTI_PING) && !api.equals("noti_call_request")) {
            if (toUc == null) {
                toUc = Core.getStoreEngine().get(toUserid);
            }
            ntfSetting = toUc == null ? User.FFUMS.getChatNotificationSetting(toUserid) : toUc.user.sendChatNotificationSetting;
        }
        Util.addDebugLog("===============sendJPNS " + ntfSetting);
        switch (ntfSetting) {
            case 1:
                //receive only from friends and favourist
//                UserConnection toUC = Core.getStoreEngine().get(toUserid);
                if (toUc == null) {
                    toUc = Core.getStoreEngine().get(toUserid);
                }
                List<String> favorists = toUc == null ? User.FFUMS.getFavouristList(toUserid) : toUc.user.favouristList;
//                toUc = toUc == null ?  new UserConnection(toUserid) : toUc;
//                if (toUc.user.favouristList.contains(fromUserid)) {
                if (favorists.contains(fromUserid)) {
                    Util.sendRequest(msg, Config.JPNSServer, Config.JPNSPort);
                }
                break;
            case 0:
                //receive from all
                Util.sendRequest(msg, Config.JPNSServer, Config.JPNSPort);
                break;
            case -1:
                Util.sendRequest(msg, Config.JPNSServer, Config.JPNSPort);
                break;
            //do not receive
        }

    }
    
    private static void sendJPNSTagBuzz(UserConnection toUc, String fromUserid, String toUserid, String api,String buzzId, String userName) {
        Util.addDebugLog("userName======================="+userName);
        JSONArray listFvt = new JSONArray();
        listFvt.add(toUserid);
        if (listFvt != null && !listFvt.isEmpty()) {
            JSONObject objNoti = new JSONObject();
            objNoti.put(ParamKey.API_NAME, API.NOTI_SHARE_MUSIC);
            objNoti.put(ParamKey.USER_NAME, userName);
            objNoti.put(ParamKey.FROM_USER_ID, fromUserid);
            objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
            objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
            Util.sendRequest(objNoti.toJSONString(), Config.JPNSServer, Config.JPNSPort);

        }

    }

    //HUNGDT #7185
    private static void sendJPNSPing(UserConnection toUc, String fromUserid, String toUserid, String api, int badge, String ip, String typecall) {

        JSONObject jo = new JSONObject();
        jo.put(ParamKey.FROM_USER_ID, fromUserid);
        jo.put(ParamKey.TOUSERID, toUserid);
        jo.put(ParamKey.API_NAME, api);
//        jo.put(ParamKey.BADGE, badge + 1);
        jo.put(ParamKey.BADGE, badge);
        jo.put(ParamKey.IP, ip);

        jo.put("typecall", typecall);

        String msg = jo.toJSONString();

        Util.sendRequest(msg, Config.JPNSServer, Config.JPNSPort);

    }

    private ChatPayment payChat(String api, Message msg, int type) {

        JSONObject jo = new JSONObject();
        jo.put(ParamKey.USER_ID, msg.from);
        jo.put(ParamKey.PARTNER_ID, msg.to);
        jo.put(ParamKey.API_NAME, api);
        jo.put(ParamKey.IP, msg.ip);
        jo.put(ParamKey.TYPE, type);
        jo.put(ParamKey.TIME, msg.serverTime);
        jo.put(ParamKey.MESSAGE_ID, msg.id);

        String request = jo.toJSONString();
        String respond = Util.sendRequest(request, Config.UMSServer_IP, Config.UMSServer_Port);
        return new ChatPayment(respond);

    }

    private void sendMainServer(String api, Message msg, boolean isCall) {

        JSONObject jo = new JSONObject();
        jo.put(ParamKey.FROM_USER_ID, msg.from);
        jo.put(ParamKey.TOUSERID, msg.to);
        jo.put(ParamKey.API_NAME, api);
        jo.put(ParamKey.IP, msg.ip);
        jo.put(ParamKey.TIME, msg.serverTime);
        if (isCall) {
            jo.put(ParamKey.MESSAGE_ID, msg.id);
            jo.put(ParamKey.MSG_TYPE, msg.msgType.toString());
            jo.put(ParamKey.CONTENT, msg.value);
        }

        String request = jo.toJSONString();
        Util.sendRequest(request, Config.MainServer_IP, Config.MainServer_Port);

    }

    private void sendUMSServer(String api, Message msg) {

//        if (msg.value.contains("&") && msg.value.contains("|")) {
        if (msg.value != null) {
            JSONObject jo = new JSONObject();
            jo.put("from", msg.from);
            jo.put("to", msg.to);
            jo.put(ParamKey.API_NAME, api);
            jo.put(ParamKey.IP, msg.ip);
            jo.put(ParamKey.TIME, msg.serverTime);
//            jo.put(ParamKey.IMAGE_ID, msg.listImgId);

            String request = jo.toJSONString();
            Util.sendRequest(request, Config.UMSServer_IP, Config.UMSServer_Port);
        }

    }

    private void proccessDeletingConnection(UserConnection uc) {

        if (uc == null) {
            return;
        }

//        Filer.saveLastChats( uc.user.lastChats, uc.user.username );
//        Filer.saveReadTime( uc.user.readTimes, uc.user.username );
        uc.destroy();

//        sendOfflineMessaegToFriendsAndAccquaintance(uc);
    }

    private void processReadTime(UserConnection uc, Message msg) {
        try {
            if (uc.user.readTimes == null) {

                return;
            }
            uc.user.readTimes.put(msg.to, Util.currentTime());

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private void updateReadTime(UserConnection uc, Message msg) {
        if (msg == null || msg.to == null || msg.to.isEmpty()) {
            return;
        }
        uc.user.readTimes.put(msg.to, Util.currentTime());
        uc.user.removeUnreadMessage(msg.to);
   
    }

    private void processFileMessage(UserConnection uc, Message msg, UserConnection toUser) {
        Util.addDebugLog("File==============================" + msg.toString());
        Util.addDebugLog("uc==============================" + uc.user.username);

//        if (msg.msgType == MessageType.FILE ) {
//            if (!Core.getDAO().isExistFileMessage(msg)) {
//                Util.addDebugLog("Not Save DB");
//                return;
//            }
//            // check message No.1 is exist or not, if message no.1 is not exist, return
//        }
        if (toUser != null) {
            toUser.user.inbox.add(msg);
        } else {
//                toUser = new UserConnection(msg.to);
//                toUser.user.putLastChat(msg.from, msg);
            User toU = User.getInstance(msg.to);
            toU.putLastChat(msg.from, msg);
//                Filer.saveLastChats(toUser.user.lastChats, toUser.user.username);

            /* Because upload successed, request UMS to remove failed_upload_point in point_exchanged_by_chat collection */
//                JSONObject jo = new JSONObject();
//                jo.put(ParamKey.API_NAME, API.REMOVE_SUCCESS_UPLOAD_POINT_DOCUMENT);
//                jo.put(ParamKey.MESSAGE_ID, msg.id);
//
//                String request = jo.toJSONString();
//                Util.sendRequest(request, Config.UMSServer_IP, Config.UMSServer_Port);
        }
//        if (msg.value.contains("&")) {
        uc.user.putLastChat(msg.to, msg);
//        }
        updateReadTime(uc, msg);

        String api = "noti_new_chat_msg_photo";
//        if (msg.value.contains("|p|")) {
//            api = "noti_new_chat_msg_photo";
//        }
//        if (msg.value.contains("|a|")) {
//            api = "noti_new_chat_msg_audio";
//        }
//        if (msg.value.contains("|v|")) {
//            api = "noti_new_chat_msg_video";
//        }
        UserInfo ui = getUserInfo(msg.to);
        if(toUser == null){
            if (ui != null && ui.deviceType == 0) {
                if ((toUser == null || toUser.webSocKet == null)) {
                    /* if( toUser == null || toUser.user == null ) */
                    int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
                    sendJPNS(toUser, msg.from, msg.to, api, unreadNum, msg.ip,msg.avaUrlFrom);
                }
            } else {
                int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
                sendJPNS(toUser, msg.from, msg.to, api, unreadNum, msg.ip,msg.avaUrlFrom);
            }
        }
    }
    
    private void processTagMessage(UserConnection uc, Message msg, UserConnection toUser) {
        String api = API.NOTI_SHARE_MUSIC;

        UserInfo ui = getUserInfo(msg.to);
        if (ui != null && ui.deviceType == 0) {
            if ((toUser == null || toUser.webSocKet == null)) {
                sendJPNSTagBuzz(toUser, msg.from, msg.to, api,msg.buzzId,msg.userNameFrom);
            }
        } else {
            int unreadNum = UnreadMessageManager.getAllUnreadMessage(msg.to);
            sendJPNSTagBuzz(toUser, msg.from, msg.to, api,msg.buzzId,msg.userNameFrom);
        }

    }

    private UserInfo getUserInfo(String id) {
        UserInfo ui = new UserInfo();
        try {
            ui = UserDAO.getUserInfor(id);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return ui;
    }

//    private void processVoiceAndVideo(UserConnection uc, Message msg, UserConnection toUser) {
//        User toU;
//        if (toUser == null || toUser.soc == null) {
//            toU = User.getInstance(msg.to);
//        } else {
//            toUser.user.inbox.add(msg);
//            toU = toUser.user;
////            if(msg.msgType == MessageType.EVIDEO || msg.msgType == MessageType.EVOICE){
////                toUser.user.putLastChat(msg.from, msg);
////            }
//        }
////        Message message = null;
//        if (msg.msgType == MessageType.EVIDEO || msg.msgType == MessageType.EVOICE) {
////            Util.addDebugLog("debug call : " + msg.value);
//            toU.putLastChat(msg.from, msg);
//        }
////        else{
////            String answer = Constant.CALL_ANSWER_VALUE.VIDEO_CALL_REFUSE;
////            if(msg.msgType == MessageType.SVOICE){
////                answer = Constant.CALL_ANSWER_VALUE.VOICE_CALL_REFUSE;
////            }
////            String value = answer + "|" + msg.id + "|" + "0";
////            message = new Message(msg.id, msg.from, msg.to, msg.msgType, value, msg.originTime, Util.currentTime(), msg.destinationTime);
////            toU.putLastChat(msg.from, message);
////            toU.increaseUnreadMessage(msg.from);
////        }
//        if (msg.msgType == MessageType.EVIDEO || msg.msgType == MessageType.EVOICE) {
//            uc.user.putLastChat(msg.to, msg);
//            updateReadTime(uc, msg);
//            Core.getLogger().log(msg);
//        }
////        else{
////            uc.user.putLastChat(msg.to, message);
////            updateReadTime(uc, message);
////            Core.getLogger().log(message);
////        }
//    }
    //change HUNGDT 
    private void processVoiceAndVideo(UserConnection uc, Message msg, UserConnection toUser) {
        User toU;
        if (toUser == null || toUser.webSocKet == null) {
            toU = User.getInstance(msg.to);
        } else {
            toU = toUser.user;
            if (msg.msgType == MessageType.EVIDEO || msg.msgType == MessageType.EVOICE) {
                toUser.user.inbox.add(msg);
            }
        }
        updateReadTime(uc, msg);

//        Message message = null;
//        if(msg.msgType == MessageType.EVIDEO || msg.msgType == MessageType.EVOICE){
//            toU.putLastChat(msg.from, msg);
//        }
//        else{
//            String answer = Constant.CALL_ANSWER_VALUE.VIDEO_CALL_REFUSE;
//            if(msg.msgType == MessageType.SVOICE){
//                answer = Constant.CALL_ANSWER_VALUE.VOICE_CALL_REFUSE;
//            }
//            String value = answer + "|" + msg.id + "|" + "0";
//            message = new Message(msg.id, msg.from, msg.to, msg.msgType, value, msg.originTime, Util.currentTime(), msg.destinationTime);
//            toU.putLastChat(msg.from, message);
//            toU.increaseUnreadMessage(msg.from);
//        }
//        if(msg.msgType == MessageType.EVIDEO || msg.msgType == MessageType.EVOICE){
//            uc.user.putLastChat(msg.to, msg);
//            updateReadTime(uc, msg);
//            Core.getLogger().log(msg);
//        }
//        else{
//            uc.user.putLastChat(msg.to, message);
//            updateReadTime(uc, message);
//            Core.getLogger().log(message);
//        }
    }

    public static void addInteraction(String userId, String friendId) {

        boolean isExistUserInteractionList = UserInteractionDAO.checkExistInteractionList(userId);
        boolean isExistUserInteraction = UserInteractionDAO.checkExistInteraction(userId, friendId);

        boolean isExistFriendInteractionList = UserInteractionDAO.checkExistInteractionList(friendId);
        boolean isExistFriendInteraction = UserInteractionDAO.checkExistInteraction(friendId, userId);

        if (isExistUserInteractionList && !isExistUserInteraction) {
            UserInteractionDAO.updateInteraction(userId, friendId);
        } else if (!isExistUserInteractionList) {
            UserInteractionDAO.addInteraction(userId, friendId);
        }

        if (isExistFriendInteractionList && !isExistFriendInteraction) {
            UserInteractionDAO.updateInteraction(friendId, userId);
        } else if (!isExistFriendInteractionList) {
            UserInteractionDAO.addInteraction(friendId, userId);
        }
    }

    private class ChatPayment {

        public int code = ErrorCode.UNKNOWN_ERROR;
        public int point = 0;

        public ChatPayment(String str) {
            try {
                if (str != null) {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(str);
                    Long returnCode = (Long) json.get(ParamKey.ERROR_CODE);
                    code = returnCode.intValue();
                    JSONObject data = (JSONObject) json.get(ParamKey.DATA);
                    Long pnt = (Long) data.get(ParamKey.POINT);
                    if (pnt == null) {
                        this.point = 0;
                    } else {
                        this.point = pnt.intValue();
                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
        }

        public ChatPayment(int code, int point) {
            this.code = code;
            this.point = point;
        }

        @Override
        public String toString() {
            return "ChatPayment{" + "code=" + code + ", point=" + point + '}';
        }

    }

}
