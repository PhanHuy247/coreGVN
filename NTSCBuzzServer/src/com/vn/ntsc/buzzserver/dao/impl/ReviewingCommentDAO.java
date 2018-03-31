/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.*;
import com.mongodb.DBCollection;
import eazycommon.constant.Constant;
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
public class ReviewingCommentDAO {

    private static DBCollection coll;
    static{
        try{
            coll = DAO.getBuzzDB().getCollection( BuzzdbKey.REVIEWING_COMMENT_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    

    public static String addComment(String buzzId, String userId, String commentId, String value, int appearFlag, long time, String ip) throws EazyException{
         String result = null;
        try{
            DBObject insertObj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.USER_ID, userId);
            insertObj.put(BuzzdbKey.REVIEWING_COMMENT.BUZZ_ID, buzzId);
            insertObj.put(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID, commentId);
            insertObj.put(BuzzdbKey.REVIEWING_COMMENT.TIME, time);
            insertObj.put(BuzzdbKey.REVIEWING_COMMENT.VALUE, value);
            insertObj.put(BuzzdbKey.REVIEWING_COMMENT.REVIEWING_SUB_COMMENT_NUMBER, 0);
            insertObj.put(BuzzdbKey.REVIEWING_COMMENT.IP, ip);
//            insertObj.put(BuzzdbKey.REVIEWING_COMMENT.REVIEW_FLAG, Constant.NO);
            insertObj.put(BuzzdbKey.REVIEWING_COMMENT.APPEAR_FLAG, appearFlag);
            insertObj.put(BuzzdbKey.REVIEWING_COMMENT.ACTION_TIME, Util.currentTime());
            coll.insert(insertObj);
            ObjectId id = (ObjectId) insertObj.get(BuzzdbKey.REVIEWING_BUZZ.ID);
            result  = id.toString();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }
    
    public static void addSubComment(String commentId, String buzzId, String userId, String value, long time, String ip) throws EazyException{
        try{
            DBObject findObj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID, commentId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
            //update command
                BasicDBObject incObj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.REVIEWING_SUB_COMMENT_NUMBER , 1);
                BasicDBObject updateCommand = new BasicDBObject("$inc", incObj );
                updateCommand.append("$set", new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.ACTION_TIME, Util.currentTime()));
                coll.update(findObj, updateCommand);
            }else{
                DBObject insertObj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.USER_ID, userId);
                insertObj.put(BuzzdbKey.REVIEWING_COMMENT.BUZZ_ID, buzzId);
                insertObj.put(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID, commentId);
                insertObj.put(BuzzdbKey.REVIEWING_COMMENT.TIME, time);
                insertObj.put(BuzzdbKey.REVIEWING_COMMENT.VALUE, value);
                insertObj.put(BuzzdbKey.REVIEWING_COMMENT.REVIEWING_SUB_COMMENT_NUMBER, 1);
                insertObj.put(BuzzdbKey.REVIEWING_COMMENT.IP, ip);
                insertObj.put(BuzzdbKey.REVIEWING_COMMENT.REVIEW_FLAG, Constant.FLAG.ON);
                insertObj.put(BuzzdbKey.REVIEWING_COMMENT.APPEAR_FLAG, Constant.FLAG.ON);
                insertObj.put(BuzzdbKey.REVIEWING_COMMENT.ACTION_TIME, Util.currentTime());
                coll.insert(insertObj);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
     }
    
    public static void removeByBuzzId(String buzzId) throws EazyException{
        try{
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.BUZZ_ID, buzzId);
            coll.remove(obj);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);   
        }
    }    
    
    public static void remove(String commentId) throws EazyException{
        try{
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID, commentId);
            coll.remove(obj);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);   
        }
    }    

    public static void updateAppearFlagByBuzzId(String buzzId, Integer flag) throws EazyException{
        try{
            DBObject findObj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.BUZZ_ID, buzzId);
            //update command
            BasicDBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.APPEAR_FLAG , flag);
            obj.append(BuzzdbKey.REVIEWING_COMMENT.ACTION_TIME , Util.currentTime());
            BasicDBObject updateCommand = new BasicDBObject("$set", obj );
            coll.updateMulti(findObj, updateCommand);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
     }     
    
}
