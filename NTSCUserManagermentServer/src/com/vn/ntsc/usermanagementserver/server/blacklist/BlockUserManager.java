/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.blacklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;

/**
 *
 * @author RuAc0n
 */
public class BlockUserManager {

    private static final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> blockUsers = new ConcurrentHashMap<>();

    public static void init() {
        try {

            HashMap<String, ConcurrentLinkedQueue<String>> map = BlockDAO.getAll();
            blockUsers.putAll(map);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static void add(String userId, String blockUser) {
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if(blockList == null){
            blockList = new ConcurrentLinkedQueue<>();
            blockList.add(blockUser);
        }else{
            blockList.add(blockUser);
        }
        blockUsers.put(userId, blockList);
    }

    public static void remove(String userId, String blockUser) {
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if(blockList != null){
            blockList.remove(blockUser);
        }
    }

    public static List<String> getBlackList(String userId){
        List<String> blackList = new ArrayList<>();
        if(blockUsers.size() > 0){
            ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
            if(blockList != null){
                blackList.addAll(blockList);
            }
        }
        return blackList;
    }
    
    public static boolean isBlock(String userId, String friendId) {
        if (userId == null){
            return true;
        }
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if(blockList != null){
            return blockList.contains(friendId);
        }
        return false;
    }
    
    public static HashMap<String, String> getBlockList(String userId){
        HashMap<String, String> result = new HashMap<>();
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if(blockList != null){
            for(String id : blockList){
                result.put(id, id);
            }
        }
        return result;
    }

}
