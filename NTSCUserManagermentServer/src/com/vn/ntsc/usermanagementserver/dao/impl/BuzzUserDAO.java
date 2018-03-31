/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;

/**
 *
 * @author hoangnh
 */
public class BuzzUserDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getBuzzDB().getCollection(BuzzdbKey.USER_BUZZ_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static void updateFlagUserBuzz(String userId, String buzzId, Integer flag){
        try{
            ObjectId id = new ObjectId(userId);
            
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.USER_BUZZ.ID, id);
            findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"."+BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            
            BasicDBObject obj = new BasicDBObject();
            obj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST+".$."+BuzzdbKey.USER_BUZZ.FLAG, flag);
            
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append("$set", obj);
            
            coll.update(findObj, updateObj, true, false);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
}
