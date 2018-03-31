/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.entity.impl.EmojiCategoryData;
import vn.com.ntsc.staticfileserver.entity.impl.StickerCategoryData;

/**
 *
 * @author hoangnh
 */
public class EmojiCategoryDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStampDB().getCollection(StampdbKey.EMOJI_CATEGORY_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static List<EmojiCategoryData> getAllCategory(){
        List<EmojiCategoryData> result = new ArrayList<>();
        try{
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.ORDER, 1);
            
            DBCursor cursor = coll.find().sort(sortObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                EmojiCategoryData item = new EmojiCategoryData();
                
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.EMOJI_CATEGORY.ID);
                item.id = id.toString();
                String enName = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.ENGLISH_NAME);
                item.enCatName = enName;
                String vnName = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.VIET_NAME);
                item.vnCatName = vnName;
                Integer num = (Integer) dbObject.get(StampdbKey.EMOJI_CATEGORY.EMOJI_NUMBER);
                item.emojiNum = num;
                String enDes = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.ENGLISH_DESCRIPTION);
                item.enCatDes= enDes;
                String vnDes = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.VIET_DESCRIPTION);
                item.vnCatDes = vnDes;
                String fileId = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.FILE_ID);
                item.fileId = fileId;
                if(dbObject.get(StampdbKey.EMOJI_CATEGORY.FLAG) != null){
                    item.flag = (Integer) dbObject.get(StampdbKey.EMOJI_CATEGORY.FLAG);
                }
                if(dbObject.get(StampdbKey.EMOJI_CATEGORY.VERSION) != null){
                    item.version = (Integer) dbObject.get(StampdbKey.EMOJI_CATEGORY.VERSION);
                }else{
                    item.version = 0;
                }
                result.add(item);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static List<EmojiCategoryData> getActiveCategory(){
        List<EmojiCategoryData> result = new ArrayList<>();
        try{
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.ORDER, 1);
            
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(StampdbKey.EMOJI_CATEGORY.FLAG, new BasicDBObject("$ne", 0));
            
            DBCursor cursor = coll.find(findObj).sort(sortObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                EmojiCategoryData item = new EmojiCategoryData();
                
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.EMOJI_CATEGORY.ID);
                item.id = id.toString();
                String enName = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.ENGLISH_NAME);
                item.enCatName = enName;
                String vnName = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.VIET_NAME);
                item.vnCatName = vnName;
                Integer num = (Integer) dbObject.get(StampdbKey.EMOJI_CATEGORY.EMOJI_NUMBER);
                item.emojiNum = num;
                String enDes = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.ENGLISH_DESCRIPTION);
                item.enCatDes= enDes;
                String vnDes = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.VIET_DESCRIPTION);
                item.vnCatDes = vnDes;
                String fileId = (String) dbObject.get(StampdbKey.EMOJI_CATEGORY.FILE_ID);
                item.fileId = fileId;
                if(dbObject.get(StampdbKey.EMOJI_CATEGORY.FLAG) != null){
                    item.flag = (Integer) dbObject.get(StampdbKey.EMOJI_CATEGORY.FLAG);
                }
                if(dbObject.get(StampdbKey.EMOJI_CATEGORY.VERSION) != null){
                    item.version = (Integer) dbObject.get(StampdbKey.EMOJI_CATEGORY.VERSION);
                }else{
                    item.version = 0;
                }
                result.add(item);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static EmojiCategoryData getCategory(String catId){
        EmojiCategoryData result = null;
        try{
            Boolean isValid = ObjectId.isValid(catId);
            if(isValid){
                ObjectId id = new ObjectId(catId);
                BasicDBObject obj = new BasicDBObject(StampdbKey.EMOJI_CATEGORY.ID, id);
                DBObject dboject = coll.findOne(obj);
                if (dboject != null) {
                    EmojiCategoryData item = new EmojiCategoryData();
                
                    item.id = catId;
                    String enName = (String) dboject.get(StampdbKey.EMOJI_CATEGORY.ENGLISH_NAME);
                    item.enCatName = enName;
                    String vnName = (String) dboject.get(StampdbKey.EMOJI_CATEGORY.VIET_NAME);
                    item.vnCatName = vnName;
                    Integer num = (Integer) dboject.get(StampdbKey.EMOJI_CATEGORY.EMOJI_NUMBER);
                    item.emojiNum = num;
                    String enDes = (String) dboject.get(StampdbKey.EMOJI_CATEGORY.ENGLISH_DESCRIPTION);
                    item.enCatDes= enDes;
                    String vnDes = (String) dboject.get(StampdbKey.EMOJI_CATEGORY.VIET_DESCRIPTION);
                    item.vnCatDes = vnDes;
                    
                    result = item;
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
