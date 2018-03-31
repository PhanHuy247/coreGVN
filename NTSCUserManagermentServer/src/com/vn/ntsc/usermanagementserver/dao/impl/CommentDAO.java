/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.*;
import com.mongodb.DBCollection;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
/**
 *
 * @author DuongLTD
 */
public class CommentDAO {

    private static DBCollection coll;
    static{
         try{
            coll = DatabaseLoader.getBuzzDB().getCollection( BuzzdbKey.COMMENT_DETAIL_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static String getUserId(String commentId) throws EazyException{
         String result = null;
        try{
            ObjectId id = new ObjectId(commentId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject obj =  coll.findOne(findObj);
            if(obj != null){
                result = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
}

