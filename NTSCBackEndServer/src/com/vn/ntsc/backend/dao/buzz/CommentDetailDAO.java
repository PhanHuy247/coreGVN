/*
 * To change this template, choose Tools | Templates
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
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.buzz.Comment;
import com.vn.ntsc.backend.entity.impl.buzz.CommentDetail;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;

/**
 *
 * @author DuongLTD
 */
public class CommentDetailDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.COMMENT_DETAIL_COLLECTION);
            coll.createIndex(new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.USER_ID, 1));
            coll.createIndex(new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.BUZZ_ID, 1));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
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

    public static List<Comment> get2FirstComment(List<Comment> lComment) throws EazyException {
        List<Comment> result = new ArrayList<>();
        try {
            List<ObjectId> lCommentId = new ArrayList<>();
            for (int i = 0; i < lComment.size(); i++) {
                Comment cmt = lComment.get(i);
                lCommentId.add(new ObjectId(cmt.cmtId));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.COMMENT_DETAIL.ID).in(lCommentId).get();
            DBCursor cursor = coll.find(query);
            int count = 0;
            while (cursor.hasNext() && count < 2) {
                Comment comment = new Comment();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
                comment.userId = userId;
                ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.COMMENT_DETAIL.ID);
                comment.cmtId = cmtId.toString();

                String cmtVal = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE);
                comment.cmtVal = cmtVal;
                Long buzzTime = (Long) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME);
                Date d = new Date(buzzTime);
                comment.cmtTime = DateFormat.format(d);
                Integer subcommentNumber = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.SUB_COMMENT_NUMBER);
                comment.subCommentNumber = subcommentNumber;

                result.add(comment);
                count++;
            }
            Collections.sort(result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<Comment> getListComment(List<String> lComment) throws EazyException {
        List<Comment> result = new ArrayList<>();
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
                comment.userId = userId;
                ObjectId cmtId = (ObjectId) obj.get(BuzzdbKey.COMMENT_DETAIL.ID);
                comment.cmtId = cmtId.toString();

                String cmtVal = (String) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE);
                comment.cmtVal = cmtVal;
                Long buzzTime = (Long) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME);
                Date d = new Date(buzzTime);
                comment.cmtTime = DateFormat.format(d);
                Integer subcommentNumber = (Integer) obj.get(BuzzdbKey.COMMENT_DETAIL.SUB_COMMENT_NUMBER);
                comment.subCommentNumber = subcommentNumber;
                result.add(comment);
            }
            Collections.sort(result);
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

    public static int getFlag(String cmtId) throws EazyException {
        int result = Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG;
        try {
            ObjectId id = new ObjectId(cmtId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                result = (int) obj.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_FLAG);
            }
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
                if (point == null) {
                    point = 0;
                }
            }
            if (num > point) {
                num = point;
            }
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

    public static boolean updateApprovedFlag(String commentId, int flag, String userDeny) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(commentId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.APPROVE_FLAG, flag);
            if (userDeny != null) {
                updateObject.put(BuzzdbKey.COMMENT_DETAIL.USER_DENY, userDeny);
            }
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    // Namhv #8001 19/04/2017
    public static SizedListData getCommentDetail(String userId, String buzzId, Long sort, Long order, int skip, int take, String fromTime, String toTime) {
        SizedListData result = new SizedListData();
        try {
            DBObject findObject = new BasicDBObject();
            if (buzzId != null) {
                findObject.put(BuzzdbKey.COMMENT_DETAIL.BUZZ_ID, buzzId);
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
                        BasicDBObject findObjectUserId = new BasicDBObject(BuzzdbKey.COMMENT_DETAIL.USER_ID, regex);
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

//            if (fromTime != null && toTime != null) {
//                long fromTimeCmt = DateFormat.parse(fromTime).getTime();
//                long toTimeCmt = Long.parseLong(toTime);
//                findObject.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME, new BasicDBObject("$gte",fromTimeCmt));
//            }
            if (fromTime != null && toTime != null) {
                long fromTimeCmt = DateFormat.parse(fromTime).getTime();
                long toTimeCmt = DateFormat.parse(toTime).getTime();
                findObject.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME, new BasicDBObject("$gte", fromTimeCmt).
                        append("$lte", toTimeCmt));
            } else if (fromTime != null && toTime == null) {
                long fromTimeCmt = DateFormat.parse(fromTime).getTime();
                findObject.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME, new BasicDBObject("$gte", fromTimeCmt));
            }
            if (toTime != null && fromTime == null) {
                long toTimeCmt = DateFormat.parse(toTime).getTime();
                findObject.put(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME, new BasicDBObject("$lte", toTimeCmt));
            }

            if (sort == 1) {
                sortObj.append(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME, or);
            }

            DBCursor cursor = coll.find(findObject).sort(sortObj);
            int number = 0;
            int count = 0;
            int limit = skip + take;
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Integer cmtFlag = (Integer) dbObject.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_FLAG);
                Integer approveFlag = (Integer) dbObject.get(BuzzdbKey.COMMENT_DETAIL.APPROVE_FLAG);
                Util.addDebugLog("======approveFlag===" + approveFlag);
//                if (cmtFlag == Constant.FLAG.OFF && approveFlag == Constant.FLAG.ON) {
                //if (isAppearBuzz) {
                    number++;
                    count++;
                    if (count > skip && count <= limit) {
                        CommentDetail commentDetail = new CommentDetail();
//                        commentDetail.commentFlag = cmtFlag;
//                        commentDetail.approveFlag = approveFlag;

                        String bId = (String) dbObject.get(BuzzdbKey.COMMENT_DETAIL.BUZZ_ID);
                        commentDetail.buzzId = bId;

                        String commentId = dbObject.get(BuzzdbKey.COMMENT_DETAIL.ID).toString();
                        commentDetail.commentId = commentId;

                        String uId = (String) dbObject.get(BuzzdbKey.COMMENT_DETAIL.USER_ID);
                        commentDetail.userId = uId;

                        String bVal = (String) dbObject.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_VALUE);
                        commentDetail.commentValue = Util.replaceBannedWordBackend(bVal);

                        Long uploadTime = (Long) dbObject.get(BuzzdbKey.COMMENT_DETAIL.COMMENT_TIME);
                        if (uploadTime != null) {
                            commentDetail.commentTime = DateFormat.format(uploadTime);
                        }
                        if (cmtFlag == Constant.FLAG.OFF || cmtFlag == -1) {
                            commentDetail.isDel = 1;
                        } else if (approveFlag == Constant.FLAG.ON) {
                            commentDetail.isCommentStatus = 1;
                        } else if (approveFlag == -1) {
                            commentDetail.isCommentStatus = -1;
                        } else {
                            commentDetail.isCommentStatus = 0;
                        }

                        String userDeny = (String) dbObject.get(BuzzdbKey.COMMENT_DETAIL.USER_DENY);
                        commentDetail.isUserDeny = userDeny;

                        list.add(commentDetail);
                    }
                //}
            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }
    // #8001
}
