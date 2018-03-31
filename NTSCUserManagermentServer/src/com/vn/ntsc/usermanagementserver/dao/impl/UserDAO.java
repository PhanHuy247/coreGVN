/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteConcern;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import java.util.TreeMap;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.dbentity.UserDB;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetUserByRegisterDateData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.About;
import com.vn.ntsc.usermanagementserver.entity.impl.user.FollowingUser;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Female;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Male;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInfor;

/**
 *
 * @author DuongLTD
 */
public class UserDAO {

    private static MessageDigest md;
    private static DBCollection coll;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.USERS_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static Integer getGenderFromUserId(String userId) throws EazyException {
        Integer gender = 0;
        ObjectId id = new ObjectId(userId);
        BasicDBObject find = new BasicDBObject(UserdbKey.USER.ID, id);
        DBObject object = coll.findOne(find);
        if(object != null){
            gender = (Integer)object.get(UserdbKey.USER.GENDER);
        }
        return gender;
    }

    private static void upadateSipPassword(String userId, String email, String password) throws EazyException {
        String pass = password;
        if (email == null) {
            pass = userId;
        }
        ObjectId id = new ObjectId(userId);
        BasicDBObject find = new BasicDBObject(UserdbKey.USER.ID, id);
        BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER.SIP_PASSWORD, pass));
        coll.update(find, set);
    }

    private static void upadateUserId(ObjectId id) throws EazyException {
        BasicDBObject find = new BasicDBObject(UserdbKey.USER.ID, id);
        BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER.USER_ID, id.toString()));
        coll.update(find, set);
    }

    public static void upadateDeviceType(String id, Integer deviceType) throws EazyException {
        BasicDBObject find = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(id));
        BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER.DEVICE_TYPE, deviceType));
        coll.update(find, set);
    }

    // Add LongLT 8/2016
    public static void updateStringField(String id, String key, String value) throws EazyException {
        BasicDBObject find = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(id));
        Util.addDebugLog("====update app_version updateStringField====" + find.toString());
        if (ParamKey.ADID.equals(key)) {
            key = ParamKey.AD_ID;
        }
        if (ParamKey.APP_VERSION.equals(key)) {

            Util.addDebugLog("update app_version " + value);
            BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(key, value));
            Util.addDebugLog("====update app_version updateStringField==== set:" + set.toString());
            coll.update(find, set);
        } else {
            BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(key, value));
            coll.update(find, set);
        }
    }

    public static boolean isexitsedEmail(String email, String id) {
        BasicDBObject doc = new BasicDBObject(UserdbKey.USER.EMAIL, email);
        DBObject obj1 = coll.findOne(doc);
        if (obj1 != null) { // found email 
            String userId = (String) obj1.get(UserdbKey.USER.USER_ID);
            if(userId.equals(id)){
                return false;
            }else{
                return true;
            }
        }
        else
            return false;
    }

    public static void updateIntField(String id, String key, int value) throws EazyException {
        BasicDBObject find = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(id));

        BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(key, value));
        coll.update(find, set);

    }

    public static User login(User user) throws EazyException {
        UserDB respondDBObject;
        User result = new User();
        String originalEmail = user.email;
        User checkLoginFb = null;
        if(user.fbId != null){
            checkLoginFb = getInfoUserLoginFb(user.fbId);
        } 
        if (checkLoginFb != null && checkLoginFb.password != null) {
            user.email = checkLoginFb.email;
        } else {
            String email = Util.createEmailField(user.email, user.fbId).toLowerCase();
            user.email = email;
        }
        try {
//            Util.addDebugLog("afterEmail : " + user.email);
            //serch object
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.EMAIL, user.email);
            //command find
            DBObject obj = coll.findOne(doc);

            if (obj == null) { // not found email
                throw new EazyException(ErrorCode.EMAIL_NOT_FOUND);
            } else { //found email
                Integer flag = (Integer) obj.get(UserdbKey.USER.FLAG);
                if (flag != null && flag == Constant.USER_STATUS_FLAG.DISABLE) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
                if (originalEmail != null) { // login by email
                    String dbPass = (String) obj.get(UserdbKey.USER.PASSWORD);
                    byte[] b = md.digest(user.password.getBytes());
                    String uPass = Util.byteToString(b);
                    if (uPass.equals(dbPass)) {
                        respondDBObject = UserDB.fromDBObject(obj);
                        result = respondDBObject.getUserLogin();
                        if (flag == null || flag == Constant.FLAG.OFF) {
                            result.isActiveUser = Constant.FLAG.ON;
                        }
                    } else {
                        throw new EazyException(ErrorCode.INCORRECT_PASSWORD);
                    }
                } else { // login by fb
                    respondDBObject = UserDB.fromDBObject(obj);
                    result = respondDBObject.getUserLogin();
                    if (flag == null || flag == Constant.FLAG.OFF) {
                        result.isActiveUser = Constant.FLAG.ON;
                    }
                }
                String sipPassword = (String) obj.get(UserdbKey.USER.SIP_PASSWORD);
                if (sipPassword == null) {
                    upadateSipPassword(result.userId, originalEmail, user.password);
                }
            }

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;

    }
    
    public static User getInfoUserLoginFb(String fbId){
        User user = new User();
        DBObject obj = new BasicDBObject(UserdbKey.USER.FB_ID, fbId);
        DBObject dbObject = coll.findOne(obj);
        if(dbObject != null){
            user.password = (String)dbObject.get("pwd");
            user.email = (String)dbObject.get("email");
        }
        return user;
    }

    public static List<String> getAllSystemAcc() throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBObject gte = new BasicDBObject("$gt", 0);
            DBObject obj = new BasicDBObject(UserdbKey.USER.SYSTEM_ACCOUNT, gte);
            DBCursor cursor = coll.find(obj);
            while (cursor.hasNext()) {
                DBObject dbO = cursor.next();
                String id = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
                result.add(id);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

//    public static List<String> getUserByOppositeGender(int gender) throws DaoException {
//        List<String> result = new ArrayList<>();
//        try {
//            DBObject obj = new BasicDBObject(UserdbKey.USER.SYSTEM_ACCOUNT, 0);
//            obj.put(UserdbKey.USER.GENDER, new BasicDBObject("$ne", gender));
//            DBCursor cursor = coll.find(obj);
//            if (cursor != null && cursor.size() > 0) {
//                while (cursor.hasNext()) {
//                    DBObject dbO = cursor.next();
//                    String id = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
//                    result.add(id);
//                }
//            }
//        } catch (Exception ex) {
//            Util.addErrorLog(ex);
//
//            throw new DaoException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }    
    public static boolean checkFemaleName(String userId, String username) throws EazyException {
        boolean result = true;
        try {
            DBObject findObj = new BasicDBObject(UserdbKey.USER.SORT_NAME, username.toLowerCase())
                    .append(UserdbKey.USER.GENDER, Constant.GENDER.FEMALE)
                    .append(UserdbKey.USER.ID, new BasicDBObject("$ne", new ObjectId(userId)));
            DBObject found = coll.findOne(findObj);
            if (found != null) {
                return false;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkUserName(String userId, String username) throws EazyException {
        boolean result = true;
        try {
            DBObject gte = new BasicDBObject("$gt", 0);
            DBObject obj = new BasicDBObject(UserdbKey.USER.SYSTEM_ACCOUNT, gte);
            DBCursor cursor = coll.find(obj);
            if (userId == null) {
                if (cursor != null && cursor.size() > 0) {
                    while (cursor.hasNext()) {
                        DBObject dbO = cursor.next();
                        String name = dbO.get(UserdbKey.USER.SORT_NAME).toString();
                        if (username != null && name.equals(username.toLowerCase())) {
                            return false;
                        }
                    }
                }
            } else if (cursor != null && cursor.size() > 0) {
                while (cursor.hasNext()) {
                    DBObject dbO = cursor.next();
                    String name = dbO.get(UserdbKey.USER.SORT_NAME).toString();
                    String id = dbO.get(UserdbKey.USER.ID).toString();
                    if (!id.equals(userId) && name.equals(username.toLowerCase())) {
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static User insertUser(User user) throws EazyException {
        User result = new User();
        String originalEmail = user.email;
        Util.addDebugLog("originalEmail try to register -------------> " + originalEmail);

        String email = Util.createEmailField(user.email, user.fbId).toLowerCase();
        user.email = email;
        Util.addDebugLog("Email try to register after createEmailField(user.email, user.fbId) -->" + email);
        try {
            //search by email
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.EMAIL, user.email);
            DBObject obj1 = coll.findOne(doc);
            if (obj1 != null) { // found email --> error
                Util.addDebugLog("Found Email ---> NOT Register");
                throw new EazyException(ErrorCode.EMAIL_REGISTED);
            } else { //not found --> register
                Util.addDebugLog("Not Found Email --->  Register");
                if (originalEmail != null) {
//                    if (!Util.validateEmail(originalEmail)) {
//                        throw new DaoException(ErrorCode.INVALID_EMAIL);
//                    }
                    String dbPass = user.password;
//                    if (dbPass == null || dbPass.isEmpty() || dbPass.length() < 6) {
//                        throw new DaoException(ErrorCode.INVALID_PASSWORD);
//                    }
                    user.sipPassword = dbPass;
                    byte[] b = md.digest(user.password.getBytes());
                    String uPass = Util.byteToString(b);
                    user.password = uPass;
                }
                UserDB inputUser = UserDB.getRegisterUserDB(user);
                DBObject obj = inputUser.toDBObject();
                coll.insert(obj, new WriteConcern(true));
                ObjectId uId = (ObjectId) obj.get(UserdbKey.USER.ID);
                inputUser.id = uId;
                result = inputUser.getRegisterUser();
                String sipPassword = (String) obj.get(UserdbKey.USER.SIP_PASSWORD);
                if (sipPassword == null) {
                    upadateSipPassword(result.userId, originalEmail, user.password);
                }
                upadateUserId(uId);

            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkSystemAccExist() throws EazyException {
        boolean result = false;
        try {
            DBObject gte = new BasicDBObject("$gt", Constant.SYSTEM_ACCOUNT.SYSTEM_SAFETY);
            DBObject obj = new BasicDBObject(UserdbKey.USER.SYSTEM_ACCOUNT, gte);
            DBCursor cursor = coll.find(obj);
            if (cursor != null && cursor.size() > 0) {
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static User getUserInfor(String userId) throws EazyException {
        User result = new User();
        if (userId != null || !userId.isEmpty()) {
            try {
                //search by id
                ObjectId id = new ObjectId(userId);
                BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

                //search command
                DBObject dbOject = coll.findOne(obj);
                UserDB user = UserDB.fromDBObject(dbOject);
                Util.addDebugLog("rateValue ================== " + user.rateValue);
                result = user.getUserInfor();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
        }
        return result;
    }

    public static String getAvatarId(String userId) throws EazyException {
        String result = null;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            //search command
            DBObject dbOject = coll.findOne(obj);
            if(dbOject != null)
                result = (String) dbOject.get("ava_id");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    //Linh add 2017/04/12
    public static Object getUserInfor(String userId, String param) throws EazyException {
        Object result = new Object();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            DBObject dbOject = coll.findOne(obj);
            result = dbOject.get(param);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean addRate(String userId, double rate, int rateNum) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.BACKSTAGE_RATE, rate);
            obj.append(UserdbKey.USER.RATE_NUMBER, rateNum);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static User getBackStageInfor(String userId) throws EazyException {
        User result = new User();
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            //search command
            DBObject dbOject = coll.findOne(obj);
            if (dbOject != null) {
                Integer backStageNum = (Integer) dbOject.get(UserdbKey.USER.BACKSTAGE_NUMBER);
                result.backstageNumber = backStageNum == null ? 0L : (long) backStageNum;
                Double rate = (Double) dbOject.get(UserdbKey.USER.BACKSTAGE_RATE);
                result.backStageRate = rate == null ? 0F : rate;
                Integer rateNum = (Integer) dbOject.get(UserdbKey.USER.RATE_NUMBER);
                result.rateNumber = rateNum == null ? 0 : rateNum;
                Object point = dbOject.get(UserdbKey.USER.POINT);
                result.point = point == null ? 0L : Long.parseLong(point.toString());
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static User updateUser(String userId, User afterUser) throws EazyException {
        User result = null;
        try {
            //serach object by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject beforeObj = new BasicDBObject(UserdbKey.USER.ID, id);

            //update object
            // ma hoa pass word
            if (afterUser.password != null) {
                byte[] b = md.digest(afterUser.password.getBytes());
                String uPass = Util.byteToString(b);
                afterUser.sipPassword = afterUser.password;
                afterUser.password = uPass;
            }

            UserDB updateObj = UserDB.getUpdateUserDB(afterUser);
            Util.addDebugLog("updateUser " + updateObj.site_id);

            DBObject afterObj = updateObj.toDBObject();

            //update command
            BasicDBObject command = new BasicDBObject("$set", afterObj);
            DBObject resultCommand = coll.findAndModify(beforeObj, null, null, false, command, true, false);
            UserDB respond = UserDB.fromDBObject(resultCommand);
            result = respond.getUserLogin();

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Boolean updateUserRate(String userId, List<Integer> rateList) throws EazyException {
        Boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);

            BasicDBObject data = new BasicDBObject();
            data.append(UserdbKey.USER.RATE_VALUE, rateList);

            coll.update(query, new BasicDBObject("$set", data));
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }

        return result;
    }

    public static User updateUserChangeType(String userId, User afterUser, int type) throws EazyException {
        User result = null;
        try {
            //serach object by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject beforeObj = new BasicDBObject(UserdbKey.USER.ID, id);

            //update object
            // ma hoa pass word
            if (afterUser.password != null) {
                byte[] b = md.digest(afterUser.password.getBytes());
                String uPass = Util.byteToString(b);
                afterUser.sipPassword = afterUser.password;
                afterUser.password = uPass;
            }

            UserDB updateObj = UserDB.getUpdateUserDB(afterUser);
            Util.addDebugLog("updateUser " + updateObj.site_id);

            DBObject afterObj = updateObj.toDBObject();
            afterObj.put("type", type);

            //update command
            BasicDBObject command = new BasicDBObject("$set", afterObj);
            DBObject resultCommand = coll.findAndModify(beforeObj, null, null, false, command, true, false);
            UserDB respond = UserDB.fromDBObject(resultCommand);
            result = respond.getUserLogin();

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void updateTextField(String userId, User user, int gender) throws EazyException {
        try {
            //serach object by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject beforeObj = new BasicDBObject(UserdbKey.USER.ID, id);

            BasicDBObject updateObj = new BasicDBObject();
            if (user.about != null && !user.about.isEmpty()) {
                updateObj.append(UserdbKey.USER.ABOUT, user.about);
            }
            if (gender == Constant.GENDER.FEMALE) {
                Female female = (Female) user;
                if (female.fetishs != null && !female.fetishs.isEmpty()) {
                    updateObj.append(UserdbKey.USER.FETISH, female.fetishs);
                }
                if (female.typeOfMan != null && !female.typeOfMan.isEmpty()) {
                    updateObj.append(UserdbKey.USER.TYPEOFMANS, female.typeOfMan);
                }
                if (female.hobby != null && !female.hobby.isEmpty()) {
                    updateObj.append(UserdbKey.USER.HOBBY, female.hobby);
                }
            } else {
                Male male = (Male) user;
                if (male.hobby != null && !male.hobby.isEmpty()) {
                    updateObj.append(UserdbKey.USER.HOBBY, male.hobby);
                }
            }
            //update command
            if (!updateObj.isEmpty()) {
                BasicDBObject command = new BasicDBObject("$set", updateObj);
                Util.addDebugLog("=========updateTextField:====" + command);
                coll.update(beforeObj, command);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void updateBlankTextField(String userId, User user, int gender) throws EazyException {
        try {
            //serach object by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject beforeObj = new BasicDBObject(UserdbKey.USER.ID, id);

            BasicDBObject updateObj = new BasicDBObject();
            if (user.about == null || user.about.isEmpty()) {
                updateObj.append(UserdbKey.USER.ABOUT, user.about);
            }
            if (gender == Constant.GENDER.FEMALE) {
                Female female = (Female) user;
                if (female.fetishs == null || female.fetishs.isEmpty()) {
                    updateObj.append(UserdbKey.USER.FETISH, female.fetishs);
                }
                if (female.typeOfMan == null || female.typeOfMan.isEmpty()) {
                    updateObj.append(UserdbKey.USER.TYPEOFMANS, female.typeOfMan);
                }
                if (female.hobby == null || female.hobby.isEmpty()) {
                    updateObj.append(UserdbKey.USER.HOBBY, female.hobby);
                }
            } else {
                Male male = (Male) user;
                if (male.hobby == null || male.hobby.isEmpty()) {
                    updateObj.append(UserdbKey.USER.HOBBY, male.hobby);
                }
            }
            //update command
            if (!updateObj.isEmpty()) {
                BasicDBObject command = new BasicDBObject("$set", updateObj);
                Util.addDebugLog("=========updateBlankTextField:====" + command);
                coll.update(beforeObj, command);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    private static int getNumberWithField(String userId, String fieldname) throws EazyException {
        int result = 0;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            Integer num = (Integer) obj.get(fieldname);
            if (num == null) {
                result = 0;
            } else {
                result = num;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    // Add LongLT 26Aug2016
    private static Long getLongField(String userId, String fieldname) throws EazyException {
        Long result = 0L;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            Long num = (Long) obj.get(fieldname);
            if (num == null) {
                result = 0L;
            } else {
                result = num;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getFavoristedNumber(String userId) throws EazyException {
        return getNumberWithField(userId, UserdbKey.USER.FAVOURITED_NUMBER);
    }

    public static int getBuzzNumber(String userId) throws EazyException {
        return getNumberWithField(userId, UserdbKey.USER.BUZZ_NUMBER);
    }

    public static int getGiftNumber(String userId) throws EazyException {
        return getNumberWithField(userId, UserdbKey.USER.GIFT_NUMBER);
    }

    public static int getFlag(String userId) throws EazyException {
        return getNumberWithField(userId, UserdbKey.USER.FLAG);
    }

    public static Integer getFlagByEmail(String email, int type) throws EazyException {
        int result = 0;
        try {
            BasicDBObject updateQuery;
            if (type == Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE) {
                updateQuery = new BasicDBObject(UserdbKey.USER.EMAIL, email);
            } else if (type == Constant.ACCOUNT_TYPE_VALUE.FB_TYPE) {
                updateQuery = new BasicDBObject(UserdbKey.USER.FB_ID, email);
            } else if (type == Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE) {
                updateQuery = new BasicDBObject(UserdbKey.USER.MOCOM_ID, email);
            } else {
                updateQuery = new BasicDBObject(UserdbKey.USER.FAMU_ID, email);
            }
            DBObject obj = coll.findOne(updateQuery);
            if (obj != null) {
                Integer flag = (Integer) obj.get(UserdbKey.USER.FLAG);
                if (flag == null) {
                    result = Constant.USER_STATUS_FLAG.ACTIVE;
                } else {
                    result = flag;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getCMCode(String userId) throws EazyException {
        String result = null;
        try {

            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if (obj != null) {
                result = (String) obj.get(UserdbKey.USER.CM_CODE);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<GetUserByRegisterDateData> getUsersByRegisterTime(String startTime, String endTime) throws EazyException {
        List<GetUserByRegisterDateData> result = new ArrayList<>();
        try {
            BasicDBList ands = new BasicDBList();
            DBObject findObject = new BasicDBObject();
            BasicDBObject objGt = new BasicDBObject();
            BasicDBObject objLt = new BasicDBObject();
            if (startTime != null && endTime != null) {
                objGt.append("$gt", startTime);
                objLt.append("$lt", endTime);
                ands.add(new BasicDBObject(UserdbKey.USER.REGISTER_DATE, objGt));
                ands.add(new BasicDBObject(UserdbKey.USER.REGISTER_DATE, objLt));
                findObject.put("$and", ands);
            } else {
                if (startTime != null) {
                    objGt.append("$gt", startTime);
                    findObject.put(UserdbKey.USER.REGISTER_DATE, objGt);
                }
                if (endTime != null) {
                    objLt.append("$lt", endTime);
                    findObject.put(UserdbKey.USER.REGISTER_DATE, objLt);
                }
            }
            DBCursor cursor = coll.find(findObject);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String userId = obj.get(UserdbKey.USER.ID).toString();
                String cmCode = (String) obj.get(UserdbKey.USER.CM_CODE);
                result.add(new GetUserByRegisterDateData(userId, cmCode));
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getUpdateEmailFlag(String userId) throws EazyException {
        return getNumberWithField(userId, UserdbKey.USER.UPDATE_EMAIL_FLAG);
    }

    public static int getRegisterFlag(String userId) throws EazyException {
        return getNumberWithField(userId, UserdbKey.USER.FINISH_REGISTER_FLAG);
    }

    public static List<Integer> getUpdateInfoFlags(String userId) throws EazyException {
        List<Integer> list = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(userId));
            DBObject found = coll.findOne(findObj);
            if (found != null) {
                Integer updateEmailFlag = (Integer) found.get(UserdbKey.USER.UPDATE_EMAIL_FLAG);
                if (updateEmailFlag == null) {
                    updateEmailFlag = 0;
                }
                Integer finishRegistFlag = (Integer) found.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                Object verificationFlag = found.get(UserdbKey.USER.VERIFICATION_FLAG);
                list.add(updateEmailFlag);
                list.add(finishRegistFlag);
                if (verificationFlag != null) {
                    list.add((Integer) verificationFlag);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return list;
    }

    public static int getBackStageNumber(String userId) throws EazyException {
        return getNumberWithField(userId, UserdbKey.USER.BACKSTAGE_NUMBER);
    }

    public static boolean increaseFieldList(String userId, String fieldname, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(fieldname, num);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean decreaseFieldList(String userId, String fieldname, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER.ID, id);
            //update command
            DBObject obD = coll.findOne(updateQuery);
            Integer point = 0;
            if (obD != null) {
                Object obj = obD.get(fieldname);
                point = obj == null ? 0 : Integer.parseInt(obj.toString());
            }
            if (num > point) {
                num = point;
            }
            BasicDBObject obj = new BasicDBObject(fieldname, (0 - num));
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<User> getRequestList(List<String> friendIdList) throws EazyException {
        List<User> result = new ArrayList<>();
        try {
            //command to take profile of friend
            List<ObjectId> listId = new ArrayList<>();
            for (int i = 0; i < friendIdList.size(); i++) {
                listId.add(new ObjectId(friendIdList.get(i)));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBCursor friendCursor = coll.find(query);
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                Integer flag = (Integer) dbObject.get(UserdbKey.USER.FLAG);
                if (flag == null || flag == Constant.FLAG.ON) {
                    UserDB userDB = UserDB.fromDBObject(dbObject);
                    User user = userDB.getFriendInfor();
                    result.add(user);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<User> getListUser(List<String> listFriendId) throws EazyException {
        List<User> result = new ArrayList<>();
        try {
            //command to take profile of friend
            List<ObjectId> listId = new ArrayList<>();
            for (String listFriendId1 : listFriendId) {
                listId.add(new ObjectId(listFriendId1));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBObject sortObject = new BasicDBObject(UserdbKey.USER.SORT_NAME, 1);
            DBCursor friendCursor = coll.find(query).sort(sortObject);
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                Integer flag = (Integer) dbObject.get(UserdbKey.USER.FLAG);
                if (flag == null || flag == Constant.FLAG.ON) {
                    UserDB friend = UserDB.fromDBObject(dbObject);
                    result.add(friend.getFriendInfor());
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, String> getListName(List<String> listUserId) throws EazyException {
        Map<String, String> result = new TreeMap<>();
        try {
            List<ObjectId> listId = new ArrayList<>();
            for (String uId : listUserId) {
                listId.add(new ObjectId(uId));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBObject sortObject = new BasicDBObject(UserdbKey.USER.SORT_NAME, 1);
            DBCursor cursor = coll.find(query).sort(sortObject);

            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                String userId = (String) dbObject.get(UserdbKey.USER.USER_ID);
                String userName = (String) dbObject.get(UserdbKey.USER.USERNAME);
                result.put(userId, userName);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void getListFollowingUser(List<String> listFriendId, Map<String, FollowingUser> map) throws EazyException {
//        List<FollowingUser> result = new ArrayList<>();
        try {
            //command to take profile of friend
            List<ObjectId> listId = new ArrayList<>();
            for (String listFriendId1 : listFriendId) {
                listId.add(new ObjectId(listFriendId1));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBCursor friendCursor = coll.find(query);
//            System.out.println("cursor :" + friendCursor.size());
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                Integer flag = (Integer) dbObject.get(UserdbKey.USER.FLAG);
                if (flag == null || flag == Constant.FLAG.ON) {
                    String userId = ((ObjectId) dbObject.get(UserdbKey.USER.ID)).toString();
                    FollowingUser friend = map.get(userId);
                    friend.avatarId = (String) dbObject.get(UserdbKey.USER.AVATAR_ID);
                    friend.userName = (String) dbObject.get(UserdbKey.USER.USERNAME);
                    friend.gender = (Integer) dbObject.get(UserdbKey.USER.GENDER);
                    String bir = (String) dbObject.get(UserdbKey.USER.BIRTHDAY);
                    friend.age = Util.convertBirthdayToAge(bir);
                    friend.region = (Integer) dbObject.get(UserdbKey.USER.REGION);
                    friend.job = (Integer) dbObject.get(UserdbKey.USER.JOB);
//                    result.add(friend);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
//        return result;
    }

    public static List<String> getUserActivate(List<String> listFriendId) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            //command to take profile of friend
            List<ObjectId> listId = new ArrayList<>();
            for (String listFriendId1 : listFriendId) {
                listId.add(new ObjectId(listFriendId1));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBObject sortObject = new BasicDBObject(UserdbKey.USER.SORT_NAME, 1);
            DBCursor friendCursor = coll.find(query).sort(sortObject);
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                Integer flag = (Integer) dbObject.get(UserdbKey.USER.FLAG);
                if (flag == null || flag == Constant.FLAG.ON) {
                    ObjectId userId = (ObjectId) dbObject.get(UserdbKey.USER.ID);
                    result.add(userId.toString());
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<User> getMapCheckOutUser(Map<String, String> mapFriendId) throws EazyException {
        List<User> result = new ArrayList<>();
        try {
            List<ObjectId> listUserId = new ArrayList<>();
            for (Map.Entry entry : mapFriendId.entrySet()) {
                listUserId.add(new ObjectId(entry.getKey().toString()));
            }
            //command to take profile of friend
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listUserId).get();
            DBCursor friendCursor = coll.find(query);
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                Integer flag = (Integer) dbObject.get(UserdbKey.USER.FLAG);
                if (flag == null || flag == Constant.FLAG.ON) {
                    UserDB userDB = UserDB.fromDBObject(dbObject);
                    User user = userDB.getFriendInfor();
                    String userId = user.userId;
                    String checkTime = mapFriendId.get(userId);
                    user.checkTime = checkTime;
                    result.add(user);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<User> getListFriend(List<String> listFriendId) throws EazyException {
        List<User> result = new ArrayList<>();
        try {
            //command to take profile of friend
            List<ObjectId> listUserId = new ArrayList<>();
            for (String listFriendId1 : listFriendId) {
                listUserId.add(new ObjectId(listFriendId1));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listUserId).get();
            DBObject sortObject = new BasicDBObject(UserdbKey.USER.SORT_NAME, 1);
            DBCursor friendCursor = coll.find(query).sort(sortObject);
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                Integer flag = (Integer) dbObject.get(UserdbKey.USER.FLAG);
                if (flag == null || flag == Constant.FLAG.ON) {
                    UserDB friend = UserDB.fromDBObject(dbObject);
                    User user = friend.getFriendInfor();
                    result.add(user);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<User> searchByName(String name) throws EazyException {
        List<User> result = new ArrayList<>();
        try {
            for (String str : Constant.SPECIAL_CHARACTER) {
                if (name.contains(str)) {
                    String string = "\\" + str;
                    name = name.replace(str, string);
                }
            }
            String[] list = name.trim().split("\\s+");
            //abc| cde
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.length; i++) {
                sb.append(list[i].toLowerCase());
                if (i < (list.length - 1)) {
                    sb.append("|");
                }
            }
            //sb= a|b|c
            BasicDBObject regex = new BasicDBObject("$regex", sb.toString());
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.SORT_NAME, regex);
            
            DBObject sortObject = new BasicDBObject(UserdbKey.USER.SORT_NAME, 1);
            DBCursor friendCursor = coll.find(query).sort(sortObject);
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                Integer systemAccount = (Integer) dbObject.get(UserdbKey.USER.SYSTEM_ACCOUNT);
                if (systemAccount != null && systemAccount > 0) {
                    continue;
                }

                Integer flag = (Integer) dbObject.get(UserdbKey.USER.FLAG);
                Integer finishRegistFlag = (Integer) dbObject.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
//                Object verificationFlag = dbObject.get(UserdbKey.USER.VERIFICATION_FLAG);
                if ((flag == null || flag == Constant.FLAG.ON)
                        && (finishRegistFlag == null || finishRegistFlag == Constant.FLAG.ON)) {
//                        && (verificationFlag == null || Integer.parseInt(verificationFlag.toString()) == Constant.APPROVED)) {
                    UserDB friend = UserDB.fromDBObject(dbObject);
                    User user = friend.getFriendInfor();
                    result.add(user);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean addReport(String userId) throws EazyException {
        return increaseFieldList(userId, UserdbKey.USER.REPORT_NUMBER, 1);
    }

    public static boolean addBuzz(String userId) throws EazyException {
        return increaseFieldList(userId, UserdbKey.USER.BUZZ_NUMBER, 1);
    }

    public static boolean removeBuzz(String userId) throws EazyException {
        return decreaseFieldList(userId, UserdbKey.USER.BUZZ_NUMBER, 1);
    }

    public static boolean addGift(String userId) throws EazyException {
        return increaseFieldList(userId, UserdbKey.USER.GIFT_NUMBER, 1);
    }

    public static boolean removeGift(String userId) throws EazyException {
        return decreaseFieldList(userId, UserdbKey.USER.GIFT_NUMBER, 1);
    }

    public static boolean addFavorist(String userId) throws EazyException {
        return increaseFieldList(userId, UserdbKey.USER.FAVOURIST_NUMBER, 1);
    }

    public static boolean removeFavourist(String userId) throws EazyException {
        return decreaseFieldList(userId, UserdbKey.USER.FAVOURIST_NUMBER, 1);
    }

    public static boolean addFavorited(String userId) throws EazyException {
        return increaseFieldList(userId, UserdbKey.USER.FAVOURITED_NUMBER, 1);
    }

    public static boolean removeFavourited(String userId) throws EazyException {
        return decreaseFieldList(userId, UserdbKey.USER.FAVOURITED_NUMBER, 1);
    }

    public static boolean addPbImage(String userId) throws EazyException {
        return increaseFieldList(userId, UserdbKey.USER.PB_IMAGE_NUMBER, 1);
    }

    public static boolean removePbImage(String userId) throws EazyException {
        return decreaseFieldList(userId, UserdbKey.USER.PB_IMAGE_NUMBER, 1);
    }

    public static boolean removePbAudio(String userId) throws EazyException {
        return decreaseFieldList(userId, UserdbKey.USER.PB_AUDIO_NUMBER, 1);
    }

    public static boolean removePbVideo(String userId) throws EazyException {
        return decreaseFieldList(userId, UserdbKey.USER.PB_VIDEO_NUMBER, 1);
    }

    public static boolean addBackstage(String userId) throws EazyException {
        return increaseFieldList(userId, UserdbKey.USER.BACKSTAGE_NUMBER, 1);
    }

    public static boolean removeBackstage(String userId) throws EazyException {
        return decreaseFieldList(userId, UserdbKey.USER.BACKSTAGE_NUMBER, 1);
    }

    public static boolean addPbVideo(String userId) throws EazyException {
        return increaseFieldList(userId, UserdbKey.USER.PB_VIDEO_NUMBER, 1);
    }

    public static boolean addPbAudio(String userId) throws EazyException {
        return increaseFieldList(userId, UserdbKey.USER.PB_AUDIO_NUMBER, 1);
    }

    /**
     * increase point of user = currentPoint + point
     *
     * @param userId
     * @param point : plus point (can negative or positive)
     * @throws EazyException
     */
    public static void changePoint(String userId, int point) throws EazyException {
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject ob = coll.findOne(doc);
            Integer curPoint = (Integer) ob.get(UserdbKey.USER.POINT);
            if (curPoint == null) {
                curPoint = 0;
            }
            int plusPoint = point;

            if (plusPoint < 0 && plusPoint + curPoint < 0) {
                plusPoint = 0 - curPoint;
            }

            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.POINT, plusPoint);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(doc, updateCommand);
//            Integer afterPoint = (Integer) res.get(UserdbKey.USER.POINT);
//            result = new Point(curPoint, afterPoint);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
//        return result;
    }

    //ThanhDD 04/10/2016 #4311
    public static Map<String, String> getUserName(List<String> listFriendId) throws EazyException {
        Map<String, String> result = new TreeMap<>();
        try {
            //command to take profile of friend
            List<ObjectId> listId = new ArrayList<>();
            for (String friend : listFriendId) {
                listId.add(new ObjectId(friend));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBObject sortObject = new BasicDBObject(UserdbKey.USER.SORT_NAME, 1);
            DBCursor friendCursor = coll.find(query).sort(sortObject);
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                String userId = dbObject.get(UserdbKey.USER.ID).toString();
                String userName = (String) dbObject.get(UserdbKey.USER.USERNAME);
                result.put(userId, userName);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean changeEmailPassword(String userId, String email, String oldPassword, String newPassword, String originalPassword, Date time) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            // command search
            DBObject dboject = coll.findOne(obj);
            if (dboject != null) {
                Integer flag = (Integer) dboject.get(UserdbKey.USER.FLAG);
                if (flag != null && flag == Constant.USER_STATUS_FLAG.DISABLE) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
//                Integer veriFlag = (Integer) dboject.get(UserdbKey.USER.VERIFICATION_FLAG);
//                if (veriFlag != null && veriFlag == Constant.NO) {
//                    throw new DaoException(ErrorCode.LOCKED_USER);
//                }
                Integer updateEmailFlag = (Integer) dboject.get(UserdbKey.USER.UPDATE_EMAIL_FLAG);
                if (updateEmailFlag != null && updateEmailFlag == Constant.FLAG.OFF) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
                Integer finishRegister = (Integer) dboject.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                if (finishRegister != null && finishRegister == Constant.FLAG.OFF) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
            }
//            String dbPass = (String) dboject.get(UserdbKey.USER.PASSWORD);
            String dbPass = (String) dboject.get(UserdbKey.USER.SIP_PASSWORD);
            Integer updateEmailFlag = (Integer)dboject.get(UserdbKey.USER.UPDATE_EMAIL_FLAG);
            //encode to md5
//            byte[] b = md.digest(oldPassword.getBytes());
//            String uPass = Util.byteToString(b);
            String encryptednPassword = null;
            Util.addDebugLog("dbPass--------------------------------------" + dbPass);
            Util.addDebugLog("oldPassword--------------------------------------" + oldPassword);
            if (dbPass.equals(oldPassword)) { // correct password
                if (newPassword != null) {
                    if (newPassword.length() < 6 || originalPassword == null || originalPassword.length() < 6) {
                        throw new EazyException(ErrorCode.INVALID_PASSWORD);
                    }
                    byte[] bt = md.digest(newPassword.getBytes());
                    encryptednPassword = Util.byteToString(bt);
                }
                BasicDBObject changePass = new BasicDBObject();
                if (email != null) {
                    changePass.append(UserdbKey.USER.EMAIL, email.toLowerCase());
                }
                if (newPassword != null) {
                    changePass.append(UserdbKey.USER.SIP_PASSWORD, newPassword);
                    changePass.append(UserdbKey.USER.ORIGINAL_PASSOWRD, originalPassword);
                    changePass.append(UserdbKey.USER.PASSWORD, encryptednPassword);
                }
                if(updateEmailFlag == null || updateEmailFlag == 0){
                    changePass.append(UserdbKey.USER.UPDATE_EMAIL_FLAG, Constant.FLAG.ON);
                }
                if (!changePass.isEmpty()) {
//                SimpleDateFormat sdf = new SimpleDateFormat(Con?tant.DATE_HOUR_FORMAT);                
                    changePass.append(UserdbKey.USER.LAST_UPDATE, DateFormat.format(time));
                    BasicDBObject updateObj = new BasicDBObject("$set", changePass);
                    coll.update(dboject, updateObj);
                }
               
                result = true;
            } else { // not correct password
                throw new EazyException(ErrorCode.INCORRECT_PASSWORD);
            }

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean changeEmailPassword_First(String userId, String email, String newPassword, String originalPassword, Date time) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            // command search
            DBObject dboject = coll.findOne(obj);

            if (dboject != null) {
                Integer flag = (Integer) dboject.get(UserdbKey.USER.FLAG);
                if (flag != null && flag == Constant.USER_STATUS_FLAG.DISABLE) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
//                Integer veriFlag = (Integer) dboject.get(UserdbKey.USER.VERIFICATION_FLAG);
//                if (veriFlag != null && veriFlag == Constant.NO) {
//                    throw new DaoException(ErrorCode.LOCKED_USER);
//                }

                Integer finishRegister = (Integer) dboject.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                if (finishRegister != null && finishRegister == Constant.FLAG.OFF) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
            }
            //encode to md5 for new pass
            String encryptednPassword = null;
            if (newPassword != null) {
                if (newPassword.length() < 6 || originalPassword == null || originalPassword.length() < 6) {
                    throw new EazyException(ErrorCode.INVALID_PASSWORD);
                }
                byte[] bt = md.digest(newPassword.getBytes());
                encryptednPassword = Util.byteToString(bt);
            } else {
                throw new EazyException(ErrorCode.INVALID_PASSWORD);
            }
            BasicDBObject changePass = new BasicDBObject(UserdbKey.USER.EMAIL, email.toLowerCase());
            if (newPassword != null && originalPassword != null) {
                changePass.append(UserdbKey.USER.SIP_PASSWORD, newPassword);
                changePass.append(UserdbKey.USER.ORIGINAL_PASSOWRD, originalPassword);
                changePass.append(UserdbKey.USER.PASSWORD, encryptednPassword);
            }
            changePass.append(UserdbKey.USER.UPDATE_EMAIL_FLAG, Constant.FLAG.ON);
//                SimpleDateFormat sdf = new SimpleDateFormat(Con?tant.DATE_HOUR_FORMAT);                
            changePass.append(UserdbKey.USER.LAST_UPDATE, DateFormat.format(time));
            BasicDBObject updateObj = new BasicDBObject("$set", changePass);
            coll.update(dboject, updateObj);
            result = true;

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean changePassword(String userId, String oldPassword, String newPassword, String originalPassword, Date time) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            // command search
            DBObject dboject = coll.findOne(obj);

            if (dboject != null) {
                Integer flag = (Integer) dboject.get(UserdbKey.USER.FLAG);
                if (flag != null && flag == Constant.USER_STATUS_FLAG.DISABLE) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
//                Integer veriFlag = (Integer) dboject.get(UserdbKey.USER.VERIFICATION_FLAG);
//                if (veriFlag != null && veriFlag == Constant.NO) {
//                    throw new DaoException(ErrorCode.LOCKED_USER);
//                }
                Integer updateEmailFlag = (Integer) dboject.get(UserdbKey.USER.UPDATE_EMAIL_FLAG);
                if (updateEmailFlag != null && updateEmailFlag == Constant.FLAG.OFF) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }

                Integer finishRegister = (Integer) dboject.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                if (finishRegister != null && finishRegister == Constant.FLAG.OFF) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
            }

            String dbPass = (String) dboject.get(UserdbKey.USER.PASSWORD);

            //encode to md5
            byte[] b = md.digest(oldPassword.getBytes());
            String uPass = Util.byteToString(b);

            if (dbPass.equals(uPass)) { // correct password
                if (newPassword == null || newPassword.length() < 6 || originalPassword == null || originalPassword.length() < 6) {
                    throw new EazyException(ErrorCode.INVALID_PASSWORD);
                }
                byte[] bt = md.digest(newPassword.getBytes());
                String encryptednPassword = Util.byteToString(bt);
                BasicDBObject changePass = new BasicDBObject(UserdbKey.USER.PASSWORD, encryptednPassword);
                changePass.append(UserdbKey.USER.SIP_PASSWORD, newPassword);
                changePass.append(UserdbKey.USER.ORIGINAL_PASSOWRD, originalPassword);
//                SimpleDateFormat sdf = new SimpleDateFormat(Con?tant.DATE_HOUR_FORMAT);                
                changePass.append(UserdbKey.USER.LAST_UPDATE, DateFormat.format(time));
                BasicDBObject updateObj = new BasicDBObject("$set", changePass);
                coll.update(dboject, updateObj);
                result = true;
            } else { // not correct password
                throw new EazyException(ErrorCode.INCORRECT_PASSWORD);
            }

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static User changePasswordInCaseForgot(String email, String newPassword, String originalPassword, Date time) throws EazyException {
        User result = new User();
        try {
            //search by email
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.EMAIL, email);
            DBObject checkObj = coll.findOne(obj);
            if (checkObj != null) {
                Integer flag = (Integer) checkObj.get(UserdbKey.USER.FLAG);
                if (flag != null && flag == Constant.USER_STATUS_FLAG.DISABLE) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
//                Integer veriFlag = (Integer) checkObj.get(UserdbKey.USER.VERIFICATION_FLAG);
//                if (veriFlag != null && veriFlag == Constant.NO) {
//                    throw new DaoException(ErrorCode.LOCKED_USER);
//                }
                Integer updateEmailFlag = (Integer) checkObj.get(UserdbKey.USER.UPDATE_EMAIL_FLAG);
                if (updateEmailFlag != null && updateEmailFlag == Constant.FLAG.OFF) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }

                Integer finishRegister = (Integer) checkObj.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                if (finishRegister != null && finishRegister == Constant.FLAG.OFF) {
                    throw new EazyException(ErrorCode.LOCKED_USER);
                }
            }
            //object update
            if (newPassword == null || newPassword.length() < 6 || originalPassword == null || originalPassword.length() < 6) {
                throw new EazyException(ErrorCode.INVALID_PASSWORD);
            }
            byte[] bt = md.digest(newPassword.getBytes());
            String encryptedPassword = Util.byteToString(bt);
            BasicDBObject changePass = new BasicDBObject(UserdbKey.USER.PASSWORD, encryptedPassword);
            changePass.append(UserdbKey.USER.SIP_PASSWORD, newPassword);
            changePass.append(UserdbKey.USER.ORIGINAL_PASSOWRD, originalPassword);
//            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_FORMAT);            
            changePass.append(UserdbKey.USER.LAST_UPDATE, DateFormat.format(time));
            BasicDBObject updateObj = new BasicDBObject("$set", changePass);

            DBObject resultCommand = coll.findAndModify(obj, null, null, false, updateObj, true, false);
            UserDB respond = UserDB.fromDBObject(resultCommand);
            result = respond.getUserLogin();
            Integer flag = (Integer) resultCommand.get(UserdbKey.USER.FLAG);
            if (flag == null || flag == Constant.FLAG.OFF) {
                result.isActiveUser = Constant.FLAG.ON;
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getAccountType(String userId) throws EazyException {
        int result = Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);

            DBObject obj = coll.findOne(doc);

            if (obj != null) { // found email
                result = (Integer) obj.get(UserdbKey.USER.ACCOUNT_TYPE);
            } else { //not found email
                throw new EazyException(ErrorCode.WRONG_DATA_FORMAT);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    private static boolean checkUniqueField(String field, String value, String userId) throws EazyException {
        boolean result = false;
        try {
            //search by email
            BasicDBObject doc = new BasicDBObject(field, value.toLowerCase());
            if (userId != null) {
                ObjectId id = new ObjectId(userId);
                BasicDBObject neObj = new BasicDBObject("$ne", id);
                doc.append(UserdbKey.USER.ID, neObj);
            }
            DBObject obj = coll.findOne(doc);
            result = obj != null;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    private static boolean checkUniqueFieldMultiapp(String field, String value, String userId, String applicationId) throws EazyException {
        boolean result = false;
        try {
            //search by email
            BasicDBObject doc = new BasicDBObject(field, value.toLowerCase());
            if (userId != null) {
                ObjectId id = new ObjectId(userId);
                BasicDBObject neObj = new BasicDBObject("$ne", id);
                doc.append(UserdbKey.USER.ID, neObj);
                doc.append(UserdbKey.USER.APPLICATION_ID, applicationId);
            }
            DBObject obj = coll.findOne(doc);
            result = obj != null;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkEmail(String email, String userId) throws EazyException {
        return checkUniqueField(UserdbKey.USER.EMAIL, email, userId);
    }

    //Add Muilapp #6374
    public static boolean checkEmailMultiapp(String email, String userId, String applicationId) throws EazyException {
        return checkUniqueFieldMultiapp(UserdbKey.USER.EMAIL, email, userId, applicationId);
    }

    public static boolean checkMocomId(String mocomId, String userId) throws EazyException {
        return checkUniqueField(UserdbKey.USER.MOCOM_ID, mocomId, userId);
    }

    public static boolean checkFacebookId(String facebookId, String userId) throws EazyException {
        return checkUniqueField(UserdbKey.USER.FB_ID, facebookId, userId);
    }

    public static boolean checkFamuId(String famuId, String userId) throws EazyException {
        return checkUniqueField(UserdbKey.USER.FAMU_ID, famuId, userId);
    }

    public static Long getNotificationReadTime(String userId) throws EazyException {
        Long result = null;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(doc);

            if (obj != null) { // found email
                Long time = (Long) obj.get(UserdbKey.USER.NOTIFICATION_READ_TIME);
                result = time;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Long getLikeNotificationReadTime(String userId) throws EazyException {
        Long result = null;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(doc);

            if (obj != null) { // found email
                Long time = (Long) obj.get(UserdbKey.USER.NOTIFICATION_LIKE_READ_TIME);
                result = time;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    // Add LongLT 25Aug2016
    public static Long getNewsNotificationReadTime(String userId) throws EazyException {
        return getLongField(userId, UserdbKey.USER.NOTIFICATION_NEWS_READ_TIME);
    }

    public static Long getQANotificationReadTime(String userId) throws EazyException {
        return getLongField(userId, UserdbKey.USER.NOTIFICATION_QA_READ_TIME);
    }

    public static String getUserName(String userId) throws EazyException {
        String result = null;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(doc);

            if (obj != null) { // found email
                String name = (String) obj.get(UserdbKey.USER.USERNAME);
                result = name;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Integer getRegion(String userId) throws EazyException {
        Integer result = null;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(doc);

            if (obj != null) { // found email
                Integer region = (Integer) obj.get(UserdbKey.USER.REGION);
                result = region;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkUser(String userId) throws EazyException {
        boolean result = false;
        try {
            //
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(doc);

            if (obj != null) { // found email
                Integer flag = (Integer) obj.get(UserdbKey.USER.FLAG);
                if (flag == null || flag == Constant.FLAG.ON) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isExists(String userId) throws EazyException {
        boolean result = false;
        try {
            //
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(doc);

            if (obj != null) { // found email
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getEmail(String userId) throws EazyException {
        String result = null;
        try {
            //
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(doc);

            if (obj != null) { // found email
                result = (String) obj.get(UserdbKey.USER.EMAIL);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeAvatar(String userId) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.AVATAR_ID, Constant.FLAG.ON);
            BasicDBObject command = new BasicDBObject("$unset", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkAvatar(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            DBObject obj = coll.findOne(query);
            String avatarId = (String) obj.get(UserdbKey.USER.AVATAR_ID);
            if (avatarId != null) {
                if (avatarId.equals(imageId)) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateAvatar(String userId, String imageId, Date time) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.AVATAR_ID, imageId);
//            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_FORMAT);
            addition.append(UserdbKey.USER.LAST_UPDATE, DateFormat.format(time));
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateCreaUserInfo(String userId, String creaId, String creaPass) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.CREA_ID, creaId);
            addition.append(UserdbKey.USER.CREA_PASS, creaPass);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);

            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateFinishRegister(String userId, int flag) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.FINISH_REGISTER_FLAG, flag);
            BasicDBObject command = new BasicDBObject("$set", addition);
            Util.addDebugLog("=========updateFinishRegister:====" + command);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateNotificationReadTime(String userid, long time) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userid);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.NOTIFICATION_READ_TIME, time);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateLikeNotificationReadTime(String userid, long time) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userid);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.NOTIFICATION_LIKE_READ_TIME, time);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    // Add LongLT 25Aug2016
    public static boolean updateNewsNotificationReadTime(String userid, long time) throws EazyException {
        return updateLongField(userid, UserdbKey.USER.NOTIFICATION_NEWS_READ_TIME, time);
    }

    // Linh add 2017/03/10
    public static boolean updateQANotificationReadTime(String userid, long time) throws EazyException {
        return updateLongField(userid, UserdbKey.USER.NOTIFICATION_QA_READ_TIME, time);
    }

    // Add LongLT 25Aug2016
    private static boolean updateLongField(String userid, String key, long value) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userid);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(key, value);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateCmCode(String userId, String cmCode) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.CM_CODE, cmCode);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateFlag(String userId, int flag) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.FLAG, flag);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateFlag2(String userId, int flag, String memo) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.FLAG, flag);
            addition.append(UserdbKey.USER.MEMO, memo);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updatePurchaseTime(String userId, Date purchaseTime) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
//            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_FORMAT);
            DBObject obj = coll.findOne(query);
            String firstPurchaseTime = (String) obj.get(UserdbKey.USER.FIRST_PURCHASE_TIME);
            String time = DateFormat.format(purchaseTime);
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.LAST_PURCHASE_TIME, time);
            if (firstPurchaseTime == null) {
                addition.append(UserdbKey.USER.FIRST_PURCHASE_TIME, time);
            }
            addition.append(UserdbKey.USER.HAVE_PURCHASE, true);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateIsPurchase(String userId, int isPurchase) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
//            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_FORMAT);
            DBObject obj = coll.findOne(query);

            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.IS_PURCHASE, isPurchase);

            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updatePurchaseTime(String userId, Date purchaseTime, boolean havePurchase) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
//            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_FORMAT);
            DBObject obj = coll.findOne(query);
            String firstPurchaseTime = (String) obj.get(UserdbKey.USER.FIRST_PURCHASE_TIME);
            String time = DateFormat.format(purchaseTime);
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.LAST_PURCHASE_TIME, time);
            if (firstPurchaseTime == null) {
                addition.append(UserdbKey.USER.FIRST_PURCHASE_TIME, time);
            }
            addition.append(UserdbKey.USER.HAVE_PURCHASE, havePurchase);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateVerificationCode(String email, String code) throws EazyException {
        boolean result = false;
        try {
            //search by email
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.EMAIL, email);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.VERIFICATION_CODE, code);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);

            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static User updateVerificationFlag(String userId, int flag) throws EazyException {
        User result = null;
        try {
            //search by email
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(userId));
            DBObject obj = coll.findOne(query);
            Integer gender = (Integer) obj.get(UserdbKey.USER.GENDER);
            if (gender != Constant.GENDER.FEMALE) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.VERIFICATION_FLAG, flag);
            BasicDBObject command = new BasicDBObject("$set", addition);
            DBObject resultCommand = coll.findAndModify(query, null, null, false, command, true, false);
            UserDB respond = UserDB.fromDBObject(resultCommand);
            result = respond.getUserLogin();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getDeactivate() throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(UserdbKey.USER.FLAG);
                if (flag != null && (flag == Constant.USER_STATUS_FLAG.DEACITIVE || flag == Constant.USER_STATUS_FLAG.DISABLE)) {
                    String userId = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
                    result.add(userId);
//                    continue;
                }
//                Integer gender = (Integer) dbO.get(UserdbKey.USER.GENDER);
//                Integer verifyFlag = (Integer) dbO.get(UserdbKey.USER.VERIFICATION_FLAG);
//                if(gender == Constant.FEMALE && verifyFlag != null && verifyFlag != Constant.APPROVED){
//                    String userId = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
//                    result.add(userId);
//                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateLoginTime(String userId, Date time) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            String timeStr = DateFormat.format(time);
            BasicDBObject setObj = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER.LAST_LOGIN_TIME, timeStr));
            coll.update(doc, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkVerificationCode(String email, String code) throws EazyException {
        boolean result = false;
        try {
            //search by email

            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.EMAIL, email);
            //command to search
            DBObject obj = coll.findOne(doc);
            if (obj == null) { //not found email
                throw new EazyException(ErrorCode.EMAIL_NOT_FOUND);
            }
            String dbCode = (String) obj.get(UserdbKey.USER.VERIFICATION_CODE);
            if (dbCode != null && dbCode.equals(code)) { //correct code
                result = true;
            } else { // wrong code
                throw new EazyException(ErrorCode.WRONG_VERIFICATION_CODE);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateCallSetting(String userId, boolean video_call, boolean voice_call) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject objFind = new BasicDBObject(UserdbKey.USER.ID, id);
            BasicDBObject objUpdate = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER.VIDEO_CALL_WAITING, video_call)
                    .append(UserdbKey.USER.VOICE_CALL_WAITING, voice_call));

            coll.update(objFind, objUpdate);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    //Linh add #5227 06/03/2017
    public static boolean updateCallSetting(String userId, boolean video_call, boolean voice_call, int call_request_setting) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject objFind = new BasicDBObject(UserdbKey.USER.ID, id);
            BasicDBObject objUpdate = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER.VIDEO_CALL_WAITING, video_call)
                    .append(UserdbKey.USER.VOICE_CALL_WAITING, voice_call)
                    .append(UserdbKey.USER.CALL_REQUEST_SETTING, call_request_setting));

            coll.update(objFind, objUpdate);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<IEntity> getListAboutbyListUserId(List<String> listUserId) throws EazyException {
        List<IEntity> result = new ArrayList<>();
        try {
            List<ObjectId> listId = new ArrayList<>();
            for (String listUserId1 : listUserId) {
                listId.add(new ObjectId(listUserId1));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                ObjectId userId = (ObjectId) obj.get(UserdbKey.USER.ID);
                String about = (String) obj.get(UserdbKey.USER.ABOUT);
                if (about != null) {
                    About abt = new About(userId.toString(), Util.replaceBannedWord(about));
                    result.add(abt);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    /**
     * initial point for user when system is the first run.
     *
     * @return
     */
    public static Map<String, UserInfor> initUserInfor() {
        Map<String, UserInfor> result = new ConcurrentHashMap<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject obj = cur.next();
                String userId = obj.get(UserdbKey.USER.ID).toString();
                try {
                    Integer point = 0;
                    Object pointObj = obj.get(UserdbKey.USER.POINT);
                    if (pointObj != null) {
                        point = (new Double(pointObj.toString()).intValue());
                    }
                    Integer gender = (Integer) obj.get(UserdbKey.USER.GENDER);
//                    if (point == null) {
//                        point = 0;
//                    }
                    Boolean havePurchase = (Boolean) obj.get(UserdbKey.USER.HAVE_PURCHASE);
                    havePurchase = havePurchase == null ? false : havePurchase;
                    UserInfor up = new UserInfor(userId, point, gender, havePurchase);
                    result.put(userId, up);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    System.out.println("userId : " + userId);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static void increatPoint(String userId, int point) throws EazyException {
        increaseFieldList(userId, UserdbKey.USER.POINT, point);
    }

    public static void updateNotNullStringParam(String userID, JSONObject obj, String... keys) throws EazyException {
        DBObject findObj = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(userID));
        DBObject query = new BasicDBObject();
        for (int i = 0; i < keys.length; i++) {
            String value = obj.get(keys[i]) != null ? obj.get(keys[i]).toString() : null;
            if (value != null) {
                if (ParamKey.ADID.equals(keys[i])) {
                    query.put(ParamKey.AD_ID, value);
                } else {
                    query.put(keys[i], value);
                }
            }
        }
        DBObject updateObj = new BasicDBObject("$set", query);
        coll.update(findObj, updateObj);
    }

    public static DBObject getUserById(String userId) {
        BasicDBObject findObject = new BasicDBObject();
        findObject.append(UserdbKey.USER.ID, new ObjectId(userId));
        DBObject result = coll.findOne(findObject);
        return result;
    }

    public static boolean isDeviceIdInUse(String deviceId) {
        DBObject findObj = new BasicDBObject(UserdbKey.USER.DEVICE_ID, deviceId);
        DBObject obj = coll.findOne(findObj);
        return obj != null;
    }

    public static void unsetThisDeviceId(String deviceId) {
        DBObject findObj = new BasicDBObject(UserdbKey.USER.DEVICE_ID, deviceId);
        DBObject query = new BasicDBObject(UserdbKey.USER.DEVICE_ID, "");
        DBObject updateObj = new BasicDBObject("$set", query);
        Util.addDebugLog("---------------- unset device id query:   " + findObj + " " + updateObj);
        coll.updateMulti(findObj, updateObj);
    }
    
    public static boolean checkFbIdAndEmail(String fbId,String userId) throws EazyException{
        Boolean result = false;
        DBObject find = new BasicDBObject("fb_id", fbId);
        find.put("pwd", new BasicDBObject("$exists",false));
        find.put("user_id", userId);
        DBObject query = coll.findOne(find);
        if(query != null){
            Integer flag = (Integer) query.get(UserdbKey.USER.FLAG);
            if (flag != null && flag == Constant.USER_STATUS_FLAG.DISABLE) {
                throw new EazyException(ErrorCode.LOCKED_USER);
            }
            result = true;
        }
        return result;
    }
    
    public static boolean addEmailAndPassForFbId(String userId,String email, String newPassword, String originalPassword, Date time) throws EazyException{
        Boolean result = false;
        ObjectId id = new ObjectId(userId);
        BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);
        DBObject dboject = coll.findOne(obj);
        String encryptednPassword = null;
        if (dboject != null) {
            if (newPassword != null) {
                if (newPassword.length() < 6 || originalPassword == null || originalPassword.length() < 6) {
                    throw new EazyException(ErrorCode.INVALID_PASSWORD);
                }
                byte[] bt = md.digest(newPassword.getBytes());
                encryptednPassword = Util.byteToString(bt);
            }
            BasicDBObject changePass = new BasicDBObject();
            if (email != null) {
                changePass.append(UserdbKey.USER.EMAIL, email.toLowerCase());
            }
            if (newPassword != null) {
                changePass.append(UserdbKey.USER.SIP_PASSWORD, newPassword);
                changePass.append(UserdbKey.USER.ORIGINAL_PASSOWRD, originalPassword);
                changePass.append(UserdbKey.USER.PASSWORD, encryptednPassword);
            }
            if (!changePass.isEmpty()) {
//                SimpleDateFormat sdf = new SimpleDateFormat(Con?tant.DATE_HOUR_FORMAT);                
                changePass.append(UserdbKey.USER.LAST_UPDATE, DateFormat.format(time));
                changePass.append(UserdbKey.USER.UPDATE_EMAIL_FLAG, 1);
                BasicDBObject updateObj = new BasicDBObject("$set", changePass);
                coll.update(dboject, updateObj);
            }
            result = true;
        }
        return result;
    }
}
