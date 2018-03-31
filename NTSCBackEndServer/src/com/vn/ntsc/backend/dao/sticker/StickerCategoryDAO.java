/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.sticker;

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
import com.vn.ntsc.backend.entity.impl.sticker.StickerCategory;

/**
 *
 * @author RuAc0n
 */
public class StickerCategoryDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getStampDB().getCollection(StampdbKey.STICKER_CATEGORY_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static String insert(String id,String enCatName, String jpCatName, int price, int type, String enDes, String jpDes, int order) throws EazyException {
        String result = null;
        try {
            ObjectId oId = new ObjectId(id);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, oId);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME, jpCatName);
            obj.append(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME, enCatName);
            obj.append(StampdbKey.STICKER_CATEGORY.FLAG, Constant.FLAG.ON);
            obj.append(StampdbKey.STICKER_CATEGORY.STICKER_NUMBER, 0);
            obj.append(StampdbKey.STICKER_CATEGORY.ORDER, order);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, type);
            obj.append(StampdbKey.STICKER_CATEGORY.ENGLISH_DESCRIPTION, enDes);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_DESCRIPTION, jpDes);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE, price);
            obj.append(StampdbKey.STICKER_CATEGORY.VERSION, 1);
            coll.insert(obj);
            result = id;
        }catch (Exception ex) {
            Util.addErrorLog(ex);                    
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

   public static String insertVer2(String id,String enCatName, String jpCatName, 
           int price, int type, String enDes, Integer newFlag, Integer liveTime, String jpDes, int order) throws EazyException {
        String result = null;
        try {
            ObjectId oId = new ObjectId(id);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, oId);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME, jpCatName);
            obj.append(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME, enCatName);
            obj.append(StampdbKey.STICKER_CATEGORY.FLAG, Constant.FLAG.ON);
            obj.append(StampdbKey.STICKER_CATEGORY.STICKER_NUMBER, 0);
            obj.append(StampdbKey.STICKER_CATEGORY.ORDER, order);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, type);
            obj.append(StampdbKey.STICKER_CATEGORY.ENGLISH_DESCRIPTION, enDes);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_DESCRIPTION, jpDes);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE, price);
            obj.append(StampdbKey.STICKER_CATEGORY.NEW_FLAG, newFlag);
            obj.append(StampdbKey.STICKER_CATEGORY.VERSION, 1);
            if(liveTime != null)
                obj.append(StampdbKey.STICKER_CATEGORY.LIVE_TIME, liveTime);
            obj.append(StampdbKey.STICKER_CATEGORY.PUBLIC_FLAG, 0);
            coll.insert(obj);
            result = id;
        }catch (Exception ex) {
            Util.addErrorLog(ex);                    
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static IEntity get(String catId) throws EazyException {
        StickerCategory result = null;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if(obj != null){
                result = new StickerCategory();
                result.id = catId;
                String enName = (String) obj.get(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME);
                result.enCatName = enName;
                String jpName = (String) obj.get(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME);
                result.jpCatName = jpName;
                Integer price = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE);
                result.categoryPrice = price;
                Integer type = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE);
                result.catType = type;
                Integer publicFlag = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.PUBLIC_FLAG);
                result.publicFlag = publicFlag;
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }       
    
    public static int getMaxOrder() throws EazyException {
        Integer result = null;
        try {
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ORDER, -1);
            DBCursor cursor = coll.find().sort(sortObj);
            if(cursor.size() == 0)
                return 0;
            else{
                DBObject obj = cursor.next();
                result = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.ORDER);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    

    public static int getCategoryType(String catId) throws EazyException {
        int result = 0;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject findObj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null)
                result = (Integer)obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    

    public static int getPublicFlag(String catId) throws EazyException {
        int result = 1;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject findObj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null)
                result = (Integer)obj.get(StampdbKey.STICKER_CATEGORY.PUBLIC_FLAG);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean checkCategory(String catId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            DBObject res = coll.findOne(obj);
            if(res == null)
                return false;
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean deleteCategory(String catId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            BasicDBObject updatObj = new BasicDBObject("$set", new BasicDBObject(StampdbKey.STICKER_CATEGORY.FLAG, Constant.FLAG.OFF));
            coll.update(obj, updatObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<IEntity> getAllCategory() throws EazyException {
        List<IEntity> result = new ArrayList<>();
        try {
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ORDER, 1);
            DBCursor cursor = coll.find(new BasicDBObject(StampdbKey.STICKER_CATEGORY.FLAG, Constant.FLAG.ON)).sort(sortObj);
            while (cursor.hasNext()) {
                StickerCategory cat = new StickerCategory();
                DBObject dbObject = cursor.next();
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.STICKER_CATEGORY.ID);
                cat.id = id.toString();
                String enName = (String) dbObject.get(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME);
                cat.enCatName = enName;
                String jpName = (String) dbObject.get(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME);
                cat.jpCatName = jpName;
                Integer num = (Integer) dbObject.get(StampdbKey.STICKER_CATEGORY.STICKER_NUMBER);
                cat.categoryNumber = num;
                Integer price = (Integer) dbObject.get(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE);
                cat.categoryPrice = price;
                Integer type = (Integer) dbObject.get(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE);
                cat.catType = type;
                String enDes = (String) dbObject.get(StampdbKey.STICKER_CATEGORY.ENGLISH_DESCRIPTION);
                cat.enCatDes= enDes;
                String jpDes = (String) dbObject.get(StampdbKey.STICKER_CATEGORY.JAPANESE_DESCRIPTION);
                cat.jpCatDes = jpDes;
                Integer newFlag = (Integer) dbObject.get(StampdbKey.STICKER_CATEGORY.NEW_FLAG);
                cat.newFlag = newFlag;
                Integer liveTime = (Integer) dbObject.get(StampdbKey.STICKER_CATEGORY.LIVE_TIME);
                cat.liveTime = liveTime;
                Integer publicFlag = (Integer) dbObject.get(StampdbKey.STICKER_CATEGORY.PUBLIC_FLAG);
                cat.publicFlag = publicFlag;                
                result.add(cat);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateNumber(String catId, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.STICKER_NUMBER, num);
            obj.append(StampdbKey.STICKER_CATEGORY.VERSION, 1);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateVersion(String catId) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.VERSION, 1);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
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
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ORDER, num);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }      
    
    public static boolean updatePublicFlag(String catId) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.PUBLIC_FLAG, 1);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean updateCategory(String catId, String enCatName, String jpCatName, String enDes, String jpDes, int type, int price) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject searchObj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME, enCatName);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME, jpCatName);
            obj.append(StampdbKey.STICKER_CATEGORY.ENGLISH_DESCRIPTION, enDes);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_DESCRIPTION, jpDes);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, type);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE, price);
            BasicDBObject incObj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.VERSION, 1);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            updateCommand.append("$inc", incObj);
            coll.update(searchObj, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateCategoryVer2(String catId, String enCatName, String jpCatName, String enDes, 
            String jpDes, Integer newFlag, Integer liveTime, int type, int price) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject searchObj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME, enCatName);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME, jpCatName);
            obj.append(StampdbKey.STICKER_CATEGORY.ENGLISH_DESCRIPTION, enDes);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_DESCRIPTION, jpDes);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, type);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE, price);
            obj.append(StampdbKey.STICKER_CATEGORY.NEW_FLAG, newFlag);
            obj.append(StampdbKey.STICKER_CATEGORY.LIVE_TIME, liveTime);
            BasicDBObject incObj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.VERSION, 1);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            updateCommand.append("$inc", incObj);
            coll.update(searchObj, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
}
