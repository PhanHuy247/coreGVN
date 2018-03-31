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
import com.vn.ntsc.backend.dao.user.UserDAO;

/**
 *
 * @author DuongLTD
 */
public class BuzzDetailDB {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = DBTest.mongo.getDB("buzzdb");
            coll = db.getCollection(BuzzdbKey.BUZZ_DETAIL_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static String addBuzz(String userId, String buzzVal, int buzzType, long time, int isApp, String ip) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.USER_ID, userId);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE, buzzVal);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE, buzzType);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME, time);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG, isApp);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.IP, ip);
            coll.insert(insertObj);
            ObjectId id = (ObjectId) insertObj.get(BuzzdbKey.BUZZ_DETAIL.ID);
            result = id.toString();
            if (buzzType == Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ) {
                UserBuzzDB.addUserBuzz(userId, result, isApp);
            }
            if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ) {
                UserDAO.addPbImage(userId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getImageId(String buzzId) throws EazyException {
        String result = null;
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, buzzId);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            Integer type = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
            if (type == Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ) {
                String imageId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                result = imageId;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getUserId(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
            String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
            result = userId;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkBuzzExist(String buzzId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                Integer buzzFlag = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.FLAG);
                if (buzzFlag == Constant.FLAG.ON) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getBuzzType(String buzzId) throws EazyException {
        int result = 0;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
            Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
            result = buzzType.intValue();
        } catch (EazyException ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Integer getFlag(String buzzId) throws EazyException {
        Integer result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
            Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
            result = buzzType;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateApprovedFlag(String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG, flag);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateFlag(String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.FLAG, flag);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isExists(String buzzId) throws EazyException {
        boolean result = false;
        try {
            //
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(query);

            if (obj != null) { // found email
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void remove(String buzzId) {
        BasicDBObject find = new BasicDBObject("_id", new ObjectId(buzzId));
        coll.remove(find);
    }
}
