/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.socketserver.websocket;

import com.vn.ntsc.Config;
import com.vn.ntsc.Core;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.messageio.MessageParser;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;
import com.vn.ntsc.chatserver.pojos.user.User;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;
import com.vn.ntsc.chatserver.ucstoreengine.impl.XStoreEngine;
import com.vn.ntsc.utils.Validator;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Administrator
 */

@ServerEndpoint(value = "/chat")
public class ServerEndPoint {
    
    @OnOpen
    public void onOpen(Session userSession) {
        Util.addDebugLog("--------- Unauthenicated session from web has connected to server...");
        UnAuthenticatedSessionPool.put(userSession);
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        Util.addDebugLog("--------- Session closed....");
        Core.getStoreEngine().websocketClose(userSession);
        userSession = null;
    }

    @OnMessage
    public void onMessage(Session userSession, String message) throws IOException {
        Util.addDebugLog("--------- Message comming from websocket : "+message);
        boolean isSession = false;
        Message msg = Message.parseFromJSONString(message);
        boolean isUnauthen = isUnauthenSession(userSession);
        if (isUnauthen){
            checkAuthenSession(msg, userSession);
        }
        else {
                if(msg.msgType == MessageType.REMOVESOCKET){
                    List<UserConnection> users = Core.getStoreEngine().gets(msg.from);
                    for (UserConnection uc : users) {
                        if (uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()) {
                            continue;
                        }
                        if (uc.webSocKet.equals(userSession)) {
                            Core.getStoreEngine().websocketClose(userSession);
                            userSession = null;
                            break;
                        }
                    }
                    return;
                }
                if (msg.msgType == MessageType.BUZZJOIN) {
                    String buzz = Util.sendRequest(msg.toJsonObject().toJSONString(), Config.MainServer_IP, Config.MainServer_Port);
                    confirmMessageSent(userSession, buzz,message);
                    return;
                }
                if (msg.msgType == MessageType.BUZZUPDATE) {
                    //join buzz for web disconnect websocket
                    joinBuzzForWebDisconnect(msg);
                    return;
                }
                if (msg.msgType == MessageType.BUZZCMT) {
                    String buzz = Util.sendRequest(msg.toJsonObject().toJSONString(), Config.MainServer_IP, Config.MainServer_Port);
                    confirmMessageSent(userSession, buzz,message);
                    return;
                }
                if (msg.msgType == MessageType.BUZZSUBCMT) {
                    String buzz = Util.sendRequest(msg.toJsonObject().toJSONString(), Config.MainServer_IP, Config.MainServer_Port);
                    confirmMessageSent(userSession, buzz,message);
                    return;
                }
                if (msg.msgType == MessageType.BUZZLEAVE) {
                    String buzz = Util.sendRequest(msg.toJsonObject().toJSONString(), Config.MainServer_IP, Config.MainServer_Port);
                    confirmMessageSent(userSession, buzz,message);
                    return;
                }
                if (msg.msgType == MessageType.BUZZTAG) {
                    String buzz = Util.sendRequest(msg.toJsonObject().toJSONString(), Config.MainServer_IP, Config.MainServer_Port);
                    confirmMessageSent(userSession, buzz,message);
                    return;
                }
                if (msg.msgType == MessageType.BUZZADDTAG) {
                    String buzz = Util.sendRequest(msg.toJsonObject().toJSONString(), Config.MainServer_IP, Config.MainServer_Port);
                    confirmMessageSent(userSession, buzz,message);
                    return;
                }
                if (msg.msgType == MessageType.BUZZDELCMT) {
                    String buzz = Util.sendRequest(msg.toJsonObject().toJSONString(), Config.MainServer_IP, Config.MainServer_Port);
                    confirmMessageSent(userSession, buzz,message);
                    return;
                }
                if (msg.msgType == MessageType.BUZZDELSUBCMT) {
                    String buzz = Util.sendRequest(msg.toJsonObject().toJSONString(), Config.MainServer_IP, Config.MainServer_Port);
                    confirmMessageSent(userSession, buzz,message);
                    return;
                }
                
                List<UserConnection> users = Core.getStoreEngine().gets(msg.from);
                for (UserConnection uc : users){
                    if(uc == null || uc.webSocKet == null || !uc.webSocKet.isOpen()){
                        continue;
                    }
                    if(uc.webSocKet.equals(userSession)){
                        Util.addDebugLog("============================ADD MESSAGE TO OUTBOX==========================");
                        isSession = true;
                        uc.user.outbox.add(msg);
                        break;
                    }
                }
                if(!isSession){
                    userSession.close();
                }
        }
    }
    
    private void confirmMessageSent(Session userSession, String buzz, String message) {
        JSONObject msg = null;
        try {
            msg = (JSONObject) new JSONParser().parse(buzz);
        } catch (ParseException ex) {
            Util.addDebugLog("error confirmMessageSent============================="+ex.getMessage());
        }
        Long code = (Long)msg.get("code");
        String newToken = (String)msg.get("new_token");
        if(code != null && code != 0L){
            try {
                msg = (JSONObject) new JSONParser().parse(message);
                msg.put("code", code);
                if(newToken != null)
                    msg.put("new_token", newToken);
                Util.addDebugLog("confirmMessageSent============================="+msg.toJSONString());
                userSession.getAsyncRemote().sendText(msg.toJSONString());
            } catch (ParseException ex) {
                Util.addDebugLog("error confirmMessageSent============================="+ex.getMessage());
            }
        }
    }
    
    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        Util.addDebugLog("--------- Session Error....");
        Core.getStoreEngine().websocketClose(session);
    }
    private boolean isUnauthenSession(Session session){
        String ssId = session.getId();
        Session ss = UnAuthenticatedSessionPool.get(ssId);
        if (ss != null){
            return true;
        }
        else {
            return false;
        }
    }
    
    private boolean checkAuthenSession(Message msg, Session session) throws IOException{
        boolean authenResult = false;
        String userId = msg.from;
        String token = msg.value;
        String authenString = authenFromToken(userId, token);

        if(msg.msgType.equals(MessageType.AUTH)){
            if(userId != null && !userId.trim().isEmpty()){
                authenResult = getAuthenResult(userId, authenString);
            }
        }else{
              Util.addInfoLog("Socket :  with User: " + msg.from + ", not authen.");                                
              Message authenNot = new Message( Config.ServerName, userId, MessageType.AUTH, MessageTypeValue.Auth_AuthenNot, Util.currentTime(),token);
              UserConnection uc = new UserConnection(session, new User(userId,token));
              MessageIO.sendMessageWebSocket(uc, authenNot);
              return authenResult;
        }
        if (authenResult){
            UnAuthenticatedSessionPool.remove(session.getId());
            Util.addDebugLog("authen success");
            Util.addDebugLog(session.getRequestURI() + " " + authenResult);
            Message authenSuccess = new Message( Config.ServerName, userId, MessageType.AUTH, MessageTypeValue.Auth_AutheSuccess, Util.currentTime(),token);
            Integer application = getApplication(authenString);
            String deviceId = getDeviceId(authenString);
            UserConnection uc = new UserConnection(session, new User(userId,token,application,deviceId));
            MessageIO.sendMessageWebSocket(uc, authenSuccess);

            Core.getStoreEngine().add( uc );
        }else{
            Util.addInfoLog("Socket with User: " + msg.from + ", authen failure.");                                
            Message authenFalse = new Message( Config.ServerName, userId, MessageType.AUTH, MessageTypeValue.Auth_AuthenFalse, Util.currentTime(),token);
            UserConnection uc = new UserConnection(session, new User(userId,token));
            MessageIO.sendMessageWebSocket(uc, authenFalse);
        }
        return authenResult;
    }
        
    private String authenFromToken( String username, String token ) {
        if( Config.IsDebug ) {
            return String.valueOf(true);
        }
        String urlStr = "http://" + Config.MainServer_IP + ":" +Config.MainServer_Port + "/checktoken=" + token;
        try {
            URL url = new URL( urlStr );
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod( "GET" );

            InputStreamReader isr = new InputStreamReader( con.getInputStream() );
            BufferedReader reader = new BufferedReader( isr );
            String line = reader.readLine();
            return line;
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return String.valueOf(false);
    }
    
    private boolean getAuthenResult(String username, String authenString){
        if( authenString != null && authenString.contains( "true" ) ) {
            return true;
        }else{
//            return Validator.isUser(username);
            return false;
        }
    }
    
     public static Integer getApplication(String inputString){
        Integer result = 0;
        String [] element = inputString.split("&");
        if(element.length >= 4){
            String []map = element[3].split("=");
            result = Integer.valueOf(map[1]);//get application from main detect web, mobile
        }
        return result;
    }
     public static String getDeviceId(String inputString){
        String result = "";
        String [] element = inputString.split("&");
        if(element.length >= 5){
            String []map = element[4].split("=");
            result = map[1];// deviceID corresponding token
        }
        return result;
    }

    private void joinBuzzForWebDisconnect(Message msg) {
        for(String buzzId : msg.listBuzzData){
            Core.getStoreEngine().add(buzzId, msg.userId);
        }
    }
}
