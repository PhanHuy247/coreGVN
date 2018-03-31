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
import com.mongodb.QueryBuilder;
import eazycommon.constant.Constant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.dao.DAO;

/**
 *
 * @author RuAc0n
 */
public class StickerDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStatisticDB().getCollection(StaticFiledbKey.STICKER_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static String insertSticker(String url, long code) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.STICKER.CODE, code);
            obj.append(StaticFiledbKey.STICKER.URL, url);
            coll.insert(obj);
            result = obj.getObjectId(StaticFiledbKey.STICKER.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getStickerURL(String stickerId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(stickerId);
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.STICKER.ID, id);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if (dbOject == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            result = (String) dbOject.get(StaticFiledbKey.STICKER.URL);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getStickerURL(long code) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.STICKER.CODE, code);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if (dbOject == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            result = (String) dbOject.get(StaticFiledbKey.STICKER.URL);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Map<String, String> getStickerURL(List<String> listImg){
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
            findObj.append(StaticFiledbKey.STICKER.ID, new BasicDBObject("$in", listId));
            
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                
                ObjectId id = (ObjectId) dbObject.get(StaticFiledbKey.STICKER.ID);
                String stk_id = id.toString();
                String url = (String) dbObject.get(StaticFiledbKey.STICKER.URL);
                if(stk_id != null && url != null){
                    result.put(stk_id, url);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static boolean updateSticker(long code, String url) throws EazyException {
        boolean result = false;
        try {
            //search by id
            BasicDBObject searchObj = new BasicDBObject(StaticFiledbKey.STICKER.CODE, code);
            //update command
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.STICKER.URL, url);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(searchObj, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static long getMaxCode() throws EazyException {
        long result = 0;
        try {
            DBObject sort = new BasicDBObject(StaticFiledbKey.STICKER.CODE, -1);
            DBCursor cursor = coll.find().sort(sort).limit(1);
            if (cursor == null || cursor.size() == 0) {
                return 99999;
            }
            DBObject obj = cursor.next();
            result = (Long) obj.get(StaticFiledbKey.STICKER.CODE);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, String> getMapSticker(List<String> listCode) throws EazyException {
        Map<String, String> result = new TreeMap<>();
        try {
            List<Long> list = new ArrayList<>();
            for (String str : listCode) {
                list.add(Long.parseLong(str));
            }
            DBObject searchObj = QueryBuilder.start(StaticFiledbKey.STICKER.CODE).in(list).get();
            DBCursor stickerCursor = coll.find(searchObj);
            while (stickerCursor.hasNext()) {
                DBObject dbObject = stickerCursor.next();
                Long code = (Long) dbObject.get(StaticFiledbKey.STICKER.CODE);
                String fileName = code.toString();
                String path = FilesAndFolders.FOLDERS.STICKERS_FOLDER + (String) dbObject.get(StaticFiledbKey.STICKER.URL);
                result.put(fileName, path);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
