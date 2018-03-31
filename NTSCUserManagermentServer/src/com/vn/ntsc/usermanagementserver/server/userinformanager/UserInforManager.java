/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.userinformanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.pointaction.PointAction;
import com.vn.ntsc.usermanagementserver.server.systemaccount.SystemAccountManager;
import eazycommon.util.Util;
/**
 *
 * @author RuAc0n
 */
public class UserInforManager {

    public static final Map<String, UserInfor> userInforMap = new ConcurrentHashMap<>();

    public static void init() {
        userInforMap.putAll(UserDAO.initUserInfor());
    }

    public static void put(UserInfor up) {
        userInforMap.put(up.userId, up);
    }

    public static UserInfor get(String userId) {
        return userInforMap.get(userId);
    }

    public static void add(String userId) {
        UserInfor up = new UserInfor(userId, 0);
        put(up);
    }

    public static void add(String userId, Integer gender) {
        UserInfor up = new UserInfor(userId, 0, gender, false);
        put(up);
    }
    public static void addMultiapp(String userId, int gender, String applicationId) {
        UserInfor up = new UserInfor(userId, 0, gender, false,applicationId);
        put(up);
    }

    public static PointAction increasePoint(String userId, int point) {
        UserInfor up = get(userId);
        int beforePoint = up.point;
        int afterpoint = beforePoint;
        if (point > 0) {
            afterpoint += point;
        }
//        put(new UserPoint(userId, afterpoint));
        PointAction result = new PointAction(beforePoint, afterpoint, point);
        get(userId).point = afterpoint;
        return result;
    }

    public static PointAction decreasePoint(String userId, int point) {
        UserInfor up = get(userId);
        int curPoint = up.getPoint();

        if (point > curPoint) {
            point = curPoint;
        }

        int afterPoint = up.point - point;
//        put(new UserPoint(userId, afterPoint));
        PointAction pa = new PointAction(curPoint, afterPoint, 0 - point);
        get(userId).point = afterPoint;
        return pa;
    }

    public static int getPoint(String userId) {
        return get(userId).getPoint();
    }

    public static int getGender(String userId) {
        return get(userId).gender;
    }
    
    public static boolean havePurchased(String userId) {
        return get(userId).havePurchased;
    }
    // LongLT 8/8/2016
    public static void setHavePurchased(String userId){
        get(userId).havePurchased=true;
    }

    public static List<String> getUserByOppositeGender(int gender){
        List<String> resutl = new ArrayList<>();
        List<String> systemAccounts = SystemAccountManager.toList();
        Collection<UserInfor> collections = userInforMap.values();
        for(UserInfor userInfor : collections){
            String userId =  userInfor.userId;
            if(userInfor.gender != gender && !systemAccounts.contains(userId)){
                resutl.add(userId);
            }
        }
        return resutl;
    }

    public static List<String> getAllUser(){
        List<String> result = new ArrayList<>();
        List<String> systemAccounts = SystemAccountManager.toList();
        Collection<UserInfor> collections = userInforMap.values();
        for(UserInfor userInfor : collections){
            String userId =  userInfor.userId;
            if(!systemAccounts.contains(userId)){
                result.add(userId);
            }
        }
        return result;
    }
}
