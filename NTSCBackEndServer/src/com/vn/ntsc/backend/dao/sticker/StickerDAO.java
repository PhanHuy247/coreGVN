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
import com.vn.ntsc.backend.entity.impl.sticker.Sticker;

/**
 *
 * @author RuAc0n
 */
public class StickerDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getStampDB().getCollection(StampdbKey.STICKER_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static boolean insert(String id, String catId, Long code, int order) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER.ID, oId);
            obj.append(StampdbKey.STICKER.CATEGORY_ID, catId);
            obj.append(StampdbKey.STICKER.CODE, code);
            obj.append(StampdbKey.STICKER.FLAG, Constant.FLAG.ON);
            obj.append(StampdbKey.STICKER.ORDER, order);
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
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.STICKER.ORDER, -1);
            DBCursor cursor = coll.find(new BasicDBObject(StampdbKey.STICKER.CATEGORY_ID, catId)).sort(sortObj);
            if(cursor.size() == 0)
                return 0;
            else{
                DBObject obj = cursor.next();
                result = (Integer) obj.get(StampdbKey.STICKER.ORDER);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }     
    
    public static List<IEntity> list(String catId) throws EazyException {
        List<IEntity> result = new ArrayList<IEntity>();
        try {
            BasicDBObject searchObj = new BasicDBObject(StampdbKey.STICKER.CATEGORY_ID, catId);
            searchObj.append(StampdbKey.STICKER.FLAG, 1);
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.STICKER.ORDER, 1);
            DBCursor cursor = coll.find(searchObj).sort(sortObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Sticker stk = new Sticker();
                stk.categoryId = catId;
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.STICKER.ID);
                stk.id = id.toString();
                Long code = (Long) dbObject.get(StampdbKey.STICKER.CODE);
                stk.code = code;
                result.add(stk);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean delete(String stkId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(stkId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER.ID, id);
            BasicDBObject updateObj = new BasicDBObject("$set", new BasicDBObject(StampdbKey.STICKER.FLAG, Constant.FLAG.OFF));
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
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER.ID, id);
            DBObject dbObj = coll.findOne(obj);
            if (dbObj != null) {
                result = (String) dbObj.get(StampdbKey.STICKER.CATEGORY_ID);
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
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.STICKER.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER.ORDER, num);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }      

}
