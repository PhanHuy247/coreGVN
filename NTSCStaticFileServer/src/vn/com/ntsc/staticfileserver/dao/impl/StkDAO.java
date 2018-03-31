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
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.entity.impl.StickerCategoryData;
import vn.com.ntsc.staticfileserver.entity.impl.StickerData;

/**
 *
 * @author hoangnh
 */
public class StkDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStampDB().getCollection(StampdbKey.STICKER_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static List<StickerCategoryData> getAllCategory(List<StickerCategoryData> listSticker){
        List<StickerCategoryData> result = new ArrayList<>();
        try{
            for(StickerCategoryData item: listSticker){
                BasicDBObject searchObj = new BasicDBObject(StampdbKey.STICKER.CATEGORY_ID, item.id);
                searchObj.append(StampdbKey.STICKER.FLAG, 1);
                BasicDBObject sortObj = new BasicDBObject(StampdbKey.STICKER.ORDER, 1);
                DBCursor cursor = coll.find(searchObj).sort(sortObj);
                List<StickerData> listStk = new ArrayList<>();
                List<String> imgId = new ArrayList<>();
                while (cursor.hasNext()) {
                    DBObject dbObject = cursor.next();
                    ObjectId id = (ObjectId) dbObject.get(StampdbKey.STICKER.ID);
                    Long code = (Long) dbObject.get(StampdbKey.STICKER.CODE);
                    StickerData stk = new StickerData();
                    stk.id = id.toString();
                    stk.code = code;
                    listStk.add(stk);
                    imgId.add(id.toString());
                }
                
                Map<String, String> mapUrl = StickerDAO.getStickerURL(imgId);
                
                for(StickerData stk: listStk){
                    String url = mapUrl.get(stk.id);
                    if(url != null){
                        stk.url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.STICKER_IMAGE + url;
                    }
                }
                item.listSticker = listStk;
                result.add(item);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
