/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.dao.impl.LastChatDAO;

/**
 *
 * @author Rua
 */
public class LastMessageManager {

    private static final ConcurrentHashMap<String, HashMap<String, String>> lastMessageMap = new ConcurrentHashMap<>();

    public static void init(){
        HashMap<String, HashMap<String, String>> map = LastChatDAO.getAll();
        lastMessageMap.putAll(map);
    }
    
    public static HashMap<String, String> getHashMapLastMessage(String userId){
        return lastMessageMap.get(userId);
    }
    
    public static Collection<String> getCollectionLastMessage(String userId){
        HashMap<String, String> map = lastMessageMap.get(userId);
        if(map != null)
            return map.keySet();
        return new HashSet<>();
    }
    

    public static void update(String userId, String friendId, Message msg){
        HashMap<String, String> map = lastMessageMap.get(userId);
        if(map == null){
            map = new HashMap<>();
        }
        map.put(friendId, msg.id);
        lastMessageMap.put(userId, map);
                
    }
    
    public static void remove(String userId, String friendId){
        HashMap<String, String> map = lastMessageMap.get(userId);
        if(map != null)
            map.remove(friendId);
    }
}
