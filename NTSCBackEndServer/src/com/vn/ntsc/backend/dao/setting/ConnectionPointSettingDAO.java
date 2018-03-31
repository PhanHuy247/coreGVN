/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.setting;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Iterator;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.DAOKeys;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.DBManager;

/**
 *
 * @author RuAc0n
 */
public class ConnectionPointSettingDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.CONNECTION_POINT_SETTING_COLLECTION);
            coll.createIndex(new BasicDBObject(SettingdbKey.CONNECTION_POINT_SETTING.TYPE, 1));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean update(JSONObject json) throws EazyException {
        boolean result = false;
        try {
            for (String type : DAOKeys.connection_type_list ) {
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.CONNECTION_POINT_SETTING.TYPE, type);
                JSONObject records = (JSONObject) json.get(type);
                Iterator itRecords = records.keySet().iterator();
                while(itRecords.hasNext()){
                    String pairs = (String) itRecords.next();
                    findObj.append(SettingdbKey.CONNECTION_POINT_SETTING.SENDER_RECEIVER, pairs);
                    
                    JSONObject dataObj = (JSONObject) records.get(pairs);
                    
                    BasicDBObject updateObject = new BasicDBObject();
                    JSONObject callerJson = (JSONObject) dataObj.get(SettingdbKey.CONNECTION_POINT_SETTING.SENDER);
                    Long value = (Long) callerJson.get(DAOKeys.value);
                    if (value == null){
                        value = 0L;
                    }
                    updateObject.append(SettingdbKey.CONNECTION_POINT_SETTING.SENDER, value.intValue());
                    
                    JSONObject potentialCallerJson = (JSONObject) dataObj.get(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER);
                    value = (Long) potentialCallerJson.get(DAOKeys.value);
                    if(value == null){
                        value = 0L;
                    }
                    updateObject.append(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER, value.intValue());
                    
                    JSONObject receiverJson = (JSONObject) dataObj.get(SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER);
                    value = (Long) receiverJson.get(DAOKeys.value);
                    if (value == null){
                        value = 0L;
                    }
                    updateObject.append(SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER, value.intValue());
                    
                    JSONObject potentialreceiverJson = (JSONObject) dataObj.get(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER);
                    value = (Long) potentialreceiverJson.get(DAOKeys.value);
                    if (value == null){
                        value = 0L;
                    }
                    updateObject.append(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER, value.intValue());
                    
                    BasicDBObject setObj = new BasicDBObject("$set", updateObject);
                    
                    coll.update(findObj, setObj, true, false);
                }
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static JSONObject get() throws EazyException {
        JSONObject result = new JSONObject();
        try {
            for (String type : DAOKeys.connection_type_list ) {
                JSONObject data = new JSONObject();
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.CONNECTION_POINT_SETTING.TYPE, type);
                DBCursor cursor = coll.find(findObj);
                while(cursor.hasNext()){
                    DBObject obj = cursor.next();
                    String pairs = (String) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.SENDER_RECEIVER);
                    
                    String [] elements = pairs.split("_");
                    
                    JSONObject dataJson = new JSONObject();
                    
                    JSONObject receiverJson = new JSONObject();
                    Integer value = (Integer) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER);
                    receiverJson.put(DAOKeys.name, elements[1]);
                    receiverJson.put(DAOKeys.value, value);
                    dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER, receiverJson);
                    
                    JSONObject potentialReceiverJson = new JSONObject();
                    value = (Integer) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER);
                    potentialReceiverJson.put(DAOKeys.name, elements[1]);
                    potentialReceiverJson.put(DAOKeys.value, value);
                    dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER, potentialReceiverJson);
                    
                    JSONObject callerJson = new JSONObject();
                    value = (Integer) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.SENDER);
                    callerJson.put(DAOKeys.name, elements[0]);
                    callerJson.put(DAOKeys.value, value);
                    dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.SENDER, callerJson);    
                    
                    JSONObject potentialCallerJson = new JSONObject();
                    value = (Integer) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER);
                    potentialCallerJson.put(DAOKeys.name, elements[0]);
                    potentialCallerJson.put(DAOKeys.value, value);
                    dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER, potentialCallerJson);                    
                    
                    data.put(pairs, dataJson);
                    
                }
                result.put(type, data);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
