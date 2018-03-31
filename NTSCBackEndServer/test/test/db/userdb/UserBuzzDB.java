/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.userdb;

import com.mongodb.*;
import com.mongodb.DBCollection;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import test.db.DBTest;

/**
 *
 * @author DuongLTD
 */
public class UserBuzzDB {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = DBTest.mongo.getDB(BuzzdbKey.DB_NAME);
            coll = db.getCollection(BuzzdbKey.USER_BUZZ_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean delBuzz(String userId, String buzzId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            BasicDBObject buzzObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzObj);
            findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.USER_BUZZ.BUZZ_LIST + ".$." + BuzzdbKey.USER_BUZZ.FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateApproveFlag(String userId, String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            BasicDBObject buzzObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzObj);
            findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.USER_BUZZ.BUZZ_LIST + ".$." + BuzzdbKey.USER_BUZZ.APPROVED_FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, flag);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean addUserBuzz(String userId, String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            BasicDBObject imageElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            imageElement.append(BuzzdbKey.USER_BUZZ.APPROVED_FLAG, 1);
            imageElement.append(BuzzdbKey.USER_BUZZ.FLAG, flag);
            BasicDBObject pbImage = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, imageElement);
            BasicDBObject updateCommand = new BasicDBObject("$push", pbImage);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Integer getFag(String userId, String BuzzId) throws EazyException {
        Integer result = null;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList imageList = (BasicDBList) respondObj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                if (!imageList.isEmpty()) {
                    for (int i = 0; i < imageList.size(); i++) {
                        BasicDBObject image = (BasicDBObject) imageList.get(i);
                        String iId = image.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                        Integer buId = (Integer) image.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                        if (iId.equals(BuzzId)) {
                            result = buId;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void remove(String userId) {
        BasicDBObject find = new BasicDBObject("_id", new ObjectId(userId));
        coll.remove(find);
    }
}
