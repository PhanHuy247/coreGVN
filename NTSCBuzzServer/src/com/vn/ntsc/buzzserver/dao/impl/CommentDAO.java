/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.Collections;
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
import com.vn.ntsc.buzzserver.entity.impl.buzz.Comment;

/**
 *
 * @author DuongLTD
 */
public class CommentDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DAO.getBuzzDB().getCollection(BuzzdbKey.COMMENT_DETAIL_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private static final BasicDBObject sortByTimeAsc = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME, 1);
    private static final BasicDBObject sortByTimeDesc = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME, -1);

    public static List<Comment> get4FirstComment(List<Comment> lComment, List<String> blockList) throws EazyException {
        List<Comment> result = new ArrayList<>();
        try {
            List<ObjectId> lCommentId = new ArrayList<>();
            for (Comment cmt : lComment) {
                String cmtId = cmt.cmtId;
                lCommentId.add(new ObjectId(cmtId));
//                String userId = cmt.userId;
//                if(!blockList.contains(userId) && !deactivateList.contains(userId)){
//                    String cmtId = cmt.cmtId;
//                    lCommentId.add(new ObjectId(cmtId));
//                }
            }
//            buzz.cmtNum = new Long(lCommentId.size());
            DBObject query = QueryBuilder.start(BuzzdbKey.COMMENT_DETAIL.ID).in(lCommentId).get();
            DBCursor cursor = coll.find(query).sort(sortByTimeAsc);
            int count = 0;
            while (cursor.hasNext() && count <= 4) {
                Comment comment = new Comment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
                if (!blockList.contains(userId) && !BlackListManager.isDeactivateUser(userId)) {
                    comment.userId = userId;
//                    if((currentUserId.equals(userId) || currentUserId.equals(buzzOwnerId)) && isDel == Constant.YES)
//                        comment.isDel = Constant.YES;
//                    else
//                        comment.isDel = Constant.NO;
                    ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.COMMENT_DETAIL.ID);
                    comment.cmtId = cmtId.toString();

                    Integer approveFlag = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.APPROVE_FLAG);
                    comment.isApp = approveFlag == null ? Constant.FLAG.ON : approveFlag;

                    Integer isDel = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.IS_DEL);
                    comment.isDel = isDel == Constant.FLAG.ON && comment.isApp == Constant.FLAG.ON ? Constant.FLAG.ON : Constant.FLAG.OFF;

                    String cmtVal = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE);
                    if (comment.isApp == 1) {
                        comment.cmtVal = Util.replaceBannedWord(cmtVal);
                    } else {
                        comment.cmtVal = cmtVal;
                    }
                    Integer subCommentNumber = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.SUB_COMMENT_NUMBER);
                    subCommentNumber = subCommentNumber == null ? 0 : subCommentNumber;
                    comment.subCommentNumber = subCommentNumber;
                    Long buzzTime = (Long) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME);
                    Date d = new Date(buzzTime);
                    comment.cmtTime = DateFormat.format(d);
                    result.add(comment);
                    count++;
                }
            }
//            Collections.sort(result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkIsDel(String commentId, String currentUserId, String buzzOwnerId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(commentId);
            DBObject query = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject obj = coll.findOne(query);
            if (obj != null) {
                String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
                Integer isDel = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.IS_DEL);
                result = (currentUserId.equals(userId) || currentUserId.equals(buzzOwnerId)) && isDel == Constant.FLAG.ON;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<Comment> getListComment(List<String> lComment, List<String> blockList) throws EazyException {
        List<Comment> result = new ArrayList<>();
        try {
            List<ObjectId> lCommentId = new ArrayList<>();
            for (String lComment1 : lComment) {
                lCommentId.add(new ObjectId(lComment1));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.COMMENT_DETAIL.ID).in(lCommentId).get();
            DBCursor cursor = coll.find(query).sort(sortByTimeAsc);
            while (cursor.hasNext()) {
                Comment comment = new Comment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
                if (!blockList.contains(userId) && !BlackListManager.isDeactivateUser(userId)) {
                    comment.userId = userId;
//                    if((currentUserId.equals(userId) || currentUserId.equals(buzzOwnerId)) && isDel == Constant.YES)
//                        comment.isDel = Constant.YES;
//                    else
//                        comment.isDel = Constant.NO;
                    ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.COMMENT_DETAIL.ID);
                    comment.cmtId = cmtId.toString();

                    Integer subCommentNumber = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.SUB_COMMENT_NUMBER);
                    subCommentNumber = subCommentNumber == null ? 0 : subCommentNumber;
                    comment.subCommentNumber = subCommentNumber;

                    Integer approveFlag = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.APPROVE_FLAG);
                    comment.isApp = approveFlag == null ? Constant.FLAG.ON : approveFlag;

                    Integer isDel = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.IS_DEL);
                    comment.isDel = isDel == Constant.FLAG.ON && comment.isApp == Constant.FLAG.ON ? Constant.FLAG.ON : Constant.FLAG.OFF;

                    String cmtVal = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE);
                    if (comment.isApp == 1) {
                        comment.cmtVal = Util.replaceBannedWord(cmtVal);
                    } else {
                        comment.cmtVal = cmtVal;
                    }
                    Long buzzTime = (Long) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME);
                    Date d = new Date(buzzTime);
                    comment.cmtTime = DateFormat.format(d);
                    result.add(comment);
                }
            }
            Collections.sort(result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Integer getNumberSubComment(List<String> lComment, List<String> blockList){
        Integer result = 0;
        List<ObjectId> lCommentId = new ArrayList<>();
        for (String lComment1 : lComment) {
            lCommentId.add(new ObjectId(lComment1));
        }
        DBObject query = QueryBuilder.start(BuzzdbKey.COMMENT_DETAIL.ID).in(lCommentId).get();
        DBCursor cursor = coll.find(query);
        while (cursor.hasNext()) {
            Comment comment = new Comment();
            DBObject obj = cursor.next();
            String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
            if (!blockList.contains(userId) && !BlackListManager.isDeactivateUser(userId)) {
                Integer subCommentNumber = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.SUB_COMMENT_NUMBER);
                subCommentNumber = subCommentNumber == null ? 0 : subCommentNumber;
                result += subCommentNumber;
            }
        }
        return result;
    }
    
    public static List<Comment> getListComment(List<String> lComment, List<String> blockList, Long sortBy) throws EazyException {
        List<Comment> result = new ArrayList<>();
        try {
            List<ObjectId> lCommentId = new ArrayList<>();
            for (String lComment1 : lComment) {
                lCommentId.add(new ObjectId(lComment1));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.COMMENT_DETAIL.ID).in(lCommentId).get();
            DBCursor cursor = null;
            if(sortBy != null && sortBy == -1){
                cursor = coll.find(query).sort(sortByTimeDesc);
            }else{
                cursor = coll.find(query).sort(sortByTimeAsc);
            }
            while (cursor.hasNext()) {
                Comment comment = new Comment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
                if (!blockList.contains(userId) && !BlackListManager.isDeactivateUser(userId)) {
                    comment.userId = userId;
//                    if((currentUserId.equals(userId) || currentUserId.equals(buzzOwnerId)) && isDel == Constant.YES)
//                        comment.isDel = Constant.YES;
//                    else
//                        comment.isDel = Constant.NO;
                    ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.COMMENT_DETAIL.ID);
                    comment.cmtId = cmtId.toString();

                    Integer subCommentNumber = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.SUB_COMMENT_NUMBER);
                    subCommentNumber = subCommentNumber == null ? 0 : subCommentNumber;
                    comment.subCommentNumber = subCommentNumber;

                    Integer approveFlag = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.APPROVE_FLAG);
                    comment.isApp = approveFlag == null ? Constant.FLAG.ON : approveFlag;

                    Integer isDel = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.IS_DEL);
                    comment.isDel = isDel == Constant.FLAG.ON && comment.isApp == Constant.FLAG.ON ? Constant.FLAG.ON : Constant.FLAG.OFF;

                    String cmtVal = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE);
                    if (comment.isApp == 1) {
                        comment.cmtVal = Util.replaceBannedWord(cmtVal);
                    } else {
                        comment.cmtVal = cmtVal;
                    }
                    Long buzzTime = (Long) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME);
                    Date d = new Date(buzzTime);
                    comment.cmtTime = DateFormat.format(d);
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
    
    public static Integer getNumberComment(List<String> lComment, List<String> blockList) throws EazyException {
        Integer result = 0;
        try {
            List<ObjectId> lCommentId = new ArrayList<>();
            for (String lComment1 : lComment) {
                lCommentId.add(new ObjectId(lComment1));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.COMMENT_DETAIL.ID).in(lCommentId).get();
            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                Comment comment = new Comment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
                if (!blockList.contains(userId) && !BlackListManager.isDeactivateUser(userId)) {
                    result ++;
                }
            }
//            Collections.sort(result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Comment get(String commentId) throws EazyException {
        try {
            ObjectId id = new ObjectId(commentId);
            DBObject query = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject obj = coll.findOne(query);
            Comment comment = new Comment();
            String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
            comment.userId = userId;
            ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.COMMENT_DETAIL.ID);
            comment.cmtId = cmtId.toString();

            Integer subCommentNumber = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.SUB_COMMENT_NUMBER);
            subCommentNumber = subCommentNumber == null ? 0 : subCommentNumber;
            comment.subCommentNumber = subCommentNumber;

            Long buzzTime = (Long) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME);
            Date d = new Date(buzzTime);
            comment.cmtTime = DateFormat.format(d);

            Integer flag = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_FLAG);
            comment.flag = flag;

            Integer approveFlag = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.APPROVE_FLAG);
            comment.isApp = approveFlag == null ? Constant.FLAG.ON : approveFlag;

            String cmtVal = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE);
            if (comment.isApp == 1) {
                comment.cmtVal = Util.replaceBannedWord(cmtVal);
            } else {
                comment.cmtVal = cmtVal;
            }

            Integer isDel = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.IS_DEL);
            comment.isDel = isDel;

            return comment;

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static String addComment(String userId, String buzzId, String commentVal, int approveFlag, int isDel, long time) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.BUZZ_ID, buzzId);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.USER_ID, userId);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE, commentVal);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME, time);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.IS_DEL, isDel);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            insertObj.put(BuzzdbKey.COMMENT_DETAIL.APPROVE_FLAG, approveFlag);
            coll.insert(insertObj);
            ObjectId id = (ObjectId) insertObj.get(BuzzdbKey.BUZZ_DETAIL.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getUserId(String commentId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(commentId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
            String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
            result = userId;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateFlag(String cmtId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(cmtId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.COMMENT_FLAG, flag);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean addSubComment(String commentId) throws EazyException {
        boolean result = false;
        try {
            result = increaseFieldList(commentId, BuzzdbKey.COMMENT_DETAIL.SUB_COMMENT_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean removeSubComment(String buzzId) throws EazyException {
        boolean result = false;
        try {
            result = decreaseFieldList(buzzId, BuzzdbKey.COMMENT_DETAIL.SUB_COMMENT_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean increaseFieldList(String commentId, String fieldname, int num) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(commentId);
            //search by id
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(fieldname, num);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean decreaseFieldList(String commentId, String fieldname, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(commentId);
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            //update command
            DBObject obD = coll.findOne(updateQuery);
            Integer point = 0;
            if (obD != null) {
                point = (Integer) obD.get(fieldname);
                point = point != null ? point : 0;
            }
            num = num > point ? point : num;
            BasicDBObject obj = new BasicDBObject(fieldname, (0 - num));
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
