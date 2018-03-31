/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.admin;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.automessage.LoginMessageTracking;

/**
 *
 * @author HuyDX
 */
public class LoginTrackingDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.LOGIN_TRACKING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }  
    public static List<String> getUserId(int count, String messageId, Integer gender){
        List<String> userIds = new ArrayList<>();
        
        try {
            DBObject findObj = new BasicDBObject(SettingdbKey.LOGIN_TRACKING.LOGIN_BONUS_TIMES, count);
            if (gender!=null)
                findObj.put(SettingdbKey.LOGIN_TRACKING.GENDER, gender);
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()){
                DBObject doc = cursor.next();
                List<String> messages = new ArrayList<>();
                    BasicDBList messageList = (BasicDBList) doc.get(SettingdbKey.LOGIN_TRACKING.MESSAGES_SENT);
                    if ( messageList!=null && !messageList.isEmpty()){
                        for (int i = 0; i < messageList.size(); i++) {
                            messages.add(messageList.get(i).toString());
                        }
                    }
                if (!messages.contains(messageId))
                    userIds.add(doc.get(SettingdbKey.LOGIN_TRACKING.ID).toString());
                }
        } catch (Exception ex){
            Util.addErrorLog(ex);
        }
        
        return userIds;
    }
    
    public static List<String> getUserIdByCount(int count, String messageId){
        List<String> userIds = new ArrayList<>();
        DBObject findObj = new BasicDBObject(SettingdbKey.LOGIN_TRACKING.LOGIN_BONUS_TIMES, count);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject doc = cursor.next();
            List<String> messages = new ArrayList<>();
                BasicDBList messageList = (BasicDBList) doc.get(SettingdbKey.LOGIN_TRACKING.MESSAGES_SENT);
                if (!messageList.isEmpty()){
                    for (int i = 0; i < messageList.size(); i++) {
                        messages.add(messageList.get(i).toString());
                    }
                }
            if (!messages.contains(messageId))
                userIds.add(doc.get(SettingdbKey.LOGIN_TRACKING.ID).toString());
            }
        return userIds;
    }
    public List<LoginMessageTracking> getAll(){
        List<LoginMessageTracking> tracks = new ArrayList<>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()){
                DBObject doc = cursor.next();
                LoginMessageTracking track = new LoginMessageTracking();
                track.setId(doc.get(SettingdbKey.LOGIN_TRACKING.ID).toString());
                track.setCount((Integer) doc.get(SettingdbKey.LOGIN_TRACKING.LOGIN_BONUS_TIMES));
                List<String> messages = new ArrayList<>();
                BasicDBList messageList = (BasicDBList) doc.get(SettingdbKey.LOGIN_TRACKING.MESSAGES_SENT);
                if (!messageList.isEmpty()){
                    for (int i = 0; i < messageList.size(); i++) {
                        messages.add(messageList.get(i).toString());
                    }
                }
                
                track.setMessages(messages);
                tracks.add(track);
            }
        } catch (Exception ex){
            Util.addErrorLog(ex);
        }
        return tracks;
        
    }

    public static boolean update(String id, String messageId) {
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(SettingdbKey.LOGIN_BONUS_MESSAGE.ID, new ObjectId(id));
            DBObject addToSetObj = new BasicDBObject("$addToSet", new BasicDBObject(SettingdbKey.LOGIN_TRACKING.MESSAGES_SENT, messageId));
            coll.update(findObj, addToSetObj, true, false);
            result = true;
        } catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    public static void updateMessageSent(String messageId, List<String> receivers) {
        for (String id: receivers){
            update(id, messageId);
        }
    }
}
