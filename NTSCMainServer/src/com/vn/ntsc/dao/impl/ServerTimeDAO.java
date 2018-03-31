/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author RuAc0n
 */
public class ServerTimeDAO {
    private static DBCollection coll;

    static{
         try{
            coll = DBLoader.getSettingDB().getCollection( SettingdbKey.SERVER_LAST_TIME_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static boolean addTime(long lastTime) throws EazyException{
        boolean result = false;
         try{
            DBObject obj = coll.findOne();
            BasicDBObject updateQuery = new BasicDBObject(SettingdbKey.SERVER_LAST_TIME.LAST_TIME, lastTime);
            if(obj == null){
                coll.insert(updateQuery);
            }else{
                coll.update(obj, updateQuery);
            }
            result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Long getTime() throws EazyException{
        Long result = null;
         try{
             DBObject dbO = coll.findOne();
             if(dbO != null)
                 result = (Long) dbO.get(SettingdbKey.SERVER_LAST_TIME.LAST_TIME);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
