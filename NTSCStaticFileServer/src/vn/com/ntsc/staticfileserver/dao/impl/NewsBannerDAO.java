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
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.dao.DAO;

/**
 *
 * @author RuAc0n
 */
public class NewsBannerDAO {

    private static DBCollection coll;

    static{
         try{
             coll = DAO.getStatisticDB().getCollection( StaticFiledbKey.NEWS_BANNER_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    

    public static String insert(String url) throws EazyException{
        String result = null;
         try{
            BasicDBObject obj =  new BasicDBObject(StaticFiledbKey.NEWS_BANNER.URL, url);
            coll.insert( obj );
            result = obj.getObjectId(StaticFiledbKey.NEWS_BANNER.ID).toString();
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getURL(String giftId) throws EazyException{
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
            
            BasicDBObject obj =  new BasicDBObject(StaticFiledbKey.NEWS_BANNER.ID, id);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if(dbOject == null){
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            result = (String) dbOject.get(StaticFiledbKey.NEWS_BANNER.URL);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
}
