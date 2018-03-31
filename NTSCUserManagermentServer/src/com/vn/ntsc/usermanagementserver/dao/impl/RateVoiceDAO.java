/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author hoangnh
 */
public class RateVoiceDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.RATE_VOICE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static List<Integer> getTopRate(String userId, Integer top){
        List<Integer> result = new ArrayList<>();
        try{
            BasicDBObject matchObj = new BasicDBObject();
            matchObj.append("$match", new BasicDBObject(UserdbKey.RATE_VOICE.REQUEST_ID, userId));
            
            
            BasicDBObject groupItem = new BasicDBObject();
            groupItem.append("_id", "$"+UserdbKey.RATE_VOICE.RATE_VALUE);
            groupItem.append("count", new BasicDBObject("$sum", 1));
            
            BasicDBObject groupObj = new BasicDBObject();
            groupObj.append("$group", groupItem);
            
            
            BasicDBObject sortObj = new BasicDBObject();
            sortObj.append("$sort", new BasicDBObject("count", -1));
            
            BasicDBObject limitObj = new BasicDBObject();
            limitObj.append("$limit", top);
            
            List<BasicDBObject> query = new ArrayList();
            query.add(matchObj);
            query.add(groupObj);
            query.add(sortObj);
            query.add(limitObj);
            
            AggregationOutput data = coll.aggregate(query);

            for (DBObject dbObject : data.results()) {
                Integer rateValue = (Integer) dbObject.get("_id");
                result.add(rateValue);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Integer getRatedValue(String userId, String friendId){
        Integer result = -1;
        try{
            if(userId != null && friendId != null){
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.RATE_VOICE.USER_ID, userId);
                findObj.append(UserdbKey.RATE_VOICE.REQUEST_ID, friendId);

                DBObject data = coll.findOne(findObj);
                if(data != null){
                    Integer rateValue = (Integer) data.get(UserdbKey.RATE_VOICE.RATE_VALUE);
                    result = rateValue;
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
