/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.PointExchangedByChatData;

/**
 *
 * @author HuyDX
 */
public class PointExchangedByChatDAO {
    private static DBCollection coll;    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.POINT_EXCHANGED_BY_CHAT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static boolean addDocument(String messageId, String userId, int pointDiffer) throws EazyException{
        try{
            BasicDBObject doc= new BasicDBObject()
                    .append(UserdbKey.POINT_EXCHANGED_BY_CHAT.MESSAGE_ID, messageId)
                    .append(UserdbKey.POINT_EXCHANGED_BY_CHAT.USER_ID, userId)
                    .append(UserdbKey.POINT_EXCHANGED_BY_CHAT.POINT_DIFFER, pointDiffer);
            
            coll.insert(doc);
        }
        catch (Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }
    
    public static boolean removeDocumentByMessageId(String messageId){
        BasicDBObject doc = new BasicDBObject(UserdbKey.POINT_EXCHANGED_BY_CHAT.MESSAGE_ID, messageId);
        DBObject dbObj = coll.findOne(doc);
        if (dbObj!=null){
            coll.remove(dbObj);
        }
        return true;
    }
    
    public static PointExchangedByChatData getDocumentByMessageId(String messageId){
        PointExchangedByChatData data = null;
        BasicDBObject doc = new BasicDBObject(UserdbKey.POINT_EXCHANGED_BY_CHAT.MESSAGE_ID, messageId);
        DBObject dbObj = coll.findOne(doc);
        
        if (dbObj!=null){
            data = new PointExchangedByChatData();
            String userId = (String) dbObj.get(UserdbKey.POINT_EXCHANGED_BY_CHAT.USER_ID);
            int pointDiffer = (Integer) dbObj.get(UserdbKey.POINT_EXCHANGED_BY_CHAT.POINT_DIFFER);
            data.setUserId(userId);
            data.setPointDiffer(pointDiffer);
            data.setMessageId(messageId);
        }
        return data;
    }
    
}
