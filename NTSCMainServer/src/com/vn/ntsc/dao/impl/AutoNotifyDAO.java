/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author Duongltd
 */
public class AutoNotifyDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getSettingDB().getCollection(SettingdbKey.AUTO_MESSAGE.AUTO_NOTIFY_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean updateClickedUser(String id, String userId) throws EazyException {
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.ID, oId);
            DBObject addToSetObj = new BasicDBObject("$addToSet", new BasicDBObject(SettingdbKey.AUTO_MESSAGE.CLICKED_USERS, userId));
            coll.update(findObj, addToSetObj);
            return true;
        } catch (Exception ex) {
            throw new EazyException(ex);
        }
    }

}
