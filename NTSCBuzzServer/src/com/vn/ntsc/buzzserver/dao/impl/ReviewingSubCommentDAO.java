/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.*;
import com.mongodb.DBCollection;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.buzzserver.dao.DAO;

/**
 *
 * @author DuongLTD
 */
public class ReviewingSubCommentDAO {

    private static DBCollection coll;
    static{
        try{
            coll = DAO.getBuzzDB().getCollection( BuzzdbKey.REVIEWING_SUB_COMMENT_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    

    public static String addSubComment(String buzzId, String userId, String commentId, String subCommentId, String value, long time, String ip) throws EazyException{
         String result = null;
        try{
            DBObject insertObj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.USER_ID, userId);
            insertObj.put(BuzzdbKey.REVIEWING_SUB_COMMENT.BUZZ_ID, buzzId);
            insertObj.put(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID, commentId);
            insertObj.put(BuzzdbKey.REVIEWING_SUB_COMMENT.SUB_COMMENT_ID, subCommentId);
            insertObj.put(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME, time);
            insertObj.put(BuzzdbKey.REVIEWING_SUB_COMMENT.VALUE, value);
            insertObj.put(BuzzdbKey.REVIEWING_SUB_COMMENT.IP, ip);
            insertObj.put(BuzzdbKey.REVIEWING_SUB_COMMENT.APPEAR_FLAG, Constant.FLAG.ON);
            coll.insert(insertObj);
            ObjectId id = (ObjectId) insertObj.get(BuzzdbKey.REVIEWING_BUZZ.ID);
            result  = id.toString();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

    public static void removeByCommentId(String commentId) throws EazyException{
        try{
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID, commentId);
            coll.remove(obj);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);   
        }
    }  
    
    public static void removeByBuzzId(String buzzId) throws EazyException{
        try{
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.BUZZ_ID, buzzId);
            coll.remove(obj);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);   
        }
    }     
    
}
