/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.cmcode;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CMcodedbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.cmcode.Afficiate;

/**
 *
 * @author RuAc0n
 */
public class AfficiateDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getCmCodeDB().getCollection(CMcodedbKey.AFFICIATE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static String insert(String name, String pass, String email, int flag, String affLoginId) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(CMcodedbKey.AFFICIATE.AFFICIATE_NAME, name);
            insertObj.put(CMcodedbKey.AFFICIATE.AFFICIATE_PASSWORD, pass);
            insertObj.put(CMcodedbKey.AFFICIATE.AFFICIATE_EMAIL, email);
            insertObj.put(CMcodedbKey.AFFICIATE.FLAG, flag);
            insertObj.put(CMcodedbKey.AFFICIATE.AFFICIATE_LOGIN_ID, affLoginId);
            coll.insert(insertObj);
            result = insertObj.get(CMcodedbKey.AFFICIATE.ID).toString();
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id, String name, String pass, String email, int flag, String affLoginId) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(CMcodedbKey.AFFICIATE.ID, oId);
            DBObject updateObj = new BasicDBObject(CMcodedbKey.AFFICIATE.AFFICIATE_NAME, name);
            updateObj.put(CMcodedbKey.AFFICIATE.AFFICIATE_PASSWORD, pass);
            updateObj.put(CMcodedbKey.AFFICIATE.AFFICIATE_EMAIL, email);
            updateObj.put(CMcodedbKey.AFFICIATE.FLAG, flag);
            updateObj.put(CMcodedbKey.AFFICIATE.AFFICIATE_LOGIN_ID, affLoginId);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, IEntity> getMapName() throws EazyException {
        Map<String, IEntity> result = new TreeMap<String, IEntity>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(CMcodedbKey.AFFICIATE.ID).toString();
                String shopName = obj.getString(CMcodedbKey.AFFICIATE.AFFICIATE_NAME);
                result.put(id, new Afficiate(id, shopName, null, null, null, null));
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<IEntity> getListName() throws EazyException {
        List<IEntity> result = new ArrayList<IEntity>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(CMcodedbKey.AFFICIATE.ID).toString();
                String shopName = obj.getString(CMcodedbKey.AFFICIATE.AFFICIATE_NAME);
                result.add(new Afficiate(id, shopName, null, null, null, null));
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<IEntity> getAll() throws EazyException {
        List<IEntity> result = new ArrayList<IEntity>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(CMcodedbKey.AFFICIATE.ID).toString();
                String shopName = obj.getString(CMcodedbKey.AFFICIATE.AFFICIATE_NAME);
                String shopPass = obj.getString(CMcodedbKey.AFFICIATE.AFFICIATE_PASSWORD);
                String shopEmail = obj.getString(CMcodedbKey.AFFICIATE.AFFICIATE_EMAIL);
                Integer flag = obj.getInt(CMcodedbKey.AFFICIATE.FLAG);
                String affLoginId = obj.getString(CMcodedbKey.AFFICIATE.AFFICIATE_LOGIN_ID);
                result.add(new Afficiate(id, shopName, shopPass, shopEmail, flag, affLoginId));
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getName(String id) throws EazyException {
        String result = null;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject obj = coll.findOne(new BasicDBObject(CMcodedbKey.AFFICIATE.ID, oId));
            if (obj != null) {
                result = (String) obj.get(CMcodedbKey.AFFICIATE.AFFICIATE_NAME);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
