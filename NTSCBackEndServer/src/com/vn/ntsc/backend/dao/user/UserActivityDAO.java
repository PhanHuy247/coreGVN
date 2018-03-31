/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;

/**
 *
 * @author DuongLTD
 */
public class UserActivityDAO {

    private static DBCollection coll;

    static {
        try {
            DBManager.getUserDB().createCollection(UserdbKey.USER_ACTIVITY_COLLECTION, null);
            coll = DBManager.getUserDB().getCollection(UserdbKey.USER_ACTIVITY_COLLECTION);
            DBObject dbObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.LOCATION, "2d");
            coll.createIndex(dbObj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
           
        }
    }

    public static boolean removeStatus(String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.STATUS, 1);
            BasicDBObject unsetObj = new BasicDBObject("$unset", updateObj);
            coll.update(findObj, unsetObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateStatus(String userId, String status) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.STATUS, status);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj, true, false);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public boolean deactivate(String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.LOCATION, 1);
            BasicDBObject unsetObj = new BasicDBObject("$unset", updateObj);
            coll.update(findObj, unsetObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    private class UserObject implements Comparable<UserObject> {

        public String userId;
        public int index;

        public UserObject(String userId, int index) {
            super();
            this.userId = userId;
            this.index = index;
        }

        public int compareTo(UserObject obj) {
            //ascending order
            return this.userId.compareTo(obj.userId);
        }
    }

    private class StatusObject implements Comparable<StatusObject> {

        public String userId;
        public String status;

        public StatusObject(String userId, String status) {
            super();
            this.userId = userId;
            this.status = status;
        }

        public int compareTo(StatusObject obj) {
            //ascending order
            return this.userId.compareTo(obj.userId);
        }
    }

}
