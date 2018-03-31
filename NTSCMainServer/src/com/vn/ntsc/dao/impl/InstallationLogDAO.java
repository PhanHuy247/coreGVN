/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author DuongLTD
 */
public class InstallationLogDAO {

    private static DBCollection coll;


    static{
         try{
             coll = DBLoader.getLogDB().getCollection( LogdbKey.INSTALLATION_LOG_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean insert(String hour , int type, String uniqueNumber) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject insertObject = new BasicDBObject(LogdbKey.INSTALLATION_LOG.HOUR, hour);
            insertObject.append(LogdbKey.INSTALLATION_LOG.DEVICE_TYPE, type);
            insertObject.append(LogdbKey.INSTALLATION_LOG.UNIQUE_NUMBER, uniqueNumber);
            coll.insert(insertObject);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
     
    
}
