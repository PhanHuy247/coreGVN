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
import com.vn.ntsc.backend.entity.impl.buzz.ReviewingComment;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;

/**
 *
 * @author DuongLTD
 */
public class ReviewingCommentDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.REVIEWING_COMMENT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static SizedListData getReviewingComment(String userId, String buzzId, Long sort, Long order, int skip, int take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBObject findObject = new BasicDBObject();
            if (buzzId != null) {
                findObject.put(BuzzdbKey.REVIEWING_COMMENT.BUZZ_ID, buzzId);
            }
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
                        BasicDBObject findObjectUserId = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.USER_ID, regex);
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
                sortObj.append(BuzzdbKey.REVIEWING_COMMENT.ACTION_TIME, or);
            }
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            int number = 0;
            int count = 0;
            //set to list
//            Integer number = cursor.size();
//            cursor = cursor.skip(skip).limit(take);
            int limit = skip + take;
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
//                Integer reviewFlag = (Integer)dbObject.get(BuzzdbKey.REVIEWING_COMMENT.REVIEW_FLAG);
//                reviewFlag = reviewFlag == null ? Constant.NO : reviewFlag;
                Integer appearFlag = (Integer) dbObject.get(BuzzdbKey.REVIEWING_COMMENT.APPEAR_FLAG);
                appearFlag = appearFlag == null ? Constant.FLAG.ON : appearFlag;
//                Integer reviewingSubCommentNumber = (Integer)dbObject.get(BuzzdbKey.REVIEWING_COMMENT.REVIEWING_SUB_COMMENT_NUMBER);
//                reviewingSubCommentNumber = reviewingSubCommentNumber == null ? 0 : reviewingSubCommentNumber;
                if (appearFlag == Constant.FLAG.ON) {
                    number++;
                    count++;
                    if (count > skip && count <= limit) {
                        ReviewingComment reviewingBuzz = new ReviewingComment();
//                        reviewingBuzz.reviewFlag = reviewFlag;
                        String bID = (String) dbObject.get(BuzzdbKey.REVIEWING_COMMENT.BUZZ_ID);
                        reviewingBuzz.buzzId = bID;
//                        reviewingBuzz.reviewingSubCommentNumber = reviewingSubCommentNumber;
                        String commentId = dbObject.get(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID).toString();
                        reviewingBuzz.commentId = commentId;
                        String uId = (String) dbObject.get(BuzzdbKey.REVIEWING_COMMENT.USER_ID);
                        reviewingBuzz.userId = uId;
                        String bVal = (String) dbObject.get(BuzzdbKey.REVIEWING_COMMENT.VALUE);
                        //HUNGDT add 3678
                        reviewingBuzz.commentValue = Util.replaceBannedWordBackend(bVal);
                        Long uploadTime = (Long) dbObject.get(BuzzdbKey.REVIEWING_COMMENT.TIME);
                        if (uploadTime != null) {
                            reviewingBuzz.commentTime = DateFormat.format(uploadTime);
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

    public static void remove(String commentId) throws EazyException {
        try {
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID, commentId);
            coll.remove(obj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void removeByBuzzId(String buzzId) throws EazyException {
        try {
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.BUZZ_ID, buzzId);
            coll.remove(obj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void reviewSubComment(String commentId) throws EazyException {
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID, commentId);
            //update command
            BasicDBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.REVIEWING_SUB_COMMENT_NUMBER, -1);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(findObj, updateCommand);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void updateReveiwFlag(String commentId, Integer flag) throws EazyException {
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID, commentId);
            //update command
            BasicDBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.REVIEW_FLAG, flag);
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.update(findObj, updateCommand);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void updateAppearFlagByBuzzId(String buzzId, Integer flag) throws EazyException {
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.BUZZ_ID, buzzId);
            //update command
            BasicDBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.APPEAR_FLAG, flag);
            obj.append(BuzzdbKey.REVIEWING_COMMENT.ACTION_TIME, Util.currentTime());
            BasicDBObject updateCommand = new BasicDBObject("$set", obj);
            coll.updateMulti(findObj, updateCommand);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static ReviewingComment get(String id) throws EazyException {
        ReviewingComment result = null;
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                result = new ReviewingComment();
                String bID = obj.get(BuzzdbKey.REVIEWING_COMMENT.BUZZ_ID).toString();
                result.buzzId = bID;
                String commentId = obj.get(BuzzdbKey.REVIEWING_COMMENT.COMMENT_ID).toString();
                result.commentId = commentId;
                String uId = (String) obj.get(BuzzdbKey.REVIEWING_COMMENT.USER_ID);
                result.userId = uId;
                String bVal = (String) obj.get(BuzzdbKey.REVIEWING_COMMENT.VALUE);
                result.commentValue = bVal;
//                Integer reviewFlag = (Integer)obj.get(BuzzdbKey.REVIEWING_COMMENT.REVIEW_FLAG);
//                result.reviewFlag = reviewFlag;
                Long uploadTime = (Long) obj.get(BuzzdbKey.REVIEWING_COMMENT.TIME);
                if (uploadTime != null) {
                    result.commentTime = DateFormat.format(uploadTime);
                }
//                Integer reviewingSubCommentNumber = (Integer)obj.get(BuzzdbKey.REVIEWING_COMMENT.REVIEWING_SUB_COMMENT_NUMBER);
//                result.reviewingSubCommentNumber = reviewingSubCommentNumber;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
