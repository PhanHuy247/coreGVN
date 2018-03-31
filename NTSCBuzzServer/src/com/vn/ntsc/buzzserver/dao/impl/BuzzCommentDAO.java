/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.buzzserver.dao.DAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Comment;

/**
 *
 * @author DuongLTD
 */
public class BuzzCommentDAO {

    private static DBCollection coll;
    static{
         try{
            coll = DAO.getBuzzDB().getCollection( BuzzdbKey.BUZZ_COMMENT_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    
    public static List<Comment> getListShareComment(String buzzId, String requestUserId){
        List<Comment> result = new ArrayList<>();
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_COMMENT.ID, new ObjectId(buzzId));
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                ObjectId id = (ObjectId) obj.get(BuzzdbKey.BUZZ_COMMENT.ID);
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST);
                if(listComment != null && !listComment.isEmpty()){
                    for (Object listComment1 : listComment) {
                        BasicDBObject cmtObj = (BasicDBObject) listComment1;
                        Integer flag = cmtObj.getInt(BuzzdbKey.BUZZ_COMMENT.FLAG);
                        if(flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            Integer approveFlag = (Integer) cmtObj.get(BuzzdbKey.BUZZ_COMMENT.APPROVE_FLAG);
                            String userId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.USER_ID);
                            if(approveFlag == null || approveFlag == Constant.FLAG.ON || (approveFlag == Constant.FLAG.OFF && userId.equals(requestUserId))){
                                Comment cmt = new Comment();
                                String cmtId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID);
                                cmt.cmtId = cmtId;
                                cmt.userId = userId;
                                result.add(cmt);
                            }
                        }
                    }
                    
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static Map<String, List<Comment>> getListComment(List<Buzz> lBuzz, String requestUserId) throws EazyException{
        Map<String, List<Comment>> resutl = new TreeMap<>();
        try{
            List<ObjectId> listBuzzId = new ArrayList<>();
            for (Buzz lBuzz1 : lBuzz) {
                listBuzzId.add(new ObjectId(lBuzz1.buzzId));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.BUZZ_COMMENT.ID).in(listBuzzId).get();
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                ObjectId id = (ObjectId) obj.get(BuzzdbKey.BUZZ_COMMENT.ID);
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST);
                if(listComment != null && !listComment.isEmpty()){
                    List<Comment> list = new ArrayList<>();
                    for (Object listComment1 : listComment) {
                        BasicDBObject cmtObj = (BasicDBObject) listComment1;
                        Integer flag = cmtObj.getInt(BuzzdbKey.BUZZ_COMMENT.FLAG);
                        if(flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            Integer approveFlag = (Integer) cmtObj.get(BuzzdbKey.BUZZ_COMMENT.APPROVE_FLAG);
                            String userId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.USER_ID);
                            if(approveFlag == null || approveFlag == Constant.FLAG.ON || (approveFlag == Constant.FLAG.OFF && userId.equals(requestUserId))){
                                Comment cmt = new Comment();
                                String cmtId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID);
                                cmt.cmtId = cmtId;
                                cmt.userId = userId;
                                list.add(cmt);
                            }
                        }
                    }
                    resutl.put(id.toString(), list);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
    }

    public static List<String> getListComment(String buzzId, String requestUserId) throws EazyException{
        List<String> resutl = new ArrayList<>();
        try{
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
            DBObject obj = coll.findOne(query);
            if(obj != null){
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST);
                if(listComment != null && !listComment.isEmpty()){
                    for (Object listComment1 : listComment) {
                        BasicDBObject cmtObj = (BasicDBObject) listComment1;
                        Integer flag = (Integer) cmtObj.get(BuzzdbKey.BUZZ_COMMENT.FLAG);
                        if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            Integer approveFlag = (Integer) cmtObj.get(BuzzdbKey.BUZZ_COMMENT.APPROVE_FLAG);
                            String userId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.USER_ID);
                            if(approveFlag == null || approveFlag == Constant.FLAG.ON || (approveFlag == Constant.FLAG.OFF && userId.equals(requestUserId))){
                                String cmtId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID);
                                resutl.add(cmtId);
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
    
    //get List User cmt Buzz
    public static List<String> getUserCommentBuzz(String buzzId) throws EazyException{
        List<String> resutl = new ArrayList<>();
        try{
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
            DBObject obj = coll.findOne(query);
            if(obj != null){
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST);
                if(listComment != null && !listComment.isEmpty()){
                    for (Object listComment1 : listComment) {
                        BasicDBObject cmtObj = (BasicDBObject) listComment1;
                        Integer flag = (Integer) cmtObj.get(BuzzdbKey.BUZZ_COMMENT.FLAG);
                        if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            Integer approveFlag = (Integer) cmtObj.get(BuzzdbKey.BUZZ_COMMENT.APPROVE_FLAG);
                            String userId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.USER_ID);
                            if(approveFlag == null || approveFlag == Constant.FLAG.ON && !resutl.contains(userId)){
                                resutl.add(userId);
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
    
    public static Integer getAllNumberComment(String buzzId, String requestUserId){
        Integer result = 0;
        try{
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
            DBObject obj = coll.findOne(query);
            if(obj != null){
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST);
                if(listComment != null && !listComment.isEmpty()){
                    for (Object listComment1 : listComment) {
                        BasicDBObject cmtObj = (BasicDBObject) listComment1;
                        Integer flag = (Integer) cmtObj.get(BuzzdbKey.BUZZ_COMMENT.FLAG);
                        if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            Integer approveFlag = (Integer) cmtObj.get(BuzzdbKey.BUZZ_COMMENT.APPROVE_FLAG);
                            String userId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.USER_ID);
                            if(approveFlag == null || approveFlag == Constant.FLAG.ON || (approveFlag == Constant.FLAG.OFF && userId.equals(requestUserId))){
                                result ++;
                            }
                        }
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return result;
    }
    public static boolean addComment(String buzzId, String commentId, String userId, int approveFlag) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
            BasicDBObject cmtElement = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.USER_ID, userId);
            cmtElement.append(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID,commentId);
            cmtElement.append(BuzzdbKey.BUZZ_COMMENT.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            cmtElement.append(BuzzdbKey.BUZZ_COMMENT.APPROVE_FLAG, approveFlag);
            BasicDBObject obj = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST , cmtElement);
            BasicDBObject updateCommand = new BasicDBObject("$push", obj );
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
      
    public static boolean delComment(String buzzId, String cmtId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(buzzId);
//            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
//            BasicDBObject obj = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID , cmtId);
//            BasicDBObject cmt = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST, obj);
//            BasicDBObject updateCommand = new BasicDBObject("$pull", cmt );
//            coll.update(updateQuery, updateCommand);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
            BasicDBObject cmtObj = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID, cmtId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", cmtObj);
            findObj.append(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST + ".$." + BuzzdbKey.BUZZ_COMMENT.FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
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

    public static boolean checkCommentExist(String buzzId, String cmtId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if(obj != null){
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST);
                if(listComment != null){
                    for (Object listComment1 : listComment) {
                        BasicDBObject comment = (BasicDBObject) listComment1;
                        Integer flag = (Integer) comment.get(BuzzdbKey.BUZZ_COMMENT.FLAG);
                        if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            String commentId = comment.getString(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID);
                            if(cmtId.equals(commentId)){
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

    public static List<String> getAllComment(String buzzId) throws EazyException {
        List<String> resutl = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
            DBObject obj = coll.findOne(query);
            if (obj != null) {
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST);
                if (listComment != null && !listComment.isEmpty()) {
                    for (int i = 0; i < listComment.size(); i++) {
                        BasicDBObject cmtObj = (BasicDBObject) listComment.get(i);
                        String cmtId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID);
                        resutl.add(cmtId);
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
    }
}
