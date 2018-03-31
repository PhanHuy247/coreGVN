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
import com.mongodb.QueryBuilder;
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
import com.vn.ntsc.backend.entity.impl.cmcode.CMCode;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;

/**
 *
 * @author RuAc0n
 */
public class CMCodeDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getCmCodeDB().getCollection(CMcodedbKey.CM_CODE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static String insert(String afficiateId, String mediaId, String code, Integer type, Double money, String regUrl, String saleUrl, String redirectUrl, String des, int flag) throws EazyException {
        String result = null;
        try {
            DBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE.CODE, code);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                throw new EazyException(ErrorCode.CM_CODE_EXISTS);
            }
            DBObject insertObj = new BasicDBObject(CMcodedbKey.CM_CODE.AFFICIATE_ID, afficiateId);
            insertObj.put(CMcodedbKey.CM_CODE.MEDIA_ID, mediaId);
            insertObj.put(CMcodedbKey.CM_CODE.CODE, code);
            insertObj.put(CMcodedbKey.CM_CODE.MONEY, money);
            insertObj.put(CMcodedbKey.CM_CODE.TYPE, type);
            insertObj.put(CMcodedbKey.CM_CODE.REDIRECT_URL, redirectUrl);
            if (regUrl != null) {
                insertObj.put(CMcodedbKey.CM_CODE.REDIRECT_URL, redirectUrl);
            }
            if (regUrl != null) {
                insertObj.put(CMcodedbKey.CM_CODE.REGISTER_URL, regUrl);
            }
            if (saleUrl != null) {
                insertObj.put(CMcodedbKey.CM_CODE.SALE_URL, saleUrl);
            }
            if (des != null) {
                insertObj.put(CMcodedbKey.CM_CODE.DESCRIPTION, des);
            }
            insertObj.put(CMcodedbKey.CM_CODE.FLAG, flag);
            coll.insert(insertObj);
            result = insertObj.get(CMcodedbKey.MEDIA.ID).toString();
        }catch (EazyException ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getMediaId(String code) throws EazyException {
        String result = null;
        try {
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE.CODE, code);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                result = (String) obj.get(CMcodedbKey.CM_CODE.MEDIA_ID);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getAfficiateId(String code) throws EazyException {
        String result = null;
        try {
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE.CODE, code);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                result = (String) obj.get(CMcodedbKey.CM_CODE.AFFICIATE_ID);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Double getMoney(String code) throws EazyException {
        Double result = null;
        try {
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE.CODE, code);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                result = (Double) obj.get(CMcodedbKey.CM_CODE.MONEY);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getId(String code) throws EazyException {
        String result = null;
        try {
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE.CODE, code);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                result = obj.get(CMcodedbKey.CM_CODE.ID).toString();
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    public static boolean update(String id, String affId, String mediaId, String cmCode, Integer type, Double money, String regUrl, String saleUrl, String redirectUrl, String des, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE.ID, oId);
            DBObject updateObject = new BasicDBObject(CMcodedbKey.CM_CODE.TYPE, type);
            updateObject.put(CMcodedbKey.CM_CODE.MONEY, money);
            if (regUrl != null) {
                updateObject.put(CMcodedbKey.CM_CODE.REDIRECT_URL, redirectUrl);
            }
            if (regUrl != null) {
                updateObject.put(CMcodedbKey.CM_CODE.REGISTER_URL, regUrl);
            }
            if (saleUrl != null) {
                updateObject.put(CMcodedbKey.CM_CODE.SALE_URL, saleUrl);
            }
            if (des != null) {
                updateObject.put(CMcodedbKey.CM_CODE.DESCRIPTION, des);
            }
            updateObject.put(CMcodedbKey.CM_CODE.FLAG, flag);
            updateObject.put(CMcodedbKey.CM_CODE.CODE, cmCode);
            updateObject.put(CMcodedbKey.CM_CODE.MEDIA_ID, mediaId);
            updateObject.put(CMcodedbKey.CM_CODE.AFFICIATE_ID, affId);
            BasicDBObject setObj = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SizedListData search(String afficiateId, String mediaId, String code, Long flag, Long type, int skip, int take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBCursor cursor = null;
            if (afficiateId == null && mediaId == null && code == null && flag == null && type == null) {
                cursor = coll.find();
            } else {
                DBObject findObj = new BasicDBObject();
                if (code != null) {
                    findObj.put(CMcodedbKey.CM_CODE.CODE, code);
                } else {
                    if (afficiateId != null) {
                        findObj.put(CMcodedbKey.CM_CODE.AFFICIATE_ID, afficiateId);
                    }
                    if (mediaId != null) {
                        findObj.put(CMcodedbKey.CM_CODE.MEDIA_ID, mediaId);
                    }
                    if (flag != null) {
                        findObj.put(CMcodedbKey.CM_CODE.FLAG, flag);
                    }
                    if (type != null) {
                        findObj.put(CMcodedbKey.CM_CODE.TYPE, type);
                    }
                }
                cursor = coll.find(findObj);
            }
            Integer size = cursor.size();
            List<IEntity> list = new ArrayList<IEntity>();
            cursor = cursor.skip(skip).limit(take);
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(CMcodedbKey.CM_CODE.ID).toString();
                String aId = obj.getString(CMcodedbKey.CM_CODE.AFFICIATE_ID);
                String mId = obj.getString(CMcodedbKey.CM_CODE.MEDIA_ID);
                String cd = obj.getString(CMcodedbKey.CM_CODE.CODE);
                Integer tp = obj.getInt(CMcodedbKey.CM_CODE.TYPE);
                Double money = obj.getDouble(CMcodedbKey.CM_CODE.MONEY);
                Integer fl = obj.getInt(CMcodedbKey.CM_CODE.FLAG);
                list.add(new CMCode(id, aId, mId, cd, money, tp, fl));
            }
            result = new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static CMCode getDetail(String id) throws EazyException {
        CMCode result = new CMCode();
        try {
            ObjectId oId = new ObjectId(id);
            BasicDBObject obj = (BasicDBObject) coll.findOne(new BasicDBObject(CMcodedbKey.CM_CODE.ID, oId));
            if (obj != null) {
                String afficiateId = obj.getString(CMcodedbKey.CM_CODE.AFFICIATE_ID);
                String mediaId = obj.getString(CMcodedbKey.CM_CODE.MEDIA_ID);
                String code = obj.getString(CMcodedbKey.CM_CODE.CODE);
                Integer type = obj.getInt(CMcodedbKey.CM_CODE.TYPE);
                Double money = obj.getDouble(CMcodedbKey.CM_CODE.MONEY);
                Integer flag = obj.getInt(CMcodedbKey.CM_CODE.FLAG);
                String rediectUrl = obj.getString(CMcodedbKey.CM_CODE.REDIRECT_URL);
                String regUrl = obj.getString(CMcodedbKey.CM_CODE.REGISTER_URL);
                String saleUrl = obj.getString(CMcodedbKey.CM_CODE.SALE_URL);
                String des = obj.getString(CMcodedbKey.CM_CODE.DESCRIPTION);
                result = new CMCode(id, afficiateId, mediaId, code, money, type, regUrl, saleUrl, rediectUrl, des, flag);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getAll() throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String cmCode = (String) obj.get(CMcodedbKey.CM_CODE.CODE);
                result.add(cmCode);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static Map<String, IEntity> getListName(Map<String, IEntity> map) throws EazyException {
        Map<String, IEntity> result = new TreeMap<>();
        try {
            List<String> listMedia = new ArrayList<>();
            for (Map.Entry<String, IEntity> pair : map.entrySet()) {
                String mediaId = pair.getKey();
                listMedia.add(mediaId);
            }
            DBObject findObj = QueryBuilder.start(CMcodedbKey.CM_CODE.MEDIA_ID).in(listMedia).get();
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(CMcodedbKey.CM_CODE.ID).toString();
                String mediaId = obj.getString(CMcodedbKey.CM_CODE.MEDIA_ID);
                String cmCode = obj.getString(CMcodedbKey.CM_CODE.CODE);
                result.put(mediaId, new CMCode(id, null, null, cmCode, null, null, null));
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
