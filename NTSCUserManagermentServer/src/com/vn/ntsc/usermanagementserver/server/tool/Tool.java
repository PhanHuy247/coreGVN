/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.tool;

import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;

/**
 *
 * @author RuAc0n
 */
public class Tool {

    public static void active(String userId, List<String> blackList) throws EazyException {
        if (DeactivateUserManager.isDeactivateUser(userId)) {
            DeactivateUserManager.remove(userId);
            List<String> listFav = FavoristDAO.getFavouristList(userId);
            for (String id : listFav) {
                if (!blackList.contains(id)) {
                    UserDAO.addFavorited(id);
                }
            }
            List<String> listFvt = FavoritedDAO.getFavoristIdList(userId);
            for (String id : listFvt) {
                if (!blackList.contains(id)) {
                    UserDAO.addFavorist(id);
                }
            }
        }
    }

    public static void deactive(String userId, List<String> blackList) throws EazyException {
        if(!DeactivateUserManager.isDeactivateUser(userId)){
            UserActivityDAO.deactivate(userId);
            DeactivateUserManager.add(userId);

            List<String> listFav = FavoristDAO.getFavouristList(userId);
            for (String id : listFav) {
                if (!blackList.contains(id)) {
                    UserDAO.removeFavourited(id);
                }
            }
            List<String> listFvt = FavoritedDAO.getFavoristIdList(userId);
            for (String id : listFvt) {
                if (!blackList.contains(id)) {
                    UserDAO.removeFavourist(id);
                }
            }
        }
    }

//    public static void removeBlackListUser(List<User> list, String userId) throws DaoException {
//        Collection<String> deactivateUsers = BlackListManager.toList();
//        List<String> blockUsers = BlockDAO.getBlackList(userId);
//        Queue<String> removeList = new LinkedList<>();
//        for (User list1 : list) {
//            String id = list1.userId;
//            if (deactivateUsers.contains(id) || blockUsers.contains(id)) {
//                removeList.add(id);
//            }
//        }
//        if (!removeList.isEmpty()) {
//            while (!removeList.isEmpty()) {
//                String id = removeList.poll();
//                for (int i = 0; i < list.size(); i++) {
//                    String uid = (String) list.get(i).userId;
//                    if (uid.equals(id)) {
//                        list.remove(i);
//                        break;
//                    }
//                }
//            }
//        }
//    }
    
    public static void removeUser(List<User> list, String userId){
        HashMap<String, String> blockUsers = BlockUserManager.getBlockList(userId);
        List<User> removeUsers = new ArrayList<>();
        for(User user : list){
            if(DeactivateUserManager.isDeactivateUser(user.userId) || blockUsers.containsKey(user.userId) || user.userId.equals(userId)){
                removeUsers.add(user);
            }
        }
        list.removeAll(removeUsers);
    }
    
    public static void removeBlackListUser(List<User> list, String userId) throws EazyException {
//        Collection<String> deactivateUsers = BlackListManager.toList();
        HashMap<String, String> blockUsers = BlockUserManager.getBlockList(userId);
        
        List<User> removeUsers = new ArrayList<>();
//        Map<String, User> map = new HashMap<>();
        for(User user : list){
//            map.put(user.userId, user);
            if(DeactivateUserManager.isDeactivateUser(user.userId) || blockUsers.containsKey(user.userId)){
                removeUsers.add(user);
            }
        }
        
        list.removeAll(removeUsers);
        
//        for (String id : deactivateUsers) {
//            if (map.containsKey(id)) {
//                list.remove(map.get(id));
//            }
//        }

//        for (String id : blockUsers) {
//            if (map.containsKey(id)) {
//                list.remove(map.get(id));
//            }
//        }
    }

//    public static void removeBlackList(Collection<String> list, String userId) throws DaoException {
//        long getBackListStart = System.currentTimeMillis();
//        Collection<String> deactivateUsers = BlackListManager.toList();
//        List<String> blockUsers = BlockDAO.getBlackList(userId);
//        long getBackListEnd = System.currentTimeMillis();
//        getBackListEnd -= getBackListStart;
//        if(getBackListEnd > 1500){
//            Util.addDebugLog("Remove Block User Id: get back list slow");
//        }
//        
//        long addRemvoveListStart = System.currentTimeMillis();
//        Queue<String> removeList = new LinkedList<>();
//        for (String id : list) {
//            if (deactivateUsers.contains(id) || blockUsers.contains(id)) {
//                removeList.add(id);
//            }
//        }
//        long addRemvoveListEnd = System.currentTimeMillis();
//        addRemvoveListEnd -= addRemvoveListStart;
//        if(addRemvoveListEnd > 1500){
//            Util.addDebugLog("Remove Block User Id: add remove list slow");
//        }
//        
//        long removeBlackListStart = System.currentTimeMillis();
//        if (!removeList.isEmpty()) {
//            while (!removeList.isEmpty()) {
//                String id = removeList.poll();
//                list.remove(id);
//            }
//        }
//        long removeBlackListEnd = System.currentTimeMillis();
//        removeBlackListEnd -= removeBlackListStart;
//        if(removeBlackListEnd > 1500){
//            Util.addDebugLog("Remove Block User Id: remove list slow");
//        }
//        
//    }
    
    public static void removeBlackList(Collection<String> list, String userId) throws EazyException {
        long getBackListStart = System.currentTimeMillis();
        HashMap<String, String> blockUsers = BlockUserManager.getBlockList(userId);
        long getBackListEnd = System.currentTimeMillis();
        getBackListEnd -= getBackListStart;
        if(getBackListEnd > 1500){
            Util.addInfoLog("Remove Black List: get bock list slow");
        }
        
        List<String> removeList = new ArrayList<>();
        long removeListStart = System.currentTimeMillis();
        for (String id : list) {
            if (DeactivateUserManager.isDeactivateUser(id) || blockUsers.containsKey(id)) {
                removeList.add(id);
            }
        }
        
        long removeListEnd = System.currentTimeMillis();
        removeListEnd -= removeListStart;
        if(removeListEnd > 1500){
            Util.addInfoLog("Remove Black List: add remove list slow " + removeListEnd );
        }
        
//        long oneDay = 24 * 60 * 60 * 1000 * 1970;
        
        removeListStart = System.currentTimeMillis();
        list.removeAll(removeList);
        removeListEnd = System.currentTimeMillis();
        removeListEnd -= removeListStart;
        if(removeListEnd > 1500){
            Util.addInfoLog("Remove Black List: remove slow " + removeListEnd);
        }
        
    }
    
//    public static void removeBlackList(Collection<String> list, String userId) throws DaoException {
//        long getBackListStart = System.currentTimeMillis();
//        Collection<String> deactivateUsers = BlackListManager.toList();
//        List<String> blockUsers = BlockDAO.getBlackList(userId);
//        long getBackListEnd = System.currentTimeMillis();
//        getBackListEnd -= getBackListStart;
//        if(getBackListEnd > 1500){
//            Util.addDebugLog("Remove Black List: get back list slow");
//        }
//        
//        long removeDeactiveListStart = System.currentTimeMillis();
////        list.removeAll(deactivateUsers);
//        for (String id : deactivateUsers) {
////            if (list.contains(id)) {
//            list.remove(id);
////            }
//        }
//        long removeDeactiveListEnd = System.currentTimeMillis();
//        removeDeactiveListEnd -= removeDeactiveListStart;
//        if(removeDeactiveListEnd > 1500){
//            Util.addDebugLog("Remove Black List: remove deactive list slow");
//        }
//        
//        long removeBlockListStart = System.currentTimeMillis();
////        list.removeAll(blockUsers);
//        for (String id : blockUsers) {
////            if (list.contains(id)) {
//            list.remove(id);
////            }
//        }
//        long removeBlockListEnd = System.currentTimeMillis();
//        removeBlockListEnd -= removeBlockListStart;
//        if(removeBlockListEnd > 1500){
//            Util.addDebugLog("Remove Black List: remove block list slow");
//        }
//        
//    }

    public static boolean checkUserName(String userId, String username) throws EazyException {
        if (username == null) {
            return true;
        }
        return UserDAO.checkUserName(userId, username);
    }

    public static boolean checkFemaleName(String userId, String username) throws EazyException {
        if (username == null) {
            return true;
        }
        return UserDAO.checkFemaleName(userId, username);
    }
        
}
