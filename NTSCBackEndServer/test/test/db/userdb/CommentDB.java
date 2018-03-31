/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.db.userdb;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import test.db.DBTest;
import com.vn.ntsc.backend.entity.impl.buzz.Comment;

/**
 *
 * @author DuongLTD
 */
public class CommentDB {

    private static DBCollection coll;
    private static DB db;
    static{
         try{
             db = DBTest.mongo.getDB("buzzdb");
            coll = db.getCollection( BuzzdbKey.COMMENT_DETAIL_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean checkIsDel(String commentId, String currentUserId, String buzzOwnerId) throws EazyException{
        boolean result = false;
        try{
            ObjectId id = new ObjectId(commentId);
            DBObject query = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject obj = coll.findOne(query);
            if(obj != null){
                String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
                Integer isDel = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.IS_DEL);
                result = (currentUserId.equals(userId) || currentUserId.equals(buzzOwnerId)) && isDel == Constant.FLAG.ON;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }    
    
    public static List<Comment> getListComment(List<String> lComment, String currentUserId, String buzzOwnerId,List<String> blackList) throws EazyException{
        List<Comment> result = new ArrayList<Comment>();
        try{
            List<ObjectId> lCommentId = new ArrayList<>();
            for(int i = 0; i < lComment.size(); i++){
                lCommentId.add(new ObjectId(lComment.get(i)));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.COMMENT_DETAIL.ID).in(lCommentId).get();
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                Comment comment = new Comment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
                if(!blackList.contains(userId)){
                    comment.userId = userId;
                    Integer isDel = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.IS_DEL);
                    ObjectId cmtId = (ObjectId)obj.get(BuzzdbKey.COMMENT_DETAIL.ID);
                    comment.cmtId = cmtId.toString();

                    String cmtVal = (String)obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE);
                    comment.cmtVal = cmtVal;
                    Long buzzTime = (Long) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME);
                    Date d = new Date(buzzTime);
                    comment.cmtTime = DateFormat.format(d);
                    result.add(comment);
                }
                Collections.sort(result);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

    public static String addComment(String userId, String buzzId, String commentVal, int isDel, long time) throws EazyException{
         String result = null;
        try{
            DBObject insertObj = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.BUZZ_ID, buzzId);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.USER_ID, userId);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE, commentVal);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME, time);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.IS_DEL, isDel);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            coll.insert(insertObj);
            ObjectId id = (ObjectId) insertObj.get(BuzzdbKey.BUZZ_DETAIL.ID);
            result  = id.toString();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

    public static String getUserId(String commentId) throws EazyException{
         String result = null;
        try{
            ObjectId id = new ObjectId(commentId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject obj =  coll.findOne(findObj);
            if(obj == null)
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
            result  = userId;
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean updateFlag(String cmtId, int flag ) throws EazyException{
        boolean result = false;
        try{
            ObjectId  id = new ObjectId(cmtId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.COMMENT_FLAG, flag);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result  = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

}

