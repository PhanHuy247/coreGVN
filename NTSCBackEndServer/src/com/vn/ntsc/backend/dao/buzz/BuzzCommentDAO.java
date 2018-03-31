/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.buzz;

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
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.buzz.Buzz;
import com.vn.ntsc.backend.entity.impl.buzz.Comment;

/**
 *
 * @author DuongLTD
 */
public class BuzzCommentDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.BUZZ_COMMENT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static Map<String, List<Comment>> getListComment(List<Buzz> lBuzz) throws EazyException {
        Map<String, List<Comment>> resutl = new TreeMap<>();
        try {
            List<ObjectId> listBuzzId = new ArrayList<>();
            for (Buzz lBuzz1 : lBuzz) {
                listBuzzId.add(new ObjectId(lBuzz1.buzzId));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.BUZZ_COMMENT.ID).in(listBuzzId).get();
            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                ObjectId id = (ObjectId) obj.get(BuzzdbKey.BUZZ_COMMENT.ID);
                BasicDBList listComment = (BasicDBList) obj.get(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST);
                if (listComment != null && !listComment.isEmpty()) {
                    List<Comment> list = new ArrayList<>();
                    for (Object listComment1 : listComment) {
                        Comment cmt = new Comment();
                        BasicDBObject cmtObj = (BasicDBObject) listComment1;
                        Integer flag = cmtObj.getInt(BuzzdbKey.BUZZ_COMMENT.FLAG);
                        if (flag != Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG) {
                            String cmtId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID);
                            String userId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.USER_ID);
                            cmt.cmtId = cmtId;
                            cmt.userId = userId;
                            list.add(cmt);
                        }
                    }
                    resutl.put(id.toString(), list);
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
    }

    public static List<String> getListComment(String buzzId) throws EazyException {
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
                        Integer flag = cmtObj.getInt(BuzzdbKey.BUZZ_COMMENT.FLAG);
                        if (flag == null || flag != Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG) {
                            String cmtId = cmtObj.getString(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID);
                            resutl.add(cmtId);
                        }
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
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

    public static boolean delComment(String buzzId, String cmtId) throws EazyException {
        boolean result = false;
        try {
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
                BasicDBObject updateObj = new BasicDBObject(field, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG);
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
    
    public static boolean updateApproveFlag(String buzzId, String commentId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
//            DBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
//            BasicDBObject buzzElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
//            BasicDBObject buzzer = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, buzzElement);
//            BasicDBObject updateCommand = new BasicDBObject("$pull", buzzer );
//            coll.update(findObj, updateCommand);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.ID, id);
            BasicDBObject buzzObj = new BasicDBObject(BuzzdbKey.BUZZ_COMMENT.COMMENT_ID, commentId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzObj);
            findObj.append(BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.BUZZ_COMMENT.COMMENT_LIST + ".$." + BuzzdbKey.BUZZ_COMMENT.APPROVE_FLAG;
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
