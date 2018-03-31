/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.dao.CommonDAO;
import org.bson.types.ObjectId;


/**
 *
 * @author HUNGDT
 */
public class ApplicationDAO {

    public static DB db;
    public static DBCollection coll;

    static {
        db = CommonDAO.mongo.getDB(SettingdbKey.DB_NAME);
        coll = db.getCollection(SettingdbKey.APPLICATION_COLLECTION);
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
}
