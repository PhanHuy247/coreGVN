/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;
import java.math.BigInteger;

/**
 *
 * @author RuAc0n
 */
public class FileDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getStaticFileDB().getCollection(StaticFiledbKey.FILE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public static List<String> getAvailableFileIds(Collection<String> ids) {
        List<String> result = new ArrayList<>();
        try {
            List<ObjectId> oIds = new ArrayList<>();
            for (String id : ids) {
                if (ObjectId.isValid(id)) {

                    oIds.add(new ObjectId(id));
                }
            }

            DBObject findObj = QueryBuilder.start(StaticFiledbKey.FILE.ID).in(oIds).get();
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                DBObject obj = cur.next();
                result.add(obj.get(StaticFiledbKey.FILE.ID).toString());
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
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
    
    public static Long getFileStatus(String fileId){
        ObjectId id = new ObjectId(fileId);
        BasicDBObject findObject = new BasicDBObject(UserdbKey.FILE.ID, id);
        DBObject respondObj = coll.findOne(findObject);
        Long result = 1L;
        if (respondObj != null) {
            Long fileStatus = (Long) respondObj.get(UserdbKey.FILE.VIDEO_STATUS);
            if(fileStatus != null){
                return fileStatus;
            }
        }
        return result;
    }
}
