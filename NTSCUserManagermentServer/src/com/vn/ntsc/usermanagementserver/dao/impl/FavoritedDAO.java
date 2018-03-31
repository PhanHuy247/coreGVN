/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eazycommon.constant.Constant;
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
public class FavoritedDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.FAVORITED_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static boolean addFavourited(String userId, String friendId, Long time) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORITED.ID, id);
            BasicDBObject objFavoristed = new BasicDBObject(UserdbKey.FAVORITED.FAVOURISTED_ID, friendId)
                    .append(UserdbKey.FAVORITED.FAVOURISTED_TIME, time);
            BasicDBObject obj = new BasicDBObject(UserdbKey.FAVORITED.FAVOURITED_LIST, objFavoristed);
            BasicDBObject updateCommand = new BasicDBObject("$push", obj);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Long checkFavourited(String userId, String friendId) {
        long result = Constant.FLAG.OFF;
        ObjectId id = new ObjectId(userId);
        BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORITED.ID, id);
        BasicDBObject elementMatch = new BasicDBObject("$elemMatch", new BasicDBObject(UserdbKey.FAVORITED.FAVOURISTED_ID, friendId));

        updateQuery.append(UserdbKey.FAVORITED.FAVOURITED_LIST, elementMatch);
        DBObject respondObj = coll.findOne(updateQuery);
        if (respondObj != null) {
            result = Constant.FLAG.ON;
        }
        return result;
    }

    public static boolean removeFavourited(String userId, String friendId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.FAVORITED.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.FAVORITED.FAVOURITED_LIST, new BasicDBObject(UserdbKey.FAVORITED.FAVOURISTED_ID, friendId));
            BasicDBObject updateCommand = new BasicDBObject("$pull", obj);
            coll.update(query, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String,FollowingUser> getFavouritedMap(String userId, List<String> fvtList) throws EazyException {
        Map<String, FollowingUser> result = new HashMap<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORITED.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList favoritedList = (BasicDBList) respondObj.get(UserdbKey.FAVORITED.FAVOURITED_LIST);
                if (!favoritedList.isEmpty()) {
                    for (int i = favoritedList.size() - 1; i >= 0; i--) {
                        BasicDBObject obj = (BasicDBObject) favoritedList.get(i);
                        String friendId = (String) obj.getString(UserdbKey.FAVORITED.FAVOURISTED_ID);
                        Long time = (Long) obj.getLong(UserdbKey.FAVORITED.FAVOURISTED_TIME);
                        FollowingUser user = new FollowingUser();
                        user.fvtTime = time;
                        user.userId = friendId;
                        result.put(friendId,user);
                        fvtList.add(friendId);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getFavoristIdList(String userId) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORITED.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList favoritedList = (BasicDBList) respondObj.get(UserdbKey.FAVORITED.FAVOURITED_LIST);
                if (!favoritedList.isEmpty()) {
                    for (Object favoritedList1 : favoritedList) {
                        BasicDBObject obj = (BasicDBObject) favoritedList1;
                        String friendId = (String) obj.getString(UserdbKey.FAVORITED.FAVOURISTED_ID);
                        result.add(friendId);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getNewFavoritedNumber(String userId, Long time) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORITED.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList favoritedList = (BasicDBList) respondObj.get(UserdbKey.FAVORITED.FAVOURITED_LIST);
                if (!favoritedList.isEmpty()) {
                    for (Object favoritedList1 : favoritedList) {
                        BasicDBObject obj = (BasicDBObject) favoritedList1;
                        String friendId = (String) obj.getString(UserdbKey.FAVORITED.FAVOURISTED_ID);
                        Long timeFvt = (Long) obj.getLong(UserdbKey.FAVORITED.FAVOURISTED_TIME);
                        if (timeFvt > time) {
                            result.add(friendId);
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

//    public static void main(String[] args) throws DaoException {
//        int result = getNewFavoritedNumber("53d1cbfc79ef791ebd282834", Long.MIN_VALUE).size();
//        System.out.println(result);
//    }
}
