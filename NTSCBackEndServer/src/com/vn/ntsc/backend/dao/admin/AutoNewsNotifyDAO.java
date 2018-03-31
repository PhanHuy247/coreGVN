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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.automessage.Message;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoNewsNotify;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;

/**
 *
 * @author hungdt
 */
public class AutoNewsNotifyDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.AUTO_MESSAGE.AUTO_NEWS_NOTIFY_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static String insert(String url, String content, String ip, String time, String query) {
        String result = "";
        try {
            DBObject insertObj = new BasicDBObject();
            insertObj.put(SettingdbKey.AUTO_MESSAGE.AUTO_NOTIFY.URL, url);
            insertObj.put(SettingdbKey.AUTO_MESSAGE.MESSAGE, content);
            insertObj.put(SettingdbKey.AUTO_MESSAGE.TIME, time);
            insertObj.put(SettingdbKey.AUTO_MESSAGE.QUERY, query);
            insertObj.put(SettingdbKey.AUTO_MESSAGE.AUTO_NOTIFY.IP, ip);
            insertObj.put(SettingdbKey.AUTO_MESSAGE.FLAG, Constant.FLAG.ON);
           
            coll.insert(insertObj);
            result = insertObj.get(SettingdbKey.AUTO_MESSAGE.ID).toString();
           ///System.out.println("commonDAO : " + result);
        } catch (Exception ex) {
           // System.out.println("insert error");
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static boolean update(String id, String url, String content, String time) throws EazyException {
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
        updateObj.put(SettingdbKey.AUTO_MESSAGE.MESSAGE, content);
        updateObj.put(SettingdbKey.AUTO_MESSAGE.AUTO_NOTIFY.URL, url);
        updateObj.put(SettingdbKey.AUTO_MESSAGE.TIME, time);
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
//                Util.addDebugLog("AutoNews check " +flag);
                if (flag == Constant.FLAG.ON) {
                    String id = dbO.get(SettingdbKey.AUTO_MESSAGE.ID).toString();
                    String timeStr = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.TIME);
                    String content = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.MESSAGE);
                    String ip = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.AUTO_NOTIFY.IP);
                    String queryStr = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.QUERY);
                    String url = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.AUTO_NOTIFY.URL);
                    JSONObject query = (JSONObject) new JSONParser().parse(queryStr);
                    result.put(id, new AutoNewsNotify(content, url, ip, id, timeStr, query, flag));
//                    Util.addDebugLog("AutoNews check " +result);
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
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.FLAG, flag);
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
               String content = obj.getString(SettingdbKey.AUTO_MESSAGE.MESSAGE);
               Integer flg = obj.getInt(SettingdbKey.AUTO_MESSAGE.FLAG);
               Integer receiverNumber = (Integer) obj.get(SettingdbKey.AUTO_MESSAGE.RECEIVER_NUMBER);
               BasicDBList clickedUsers = (BasicDBList) obj.get(SettingdbKey.AUTO_MESSAGE.CLICKED_USERS);
               int clickedUsersNumber = clickedUsers == null ? 0 : clickedUsers.size();
               Double rationClickedUser = null;
               if(receiverNumber != null){
                    rationClickedUser = formatDouble(((double) clickedUsersNumber/receiverNumber) *100F);
               }
               String url = obj.getString(SettingdbKey.AUTO_MESSAGE.AUTO_NOTIFY.URL);
               AutoNewsNotify autoNotify = new AutoNewsNotify(content, url, id, time, flg, receiverNumber);
               autoNotify.clickUserNumber = clickedUsersNumber;
               autoNotify.ratioClickedUser = rationClickedUser;
               list.add(autoNotify);
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
    
    public static AutoNewsNotify get(String id) throws EazyException {
        try {
            
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.AUTO_MESSAGE.ID, oId);
            DBObject dbO = coll.findOne(findObj);
            String timeStr = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.TIME);
            String content = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.MESSAGE);
            String queryStr = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.QUERY);
            String url = (String) dbO.get(SettingdbKey.AUTO_MESSAGE.AUTO_NOTIFY.URL);
            JSONObject query = (JSONObject) new JSONParser().parse(queryStr);
            Integer receiverNumber = (Integer) dbO.get(SettingdbKey.AUTO_MESSAGE.RECEIVER_NUMBER);
            BasicDBList clickedUsers = (BasicDBList) dbO.get(SettingdbKey.AUTO_MESSAGE.CLICKED_USERS);
            int clickedUsersNumber = clickedUsers == null ? 0 : clickedUsers.size();
            Double rationClickedUser = null;
            if(receiverNumber != null){
                 rationClickedUser = formatDouble(((double) clickedUsersNumber/receiverNumber) *100F);
            }
            AutoNewsNotify autoNotify = new AutoNewsNotify(id, content, url, timeStr, query, receiverNumber);
            autoNotify.clickUserNumber = clickedUsersNumber;
            autoNotify.ratioClickedUser = rationClickedUser;
            return autoNotify;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    private static Double formatDouble(double input){
        DecimalFormat df = new DecimalFormat("#.#");      
        return Double.valueOf(df.format(input));
    }  

    
}
