/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import com.vn.ntsc.buzzserver.blacklist.BlackListManager;
import com.vn.ntsc.buzzserver.dao.DAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Comment;
import com.vn.ntsc.buzzserver.entity.impl.buzz.SubComment;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author DuongLTD
 */
public class SubCommentDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DAO.getBuzzDB().getCollection(BuzzdbKey.SUB_COMMENT_DETAIL_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private static final BasicDBObject sortByTimeAsc = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.TIME, 1);
    private static final BasicDBObject sortByTimeDesc = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.TIME, -1);
    private static final int MAX_SUB_COMMENT = 4;

    public static List<SubComment> get4CommentByIds(List<String> listId, List<String> blockList) throws EazyException {
        List<SubComment> result = new ArrayList<>();
        try {
            List<ObjectId> lSubCommentId = new ArrayList<>();
            for (String id : listId) {
                lSubCommentId.add(new ObjectId(id));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.SUB_COMMENT_DETAIL.ID).in(lSubCommentId).get();
            DBCursor cursor = coll.find(query).sort(sortByTimeAsc);
            int count = 0;
            while (cursor.hasNext() && count < MAX_SUB_COMMENT) {
                SubComment comment = new SubComment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID);
                if (!blockList.contains(userId) && !BlackListManager.isDeactivateUser(userId)) {
                    comment.userId = userId;
                    ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.ID);
                    comment.subCommentId = cmtId.toString();

                    Long buzzTime = (Long) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.TIME);
                    Date d = new Date(buzzTime);
                    comment.time = DateFormat.format(d);
                    Integer isApprove = (Integer) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.APPROVE_FLAG);
                    comment.isApp = isApprove == null ? Constant.FLAG.ON : isApprove;

                    String cmtVal = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE);
                    if (comment.isApp == 1) {
                        comment.value = Util.replaceBannedWord(cmtVal);
                    } else {
                        comment.value = cmtVal;
                    }

                    result.add(comment);
                    count++;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<SubComment> getDetailByIds(List<String> listId, List<String> blockList) throws EazyException {
        List<SubComment> result = new ArrayList<>();
        try {
            List<ObjectId> lSubCommentId = new ArrayList<>();
            for (String id : listId) {
                lSubCommentId.add(new ObjectId(id));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.SUB_COMMENT_DETAIL.ID).in(lSubCommentId).get();
            DBCursor cursor = coll.find(query).sort(sortByTimeDesc);
            while (cursor.hasNext()) {
                SubComment comment = new SubComment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID);
                if (!blockList.contains(userId) && !BlackListManager.isDeactivateUser(userId)) {
                    comment.userId = userId;
                    ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.ID);
                    comment.subCommentId = cmtId.toString();

                    Long buzzTime = (Long) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.TIME);
                    Date d = new Date(buzzTime);
                    comment.time = DateFormat.format(d);
                    Integer isApprove = (Integer) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.APPROVE_FLAG);
                    comment.isApp = isApprove == null ? Constant.FLAG.ON : isApprove;
                    String cmtVal = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE);
                    if (comment.isApp == 1) {
                        comment.value = Util.replaceBannedWord(cmtVal);
                    } else {
                        comment.value = cmtVal;
                    }
                    result.add(comment);
                }
            }
//            Collections.sort(result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<SubComment> getDetailByIdsSortBy(List<String> listId, List<String> blockList,Long sortBy) throws EazyException {
        List<SubComment> result = new ArrayList<>();
        try {
            List<ObjectId> lSubCommentId = new ArrayList<>();
            for (String id : listId) {
                lSubCommentId.add(new ObjectId(id));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.SUB_COMMENT_DETAIL.ID).in(lSubCommentId).get();
            DBCursor cursor = null;
            if(sortBy != null && sortBy == -1){
                cursor = coll.find(query).sort(sortByTimeDesc);
            }else{
                cursor = coll.find(query).sort(sortByTimeAsc);
            }
            while (cursor.hasNext()) {
                SubComment comment = new SubComment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID);
                if (!blockList.contains(userId) && !BlackListManager.isDeactivateUser(userId)) {
                    comment.userId = userId;
                    ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.ID);
                    comment.subCommentId = cmtId.toString();

                    Long buzzTime = (Long) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.TIME);
                    Date d = new Date(buzzTime);
                    comment.time = DateFormat.format(d);
                    Integer isApprove = (Integer) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.APPROVE_FLAG);
                    comment.isApp = isApprove == null ? Constant.FLAG.ON : isApprove;
                    String cmtVal = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE);
                    if (comment.isApp == 1) {
                        comment.value = Util.replaceBannedWord(cmtVal);
                    } else {
                        comment.value = cmtVal;
                    }
                    result.add(comment);
                }
            }
//            Collections.sort(result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Integer getSubCommentNumber(String buzzId, List<String> blockList, List<String> lCommnet) throws EazyException {
        Integer result = 0;
        try{
            BasicDBObject userObj = new BasicDBObject("$nin", blockList);
            BasicDBObject commentObj = new BasicDBObject("$in", lCommnet);
            
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.SUB_COMMENT_DETAIL.BUZZ_ID, buzzId);
            findObj.append(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID, userObj);
            findObj.append(BuzzdbKey.SUB_COMMENT_DETAIL.COMMENT_ID, commentObj);
            findObj.append(BuzzdbKey.SUB_COMMENT_DETAIL.FLAG, Constant.FLAG.ON);
            
            Long subCommentNum = coll.count(findObj);
            result = subCommentNum.intValue();
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Map<String, Integer> getListSubCommentNum(List<Buzz> lBuzz, List<String> blockList, Map<String, List<Comment>> mComment) throws EazyException{
        Map<String, Integer> resutl = new TreeMap<>();
        try{
            for (Buzz lBuzz1 : lBuzz) {
                List<String> lCommentId = new ArrayList<>();
                List<Comment> lComment = mComment.get(lBuzz1.buzzId);
                if(lComment != null){
                    for (Comment comment : lComment) {
                        String commenter = comment.userId;
                        if (!BlackListManager.isDeactivateUser(commenter) && !blockList.contains(commenter)) {
                            lCommentId.add(comment.cmtId);
                        }
                    }
                }
                
                
                BasicDBObject obj = new BasicDBObject("$nin", blockList);
                BasicDBObject commentObj = new BasicDBObject("$in", lCommentId);
                
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(BuzzdbKey.SUB_COMMENT_DETAIL.BUZZ_ID, lBuzz1.buzzId);
                findObj.append(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID, obj);
                findObj.append(BuzzdbKey.SUB_COMMENT_DETAIL.COMMENT_ID, commentObj);
                findObj.append(BuzzdbKey.SUB_COMMENT_DETAIL.FLAG, Constant.FLAG.ON);

                Long subCommentNum = coll.count(findObj);
                
                resutl.put(lBuzz1.buzzId, subCommentNum.intValue());
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
    }

    public static List<SubComment> getDetailByCommentId(String comemntId, List<String> blockList) throws EazyException {
        List<SubComment> result = new ArrayList<>();
        try {

            DBObject query = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.COMMENT_ID, comemntId).append(BuzzdbKey.SUB_COMMENT_DETAIL.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            DBCursor cursor = coll.find(query).sort(sortByTimeAsc);
            while (cursor.hasNext()) {
                SubComment comment = new SubComment();
                DBObject obj = cursor.next();
                String uId = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID);
                if (!blockList.contains(uId) && !BlackListManager.isDeactivateUser(uId)) {
                    comment.userId = uId;
                    ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.ID);
                    comment.subCommentId = cmtId.toString();

                    Long buzzTime = (Long) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.TIME);
                    Date d = new Date(buzzTime);
                    comment.time = DateFormat.format(d);
                    Integer isApprove = (Integer) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.APPROVE_FLAG);
                    comment.isApp = isApprove == null ? Constant.FLAG.ON : isApprove;

                    String cmtVal = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE);
                    if (comment.isApp == 1) {
                        comment.value = Util.replaceBannedWord(cmtVal);
                    } else {
                        comment.value = cmtVal;
                    }
                    result.add(comment);
                }
            }
//            Collections.sort(result);
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
            ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.ID);
            comment.subCommentId = cmtId.toString();

            String buzzId = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.BUZZ_ID);
            comment.buzzId = buzzId;

            Long buzzTime = (Long) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.TIME);
            Date d = new Date(buzzTime);
            comment.time = DateFormat.format(d);

            Integer flag = (Integer) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.FLAG);
            comment.flag = flag;

            Integer isApprove = (Integer) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.APPROVE_FLAG);
            comment.isApp = isApprove == null ? Constant.FLAG.ON : isApprove;

            String cmtVal = (String) obj.get(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE);
            if (comment.isApp == 1) {
                comment.value = Util.replaceBannedWord(cmtVal);
            } else {
                comment.value = cmtVal;
            }

            return comment;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static String addSubComment(String userId, String buzzId, String commentId, String value, int approveFlag, long time) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(BuzzdbKey.SUB_COMMENT_DETAIL.BUZZ_ID, buzzId);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.USER_ID, userId);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.COMMENT_ID, commentId);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.VALUE, value);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.TIME, time);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            insertObj.put(BuzzdbKey.SUB_COMMENT_DETAIL.APPROVE_FLAG, approveFlag);
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

}
