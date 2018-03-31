/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author hoangnh
 */
public class UserRateDAO {
    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.RATE_VOICE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
           
        }
    }
    
    public static boolean addRate(String userId, String requestId, Integer value) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject query = new BasicDBObject();
            query.append(UserdbKey.RATE_VOICE.USER_ID, userId);
            query.append(UserdbKey.RATE_VOICE.REQUEST_ID, requestId);
            
            BasicDBObject data = new BasicDBObject();
            data.append(UserdbKey.RATE_VOICE.USER_ID, userId);
            data.append(UserdbKey.RATE_VOICE.REQUEST_ID, requestId);
            data.append(UserdbKey.RATE_VOICE.RATE_VALUE, value);

            coll.update(query, new BasicDBObject("$set", data), true, false);
            
            result = true;
            
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<Integer> getUserRate(String userId){
        List<Integer> result = new ArrayList();
        try{
            BasicDBObject query = new BasicDBObject();
            query.append(UserdbKey.RATE_VOICE.REQUEST_ID, userId);
            result = coll.distinct(UserdbKey.RATE_VOICE.RATE_VALUE, query);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return result;
    }
}
