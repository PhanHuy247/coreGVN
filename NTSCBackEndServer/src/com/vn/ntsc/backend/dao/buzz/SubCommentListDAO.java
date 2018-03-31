/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.dao.buzz;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;

/**
 *
 * @author DuongLTD
 */
public class SubCommentListDAO {

    private static DBCollection coll;
    static{
         try{
            coll = DBManager.getBuzzDB().getCollection( BuzzdbKey.SUB_COMMENT_LIST_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static List<String> getListSubComment(String commentId) throws EazyException{
        List<String> resutl = new ArrayList<>();
        try{
            ObjectId id = new ObjectId(commentId);
            DBObject query = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.ID, id);
            DBObject obj = coll.findOne(query);
            if(obj != null){
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_LIST);
                if(listComment != null && !listComment.isEmpty()){
                    for (Object listComment1 : listComment) {
                        BasicDBObject cmtObj = (BasicDBObject) listComment1;
                        Integer flag = cmtObj.getInt(BuzzdbKey.SUB_COMMENT_LIST.FLAG);
                        if(flag != Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG){
                            String cmtId = cmtObj.getString(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_ID);
                            resutl.add(cmtId);
                        }
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
     }
//
    public static Set<String> getAllUserAddSubComment(String commentId) throws EazyException{
        Set<String> resutl = new HashSet<>();
        try{
            ObjectId id = new ObjectId(commentId);
            DBObject query = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.ID, id);
            DBObject obj = coll.findOne(query);
            if(obj != null){
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_LIST);
                if(listComment != null && !listComment.isEmpty()){
                    for (Object listComment1 : listComment) {
                        BasicDBObject cmtObj = (BasicDBObject) listComment1;
                        Integer flag = cmtObj.getInt(BuzzdbKey.SUB_COMMENT_LIST.FLAG);
                        if(flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            Integer approveFlag = (Integer) cmtObj.get(BuzzdbKey.SUB_COMMENT_LIST.APPROVE_FLAG);
                            if(approveFlag == null  || approveFlag == Constant.FLAG.ON){
                                String uId = cmtObj.getString(BuzzdbKey.SUB_COMMENT_LIST.USER_ID);
                                resutl.add(uId);
                            }
                        }
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
     }

    public static boolean addSubComment(String commentId, String subCommentId, String userId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(commentId);
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.ID, id);
            BasicDBObject cmtElement = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.USER_ID, userId);
            cmtElement.append(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_ID,subCommentId);
            cmtElement.append(BuzzdbKey.SUB_COMMENT_LIST.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            BasicDBObject obj = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_LIST , cmtElement);
            BasicDBObject updateCommand = new BasicDBObject("$push", obj );
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
      
    public static boolean delSubComment(String commentId, String subcommentId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(commentId);
//            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
//            BasicDBObject obj = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID , cmtId);
//            BasicDBObject cmt = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST, obj);
//            BasicDBObject updateCommand = new BasicDBObject("$pull", cmt );
//            coll.update(updateQuery, updateCommand);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.ID, id);
            BasicDBObject cmtObj = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_ID, subcommentId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", cmtObj);
            findObj.append(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_LIST + ".$." + BuzzdbKey.SUB_COMMENT_LIST.FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }            
            result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkSubCommentExist(String commentId, String subCommentId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(commentId);
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if(obj != null){
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_LIST);
                if(listComment != null){
                    for (Object listComment1 : listComment) {
                        BasicDBObject comment = (BasicDBObject) listComment1;
                        Integer flag = comment.getInt(BuzzdbKey.SUB_COMMENT_LIST.FLAG);
                        if(flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            String subComment = comment.getString(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_ID);
                            if(subComment.equals(subCommentId)){
                                result = true;
                                break;
                            }
                        }
                    }
                }
            }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateApproveFlag(String commentId, String subCommentId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(commentId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.ID, id);
            BasicDBObject buzzObj = new BasicDBObject(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_ID, subCommentId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzObj);
            findObj.append(BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.SUB_COMMENT_LIST.SUB_COMMENT_LIST + ".$." + BuzzdbKey.SUB_COMMENT_LIST.APPROVE_FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, flag);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
}
