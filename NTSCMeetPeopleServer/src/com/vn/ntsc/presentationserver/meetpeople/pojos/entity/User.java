/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople.pojos.entity;

import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import org.json.simple.JSONObject;
import com.vn.ntsc.presentationserver.meetpeople.pojos.Constants;

public class User {

    public int showme;
    public int age;
    public double lon;
    public double lat;
    public Double distance;
    public boolean isOnline;
    public String email;
    public String username;
    public String avatarId;
    public long lastOnline;
    public long registerTime;
    public int location;
    public String lastLogin;
    public int callWaiting;
    public boolean isVideoCallWating;
    public boolean isVoicecallWaiting;
    public int body_type;
    public int is_avatar;

    public User() {
        showme = 0;
        age = 0;
        lon = 0;
        lat = 0;
        isOnline = false;
        email = null;
        username = null;
        lastOnline = 0;
        registerTime = 0;
        location = 0;
        callWaiting = 1;
        lastLogin = null;
        isVideoCallWating = true;
        isVoicecallWaiting = true;
        body_type = -1;
        is_avatar = -1;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return this.email.equals(user.email);
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        jo.put(ParamKey.GENDER, showme);
        jo.put("age", age);
        jo.put("body_type", body_type);
        jo.put("is_avatar", is_avatar);
        jo.put(ParamKey.LONGITUDE, lon);
        jo.put(ParamKey.LATITUDE, lat);
        jo.put(ParamKey.REGION, location);
        jo.put(ParamKey.LAST_LOGIN, lastLogin);
        jo.put("call_waiting", callWaiting);
        if (distance != null) {
            jo.put(ParamKey.DIST, distance);
        }
        jo.put(ParamKey.IS_ONLINE, isOnline);
        if (email != null) {
            jo.put(ParamKey.USER_ID, email);
        }
        if (username != null) {
            jo.put(ParamKey.USER_NAME, Util.replaceBannedWord(username));
        }
        if (avatarId != null) {
            jo.put(ParamKey.AVATAR_ID, avatarId);
        }
        jo.put(ParamKey.VIDEO_CALL_WAITING, isVideoCallWating);
        jo.put(ParamKey.VOICE_CALL_WAITING, isVoicecallWaiting);
        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

    public int calCollection() {
        int res = showme * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + location;
        return res;
    }

    public User(int showme, int callWaiting, int age, long registerTime, double lon, double lat,
            boolean isOnline, String email, String userId, String avatarID, int location, boolean isVideo, boolean isVoice, long lastOnline, int body_type) {
        this.showme = showme;
        this.age = age;
        this.lon = lon;
        this.lat = lat;
        this.isOnline = isOnline;
        this.email = email;
        this.username = userId;
        this.avatarId = avatarID;
        this.lastOnline = lastOnline;
        this.location = location;
        this.callWaiting = callWaiting;
        this.registerTime = registerTime;
        this.isVideoCallWating = isVideo;
        this.isVoicecallWaiting = isVoice;
        this.body_type = body_type;
        this.is_avatar = is_avatar;
    }

    public User(User user, double lon, double lat) {
        this.showme = user.showme;
        this.age = user.age;
        this.lon = lon;
        this.lat = lat;
        this.isOnline = user.isOnline;
        this.email = user.email;
        this.username = user.username;
        this.avatarId = user.avatarId;
        this.lastOnline = user.lastOnline;
        this.location = user.location;
        this.registerTime = user.registerTime;
        this.callWaiting = user.callWaiting;
        this.isVideoCallWating = user.isVideoCallWating;
        this.isVoicecallWaiting = user.isVoicecallWaiting;
        this.body_type = user.body_type;
        this.is_avatar = user.is_avatar;
    }

    public static double sqr(double x) {
        return x * x;
    }

    public static double calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {
        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);
        double a = sqr(Math.sin(latDistance / 2))
                + (Math.cos(Math.toRadians(userLat)))
                * (Math.cos(Math.toRadians(venueLat)))
                * sqr(Math.sin(lngDistance / 2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(Constant.R_EARTH * c);
    }

    public User getInfor(double ulon, double ulat, long lastOnline) {
        User user = new User(showme, callWaiting, age, registerTime, lon, lat, isOnline, email, username, avatarId, location, isVideoCallWating, isVoicecallWaiting, lastOnline, body_type);
        user.distance = calculateDistance(user.lat, user.lon, ulat, ulon);
        // double R = Constant.fakeRadius / Constant.R_earth * Constant.PI_degree / Constant.PI;
        int intLon, intLat;

        intLon = (int) (user.lon * 100);
        intLat = (int) (user.lat * 100);

        user.lon = (intLon) * 1.0 / 100.0;
        user.lat = (intLat) * 1.0 / 100.0;

        user.lastLogin = DateFormat.format(new Date(user.lastOnline));

        return user;
    }

    @Override
    public String toString() {
        String str = "username = " + this.username
                + ", user_id = " + this.email
                + ", isOnline = " + this.isOnline
                + ", gender = " + this.showme
                + ", callWaiting = " + this.callWaiting
                + ", age = " + this.age
                + ", registerTime = " + this.registerTime
                + ", long = " + this.lon
                + ", lat = " + this.lat
                + ", dist = " + this.distance
                + ", location = " + this.location;

        return str;
    }

    public static class SortByTime implements Comparator<User> {

        @Override
        public int compare(User u1, User u2) {
            if (u1.lastOnline == u2.lastOnline) {
                return u1.email.compareTo(u2.email);
            }
            if (u1.lastOnline > u2.lastOnline) {
                return -1;
            }
            return 1;
        }

    }

    public static class SortByRegisterTime implements Comparator<User> {

        @Override
        public int compare(User u1, User u2) {
            if (u1.registerTime == u2.registerTime) {
                return u1.email.compareTo(u2.email);
            }
            if (u1.registerTime > u2.registerTime) {
                return -1;
            }
            return 1;
        }

    }

    public class SortByAgeAndDistance implements Comparator<User> {

        double lonCompare, latCompare;

        public SortByAgeAndDistance() {
        }

        public SortByAgeAndDistance(double lonCompare, double latCompare) {
            this.lonCompare = lonCompare;
            this.latCompare = latCompare;
        }

        @Override
        public int compare(User u1, User u2) {
            if (u1.age == u2.age) {
                double t1 = Math.abs(u1.lon - lonCompare) + Math.abs(u1.lat - latCompare);
                double t2 = Math.abs(u2.lon - lonCompare) + Math.abs(u2.lat - latCompare);
                if (t1 == t2) {
                    return u1.email.compareTo(u2.email);
                }
                if (t1 < t2) {
                    return -1;
                }
                return 1;
            }
            return u1.age - u2.age;
        }

    }

    public class SortByAgeAndTime implements Comparator<User> {

        @Override
        public int compare(User u1, User u2) {
            if (u1.age == u2.age) {
                if (u1.lastOnline == u2.lastOnline) {
                    return u1.email.compareTo(u2.email);
                }
                if (u1.lastOnline > u2.lastOnline) {
                    return -1;
                }
                return 1;
            }
            return u1.age - u2.age;
        }

    }

    public static LinkedList<User> sortByRequirement(LinkedList<User> in, Double lon, Double lat, int skip, int take, int sortType) {
        LinkedList<User> res = new LinkedList<>();
        if (sortType == Constants.SORT_BY_REGISTER_TIME) {
            res = sortByRegisterTime(in, skip, take);
        } else if (sortType == Constants.SORT_BY_LOGIN_TIME) {
            res = sortByLoginTime(in, skip, take);
        }
        LinkedList<User> RR = new LinkedList<>();
        Iterator<User> it = res.iterator();
        while (it.hasNext()) {
            try {
                User u = it.next();
                RR.add(u.getInfor(lon, lat, u.lastOnline));
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
        }

        return RR;
    }

    private static LinkedList<User> sortByRegisterTime(LinkedList<User> in, int skip, int take) {
        LinkedList<User> res = new LinkedList<>();
        synchronized (in) {
            Collections.sort(in, new SortByRegisterTime());
        }
        Iterator<User> it = in.iterator();
        User user;
        int run = 0;
        while (it.hasNext()) {
            if (run >= skip + take) {
                break;
            }
            user = it.next();
            if (run >= skip) {
                res.add(user);
            }
            run++;
        }
        return res;
    }

//    private static LinkedList<User> sortByLoginTime(LinkedList<User> in, int skip, int take) {
//        LinkedList<User> res = new LinkedList<User>();
//        List<User> lOn = new LinkedList<User>();
//        LinkedList<User> lOff = new LinkedList<User>();
//        LinkedList<User> ltemp = new LinkedList<User>();
//        Iterator<User> it = in.iterator();
//        User user;
//        while(it.hasNext()){
//            user = it.next();
//            if(user.isOnline){
//                lOn.add(user);
//            }
//            else lOff.add(user);
//        }
//        
//        Collections.sort(lOn, new SortByTime());
//                
//        if(lOn.size() < skip + take){
//            Collections.sort(lOff, new SortByTime());
//        }
//        
//        it = lOn.iterator();
//        while(it.hasNext()){
//            ltemp.add(it.next());
//        }
//        it = lOff.iterator();
//        while(it.hasNext()){
//            ltemp.add(it.next());
//        }
//        
//        it = ltemp.iterator();
//        int run = 0;
//        while(it.hasNext()){
//            if(run >= skip + take){
//                break;
//            }
//            user = it.next();
//            if(run >= skip){
//                res.add(user);
//            }
//            run++;       
//        }
//        return res;
//    }     
    private static LinkedList<User> sortByLoginTime(LinkedList<User> in, int skip, int take) {
        LinkedList<User> res = new LinkedList<>();
//        List<User> lOn = new LinkedList<>();
//        LinkedList<User> lOff = new LinkedList<>();
        LinkedList<User> ltemp = new LinkedList<>();
        Util.addDebugLog("--------------- 1: "+in.size());
        Iterator<User> it = in.iterator();
        Util.addDebugLog("--------------- 2: "+skip + "        "+ take);
        User user;
        while (it.hasNext()) {
            user = it.next();
            ltemp.add(user);
        }

        synchronized (ltemp) {
            Collections.sort(ltemp, new SortByTime());
        }

        it = ltemp.iterator();
        int run = 0;
        while (it.hasNext()) {
            if (run >= skip + take) {
                break;
            }
            user = it.next();
            if (run >= skip) {
                res.add(user);
            }
            run++;
        }
        return res;
    }
}
