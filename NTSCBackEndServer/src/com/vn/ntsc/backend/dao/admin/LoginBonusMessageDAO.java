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
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.automessage.Message;
import com.vn.ntsc.backend.entity.impl.automessage.extend.LoginBonusMessage;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;

/**
 *
 * @author HuyDX
 */
public class LoginBonusMessageDAO {
    private static DBCollection coll;
    private static final int DES = -1;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.LOGIN_BONUS_MESSAGE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static Map<String, Message> getAll(){
        Map<String, Message> all = new TreeMap<>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()){
                DBObject doc = cursor.next();
                LoginBonusMessage message = new LoginBonusMessage();
                message.id = doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.ID).toString();
                message.setLogin_bonus_times((Integer) doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.LOGIN_BONUS_TIMES));
                message.setMessage_type((String) doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.SENDER));
                message.setContent((String) doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.CONTENT));
                message.setGender((Integer)doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.GENDER));
                message.time = DateFormat.format_yyyyMMddHHmm(Util.currentTime());
                all.put(message.id, message);
            }
        } catch (Exception ex){
            Util.addErrorLog(ex);
        }
        return all;
    }
    
    public static String insert(LoginBonusMessage message) throws EazyException{
        String result = null;
        try{
            DBObject insertObject = new BasicDBObject();
            insertObject.put(SettingdbKey.LOGIN_BONUS_MESSAGE.LOGIN_BONUS_TIMES, message.getLogin_bonus_times());
            insertObject.put(SettingdbKey.LOGIN_BONUS_MESSAGE.SENDER, message.getMessage_type());
            insertObject.put(SettingdbKey.LOGIN_BONUS_MESSAGE.CONTENT, message.getContent());
            if (message.getGender()!=null)
                insertObject.put(SettingdbKey.LOGIN_BONUS_MESSAGE.GENDER, message.getGender());
            coll.insert(insertObject);
            result = insertObject.get(SettingdbKey.LOGIN_BONUS_MESSAGE.ID).toString();
        } catch (Exception ex){            
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);         
        }
        return result;
    }
    
    public static void update(LoginBonusMessage message){
        DBObject findObject = new BasicDBObject(SettingdbKey.LOGIN_BONUS_MESSAGE.ID, new ObjectId(message.id));
        DBObject updateField = new BasicDBObject();
        updateField.put(SettingdbKey.LOGIN_BONUS_MESSAGE.LOGIN_BONUS_TIMES, message.getLogin_bonus_times());
        updateField.put(SettingdbKey.LOGIN_BONUS_MESSAGE.SENDER, message.getMessage_type());
        updateField.put(SettingdbKey.LOGIN_BONUS_MESSAGE.CONTENT, message.getContent());
        updateField.put(SettingdbKey.LOGIN_BONUS_MESSAGE.GENDER, message.getGender());
        DBObject updateObject = new BasicDBObject("$set", updateField);
        coll.update(findObject, updateObject);
        
        if (message.getGender()==null){
            DBObject unset = new BasicDBObject("$unset", new BasicDBObject(SettingdbKey.LOGIN_BONUS_MESSAGE.GENDER, 1));
            coll.update(findObject, unset);
        }
    }
    
    public static boolean updateReceivers (String id, List<String> receiverIds){
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(SettingdbKey.LOGIN_BONUS_MESSAGE.ID, new ObjectId(id));
            DBObject eachArrayObject = new BasicDBObject ("$each", receiverIds.toArray());
            DBObject addToSetObj = new BasicDBObject("$addToSet", new BasicDBObject(SettingdbKey.LOGIN_BONUS_MESSAGE.RECEIVERS, eachArrayObject));
            coll.update(findObj, addToSetObj, true, false);
            BasicDBList receivers = (BasicDBList) (coll.findOne(findObj).get(SettingdbKey.LOGIN_BONUS_MESSAGE.RECEIVERS));
            int receiver_count = receivers.size();
            coll.update(findObj, new BasicDBObject("$set", new BasicDBObject(SettingdbKey.LOGIN_BONUS_MESSAGE.RECEIVER_COUNT, receiver_count)));
            result = true;
        } catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static SizedListData list(int skip, int take){
        SizedListData result = null;
        List<LoginBonusMessage> loginBonusMessages = new ArrayList<>();
        int size = 0;
        try {
            DBCursor cursor = coll.find();
            cursor = cursor.skip(skip).limit(take);
            DBObject orderBy = new BasicDBObject(SettingdbKey.LOGIN_BONUS_MESSAGE.ID, DES);
            size = cursor.size();
            cursor.sort(orderBy);
            while (cursor.hasNext()){
                LoginBonusMessage message = new LoginBonusMessage();
                DBObject doc = cursor.next();
                message.id = doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.ID).toString();
                message.setLogin_bonus_times((Integer) doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.LOGIN_BONUS_TIMES));
                message.setMessage_type((String) doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.SENDER));
                message.setContent((String) doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.CONTENT));
                message.setGender((Integer)doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.GENDER));
                message.receiverNumber = (Integer)doc.get(SettingdbKey.LOGIN_BONUS_MESSAGE.RECEIVER_COUNT);
                loginBonusMessages.add(message);
            }
        } catch (Exception ex){
            Util.addErrorLog(ex);
        }
        result = new SizedListData(size, loginBonusMessages);
        return result;
    }
    
    public static void delete(String messageId){
        DBObject dbObject = new BasicDBObject(SettingdbKey.LOGIN_BONUS_MESSAGE.ID, new ObjectId(messageId));
        coll.remove(dbObject);
    }

    public static List<String> getReceivers(String messageId) {
        List<String> receiverIds = new ArrayList<>();
        try {
            DBObject findObject = new BasicDBObject(SettingdbKey.LOGIN_BONUS_MESSAGE.ID, new ObjectId(messageId));
            BasicDBList receivers = (BasicDBList) (coll.findOne(findObject).get(SettingdbKey.LOGIN_BONUS_MESSAGE.RECEIVERS));
            for (Object receiver:receivers){
                receiverIds.add(receiver.toString());
            }
        } catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return receiverIds;
    }
}
