/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.DAO;

/**
 *
 * @author RuAc0n
 */
public class GiftDAO {

    private static DBCollection coll;

    static{
         try{
             coll = DAO.getStatisticDB().getCollection( StaticFiledbKey.GIFT_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    

    public static String insertGift(String url) throws EazyException{
        String result = null;
         try{
            BasicDBObject obj =  new BasicDBObject(StaticFiledbKey.GIFT.URL, url);
            coll.insert( obj );
            result = obj.getObjectId(StaticFiledbKey.GIFT.ID).toString();
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getGiftURL(String giftId) throws EazyException{
        String result = null;
         try{
           
            ObjectId id = null;
            try{
                id = new ObjectId(giftId);
            }catch(Exception ex){
                id = null;
            }
            if(id == null)
                return null;
            
            BasicDBObject obj =  new BasicDBObject(StaticFiledbKey.GIFT.ID, id);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if(dbOject == null){
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            result = (String) dbOject.get(StaticFiledbKey.GIFT.URL);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    

    public static Map<String, String> getGiftUrl(List<String> listGiftId) {
        Map<String, String> mapUrl = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listGiftId){
            listid.add(new ObjectId(id));
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.GIFT.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.GIFT.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.GIFT.URL);
            url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.GIFT + url;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
}
