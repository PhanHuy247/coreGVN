/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

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
import com.vn.ntsc.dao.DBLoader;


/**
 *
 * @author RuAc0n
 */
public class StickerDAO {
    
    private static DBCollection coll;

    static{
         try{
             coll = DBLoader.getStampDB().getCollection( StampdbKey.STICKER_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }

   public static List<String> getListSticker(String catId) throws EazyException{
        List<String> result = new ArrayList<>();
        try{
             BasicDBObject searchObj = new BasicDBObject(StampdbKey.STICKER.CATEGORY_ID, catId);
             DBObject sortObj = new BasicDBObject(StampdbKey.STICKER.ORDER, 1);
             DBCursor stickerCursor = coll.find(searchObj).sort(sortObj);
             while(stickerCursor.hasNext()){
                DBObject dbObject = stickerCursor.next();
                Integer flag = (Integer) dbObject.get(StampdbKey.STICKER.FLAG);
                if(flag != null && flag == Constant.FLAG.ON){
                    Long code = (Long) dbObject.get(StampdbKey.STICKER.CODE);
                    String fileName =code.toString();
                    result.add(fileName);
                }
             }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
   }   
   
}
