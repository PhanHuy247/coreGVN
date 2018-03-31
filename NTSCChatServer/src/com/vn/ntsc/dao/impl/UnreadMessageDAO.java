/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.HashMap;
import java.util.Map;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import org.bson.types.ObjectId;

public class UnreadMessageDAO {

    private static final String ObjectID = "_id";

    private static final String Unread_Message_Collection = "unread_message";
    private static final String Field_Unread_Message_List = "unread_message_list";
    private static final String Field_User_ID = "user_id";
    private static final String Field_Unread_Message_Number = "unread_msg_number";

    private static DB dbuser;
    private static DBCollection unreadMessageColl;

    static {
        try {
            dbuser = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            unreadMessageColl = dbuser.getCollection(Unread_Message_Collection);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static HashMap<String, HashMap<String, Integer>> getAll() {
        HashMap<String, HashMap<String, Integer>> result = new HashMap<>();
        try {
            DBCursor cursor = unreadMessageColl.find();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String userId = obj.get(ObjectID).toString();
                BasicDBList unreadMessageList = (BasicDBList) obj.get(Field_Unread_Message_List);
                if (unreadMessageList != null && !unreadMessageList.isEmpty()) {
                    HashMap<String, Integer> unreadMessageMap = new HashMap<>();
                    for (Object o : unreadMessageList) {
                        DBObject unreadMessageObj = (DBObject) o;
                        String friendId = (String) unreadMessageObj.get(Field_User_ID);
                        Integer unread = (Integer) unreadMessageObj.get(Field_Unread_Message_Number);
                        unreadMessageMap.put(friendId, unread);
                    }
                    result.put(userId, unreadMessageMap);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

//        private static final String userIdField = Field_Unread_Message_List + ".$." + Field_User_ID;
    private static final String unreadMessageNumberField = Field_Unread_Message_List + ".$." + Field_Unread_Message_Number;

    public static void increase(String userId, String friendId) {
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(ObjectID, id);
            BasicDBObject lastChatObj = new BasicDBObject(Field_User_ID, friendId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", lastChatObj);
            findObj.append(Field_Unread_Message_List, elemMatch);
            DBObject obj = unreadMessageColl.findOne(findObj);
            if (obj != null) {
                BasicDBObject updateObj = new BasicDBObject(unreadMessageNumberField, 1);
                BasicDBObject setObj = new BasicDBObject("$inc", updateObj);
                unreadMessageColl.update(findObj, setObj);
            } else {
                BasicDBObject updateQuery = new BasicDBObject(ObjectID, id);
                BasicDBObject unreadMessage = new BasicDBObject(Field_User_ID, friendId);
                unreadMessage.append(Field_Unread_Message_Number, 1);
                BasicDBObject lastChatO = new BasicDBObject(Field_Unread_Message_List, unreadMessage);
                BasicDBObject updateCommand = new BasicDBObject("$push", lastChatO);
                unreadMessageColl.update(updateQuery, updateCommand, true, false);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void remove(String userId, String friendId) {
        try {
            BasicDBObject query = new BasicDBObject();
            query.put(ObjectID, new ObjectId(userId));
            BasicDBObject obj = new BasicDBObject(Field_Unread_Message_List, new BasicDBObject(Field_User_ID, friendId));
            BasicDBObject updateCommand = new BasicDBObject("$pull", obj);
            unreadMessageColl.update(query, updateCommand);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static void update(String userId, HashMap<String, Integer> unreadMap) {
        try {
            BasicDBObject query = new BasicDBObject();
            query.put(ObjectID, new ObjectId(userId));
            BasicDBList list = new BasicDBList();
            if(unreadMap != null && !unreadMap.isEmpty()){
                for (Map.Entry<String, Integer> pairs : unreadMap.entrySet()) {
                    String uId = pairs.getKey();
                    Integer number = pairs.getValue();
                    BasicDBObject unreadMessage = new BasicDBObject(Field_User_ID, uId);
                    unreadMessage.append(Field_Unread_Message_Number, number);
                    list.add(unreadMessage);
                }
            }
            BasicDBObject updateCommand = new BasicDBObject("$set", new BasicDBObject(Field_Unread_Message_List, list));
            unreadMessageColl.update(query, updateCommand, true, false);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean isUnreadMessageListExists() {
        try {
            return dbuser.collectionExists(Unread_Message_Collection);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return false;
    }

}
