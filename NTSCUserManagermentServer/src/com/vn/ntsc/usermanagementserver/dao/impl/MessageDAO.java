/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.SizedListData;

/**
 *
 * @author RuAc0n
 */
public class MessageDAO {

    private static DBCollection coll;
    private static DB dbuser;
    
    static {
        try {
//            coll = DatabaseLoader.getChatLogExtensionDB().getCollection(UserdbKey.CHAT_LOG_EXTENSION);
            dbuser = DatabaseLoader.getChatLogExtensionDB();
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addChatImage(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.CHAT_IMAGE.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.CHAT_IMAGE.CHAT_IMAGE_LIST, imageId);
            BasicDBObject updateCommand = new BasicDBObject("$push", obj);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateReadMessage(String msgId,String userID){
        boolean result =true;
        DBCollection coll;
        try{
           coll= dbuser.getCollection(userID);
           if (msgId!=null){
               DBObject query = new BasicDBObject(UserdbKey.MESSAGE.Field_MsgID, msgId);
               BasicDBObject updateCommand = new BasicDBObject("$set", new BasicDBObject(UserdbKey.MESSAGE.Field_IsLock, 1));
               //BasicDBObject updateCommand = new BasicDBObject("$set", setObj);
               
               coll.update(query, updateCommand, false, true);
           } 
           return  result;
        } catch(Exception ex){
            Util.addErrorLog(ex);
            return false;
        }
    }
}
