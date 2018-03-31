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
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.ChatlogdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import java.util.HashMap;
import org.bson.types.ObjectId;
import com.vn.ntsc.Config;
import com.vn.ntsc.blacklist.BlockUserManager;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.SendFileMessage;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;
import com.vn.ntsc.dao.IDAO;
import com.vn.ntsc.utils.Validator;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.FileChatLogdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Mỗi user một collection nằm trong databases chatlog
 *
 * @author tuannxv00804
 */
public class MongoDAO implements IDAO {

    private static final String Field_MsgID = "msgid";
    private static final String Field_From = "from";
    private static final String Field_To = "to";
    private static final String Field_MsgType = "type";
    private static final String Field_Value = "value";
    private static final String Field_Read_Time = "rt";
    private static final String Field_OriginTime = "ot";
    private static final String Field_ServerTime = "st";
    private static final String Field_DestTime = "dt";
    private static final String Field_IsDel = "del";
    private static final String Field_IsLock = "islock";
    private static final String Field_Files = "files";
    private static final String Field_CatId = "cat_id";
    private static final String Field_StickerUrl = "sticker_url";
    private static final String Field_MsgFileId = "msg_file_id";
    
    //file chat
    private static final String Field_ThumbnailUrl = "thumbnail_url";
    private static final String Field_OriginalUrl = "original_url";
    private static final String Field_Duration = "file_duration";    
    private static final String Field_FileUrl = "file_url";
    private static final String File_Type_Video = "video";
    private static final String File_Type_ALL = "all_file_chat";
    private static final String File_Type_Audio = "audio";
    private static final String File_Type_Image = "image";
    private static final String Size_Image = "size_image";
    private static final String Size_Audio = "size_audio";
    private static final String Size_Video = "size_video";
    private static final String Size_All_File = "size_all_file";

//    private static final String Field_BlackList = "bl";
    private static final String ObjectID = "_id";
    private static final String Field_Ip = "ip";

    private static final String User_Collection = "user";
    private static final String Chat_DB_Collection = "chat_db";
    private static final String Field_Flag = "flag";
//    private static final String Field_Verification_Flag = "verification_flag";
    private static final String Field_Chat_DB_Name = "chat_db_name";

    private static final String Field_CallID = "callid";

    private static DB db;
    private static DB dbExtension;
    private static DB dbChatLog;
    private static DB dbuser;
    private static DBCollection userColl;
    private static DBCollection chatDBColl;
    
    static {
        try {
            db = CommonDAO.mongo.getDB(ChatlogdbKey.DB_NAME);
            dbExtension = CommonDAO.mongo.getDB(ChatlogdbKey.DB_EXTENSION);
            dbChatLog = CommonDAO.mongo.getDB(FileChatLogdbKey.DB_NAME);
            dbuser = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            userColl = dbuser.getCollection(User_Collection);
            chatDBColl = dbuser.getCollection(Chat_DB_Collection);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public MongoDAO() {
    }

    public static BasicDBObject castMsg(Message msg) {
        if (msg == null) {
            return null;
        }
        BasicDBObject obj = new BasicDBObject().append(Field_MsgID, msg.id).append(Field_From, msg.from).append(Field_To, msg.to)
                .append(Field_MsgType, msg.msgType.toString())
                .append(Field_Value, msg.value).append(Field_OriginTime, msg.originTime)
                .append(Field_ServerTime, msg.serverTime).append(Field_DestTime, msg.destinationTime);
        if (msg.ip != null) {
            obj.append(Field_Ip, msg.ip);
        }
        if (msg.callId != null) {
            obj.append(Field_CallID, msg.callId);
        }
        if(msg.files != null){
            obj.append(Field_Files, msg.files.toString());
        }
        if(msg.catId != null){
            obj.append(Field_CatId, msg.catId);
        }
        if(msg.stickerUrl != null){
            obj.append(Field_StickerUrl, msg.stickerUrl);
        }
        if(msg.msgFileId != null){
            obj.append(Field_MsgFileId, msg.msgFileId);
        }

        return obj;
    }

    public static BasicDBObject[] castMsgs(Message[] arr) {
        if (arr == null) {
            return null;
        }
        BasicDBObject[] dbos = new BasicDBObject[arr.length];
        for (int i = 0; i < arr.length; i++) {
            dbos[i] = castMsg(arr[i]);
        }
        return dbos;
    }

    public static Message castDBObject(DBObject o) {
        if (o == null) {
            return null;
        }
        Message msg = new Message();
        try {
            msg.id = (String) o.get(Field_MsgID);
            msg.from = (String) o.get(Field_From);
            msg.to = (String) o.get(Field_To);
            String msgTypeStr = (String) o.get(Field_MsgType);
            msg.msgType = MessageType.valueOf(msgTypeStr);
            msg.value = (String) o.get(Field_Value);
            msg.callId = (String) o.get( Field_CallID );
            msg.originTime = (Date) o.get(Field_OriginTime);
            //msg.serverTime = (Date) o.get( Field_ServerTime );
            msg.serverTime = (Long) o.get(Field_ServerTime);
            msg.readTime = (Long) o.get(Field_Read_Time);
            msg.destinationTime = (Date) o.get(Field_DestTime);
            msg.islock = (Long) o.get(Field_IsLock);//thanhdd fix #6782
            String files = (String)o.get(Field_Files);
            if (files != null) {
                JSONParser parser = new JSONParser();
                msg.files = (JSONArray) parser.parse(files);
            }
            String catId = (String)o.get(Field_CatId);
            if(catId != null){
                msg.catId = catId;
            }
            String stickerUrl = (String)o.get(Field_StickerUrl);
            if(stickerUrl != null){
                msg.stickerUrl = stickerUrl;
            }
            String msgFileId = (String)o.get(Field_MsgFileId);
            if(msgFileId != null){
                msg.msgFileId = msgFileId;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return msg;
    }

    public static Message[] castDBObject(DBObject[] arr) {
        if (arr == null) {
            return null;
        }
        Message[] msgs = new Message[arr.length];
        for (int i = 0; i < arr.length; i++) {
            msgs[i] = castDBObject(arr[i]);
        }
        return msgs;
    }

    @Override
    public void save(Message msg) {
        if (msg == null) {
            return;
        }

        BasicDBObject o = castMsg(msg);
        o = o.append(Field_IsDel, false);
        Util.addDebugLog("save(Message msg) msg.callId============" + msg.callId);
        //o = o.append(Field_CallID, msg.callId);
        
        boolean isBlocked = BlockUserManager.isBlock(msg.from, msg.to);
        if (!isBlocked) {
            if (msg.to != null && !msg.to.isEmpty()) {
                DBCollection clt1 = getDB(msg.to).getCollection(msg.to);
                clt1.insert(o);
            }
        }

        if (msg.from != null && !msg.from.isEmpty()) {
            DBCollection clt2 = getDB(msg.from).getCollection(msg.from);
            clt2.insert(o);
        }
        
        //save data file in fileChatLog
        if(msg.msgType == MessageType.FILE){
            saveFileChatLog(msg);
        }
    }
    
    
    //NOT ready
    /**
     * All messages must have the same dest_user
     *
     * @param arr
     */
    @Override
    public void save(Message[] arr) {
        if (arr == null || arr[0] == null) {
            return;
        }
        DBCollection clt = getDB(arr[0].to).getCollection(arr[0].to);
        BasicDBObject[] objs = castMsgs(arr);
        clt.insert(objs);
    }

    //NOT ready
    @Override
    public void save(List<Message> msgs) {
        if (msgs == null || msgs.isEmpty()) {
            return;
        }
        Message[] arr = msgs.toArray(new Message[msgs.size()]);
        save(arr);
    }

//    private static final BasicDBObject OrderBy_OriginTime_Asc = new BasicDBObject( Field_OriginTime, 1 );
//    private static final BasicDBObject OrderBy_OriginTime_Desc = new BasicDBObject( Field_OriginTime, -1 );
//    private static final BasicDBObject OrderBy_SeverTime_Asc = new BasicDBObject( Field_ServerTime, 1 );
    private static final BasicDBObject OrderBy_ServerTime_Desc = new BasicDBObject(Field_ServerTime, -1);
    private static final BasicDBObject OrderBy_ServerTime_Asce = new BasicDBObject(Field_ServerTime, 1);

    public List<Message> getMessages(String userID, String friendID, Long serverTimeStamp, int take, String operation) {
        List<Message> list = new ArrayList<>();
        try {
            BasicDBObject lt = new BasicDBObject(operation, serverTimeStamp);
            DBCollection clt1 = getDB(userID).getCollection(userID);

            BasicDBObject and1 = new BasicDBObject(Field_From, friendID);
            BasicDBObject and2 = new BasicDBObject(Field_To, friendID);
            BasicDBObject[] ands = new BasicDBObject[2];
            ands[0] = and1;
            ands[1] = and2;

            BasicDBObject query1 = new BasicDBObject("$or", ands)
                    .append(Field_IsDel, false);
            if (serverTimeStamp != null) {
                query1.append(Field_ServerTime, lt);
            }

            DBCursor cursor = clt1.find(query1).sort(OrderBy_ServerTime_Desc).limit(take);
            while (cursor.hasNext()) {
                Message msg = castDBObject(cursor.next());
                list.add(msg);
            }

            Collections.sort(list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return list;
    }

    @Override
    public Message getMessages(String userId, String messageId) {

        DBCollection clt1 = getDB(userId).getCollection(userId);

        BasicDBObject query1 = new BasicDBObject(Field_MsgID, messageId);

        DBObject obj = clt1.findOne(query1);
        return castDBObject(obj);
    }

    @Override
    public boolean removeMessage(String userId, String messageId) {
        DBCollection coll = getDB(userId).getCollection(userId);

        BasicDBObject query = new BasicDBObject(Field_MsgID, messageId);
        DBObject obj = coll.findOne(query);
        if (obj != null) {
            coll.remove(obj);
            return true;
        }
        return false;
    }

    @Override
    public List<Message> getHistory(String userID, String friendID, Long serverTimeStamp, int take) {
        return getMessages(userID, friendID, serverTimeStamp, take, "$lt");
    }

    @Override
    public List<Message> getNewMessage(String userID, String friendID, Long serverTimeStamp) {
        return getMessages(userID, friendID, serverTimeStamp, 1000, "$gt");
    }

    @Override
    public boolean isUser(String friendid) {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(friendid);
            DBObject obj = userColl.findOne(new BasicDBObject(ObjectID, id));
            if (obj != null) {
                Integer flag = (Integer) obj.get(Field_Flag);
                if (flag != null && flag != Constant.USER_STATUS_FLAG.ACTIVE) {
                    return false;
                }
//                Integer verificationFlag = (Integer) obj.get(Field_Verification_Flag);
//                if(verificationFlag != null && verificationFlag != Constant.APPROVED)
//                    return false;
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    @Override
    public List<String> getDeactivate() {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cur = userColl.find();
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(UserdbKey.USER.FLAG);
                if (flag != null && (flag == Constant.USER_STATUS_FLAG.DEACITIVE || flag == Constant.USER_STATUS_FLAG.DISABLE)) {
                    String userId = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
                    result.add(userId);
//                    continue;
                }
//                Integer gender = (Integer) dbO.get(UserdbKey.USER.GENDER);
//                Integer verifyFlag = (Integer) dbO.get(UserdbKey.USER.VERIFICATION_FLAG);
//                if(gender == Constant.FEMALE && verifyFlag != null && verifyFlag != Constant.APPROVED){
//                    String userId = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
//                    result.add(userId);
//                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    @Override
    public List<String> getListUserIds(Collection<String> ids) {
        List<String> result = new ArrayList<>();
        try {
            if (ids != null && !ids.isEmpty()) {
                List<ObjectId> obIds = new ArrayList<>();
                for (String id : ids) {
                    if (ObjectId.isValid(id)) {
                        ObjectId obi = new ObjectId(id);
                        obIds.add(obi);
                    }
                }
                DBObject query = QueryBuilder.start(ObjectID).in(obIds).get();
                DBCursor cursor = userColl.find(query);
                while (cursor.hasNext()) {
                    DBObject obj = cursor.next();
                    Integer flag = (Integer) obj.get(Field_Flag);
                    if (flag == null || flag == Constant.USER_STATUS_FLAG.ACTIVE) {
                        String fId = obj.get(ObjectID).toString();
                        result.add(fId);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    @Override
    public int getUnreadNumber(UserConnection uc, String friendid) {
        int unread = 0;
        try {
            DBCollection clt = getDB(uc.user.username).getCollection(uc.user.username);
            Long readtime = uc.user.readTimes.get(friendid);
            readtime = readtime != null ? readtime : (Util.currentTime() - Config.OneYear);

            BasicDBObject ge = new BasicDBObject("$gt", readtime);
            BasicDBObject query = new BasicDBObject(Field_From, friendid)
                    .append(Field_ServerTime, ge)
                    .append(Field_IsDel, false);

            unread = clt.find(query).count();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return unread;
    }

    @Override
    public void del(String userid, List<String> llFriends) {
        DBCollection clt = getDB(userid).getCollection(userid);

        LinkedList<BasicDBObject> llDBO = new LinkedList<>();
        for (String fid : llFriends) {
            BasicDBObject o1 = new BasicDBObject(Field_From, fid);
            BasicDBObject o2 = new BasicDBObject(Field_To, fid);
            llDBO.add(o1);
            llDBO.add(o2);
        }

        BasicDBObject[] oarr = llDBO.toArray(new BasicDBObject[llDBO.size()]);
        BasicDBObject query = new BasicDBObject("$or", oarr);

//        clt.remove( query );
        BasicDBObject updater = new BasicDBObject("$set", new BasicDBObject(Field_IsDel, true));
        clt.update(query, updater, false, true);
    }

    @Override
    public boolean isExistFileMessage(Message msg) {
        boolean result = true;

        try {
//            BasicDBObject query = new BasicDBObject( Field_MsgType, MessageType.FILE.toString() )
//                    .append( Field_MsgID, SendFileMessage.getMsgID( msg.value ) );
            DBCollection cltFrom = getDB(msg.from).getCollection(msg.from);
            DBCollection cltTo = getDB(msg.to).getCollection(msg.to);

//            BasicDBObject query = new BasicDBObject(Field_MsgID, SendFileMessage.getMsgID(msg.value));
//            BasicDBObject query = new BasicDBObject(Field_MsgID, SendFileMessage.getMsgID(msg.value));
            BasicDBObject query  = new BasicDBObject(Field_MsgID, msg.id);
                    

            DBObject objFrom = cltFrom.findOne(query);
            DBObject objTo = cltTo.findOne(query);

            if (objFrom == null && objTo == null) {

                result = false;

            }

            return result;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
    }

    @Override
    public boolean updateFileMessage(Message msg) {
        boolean result = true;
//        if( msg.msgType == MessageType.FILE ){
//            LoggingThread.isConfirmFile( msg );
//        }

        try {
//            BasicDBObject query = new BasicDBObject( Field_MsgType, MessageType.FILE.toString() )
//                    .append( Field_MsgID, SendFileMessage.getMsgID( msg.value ) );
            
            DBCollection cltFrom = getDB(msg.from).getCollection(msg.from);
            DBCollection cltTo = getDB(msg.to).getCollection(msg.to);

            BasicDBObject query = new BasicDBObject(Field_MsgID, msg.id);

            DBObject objFrom = cltFrom.findOne(query);
            DBObject objTo = cltTo.findOne(query);

            if (objFrom == null || objTo == null) {

                return false;

            } else {

                BasicDBObject updater = new BasicDBObject("$set", new BasicDBObject(Field_Value, msg.value));

                cltFrom.update(query, updater);

                cltTo.update(query, updater);
            }

            return result;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
    }
    
   
    @Override
    public boolean updateReadMessage(String from, String to, String userId) {
        boolean result = true;
//        if( msg.msgType == MessageType.FILE ){
//            LoggingThread.isConfirmFile( msg );
//        }

        try {
//            BasicDBObject query = new BasicDBObject( Field_MsgType, MessageType.FILE.toString() )
//                    .append( Field_MsgID, SendFileMessage.getMsgID( msg.value ) );

            BasicDBObject query = new BasicDBObject(Field_Read_Time, new BasicDBObject("$exists", false));
            
            query.append(Field_IsDel, false);
            query.append(Field_To, to);
            query.append(Field_From, from);
            
            BasicDBObject updater = new BasicDBObject("$set", new BasicDBObject(Field_Read_Time, Util.currentTime()));

//            if(from != null && !from.isEmpty()){
//                DBCollection cltFrom = getDB(from).getCollection( from );
//                cltFrom.update( query, updater,false, true);
//            }
            if (from != null && !from.isEmpty()) {
                Util.addDebugLog("query==============================="+query);
                Util.addDebugLog("updater==============================="+updater);
                DBCollection clt = getDB(from).getCollection(from);
                clt.update(query, updater, false, true);
            }

            return result;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
    }
    
    @Override
    public boolean updateCallMessage(String msgId, Message msg) {
        boolean result = true;
//        if( msg.msgType == MessageType.FILE ){
//            LoggingThread.isConfirmFile( msg );
//        }

        try {
//            BasicDBObject query = new BasicDBObject( Field_MsgType, MessageType.FILE.toString() )
//                    .append( Field_MsgID, SendFileMessage.getMsgID( msg.value ) );

            DBObject query = new BasicDBObject(Field_MsgID, msgId);
            BasicDBObject setObj = new BasicDBObject(Field_Value, msg.value);
            setObj.append(Field_MsgID, msg.id);

            BasicDBObject updater = new BasicDBObject("$set", setObj);

            if (msg.from != null && !msg.from.isEmpty()) {
                DBCollection cltFrom = getDB(msg.from).getCollection(msg.from);
                cltFrom.update(query, updater, false, true);
            }

            if (msg.to != null && !msg.to.isEmpty()) {
                DBCollection clt = getDB(msg.to).getCollection(msg.to);
                clt.update(query, updater, false, true);
            }

            return result;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
    }

    @Override
    public boolean updateReadMessage(String userId, List<String> msgIds) {
        boolean result = true;
//        if( msg.msgType == MessageType.FILE ){
//            LoggingThread.isConfirmFile( msg );
//        }

        try {
//            BasicDBObject query = new BasicDBObject( Field_MsgType, MessageType.FILE.toString() )
//                    .append( Field_MsgID, SendFileMessage.getMsgID( msg.value ) );

            DBObject query = QueryBuilder.start(Field_MsgID).in(msgIds).get();

            BasicDBObject updater = new BasicDBObject("$set", new BasicDBObject(Field_Read_Time, Util.currentTime()));

//            if(from != null && !from.isEmpty()){
//                DBCollection cltFrom = getDB(from).getCollection( from );
//                cltFrom.update( query, updater,false, true);
//            }
            if (userId != null && !userId.isEmpty()) {
                DBCollection clt = getDB(userId).getCollection(userId);
                clt.update(query, updater, false, true);
            }

            return result;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
    }

    @Override
    public boolean updateReadMessage(String userId, String msgId) {
        if (msgId != null && !msgId.isEmpty()) {
            List<String> msgIds = Arrays.asList(msgId);
            return updateReadMessage(userId, msgIds);
        }
        return false;
    }

    private DB getDB(String collectionName) {
//        String dbName = getDBName(collectionName);
//        if (dbName != null) {
//            if (dbName.trim().equals(ChatlogdbKey.DB_NAME)) {
//                return db;
//            }
//        }
        return dbExtension;
    }
    
    private String getDBName(String collectionName) {
        String result = null;
        try {
            ObjectId id = new ObjectId(collectionName);
            DBObject obj = chatDBColl.findOne(new BasicDBObject(ObjectID, id));
            if (obj != null) {
                result = (String) obj.get(Field_Chat_DB_Name);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    //HUNGDT 20425
    @Override
    public boolean updateCallMessage(String from, String to, String callId, String value, String msgId) {
        boolean result = true;
//        if( msg.msgType == MessageType.FILE ){
//            LoggingThread.isConfirmFile( msg );
//        }

        try {
//            BasicDBObject query = new BasicDBObject( Field_MsgType, MessageType.FILE.toString() )
//                    .append( Field_MsgID, SendFileMessage.getMsgID( msg.value ) );
            DBCollection cltFrom = getDB(from).getCollection(from);
            DBCollection cltTo = getDB(to).getCollection(to);

            BasicDBObject query = new BasicDBObject(Field_CallID, callId);

            DBObject objFrom = cltFrom.findOne(query);
            DBObject objTo = cltTo.findOne(query);

            if (objFrom == null || objTo == null) {

                result = false;

            } else {
                //BasicDBObject updateObject = new BasicDBObject(Field_Value, value).append(Field_MsgID, msgId);
                BasicDBObject updateObject = new BasicDBObject(Field_Value, value);
//                BasicDBObject updater = new BasicDBObject( "$set", new BasicDBObject( Field_Value, msg.value ) );
                BasicDBObject updater = new BasicDBObject("$set", updateObject);

                cltFrom.update(query, updater);

                cltTo.update(query, updater);
            }

            return result;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
    }

    public boolean updateCallMessageUser(String userId, String callId, String value, String msgId) {
        boolean result = true;

        try {
            DBCollection clt = getDB(userId).getCollection(userId);

            BasicDBObject query = new BasicDBObject(Field_CallID, callId);

            DBObject obj = clt.findOne(query);
            if (obj == null) {
                return false;
            } else {
                BasicDBObject updateObj = new BasicDBObject(Field_Value, value).append(Field_MsgID, msgId);

                BasicDBObject update = new BasicDBObject("$set", updateObj);

                clt.update(query, update);
            }

            return result;
        } catch (Exception e) {
            Util.addErrorLog(e);
            return false;
        }
    }
    
    //Linh add 2017/06/27 #9088
    @Override
    public void delMsg(String userId, List<String> msgIdList){
        DBCollection clt = getDB(userId).getCollection(userId);
        BasicDBList listId = new BasicDBList();
        listId.addAll(msgIdList);
        DBObject findquery = new BasicDBObject("$in", listId);
        DBObject findObj = new BasicDBObject(Field_MsgID, findquery);
        DBObject query = new BasicDBObject(Field_IsDel, true);
        DBObject updateObj = new BasicDBObject("$set", query);
        clt.updateMulti(findObj, updateObj);
    }
    
    @Override
    public Message getLastMessage(String userId, String friendId){
        DBCollection clt = getDB(userId).getCollection(userId);
        BasicDBList query = new BasicDBList();
        query.add(new BasicDBObject(Field_MsgID, new BasicDBObject("$regex", userId)));
        query.add(new BasicDBObject(Field_MsgID, new BasicDBObject("$regex", friendId)));
        DBObject findObj = new BasicDBObject();
        findObj.put("$and", query);
        findObj.put(Field_IsDel, false);
        DBObject sortObj = new BasicDBObject(Field_OriginTime, -1);
        DBCursor cursor = clt.find(findObj).sort(sortObj);
        while(cursor.hasNext()){
            DBObject obj = cursor.next();
            Message msg = castDBObject(obj);
            return msg;
        }
        return null;
    }

    @Override
    public List<String> getFileValueById(String userId, List<String> listMsgId) {
        List<String> result = new LinkedList<>();
        DBCollection clt = getDB(userId).getCollection(userId);
        BasicDBList listId = new BasicDBList();
        listId.addAll(listMsgId);
        DBObject query = new BasicDBObject();
        query.put(Field_MsgID, new BasicDBObject("$in", listId));
        query.put(Field_MsgType, "FILE");
        DBCursor cursor = clt.find(query);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String value = (String) obj.get(Field_Value);
            result.add(value);
        }
        return result;
    }

    @Override
    public void removeButKeepFavoristFriends(String userId, List<String> llFav) {
        DBCollection clt = getDB(userId).getCollection(userId);
        BasicDBList and = new BasicDBList();
        and.add(new BasicDBObject(Field_From, new BasicDBObject("$nin", llFav)));
        and.add(new BasicDBObject(Field_To, new BasicDBObject("$nin", llFav)));
        and.add(new BasicDBObject(Field_IsDel, new BasicDBObject("$ne", true)));
        DBObject findQuery = new BasicDBObject("$and", and);
        DBObject updateObj = new BasicDBObject("$set", new BasicDBObject(Field_IsDel, true));
        Util.addDebugLog("---------- findQuery: "+findQuery);
        Util.addDebugLog("---------- updateObj: "+updateObj);
        clt.updateMulti(findQuery, updateObj);
    }

    private void saveFileChatLog(Message msg) {
        //files has 1 elements
        JSONArray array = msg.files;
        Iterator<JSONObject> it = array.iterator();
        while (it.hasNext()) {
            JSONObject jsonObject = (JSONObject) it.next();
            BasicDBObject o = castMsgTypeFile(msg,jsonObject);
            o = o.append(Field_IsDel, false);

            boolean isBlocked = BlockUserManager.isBlock(msg.from, msg.to);
            if (!isBlocked) {
                if (msg.to != null && !msg.to.isEmpty()) {
                    DBCollection colectionTo = getDBFileChat().getCollection(msg.to);
                    colectionTo.insert(o);
                }
            }

            if (msg.from != null && !msg.from.isEmpty()) {
                DBCollection colectionFrom = getDBFileChat().getCollection(msg.from);
                colectionFrom.insert(o);
            }
        }
    }

    private BasicDBObject castMsgTypeFile(Message msg,JSONObject jsonObject) {
        
        String thumbnailUrl = (String)jsonObject.get("thumbnail_url");
        String originalUrl = (String)jsonObject.get("original_url");
        String fileId = (String)jsonObject.get("file_id");
        String fileType = (String)jsonObject.get("file_type");
        Long fileDuration = (Long)jsonObject.get("file_duration");
        String fileUrl = (String)jsonObject.get("file_url");
        Long originalWidth = (Long)jsonObject.get("original_width");
        Long originalHeight = (Long)jsonObject.get("original_height");
        
        BasicDBObject obj = new BasicDBObject().append(Field_MsgID, msg.id).append(Field_From, msg.from).append(Field_To, msg.to)
                .append(Field_MsgType, msg.msgType.toString())
                .append(Field_OriginTime, msg.originTime)
                .append(Field_ServerTime, msg.serverTime).append(Field_DestTime, msg.destinationTime);
        if (msg.ip != null) {
            obj.append(Field_Ip, msg.ip);
        }
        if (thumbnailUrl != null) {
            obj.append(Field_ThumbnailUrl, thumbnailUrl);
        }
        if(originalUrl != null){
            obj.append(Field_OriginalUrl, originalUrl);
        }
        if(fileId != null){
            obj.append(Field_MsgFileId, fileId);
        }
        if(fileType != null){
            obj.append(Field_MsgType, fileType);
        }
        if(fileDuration != null){
            obj.append(Field_Duration, fileDuration);
        }
        if(fileUrl != null){
            obj.append(Field_FileUrl, fileUrl);
        }
        if(originalWidth != null){
            obj.append("original_width", originalWidth);
        }
        if(originalHeight != null){
            obj.append("original_height", originalHeight);
        }

        return obj;
    }

    private DB getDBFileChat() {
        return dbChatLog;
    }
    
    @Override
    public String getFileChat(String userId, String friendId,Long take,Long skip,Long type, Long sort){
        DBCollection clt1 = getDBFileChat().getCollection(userId);
        JSONObject jsonObject = new JSONObject();
        JSONArray arrayImage = new JSONArray();
        JSONArray arrayVideo = new JSONArray();
        JSONArray arrayAudio = new JSONArray();
        JSONArray arrayFileChat = new JSONArray();
        Integer sizeFile = 0;
        try {
            BasicDBObject objFrom = new BasicDBObject(Field_From, friendId);
            BasicDBObject objTo = new BasicDBObject(Field_To, friendId);
            BasicDBList fromOrTo = new BasicDBList();
            fromOrTo.add(objFrom);
            fromOrTo.add(objTo);
            
            DBObject obj = new BasicDBObject( "$or", fromOrTo );
            obj.put(Field_IsDel, new BasicDBObject("$ne", true));
            Util.addDebugLog("obj========================="+obj.toString());
            DBCursor find = null;
            if(sort == 1){
                if(take == null && skip == null){
                    find = clt1.find(obj).sort(OrderBy_ServerTime_Asce);
                }else{
                    find = clt1.find(obj).sort(OrderBy_ServerTime_Asce).skip(skip.intValue()).limit(take.intValue());
                }
            }else{
                if(take == null && skip == null){
                    find = clt1.find(obj).sort(OrderBy_ServerTime_Desc);
                }else{
                    find = clt1.find(obj).sort(OrderBy_ServerTime_Desc).skip(skip.intValue()).limit(take.intValue());
                }
            }
            while(find.hasNext()){
                DBObject fileChat  = find.next();
                String fileType = (String)fileChat.get("type");
                if(type == -1){
                    if (File_Type_Audio.equals(fileType)) {
                        JSONObject audio = new JSONObject();
                        createJsonArrayAudio(audio, arrayFileChat, fileChat);
                    }
                    if (File_Type_Image.equals(fileType)) {
                        JSONObject image = new JSONObject();
                        createJsonArrayImage(image, arrayFileChat, fileChat);
                    }
                    if (File_Type_Video.equals(fileType)) {
                        JSONObject video = new JSONObject();
                        createJsonArrayVideo(video, arrayFileChat, fileChat);
                    }
                }else{
                    if(File_Type_Audio.equals(fileType)){
                        JSONObject audio = new JSONObject();
                        createJsonArrayAudio(audio,arrayAudio,fileChat);
                    }
                    if(File_Type_Image.equals(fileType)){
                        JSONObject image = new JSONObject();
                        createJsonArrayImage(image,arrayImage,fileChat);
                    }
                    if(File_Type_Video.equals(fileType)){
                        JSONObject video = new JSONObject();
                        createJsonArrayVideo(video,arrayVideo,fileChat);
                    }
                }
            }
            sizeFile = getNumberFile(obj, clt1);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        
        if(type == null || type == -1){
            jsonObject.put(Size_All_File, sizeFile);
            jsonObject.put(File_Type_ALL, arrayFileChat);
        }else{
            if(type == 0){
                jsonObject.put(File_Type_Image, arrayImage);
                jsonObject.put(File_Type_Audio, arrayAudio);
                jsonObject.put(File_Type_Video, arrayVideo);
                jsonObject.put(Size_All_File, sizeFile);
            }else if(type == 1){
                jsonObject.put(File_Type_Image, arrayImage);
//                jsonObject.put(Size_Image, arrayImage.size());
            }else if(type == 2){
                jsonObject.put(File_Type_Video, arrayVideo);
//                jsonObject.put(Size_Video, arrayVideo.size());
            }else{
                jsonObject.put(File_Type_Audio, arrayAudio);
//                jsonObject.put(Size_Audio, arrayAudio.size());
            }
        }
        jsonObject.put("user_id", userId);
        jsonObject.put("friend_id", friendId);
        return jsonObject.toJSONString();
    }
    
    private static void createJsonArrayImage(JSONObject image, JSONArray array,DBObject fileChat){
//        image.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_HEIGHT, (Double) fileChat.get("thumbnail_height"));
        image.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_URL, (String) fileChat.get("thumbnail_url"));
//        image.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_WIDTH, (Double) fileChat.get("THUMBNAIL_WIDTH"));
        image.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_HEIGHT, (Long) fileChat.get("original_height"));
        image.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_WIDTH, (Long) fileChat.get("original_width"));
        image.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_URL, (String) fileChat.get("original_url"));
        image.put(StaticFiledbKey.FILE_CHAT.FILE_ID, (String) fileChat.get("msg_file_id"));
        image.put("type", "image");
        Long serverTime = (Long) fileChat.get("st");
        image.put(StaticFiledbKey.FILE_CHAT.TIME_SENDER, DateFormat.format_yyyyMMddHHmmssSSS(serverTime));
        array.add(image);
    }
    private static void createJsonArrayVideo(JSONObject video, JSONArray array,DBObject fileChat){
//        video.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_HEIGHT, (Double) fileChat.get("thumbnail_height"));
        video.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_URL, (String) fileChat.get("thumbnail_url"));
//        video.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_WIDTH, (Double) fileChat.get("THUMBNAIL_WIDTH"));
//        video.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_HEIGHT, (Double) fileChat.get("ORIGINAL_HEIGHT"));
//        video.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_WIDTH, (Double) fileChat.get("ORIGINAL_WIDTH"));
        video.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_URL, (String) fileChat.get("original_url"));
        video.put(StaticFiledbKey.FILE_CHAT.FILE_ID, (String) fileChat.get("msg_file_id"));
        video.put(StaticFiledbKey.FILE_CHAT.FILE_DURATION, (Long) fileChat.get(StaticFiledbKey.FILE_CHAT.FILE_DURATION));
        video.put(StaticFiledbKey.FILE_CHAT.FILE_URL, (String) fileChat.get(StaticFiledbKey.FILE_CHAT.FILE_URL));
        video.put("type", "video");
        Long serverTime = (Long) fileChat.get("st");
        video.put(StaticFiledbKey.FILE_CHAT.TIME_SENDER, DateFormat.format_yyyyMMddHHmmssSSS(serverTime));
        array.add(video);
    }
    
    private static void createJsonArrayAudio(JSONObject video, JSONArray array,DBObject fileChat){
//        video.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_HEIGHT, (Double) fileChat.get("thumbnail_height"));
        video.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_URL, (String) fileChat.get("thumbnail_url"));
//        video.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_WIDTH, (Double) fileChat.get("THUMBNAIL_WIDTH"));
//        video.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_HEIGHT, (Double) fileChat.get("ORIGINAL_HEIGHT"));
//        video.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_WIDTH, (Double) fileChat.get("ORIGINAL_WIDTH"));
        video.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_URL, (String) fileChat.get("original_url"));
        video.put(StaticFiledbKey.FILE_CHAT.FILE_ID, (String) fileChat.get("msg_file_id"));
        video.put(StaticFiledbKey.FILE_CHAT.FILE_DURATION, (Long) fileChat.get(StaticFiledbKey.FILE_CHAT.FILE_DURATION));
        video.put(StaticFiledbKey.FILE_CHAT.FILE_URL, (String) fileChat.get(StaticFiledbKey.FILE_CHAT.FILE_URL));
        video.put("type", "audio");
        Long serverTime = (Long) fileChat.get("st");
        video.put(StaticFiledbKey.FILE_CHAT.TIME_SENDER, DateFormat.format_yyyyMMddHHmmssSSS(serverTime));
        array.add(video);
    }
    
    private Integer getNumberFile(DBObject obj, DBCollection clt1){
        return clt1.find(obj).count();
    }
    
    @Override
    public void delFileChat(String userid, List<String> llFriends) {
        DBCollection clt = getDBFileChat().getCollection(userid);

        LinkedList<BasicDBObject> llDBO = new LinkedList<>();
        for (String fid : llFriends) {
            BasicDBObject o1 = new BasicDBObject(Field_From, fid);
            BasicDBObject o2 = new BasicDBObject(Field_To, fid);
            llDBO.add(o1);
            llDBO.add(o2);
        }

        BasicDBObject[] oarr = llDBO.toArray(new BasicDBObject[llDBO.size()]);
        BasicDBObject query = new BasicDBObject("$or", oarr);

//        clt.remove( query );
        BasicDBObject updater = new BasicDBObject("$set", new BasicDBObject(Field_IsDel, true));
        clt.update(query, updater, false, true);
    }
    
    @Override
    public void removeAllFileChat(String userId, List<String> llFav) {
        DBCollection clt = getDBFileChat().getCollection(userId);
        BasicDBList and = new BasicDBList();
        and.add(new BasicDBObject(Field_From, new BasicDBObject("$nin", llFav)));
        and.add(new BasicDBObject(Field_To, new BasicDBObject("$nin", llFav)));
        and.add(new BasicDBObject(Field_IsDel, new BasicDBObject("$ne", true)));
        DBObject findQuery = new BasicDBObject("$and", and);
        DBObject updateObj = new BasicDBObject("$set", new BasicDBObject(Field_IsDel, true));
        Util.addDebugLog("---------- findQuery: "+findQuery);
        Util.addDebugLog("---------- updateObj: "+updateObj);
        clt.updateMulti(findQuery, updateObj);
    }
}
