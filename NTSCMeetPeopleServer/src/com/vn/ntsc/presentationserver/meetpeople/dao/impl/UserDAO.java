/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.security.MessageDigest;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;

/**
 *
 * @author DuongLTD
 */
public class UserDAO {

    private static MessageDigest md;
    private static DBCollection coll;

    public static DB db;

    static {
        db = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
        coll = db.getCollection(UserdbKey.USERS_COLLECTION);
    }

    public static int getUserLocation(String userId) throws EazyException {
        int result = 0;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            //search command
            DBObject dbObject = coll.findOne(obj);            
            result = dbObject.get(UserdbKey.USER.REGION)!=null?
                    new Integer(dbObject.get(UserdbKey.USER.REGION).toString()) : 0;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Object getUserInfor(String userId, String param){
        Object result = new Object();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            DBObject dbOject = coll.findOne(obj);
            result = dbOject.get(param);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }


}
