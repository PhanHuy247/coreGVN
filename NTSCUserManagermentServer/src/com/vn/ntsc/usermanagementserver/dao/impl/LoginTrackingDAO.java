/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author HuyDX
 */
public class LoginTrackingDAO {
    private static DBCollection coll;
    static {
        try {
            coll = DatabaseLoader.getSettingDB().getCollection(SettingdbKey.LOGIN_TRACKING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
//    public static boolean initialize(String userId){
//        boolean result = false;
//        try {
//            DBObject insertObject = new BasicDBObject(SettingdbKey.LOGIN_TRACKING.ID, new ObjectId(userId));
//            DBCursor cursor = coll.find(insertObject);
//            if (cursor.size()==0){
//                insertObject.put(SettingdbKey.LOGIN_TRACKING.LOGIN_BONUS_TIMES, 0);
//                coll.insert(insertObject);
//                result = true;
//            }
//        } catch (Exception ex){
//            Util.addErrorLog(ex);
//        }
//        return result;
//    }
    
    public static boolean increaseCount (String userId){
        Util.addInfoLog("increase count: " + userId);
        boolean result = false;
        try {
            DBObject findObject = new BasicDBObject(SettingdbKey.LOGIN_TRACKING.ID, new ObjectId(userId));
            DBObject increaseField = new BasicDBObject(SettingdbKey.LOGIN_TRACKING.LOGIN_BONUS_TIMES, 1);
            DBObject updateObject = new BasicDBObject("$inc", increaseField);
            coll.update(findObject, updateObject);
            result = true;
        } catch (Exception ex){
            Util.addErrorLog(ex);
        }
        
        return result;
    }

    public static boolean initialize(String userId, Integer gender){
        Util.addInfoLog("initialize: userId - " + userId + " gender = " + gender);
        boolean result = false;
        try {
            DBObject insertObject = new BasicDBObject(SettingdbKey.LOGIN_TRACKING.ID, new ObjectId(userId));
            DBCursor cursor = coll.find(insertObject);
            if (cursor.size()==0){
                insertObject.put(SettingdbKey.LOGIN_TRACKING.LOGIN_BONUS_TIMES, 0);
                insertObject.put(SettingdbKey.LOGIN_TRACKING.GENDER, gender);
                coll.insert(insertObject);
                result = true;
            }
        } catch (Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
