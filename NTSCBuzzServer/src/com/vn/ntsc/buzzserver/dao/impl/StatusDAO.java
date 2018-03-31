/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import com.vn.ntsc.buzzserver.dao.DAO;

/**
 *
 * @author DuongLTD
 */
public class StatusDAO {

    private static DBCollection coll;
    static{
         try{
            coll = DAO.getBuzzDB().getCollection( BuzzdbKey.USER_STATUS_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static int checkStatus(String buzzId,String userId ) throws EazyException{
        int result = Constant.FLAG.OFF;
        try{
            ObjectId id = new ObjectId(userId);
            DBObject query = new BasicDBObject(BuzzdbKey.USER_STATUS.ID, id);
            DBObject obj = coll.findOne(query);
            if(obj != null){
                String status = (String) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                if(status != null && status.equals(buzzId)){
                    result = Constant.FLAG.ON;
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

    public static boolean addStatus(String buzzId, String userId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            BasicDBObject status = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            BasicDBObject updateCommand = new BasicDBObject("$set", status );
            coll.update(findObj, updateCommand, true, false);
            result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }



}
