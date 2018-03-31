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
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.buzz.ReviewingBuzz;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;

/**
 *
 * @author DuongLTD
 */
public class ReviewingBuzzDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.REVIEWING_BUZZ_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static SizedListData getReviewingBuzz(String userId, String buzzId, Long sort, Long order, int skip, int take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBObject findObject = new BasicDBObject();
            if (buzzId != null) {
                findObject.put(BuzzdbKey.REVIEWING_BUZZ.ID, new ObjectId(buzzId));
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
                        BasicDBObject findObjectUserId = new BasicDBObject(BuzzdbKey.REVIEWING_BUZZ.USER_ID, regex);
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
                sortObj.append(BuzzdbKey.REVIEWING_BUZZ.BUZZ_TIME, or);
            }
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            //set to list
            Integer number = cursor.size();
            cursor = cursor.skip(skip).limit(take);
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                ReviewingBuzz reviewingBuzz = new ReviewingBuzz();
                String bID = dbObject.get(BuzzdbKey.REVIEWING_BUZZ.ID).toString();
                reviewingBuzz.buzzId = bID;
                String uId = (String) dbObject.get(BuzzdbKey.REVIEWING_BUZZ.USER_ID);
                reviewingBuzz.userId = uId;
                String bVal = (String) dbObject.get(BuzzdbKey.REVIEWING_BUZZ.BUZZ_VALUE);
                //HUNGDT add 3678
                reviewingBuzz.buzzVal = Util.replaceBannedWordBackend(bVal);
                Long uploadTime = (Long) dbObject.get(BuzzdbKey.REVIEWING_BUZZ.BUZZ_TIME);
                if (uploadTime != null) {
                    reviewingBuzz.buzzTime = DateFormat.format(uploadTime);
                }
                list.add(reviewingBuzz);
            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void remove(String id) throws EazyException {
        try {
            DBObject obj = new BasicDBObject(BuzzdbKey.REVIEWING_BUZZ.ID, new ObjectId(id));
            coll.remove(obj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static ReviewingBuzz get(String id) throws EazyException {
        ReviewingBuzz result = null;
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.REVIEWING_BUZZ.ID, new ObjectId(id));
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                result = new ReviewingBuzz();
                String bID = obj.get(BuzzdbKey.REVIEWING_BUZZ.ID).toString();
                result.buzzId = bID;
                String uId = (String) obj.get(BuzzdbKey.REVIEWING_BUZZ.USER_ID);
                result.userId = uId;
                String bVal = (String) obj.get(BuzzdbKey.REVIEWING_BUZZ.BUZZ_VALUE);
                result.buzzVal = bVal;
                Long uploadTime = (Long) obj.get(BuzzdbKey.REVIEWING_BUZZ.BUZZ_TIME);
                if (uploadTime != null) {
                    result.buzzTime = DateFormat.format(uploadTime);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
