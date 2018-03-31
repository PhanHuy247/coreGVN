/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author HuyDX
 */
public class ContactTrackingDAO {
    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.CONTACT_TRACKING_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
        }
    }
    
    public static boolean update(String userId, String friendId) throws EazyException{
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(UserdbKey.CONTACT_TRACKING.USER_ID, userId);
            DBObject addToSetObj = new BasicDBObject("$addToSet", new BasicDBObject(UserdbKey.CONTACT_TRACKING.CONTACTED_USERS, friendId));
            coll.update(findObj, addToSetObj, true, false);
            result = true;
        }
        catch (Exception ex){
            throw new EazyException(ex);
        }
        return result;
    }
    
    public static List<String> getContactedUsers(String userId) throws EazyException{
        List<String> contactedUsers = new ArrayList<>();
        try {
            DBObject findObj = new BasicDBObject(UserdbKey.CONTACT_TRACKING.USER_ID, userId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBList contactedUserDBList = (BasicDBList) obj.get(UserdbKey.CONTACT_TRACKING.CONTACTED_USERS);
                if (contactedUserDBList != null){
                    for (Object contactedUserDBList1 : contactedUserDBList) {
                        contactedUsers.add((String) contactedUserDBList1);
                    }
                }
            }
        }
        catch (Exception ex){
            throw new EazyException(ex);
        }
        return contactedUsers;
    }
}
