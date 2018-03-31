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
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import java.util.List;
import org.bson.types.ObjectId;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class LastChatDAO {

    private static final String LastChatCollectionName = "last_chat";
    private static final String ObjectID = "_id";

    private static final String LastChatListField = "last_chat_list";
    private static final String UserIdField = "user_id";
    private static final String MsgIdField = "msg_id";
    private static final String FromField = "from";
    private static final String ToField = "to";
    private static final String ValueField = "value";
    private static final String SentTimeField = "sent_time";
    private static final String MessageTypeField = "message_type";
    private static final String ValueFileField = "files";

    private static DB dbuser;
    private static DBCollection lastChatCollection;

    static {
        try {
            dbuser = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            lastChatCollection = dbuser.getCollection(LastChatCollectionName);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static HashMap<String, HashMap<String, String>> getAll() {
        HashMap<String, HashMap<String, String>> result = new HashMap<>();
        try {
            DBCursor cursor = lastChatCollection.find();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String userId = obj.get(ObjectID).toString();
                BasicDBList unreadMessageList = (BasicDBList) obj.get(LastChatListField);
                if (unreadMessageList != null && !unreadMessageList.isEmpty()) {
                    HashMap<String, String> lastChatMap = new HashMap<>();
                    for (Object o : unreadMessageList) {
                        DBObject unreadMessageObj = (DBObject) o;
                        String friendId = (String) unreadMessageObj.get(UserIdField);
                        String msgId = (String) unreadMessageObj.get(MsgIdField);
                        lastChatMap.put(friendId, msgId);
                    }
                    result.put(userId, lastChatMap);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }    
    
    public static HashMap<String, Message> getLastChatList(String userid) {
        HashMap<String, Message> ret = new HashMap<>();
        try {
            BasicDBObject query = new BasicDBObject(ObjectID, new ObjectId(userid));
            BasicDBObject obj = (BasicDBObject) lastChatCollection.findOne(query);
            if (obj == null) {
                return ret;
            }
            BasicDBList lastChatList = (BasicDBList) obj.get(LastChatListField);

            if (lastChatList != null) {
                for (Object lcO : lastChatList) {
                    BasicDBObject lc = (BasicDBObject) lcO;
                    Message msg = new Message();
                    String msgId = lc.getString(MsgIdField);
                    msg.id = msgId;
                    String from = lc.getString(FromField);
                    msg.from = from;
                    String to = lc.getString(ToField);
                    msg.to = to;
                    String value = lc.getString(ValueField);
                    msg.value = value;
                    String msgType = lc.getString(MessageTypeField);
                    msg.msgType = MessageType.valueOf(msgType);
                    Long sentTime = lc.getLong(SentTimeField);
                    msg.serverTime = sentTime;
                    String uId = lc.getString(UserIdField);
                    String files = lc.getString(ValueFileField);
                    if(files != null){
                        JSONParser parser = new JSONParser();
                        msg.files = (JSONArray)parser.parse(files);
                    }
                         
                    ret.put(uId, msg);

                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return ret;
    }
    
    //HUNGDT add
    public static HashMap<String, Message> getLastChatList2() {
        HashMap<String, Message> ret = new HashMap<>();
        try {
            //BasicDBObject query = new BasicDBObject(ObjectID, new ObjectId(userid));
            BasicDBObject obj = (BasicDBObject) lastChatCollection.findOne();
            if (obj == null) {
                return ret;
            }
            BasicDBList lastChatList = (BasicDBList) obj.get(LastChatListField);

            if (lastChatList != null) {
                for (Object lcO : lastChatList) {
                    BasicDBObject lc = (BasicDBObject) lcO;
                    Message msg = new Message();
                    String msgId = lc.getString(MsgIdField);
                    msg.id = msgId;
                    String from = lc.getString(FromField);
                    msg.from = from;
                    String to = lc.getString(ToField);
                    msg.to = to;
                    String value = lc.getString(ValueField);
                    msg.value = value;
                    String msgType = lc.getString(MessageTypeField);
                    msg.msgType = MessageType.valueOf(msgType);
                    Long sentTime = lc.getLong(SentTimeField);
                    msg.serverTime = sentTime;
                    String uId = lc.getString(UserIdField);
                    ret.put(uId, msg);

                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return ret;
    }

    private static final String msgIdElement = LastChatListField + ".$." + MsgIdField;
    private static final String valueElement = LastChatListField + ".$." + ValueField;
    private static final String sentTimeElement = LastChatListField + ".$." + SentTimeField;
    private static final String fromElement = LastChatListField + ".$." + FromField;
    private static final String toElement = LastChatListField + ".$." + ToField;
    private static final String messageTypeElement = LastChatListField + ".$." + MessageTypeField;
    private static final String valueFileElement = LastChatListField + ".$." + ValueFileField;

    public static void putLastChatList(String userId, String friendId, Message msg) {
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(ObjectID, id);
            BasicDBObject lastChatObj = new BasicDBObject(UserIdField, friendId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", lastChatObj);
            findObj.append(LastChatListField, elemMatch);
            DBObject obj = lastChatCollection.findOne(findObj);
            if (obj != null) {
                BasicDBObject updateObj = new BasicDBObject(msgIdElement, msg.id);
                updateObj.append(valueElement, msg.value);
                updateObj.append(fromElement, msg.from);
                updateObj.append(toElement, msg.to);
                updateObj.append(sentTimeElement, msg.serverTime);
                updateObj.append(messageTypeElement, msg.msgType.toString());
                if(msg.files != null){
                    updateObj.append(valueFileElement, msg.files.toJSONString());
                }
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                lastChatCollection.update(findObj, setObj);
            } else {
                BasicDBObject updateQuery = new BasicDBObject(ObjectID, id);
                BasicDBObject lastchat = new BasicDBObject(UserIdField, friendId);
                lastchat.append(MsgIdField, msg.id);
                lastchat.append(ValueField, msg.value);
                lastchat.append(FromField, msg.from);
                lastchat.append(ToField, msg.to);
                lastchat.append(SentTimeField, msg.serverTime);
                lastchat.append(MessageTypeField, msg.msgType.toString());
                if(msg.files != null){
                    lastchat.append(ValueFileField, msg.files.toJSONString());
                }
                BasicDBObject lastChatO = new BasicDBObject(LastChatListField, lastchat);
                BasicDBObject updateCommand = new BasicDBObject("$push", lastChatO);
                lastChatCollection.update(updateQuery, updateCommand, true, false);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void removeLastChatList(String userId, String friendId) {
        try {
            BasicDBObject query = new BasicDBObject();
            query.put(ObjectID, new ObjectId(userId));
            BasicDBObject obj = new BasicDBObject(LastChatListField, new BasicDBObject(UserIdField, friendId));
            BasicDBObject updateCommand = new BasicDBObject("$pull", obj);
            lastChatCollection.update(query, updateCommand);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static Message getLastChat(String userId, String friendId) {
        BasicDBObject query = new BasicDBObject(ObjectID, new ObjectId(userId));
        DBObject obj = lastChatCollection.findOne(query);
        BasicDBList lastChatList = (BasicDBList) obj.get(LastChatListField);
        if (lastChatList != null && !lastChatList.isEmpty()) {
            HashMap<String, String> lastChatMap = new HashMap<>();
            for (Object o : lastChatList) {
                DBObject lastChatObject = (DBObject) o;
                String id = (String) lastChatObject.get(UserIdField);
                if (friendId.endsWith(id)){
                    String msgid = (String) lastChatObject.get(MsgIdField);
                    String value = (String) lastChatObject.get(ValueField);
                    String from = (String) lastChatObject.get(FromField);
                    String to = (String) lastChatObject.get(ToField);
                    String msgType = (String) lastChatObject.get(MessageTypeField);
                    Message msg = new Message(msgid, from, to, MessageType.valueOf(msgType), value);
                    return msg;
                }
            }
        }
        return null;
    }

    public static void removeExceptList(String userid, List<String> llFav) {
        DBObject query = new BasicDBObject(ObjectID, new ObjectId(userid));
        DBObject dbObject = lastChatCollection.findOne(query);
        if (dbObject != null){
            BasicDBList list = new BasicDBList();
            BasicDBList listLastChat = (BasicDBList) dbObject.get(LastChatListField);
            Util.addDebugLog("--------------- lastChat before: "+listLastChat.size());
            for (Object obj : listLastChat){
                DBObject lastchatObj = (DBObject) obj;
                String friendId = (String) lastchatObj.get(UserIdField);
                Util.addDebugLog("------------ frID "+friendId );
                if (llFav.contains(friendId)){
//                    removeLastChatList(userid, friendId);
                    list.add(lastchatObj);
                }
            }
            Util.addDebugLog("--------------- lastChat after: "+list.size());
            DBObject updateObj = new BasicDBObject();
            updateObj.put("$set", new BasicDBObject(LastChatListField, list));
            lastChatCollection.update(query, updateObj);
        }
    }
}
