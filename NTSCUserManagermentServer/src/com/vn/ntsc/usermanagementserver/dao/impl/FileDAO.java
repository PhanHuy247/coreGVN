/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

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
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author RuAc0n
 */
public class FileDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getStaticDB().getCollection(UserdbKey.FILE_COLLECTION);
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


}
