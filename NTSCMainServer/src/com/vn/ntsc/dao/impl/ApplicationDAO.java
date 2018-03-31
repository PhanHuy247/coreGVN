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
import eazycommon.constant.mongokey.SettingdbKey;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import com.vn.ntsc.Config;
import eazycommon.constant.mongokey.SettingdbKey;
import com.vn.ntsc.dao.DBLoader;


/**
 *
 * @author RuAc0n
 */
public class ApplicationDAO {

    private static DB db;
    private static DBCollection coll;

    static {
        coll = DBLoader.getSettingDB().getCollection(SettingdbKey.APPLICATION_COLLECTION);
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

    public static Map<String, Integer> getMapTotalUser() {
        Map<String, Integer> result = new HashMap<>();
        DBCursor cursor = coll.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            String unique = (String) obj.get(SettingdbKey.APPLICATION.UNIQUE_NAME);
            Integer totalUser = (Integer) obj.get(SettingdbKey.APPLICATION.USER_NUMBER);
            totalUser = totalUser == null ? 0 : totalUser;
            result.put(unique, totalUser);
        }
        return result;
    }

}
