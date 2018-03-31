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
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.impl.GiftCategory;

/**
 *
 * @author RuAc0n
 */
public class GiftCategoryDAO{

    private static DBCollection coll;

    static{
         try{
             coll = DBLoader.getStampDB().getCollection( StampdbKey.GIFT_CATEGORY_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }    

    public static List<GiftCategory> getAllCategory(String language) throws EazyException{
        List<GiftCategory> result = new ArrayList<>();
         try{
             BasicDBObject sortObj = new BasicDBObject(StampdbKey.GIFT_CATEGORY.ORDER, 1);
             DBCursor catCursor = coll.find(new BasicDBObject(StampdbKey.GIFT_CATEGORY.FLAG, 1)).sort(sortObj);
             while(catCursor.hasNext()){
                GiftCategory cat = new GiftCategory();
                DBObject dbObject = catCursor.next();
                ObjectId id = (ObjectId) dbObject.get(StampdbKey.GIFT_CATEGORY.ID);
                String catId = id.toString();
                cat.id = catId;
                String enName = (String) dbObject.get(StampdbKey.GIFT_CATEGORY.ENGLISH_NAME);
                String jpName = (String) dbObject.get(StampdbKey.GIFT_CATEGORY.JAPANESE_NAME);
                if(language == null){
                    cat.enCatName = enName;
                    cat.jpCatName = jpName;
                }else{
                    if(language.equals(Constant.LANGUAGE.JAPANESE)){
                        cat.categoryName = jpName;
                    }else{
                        cat.categoryName = enName;
                    }
                }
                result.add(cat);
             }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
