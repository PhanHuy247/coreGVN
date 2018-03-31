/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
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
public class ChatImageDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.CHAT_IMAGE_COLLECTION);
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

    public static boolean removeChatImage(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.CHAT_IMAGE.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.CHAT_IMAGE.CHAT_IMAGE_LIST, imageId);
            BasicDBObject updateCommand = new BasicDBObject("$pull", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SizedListData getChatImage(String userId, long skip, long take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.CHAT_IMAGE.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList imageList = (BasicDBList) respondObj.get(UserdbKey.CHAT_IMAGE.CHAT_IMAGE_LIST);
                if (!imageList.isEmpty()) {
                    if (skip > imageList.size()) {
                        return new SizedListData(imageList.size(), null);
                    }
                    long startIndex = imageList.size() - skip - 1;
                    long endIndex = startIndex - take;
                    if (endIndex < -1) {
                        endIndex = -1;
                    }
                    if (skip == 0 && take == 0) {
                        startIndex = imageList.size() - 1;
                        endIndex = -1;
                    }
                    List<String> res = new ArrayList<String>();
                    for (long i = startIndex; i > endIndex; i--) {
                        res.add(imageList.get((int) i).toString());
                    }
                    return new SizedListData(imageList.size(), res);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
