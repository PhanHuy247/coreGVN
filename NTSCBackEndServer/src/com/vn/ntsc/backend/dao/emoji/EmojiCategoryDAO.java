/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.emoji;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.vn.ntsc.backend.dao.DBManager;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;

/**
 *
 * @author hoangnh
 */
public class EmojiCategoryDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getStampDB().getCollection(StampdbKey.EMOJI_CATEGORY_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static int getMaxOrder() throws EazyException {
        Integer result = null;
        try {
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.ORDER, -1);
            DBCursor cursor = coll.find().sort(sortObj);
            if(cursor.size() == 0)
                return 0;
            else{
                DBObject obj = cursor.next();
                result = (Integer) obj.get(StampdbKey.EMOJI_CATEGORY.ORDER);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String addEmojiCategory(String fileId, String vnCatName, String vnDes, String enCatName, String enDes, Integer order) throws EazyException {
        String result = null;
        try {
            ObjectId oId = new ObjectId(fileId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.ID, oId);
            obj.append(StampdbKey.EMOJI_CATEGORY.VIET_NAME, vnCatName);
            obj.append(StampdbKey.EMOJI_CATEGORY.VIET_DESCRIPTION, vnDes);
            obj.append(StampdbKey.EMOJI_CATEGORY.ENGLISH_NAME, enCatName);
            obj.append(StampdbKey.EMOJI_CATEGORY.ENGLISH_DESCRIPTION, enDes);
            obj.append(StampdbKey.EMOJI_CATEGORY.ORDER, order);
            obj.append(StampdbKey.EMOJI_CATEGORY.EMOJI_NUMBER, 0);
            obj.append(StampdbKey.EMOJI_CATEGORY.FILE_ID, fileId);
            obj.append(StampdbKey.EMOJI_CATEGORY.FLAG, 0);
            coll.insert(obj);
            result = fileId;
        }catch (Exception ex) {
            Util.addErrorLog(ex);                    
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static void updateEmojiCategory(String catId, String fileId, String vnCatName, String vnDes, String enCatName, String enDes, Integer flag) throws EazyException{
        try{
            Boolean isValid = ObjectId.isValid(catId);
            if(isValid){
                ObjectId oId = new ObjectId(catId);
                BasicDBObject findObj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.ID, oId);
                
                BasicDBObject updateObj = new BasicDBObject();
                updateObj.append(StampdbKey.EMOJI_CATEGORY.VIET_NAME, vnCatName);
                updateObj.append(StampdbKey.EMOJI_CATEGORY.VIET_DESCRIPTION, vnDes);
                updateObj.append(StampdbKey.EMOJI_CATEGORY.ENGLISH_NAME, enCatName);
                updateObj.append(StampdbKey.EMOJI_CATEGORY.ENGLISH_DESCRIPTION, enDes);
                
                if(flag != null){
                    updateObj.append(StampdbKey.EMOJI_CATEGORY.FLAG, flag);
                }
                
                if(fileId != null){
                    updateObj.append(StampdbKey.EMOJI_CATEGORY.FILE_ID, fileId);
                }
                
                BasicDBObject incObj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.VERSION, 1);
                
                BasicDBObject updateCommand = new BasicDBObject();
                updateCommand.append("$set", updateObj);
                updateCommand.append("$inc", incObj);
                
                DBObject dboject = coll.findAndModify(findObj, updateCommand);
                if (dboject != null) {
                    
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
    public static boolean deleteCategory(String catId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.ID, id);
            coll.remove(obj);
            result = true;
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
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.EMOJI_NUMBER, num);
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
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.VERSION, 1);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
