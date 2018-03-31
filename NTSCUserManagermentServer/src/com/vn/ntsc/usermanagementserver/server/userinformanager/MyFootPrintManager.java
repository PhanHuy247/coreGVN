/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.userinformanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.vn.ntsc.usermanagementserver.dao.impl.MyFootPrintDAO;

/**
 *
 * @author Rua
 */
public class MyFootPrintManager {

    private static final ConcurrentHashMap<String, HashMap<String, Long>> myFootprintMap = new ConcurrentHashMap<>();

    public static void init(){
        HashMap<String, HashMap<String, Long>> map = MyFootPrintDAO.getAll();
        myFootprintMap.putAll(map);
    }
    
    public static HashMap<String, Long> getMapCheckout(String userId){
        return myFootprintMap.get(userId);
    }
    public static Set<String> getMyFootprintCollections(String userId){
        Set<String> set = new HashSet<>();
        HashMap<String, Long> map = myFootprintMap.get(userId);
        if(map != null){
            for(Map.Entry<String, Long> pairs : map.entrySet()){
                String friendId = pairs.getKey();
                set.add(friendId);
            }
        }
        return set;
    }
  
//    public static Set<String> getCollectionNumberByTime(String userId, long time){
//        Set<String> set = new HashSet<>();
//        HashMap<String, Long> map = myFootprintMap.get(userId);
//        if(map != null){
//            for(Map.Entry<String, Long> pairs : map.entrySet()){
//                Long value = pairs.getValue();
//                if(value > time){
//                    String friendId = pairs.getKey();
//                    set.add(friendId);
//                }
//            }
//        }
//        return set;
//    }
    
    public static void update(String userId, String friendId, long time){
        if (userId == null || userId.isEmpty()){
            return;
        }
        HashMap<String, Long> map = myFootprintMap.get(userId);
        if(map == null){
            map = new HashMap<>();
        }
        map.put(friendId, time);
        myFootprintMap.put(userId, map);
                
    }
    
}
