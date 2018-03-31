/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.user;

import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import com.vn.ntsc.blacklist.BlockUserManager;
import com.vn.ntsc.dao.impl.UnreadMessageDAO;
import com.vn.ntsc.utils.Validator;

/**
 *
 * @author Rua
 */
public class UnreadMessageManager {

    private static final ConcurrentHashMap<String, HashMap<String, Integer>> unreadMessageMap = new ConcurrentHashMap<>();

    public static void init(){
        HashMap<String, HashMap<String, Integer>> map = UnreadMessageDAO.getAll();
        unreadMessageMap.putAll(map);
    }
    
    public static HashMap<String, Integer> getHashMapUnreadMessage(String username){
        return unreadMessageMap.get(username);
    }
    public static Collection<String> getCollectionUnreadMessage(String username){
        HashMap<String, Integer> map = unreadMessageMap.get(username);
        if(map != null)
            return map.keySet();
        return null;
    }
    
    public static int getAllUnreadMessage(UserConnection uc){
//        int result = 0;
        if(uc != null && uc.user != null){
            return getAllUnreadMessage(uc.user.username);
        }
        return 0;
    }
    
    public static int getAllUnreadMessage(String username){
//        int result = 0;
//        User user;
//        UserConnection uc = Core.getStoreEngine().get(username);
//        user = uc == null ? User.getInstance(username) : uc.user;
        
        // get last chat
        Collection<String> lastchat = LastMessageManager.getCollectionLastMessage(username);
//        if(uc == null){
//            Map<String, Message> mapLastChat = user.getLastChat();
//            lastchat = mapLastChat != null? mapLastChat.keySet() : user.lastChats.keySet();
//        }else{
//            lastchat = user.lastChats.keySet();
//        }
        
        // remove deactive user
//        List<String> friends = Core.getDAO().getListUserIds(lastchat);
        List<String> friends = new ArrayList<>();
        for(String userId : lastchat){
            if(Validator.isUser(userId)){
                friends.add(userId);
            }
        }
        
        //remove block user
        //        User.FFUMS.removeBlockUser(username, friends);
        List<String> blockList = BlockUserManager.getBlackList(username);
        if(blockList != null){
            friends.removeAll(blockList);
        }
        return getAllUnreadByUsers(username, friends);
    }
    
    public static int getAllUnreadByUsers(String username, List<String> friendIds){
        int result = 0;
        HashMap<String, Integer> map = unreadMessageMap.get(username);
        friendIds.remove(username);
        if(map != null && !map.isEmpty()){
            for(String friendId : friendIds){
                Integer ureadMessageNumber =  map.get(friendId);
                int unread = ureadMessageNumber != null ? ureadMessageNumber : 0;
                result += unread;
            }
        }
        return result;
    }
    
    public static int getAllUnreadByUser(String username, String friendId){
        int result = 0;
        if(!friendId.equals(username)){
            if(username != null){
                HashMap<String, Integer> map = unreadMessageMap.get(username);
                if(map != null){
                    Integer ureadMessageNumber =  map.get(friendId);
                    result = ureadMessageNumber != null ? ureadMessageNumber : 0;
                }
            }
        }
        return result;
    }
    
    public static void increase(String username, String friendId){
        HashMap<String, Integer> map = unreadMessageMap.get(username);
        if(map != null){
            Integer unreadMessageNumber = map.get(friendId);
            int value = unreadMessageNumber != null ? unreadMessageNumber + 1 : 1; 
            map.put(friendId, value);
        }else{
            map = new HashMap<>();
            map.put(friendId, 1);
            unreadMessageMap.put(username, map);
        }
                
    }
    public static void update(String username, String friendId, int unreadMessage){
        HashMap<String, Integer> map = unreadMessageMap.get(username);
        if(map == null){
            map = new HashMap<>();
        }
        map.put(friendId, unreadMessage);
        unreadMessageMap.put(username, map);
                
    }
    
    public static void remove(String username, String friendId){
        HashMap<String, Integer> map = unreadMessageMap.get(username);
        if(map != null)
            map.remove(friendId);
    }

    static void keep(String username, List<String> friendIdList) {
        HashMap<String, Integer> map = unreadMessageMap.get(username);
        if (map != null){
            map.keySet().retainAll(friendIdList);
        }
    }
}
