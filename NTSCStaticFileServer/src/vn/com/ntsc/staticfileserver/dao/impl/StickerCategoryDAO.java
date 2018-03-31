/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import static eazycommon.constant.FilesAndFolders.FOLDERS.STICKER_CATEGORY_IMAGE;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.entity.impl.StickerCategoryData;

/**
 *
 * @author RuAc0n
 */
public class StickerCategoryDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStatisticDB().getCollection(StaticFiledbKey.STICKER_CATEGORY_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static String insertCategory(String url) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.STICKER_CATEGORY.AVATAR_URL, url);
            coll.insert(obj);
            result = obj.getObjectId(StaticFiledbKey.STICKER_CATEGORY.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean insertCategory(String catId, String url) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.STICKER_CATEGORY.ID, id);
            findObj.append(StaticFiledbKey.STICKER_CATEGORY.AVATAR_URL, url);
            coll.save(findObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getCategoryAvaPath(String catId) throws EazyException {
        String result = null;
        try {

            ObjectId id = null;
            try {
                id = new ObjectId(catId);
            } catch (Exception ex) {
                id = null;
            }
            if (id == null) {
                return null;
            }
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.STICKER_CATEGORY.ID, id);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if (dbOject == null) {
                return null;
            }
            result = (String) dbOject.get(StaticFiledbKey.STICKER_CATEGORY.AVATAR_URL);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<StickerCategoryData> getCategoryUrl(List<StickerCategoryData> listSticker){
        List<StickerCategoryData> result = new ArrayList<>();
        try{
            for(StickerCategoryData item: listSticker){
                Boolean isValid = ObjectId.isValid(item.id);
                if(isValid){
                    ObjectId id = new ObjectId(item.id);
                    BasicDBObject obj = new BasicDBObject(StaticFiledbKey.STICKER_CATEGORY.ID, id);
                    DBObject dbOject = (DBObject) coll.findOne(obj);
                    if(dbOject != null){
                        String url = (String) dbOject.get(StaticFiledbKey.STICKER_CATEGORY.AVATAR_URL);
                        item.categoryUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.STICKER_CATEGORY_IMAGE +url;
                    }
                }
                result.add(item);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
