/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.chat;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.ChatlogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import java.util.Date;
import eazycommon.util.Util;
import java.util.Collections;
import java.util.Objects;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.admin.SystemAccount;
import com.vn.ntsc.backend.entity.impl.chat.Message;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.user.User;

public class ChatDAO {

    private static final String Field_From = "from";
    private static final String Field_To = "to";
    private static final String Field_MsgType = "type";
    private static final String Field_Value = "value";
    private static final String Field_ServerTime = "st";
    private static final String Field_Ip = "ip";
    public static final String ObjectID = "_id";
    private static final String Field_MsgRT = "rt";
    private static final String Field_IsLock = "islock";

    private static DBCollection chatDBColl;
    public static final String Chat_DB_Collection = "chat_db";
    public static final String Field_Chat_DB_Name = "chat_db_name";

    static {
        try {
            chatDBColl = DBManager.getUserDB().getCollection(Chat_DB_Collection);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    private static DB getDB(String collectionName) {
        String dbName = getDBName(collectionName);
        if (dbName != null) {
            if (dbName.trim().equals(ChatlogdbKey.DB_NAME)) {
                return DBManager.getChatDB();
            }
        }
        return DBManager.getChatExtensionDB();
    }

    private static String getDBName(String collectionName) {
        String result = null;
        try {
//            Util.addInfoLog("Collection Name :" + collectionName);
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

    //thanhdd add 04/11/2016 #5186
    public static SizedListData getMessagesByTypeUser(String userId, List<String> friendList, Long fromTime, Long toTime, Long sort, Long order,
            Long skip, Long take, boolean isCsv, Long by_user_type, Long message_type) throws EazyException {
        SizedListData map = new SizedListData();
        try {
            DBCollection clt = getDB(userId).getCollection(userId);
            BasicDBObject findObj = new BasicDBObject();

            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(Field_ServerTime, lte);
                ands[1] = new BasicDBObject(Field_ServerTime, gte);
                findObj.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObj.append(Field_ServerTime, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObj.append(Field_ServerTime, lte);
                }
            }

            if (by_user_type != null){
                if (by_user_type == 1){
                    findObj.append(Field_From, userId);
                }
                else if (by_user_type == 0){
                    findObj.append(Field_To, userId);
                }
            }
            
            if (message_type != null){
                if (message_type == 1){
                    findObj.append(Field_MsgType, "FILE");
                    findObj.append(Field_Value, new BasicDBObject("$regex", "/|p|/"));
                }
                else if (message_type == 2){
                    findObj.append(Field_MsgType, "FILE");
                    findObj.append(Field_Value, new BasicDBObject("$regex", "/|v|/"));
                }
            }
            
            
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(Field_ServerTime, or);
            }
            
            Util.addDebugLog("============== findObj: "+findObj);
            DBCursor cursor;
            if (findObj.isEmpty()) {
                cursor = clt.find().sort(sortObj);
            } else {
                cursor = clt.find(findObj).sort(sortObj);
            }
            
            int size = cursor.size();
            if (!isCsv) { 
                cursor.skip(skip.intValue()).limit(take.intValue());
            }

            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String msgValue = (String) obj.get(Field_Value);
                if (message_type == 1 && msgValue.contains("|p|")){
                    Message msg = castDBObject(obj, userId);
                    list.add(msg);
                }
                else if (message_type == 2 && msgValue.contains("|v|")){
                    Message msg = castDBObject(obj, userId);
                    list.add(msg);
                }
                else if (message_type != 1 && message_type != 2) {
                    Message msg = castDBObject(obj, userId);
                    list.add(msg);
                }

            }
            map = new SizedListData(size, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return map;
    }

    public static SizedListData getMessages(String userId, List<String> friendList, Long fromTime, Long toTime, Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData map = new SizedListData();
        try {
            DBCollection clt = getDB(userId).getCollection(userId);
            BasicDBObject findObj = new BasicDBObject();
//            if(friendList == null){
//                friendList = new ArrayList<String>();
//                friendList.add(userId);    
//            }
//            if (friendList != null) {
//                BasicDBObject inObj = new BasicDBObject("$in", friendList);
//                BasicDBObject[] ors = new BasicDBObject[2];
//                ors[0] = new BasicDBObject(Field_From, inObj);
//                ors[1] = new BasicDBObject(Field_To, inObj);
//                findObj.append("$or", ors); 
////                findObj.append(Field_From, inObj);
////                findObj.append(Field_To, inObj);
//            }
            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(Field_ServerTime, lte);
                ands[1] = new BasicDBObject(Field_ServerTime, gte);
                findObj.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObj.append(Field_ServerTime, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObj.append(Field_ServerTime, lte);
                }
            }

            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(Field_ServerTime, or);
            }
            DBCursor cursor;
            if (findObj.isEmpty()) {
                cursor = clt.find().sort(sortObj);
            } else {
                cursor = clt.find(findObj).sort(sortObj);
            }

            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                Message msg = castDBObject(cursor.next(), userId);
                if (msg != null) {
                    //msg.content = Util.replaceWordChatBackend(msg.content);
                    if (friendList != null) {
                        if (friendList.contains(msg.userId)) {
                            list.add(msg);
                        }
                    } else {
                        list.add(msg);
                    }
                }
            }
            int size = list.size();
            if (!isCsv) {
                if (skip > list.size()) {
                    map = new SizedListData(size, new ArrayList<IEntity>());
                    return map;
                }
                int startIndex = skip.intValue();
                int endIndex = skip.intValue() + take.intValue();
                if (endIndex > list.size()) {
                    endIndex = list.size();
                }
                list = list.subList(startIndex, endIndex);
            }
            map = new SizedListData(size, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return map;
    }

    // Fixbug #4662 - ThanhDD
    public static SizedListData getMessagesToLog(String userId, List<String> friendList,
            Long fromTime, Long toTime, Long sort, Long order, Long skip, Long take, boolean isCsv, Long gender) throws EazyException {
        SizedListData map = new SizedListData();
        try {
            DBCollection clt = getDB(userId).getCollection(userId);
            BasicDBObject findObj = new BasicDBObject();

            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(Field_ServerTime, lte);
                ands[1] = new BasicDBObject(Field_ServerTime, gte);
                findObj.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObj.append(Field_ServerTime, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObj.append(Field_ServerTime, lte);
                }
            }

            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(Field_ServerTime, or);
            }
            DBCursor cursor;
            if (findObj.isEmpty()) {
                cursor = clt.find().sort(sortObj);
            } else {
                cursor = clt.find(findObj).sort(sortObj);
            }

            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                Message msg = castDBObjectToLog(cursor.next(), userId, gender);

                if (msg != null) {
                    if (friendList != null) {
                        if (friendList.contains(msg.userId)) {
                            list.add(msg);
                        }
                    } else {
                        list.add(msg);
                    }
                }
            }
            int size = list.size();

            if (skip > list.size()) {
                map = new SizedListData(size, new ArrayList<IEntity>());
                return map;
            } else {
                int startIndex = skip.intValue();
                int endIndex = skip.intValue() + take.intValue();
                if (endIndex > size) {
                    endIndex = size;
                }
                list = list.subList(startIndex, endIndex);
            }

            if (!isCsv) {
                if (skip > list.size()) {
                    map = new SizedListData(size, new ArrayList<IEntity>());
                    return map;
                }
                int startIndex = skip.intValue();
                int endIndex = skip.intValue() + take.intValue();
                if (endIndex > list.size()) {
                    endIndex = list.size();
                }
                list = list.subList(startIndex, endIndex);
            }
            map = new SizedListData(size, list);

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return map;
    }

    public static SizedListData getMessagesToLogOrder(String userId, List<String> friendList, Long by_user_type,//thanhdd add role param
            Long fromTime, Long toTime, Long sort, Long order, Long skip, boolean isCsv, Long gender) throws EazyException {
        SizedListData map = new SizedListData();
        try {
            DBCollection clt = getDB(userId).getCollection(userId);
            BasicDBObject findObj = new BasicDBObject();

            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(Field_ServerTime, lte);
                ands[1] = new BasicDBObject(Field_ServerTime, gte);
                findObj.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObj.append(Field_ServerTime, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObj.append(Field_ServerTime, lte);
                }
            }

            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(Field_ServerTime, or);
            }
            DBCursor cursor;
            if (findObj.isEmpty()) {
                cursor = clt.find().sort(sortObj);
            } else {
                cursor = clt.find(findObj).sort(sortObj);
            }

            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                Message msg = castDBObjectToLog(cursor.next(), userId, gender);
                if (by_user_type != null && (by_user_type == 1 || by_user_type == 0)) { //thanhdd edit
                    if (msg != null) {
                        if (friendList != null) {
                            if (friendList.contains(msg.userId)) {
                                // if owner
                                if (msg.isOwn == Integer.parseInt(by_user_type.toString())) {
                                    list.add(msg);
                                }

                            }
                        } else // if owner
                         if (msg.isOwn == Integer.parseInt(by_user_type.toString())) {
                                list.add(msg);
                            }
                    }
                } else if (friendList != null) {
                    if (friendList.contains(msg.userId)) {
                        list.add(msg);
                    }
                } else {
                    list.add(msg);
                }
            }

            int size = list.size();

            if (skip > list.size()) {
                map = new SizedListData(size, new ArrayList<IEntity>());
                return map;
            } else {
                int startIndex = skip.intValue();
                if (startIndex > size) {
                    startIndex = size;
                }
                list = list.subList(startIndex, size);
            }

            if (!isCsv) {
                if (skip > list.size()) {
                    map = new SizedListData(size, new ArrayList<IEntity>());
                    return map;
                }
                int startIndex = skip.intValue();
                if (startIndex > list.size()) {
                    startIndex = list.size();
                }
                list = list.subList(startIndex, size);
            }
            map = new SizedListData(size, list);

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return map;
    }

    public static Message castDBObject(DBObject o, String userId) {
        if (o == null) {
            return null;
        }
        Message msg = new Message();
        try {
            String fromId = (String) o.get(Field_From);
            String toId = (String) o.get(Field_To);
            if (fromId != null) {
                if (fromId.equals(userId)) {
                    msg.isOwn = Constant.FLAG.ON;
                    msg.userId = toId;
                    msg.reqId = fromId;
                } else {
                    msg.isOwn = Constant.FLAG.OFF;
                    msg.userId = fromId;
                    msg.reqId = toId;
                }
            }

            List<IEntity> listSysAcc = UserDAO.getAllSystemAcc();
            IEntity user = null;
            SystemAccount account = null;

            int isRead = 0;

            int isAdmin = 0;
            for (int i = 0; i < listSysAcc.size(); i++) {
                user = listSysAcc.get(i);
                if (user instanceof SystemAccount) {
                    account = (SystemAccount) user;
                }

                if (msg.userId.equals(account.id)) {
                    isAdmin = 1;
                    break;
                }
            }
            msg.isAdmin = isAdmin;

            //msg.messageReadTime = (String) o.get(Field_MsgRT);
            if(o.get(Field_MsgRT) != null) {
                msg.messageReadTime = o.get(Field_MsgRT).toString();
            }
            if (msg.messageReadTime != null) {
                isRead = 1;
            }
            msg.isRead = isRead;
            
            if (o.get(Field_IsLock) !=null){
                msg.isLock = (Integer) o.get(Field_IsLock);
            } else {
                msg.isLock=0;
            }
            
            msg.messageType = (String) o.get(Field_MsgType);
            msg.content = (String) o.get(Field_Value);
            Long time = (Long) o.get(Field_ServerTime);
            if (time != null) {
                msg.time = DateFormat.format(time);
            }

            msg.ip = (String) o.get(Field_Ip);

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            msg = null;
        }
        return msg;
    }

    public static Message castDBObjectToLog(DBObject o, String userId, Long gender) {
        if (o == null) {
            return null;
        }

        Message msg = null;
        try {
            String fromId = (String) o.get(Field_From);
            String toId = (String) o.get(Field_To);
            if (fromId != null) {
                msg = new Message();
                if (fromId.equals(userId)) {

                    if (gender != null) {

                        User tempUser;
                        IEntity userObject;

                        userObject = UserDAO.getDetailUser(userId);

                        if (userObject == null) {
                            return null;
                        }

                        tempUser = (User) userObject;
                        if (!Objects.equals(gender, tempUser.gender)) {
                            return null;
                        }

                    }

                    msg.isOwn = Constant.FLAG.ON;
                    msg.userId = toId;
                    msg.reqId = fromId;
                } else {
//                    msg.isOwn = Constant.FLAG.OFF;
//                    msg.userId = fromId;
//                    msg.reqId = toId;
                    return null;
                }
                msg.messageType = (String) o.get(Field_MsgType);
                msg.content = (String) o.get(Field_Value);
                Long time = (Long) o.get(Field_ServerTime);
                if (time != null) {
                    msg.time = DateFormat.format(time);
                }

                msg.ip = (String) o.get(Field_Ip);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            msg = null;
        }
        return msg;
    }
}
