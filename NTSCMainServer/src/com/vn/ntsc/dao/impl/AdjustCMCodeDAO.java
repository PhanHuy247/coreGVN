/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CMcodedbKey;
import com.vn.ntsc.dao.DBLoader;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;

/**
 *
 * @author hungdt
 */
public class AdjustCMCodeDAO {

    private static DB db;
    private static DBCollection coll;

    static {
        try {
            //db = DAO.mongo.getDB(Config.CM_CODE_DB);
            //coll = db.getCollection(DBParamKey.ADJUST_CMCODE_COLLECTION);
            coll = DBLoader.getCmCodeDB().getCollection(CMcodedbKey.ADJUST_CMCODE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            //Future logging here

        }
    }

    public static String getTrackerId(String deviceId) {
        BasicDBObject query = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.DEVICEID, deviceId);
        DBCursor cursor = coll.find(query);
        String tracker_id = null;
        if (cursor.hasNext()) {
            BasicDBObject o = (BasicDBObject) cursor.next();
            tracker_id = (String) o.get(CMcodedbKey.ADJUST_CMCODE.TRACKERID);
        }
        cursor.close();
        return tracker_id;
    }

    public String getLabel(String deviceId) {
        BasicDBObject query = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.DEVICEID, deviceId);
        DBCursor cursor = coll.find(query);
        String tracker_id = null;
        if (cursor.hasNext()) {
            BasicDBObject o = (BasicDBObject) cursor.next();
            tracker_id = (String) o.get(CMcodedbKey.ADJUST_CMCODE.Label);
        }
        cursor.close();
        return tracker_id;
    }

    public String getInfo(String deviceId) {
        BasicDBObject query = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.DEVICEID, deviceId);
        DBCursor cursor = coll.find(query);
        String tracker_id = null;
        if (cursor.hasNext()) {
            BasicDBObject o = (BasicDBObject) cursor.next();
            tracker_id = (String) o.get(CMcodedbKey.ADJUST_CMCODE.DEVICEID) + "_" + (Integer) o.get(CMcodedbKey.ADJUST_CMCODE.FLAG);
        }
        cursor.close();
        return tracker_id;
    }

    public static void remove(String deviceId) {
        if (deviceId == null || deviceId.isEmpty()) {
            return;
        }
        BasicDBObject query = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.DEVICEID, deviceId);
        coll.remove(query);
    }

    public void remove(int flag) {
        BasicDBObject query = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.FLAG, flag);
        coll.remove(query);
    }

    public static boolean installTracker(String tracker_id, String deviceType, String deviceId, Date time, int flag, String label) throws EazyException {
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.DEVICEID, deviceId);
            DBObject obj = coll.findOne(findObj);

            if (obj != null) {
                Util.addInfoLog("remove " + deviceId);
                remove(deviceId);
            }
            Util.addInfoLog("insert " + deviceId);
            DBObject insertObj = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.DEVICEID, deviceId);
            insertObj.put(CMcodedbKey.ADJUST_CMCODE.Label, label);
            insertObj.put(CMcodedbKey.ADJUST_CMCODE.TRACKERID, tracker_id);
            insertObj.put(CMcodedbKey.ADJUST_CMCODE.DEVICETYPE, deviceType);
            insertObj.put(CMcodedbKey.ADJUST_CMCODE.TIME, time);
            insertObj.put(CMcodedbKey.ADJUST_CMCODE.FLAG, flag);

            coll.insert(insertObj);
            result = true;

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public String os_name(String deviceId) {
        BasicDBObject query = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.DEVICEID, deviceId);
        DBCursor cursor = coll.find(query);
        String os_name = null;
        if (cursor.hasNext()) {
            BasicDBObject o = (BasicDBObject) cursor.next();
            os_name = (String) o.get(CMcodedbKey.ADJUST_CMCODE.DEVICETYPE);
        }
        cursor.close();
        return os_name;
    }

    public static boolean updateFlag(String deviceId, int flag) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject find = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.DEVICEID, deviceId);
            BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.FLAG, flag));
            coll.update(find, set);
            result = true;
//            BasicDBObject query = new BasicDBObject(CMcodedbKey.ADJUST_CMCODE.DEVICEID, deviceId);
//            DBObject updateObject = coll.findOne(query);
//            if (updateObject != null) {
//                updateObject.put(CMcodedbKey.ADJUST_CMCODE.FLAG, flag);
//                BasicDBObject setObj = new BasicDBObject("$set", updateObject);
//                coll.update(updateObject, setObj);
//                AdjustCMCodeDAO a = new AdjustCMCodeDAO();
//                Util.addDebugLog("deviceId " + a.getInfo(deviceId));
//                result = true;
//            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
