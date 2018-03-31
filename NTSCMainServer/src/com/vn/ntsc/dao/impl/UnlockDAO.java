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
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author LongLT
 */
public class UnlockDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getUserDB().getCollection(UserdbKey.UNLOCK_COLLECTION);
            DBObject index1 = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, 1).append(UserdbKey.UNLOCK.UNLOCK_ID, 1);
            coll.createIndex(index1);
            DBObject index2 = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, 1).append(UserdbKey.UNLOCK.IMAGE_ID, 1);
            coll.createIndex(index2);
            DBObject index3 = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, 1).append(UserdbKey.UNLOCK.VIDEO_ID, 1);
            coll.createIndex(index3);
            DBObject index4 = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, 1).append(UserdbKey.UNLOCK.AUDIO_ID, 1);
            coll.createIndex(index4);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static List<String> getImageIdsUnlocked(String userId, Collection<String> imageIds) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            List<String> oIds = new ArrayList<>();
            for (String id : imageIds) {
                oIds.add(id);
            }
            BasicDBObject findObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
            BasicDBObject inObj = new BasicDBObject("$in", oIds);
            findObj.append(UserdbKey.UNLOCK.IMAGE_ID, inObj);
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String imageId = (String) obj.get(UserdbKey.UNLOCK.IMAGE_ID);
                result.add(imageId);
            }
//            DBObject findObj = QueryBuilder
//                    .start().
//                    and(QueryBuilder.start().put(UserdbKey.UNLOCK.IMAGE_ID).in(oIds).get(),
//                    QueryBuilder.start().put(UserdbKey.UNLOCK.USER_ID).is(userId).get()).get();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isImageUnlock(String userId, String imageId) throws EazyException {
        boolean result = false;
        if (imageId != null && !imageId.isEmpty()) {
            try {
                BasicDBObject findObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
                findObj.append(UserdbKey.UNLOCK.IMAGE_ID, imageId);
                DBCursor cur = coll.find(findObj);
                result = cur.hasNext();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
        }
        return result;
    }

    public static List<String> getFileIdsUnlocked(String userId, Collection<String> fileIds) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            List<String> oIds = new ArrayList<>();
            for (String id : fileIds) {
                oIds.add(id);
            }
            BasicDBObject findObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
            BasicDBObject inObj = new BasicDBObject("$in", oIds);
            findObj.append(UserdbKey.UNLOCK.VIDEO_ID, inObj);
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String fileId = (String) obj.get(UserdbKey.UNLOCK.VIDEO_ID);
                result.add(fileId);
            }
//            DBObject findObj = QueryBuilder
//                    .start().
//                    and(QueryBuilder.start().put(UserdbKey.UNLOCK.IMAGE_ID).in(oIds).get(),
//                    QueryBuilder.start().put(UserdbKey.UNLOCK.USER_ID).is(userId).get()).get();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
