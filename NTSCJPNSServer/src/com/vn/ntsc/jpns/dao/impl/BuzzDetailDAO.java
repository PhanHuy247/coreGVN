/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;

/**
 *
 * @author Phan Huy
 */
public class BuzzDetailDAO {
    private static DBCollection coll;
    private static DB db;
     static {
        try {
            db = CommonDAO.mongo.getDB(BuzzdbKey.DB_NAME);
            coll = db.getCollection(BuzzdbKey.BUZZ_DETAIL_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
     
     public static String getImageId(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            String imageId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
            result = imageId;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
   public static Integer getType(String buzzId) throws EazyException {
        Integer result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            Integer type = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
            result = type;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
    public static String getText(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            String type = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
            result = type;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
}
