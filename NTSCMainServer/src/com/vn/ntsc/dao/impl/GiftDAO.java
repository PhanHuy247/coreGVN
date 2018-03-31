/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.impl.Gift;

/**
 *
 * @author RuAc0n
 */
public class GiftDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getStampDB().getCollection(StampdbKey.GIFT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static List<Gift> getListGift(List<String> listId) throws EazyException {
        List<Gift> result = new ArrayList<>();
        List<ObjectId> list = new ArrayList<>();
        for (String listId1 : listId) {
            ObjectId id = new ObjectId(listId1);
            list.add(id);
        }
        try {
            DBObject query = QueryBuilder.start(StampdbKey.GIFT.ID).in(list).get();
            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Gift gift = new Gift();
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.GIFT.ID);
                gift.giftId = id.toString();
                String giftInfor = (String) dbObject.get(StampdbKey.GIFT.GIFT_INFOR);
                gift.giftInfor = giftInfor;
                Double price = (Double) dbObject.get(StampdbKey.GIFT.PRICE);
                gift.giftPrice = price;
                result.add(gift);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<Gift> getListGiftInfor(String catId, String language) throws EazyException {
        List<Gift> result = new ArrayList<Gift>();
        try {
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.GIFT.ORDER, 1);
            BasicDBObject searchObj = new BasicDBObject(StampdbKey.GIFT.CATEGORY_ID, catId);
            searchObj.append(StampdbKey.GIFT.FLAG, 1);
            DBCursor cursor = coll.find(searchObj).sort(sortObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Gift gift = new Gift();
                gift.categoryId = catId;
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.GIFT.ID);
                gift.giftId = id.toString();
                Double price = (Double) dbObject.get(StampdbKey.GIFT.PRICE);
                gift.giftPrice = price;
                String giftInfor = (String) dbObject.get(StampdbKey.GIFT.GIFT_INFOR);
                gift.giftInfor = giftInfor;
                String enGiftName = (String) dbObject.get(StampdbKey.GIFT.ENGLISH_NAME);
                String jpGiftName = (String) dbObject.get(StampdbKey.GIFT.JAPANESE_NAME);
                if (language == null) {
                    gift.enGiftName = enGiftName;
                    gift.jpGiftName = jpGiftName;
                } else {
                    if (language.equals(Constant.LANGUAGE.JAPANESE)) {
                        gift.giftName = jpGiftName;
                    } else {
                        gift.giftName = enGiftName;
                    }
                }
                result.add(gift);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<Gift> getAllGift(String language) throws EazyException {
        List<Gift> result = new ArrayList<Gift>();
        try {
            BasicDBObject sort = new BasicDBObject(StampdbKey.GIFT.CATEGORY_ID, 1);
            DBCursor cursor = coll.find(new BasicDBObject(StampdbKey.GIFT.FLAG, 1)).sort(sort);
            int size = cursor.size();
            String cId = null;
            List<Gift> list = new ArrayList<Gift>();
            int count = 0;
            while (cursor.hasNext()) {
                count ++;
                DBObject dbObject = cursor.next();
                Gift gift = new Gift();
                String catId = (String) dbObject.get(StampdbKey.GIFT.CATEGORY_ID);
                gift.categoryId = catId;
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.GIFT.ID);
                gift.giftId = id.toString();
                Double price = (Double) dbObject.get(StampdbKey.GIFT.PRICE);
                gift.giftPrice = price;
                String giftInfor = (String) dbObject.get(StampdbKey.GIFT.GIFT_INFOR);
                gift.giftInfor = giftInfor;
                String enGiftName = (String) dbObject.get(StampdbKey.GIFT.ENGLISH_NAME);
                String jpGiftName = (String) dbObject.get(StampdbKey.GIFT.JAPANESE_NAME);

                if (language == null) {
                    gift.enGiftName = enGiftName;
                    gift.jpGiftName = jpGiftName;
                } else {
                    if (language.equals(Constant.LANGUAGE.JAPANESE)) {
                        gift.giftName = jpGiftName;
                    } else {
                        gift.giftName = enGiftName;
                    }
                }
                Integer order = (Integer) dbObject.get(StampdbKey.GIFT.ORDER);
                gift.order = order;
                if(cId == null || cId.equals(catId)){
                    list.add(gift);
                }else{
                    Collections.sort(list);
                    result.addAll(list);
                    list = new ArrayList<Gift>();
                    list.add(gift);
                }
                cId = catId;
                if(count == size)
                    result.addAll(list);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
