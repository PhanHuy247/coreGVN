/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.thumbnail;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.backend.dao.DBManager;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;

/**
 *
 * @author Phan Huy
 */
public class ThumbNailDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getStaticFileDB().getCollection(StaticFiledbKey.THUMBNAIL_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static String getThumbnailUrl(String imageId) throws EazyException {
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
            Util.addDebugLog("id-------------------------------"+id);
            Util.addDebugLog("obj-------------------------------"+obj);
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
}
