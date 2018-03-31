/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import eazycommon.util.Util;
import com.vn.ntsc.jpns.dao.impl.BlockDAO;

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

    public static List<String> getBlackList(String userId){
        List<String> blackList = new ArrayList<>();
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if(blockList != null){
            blackList.addAll(blockList);
        }
        return blackList;
    }

}
