/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.gift;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.gift.Gift;

/**
 *
 * @author RuAc0n
 */
public class GiftDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getStampDB().getCollection(StampdbKey.GIFT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static boolean insertGift(String id, String catId, double price, String giftInfor, String enGiftName, String jpGiftName, int order) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT.ID, oId);
            obj.append(StampdbKey.GIFT.PRICE, price);
            obj.append(StampdbKey.GIFT.CATEGORY_ID, catId);
            obj.append(StampdbKey.GIFT.ENGLISH_NAME, enGiftName);
            obj.append(StampdbKey.GIFT.JAPANESE_NAME, jpGiftName);
            obj.append(StampdbKey.GIFT.ORDER, order);
            obj.append(StampdbKey.GIFT.FLAG, Constant.FLAG.ON );
            if (giftInfor != null) {
                obj.append(StampdbKey.GIFT.GIFT_INFOR, giftInfor);
            }
            coll.insert(obj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getMaxOrder(String catId) throws EazyException {
        Integer result = null;
        try {
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.GIFT.ORDER, -1);
            DBCursor cursor = coll.find(new BasicDBObject(StampdbKey.GIFT.CATEGORY_ID, catId)).sort(sortObj);
            if(cursor.size() == 0)
                return 0;
            else{
                DBObject obj = cursor.next();
                result = (Integer) obj.get(StampdbKey.GIFT.ORDER);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }     
    
    public static List<IEntity> getListGift(String catId) throws EazyException {
        List<IEntity> result = new ArrayList<>();
        try {
            BasicDBObject searchObj = new BasicDBObject(StampdbKey.GIFT.CATEGORY_ID, catId);
            searchObj.append(StampdbKey.GIFT.FLAG, 1);
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.GIFT.ORDER, 1);
            DBCursor cursor = coll.find(searchObj).sort(sortObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Gift gift = new Gift();
                gift.categoryId = catId;
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.GIFT.ID);
                gift.giftId = id.toString();
                Double price = (Double) dbObject.get(StampdbKey.GIFT.PRICE);
                gift.giftPrice = price.intValue();
                String giftInfor = (String) dbObject.get(StampdbKey.GIFT.GIFT_INFOR);
                gift.giftInfor = giftInfor;
                String enGiftName = (String) dbObject.get(StampdbKey.GIFT.ENGLISH_NAME);
                gift.enGiftName = enGiftName;
                String jpGiftName = (String) dbObject.get(StampdbKey.GIFT.JAPANESE_NAME);
                gift.jpGiftName = jpGiftName;
                result.add(gift);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean deleteGift(String giftId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(giftId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT.ID, id);
            BasicDBObject updateObj = new BasicDBObject("$set", new BasicDBObject(StampdbKey.GIFT.FLAG, Constant.FLAG.OFF));
            coll.update(obj, updateObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getCategoryId(String giftId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(giftId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT.ID, id);
            DBObject dbObj = coll.findOne(obj);
            if (dbObj != null) {
                result = (String) dbObj.get(StampdbKey.GIFT.CATEGORY_ID);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateOrder(String giftId, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(giftId);
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.GIFT.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT.ORDER, num);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }      
        
    
    public static boolean updateGift(String giftId,double price, String giftInfor, String enGiftName, String jpGiftName) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(giftId);
            BasicDBObject searchObj = new BasicDBObject(StampdbKey.GIFT.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT.PRICE, price);
            obj.append(StampdbKey.GIFT.GIFT_INFOR, giftInfor);
            obj.append(StampdbKey.GIFT.ENGLISH_NAME, enGiftName);
            obj.append(StampdbKey.GIFT.JAPANESE_NAME, jpGiftName);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(searchObj, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
  
    
}
