/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.server.unlockpool.UnlockInfor;

/**
 *
 * @author RuAc0n
 */
public class UnlockDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.UNLOCK_COLLECTION);
            DBObject index1 = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, 1).append(UserdbKey.UNLOCK.UNLOCK_ID, 1);
            coll.createIndex(index1);
            DBObject index2 = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, 1).append(UserdbKey.UNLOCK.IMAGE_ID, 1);
            coll.createIndex(index2);
            DBObject index3 = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, 1).append(UserdbKey.UNLOCK.VIDEO_ID, 1);
            coll.createIndex(index3);
            DBObject index4 = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, 1).append(UserdbKey.UNLOCK.AUDIO_ID, 1);
            coll.createIndex(index4);
        } catch (Exception ex) {
            //Future logging here

        }
    }

    public static String addUnlockBackstage(String userId, long endTime, String unlockId) throws EazyException {
        String result = null;
        try {
            BasicDBObject addObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
            //addObj.append(UserdbKey.UNLOCK.UNLOCK_TYPE, unlockType);
            addObj.append(UserdbKey.UNLOCK.END_TIME, endTime);
            if (unlockId != null) {
                addObj.append(UserdbKey.UNLOCK.UNLOCK_ID, unlockId);
            }
            coll.insert(addObj);
            result = addObj.getObjectId(UserdbKey.UNLOCK.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String addUnlockAudio(String userId, long endTime, String unlockId, String fileId) throws EazyException {
        String result = null;
        try {
            BasicDBObject addObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
            //addObj.append(UserdbKey.UNLOCK.UNLOCK_TYPE, unlockType);
            addObj.append(UserdbKey.UNLOCK.END_TIME, endTime);
            addObj.append(UserdbKey.UNLOCK.AUDIO_ID, fileId);
            if (unlockId != null) {
                addObj.append(UserdbKey.UNLOCK.UNLOCK_ID, unlockId);
            }
            coll.insert(addObj);
            result = addObj.getObjectId(UserdbKey.UNLOCK.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String addUnlockVideo(String userId, long endTime, String unlockId, String fileId) throws EazyException {
        String result = null;
        try {
            BasicDBObject addObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
            //addObj.append(UserdbKey.UNLOCK.UNLOCK_TYPE, unlockType);
            addObj.append(UserdbKey.UNLOCK.END_TIME, endTime);
            addObj.append(UserdbKey.UNLOCK.VIDEO_ID, fileId);
            if (unlockId != null) {
                addObj.append(UserdbKey.UNLOCK.UNLOCK_ID, unlockId);
            }
            coll.insert(addObj);
            result = addObj.getObjectId(UserdbKey.UNLOCK.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String addUnlockImage(String userId, long endTime, String unlockId, String imageId) throws EazyException {
        String result = null;
        try {
            BasicDBObject addObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
            //addObj.append(UserdbKey.UNLOCK.UNLOCK_TYPE, unlockType);
            addObj.append(UserdbKey.UNLOCK.END_TIME, endTime);
            addObj.append(UserdbKey.UNLOCK.IMAGE_ID, imageId);
            if (unlockId != null) {
                addObj.append(UserdbKey.UNLOCK.UNLOCK_ID, unlockId);
            }
            coll.insert(addObj);
            result = addObj.getObjectId(UserdbKey.UNLOCK.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeUnlock(String id) {
        boolean result = false;
        try {
            ObjectId objId = new ObjectId(id);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.UNLOCK.ID, objId);
            coll.remove(findObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static boolean removeByIds(List<String> ids) {
        boolean result = false;
        try {
            List<ObjectId> obIds = new ArrayList<>();
            for (String id : ids) {
                if (ObjectId.isValid(id)) {
                    obIds.add(new ObjectId(id));
                }
            }
            DBObject query = QueryBuilder.start(UserdbKey.UNLOCK.ID).in(obIds).get();
            coll.remove(query);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static boolean isBackStageUnlock(String userId, String unlockId) throws EazyException {
        boolean result = false;
        if (userId == null || userId.isEmpty()){
            return result;
        }
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
            findObj.append(UserdbKey.UNLOCK.UNLOCK_ID, unlockId);
            findObj.append(UserdbKey.UNLOCK.AUDIO_ID, new BasicDBObject("$exists", false));
            findObj.append(UserdbKey.UNLOCK.IMAGE_ID, new BasicDBObject("$exists", false));
            findObj.append(UserdbKey.UNLOCK.VIDEO_ID, new BasicDBObject("$exists", false));
            DBCursor cur = coll.find(findObj);
            result = cur.hasNext();
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

    public static boolean isVideoUnlock(String userId, String fileId) throws EazyException {
        boolean result = false;
        if (fileId != null && !fileId.isEmpty()) {
            try {
                BasicDBObject findObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
                findObj.append(UserdbKey.UNLOCK.VIDEO_ID, fileId);
                DBCursor cur = coll.find(findObj);
                result = cur.hasNext();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
        }
        return result;
    }

    public static boolean isAudioUnlock(String userId, String fileId) throws EazyException {
        boolean result = false;
        if (fileId != null && !fileId.isEmpty()) {
            try {
                BasicDBObject findObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
                findObj.append(UserdbKey.UNLOCK.AUDIO_ID, fileId);
                DBCursor cur = coll.find(findObj);
                result = cur.hasNext();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
        }
        return result;
    }

    public static Map<String, UnlockInfor> initUnlockPool() {
        Map<String, UnlockInfor> result = new ConcurrentHashMap<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                ObjectId id = (ObjectId) dbO.get(UserdbKey.UNLOCK.ID);
                String userId = (String) dbO.get(UserdbKey.UNLOCK.USER_ID);
                Long endTime = (Long) dbO.get(UserdbKey.UNLOCK.END_TIME);
                UnlockInfor infor = new UnlockInfor(id.toString(), userId, endTime);
                result.put(id.toString(), infor);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static List<String> getImageIdsUnlocked(String userId, List<String> imageIds) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.UNLOCK.USER_ID, userId);
            BasicDBObject inObj = new BasicDBObject("$in", imageIds);
            findObj.append(UserdbKey.UNLOCK.IMAGE_ID, inObj);
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String imageId = (String) obj.get(UserdbKey.UNLOCK.IMAGE_ID);
                result.add(imageId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
