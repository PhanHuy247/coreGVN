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
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.entity.impl.StickerCategoryData;

/**
 *
 * @author hoangnh
 */
public class StkCatDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStampDB().getCollection(StampdbKey.STICKER_CATEGORY_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static List<StickerCategoryData> getAllCategory(){
        List<StickerCategoryData> result = new ArrayList<>();
        try{
            BasicDBObject sortObj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ORDER, 1);
            DBCursor cursor = coll.find(new BasicDBObject(StampdbKey.STICKER_CATEGORY.FLAG, Constant.FLAG.ON)).sort(sortObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                StickerCategoryData item = new StickerCategoryData();
                
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.STICKER_CATEGORY.ID);
                item.id = id.toString();
                String enName = (String) dbObject.get(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME);
                item.enCatName = enName;
                String jpName = (String) dbObject.get(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME);
                item.jpCatName = jpName;
                Integer num = (Integer) dbObject.get(StampdbKey.STICKER_CATEGORY.STICKER_NUMBER);
                item.categoryNumber = num;
                String enDes = (String) dbObject.get(StampdbKey.STICKER_CATEGORY.ENGLISH_DESCRIPTION);
                item.enCatDes= enDes;
                String jpDes = (String) dbObject.get(StampdbKey.STICKER_CATEGORY.JAPANESE_DESCRIPTION);
                item.jpCatDes = jpDes;
                item.version = (Integer) dbObject.get(StampdbKey.STICKER_CATEGORY.VERSION);
                
                result.add(item);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
