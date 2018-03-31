/*
 * To change this template, choose Tools | Templates
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
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author RuAc0n
 */
public class FavoristDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.FAVORIST_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
        }
    }

    public static boolean addFavorist(String userId, String friendId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORIST.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.FAVORIST.FAVOURIS_LIST, friendId);
            BasicDBObject updateCommand = new BasicDBObject("$push", obj);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeFavorist(String userId, String friendId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORIST.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.FAVORIST.FAVOURIS_LIST, friendId);
            BasicDBObject updateCommand = new BasicDBObject("$pull", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getFavouristList(String userId) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORIST.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList favoristList = (BasicDBList) respondObj.get(UserdbKey.FAVORIST.FAVOURIS_LIST);
                if (!favoristList.isEmpty()) {
                    for (Object favoristList1 : favoristList) {
                        result.add(favoristList1.toString());
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Long checkFavourist(String userId, String friendId) {
        long result = Constant.FLAG.OFF;
        if (userId == null || userId.isEmpty()){
            return result;
        }
        ObjectId id = new ObjectId(userId);
        BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORIST.ID, id);
        updateQuery.append(UserdbKey.FAVORIST.FAVOURIS_LIST, friendId);
        DBObject respondObj = coll.findOne(updateQuery);
        if (respondObj != null) {
            result = Constant.FLAG.ON;
        }
        return result;
    }

}
