/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;


/**
 *
 * @author RuAc0n
 */
public class BuzzUserDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.USER_BUZZ_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean add(String buzzId, String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject insertObj = new BasicDBObject(UserdbKey.USER_BUZZ.ID, id);
            insertObj.append(UserdbKey.USER_BUZZ.USER_ID, userId);
            coll.save(insertObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    } 
    
    public static boolean remove(String buzzId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject removeObj = new BasicDBObject(UserdbKey.USER_BUZZ.ID, id);
            coll.remove(removeObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }     
    
}
