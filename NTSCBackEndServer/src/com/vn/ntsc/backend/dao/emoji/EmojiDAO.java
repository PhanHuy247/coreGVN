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
import java.util.HashSet;
import java.util.Set;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author hoangnh
 */
public class EmojiDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getStampDB().getCollection(StampdbKey.EMOJI_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static int getMaxOrder() throws EazyException {
        Integer result = null;
        try {
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.EMOJI.ORDER, -1);
            DBCursor cursor = coll.find().sort(sortObj);
            if(cursor.size() == 0)
                return 0;
            else{
                DBObject obj = cursor.next();
                result = (Integer) obj.get(StampdbKey.EMOJI.ORDER);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String addEmoji(String fileId, String categoryId, String code, String name, Integer order, String fileType, Integer duplicate) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject();
            obj.append(StampdbKey.EMOJI.CATEGORY_ID, categoryId);
            obj.append(StampdbKey.EMOJI.CODE, code);
            obj.append(StampdbKey.EMOJI.NAME, name);
            obj.append(StampdbKey.EMOJI.ORDER, order);
            obj.append(StampdbKey.EMOJI.FILE_ID, fileId);
            obj.append(StampdbKey.EMOJI.FILE_TYPE, fileType);
            obj.append(StampdbKey.EMOJI.WARNING_FLAG, duplicate);
            coll.insert(obj);
            result = fileId;
        }catch (Exception ex) {
            Util.addErrorLog(ex);                    
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static void updateEmoji(String id, String fileId, String categoryId, String code, String name, String fileType, Integer duplicate) throws EazyException{
        try{
            Boolean isValid = ObjectId.isValid(id);
            if(isValid){
                ObjectId oId = new ObjectId(id);
                BasicDBObject findObj = new BasicDBObject(StampdbKey.EMOJI.ID, oId);
                findObj.append(StampdbKey.EMOJI.CATEGORY_ID, categoryId);
                
                BasicDBObject updateObj = new BasicDBObject();
                updateObj.append(StampdbKey.EMOJI.NAME, name);
                updateObj.append(StampdbKey.EMOJI.CODE, code);
                if(fileId != null){
                    updateObj.append(StampdbKey.EMOJI.FILE_ID, fileId);
                    updateObj.append(StampdbKey.EMOJI.FILE_TYPE, fileType);
                }
                updateObj.append(StampdbKey.EMOJI.WARNING_FLAG, duplicate);
                DBObject dboject = coll.findAndModify(findObj, new BasicDBObject("$set", updateObj));
                if (dboject != null) {
                    
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
    public static boolean delEmojiCategory(String catId){
        boolean result = false;
        try {
            BasicDBObject obj = new BasicDBObject(StampdbKey.EMOJI.CATEGORY_ID, catId);
            coll.remove(obj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static boolean delEmoji(String catId, String emojiId){
        boolean result = false;
        try{
            Boolean isValid = ObjectId.isValid(emojiId);
            if(isValid){
                ObjectId id = new ObjectId(emojiId);
                BasicDBObject obj = new BasicDBObject(StampdbKey.EMOJI.ID, id);
                obj.append(StampdbKey.EMOJI.CATEGORY_ID, catId);
                coll.remove(obj);
                result = true;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Set<String> getListCode(String emojiId){
        Set<String> result = new HashSet<>();
        try{
            BasicDBObject findObject = new BasicDBObject();
            if(emojiId != null){
                Boolean isValid = ObjectId.isValid(emojiId);
                if(isValid){
                    ObjectId id = new ObjectId(emojiId);
                    findObject.append(StampdbKey.EMOJI.ID, new BasicDBObject("$ne", id));
                }
            }
            DBCursor cursor = coll.find(findObject);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                String code = (String) dbObject.get(StampdbKey.EMOJI.CODE);
                
                JSONArray query = (JSONArray) new JSONParser().parse(code);
                for (int i = 0; i < query.size(); i++) {
                    String item = query.get(i).toString();
                    result.add(item);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Set<String> getSetCode(String emojiId, String catId, Boolean inCatId){
        Set<String> result = new HashSet<>();
        try{
            BasicDBObject findObject = new BasicDBObject();
            if(inCatId){
                findObject.append(StampdbKey.EMOJI.CATEGORY_ID, catId);
            }else{
                findObject.append(StampdbKey.EMOJI.CATEGORY_ID, new BasicDBObject("$ne", catId));
            }
            if(emojiId != null){
                Boolean isValid = ObjectId.isValid(emojiId);
                if(isValid){
                    ObjectId id = new ObjectId(emojiId);
                    findObject.append(StampdbKey.EMOJI.ID, new BasicDBObject("$ne", id));
                }
            }
            DBCursor cursor = coll.find(findObject);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                String code = (String) dbObject.get(StampdbKey.EMOJI.CODE);
                
                JSONArray query = (JSONArray) new JSONParser().parse(code);
                for (int i = 0; i < query.size(); i++) {
                    String item = query.get(i).toString();
                    result.add(item);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Boolean isEmojiNameExist(String catId, String emojiName, String emojiId){
        Boolean result = false;
        try{
            BasicDBObject findObject = new BasicDBObject();
            if(emojiId != null){
                Boolean isValid = ObjectId.isValid(emojiId);
                if(isValid){
                    ObjectId id = new ObjectId(emojiId);
                    findObject.append(StampdbKey.EMOJI.ID, new BasicDBObject("$ne", id));
                }
            }
            findObject.append(StampdbKey.EMOJI.NAME, emojiName);
            findObject.append(StampdbKey.EMOJI.CATEGORY_ID, catId);
            DBObject obj = coll.findOne(findObject);
            if(obj != null){
                result = true;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
