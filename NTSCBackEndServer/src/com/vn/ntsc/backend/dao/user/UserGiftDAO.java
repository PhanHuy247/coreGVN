/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
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
public class UserGiftDAO {

    private static DBCollection coll;
    private static final String NUMBER_GIFT_FIELD = UserdbKey.USER_GIFT.GIFT_LIST + ".$." + UserdbKey.USER_GIFT.GIF_NUMBER;
    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.USER_GIFT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addGift(String userId, String giftId) throws EazyException {
        boolean result = false;
        try{
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_GIFT.ID, id);
            BasicDBObject checkerObj = new BasicDBObject(UserdbKey.USER_GIFT.GIFT_ID, giftId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", checkerObj);
            findObj.append(UserdbKey.USER_GIFT.GIFT_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBObject updateObj = new BasicDBObject(NUMBER_GIFT_FIELD, 1);
                
                BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
                coll.update(findObj, incObj);
            }else{
                BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER_GIFT.ID, id);
                BasicDBObject giftElement = new BasicDBObject(UserdbKey.USER_GIFT.GIFT_ID, giftId);
                giftElement.append(UserdbKey.USER_GIFT.GIF_NUMBER, 1);
                BasicDBObject giftObj = new BasicDBObject(UserdbKey.USER_GIFT.GIFT_LIST, giftElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", giftObj );
                coll.update(updateQuery, updateCommand, true, false);
            }
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean removeGift(String userId, String giftId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_GIFT.ID, id);
            BasicDBObject checkerObj = new BasicDBObject(UserdbKey.USER_GIFT.GIFT_ID, giftId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", checkerObj);
            findObj.append(UserdbKey.USER_GIFT.GIFT_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBObject updateObj = new BasicDBObject(NUMBER_GIFT_FIELD, -1);
                
                BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
                coll.update(findObj, incObj);
            }
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }


}
