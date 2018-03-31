/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CMcodedbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author RuAc0n
 */
public class CMCodeDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getCmCodeDB().getCollection(CMcodedbKey.CM_CODE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            //Future logging here
           
        }
    }

    public static String insert(String afficiateId, String mediaId, String code, Integer type, Double money, String regUrl, String saleUrl, String des, int flag) throws EazyException {
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
            result = insertObj.get(CMcodedbKey.CM_CODE.ID).toString();
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

    public static String getRedirectUrl(String code) throws EazyException {
        String result = null;
        try {
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE.CODE, code);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                result = (String) obj.get(CMcodedbKey.CM_CODE.REDIRECT_URL);
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

    public static boolean update(String id, Integer type, Double money, String regUrl, String saleUrl, String des, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE.ID, oId);
            DBObject updateObject = new BasicDBObject(CMcodedbKey.CM_CODE.TYPE, type);
            updateObject.put(CMcodedbKey.CM_CODE.MONEY, money);
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
            BasicDBObject setObj = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
