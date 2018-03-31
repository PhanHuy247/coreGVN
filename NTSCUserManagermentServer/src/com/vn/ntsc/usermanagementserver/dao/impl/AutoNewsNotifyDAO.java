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
import eazycommon.backlist.DBManager;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author Administrator
 */
public class AutoNewsNotifyDAO {
    private static DBCollection coll;
    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.AUTO_MESSAGE.AUTO_NEWS_NOTIFY_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static void addClickedUser(String userId, String newId){
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.append("_id", new ObjectId(newId));
        BasicDBObject modifiedObject = new BasicDBObject();
        modifiedObject.put("$push", new BasicDBObject().append(SettingdbKey.AUTO_MESSAGE.CLICKED_USERS, userId));
        coll.update(searchObject, modifiedObject);
    }
    
    public static boolean checkIfExistClickedUser(String userId, String newId){
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.append("_id", new ObjectId(newId));
        DBObject resultObject = coll.findOne(searchObject);
        if (resultObject != null){
            Util.addDebugLog("Test from db "+resultObject.toString());
            BasicDBList clickedUsers = (BasicDBList) resultObject.get(SettingdbKey.AUTO_MESSAGE.CLICKED_USERS); 
            if(clickedUsers == null){
                return true;
            }
            else{
                for(int i=0; i< clickedUsers.size();i++){
                    if(clickedUsers.get(i).equals(userId)){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
