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
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GiftData;

/**
 *
 * @author RuAc0n
 */
public class UserGiftDAO {

    private static DBCollection coll;
    private static final String NUMBER_GIFT_FIELD = UserdbKey.USER_GIFT.GIFT_LIST + ".$." + UserdbKey.USER_GIFT.GIF_NUMBER;
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.USER_GIFT_COLLECTION);
        } catch (Exception ex) {
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

    public static List<GiftData> getGiftList(String userId, List<String> listGift) throws EazyException {
        List<GiftData> result = new ArrayList<GiftData>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER_GIFT.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList giftList = (BasicDBList) respondObj.get(UserdbKey.USER_GIFT.GIFT_LIST);
                if(giftList != null){
                    for(String giftId: listGift){
                        int number = 0;
                        for (Object gift : giftList) {
                            BasicDBObject giftObj = (BasicDBObject) gift;
                            String gId = giftObj.getString(UserdbKey.USER_GIFT.GIFT_ID);
                            if(giftId.equals(gId)){
                                number = giftObj.getInt(UserdbKey.USER_GIFT.GIF_NUMBER);
                                break;
                            }
                        }
                        result.add(new GiftData(giftId, number));
                    }
                }else{
                    for(String gift: listGift){
                        result.add(new GiftData(gift,0));
                    }                
                }
            }else{
                for(String gift: listGift){
                    result.add(new GiftData(gift,0));
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
