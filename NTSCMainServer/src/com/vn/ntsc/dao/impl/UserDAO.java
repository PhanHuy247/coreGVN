/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.impl.User;
import com.vn.ntsc.otherservice.entity.impl.UserDB;

/**
 *
 * @author DuongLTD
 */
public class UserDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getUserDB().getCollection(UserdbKey.USERS_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean checkUser(String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObject = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(findObject);
            if (obj != null) {
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int countMaleUser() throws EazyException {
        int result = 0;
        try {
            BasicDBObject findObject = new BasicDBObject(UserdbKey.USER.GENDER, Constant.GENDER.MALE);
            DBCursor cursor = coll.find(findObject);
            if (cursor != null) {
                result = cursor.count();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int countUser() throws EazyException {
        int result = 0;
        try {
            DBCursor cursor = coll.find(new BasicDBObject(UserdbKey.USER.SYSTEM_ACCOUNT, 0));
            if (cursor != null) {
                result = cursor.count();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int countFemaleUser() throws EazyException {
        int result = 0;
        try {
            BasicDBObject findObject = new BasicDBObject(UserdbKey.USER.GENDER, Constant.GENDER.FEMALE);
            DBCursor cursor = coll.find(findObject);
            if (cursor != null) {
                result = cursor.count();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, String> getListAboutbyListUserId(List<String> ids) {
        Map<String, String> result = new HashMap<>();
        try {
            long addListObjectIDStart = System.currentTimeMillis();
            List<ObjectId> oIds = new ArrayList<>();
            for (String id : ids) {
                oIds.add(new ObjectId(id));
            }
            long addListObjectIDEnd = System.currentTimeMillis();
            addListObjectIDEnd -= addListObjectIDStart;
            if (addListObjectIDEnd >= 1000) {
                Util.addInfoLog("DAO : getListAboutbyListUserId : add list ObjectId slow : " + addListObjectIDEnd);
            }

            long queryStart = System.currentTimeMillis();
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(oIds).get();
            DBCursor cursor = coll.find(query);
            long queryEnd = System.currentTimeMillis();
            queryEnd -= queryStart;
            if (queryEnd >= 1000) {
                Util.addInfoLog("DAO : getListAboutbyListUserId : query slow : " + queryEnd);
            }

            long cursorStart = System.currentTimeMillis();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String userId = obj.get(UserdbKey.USER.ID).toString();
                String about = (String) obj.get(UserdbKey.USER.ABOUT);
                if (about != null) {
                    result.put(userId, about);
                }
            }

            long cursorEnd = System.currentTimeMillis();
            cursorEnd -= cursorStart;
            if (cursorEnd >= 1000) {
                Util.addInfoLog("DAO : getListAboutbyListUserId : cursor slow : " + cursorEnd);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    //HUNGDT add 16-04-2016
    public static boolean updateCmCode(String userId, String cmCode) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            DBCursor cursor = coll.find(query);
            if (cursor != null && cursor.size() > 0) {
                while (cursor.hasNext()) {
                    DBObject dbO = cursor.next();
                    if (dbO.get(UserdbKey.USER.CM_CODE) != null) {
                        String code = dbO.get(UserdbKey.USER.CM_CODE).toString();
                    } else {
                        //command add verification code
                        BasicDBObject addition = new BasicDBObject(UserdbKey.USER.CM_CODE, cmCode);
                        BasicDBObject command = new BasicDBObject("$set", addition);
                        coll.update(query, command);
                    }
                }
            }

            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void updateOSName(String id, String osname) throws EazyException {
        BasicDBObject find = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(id));
        if (osname != null) {
            BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER.OS_NAME, osname));
            coll.update(find, set);
        }

    }

    public static void updateAdid(String id, String adid) throws EazyException {
        BasicDBObject find = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(id));
        if (adid != null) {
            BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER.AdId, adid));
            coll.update(find, set);
        }
    }

    public static void updateDeviceId(String id, String deviceid) throws EazyException {
        BasicDBObject find = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(id));
        if (deviceid != null) {
            BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER.DEVICE_ID, deviceid));
            coll.update(find, set);
        }
    }

    //END
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

    //HUNGDT add for #4415
    public static User getUserInfor(String userId) throws EazyException {
        User result = new User();
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            //search command
            DBObject dbOject = coll.findOne(obj);
            UserDB user = UserDB.fromDBObject(dbOject);
            result = user.getUserInfor();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static DBObject getUserInforJSON(String userId) throws EazyException {
        Util.addDebugLog("GET APP VER FROM TOKEN USER ID"+ userId);
        DBObject result = null;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            //search command
            DBObject dbObject = coll.findOne(obj);
            result = dbObject;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean increaseFieldInt(String userId, String fieldname, int num) throws EazyException {
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
    public static boolean increaseFieldDouble(String userId, String fieldname, double num) throws EazyException {
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

    public static Object getUserInfor(String userId, String param) throws EazyException {
        Object result = null;
        try {
            if(!ObjectId.isValid(userId)) return result;
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            DBObject dbOject = coll.findOne(obj);
            if (dbOject != null){
                result = dbOject.get(param);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static void unsetThisDeviceId(String deviceId) {
        DBObject findObj = new BasicDBObject(UserdbKey.USER.DEVICE_ID, deviceId);
        DBObject query = new BasicDBObject(UserdbKey.USER.DEVICE_ID, "");
        DBObject updateObj = new BasicDBObject("$set", query);
        coll.updateMulti(findObj, updateObj);
    }
}
