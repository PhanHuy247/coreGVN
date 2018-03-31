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
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import static eazycommon.constant.FilesAndFolders.FOLDERS.EMOJI_IMAGE;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.entity.impl.EmojiCategoryData;
import vn.com.ntsc.staticfileserver.entity.impl.EmojiData;
import vn.com.ntsc.staticfileserver.entity.impl.StickerCategoryData;
import vn.com.ntsc.staticfileserver.entity.impl.StickerData;

/**
 *
 * @author hoangnh
 */
public class EmojiDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStatisticDB().getCollection(StaticFiledbKey.EMOJI_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static String insertEmoji(String url) throws EazyException{
        String result = "";
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.EMOJI.URL, url);
            coll.insert(obj);
            ObjectId id = (ObjectId) obj.get(StaticFiledbKey.EMOJI.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<EmojiCategoryData> getCategoryUrl(List<EmojiCategoryData> listEmoji){
        List<EmojiCategoryData> result = new ArrayList<>();
        try{
            for(EmojiCategoryData item: listEmoji){
                String fileId = "";
                if(item.fileId != null){
                    fileId = item.fileId;
                }else{
                    fileId = item.id;
                }
                Boolean isValid = ObjectId.isValid(fileId);
                if(isValid){
                    ObjectId id = new ObjectId(fileId);
                    BasicDBObject obj = new BasicDBObject(StaticFiledbKey.EMOJI.ID, id);
                    DBObject dbOject = (DBObject) coll.findOne(obj);
                    if(dbOject != null){
                        String url = (String) dbOject.get(StaticFiledbKey.EMOJI.URL);
                        item.categoryUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.EMOJI_IMAGE +url;
                    }
                }
                result.add(item);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Map<String, String> getEmojiURL(List<String> listImg){
        Map<String, String> result = new TreeMap<>();
        try{
            List<ObjectId> listId = new ArrayList<>();
            for(String item: listImg){
                Boolean isValid = ObjectId.isValid(item);
                if(isValid){
                    ObjectId id = new ObjectId(item);
                    listId.add(id);
                }
            }
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(StaticFiledbKey.EMOJI.ID, new BasicDBObject("$in", listId));
            
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                
                ObjectId id = (ObjectId) dbObject.get(StaticFiledbKey.EMOJI.ID);
                String emojiId = id.toString();
                String url = (String) dbObject.get(StaticFiledbKey.EMOJI.URL);
                if(emojiId != null && url != null){
                    result.put(emojiId, url);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
