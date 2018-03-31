/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.buzz;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.buzz.ReviewingSubComment;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;

/**
 *
 * @author DuongLTD
 */
public class ReviewingSubCommentDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.REVIEWING_SUB_COMMENT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static SizedListData getReviewingSubComment(String userId, Long sort, Long order, int skip, int take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
//            DBObject findObject = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID,commentId);
//            BasicDBObject sortObj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME, 1);
//            DBCursor cursor = coll.find(findObject).sort(sortObj);
//            DBObject findObject = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID,commentId);
//            BasicDBObject sortObj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME, 1);
            DBObject findObject = new BasicDBObject();
            if (userId != null && !userId.isEmpty()) {
                ArrayList<BasicDBObject> listFindObject = new ArrayList<>();
                String listUserId[] = userId.split("[,、，､]");
                for (String i : listUserId) {
                    if (i != null && !i.trim().isEmpty()) {
                        i = i.trim();
                        for (String str : Constant.SPECIAL_CHARACTER) {
                            if (i.contains(str)) {
                                String string = "\\" + str;
                                i = i.replace(str, string);
                            }
                        }
                        BasicDBObject regex = new BasicDBObject("$regex", i.toLowerCase());
                        BasicDBObject findObjectUserId = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.USER_ID, regex);
                        listFindObject.add(findObjectUserId);
                    }
                }
                findObject.put("$or", listFindObject);
            }
            if (order == null) {
                return null;
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            if (sort == null) {
                return null;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME, or);
            }

            DBCursor cursor = coll.find(findObject).sort(sortObj);
//            Integer number = cursor.size();
//            cursor = cursor.skip(skip).limit(take);
            int number = 0;
            int count = 0;
            //set to list
//            Integer number = cursor.size();
//            cursor = cursor.skip(skip).limit(take);
            int limit = skip + take;
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Integer appearFlag = (Integer) dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.APPEAR_FLAG);
                appearFlag = appearFlag == null ? Constant.FLAG.ON : appearFlag;
                if (appearFlag == Constant.FLAG.ON) {
                    number++;
                    count++;
                    if (count > skip && count <= limit) {
                        ReviewingSubComment reviewingBuzz = new ReviewingSubComment();
                        String bID = dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.BUZZ_ID).toString();
                        reviewingBuzz.buzzId = bID;
                        String commentId = dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID).toString();
                        reviewingBuzz.commentId = commentId;
                        String uId = (String) dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.USER_ID);
                        reviewingBuzz.userId = uId;
                        String subCommentId = (String) dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.SUB_COMMENT_ID);
                        reviewingBuzz.subCommentId = subCommentId;
                        String bVal = (String) dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.VALUE);
                        //HUNGDT add 3678
                        reviewingBuzz.value = Util.replaceBannedWordBackend(bVal);
                        Long uploadTime = (Long) dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME);
                        if (uploadTime != null) {
                            reviewingBuzz.time = DateFormat.format(uploadTime);
                        }
                        list.add(reviewingBuzz);
                    }
                }
            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void updateAppearFlagByBuzzId(String buzzId, Integer flag) throws EazyException {
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.BUZZ_ID, buzzId);
            //update command
            BasicDBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.APPEAR_FLAG, flag);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.updateMulti(findObj, updateCommand);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static List<ReviewingSubComment> geListtReviewingSubComment(String userId, Long sort, Long order, int skip, int take) throws EazyException {
        List<ReviewingSubComment> result = new ArrayList<>();
        try {
//            DBObject findObject = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID,commentId);
//            BasicDBObject sortObj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME, 1);
//            DBCursor cursor = coll.find(findObject).sort(sortObj);
//            DBObject findObject = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID,commentId);
//            BasicDBObject sortObj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME, 1);
            DBObject findObject = new BasicDBObject();
            if (userId != null && !userId.isEmpty()) {
                ArrayList<BasicDBObject> listFindObject = new ArrayList<>();
                String listUserId[] = userId.split("[,、，､]");
                for (String i : listUserId) {
                    if (i != null && !i.trim().isEmpty()) {
                        i = i.trim();
                        for (String str : Constant.SPECIAL_CHARACTER) {
                            if (i.contains(str)) {
                                String string = "\\" + str;
                                i = i.replace(str, string);
                            }
                        }
                        BasicDBObject regex = new BasicDBObject("$regex", i.toLowerCase());
                        BasicDBObject findObjectUserId = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.USER_ID, regex);
                        listFindObject.add(findObjectUserId);
                    }
                }
                findObject.put("$or", listFindObject);
            }
            if (order == null) {
                return null;
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            if (sort == null) {
                return null;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME, or);
            }

            DBCursor cursor = coll.find().sort(sortObj);
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                ReviewingSubComment reviewingBuzz = new ReviewingSubComment();
                String bID = dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.BUZZ_ID).toString();
                reviewingBuzz.buzzId = bID;
                String commentId = dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID).toString();
                reviewingBuzz.commentId = commentId;
                String uId = (String) dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.USER_ID);
                reviewingBuzz.userId = uId;
                String subCommentId = (String) dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.SUB_COMMENT_ID);
                reviewingBuzz.subCommentId = subCommentId;
                String bVal = (String) dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.VALUE);
                reviewingBuzz.value = bVal;
                Long uploadTime = (Long) dbObject.get(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME);
                if (uploadTime != null) {
                    reviewingBuzz.time = DateFormat.format(uploadTime);
                }
                result.add(reviewingBuzz);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void remove(String subCommentId) throws EazyException {
        try {
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.SUB_COMMENT_ID, subCommentId);
            coll.remove(obj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void removeByCommentId(String commentId) throws EazyException {
        try {
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID, commentId);
            coll.remove(obj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void removeByBuzzId(String buzzId) throws EazyException {
        try {
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.BUZZ_ID, buzzId);
            coll.remove(obj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static ReviewingSubComment get(String id) throws EazyException {
        ReviewingSubComment result = null;
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.REVIEWING_SUB_COMMENT.SUB_COMMENT_ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                result = new ReviewingSubComment();
                String subCommentId = obj.get(BuzzdbKey.REVIEWING_SUB_COMMENT.SUB_COMMENT_ID).toString();
                result.subCommentId = subCommentId;
                String bID = obj.get(BuzzdbKey.REVIEWING_SUB_COMMENT.BUZZ_ID).toString();
                result.buzzId = bID;
                String commentId = obj.get(BuzzdbKey.REVIEWING_SUB_COMMENT.COMMENT_ID).toString();
                result.commentId = commentId;
                String uId = (String) obj.get(BuzzdbKey.REVIEWING_SUB_COMMENT.USER_ID);
                result.userId = uId;
                String bVal = (String) obj.get(BuzzdbKey.REVIEWING_SUB_COMMENT.VALUE);
                result.value = bVal;
                Long uploadTime = (Long) obj.get(BuzzdbKey.REVIEWING_SUB_COMMENT.TIME);
                if (uploadTime != null) {
                    result.time = DateFormat.format(uploadTime);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
