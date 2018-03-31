/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.stampdb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import test.db.DBTest;
import com.vn.ntsc.backend.dao.gift.GiftDAO;
import com.vn.ntsc.backend.entity.impl.gift.Gift;

/**
 *
 * @author duyetpt
 */
public class Giftdb {

    private static DB db;
    private static DBCollection coll;

    static {
        try {
            db = DBTest.mongo.getDB(StampdbKey.DB_NAME);
            coll = db.getCollection(StampdbKey.GIFT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static Gift getGift(String idg) {
        BasicDBObject findObj = new BasicDBObject("_id", new ObjectId(idg));
        DBObject dbObject = coll.findOne(findObj);
        com.vn.ntsc.backend.entity.impl.gift.Gift gift = new com.vn.ntsc.backend.entity.impl.gift.Gift();
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
        return gift;
    }

    public static void insertGift(String id) throws EazyException {
        GiftDAO.insertGift(id, "catId", 100, "gift_info", "name", "男性", 1);
    }
}
