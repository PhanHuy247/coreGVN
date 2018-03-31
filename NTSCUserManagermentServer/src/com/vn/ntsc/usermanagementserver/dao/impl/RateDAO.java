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
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author RuAc0n
 */
public class RateDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.RATE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addRate(String userId, String friendId, int ratePoint) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.RATE.ID, id);
            BasicDBObject rateObj = new BasicDBObject(UserdbKey.RATE.RATE_ID, friendId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", rateObj);
            findObj.append(UserdbKey.RATE.RATE_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                Date d = new Date();
                String field = UserdbKey.RATE.RATE_LIST + ".$." + UserdbKey.RATE.RATE_POINT;
                BasicDBObject updateObj = new BasicDBObject(field, ratePoint);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            } else {
                BasicDBObject updateQuery = new BasicDBObject(UserdbKey.RATE.ID, id);
                BasicDBObject checkerElement = new BasicDBObject(UserdbKey.RATE.RATE_ID, friendId);
                checkerElement.append(UserdbKey.RATE.RATE_POINT, ratePoint);
                BasicDBObject rate = new BasicDBObject(UserdbKey.RATE.RATE_LIST, checkerElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", rate);
                coll.update(updateQuery, updateCommand, true, false);
            }
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getRatePoint(String userId, String friendId) throws EazyException {
        int result = 0;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.RATE.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList rateList = (BasicDBList) respondObj.get(UserdbKey.RATE.RATE_LIST);
                if (rateList != null && !rateList.isEmpty()) {
                    //get friend Id
                    for (int i = 0; i < rateList.size(); i++) {
                        BasicDBObject obj = (BasicDBObject) rateList.get(i);
                        String rateId = obj.getString(UserdbKey.RATE.RATE_ID);
                        if (rateId.equals(friendId)) {
                            Integer point = obj.getInt(UserdbKey.RATE.RATE_POINT);
                            result = point;
                            break;
                        }
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<Integer> getRateList(String userId) throws EazyException {
        List<Integer> result = new ArrayList<Integer>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.RATE.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList rateList = (BasicDBList) respondObj.get(UserdbKey.RATE.RATE_LIST);
                if (rateList != null && !rateList.isEmpty()) {
                    //get friend Id
                    for (int i = 0; i < rateList.size(); i++) {
                        BasicDBObject obj = (BasicDBObject) rateList.get(i);
                        Integer rate = obj.getInt(UserdbKey.RATE.RATE_POINT);
                        result.add(rate);
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
