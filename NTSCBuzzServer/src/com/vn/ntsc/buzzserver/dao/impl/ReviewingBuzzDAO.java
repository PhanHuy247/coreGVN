/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.*;
import com.mongodb.DBCollection;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import com.vn.ntsc.buzzserver.dao.DAO;

/**
 *
 * @author DuongLTD
 */
public class ReviewingBuzzDAO {

    private static DBCollection coll;
    static{
        try{
            coll = DAO.getBuzzDB().getCollection( BuzzdbKey.REVIEWING_BUZZ_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    

    public static String addBuzz(String buzzId, String userId, String buzzVal, int buzzType, long time, String ip) throws EazyException{
         String result = null;
        try{
            DBObject insertObj = new BasicDBObject(BuzzdbKey.REVIEWING_BUZZ.ID, new ObjectId(buzzId));
            insertObj.put(BuzzdbKey.REVIEWING_BUZZ.USER_ID, userId);
            insertObj.put(BuzzdbKey.REVIEWING_BUZZ.BUZZ_VALUE, buzzVal);
            insertObj.put(BuzzdbKey.REVIEWING_BUZZ.BUZZ_TYPE, buzzType);
            insertObj.put(BuzzdbKey.REVIEWING_BUZZ.BUZZ_TIME, time);
            insertObj.put(BuzzdbKey.REVIEWING_BUZZ.IP, ip);
            coll.insert(insertObj);
            ObjectId id = (ObjectId) insertObj.get(BuzzdbKey.REVIEWING_BUZZ.ID);
            result  = id.toString();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

}
