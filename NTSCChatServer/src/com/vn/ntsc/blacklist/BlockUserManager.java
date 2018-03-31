/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.blacklist;

import eazycommon.exception.EazyException;
import eazycommon.inspection.version.InspectionVersionDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import eazycommon.util.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.vn.ntsc.chatserver.pojos.user.User;
import com.vn.ntsc.chatserver.pojos.user.UserInfo;
import com.vn.ntsc.dao.impl.UserDAO;

/**
 *
 * @author RuAc0n
 */
public class BlockUserManager {

    private static final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> blockUsers = new ConcurrentHashMap<>();

    public static void init() {
        try {

            HashMap<String, ConcurrentLinkedQueue<String>> map = User.FFUMS.getAllBlockUser();
            blockUsers.putAll(map);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void add(String userId, String blockUser) {
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if (blockList == null) {
            blockList = new ConcurrentLinkedQueue<>();
            blockList.add(blockUser);
        } else {
            blockList.add(blockUser);
        }
        blockUsers.put(userId, blockList);
    }

    public static void remove(String userId, String blockUser) {
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if (blockList != null) {
            blockList.remove(blockUser);
        }
    }

    public static List<String> getBlackList(String userId) {
        List<String> blackList = new ArrayList<>();
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if (blockList != null) {
            blackList.addAll(blockList);
        }
        return blackList;
    }

    public static boolean isBlock(String userId, String friendId) {
//        try {
//            UserInfo u = UserDAO.getUserInfor(friendId);
//            UserInfo u2 = UserDAO.getUserInfor(userId);
//            if ((u.deviceType == 0 || u2.deviceType == 0)
//                    && ("2".equals(u.applicationId) && "2".equals(u2.applicationId))) {
//                if (isInspection(userId, friendId,u,u2)) {
//                    return true;
//                }
//            } else 
//            if ("2".equals(u.applicationId) || "2".equals(u2.applicationId)) {
//                if (isInspection(userId, friendId)) {
//                    return true;
////                }
//            }
//        } catch (EazyException ex) {
//            Logger.getLogger(BlockUserManager.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        if(isInspection(userId,friendId)){
//            return true;
//        }
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if (blockList != null) {
            return blockList.contains(friendId);
        }

        return false;
    }

    public static boolean isInspection(String userId, String friendId) {
        try {
            if (friendId == null) {
                return false;
            }
            if (friendId.equals("server")) {
                return false;
            }

            UserInfo u = UserDAO.getUserInfor(friendId);
            if (u == null) {
                return false;
            }
            if (u.deviceType == null) {
                return false;
            }
            if (u.appVersion == null) {
                return false;
            }
            if (u.deviceType != 0) {
                return false;
            }
            String safaryVersion = InspectionVersionDAO.getIOSTurnOffSafaryVersion();
            if (!u.appVersion.equals(safaryVersion)) {
                return false;
            }

            UserInfo u2 = UserDAO.getUserInfor(userId);
            if (u2 == null) {
                return false;
            }
            if (u2.deviceType == null) {
                return false;
            }
            if (u2.appVersion == null) {
                return false;
            }
            if (u2.deviceType != 0) {
                return true;
            }
            if (u.appVersion.equals(u2.appVersion)) {
                return false;
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return true;
    }

    public static boolean isInspection(String userId, String friendId, UserInfo u, UserInfo u2) {
        try {
            if (friendId == null) {
                return false;
            }
            if (friendId.equals("server")) {
                return false;
            }

//            UserInfo u = UserDAO.getUserInfor(friendId);
            if (u == null) {
                return false;
            }
            if (u.deviceType == null) {
                return false;
            }
            if (u.appVersion == null) {
                return false;
            }
            if (u.deviceType != 0) {
                return false;
            }
            String safaryVersion = InspectionVersionDAO.getIOSTurnOffSafaryVersion();
            if (!u.appVersion.equals(safaryVersion)) {
                return false;
            }

//            UserInfo u2 = UserDAO.getUserInfor(userId);
            if (u2 == null) {
                return false;
            }
            if (u2.deviceType == null) {
                return false;
            }
            if (u2.appVersion == null) {
                return false;
            }
            if (u2.deviceType != 0) {
                return true;
            }
            if (u.appVersion.equals(u2.appVersion)) {
                return false;
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return true;
    }

    public static HashMap<String, String> getBlockList(String userId) {
        HashMap<String, String> result = new HashMap<>();
        ConcurrentLinkedQueue<String> blockList = blockUsers.get(userId);
        if (blockList != null) {
            for (String id : blockList) {
                result.put(id, id);
            }
        }
        return result;
    }

}
