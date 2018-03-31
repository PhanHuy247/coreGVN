/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.dao.admin;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.automessage.Message;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoMessage;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;

/**
 *
 * @author Admin
 */
public class AutoMessageChatDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.AUTO_MESSAGE.AUTO_MESSAGE_CHAT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

     public static String insert(String sender, String message, String time, String query) throws EazyException {
        String result = "";
        DBObject insertObj = new BasicDBObject();
        insertObj.put(SettingdbKey.AUTO_MESSAGE.AUTO_MESSAGE_CHAT.SENDER, sender);
        insertObj.put(SettingdbKey.AUTO_MESSAGE.MESSAGE, message);
        insertObj.put(SettingdbKey.AUTO_MESSAGE.TIME, time);
        insertObj.put(SettingdbKey.AUTO_MESSAGE.QUERY, query);
        insertObj.put(SettingdbKey.AUTO_MESSAGE.FLAG, Constant.FLAG.ON);
        coll.insert(insertObj);
        result = insertObj.get(SettingdbKey.AUTO_MESSAGE.ID).toString();
        //System.out.println("commonDAO : " + result);
        return result;
    }

    public static boolean update(String id, String sender, String message, String time, String query) throws EazyException {
        ObjectId oId = new ObjectId(id);
        DBObject findObj = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.ID, oId);
        DBObject obj = coll.findOne(findObj);
        if (obj != null) {
            Integer flag = (Integer) obj.get(SettingdbKey.AUTO_MESSAGE.FLAG);
            if (flag == Constant.FLAG.OFF) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
        }
        DBObject updateObj = new BasicDBObject();
        updateObj.put(SettingdbKey.AUTO_MESSAGE.MESSAGE, message);
        updateObj.put(SettingdbKey.AUTO_MESSAGE.TIME, time);
        if (query != null) {
            updateObj.put(SettingdbKey.AUTO_MESSAGE.QUERY, query);
        }
        updateObj.put(SettingdbKey.AUTO_MESSAGE.AUTO_MESSAGE_CHAT.SENDER, sender);
        DBObject setObj = new BasicDBObject("$set", updateObj);
        coll.update(findObj, setObj);
        return true;
    }

    public static boolean delete(String id) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.ID, oId);
            coll.remove(findObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, Message> getAll() throws EazyException {
        Map<String, Message> result = new TreeMap<>();
        try {
            DBCursor cur = coll.find();

            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(SettingdbKey.AUTO_MESSAGE.FLAG);
                if (flag == Constant.FLAG.ON) {
                    String id = dbO.get(SettingdbKey.AUTO_MESSAGE.ID).toString();
                    String timeStr = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.TIME);
                    String mes = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.MESSAGE);
                    String sender = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.AUTO_MESSAGE_CHAT.SENDER);
                    String queryStr = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.QUERY);
                    JSONObject query = (JSONObject) new JSONParser().parse(queryStr);
                    String ip = (String) query.get("ip");
                    result.put(id, new AutoMessage(mes, sender, id, timeStr, query, flag, ip));
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SizedListData getList(Integer flag, Long skip, Long take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBCursor cursor = null;
            BasicDBObject sort = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.TIME, -1);
            if (flag != null) {
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.FLAG, flag.intValue());
                cursor = coll.find(findObj).sort(sort);
            } else {
                cursor = coll.find().sort(sort);
            }
            int size = cursor.size();
            if (skip != null && take != null) {
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            }
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.AUTO_MESSAGE.ID).toString();
                String time = obj.getString(SettingdbKey.AUTO_MESSAGE.TIME);
                String message = obj.getString(SettingdbKey.AUTO_MESSAGE.MESSAGE);
                Integer flg = obj.getInt(SettingdbKey.AUTO_MESSAGE.FLAG);
                Integer receiverNumber = (Integer) obj.get(SettingdbKey.AUTO_MESSAGE.RECEIVER_NUMBER);
                String sender = obj.getString(SettingdbKey.AUTO_MESSAGE.AUTO_MESSAGE_CHAT.SENDER);
                list.add(new AutoMessage(message, sender, id, time, flg, receiverNumber, null));
            }

            result = new SizedListData(size, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateMessage(String id, int flag, List<String> receivers) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.ID, oId);
            DBObject updateObj = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.FLAG, flag);
            if(!receivers.isEmpty())
                updateObj.put(SettingdbKey.AUTO_MESSAGE.RECEIVER_NUMBER, receivers.size());
            updateObj.put(SettingdbKey.AUTO_MESSAGE.RECEIVERS, receivers);
            DBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<String> getReceiversById(String id) throws EazyException {
        List<String> receivers = new ArrayList<>();
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.ID, oId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                receivers = (List<String>) obj.get(SettingdbKey.AUTO_MESSAGE.RECEIVERS);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return receivers;
    }
}
