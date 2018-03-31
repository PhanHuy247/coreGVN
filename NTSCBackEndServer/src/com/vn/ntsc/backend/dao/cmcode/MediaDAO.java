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
import com.vn.ntsc.backend.entity.impl.cmcode.Media;

/**
 *
 * @author RuAc0n
 */
public class MediaDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getCmCodeDB().getCollection(CMcodedbKey.MEDIA_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static String insert(String afficiateId, String media, String url, int flag) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(CMcodedbKey.MEDIA.AFFICIATE_ID, afficiateId);
            insertObj.put(CMcodedbKey.MEDIA.MEDIA_NAME, media);
            insertObj.put(CMcodedbKey.MEDIA.MEDIA_URL, url);
            insertObj.put(CMcodedbKey.MEDIA.FLAG, flag);
            coll.insert(insertObj);
            result = insertObj.get(CMcodedbKey.MEDIA.ID).toString();
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id, String afficiateId, String media, String url, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(CMcodedbKey.MEDIA.ID, oId);
            DBObject updateObj = new BasicDBObject(CMcodedbKey.MEDIA.AFFICIATE_ID, afficiateId);
            updateObj.put(CMcodedbKey.MEDIA.MEDIA_NAME, media);
            updateObj.put(CMcodedbKey.MEDIA.MEDIA_URL, url);
            updateObj.put(CMcodedbKey.MEDIA.FLAG, flag);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj);
            result = true;
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
            DBObject obj = coll.findOne(new BasicDBObject(CMcodedbKey.MEDIA.ID, oId));
            if (obj != null) {
                result = (String) obj.get(CMcodedbKey.MEDIA.MEDIA_NAME);
            }
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
                String id = obj.getObjectId(CMcodedbKey.MEDIA.ID).toString();
                String mediaName = obj.getString(CMcodedbKey.MEDIA.MEDIA_NAME);
                result.put(id, new Media(id, null, mediaName, null, null));
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
                String id = obj.getObjectId(CMcodedbKey.MEDIA.ID).toString();
                String mediaName = obj.getString(CMcodedbKey.MEDIA.MEDIA_NAME);
                String aId = obj.getString(CMcodedbKey.MEDIA.AFFICIATE_ID);
                result.add(new Media(id, aId, mediaName, null, null));
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
                String id = obj.getObjectId(CMcodedbKey.MEDIA.ID).toString();
                String afficiateId = obj.getString(CMcodedbKey.MEDIA.AFFICIATE_ID);
                String mediaName = obj.getString(CMcodedbKey.MEDIA.MEDIA_NAME);
                String mediaUrl = obj.getString(CMcodedbKey.MEDIA.MEDIA_URL);
                Integer flag = obj.getInt(CMcodedbKey.AFFICIATE.FLAG);
                result.add(new Media(id, afficiateId, mediaName, mediaUrl, flag));
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
