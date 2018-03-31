/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.mongokey.UserdbKey;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author duyetpt
 */
public class LastTimeGetDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.LAST_TIME_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
        }
    }

    public static void updateLastGetFavoristed(String userId, Long time, List<String> list) {
        BasicDBList dbList = new BasicDBList();
        if (list != null) {
            dbList.addAll(list);
        }
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.LAST_TIME.ID, id);
        DBObject foundObj = coll.findOne(findObj);
        if (foundObj != null) {
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.LAST_TIME.LAST_TIME_GET_FAVORITED_LIST, time);
            updateObj.append(UserdbKey.LAST_TIME.OLD_USER_FAVORITED_LIST, dbList);
            BasicDBObject update = new BasicDBObject("$set", updateObj);
            coll.update(findObj, update);
        } else {
            findObj.append(UserdbKey.LAST_TIME.LAST_TIME_GET_FAVORITED_LIST, time);
            findObj.append(UserdbKey.LAST_TIME.OLD_USER_FAVORITED_LIST, dbList);
            coll.insert(findObj);
        }
    }

    public static void updateLastGetCheckOut(String userId, Long time, List<String> list) {
        BasicDBList dbList = new BasicDBList();
        if (list != null) {
            dbList.addAll(list);
        }
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.LAST_TIME.ID, id);
        DBObject foundObj = coll.findOne(findObj);
        if (foundObj != null) {
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.LAST_TIME.LAST_TIME_GET_CHECKOUT_LIST, time);
            updateObj.append(UserdbKey.LAST_TIME.OLD_USER_CHECKOUT_LIST, dbList);
            BasicDBObject update = new BasicDBObject("$set", updateObj);
            coll.update(findObj, update);
        } else {
            findObj.append(UserdbKey.LAST_TIME.LAST_TIME_GET_CHECKOUT_LIST, time)
                    .append(UserdbKey.LAST_TIME.OLD_USER_CHECKOUT_LIST, dbList);
            coll.insert(findObj);
        }
    }

    public static Long getLastTimeFvt(String userId) {
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.LAST_TIME.ID, id);

        DBObject found = coll.findOne(findObj);
        if (found != null) {
            Long time = (Long) found.get(UserdbKey.LAST_TIME.LAST_TIME_GET_FAVORITED_LIST);
            return time;
        }
        return null;
    }

    public static Long getLastTimeCheckout(String userId) {
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.LAST_TIME.ID, id);

        DBObject found = coll.findOne(findObj);
        if (found != null) {
            Long time = (Long) found.get(UserdbKey.LAST_TIME.LAST_TIME_GET_CHECKOUT_LIST);
            return time;
        }
        return null;
    }

    public static List<String> getListCheckout(String userId) {
        List<String> list = new ArrayList<>();
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.LAST_TIME.ID, id);
        DBObject found = coll.findOne(findObj);
        if (found != null) {
            BasicDBList listObj = (BasicDBList) found.get(UserdbKey.LAST_TIME.OLD_USER_CHECKOUT_LIST);
            for (Object obj : listObj) {
                list.add((String) obj);
            }
            return list;
        }
        return null;
    }

    public static List<String> getListFavorited(String userId) {
        List<String> list = new ArrayList<>();
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.LAST_TIME.ID, id);
        DBObject found = coll.findOne(findObj);
        if (found != null) {
            BasicDBList listObj = (BasicDBList) found.get(UserdbKey.LAST_TIME.OLD_USER_FAVORITED_LIST);
            for (Object obj : listObj) {
                list.add((String) obj);
            }
            return list;
        }
        return null;
    }

}
