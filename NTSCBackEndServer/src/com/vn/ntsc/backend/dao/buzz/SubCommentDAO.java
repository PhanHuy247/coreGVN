/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.buzz;

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
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.buzz.SubComment;

/**
 *
 * @author DuongLTD
 */
public class SubCommentDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.SUB_COMMENT_DETAIL_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
//    
    private static final BasicDBObject sortByTimeAsc = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.TIME, 1);
//    private static final int MAX_SUB_COMMENT = 4;

    public static List<SubComment> get4CommentByIds(List<String> listId) throws EazyException {
        List<SubComment> result = new ArrayList<>();
        try {
            List<ObjectId> lSubCommentId = new ArrayList<>();
            for (String id : listId) {
                lSubCommentId.add(new ObjectId(id));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.SUB_COMMENT_DETAIL.ID).in(lSubCommentId).get();
            DBCursor cursor = coll.find(query).sort(sortByTimeAsc);
            int count = 0;
            while (cursor.hasNext() && count <= 4) {
                SubComment comment = new SubComment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID);
                comment.userId = userId;
                ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.ID);
                comment.subCommentId = cmtId.toString();

                String cmtVal = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE);
                comment.value = cmtVal;
                Long buzzTime = (Long) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.TIME);
                Date d = new Date(buzzTime);
                comment.time = DateFormat.format(d);
                result.add(comment);
                count++;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<SubComment> getDetailByIds(List<String> listId) throws EazyException {
        List<SubComment> result = new ArrayList<>();
        try {
            List<ObjectId> lSubCommentId = new ArrayList<>();
            for (String id : listId) {
                lSubCommentId.add(new ObjectId(id));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.SUB_COMMENT_DETAIL.ID).in(lSubCommentId).get();
            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                SubComment comment = new SubComment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID);
                comment.userId = userId;
                ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.ID);
                comment.subCommentId = cmtId.toString();

                String cmtVal = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE);
                //HUNGDT add 3678
                comment.value = Util.replaceBannedWordBackend(cmtVal);
                Long buzzTime = (Long) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.TIME);
                Date d = new Date(buzzTime);
                comment.time = DateFormat.format(d);
                result.add(comment);
            }
            Collections.sort(result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SubComment get(String subCommentId) throws EazyException {
        try {
            ObjectId id = new ObjectId(subCommentId);
            DBObject query = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.ID, id);
            DBObject obj = coll.findOne(query);
            SubComment comment = new SubComment();
            String userId = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID);
            comment.userId = userId;
            ObjectId subCmtId = (ObjectId) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.ID);
            comment.subCommentId = subCmtId.toString();

            String cmtVal = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE);
            comment.value = cmtVal;

            String cmtId = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.COMMENT_ID);
            comment.commentId = cmtId;

            Integer flag = (Integer) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.FLAG);
            comment.flag = flag;

            Long buzzTime = (Long) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.TIME);
            Date d = new Date(buzzTime);
            comment.time = DateFormat.format(d);

            return comment;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static String addSubComment(String userId, String buzzId, String commentId, String value, long time) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.BUZZ_ID, buzzId);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID, userId);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.COMMENT_ID, commentId);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE, value);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.TIME, time);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            coll.insert(insertObj);
            result = insertObj.get(BuzzdbKey.SUB_COMMENT_DETAIL.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateFlag(String subCommenId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(subCommenId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.FLAG, flag);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateApprovedFlag(String subCommentId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(subCommentId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.APPROVE_FLAG, flag);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
