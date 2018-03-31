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
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author RuAc0n
 */
public class ImageStfDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getStaticFileDB().getCollection(UserdbKey.IMAGE_STF_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static String insertImage(String url) throws EazyException {
        String result = "";
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.URL, url);
            coll.insert(obj);
            ObjectId id = (ObjectId) obj.get(StaticFiledbKey.IMAGE.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean insertImage(String url, String fileId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(fileId);
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.URL, url);
            obj.append(StaticFiledbKey.IMAGE.ID, id);
            coll.insert(obj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean insertImage2(String url, String fileId, int is_free) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(fileId);
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.URL, url);
            obj.append(StaticFiledbKey.IMAGE.ID, id);
            obj.append(StaticFiledbKey.IMAGE.IS_FREE, is_free);
            coll.insert(obj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getImageUrl(String imageId) throws EazyException {
        String url = null;
        try {

            //search by id
            ObjectId id = null;
            try {
                id = new ObjectId(imageId);
            } catch (Exception ex) {
                id = null;
            }
            if (id == null) {
                return null;
            }
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.ID, id);

            // command search
            DBObject dboject = coll.findOne(obj);
            if (dboject == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            url = (String) dboject.get(StaticFiledbKey.IMAGE.URL);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return url;
    }

    //HUNGDT add
    public static int getIsFree(String imageId) {
        int result = 0;
        ObjectId id = new ObjectId(imageId);
        BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.ID, id);
        DBObject respondObj = coll.findOne(findObject);
        if (respondObj != null) {
            Integer is_free = (Integer) respondObj.get(UserdbKey.IMAGE.IS_FREE);
            if (is_free != null && is_free == 1) {
                Util.addDebugLog("getIsFree " + is_free);
                result = 1;
                return result;
            } else {
                Util.addDebugLog("getIsFree1 " + is_free);
            }
            if (is_free == null) {
                result = -1;
                return result;
            }
        }
        return result;
    }
}
