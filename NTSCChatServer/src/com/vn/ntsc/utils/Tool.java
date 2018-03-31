/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.utils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;

/**
 *
 * @author Rua
 */
public class Tool {

    private static DBCollection collLastChat;
    private static DBCollection collUnreadMessage;
    private static DBCollection collChatDBName;
    private static DBCollection collUser;
    private static Mongo mongo;
    private static DB userdb;
    private static DB db;
    public static DB dbExtension;
    private static long OneYear = (long) 365 * 24 * 60 * 60 * 1000;

    static {
        try {
//            mongo = new Mongo("10.202.3.103");
            mongo = new Mongo("localhost");
            userdb = mongo.getDB("userdb");
            db = mongo.getDB("chatlog");
            dbExtension = mongo.getDB("chatlogextension");
            collUser = userdb.getCollection("user");
            collLastChat = userdb.getCollection("last_chat");
            collUnreadMessage = userdb.getCollection("unread_message");
            collChatDBName = userdb.getCollection("chat_db");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void reverstHistory (String userId){
        try{
            DB usedb = getDB(userId);
            DBCollection coll = usedb.getCollection( userId );
            Collection<String> lastchats = getLastChatList(coll);
            lastchats.remove(userId);
            if(!lastchats.isEmpty()){
                undeleted(coll, lastchats);
                for(String friendId : lastchats){
                    Message message = getMessages(coll, friendId);
                    if(message != null){
                        putLastChatList(userId, friendId, message);
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
    }
    
    public static Message castDBObject( DBObject o ) {
        if( o == null ) {
            return null;
        }
        Message msg = new Message();
        try {
            msg.id = (String) o.get( "msgid" );
            msg.from = (String) o.get( "from" );
            msg.to = (String) o.get( "to" );
            String msgTypeStr = (String) o.get( "type" );
            msg.msgType = MessageType.valueOf( msgTypeStr );
            msg.value = (String) o.get( "value" );

            msg.originTime = (Date) o.get( "ot" );
            //msg.serverTime = (Date) o.get( Field_ServerTime );
            msg.serverTime = (Long) o.get( "st" );
            msg.destinationTime = (Date) o.get( "dt" );

        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return msg;
    }    
    
    public static final BasicDBObject OrderBy_ServerTime_Desc = new BasicDBObject( "st", -1 );
    
    public static Message getMessages( DBCollection coll, String friendID ) {
        try{

            BasicDBObject and1 = new BasicDBObject( "from", friendID );
            BasicDBObject and2 = new BasicDBObject( "to", friendID );
            BasicDBObject[] ands = new BasicDBObject[2];
            ands[0] = and1;
            ands[1] = and2;

            BasicDBObject query1 = new BasicDBObject( "$or", ands );

            DBCursor cursor = coll.find( query1 ).sort( OrderBy_ServerTime_Desc ).limit( 1 );
            if(cursor.hasNext()){
                return castDBObject(cursor.next());
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return null;
    }    
    
    public static final String LastChatListField = "last_chat_list";
    public static final String UserIdField = "user_id";
    public static final String MsgIdField = "msg_id";
    public static final String FromField = "from";
    public static final String ToField = "to";
    public static final String ValueField = "value";
    public static final String SentTimeField = "sent_time";
    public static final String MessageTypeField = "message_type";    
    
    private static final String msgIdField = LastChatListField + ".$." + MsgIdField;
    private static final String valueField = LastChatListField + ".$." + ValueField;
    private static final String sentTimeField = LastChatListField + ".$." + SentTimeField;
    private static final String fromField = LastChatListField + ".$." + FromField;
    private static final String toField = LastChatListField + ".$." + ToField;
    private static final String messageTypeField = LastChatListField + ".$." + MessageTypeField;
    
    public static void putLastChatList(String userId, String friendId, Message msg ){
        try{
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject("_id", id);
            BasicDBObject lastChatObj = new BasicDBObject("user_id", friendId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", lastChatObj);
            findObj.append("last_chat_list", elemMatch);
            DBObject obj = collLastChat.findOne(findObj);
            if (obj != null) {
                BasicDBObject updateObj = new BasicDBObject(msgIdField, msg.id);
                updateObj.append(valueField, msg.value);
                updateObj.append(fromField, msg.from);
                updateObj.append(toField, msg.to);
                updateObj.append(sentTimeField, msg.serverTime);
                updateObj.append(messageTypeField, msg.msgType.toString());
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                collLastChat.update(findObj, setObj);
            } else {
                BasicDBObject updateQuery = new BasicDBObject("_id", id);
                BasicDBObject lastchat = new BasicDBObject("user_id", friendId);
                lastchat.append(MsgIdField, msg.id);
                lastchat.append(ValueField, msg.value);
                lastchat.append(FromField, msg.from);
                lastchat.append(ToField, msg.to);
                lastchat.append(SentTimeField, msg.serverTime);
                lastchat.append(MessageTypeField, msg.msgType.toString());
                BasicDBObject lastChatO = new BasicDBObject("last_chat_list", lastchat);
                BasicDBObject updateCommand = new BasicDBObject("$push", lastChatO);
                collLastChat.update(updateQuery, updateCommand, true, false);
            }
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
    }    

    public static void undeleted( DBCollection coll, Collection<String> llFriends ) {
        
        List<BasicDBObject> llDBO = new ArrayList<>();
        for (String fid : llFriends) {
            BasicDBObject o1 = new BasicDBObject( "from", fid );
            BasicDBObject o2 = new BasicDBObject( "to", fid );
            llDBO.add( o1 );
            llDBO.add( o2 );
        }
        
        BasicDBObject[] oarr = llDBO.toArray( new BasicDBObject[llDBO.size()] );
        BasicDBObject query = new BasicDBObject( "$or", oarr );
        
//        clt.remove( query );
        BasicDBObject updater = new BasicDBObject( "$set", new BasicDBObject( "del", false ) );
        coll.update( query, updater, false, true );
    }
    
//    public static List<String> getLastChatList( String userid ){
//        List<String> ret = new ArrayList<>();
//        try{
//            BasicDBObject query = new BasicDBObject("_id", new ObjectId( userid ) );
//            BasicDBObject obj = (BasicDBObject) collLastChat.findOne( query );
//            if( obj == null ) return ret;
//            BasicDBList lastChatList = (BasicDBList) obj.get( "last_chat_list" );
//
//            if( lastChatList != null ){
//                for (Object lcO : lastChatList) {
//                    BasicDBObject lc = (BasicDBObject) lcO;
//                    String uId = lc.getString("user_id");
//                    ret.add(uId);
//                }
//            }
//        } catch( Exception ex ) {
//            Util.addErrorLog(ex);
//        }
//        return ret;
//    }    
    public static Collection<String> getLastChatList( DBCollection coll ){
        Set<String> ret = new HashSet<>();
        try{
            List<String> froms = coll.distinct(FromField);
            List<String> tos = coll.distinct(ToField);
            if(froms != null && !froms.isEmpty()){
                ret.addAll(froms);
            }
            if(tos != null && !tos.isEmpty()){
                ret.addAll(tos);
            }
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return ret;
    }    
    
    private static DB getDB(String collectionName){
        String dbName = getDBName(collectionName);
        if(dbName != null){
            if(dbName.trim().equals("chatlog")){
                return db;
            }
        }
//        if(db.collectionExists(collectionName))
//            return db;
        return dbExtension;
    }
    
    private static String getDBName(String collectionName){
        String result = null;
        try{
            ObjectId id = new ObjectId(collectionName);
            DBObject obj = collChatDBName.findOne(new BasicDBObject("_id", id));
            if(obj != null){
                result = (String) obj.get("chat_db_name");
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }    
    
    public static void convertChatDBName() {
        try {
            if (!userdb.collectionExists("chat_db")) {
                DBCursor cursor = collUser.find();
                while (cursor.hasNext()) {
                    DBObject user = cursor.next();
                    String userId = (String) user.get("_id").toString();
                    String dbName = "chatlogextension";
                    if (db.collectionExists(userId)) {
                        dbName = "chatlog";
                    }
                    BasicDBObject inseartObject = new BasicDBObject("_id", new ObjectId(userId));
                    inseartObject.append("chat_db_name", dbName);
                    collChatDBName.save(inseartObject, new WriteConcern(true));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void fixUnreadMessage() {
        try {
            BasicDBList ands = new BasicDBList();
            DBObject findObject = new BasicDBObject();
            BasicDBObject objGt = new BasicDBObject();
            BasicDBObject objLt = new BasicDBObject();
            objGt.append("$gt", "20150427090000");
            objLt.append("$lt", "20150509085959");
            ands.add(new BasicDBObject("reg_date", objGt));
            ands.add(new BasicDBObject("reg_date", objLt));
            findObject.put("$and", ands);
            DBCursor cursor = collUser.find(findObject);
            while (cursor.hasNext()) {
                DBObject user = cursor.next();
                String userId = (String) user.get("_id").toString();
                BasicDBList list = new BasicDBList();
                BasicDBObject inseartObject = new BasicDBObject("_id", new ObjectId(userId));
                inseartObject.append("unread_message_list", list);
                collUnreadMessage.save(inseartObject, new WriteConcern(true));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getUnreadNumber(String userId, String friendid, Long readtime) {
        int unread = 0;
        try {

            DBCollection clt = db.getCollection(userId);
            readtime = readtime != null ? readtime : (Util.currentTime() - OneYear);

            BasicDBObject ge = new BasicDBObject("$gt", readtime);
            BasicDBObject query = new BasicDBObject("from", friendid)
                    .append("st", ge)
                    .append("del", false);

            DBCursor cursor = clt.find(query);
            unread = cursor.count();
            cursor.close();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return unread;
    }
}
