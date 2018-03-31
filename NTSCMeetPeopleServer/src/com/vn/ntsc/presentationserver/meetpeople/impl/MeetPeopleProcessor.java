/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople.impl;

import com.vn.ntsc.presentationserver.meetpeople.pojos.Setting;
import com.vn.ntsc.presentationserver.meetpeople.pojos.Constants;
import com.vn.ntsc.presentationserver.meetpeople.pojos.Tool;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bson.types.ObjectId;
import com.vn.ntsc.presentationserver.meetpeople.IMeetPeopleProcessor;
import com.vn.ntsc.presentationserver.meetpeople.dao.DatabaseLoader;
import static com.vn.ntsc.presentationserver.meetpeople.dao.DatabaseLoader.db;
import com.vn.ntsc.presentationserver.meetpeople.dao.IDAO;
import com.vn.ntsc.presentationserver.meetpeople.dao.impl.UserDAO;
import com.vn.ntsc.presentationserver.meetpeople.dao.impl.UserInteractionDAO;
import com.vn.ntsc.presentationserver.meetpeople.dao.impl.UserSettingDAO;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.Data;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.QuerySetting;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.StatePosition;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.User;
import com.vn.ntsc.presentationserver.server.Request;
import com.vn.ntsc.presentationserver.server.response.R_GetUserOnline;
import com.vn.ntsc.presentationserver.server.response.R_UpdateUser;

/**
 *
 * @author Administrator
 */
public class MeetPeopleProcessor implements IMeetPeopleProcessor {

    public static Map<String, User> allUser;
    public static Map<String, User> allSafeUser;
    public static Map<Integer, List<Data>> datas;
    public static ConcurrentLinkedQueue<Data> allData;
    public static Map<Integer, List<Data>> safeDatas;
    public static ConcurrentLinkedQueue<Data> allSafeData;

    public static IDAO dao;
    public static ListPosition listP;

    //<editor-fold desc="Init MeetPeopleProcessor for the first time that server started.">
    public static void init() {
        dao = new UserSettingDAO();
        listP = new ListPosition();
        allUser = new ConcurrentHashMap<>();
        allSafeUser = new ConcurrentHashMap<>();
        allData = new ConcurrentLinkedQueue<>();
        datas = new ConcurrentHashMap<>();
        allSafeData = new ConcurrentLinkedQueue<>();
        safeDatas = new ConcurrentHashMap<>();
//        for (int i = 0; i < Constant.multipleAll; i++) {
//            datas[i] = new TreeSet<>();
//        }
        AgePlus.agePlusRun();
    }
    //</editor-fold>   

    public static void getSafeUser(Map<String, User> allSafeUser) {

    }

    public static boolean meetOneCollectionPeople(LinkedList<User> res, Integer gender, int callWaiting, int age, Double lon, Double lat,
            double distance, String email, int location) {
        try {
            List<Data> list = new ArrayList<>();
            if (gender != null && (gender == 0 || gender == 1)){
                int collection = gender * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + (int) location;
                List data = datas.get(collection);
                if (data != null && !data.isEmpty())
                    list.addAll(data);
            }
            else {
                int collection1 = Constant.GENDER.MALE * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + (int) location;
                int collection2 = Constant.GENDER.FEMALE * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + (int) location;
                List data1 = datas.get(collection1);
                List data2 = datas.get(collection2);
                if (data1 != null && !data1.isEmpty())
                    list.addAll(data1);
                if (data2 != null && !data2.isEmpty())
                    list.addAll(data2);
            }
            if (!list.isEmpty()) {
                for (Data dt : list) {
                    String dtEmail = dt.email;
                    if (dtEmail != null) {
                        try {
                            if (User.calculateDistance(lat, lon, dt.lat, dt.lon) <= distance && !dtEmail.equals(email)) {
                                User u = allUser.get(dtEmail);
                                if (u != null) {
                                    res.add(u);
                                }
                            }
                        } catch (Exception ex) {
                            Util.addErrorLog(ex);
                        }
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
    }

    public static boolean meetOneCollectionPeopleSafeUser(LinkedList<User> res, long showme, int callWaiting, int age, Double lon, Double lat,
            double distance, String email, int location) {
        try {
            int collection = (int) showme * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + location;

            List<Data> list = safeDatas.get(collection);
            if (list != null) {
                for (Data dt : list) {
                    String dtEmail = dt.email;
                    if (dtEmail != null) {
                        try {
                            if (User.calculateDistance(lat, lon, dt.lat, dt.lon) <= distance && !email.equals(dtEmail)) {
                                User u = allSafeUser.get(dtEmail);
                                if (u != null) {
                                    res.add(u);
                                }
                            }
                        } catch (Exception ex) {
                            Util.addErrorLog(ex);
                        }
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
    }

    @Override
    public boolean initAllUsers() {
        return DatabaseLoader.initAllUsers();
    }

    @Override
    public LinkedList<User> meetPeople(Request request) {

        int lowerAge = request.meetPeople.lowerAge;
        int upperAge = request.meetPeople.upperAge;
        boolean isNewLogin = request.meetPeople.isNewLogin;
        String body_type = request.meetPeople.body_type;
        int is_avatar = request.meetPeople.is_avatar;
        int filter = request.meetPeople.filter;
        int distance = request.meetPeople.distance;
        ArrayList<Long> location = request.meetPeople.location;
        Double lon = request.meetPeople.lon;
        Double lat = request.meetPeople.lat;
        int skip = request.meetPeople.skip;
        int take = request.meetPeople.take;
        String email = request.meetPeople.email;
        int sortType = request.meetPeople.sortType;
        boolean is_interacted = request.meetPeople.is_interacted;
//        boolean isCheck = request.meetPeople.isCheck;
//        boolean isCheckFemale = request.meetPeople.isCheckFemale;
        Integer gender = request.meetPeople.gender;

//        if (isCheckFemale) {
//            Util.addDebugLog("IsCheckFemail TRUE");
//            insertSafeUserFromID(email);
//        }

        LinkedList<User> res = new LinkedList<>();
        boolean result = false;

//        if (isCheck) {
//            result = meetPeopleForSafeUser(res, email, lowerAge, upperAge, filter, isNewLogin, location, distance, lon, lat, skip, take, sortType, true, body_type, is_avatar, is_interacted);
//            if (!result) {
//                res = new LinkedList<>();
////            meetPeople(res, email, lowerAge, upperAge, filter, isNewLogin, location, distance, lon, lat, skip, take, sortType, true, body_type, is_avatar);
//                meetPeopleForSafeUser(res, email, lowerAge, upperAge, filter, isNewLogin, location, distance, lon, lat, skip, take, sortType, true, body_type, is_avatar, is_interacted);
//            }
//        } else {
            //        boolean result = meetPeople(res, email, lowerAge, upperAge, filter, isNewLogin, location, distance, lon, lat, skip, take, sortType, true, body_type, is_avatar);
            result = meetPeople(res, email, gender, lowerAge, upperAge, filter, isNewLogin, location, distance, lon, lat, skip, take, sortType, true, body_type, is_avatar, is_interacted);
            if (!result) {
                res = new LinkedList<>();
//            meetPeople(res, email, lowerAge, upperAge, filter, isNewLogin, location, distance, lon, lat, skip, take, sortType, true, body_type, is_avatar);
                meetPeople(res, email, gender, lowerAge, upperAge, filter, isNewLogin, location, distance, lon, lat, skip, take, sortType, true, body_type, is_avatar, is_interacted);
            }
//        }
        return res;
    }

    @Override
    public LinkedList<User> getListUserProfile(Request request) {

        int lowerAge = request.meetPeople.lowerAge;
        int upperAge = request.meetPeople.upperAge;
        boolean isNewLogin = request.meetPeople.isNewLogin;
        String body_type = request.meetPeople.body_type;
        int is_avatar = request.meetPeople.is_avatar;
        int filter = request.meetPeople.filter;
        int distance = request.meetPeople.distance;
        ArrayList<Long> location = request.meetPeople.location;
        Double lon = request.meetPeople.lon;
        Double lat = request.meetPeople.lat;
        int skip = request.meetPeople.skip;
        int take = request.meetPeople.take;
        String email = request.meetPeople.email;
        int sortType = request.meetPeople.sortType;
        boolean is_interacted = request.meetPeople.is_interacted;
        int gender = request.meetPeople.gender;
        LinkedList<User> res = new LinkedList<>();
        meetPeople(res, email, gender, lowerAge, upperAge, filter, isNewLogin, location, distance, lon, lat, skip, take, sortType, false, body_type, is_avatar, is_interacted);
        return res;
    }

    private static final long TWO_MONTH = 60L * Constant.A_DAY;

    public boolean meetPeople(LinkedList<User> result, String email, int lowerAge, int upperAge,
            int filter, boolean isNewLogin, ArrayList<Long> location, int distance, Double lon, Double lat, int skip, int take, int sortType, boolean updatePresentaion, int body_type, int is_avatar) {
        try {
            LinkedList<User> res = new LinkedList<>();
            if ((location == null || location.isEmpty()) && distance == Constants.SEARCH_BY_REGION) {
                location = new ArrayList<>();
                for (int i = 0; i < Constants.NUMBER_LOCATION; i++) {
                    location.add((long) i);
                }
            }
            User us = allUser.get(email);
            int show = 0;
            if (us.showme == 0) {
                show = 1;
            }
            if (lon == null || lat == null) {
                StatePosition cp = ListPosition.posMap.get(us.location);
                lon = cp.longtitude;
                lat = cp.latitude;
            }
            boolean isNewRegister = false;
            int lowerCall = 0, upperCall = 1;
            if (filter == Constants.FILTER_BY_CALL_WAITING) {
                lowerCall = 1;
            } else if (filter == Constants.FILTER_BY_REGISTER_TIME) {
                isNewRegister = true;
            }
            if (updatePresentaion) {
                updatePresentationByEmail(email, true);
            }
            us.lastOnline = Util.currentTime();
            if (distance == Constants.SEARCH_BY_REGION && location != null && !location.isEmpty()) { // search by location
                for (int age = lowerAge; age <= upperAge; age++) {
                    //Util.addDebugLog("SEARCH_BY_REGION location" + location.toString());
                    for (long loc : location) {
                        for (int callWaiting = lowerCall; callWaiting <= upperCall; callWaiting++) {
                            int collection = (int) show * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + (int) loc;

                            List<Data> list = datas.get(collection);
                            if (list != null) {
                                for (Data dt : list) {
                                    try {
                                        if (!email.equals(dt.email)) {
                                            User u = allUser.get(dt.email);
                                            if (u != null) {
                                                res.add(u);
//                                                if(u.location!=loc) {
//                                                    res.remove(u);
//                                                }
                                            }
                                        }
                                    } catch (Exception ex) {
                                        Util.addErrorLog(ex);
                                    }
                                }
                            }
                        }
                    }
                }
            } else { // search by distance
                double dis = Setting.Distance.Value.get(distance);
                for (int age = lowerAge; age <= upperAge; age++) {
                    for (int callWaiting = lowerCall; callWaiting <= upperCall; callWaiting++) {
                        for (int loc = 0; loc < Constants.MAX_LOCATION; loc++) {
                            LinkedList<User> list = new LinkedList<>();
                            boolean getOneCollectionResult = meetOneCollectionPeople(list, show, callWaiting, age, lon, lat, dis, email, loc);
                            if (!getOneCollectionResult) {
                                list = new LinkedList<>();
                                meetOneCollectionPeople(list, show, callWaiting, age, lon, lat, dis, email, loc);
                            }
                            res.addAll(list);
                        }
                    }
                }
            }
//        System.out.println("res : " + res.size());
            LinkedList<User> unsortedList = new LinkedList<>();
            long now = Util.getGMTTime().getTime();
            long aDayAgo = now - Constant.A_DAY;
            long twoMonthAgo = now - TWO_MONTH;
            for (User user : res) {
                try {
                    if (isNewRegister && user.registerTime <= twoMonthAgo) {
                        continue;
                    }
                    if (isNewLogin && user.lastOnline <= aDayAgo) {
                        continue;
                    }
                    unsortedList.add(user);
//                    Util.addDebugLog("body_type9 " +body_type);
//                    Util.addDebugLog("body_typereal " +user.body_type);
//                     Util.addDebugLog("List " +unsortedList.toString());

                    if (body_type != -1) {
                        if (body_type == user.body_type) {
                            //Util.addDebugLog("body_type9 = " +user.body_type);

                        } else {
                            //Util.addDebugLog("body_type9 != " +user.body_type);
                            unsortedList.remove(user);
                        }
                    }
                    if (is_avatar != -1) {
                        if (is_avatar == 1) {
                            if (user.avatarId == null) {
                                unsortedList.remove(user);
                            }
                        } else if (is_avatar == 0) {
                            if (user.avatarId != null) {
                                unsortedList.remove(user);
                            }
                        } else {

                        }
                    }

                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
            }
            LinkedList<User> sortedList = User.sortByRequirement(unsortedList, lon, lat, skip, take, sortType);
            result.addAll(sortedList);
            return true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return false;

    }

    public boolean meetPeople(LinkedList<User> result, String email, Integer gender, int lowerAge, int upperAge,
            int filter, boolean isNewLogin, ArrayList<Long> location, int distance, Double lon, Double lat, int skip, int take, int sortType, boolean updatePresentaion, String body_type, int is_avatar, boolean is_interacted) {
        try {
            LinkedList<User> res = new LinkedList<>();
            if ((location == null || location.isEmpty()) && distance == Constants.SEARCH_BY_REGION) {
                location = new ArrayList<>();
                for (int i = 0; i < Constants.NUMBER_LOCATION; i++) {
                    location.add((long) i);
                }
            }
            User us = null;
            if (email != null)
                us = allUser.get(email);
            
//            int show = 0;
//            if (us.showme == 0) {
//                show = 1;
//            }
            if (us != null){
                us.lastOnline = Util.currentTime();
                if (lon == null || lat == null) {
                    StatePosition cp = ListPosition.posMap.get(us.location);
                    lon = cp.longtitude;
                    lat = cp.latitude;
                }
            }
            else {
                lon = 0.0;
                lat = 0.0;
            }
            boolean isNewRegister = false;
            int lowerCall = 0, upperCall = 1;
            if (filter == Constants.FILTER_BY_CALL_WAITING) {
                lowerCall = 1;
            } else if (filter == Constants.FILTER_BY_REGISTER_TIME) {
                isNewRegister = true;
            }
            if (updatePresentaion) {
                updatePresentationByEmail(email, true);
            }
            if (distance == Constants.SEARCH_BY_REGION && location != null && !location.isEmpty()) { // search by location
                for (int age = lowerAge; age <= upperAge; age++) {
                    for (long loc : location) {
                        for (int callWaiting = lowerCall; callWaiting <= upperCall; callWaiting++) {
                            List<Data> list = new ArrayList<>();
                            if (gender != null && (gender == 0 || gender == 1)){
                                int collection = gender * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + (int) loc;
                                List data = datas.get(collection);
                                if (data != null && !data.isEmpty())
                                    list.addAll(data);
                            }
                            else {
                                int collection1 = Constant.GENDER.MALE * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + (int) loc;
                                int collection2 = Constant.GENDER.FEMALE * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + (int) loc;
                                List data1 = datas.get(collection1);
                                List data2 = datas.get(collection2);
                                if (data1 != null && !data1.isEmpty())
                                    list.addAll(data1);
                                if (data2 != null && !data2.isEmpty())
                                    list.addAll(data2);
                            }
                            if (!list.isEmpty()) {
                                for (Data dt : list) {
                                    try {
                                        if (!dt.email.equals(email)) {
                                            User u = allUser.get(dt.email);
                                            if (u != null) {
                                                res.add(u);
//                                                if(u.location!=loc) {
//                                                    res.remove(u);
//                                                }
                                            }
                                        }
                                    } catch (Exception ex) {
                                        Util.addErrorLog(ex);
                                    }
                                }
                            }
                        }
                    }
                }
            } else { // search by distance
                double dis = Setting.Distance.Value.get(distance);
                for (int age = lowerAge; age <= upperAge; age++) {
                    for (int callWaiting = lowerCall; callWaiting <= upperCall; callWaiting++) {
                        for (int loc = 0; loc < Constants.MAX_LOCATION; loc++) {
                            LinkedList<User> list = new LinkedList<>();
                            boolean getOneCollectionResult = meetOneCollectionPeople(list, gender, callWaiting, age, lon, lat, dis, email, loc);
                            if (!getOneCollectionResult) {
                                list = new LinkedList<>();
                                meetOneCollectionPeople(list, gender, callWaiting, age, lon, lat, dis, email, loc);
                            }
                            res.addAll(list);
                        }
                    }
                }
            }
            Util.addDebugLog("----------------res : " + res.size());
            LinkedList<User> unsortedList = new LinkedList<>();
            long now = Util.getGMTTime().getTime();
            long aDayAgo = now - Constant.A_DAY;
            long twoMonthAgo = now - TWO_MONTH;

            List<String> interactedList = UserInteractionDAO.getListInteraction(email);
            for (User user : res) {
                try {
                    if (isNewRegister && user.registerTime <= twoMonthAgo) {
                        continue;
                    }
                    if (isNewLogin && user.lastOnline <= aDayAgo) {
                        continue;
                    }
                    
                    if (body_type != null && !body_type.isEmpty()) {
                        body_type = body_type.replaceAll(" ", "");
                        String[] bodyArr = body_type.split(",");
                        ArrayList<Integer> lstBodyType = new ArrayList<>();
                        for (String type : bodyArr) {
                            lstBodyType.add(Integer.parseInt(type));
                        }
                        if (!lstBodyType.contains(user.body_type)) {
                            continue;
                        }
                    }
                    
                    if (is_avatar != -1) {
                        if (is_avatar == 1) {
                            if (user.avatarId == null) {
                                continue;
                            }
                        } else if (is_avatar == 0) {
                            if (user.avatarId != null) {
                                continue;
                            }
                        }
                    }
                    
                    //Linh add #5747
                    if (is_interacted && interactedList.contains(user.email)) {
                        continue;
                    }
                    
                    unsortedList.add(user);
                    
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
            }
            Util.addDebugLog("---------------- after : " + unsortedList.size());
            LinkedList<User> sortedList = User.sortByRequirement(unsortedList, lon, lat, skip, take, sortType);
            result.addAll(sortedList);
            return true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return false;

    }

    public boolean meetPeopleForSafeUser(LinkedList<User> result, String email, int lowerAge, int upperAge,
            int filter, boolean isNewLogin, ArrayList<Long> location, int distance, Double lon, Double lat, int skip, int take, int sortType, boolean updatePresentaion, int body_type, int is_avatar, boolean is_interacted) {
        try {
            LinkedList<User> res = new LinkedList<>();
            if ((location == null || location.isEmpty()) && distance == Constants.SEARCH_BY_REGION) {
                location = new ArrayList<>();
                for (int i = 0; i < Constants.NUMBER_LOCATION; i++) {
                    location.add((long) i);
                }
            }
            User us = allUser.get(email);

            int show = 0;
            if (us.showme == 0) {
                show = 1;
            }
            if (lon == null || lat == null) {
                StatePosition cp = ListPosition.posMap.get(us.location);
                lon = cp.longtitude;
                lat = cp.latitude;
            }
            boolean isNewRegister = false;
            int lowerCall = 0, upperCall = 1;
            if (filter == Constants.FILTER_BY_CALL_WAITING) {
                lowerCall = 1;
            } else if (filter == Constants.FILTER_BY_REGISTER_TIME) {
                isNewRegister = true;
            }
            if (updatePresentaion) {
                updatePresentationByEmail(email, true);
            }
            us.lastOnline = Util.currentTime();
            if (distance == Constants.SEARCH_BY_REGION && location != null && !location.isEmpty()) { // search by location
                for (int age = lowerAge; age <= upperAge; age++) {
                    //Util.addDebugLog("SEARCH_BY_REGION location" + location.toString());
                    for (long loc : location) {
                        for (int callWaiting = lowerCall; callWaiting <= upperCall; callWaiting++) {
                            int collection = (int) show * Constants.MUL_SHOW_ME + callWaiting * Constants.MUL_CALL_WAITING + age * Constants.MUL_AGE + (int) loc;

                            List<Data> list = safeDatas.get(collection);
                            if (list != null) {
                                for (Data dt : list) {
                                    try {
                                        if (!email.equals(dt.email)) {
                                            User u = allSafeUser.get(dt.email);
                                            if (u != null) {
                                                res.add(u);
//                                                if(u.location!=loc) {
//                                                    res.remove(u);
//                                                }
                                            }
                                        }
                                    } catch (Exception ex) {
                                        Util.addErrorLog(ex);
                                    }
                                }
                            }
                        }
                    }
                }
            } else { // search by distance
                double dis = Setting.Distance.Value.get(distance);
                for (int age = lowerAge; age <= upperAge; age++) {
                    for (int callWaiting = lowerCall; callWaiting <= upperCall; callWaiting++) {
                        for (int loc = 0; loc < Constants.MAX_LOCATION; loc++) {
                            LinkedList<User> list = new LinkedList<>();
                            boolean getOneCollectionResult = meetOneCollectionPeopleSafeUser(list, show, callWaiting, age, lon, lat, dis, email, loc);
                            if (!getOneCollectionResult) {
                                list = new LinkedList<>();
                                meetOneCollectionPeopleSafeUser(list, show, callWaiting, age, lon, lat, dis, email, loc);
                            }
                            res.addAll(list);
                        }
                    }
                }
            }
//        System.out.println("res : " + res.size());
            LinkedList<User> unsortedList = new LinkedList<>();
            long now = Util.getGMTTime().getTime();
            long aDayAgo = now - Constant.A_DAY;
            long twoMonthAgo = now - TWO_MONTH;

            List<String> interactedList = UserInteractionDAO.getListInteraction(email);
            for (User user : res) {
                try {
                    if (isNewRegister && user.registerTime <= twoMonthAgo) {
                        continue;
                    }
                    if (isNewLogin && user.lastOnline <= aDayAgo) {
                        continue;
                    }
                    unsortedList.add(user);
//                    Util.addDebugLog("body_type9 " +body_type);
//                    Util.addDebugLog("body_typereal " +user.body_type);
//                     Util.addDebugLog("List " +unsortedList.toString());

                    if (body_type != -1) {
                        if (body_type != user.body_type) {
                            unsortedList.remove(user);
                        }
                    }
                    if (is_avatar != -1) {
                        if (is_avatar == 1) {
                            if (user.avatarId == null) {
                                unsortedList.remove(user);
                            }
                        } else if (is_avatar == 0) {
                            if (user.avatarId != null) {
                                unsortedList.remove(user);
                            }
                        } else {

                        }
                    }
                    //Linh add #5747
                    if (is_interacted && interactedList.contains(user.email)) {
                        unsortedList.remove(user);
                    }

                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
            }
            LinkedList<User> sortedList = User.sortByRequirement(unsortedList, lon, lat, skip, take, sortType);
            result.addAll(sortedList);
            return true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return false;

    }

    private boolean updatePresentationByEmail(String email, boolean isOnline) {
        try {
            if (email != null && allUser.containsKey(email)) {
                User user = (User) allUser.get(email);
                Data dt = new Data(user.lon, user.lat, user.email, user.isOnline);
                allData.remove(dt);
                user.isOnline = isOnline;
                dt = new Data(user.lon, user.lat, user.email, user.isOnline);
                allData.add(dt);
                return true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return false;
    }

    @Override
    public LinkedList<User> getUsersPresentation(Request request) {
        LinkedList<User> res = new LinkedList<>();
        Iterator<String> it = request.getUsersPresentation.llUserEmail.iterator();
        String email = request.getUsersPresentation.userId;
        
        double lon = 0, lat = 0;
        if (email != null){
            User user = allUser.get(email);
            if (user != null) {
                lon = user.lon;
                lat = user.lat;
            }
        }

        while (it.hasNext()) {
            String id = it.next();
            res.add(allUser.get(id));
        }

        LinkedList<User> RR = new LinkedList<>();
        Iterator<User> itU = res.iterator();
        while (itU.hasNext()) {
            User u = itU.next();
            if (u == null) {
                RR.add(null);
            } else {
                try {
                    User addUser = u.getInfor(lon, lat, u.lastOnline);
                    RR.add(addUser);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    RR.add(null);
                }
            }
        }
        return RR;
//        } else {
//            return new LinkedList<>();
//        }

    }

    @Override
    public boolean insert(Request request) {
        try {
            String email = request.insertUser.email;
            boolean isOnline = false;
            if (allUser.containsKey(email)) {
                User user = (User) allUser.get(email);
                if (user != null) {
                    isOnline = user.isOnline;
                }
            }
            removeUserByMail(email);
            int showme = request.insertUser.showme;
            int age;
            if (request.insertUser.birthday != null) {
                String birthday = request.insertUser.birthday;
                Date bir = new Date();
                try {
                    bir = DateFormat.parse_yyyyMMdd(birthday);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    age = 0;
                }
                AgePlus.mDate.put(email, bir);
                Date now = new Date();
                age = Tool.calAge(now, bir);
            } else {
                age = request.insertUser.age;
            }
            long registerTime = request.insertUser.registerTime;
            double lon = request.insertUser.lon;
            double lat = request.insertUser.lat;
            String userName = request.insertUser.username;
            String avatarId = request.insertUser.avataID;
            int location = request.insertUser.location;
            int callWaiting = request.insertUser.callWaiting;
            boolean isVideo = request.insertUser.isVideo;
            boolean isVoice = request.insertUser.isVoice;
            long lastOnline = request.insertUser.lastOnline;
            int body_type = request.insertUser.body_type;
            return insertUser(showme, callWaiting, age, registerTime, lon, lat, isOnline, email, userName, avatarId, location, isVideo, isVoice, lastOnline, body_type);
        } catch (Exception ex) {

            Util.addErrorLog(ex);
            return false;
        }

    }

    public static boolean insertUser(int showme, int callWaiting, int age, long registerTime, double lon, double lat, boolean isOnline,
            String email, String userName, String avatarId, int location, boolean isVideo, boolean isVoice, long lastOnline, int body_type) {
        try {
            if (allUser.containsKey(email)) {
                return false;
            }
            User u = new User(showme, callWaiting, age, registerTime, lon, lat, isOnline, email, userName, avatarId, location, isVideo, isVoice, lastOnline, body_type);
            allUser.put(email, u);
            int collection = u.calCollection();
            Data data = new Data(lon, lat, email, isOnline);
            allData.add(data);
            List<Data> list = datas.get(collection);
            if (list == null) {
                list = new ArrayList<>();
            }
            if (!list.contains(data)) {
                list.add(data);
            }
            datas.put(collection, list);
            return true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
    }

    public static void insertSafeUserFromID(String userId) {
        try {
            DBCollection coll = db.getCollection(UserdbKey.USERS_COLLECTION);
            DBCollection collLocation = db.getCollection(UserdbKey.USER_ACTIVITY_COLLECTION);
            BasicDBObject find = new BasicDBObject(UserdbKey.USER.USER_ID, userId);

            DBCursor dbCur = coll.find(find);
            Iterator<DBObject> iDb = dbCur.iterator();
            DBObject oDb;
            Date now = new Date();
            while (iDb.hasNext()) {

                oDb = iDb.next();

                Integer flag = (Integer) oDb.get(UserdbKey.USER.FLAG);
                flag = flag == null ? Constant.FLAG.ON : flag;
//                    Integer verificationFlag = (Integer) oDb.get(UserdbKey.USER.VERIFICATION_FLAG);
                Integer finishRegisFlag = (Integer) oDb.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                finishRegisFlag = finishRegisFlag == null ? Constant.FLAG.ON : finishRegisFlag;
                Integer systemAccount = (Integer) oDb.get("sys_acc");
                boolean isSystemAccount = systemAccount != null && systemAccount > 0;
                if (isSystemAccount || ((flag == Constant.FLAG.ON) /**
                         * && (verificationFlag == null || verificationFlag ==
                         * Constant.YES)
                         */
                        && (finishRegisFlag == Constant.FLAG.ON))) {
                    ObjectId obId = (ObjectId) oDb.get(UserdbKey.USER.ID);

                    String user_id = obId.toString();
                    String user_name = (String) oDb.get(UserdbKey.USER.USERNAME);
                    String avatar_id = (String) oDb.get(UserdbKey.USER.AVATAR_ID);
                    Integer gender = (Integer) oDb.get(UserdbKey.USER.GENDER);
                    Integer body_type = (Integer) oDb.get(UserdbKey.USER.BODY_TYPE);
                    if (body_type == null) {
                        body_type = 999;
                    }
                    if (gender != null) {
                        Integer loc = (Integer) oDb.get(UserdbKey.USER.REGION);
                        int location = 0;
                        if (loc != null && loc != -1) {
                            location = loc;
                        }
                        String birthday = (String) oDb.get(UserdbKey.USER.BIRTHDAY);
                        Boolean videoCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VIDEO_CALL_WAITING);
                        Boolean voiceCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VOICE_CALL_WAITING);
                        boolean isVideo = false;
                        if (videoCallWaiting == null) {
                            if (gender == 1) {
                                isVideo = true;
                            }
                        } else {
                            isVideo = videoCallWaiting;
                        }

                        boolean isVoice = false;
                        if (voiceCallWaiting == null) {
                            if (gender == 1) {
                                isVoice = true;
                            }
                        } else {
                            isVoice = voiceCallWaiting;
                        }
                        int callWaiting = 1;
                        if (!isVideo && !isVoice) {
                            callWaiting = 0;
                        }
                        Date bir = new Date();
                        try {
                            bir = DateFormat.parse_yyyyMMdd(birthday);
                        } catch (Exception ex) {
                            Util.addErrorLog(ex);
                        }
                        AgePlus.mDate.put(user_id, bir);
                        long age = Tool.calAge(now, bir);
                        String registerTimeStr = (String) oDb.get(UserdbKey.USER.REGISTER_DATE);
                        long registerTime = System.currentTimeMillis();
                        if (registerTimeStr != null) {
                            registerTime = DateFormat.parse(registerTimeStr).getTime();
                        }
                        String lastLogin = (String) oDb.get(UserdbKey.USER.LAST_LOGIN_TIME);
                        long lastOnline = registerTime;
                        if (lastLogin != null) {
                            lastOnline = DateFormat.parse(lastLogin).getTime();
                        }
                        DBObject oDb_loc = collLocation.findOne(new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, obId));
                        double lon = 0, lat = 0;
                        if (oDb_loc != null) {
                            ArrayList<Double> locs = (ArrayList<Double>) (oDb_loc.get(UserdbKey.USER_ACTIVITY.LOCATION));
                            if (locs != null) {
                                lon = locs.get(0);
                                lat = locs.get(1);
                            }
                        }
                        boolean isOnline = false;

                        MeetPeopleProcessor.insertSafeUser((int) gender, callWaiting, (int) age, registerTime, lon, lat, isOnline, user_id, user_name, avatar_id, location, isVideo, isVoice, lastOnline, (int) body_type);
                    }
                }

            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean insertSafeUser(int showme, int callWaiting, int age, long registerTime, double lon, double lat, boolean isOnline,
            String email, String userName, String avatarId, int location, boolean isVideo, boolean isVoice, long lastOnline, int body_type) {
        Util.addDebugLog("IsCheckFemail insertSafeUser START");
        Util.addDebugLog("IsCheckFemail insertSafeUser allSafeUser " + allSafeUser.size());
        Util.addDebugLog("IsCheckFemail insertSafeUser allSafeData " + allSafeData.size());
        Util.addDebugLog("IsCheckFemail insertSafeUser safeDatas " + safeDatas.size());
        Util.addDebugLog("IsCheckFemail insertSafeUser allSafeUser ===============================");

        try {
            if (allSafeUser.containsKey(email)) {
                return false;
            }
            User u = new User(showme, callWaiting, age, registerTime, lon, lat, isOnline, email, userName, avatarId, location, isVideo, isVoice, lastOnline, body_type);
            allSafeUser.put(email, u);
            int collection = u.calCollection();
            Data data = new Data(lon, lat, email, isOnline);
            allSafeData.add(data);
            List<Data> list = safeDatas.get(collection);
            if (list == null) {
                list = new ArrayList<>();
            }
            if (!list.contains(data)) {
                list.add(data);
            }
            safeDatas.put(collection, list);
            Util.addDebugLog("IsCheckFemail insertSafeUser allSafeUser " + allSafeUser.size());
            Util.addDebugLog("IsCheckFemail insertSafeUser allSafeData " + allSafeData.size());
            Util.addDebugLog("IsCheckFemail insertSafeUser safeDatas " + safeDatas.size());
            Util.addDebugLog("IsCheckFemail insertSafeUser END");
            return true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }

    }

    public static boolean removeUserByMail(String email) {
        if (allUser.containsKey(email)) {
            User user = (User) allUser.get(email);
            if (user == null) {
                return true;
            }
            int collection = user.calCollection();
            Data dt = new Data(user.lon, user.lat, user.email, user.isOnline);
            try {
                allData.remove(dt);
                List<Data> list = datas.get(collection);
                if (list != null) {
                    list.remove(dt);
                }
                datas.put(collection, list);
                allUser.remove(email);
                // mDate.remove(email);
            } catch (Exception ex) {
                Util.addErrorLog(ex);

                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeUser(Request request) {
        String email = request.removeUser.email;
        return removeUserByMail(email);
    }

    @Override
    public boolean updatePlace(Request request) {
        double lon = request.updatePlace.lon;
        double lat = request.updatePlace.lat;
        String email = request.updatePlace.email;
        User u = allUser.get(email);
        Data dt = new Data(lon, lat, email, true);
        if (allUser.containsKey(email)) {
            User user = allUser.get(email);
            removeUserByMail(email);
            allUser.put(email, user);
            allData.add(dt);
            List<Data> list = datas.get(user.calCollection());
            list.add(dt);
            datas.put(user.calCollection(), list);
//            datas.put(user.calCollection());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateMeetPeopleSetting(Request request) {

        String email = request.updateMeetPeopleSetting.email;
        ArrayList<Long> showme = request.updateMeetPeopleSetting.showme;
//        int interest = request.updateMeetPeopleSetting.interest;
        int lower_age = request.updateMeetPeopleSetting.lower_age;
        int upper_age = request.updateMeetPeopleSetting.upper_age;
        ArrayList<Long> ethnics = request.updateMeetPeopleSetting.ethnics;
        ArrayList<Long> location = request.updateMeetPeopleSetting.location;
        int distance = request.updateMeetPeopleSetting.distance;
        QuerySetting query = new QuerySetting(showme, null, lower_age, upper_age, ethnics, distance, location);
        dao.updateMeetPeopleSetting(email, query);

        return true;
    }

    @Override
    public QuerySetting getMeetPeopleSetting(Request request) {
        String email = request.getMeetPeopleSetting.email;
        return getMeetPeopleSetting(email);
    }

    public QuerySetting getMeetPeopleSetting(String email) {
        QuerySetting setting = dao.getMeetPeopleSetting(email);
        if (setting == null) {
            setting = new QuerySetting();
        }
        return setting;
    }

    @Override
    public R_UpdateUser updateUser(Request request) {
        String email = request.updateUser.email;
        String user_name = request.updateUser.username;
        String avatar_id = request.updateUser.avataID;
        Integer age = request.updateUser.age;
        Double lon = request.updateUser.lon;
        Double lat = request.updateUser.lat;
        Boolean isOnline = request.updateUser.isOnline;
        Integer location = request.updateUser.location;
        String bir = request.updateUser.bir;
        Integer body_type = request.updateUser.bodyType;
        Boolean isVideo = request.updateUser.isVideo;
        Boolean isVoice = request.updateUser.isVoice;

        User user = allUser.get(email);
        Data dt = new Data();
        if (user == null) {
            return new R_UpdateUser(false);
        } else {
            dt = new Data(user.lon, user.lat, email, user.isOnline);
            int collection = user.calCollection();
            allData.remove(dt);
            List<Data> list = datas.get(collection);
            if (list != null) {
                list.remove(dt);
            }
            datas.put(collection, list);
        }
        if (isVideo != null) {
            user.isVideoCallWating = isVideo;
        }
        if (isVoice != null) {
            user.isVoicecallWaiting = isVoice;
        }
        if (!user.isVideoCallWating && !user.isVoicecallWaiting) {
            user.callWaiting = 0;
        } else {
            user.callWaiting = 1;
        }
        if (user_name != null) {
            user.username = user_name;
        }
        if (avatar_id != null) {
            if (avatar_id.compareTo("-1") == 0) {
                avatar_id = null;
            }
            user.avatarId = avatar_id;
        }

        if (age != null) {
            user.age = age;
        }
        if (location != null) {
            user.location = location;
        }
        if (lon != null) {
            user.lon = lon;
            dt.lon = lon;
        }

        if (lat != null) {
            user.lat = lat;
            dt.lat = lat;

        }
        if (bir != null) {
            Date birthday = new Date();
            try {
                birthday = DateFormat.parse_yyyyMMdd(bir);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            AgePlus.mDate.put(email, birthday);
            int newAge = Tool.calAge(new Date(), birthday);
            user.age = newAge;
        }
        
        if(body_type != null){
            user.body_type = body_type;
        }
        allData.add(dt);
        List<Data> list = datas.get(user.calCollection());
        if (list == null) {
            list = new ArrayList<>();
        }
        if (!list.contains(dt)) {
            list.add(dt);
        }
        datas.put(user.calCollection(), list);

        if (isOnline != null) {
            user.isOnline = isOnline;
        }

        return new R_UpdateUser(true);

    }

    @Override
    public boolean updateDistance(Request request) {
        ArrayList<Double> dis = request.updateDistance.distance;
        for (int i = 0; i < dis.size() && i < 4; i++) {
            Setting.Distance.Value.set(i, dis.get(i));
        }
        return true;
    }

    LinkedList<String> randomUser(LinkedList<String> user, int number) {
        LinkedList<String> res = new LinkedList<>();
        int n = user.size();

        boolean[] choose = new boolean[n];
        if (number > n) {
            number = n;
        }
        if (number + number > n) {
            int d = n - number, x;
            for (int i = 0; i < n; i++) {
                choose[i] = true;
            }
            for (int i = 0; i < d; i++) {
                while (true) {
                    x = (int) (Math.random() * n);
                    if (choose[x]) {
                        choose[x] = false;
                        break;
                    }
                }
            }
        } else {
            int x;
            for (int i = 0; i < number; i++) {
                while (true) {
                    x = (int) (Math.random() * n);
                    if (!choose[x]) {
                        choose[x] = true;
                        break;
                    }
                }
            }
        }

        Iterator<String> it = user.iterator();
        int run = 0;
        while (it.hasNext()) {
            String s = it.next();
            if (choose[run]) {
                res.add(s);
            }
            run++;
        }
        return res;
    }

    @Override
    public R_GetUserOnline getNumberUserOnline(Request request) {
        R_GetUserOnline res = new R_GetUserOnline();
        int RR = 0;
        User u;
        ArrayList<String> arr = new ArrayList<>();
        int skip = request.getUserOnline.skip;
        int take = request.getUserOnline.take;
        int female = 0, male = 0, voiceCall = 0, videoCall = 0;
        for (Data data : allData) {
            try {
                String email = data.email;
                if (email != null) {
                    u = allUser.get(email);
                    if (u != null) {
                        if (!arr.contains(email)) {
                            if (u.isOnline) {
                                RR++;
                                arr.add(email);
                                if (u.showme == Constant.GENDER.FEMALE) {
                                    female++;
                                } else {
                                    male++;
                                }
                                if (u.isVideoCallWating) {
                                    videoCall++;
                                }
                                if (u.isVoicecallWaiting) {
                                    voiceCall++;
                                }
                            }

                        }
                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
        }

        res.total = RR;
        res.female = female;
        res.male = male;
        res.videoCall = videoCall;
        res.voiceCall = voiceCall;
        for (int i = skip; i < skip + take; i++) {
            if (i < arr.size()) {
                res.llUser.add(arr.get(i));
            }
        }
        return res;
    }

    @Override
    public LinkedList<String> autoMessage(Request request) {
        int number = request.autoMessage.number;

        LinkedList<String> lluser = new LinkedList<>();
        Iterator<Data> idt = allData.iterator();
        while (idt.hasNext()) {
            String e = idt.next().email;
            if (e != null) {
                lluser.add(e);
            }
        }

        int n = lluser.size();
        if (number == 0) {
            number = n;
        } else {
            number = (int) (number * 0.25 * n);
        }
        return randomUser(lluser, number);
    }

    @Override
    public boolean login(Request request) {
        boolean result = false;
        String email = request.login.email;
        User user = (User) allUser.get(email);
        if (user != null) {
            Data dt = new Data(user.lon, user.lat, user.email, user.isOnline);
            allData.remove(dt);
            if (!user.isOnline) {
                result = true;
            }
            Integer bodyType = (Integer) UserDAO.getUserInfor(email, ParamKey.BODY_TYPE);
            if (bodyType != null){
                user.body_type = bodyType;
            }
            else {
                bodyType = -1;
            }

            user.isOnline = true;
            user.lastOnline = Util.currentTime();
            dt = new Data(user.lon, user.lat, user.email, user.isOnline);
            allData.add(dt);
        }
        return result;
    }

    @Override
    public void logout(Request request) {
        String email = request.logout.email;
        User user = (User) allUser.get(email);
        if (user != null) {
            updatePresentationByEmail(email, false);
        }
    }

    @Override
    public void deletePresentation(Request request) {
        ArrayList<String> list = request.deletePresentation.list;
        for (String email : list) {
            User user = (User) allUser.get(email);
            if (user != null) {
                updatePresentationByEmail(email, false);
            }
        }
    }

    @Override
    public void updatePresentaion(Request request) {
        ArrayList<String> list = request.updatePresentation.list;
        for (String email : list) {
            User user = (User) allUser.get(email);
            if (user != null) {
                updatePresentationByEmail(email, true);
            }
        }
    }

    @Override
    public void autoLogout(Request request) {
        List<String> list = request.autoLogout.list;
        for (String email : list) {
            User user = (User) allUser.get(email);
            if (user != null) {
                updatePresentationByEmail(email, false);
            }
        }
    }

}
