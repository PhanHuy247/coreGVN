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
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import static eazycommon.constant.FilesAndFolders.FOLDERS.EMOJI_IMAGE;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.entity.impl.EmojiCategoryData;
import vn.com.ntsc.staticfileserver.entity.impl.EmojiData;

/**
 *
 * @author hoangnh
 */
public class EmojiItemDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStampDB().getCollection(StampdbKey.EMOJI_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static List<EmojiCategoryData> getAllCategory(List<EmojiCategoryData> listEmoji){
        List<EmojiCategoryData> result = new ArrayList<>();
        try{
            for(EmojiCategoryData item: listEmoji){
                BasicDBObject searchObj = new BasicDBObject(StampdbKey.EMOJI.CATEGORY_ID, item.id);
                searchObj.append(StampdbKey.EMOJI.FLAG, new BasicDBObject("$ne", Constant.FLAG.OFF));
                BasicDBObject sortObj = new BasicDBObject(StampdbKey.EMOJI.ORDER, 1);
                DBCursor cursor = coll.find(searchObj).sort(sortObj);
                List<EmojiData> listItem = new ArrayList<>();
                List<String> listImgId = new ArrayList<>();
                while (cursor.hasNext()) {
                    DBObject dbObject = cursor.next();
                    ObjectId id = (ObjectId) dbObject.get(StampdbKey.EMOJI.ID);
                    String code = (String) dbObject.get(StampdbKey.EMOJI.CODE);
                    String name = (String) dbObject.get(StampdbKey.EMOJI.NAME);
                    String fileId = (String) dbObject.get(StampdbKey.EMOJI.FILE_ID);
                    String fileType = (String) dbObject.get(StampdbKey.EMOJI.FILE_TYPE);
                    
                    JSONParser parser = new JSONParser();
                    JSONArray arrayCode = new JSONArray();
                    try {
                        arrayCode = (JSONArray)parser.parse(code);
                    } catch (ParseException ex) {
                        Util.addErrorLog(ex);
                    }
                    
                    EmojiData emoji = new EmojiData();
                    emoji.id = id.toString();
                    emoji.codeLst = arrayCode;
                    emoji.code = code;
                    emoji.name = name;
                    emoji.fileId = fileId;
                    emoji.fileType = fileType;
                    listItem.add(emoji);
                    if(fileId != null){
                        listImgId.add(fileId);
                    }else{
                        listImgId.add(id.toString());
                    }
                }
                
                Map<String, String> mapUrl = EmojiDAO.getEmojiURL(listImgId);
                
                for(EmojiData emo: listItem){
                    String url = "";
                    if(emo.fileId != null){
                        url = mapUrl.get(emo.fileId);
                    }else{
                        url = mapUrl.get(emo.id);
                    }
                    if(url != null){
                        emo.url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.EMOJI_IMAGE + url;
                    }
                }
                item.listEmoji = listItem;
                item.emojiNum = listItem.size();
                result.add(item);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static List<EmojiData> getListEmoji(String catId){
        List<EmojiData> result = new ArrayList<>();
        try{
            BasicDBObject searchObj = new BasicDBObject(StampdbKey.EMOJI.CATEGORY_ID, catId);
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.EMOJI.ORDER, 1);
            DBCursor cursor = coll.find(searchObj).sort(sortObj);
            List<EmojiData> listItem = new ArrayList<>();
            List<String> listImgId = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.EMOJI.ID);
                String code = (String) dbObject.get(StampdbKey.EMOJI.CODE);
                String name = (String) dbObject.get(StampdbKey.EMOJI.NAME);
                String fileId = (String) dbObject.get(StampdbKey.EMOJI.FILE_ID);
                
                JSONParser parser = new JSONParser();
                JSONArray arrayCode = new JSONArray();
                try {
                    arrayCode = (JSONArray)parser.parse(code);
                } catch (ParseException ex) {
                    Util.addErrorLog(ex);
                }
                    
                EmojiData emoji = new EmojiData();
                emoji.id = id.toString();
                emoji.codeLst = arrayCode;
                emoji.code = code;
                emoji.name = name;
                emoji.fileId = fileId;
                listItem.add(emoji);
                if(fileId != null){
                    listImgId.add(fileId);
                }else{
                    listImgId.add(id.toString());
                }
            }
            
            Map<String, String> mapUrl = EmojiDAO.getEmojiURL(listImgId);
                
            for(EmojiData emo: listItem){
                String url = "";
                if(emo.fileId != null){
                    url = mapUrl.get(emo.fileId);
                }else{
                    url = mapUrl.get(emo.id);
                }
                if(url != null){
                    emo.url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.EMOJI_IMAGE + url;
                }
            }
            result = listItem;
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static List<EmojiData> getAllEmoji() {
        List<EmojiData> result = new ArrayList<>();
        try{
            List<EmojiData> listItem = new ArrayList<>();
            List<String> listImgId = new ArrayList<>();
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                String code = (String) dbObject.get(StampdbKey.EMOJI.CODE);
                String fileId = (String) dbObject.get(StampdbKey.EMOJI.FILE_ID);
                
                EmojiData emoji = new EmojiData();
                emoji.code = code;
                emoji.fileId = fileId;
                listImgId.add(fileId);
                listItem.add(emoji);
            }
            
            Map<String, String> mapUrl = EmojiDAO.getEmojiURL(listImgId);
            for(EmojiData emo: listItem){
                String url = "";
                if(emo.fileId != null){
                    url = mapUrl.get(emo.fileId);
                }else{
                    url = mapUrl.get(emo.id);
                }
                if(url != null){
                    emo.url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.EMOJI_IMAGE + url;
                }
            }
            result = listItem;
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
