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

/**
 *
 * @author RuAc0n
 */
public class GiftUserDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.GIFT_USER_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addGift(String userId, String sender, String giftId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.GIFT_USER.ID, id);
            BasicDBObject giftElement = new BasicDBObject(UserdbKey.GIFT_USER.SENDER_ID, sender);
            giftElement.append(UserdbKey.GIFT_USER.GIFT_ID, giftId);
            BasicDBObject gift = new BasicDBObject(UserdbKey.GIFT_USER.GIFT_LIST, giftElement);
            BasicDBObject updateCommand = new BasicDBObject("$push", gift);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkGiftExist(String userId, String giftId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.GIFT_USER.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if (obj != null) {
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.GIFT_USER.GIFT_LIST);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        BasicDBObject gift = (BasicDBObject) list.get((int) i);
                        String gId = gift.getString(UserdbKey.GIFT_USER.GIFT_ID);
                        if (giftId.equals(gId)) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeGift(String userId, String giftId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.GIFT_USER.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.GIFT_USER.GIFT_ID, giftId);
            BasicDBObject gift = new BasicDBObject(UserdbKey.GIFT_USER.GIFT_LIST, obj);
            BasicDBObject updateCommand = new BasicDBObject("$pull", gift);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getGiftList(String userId, long skip, long take) throws EazyException {
        List<String> result = new ArrayList<String>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.GIFT_USER.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList giftList = (BasicDBList) respondObj.get(UserdbKey.GIFT_USER.GIFT_LIST);
                if (!giftList.isEmpty()) {
                    if (skip > giftList.size()) {
                        return result;
                    }
                    long startIndex = giftList.size() - skip - 1;
                    long endIndex = startIndex - take;
                    if (endIndex < -1) {
                        endIndex = -1;
                    }
                    if (skip == 0 && take == 0) {
                        startIndex = giftList.size() - 1;
                        endIndex = -1;
                    }
                    for (long i = startIndex; i > endIndex; i--) {
                        BasicDBObject giftObj = (BasicDBObject) giftList.get((int) i);
                        //String sender = giftObj.getString(UserdbKey.GIFT.SENDER_ID);
                        String giftId = giftObj.getString(UserdbKey.GIFT_USER.GIFT_ID);
                        result.add(giftId);
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public class Checker {

        public String chk_id;
        public String chk_time;

        public Checker(String id, String time) {
            this.chk_id = id;
            this.chk_time = time;
        }
    }

}
