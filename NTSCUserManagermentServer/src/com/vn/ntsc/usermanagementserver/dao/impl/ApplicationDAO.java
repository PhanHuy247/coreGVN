/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.backlist.DBManager;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.Config;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;


/**
 *
 * @author HUNGDT
 */
public class ApplicationDAO {

    private static DBCollection coll;
    private static DB db;

    static {
        coll = DatabaseLoader.getSettingDB().getCollection(SettingdbKey.APPLICATION_COLLECTION);
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
        if (uniqueName != null) {
            DBObject dbO = coll.findOne(new BasicDBObject(SettingdbKey.APPLICATION.UNIQUE_NAME, uniqueName));
            if (dbO != null) {
                return dbO.get(SettingdbKey.APPLICATION.APPLICATION_ID).toString();
            }
        }
        return null;
    }

    public static String getDisplayNameById(String applicationId) {
        if (applicationId != null) {
            int id = Integer.parseInt(applicationId);
            DBObject dbO = coll.findOne(new BasicDBObject(SettingdbKey.APPLICATION.APPLICATION_ID, id));
            if (dbO != null) {
                return dbO.get(SettingdbKey.APPLICATION.DISPLAY_NAME).toString();
            }
        }
        return null;
    }

    public static List<String> getApplicationIds() {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String id = obj.get(SettingdbKey.APPLICATION.ID).toString();
                result.add(id);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static void insertUser(String applicationId) {
        try {
            int id = Integer.parseInt(applicationId);
            BasicDBObject findObject = new BasicDBObject(SettingdbKey.APPLICATION.APPLICATION_ID, id);
            BasicDBObject incObj = new BasicDBObject("$inc", new BasicDBObject(SettingdbKey.APPLICATION.USER_NUMBER, 1));
            coll.update(findObject, incObj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
}
