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
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;

/**
 *
 * @author Phan Huy
 */
public class ThumbnailDAO {
    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = CommonDAO.mongo.getDB(StaticFiledbKey.DB_NAME);
            coll = db.getCollection(StaticFiledbKey.THUMBNAIL_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
     public static String getImageUrl(String buzzId) throws EazyException {
        String result = null;
        try {
            Util.addDebugLog("buzzId===================================="+buzzId);
            if(!ObjectId.isValid(buzzId)){
                return result;
            }
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            String url = (String) obj.get("url");
            result = url;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
