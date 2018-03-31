/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.dao.impl;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteConcern;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.JPNSdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.jpns.dao.UserDB;

/**
 *
 * @author DuongLTD
 */
public class UserDAO {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            coll = db.getCollection(UserdbKey.USERS_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static User getUserInfor(String userId) throws EazyException {
        User result = new User();
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            //search command
            DBObject dbOject = coll.findOne(obj);
            UserDB user = UserDB.fromDBObject(dbOject);
            result = user.getUserInfor();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getDeactivate() throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(UserdbKey.USER.FLAG);
                if (flag != null && (flag == Constant.USER_STATUS_FLAG.DEACITIVE || flag == Constant.USER_STATUS_FLAG.DISABLE)) {
                    String userId = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
                    result.add(userId);
//                    continue;
                }
//                Integer gender = (Integer) dbO.get(UserdbKey.USER.GENDER);
//                Integer verifyFlag = (Integer) dbO.get(UserdbKey.USER.VERIFICATION_FLAG);
//                if(gender == Constant.FEMALE && verifyFlag != null && verifyFlag != Constant.APPROVED){
//                    String userId = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
//                    result.add(userId);
//                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Long getNotificationReadTime(String userId) throws EazyException {
        Long result = null;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject doc = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(doc);

            if (obj != null) { // found email
                Long time = (Long) obj.get(UserdbKey.USER.NOTIFICATION_READ_TIME);
                result = time;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getAvatarId(String userId) throws EazyException {
        String result = null;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);
            //search command
            DBObject dbOject = coll.findOne(obj);
            result = (String) dbOject.get("ava_id");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String getUserName(String userId) throws EazyException {
        String result = null;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);
            //search command
            DBObject dbOject = coll.findOne(obj);
            result = (String) dbOject.get("user_name");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Integer getGender(String userId) throws EazyException {
        Integer result = 0;
        try{
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER.ID, id);
            
            DBObject dbOject = coll.findOne(findObj);
            result = (Integer) dbOject.get(UserdbKey.USER.GENDER);
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
