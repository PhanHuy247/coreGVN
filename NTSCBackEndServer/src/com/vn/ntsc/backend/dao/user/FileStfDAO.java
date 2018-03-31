/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;


/**
 *
 * @author RuAc0n
 */
public class FileStfDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getStaticFileDB().getCollection(UserdbKey.FILE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    //HUNGDT add
    public static int getIsFree(String fileId) {
        int result = 1;
        ObjectId id = new ObjectId(fileId);
        BasicDBObject findObject = new BasicDBObject(UserdbKey.FILE.ID, id);
        DBObject respondObj = coll.findOne(findObject);
        if (respondObj != null) {
            Integer is_free = (Integer) respondObj.get(UserdbKey.FILE.IS_FREE);
            if (is_free != null) {
                return is_free;
            }
        }
        return result;
    }

    public static String getFileUrl(String fileId) throws EazyException {
        String url = null;
        try {
            //search by id
            ObjectId id = new ObjectId(fileId);
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.THUMBNAIL.ID, id);

            // command search
            DBObject dboject = coll.findOne(obj);
            if (dboject != null) {
                url = (String) dboject.get(StaticFiledbKey.FILE.URL);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return url;
    }

}
