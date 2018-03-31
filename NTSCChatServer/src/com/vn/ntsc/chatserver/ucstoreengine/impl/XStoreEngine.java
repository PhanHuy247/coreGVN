/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.ucstoreengine.impl;

import com.vn.ntsc.Config;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import eazycommon.util.Util;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;
import com.vn.ntsc.chatserver.ucstoreengine.IStoreEngine;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Administrator
 */
public class XStoreEngine implements IStoreEngine {

    private static final ConcurrentHashMap<String, ArrayList<UserConnection>> conMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ArrayList<String>> conMapBuzz = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ArrayList<String>> conMapBuzzNoti = new ConcurrentHashMap<>();
//    private static final ConcurrentHashMap<String, UserConnection> conMap = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<UserConnection> queue = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<UserConnection> queueSessionRemove = new ConcurrentLinkedQueue<>();
        
    @Override
    public synchronized void put( UserConnection uc ){
        queue.add( uc );
    }
    
    @Override
    public void add( UserConnection uc ) {
        //a connection started to work from here.
        String userName = uc.user.username;
        if( !conMap.containsKey(userName) ) {
            conMap.put( userName, new ArrayList<UserConnection>() );
        }

        boolean isQueueContainUC = queue.contains(uc);
        boolean isMapContainUC = conMap.get(userName).contains(uc);
        for(UserConnection userConnection : conMap.get(userName)){
            if(uc.user.token.equals(userConnection.user.token)){
               
               if(uc.user.applicationType != null && uc.user.applicationType != Constant.APPLICATION_TYPE.WEB_APPLICATION){
                   Util.addDebugLog("====================Remove session equal token");
                   queueSessionRemove.add(userConnection);
               }
            }
//            if(uc.webSocKet.equals(userConnection.webSocKet)){
//               websocketClose(userConnection.webSocKet);
//               break;
//            }
        }
        if(!isQueueContainUC && !isMapContainUC ){
            conMap.get( userName ).add( uc );
            queue.add( uc );
        }
    }
    
    /**
     * Actually it works like a poll()
     * @return 
     */
    @Override
    public synchronized UserConnection poll() {
        return queue.poll();
    }

    @Override
    public UserConnection get( String username ) {
        Util.addDebugLog("CHECK STORED USER CONNECTION");   
        
        //check all websocket and remove websocket not exist
        for(String key : conMap.keySet()){
            Util.addDebugLog("STORED USER " + key);
            Util.addDebugLog("NUMBER CONNECTION " + conMap.get(key).size());
            if(conMap.get(key).size() > 0 ){
                for(UserConnection userConnection : conMap.get(key)){
                    if(userConnection == null){
                        Util.addDebugLog("USER CONNECTION WAS NULL");
                        continue;
                    }
                    if(userConnection.webSocKet == null ){
                        remove(userConnection);
                        Util.addDebugLog("USER CONNECTION HAVE WEBSOCKET WAS NULL");
                        break;
                    }
                    if(!userConnection.webSocKet.isOpen()){
                        remove(userConnection);
                        Util.addDebugLog("USER CONNECTION HAVE WEBSOCKET WAS CLOSE");
                        break;
                    }
                }
                
            }
        }
        
        ArrayList<UserConnection> ar = conMap.get( username );
        if( ar != null && !ar.isEmpty() ){
            UserConnection result = ar.get(0);
            for(UserConnection uc : ar){
                if(uc.user.lastChatTime > result.user.lastChatTime){
                    result = uc;
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public List<UserConnection> gets( String username ) {
        return conMap.get( username );
    }

    @Override
    public synchronized boolean remove( UserConnection uc ) {
        if( uc == null ) return false;
        try {
            Util.addInfoLog("Socket UserId " + uc.user.username + " close remove");
            if(!conMap.get(uc.user.username).contains(uc)){
                int indexUserConnection = 0;
                for(UserConnection userConnection : conMap.get(uc.user.username)){
                    if(userConnection.webSocKet.equals(uc.webSocKet)){
//                        conMap.get( uc.user.username ).remove(userConnection);
                        break;
                    }
                    indexUserConnection ++ ;
                }
                if(indexUserConnection < conMap.get(uc.user.username).size()){
                    conMap.get( uc.user.username ).remove(indexUserConnection);
                }
            }else{
                conMap.get( uc.user.username ).remove(uc);
            }
            if(conMap.get(uc.user.username).size() == 0){
                for(String buzzId : conMapBuzz.keySet()){
                    if(conMapBuzz.get(buzzId).contains(uc.user.username)){
                        conMapBuzz.get(buzzId).remove(uc.user.username);
                    }
                }
            }
            if(uc.webSocKet.isOpen())
                uc.webSocKet.close();
            return true;
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return false;
    }
    
    @Override
    public String size(){
        return "Queue size : " + queue.size() + ", Connection Map size: " + conMap.size();
    }

    @Override
    public void add(String buzzId,String userId) {
        if(!conMapBuzz.containsKey(buzzId)){
            conMapBuzz.put(buzzId,new ArrayList<String>());
        }
        if(!conMapBuzz.get(buzzId).contains(userId)){
            conMapBuzz.get(buzzId).add(userId);
        }
    }
    
    @Override
    public void addUserNotiWebsocket(String buzzId,String userId) {
        if(!conMapBuzzNoti.containsKey(buzzId)){
            conMapBuzzNoti.put(buzzId,new ArrayList<String>());
        }
        if(!conMapBuzzNoti.get(buzzId).contains(userId)){
            conMapBuzzNoti.get(buzzId).add(userId);
        }
    }

    @Override
    public List<String> getListUserTo(String buzzId) {
        return conMapBuzz.get(buzzId);
    }

    @Override
    public synchronized boolean remove(String buzzId,String userId) {
        boolean result = false;
        if(buzzId == null || userId == null) return result;
        if(conMapBuzz.containsKey(buzzId) && conMapBuzz.get(buzzId).contains(userId)){
            conMapBuzz.get(buzzId).remove(userId);
            result = true;
        }
        return result;
    }

    @Override
    public void websocketClose(Session session) {
      
        Iterator iteratorUser = queue.iterator();
        while(iteratorUser.hasNext()){
            UserConnection user = (UserConnection)iteratorUser.next();
            if(user.webSocKet.equals(session)){
                try {
                    user.webSocKet.close();
                    if(session.isOpen()){
                        session.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(XStoreEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public String websocketSate() {
        JSONObject jsonObj = new JSONObject();
        
        JSONObject jsonQueue = new JSONObject();
        JSONArray arrayQueue = new JSONArray();
        jsonQueue.put("size_user_connection_queue", queue.size());
        for(UserConnection user : queue){
            JSONObject jsonQueueUserConnection = new JSONObject();
            jsonQueueUserConnection.put("user_id", user.user.username);
            jsonQueueUserConnection.put("token", user.user.token);
            arrayQueue.add(jsonQueueUserConnection);
        }
        jsonQueue.put("info", arrayQueue);
        
        jsonObj.put("queue", jsonQueue);
        
        JSONObject jsonMap = new JSONObject();
        JSONArray arrayMapUserConnection = new JSONArray();
        jsonMap.put("size_user_connection_map", conMap.size());
        for(String user : conMap.keySet()){
            JSONObject jsonMapUserName = new JSONObject();
            jsonMapUserName.put("user_id", user);
            JSONArray arrayUserConnect = new JSONArray();
            for(UserConnection userConnect : conMap.get(user)){
                JSONObject jsonMapUserConnection = new JSONObject();
                jsonMapUserConnection.put("user_id", userConnect.user.username);
                jsonMapUserConnection.put("token", userConnect.user.token);
                arrayUserConnect.add(jsonMapUserConnection);
            }
            jsonMapUserName.put("info", arrayUserConnect);
            arrayMapUserConnection.add(jsonMapUserName);
        }
        jsonMap.put("info", arrayMapUserConnection);
        
        jsonObj.put("mapUserConnection", jsonMap);
        
        JSONObject jsonBuzz = new JSONObject();
        JSONArray arrayBuzz = new JSONArray();
        jsonBuzz.put("size_buzz", conMapBuzz.size());
        for(String buzz : conMapBuzz.keySet()){
            JSONObject jsonBuzzUserName = new JSONObject();
            jsonBuzzUserName.put("buzz_id", buzz);
            JSONArray arrayBuzzUserID = new JSONArray();
            for(String userConnect : conMapBuzz.get(buzz)){
                JSONObject jsonMapBuzz = new JSONObject();
                jsonMapBuzz.put("user_id", userConnect);
                arrayBuzzUserID.add(userConnect);
            }
            jsonBuzzUserName.put("user_id", arrayBuzzUserID);
            arrayBuzz.add(jsonBuzzUserName);
        }
        jsonBuzz.put("info", arrayBuzz);
        
        jsonObj.put("mapBuzz", jsonBuzz);
        
        JSONObject jsonQueueRemove = new JSONObject();
        JSONArray arrayQueueRemove = new JSONArray();
        jsonQueueRemove.put("size_user_remove_connection_queue", queueSessionRemove.size());
        for(UserConnection user : queueSessionRemove){
            JSONObject jsonQueueUserConnectionRemove = new JSONObject();
            jsonQueueUserConnectionRemove.put("user_id", user.user.username);
            jsonQueueUserConnectionRemove.put("token", user.user.token);
            arrayQueueRemove.add(jsonQueueUserConnectionRemove);
        }
        jsonQueueRemove.put("info", arrayQueueRemove);
        
        jsonObj.put("queue", jsonQueueRemove);
        
        return jsonObj.toJSONString();
    }
    
    @Override
    public void expireToken(){
        for(String user : conMap.keySet()){
            for(UserConnection userConnect : conMap.get(user)){
//                Message msgPing = new Message( Config.ServerName, user, MessageType.PING, Util.currentTime() );
                Message msg = new Message();
//                MessageIO.sendMessageWebSocket(userConnect, msgPing);
                msg.token = userConnect.user.token;
                msg.api = API.EXPIRE_TOKEN;
                String code = Util.sendRequest(msg.toJsonObject().toJSONString(), Config.MainServer_IP, Config.MainServer_Port);
                if(code == null) continue;
                Util.addDebugLog("code Token====================================="+code);
                JSONObject codeToken = null;
                try {
                    codeToken = (JSONObject) new JSONParser().parse(code);
                } catch (ParseException ex) {
                    Util.addDebugLog("error parse code token=============================" + ex.getMessage());
                }
                Long tokenCode = (Long) codeToken.get("code");
                if (tokenCode != null && tokenCode == 3L) {
                    try {
                        Util.addDebugLog("code Token====================================="+tokenCode);
                        userConnect.webSocKet.close();
                    } catch (IOException ex) {
                        Util.addDebugLog("error parse code token websocket close=============================" + ex.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void removeSessionSameToken(UserConnection user) {
        if(queueSessionRemove.isEmpty() || user == null || user.webSocKet == null) return;
        UserConnection userRemove = queueSessionRemove.poll();
        if(userRemove == null || userRemove.webSocKet == null) return;
        if(user.webSocKet.equals(userRemove.webSocKet)){
            try {
                user.webSocKet.close();
            } catch (IOException ex) {
                Logger.getLogger(XStoreEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            queueSessionRemove.add(userRemove);
        }
    }

    @Override
    public boolean removeSessionTokenFromClient(String userId, String token) {
        for(UserConnection userConnection : conMap.get(userId)){
            if(userConnection.user.token.equals(token)){
                queueSessionRemove.add(userConnection);
            }
        }
        return true;
    }

}
