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
import java.util.logging.Level;
import java.util.logging.Logger;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.gift.GiftCategory;

/**
 *
 * @author RuAc0n
 */
public class GiftCategoryDAO {

    private static DBCollection coll;
    public static String ID;

    static {
        try {
            coll = DBManager.getStampDB().getCollection(StampdbKey.GIFT_CATEGORY_COLLECTION);
            try {
                String id = getCategoryID();
                if (id != null) {
                    ID = id;
                } else {
                    ID = insertCategory("default", "既定", 0);
                }
            } catch (EazyException ex) {
                Logger.getLogger(GiftCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void init() {

    }

    public static String insertCategory(String enCatName, String jpCatName, int order) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT_CATEGORY.JAPANESE_NAME, jpCatName);
            obj.append(StampdbKey.GIFT_CATEGORY.ENGLISH_NAME, enCatName);
            obj.append(StampdbKey.GIFT_CATEGORY.FLAG, Constant.FLAG.ON);
            obj.append(StampdbKey.GIFT_CATEGORY.GIFT_NUMBER, 0);
            obj.append(StampdbKey.GIFT_CATEGORY.ORDER, order);
            coll.insert(obj);
            result = obj.getObjectId(StampdbKey.GIFT_CATEGORY.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getMaxOrder() throws EazyException {
        Integer result = null;
        try {
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ORDER, -1);
            DBCursor cursor = coll.find().sort(sortObj);
            if (cursor.size() == 0) {
                return 0;
            } else {
                DBObject obj = cursor.next();
                result = (Integer) obj.get(StampdbKey.GIFT_CATEGORY.ORDER);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean deleteCategory(String catId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ID, id);
            BasicDBObject updatObj = new BasicDBObject("$set", new BasicDBObject(StampdbKey.GIFT_CATEGORY.FLAG, Constant.FLAG.OFF));
            coll.update(obj, updatObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<IEntity> getAllCategory() throws EazyException {
        List<IEntity> result = new ArrayList<>();
        try {
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ORDER, 1);
            DBCursor cursor = coll.find(new BasicDBObject(StampdbKey.GIFT_CATEGORY.FLAG, Constant.FLAG.ON)).sort(sortObj);
            while (cursor.hasNext()) {
                GiftCategory cat = new GiftCategory();
                DBObject dbObject = cursor.next();
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.GIFT_CATEGORY.ID);
                cat.id = id.toString();
                String enName = (String) dbObject.get(StampdbKey.GIFT_CATEGORY.ENGLISH_NAME);
                cat.enCatName = enName;
                String jpName = (String) dbObject.get(StampdbKey.GIFT_CATEGORY.JAPANESE_NAME);
                cat.jpCatName = jpName;
                Integer giftNum = (Integer) dbObject.get(StampdbKey.GIFT_CATEGORY.GIFT_NUMBER);
                cat.giftNumber = giftNum;
                result.add(cat);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getCategoryID() throws EazyException {
        String result = null;
        try {
            DBObject dbObject = coll.findOne();
            if (dbObject != null) {
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.GIFT_CATEGORY.ID);
                result = id.toString();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateGiftNumber(String catId, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT_CATEGORY.GIFT_NUMBER, num);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateOrder(String catId, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ORDER, num);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static IEntity get(String catId) throws EazyException {
        GiftCategory result = null;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if (obj != null) {
                result = new GiftCategory();
                result.id = catId;
                String enName = (String) obj.get(StampdbKey.GIFT_CATEGORY.ENGLISH_NAME);
                result.enCatName = enName;
                String jpName = (String) obj.get(StampdbKey.GIFT_CATEGORY.JAPANESE_NAME);
                result.jpCatName = jpName;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateCategory(String catId, String enCatName, String jpCatName) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject searchObj = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ENGLISH_NAME, enCatName);
            obj.append(StampdbKey.GIFT_CATEGORY.JAPANESE_NAME, jpCatName);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(searchObj, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
