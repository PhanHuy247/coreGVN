/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.user.FollowingUser;

/**
 *
 * @author RuAc0n
 */
public class MyFootPrintDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.MY_FOOTPRINT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static boolean add(String userId, String friendId, Date time) throws EazyException {
        boolean result = false;
        if (userId == null || userId.isEmpty()){
            return result;
        }
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.MY_FOOTPRINTS.ID, id);
            BasicDBObject checkerObj = new BasicDBObject(UserdbKey.MY_FOOTPRINTS.VISITOR_ID, friendId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", checkerObj);
            findObj.append(UserdbKey.MY_FOOTPRINTS.MY_FOOTPRINTS_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = UserdbKey.MY_FOOTPRINTS.MY_FOOTPRINTS_LIST + ".$." + UserdbKey.MY_FOOTPRINTS.VISITED_TIME;
                BasicDBObject updateObj = new BasicDBObject(field, time.getTime());
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            } else {
                BasicDBObject updateQuery = new BasicDBObject(UserdbKey.MY_FOOTPRINTS.ID, id);
                BasicDBObject checkerElement = new BasicDBObject(UserdbKey.MY_FOOTPRINTS.VISITOR_ID, friendId);
                checkerElement.append(UserdbKey.MY_FOOTPRINTS.VISITED_TIME, time.getTime());
                BasicDBObject checker = new BasicDBObject(UserdbKey.MY_FOOTPRINTS.MY_FOOTPRINTS_LIST, checkerElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", checker);
                coll.update(updateQuery, updateCommand, true, false);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Date getVisitedTime(String userId, String friendId) throws EazyException {
        Date result = null;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.MY_FOOTPRINTS.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList checkList = (BasicDBList) respondObj.get(UserdbKey.MY_FOOTPRINTS.MY_FOOTPRINTS_LIST);
                if (checkList != null && !checkList.isEmpty()) {
                    for (Object checkList1 : checkList) {
                        BasicDBObject check = (BasicDBObject) checkList1;
                        String checkId = check.getString(UserdbKey.MY_FOOTPRINTS.VISITOR_ID);
                        if (checkId.equals(friendId)) {
                            Long checkTime = (Long) check.get(UserdbKey.MY_FOOTPRINTS.VISITED_TIME);
                            String strTime = DateFormat.format(checkTime);
                            result = DateFormat.parse(strTime);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, FollowingUser> getMyFootprintsList(String userId, List<String> listCheckout) throws EazyException {
        Map<String, FollowingUser> result = new HashMap<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.MY_FOOTPRINTS.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList checkList = (BasicDBList) respondObj.get(UserdbKey.MY_FOOTPRINTS.MY_FOOTPRINTS_LIST);
                if (checkList != null && !checkList.isEmpty()) {
                    //get friend Id
                    for (int i = checkList.size() - 1; i >= 0; i--) {
                        BasicDBObject obj = (BasicDBObject) checkList.get(i);
                        FollowingUser user = new FollowingUser();
                        String checkId = obj.getString(UserdbKey.MY_FOOTPRINTS.VISITOR_ID);
                        Long time = obj.getLong(UserdbKey.MY_FOOTPRINTS.VISITED_TIME);
                        user.userId = checkId;
                        user.checkoutTime = time;
                        listCheckout.add(checkId);
                        result.put(checkId, user);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static HashMap<String, HashMap<String, Long>> getAll() {
        HashMap<String, HashMap<String, Long>> result = new HashMap<>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String userId = obj.get(UserdbKey.MY_FOOTPRINTS.ID).toString();
                BasicDBList checkoutList = (BasicDBList) obj.get(UserdbKey.MY_FOOTPRINTS.MY_FOOTPRINTS_LIST);
                if (checkoutList != null && !checkoutList.isEmpty()) {
                    HashMap<String, Long> checkoutMap = new  HashMap<>();
                    for (Object o : checkoutList) {
                        DBObject checkoutObj = (DBObject) o;
                        String friendId = (String) checkoutObj.get(UserdbKey.MY_FOOTPRINTS.VISITOR_ID);
                        Long time = (Long) checkoutObj.get(UserdbKey.MY_FOOTPRINTS.VISITED_TIME);
                        checkoutMap.put(friendId, time);
                    }
                    result.put(userId, checkoutMap);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static boolean removeFootprint(ObjectId id) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject removeObj = new BasicDBObject(UserdbKey.MY_FOOTPRINTS.ID, id);
            coll.remove(removeObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
}
