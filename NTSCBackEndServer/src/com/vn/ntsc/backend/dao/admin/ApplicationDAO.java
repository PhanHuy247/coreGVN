/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.admin;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import com.vn.ntsc.backend.Config;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.Application;


/**
 *
 * @author HUNGDT
 */
public class ApplicationDAO {

    public static DB db;
    public static DBCollection coll;

    static {
        coll = DBManager.getSettingDB().getCollection(SettingdbKey.APPLICATION_COLLECTION);
    }

    public static String getUniqueNameById(String applicationId) {
//        if (!ObjectId.isValid(applicationId)) {
//            return null;
//        }
        int id = Integer.parseInt(applicationId);
        DBObject dbO = coll.findOne(new BasicDBObject(SettingdbKey.APPLICATION.APPLICATION_ID, id));
        if (dbO != null) {
            return dbO.get(SettingdbKey.APPLICATION.UNIQUE_NAME).toString();
        }
        return null;
    }

    public static String getIdByUniqueName(String uniqueName) {
        DBObject dbO = coll.findOne(new BasicDBObject(SettingdbKey.APPLICATION.UNIQUE_NAME, uniqueName));
        if (dbO != null) {
            return dbO.get(SettingdbKey.APPLICATION.ID).toString();
        }
        return null;
    }

    public static String insert(String displayName, String uniqueName, int order) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(SettingdbKey.APPLICATION.DISPLAY_NAME, displayName);
            obj.append(SettingdbKey.APPLICATION.UNIQUE_NAME, uniqueName);
            obj.append(SettingdbKey.APPLICATION.UNIQUE_NAME, uniqueName);
            obj.append(SettingdbKey.APPLICATION.ORDER, order);
            obj.append(SettingdbKey.APPLICATION.USER_NUMBER, 0);
            coll.insert(obj);
            result = obj.get(SettingdbKey.APPLICATION.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getMaxOrder() throws EazyException {
        Integer result = null;
        try {
            BasicDBObject sortObj = new BasicDBObject(SettingdbKey.APPLICATION.ORDER, -1);
            DBCursor cursor = coll.find().sort(sortObj);
            if (cursor.size() == 0) {
                return 0;
            } else {
                DBObject obj = cursor.next();
                result = (Integer) obj.get(SettingdbKey.APPLICATION.ORDER);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<Application> list() throws EazyException {
        List<Application> result = new ArrayList<>();
        try {
            BasicDBObject sortObj = new BasicDBObject(SettingdbKey.APPLICATION.ORDER, 1);
            DBCursor cursor = coll.find().sort(sortObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Application application = new Application();
                ObjectId id = (ObjectId) dbObject.get(SettingdbKey.APPLICATION.ID);
                application.id = id.toString();
                String displayName = (String) dbObject.get(SettingdbKey.APPLICATION.DISPLAY_NAME);
                application.displayName = displayName;
                String uniqueName = (String) dbObject.get(SettingdbKey.APPLICATION.UNIQUE_NAME);
                application.uniqueName = uniqueName;
                Integer numberOfUser = (Integer) dbObject.get(SettingdbKey.APPLICATION.USER_NUMBER);
                application.numberOfUser = numberOfUser;
                if (numberOfUser != null && numberOfUser == 0) {
                    application.canDelete = true;
                }
                result.add(application);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean delete(String giftId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(giftId);
            BasicDBObject obj = new BasicDBObject(SettingdbKey.APPLICATION.ID, id);
            coll.remove(obj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateOrder(String giftId, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(giftId);
            BasicDBObject updateQuery = new BasicDBObject(SettingdbKey.APPLICATION.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(SettingdbKey.APPLICATION.ORDER, num);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String applicationId, String displayName) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(applicationId);
            BasicDBObject searchObj = new BasicDBObject(SettingdbKey.APPLICATION.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(SettingdbKey.APPLICATION.DISPLAY_NAME, displayName);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(searchObj, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
