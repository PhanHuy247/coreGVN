/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.blacklist;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;

/**
 *
 * @author RuAc0n
 */
public class DeactivateUserManager {

//    private static final ConcurrentLinkedQueue<String> deactivateUsers = new ConcurrentLinkedQueue<>();
    private static final ConcurrentHashMap<String, String> deactivateUsers = new ConcurrentHashMap<>();

    public static void init() {
        try {

            List<String> list = UserDAO.getDeactivate();
            for(String id : list){
                deactivateUsers.put(id, id);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static void add(String userId) {
//        deactivateUsers.add(userId);
        deactivateUsers.put(userId, userId);
    }

    public static void remove(String userId) {
        deactivateUsers.remove(userId);
    }

    public static boolean isDeactivateUser(String userId) {
        return deactivateUsers.containsKey(userId);
    }

    public static Collection<String> toList() {
        return deactivateUsers.values();
    }

}
