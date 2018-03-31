/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import eazycommon.constant.ErrorCode;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author RuAc0n
 */
public class InvitationCodeDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.INVITATION_CODE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static String addInvitationCode(String userId) throws EazyException {
        String result = null;
        try {
            BasicDBObject inserObj = new BasicDBObject(UserdbKey.INVITATION_CODE.USER_ID, userId);
            coll.insert(inserObj);
            result = ((ObjectId) inserObj.getObjectId(UserdbKey.INVITATION_CODE.ID)).toString();
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getInvattionCode(String userId) throws EazyException {
        String result = null;
        try {
            BasicDBObject findỌbject = new BasicDBObject(UserdbKey.INVITATION_CODE.USER_ID, userId);
            DBObject obj = coll.findOne(findỌbject);
            if (obj != null) {
                result = ((ObjectId) obj.get(UserdbKey.INVITATION_CODE.ID)).toString();
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getUserId(String invitationCode) throws EazyException {
        String result = null;
        try {
            ObjectId id = null;
            try {
                id = new ObjectId(invitationCode);
            } catch (Exception ex) {
                return result;
            }
            BasicDBObject findỌbject = new BasicDBObject(UserdbKey.INVITATION_CODE.ID, id);
            DBObject obj = coll.findOne(findỌbject);
            if (obj != null) {
                result = (String) obj.get(UserdbKey.INVITATION_CODE.USER_ID);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
