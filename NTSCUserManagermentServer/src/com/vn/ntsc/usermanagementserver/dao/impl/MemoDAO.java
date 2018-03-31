/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Memo;

/**
 *
 * @author khanhdd
 */
public class MemoDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.MEMO_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void updateMemo(String userId, String friendId, String memo, Date time) {
        DBObject findObj = new BasicDBObject(UserdbKey.MEMO.USER_ID, userId)
                .append(UserdbKey.MEMO.FRIEND_ID, friendId);
        String timeStr = DateFormat.format(time);
        DBObject setObj = new BasicDBObject(UserdbKey.MEMO.USER_ID, userId)
                .append(UserdbKey.MEMO.FRIEND_ID, friendId)
                .append(UserdbKey.MEMO.MEMO, memo)
                .append(UserdbKey.MEMO.UPDATE_TIME, DateFormat.format(time));
        DBObject updateObj = new BasicDBObject("$set", setObj);
        coll.update(findObj, updateObj, true, false);
    }

    public static Map<String, String> getMemos(String userId) {
        Map<String, String> map = new HashMap<>();
        try {
            DBObject id = new BasicDBObject(UserdbKey.MEMO.USER_ID, userId);
            DBCursor cursor = coll.find(id);
            while (cursor.hasNext()) {
                DBObject dbo = cursor.next();
                String friendId = (String) dbo.get(UserdbKey.MEMO.FRIEND_ID);
                String memo = (String) dbo.get(UserdbKey.MEMO.MEMO);
                map.put(friendId, memo);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return map;
    }

    public static List<Memo> getMemoListByUserId(String userId, Long skip, Long take) {
        List<Memo> result = new ArrayList<>();
        BasicDBObject sort = new BasicDBObject(UserdbKey.MEMO.UPDATE_TIME, -1);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.MEMO.USER_ID, userId);
        Util.addDebugLog("Test skip " + skip);
        Util.addDebugLog("Test take " + take);
        DBCursor cursor = coll.find(findObj).sort(sort);
        int size = cursor.size();
        Util.addDebugLog("Test size " + size);
        if (skip != null && take != null) {
            cursor = cursor.skip(skip.intValue()).limit(take.intValue());
        }
        while (cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject) cursor.next();

            String memo = obj.getString(UserdbKey.MEMO.MEMO);
            if (memo == null || "".equals(memo)) {
                continue;
            }

            String id = obj.getObjectId(UserdbKey.MEMO.ID).toString();
            Util.addDebugLog("Test for id " + id);
            String friendId = obj.getString(UserdbKey.MEMO.FRIEND_ID);
            DBObject userObject = UserDAO.getUserById(friendId);
            String friendName = userObject.get(UserdbKey.USER.USERNAME).toString();
            String friendBirth = userObject.get(UserdbKey.USER.BIRTHDAY).toString();

            int age = Util.convertBirthdayToAge(friendBirth);
            String friendAvatar = null;
            if (ImageDAO.getAvatarIdByUserId(friendId) != null) {
                friendAvatar = ImageDAO.getAvatarIdByUserId(friendId);
            }
            Util.addDebugLog("Test for friendAvatar " + friendAvatar);

            String time = obj.getString(UserdbKey.MEMO.UPDATE_TIME);
            result.add(new Memo(id, friendId, memo, time, friendAvatar, friendName, friendBirth, age));
        }
        return result;
    }

    public static String getMemoByFriendID(String userId, String friendId) {
        String result = "";
        if (userId == null || userId.isEmpty()){
            return result;
        }
        BasicDBObject findObj = new BasicDBObject();
        findObj.append(UserdbKey.MEMO.USER_ID, userId);
        findObj.append(UserdbKey.MEMO.FRIEND_ID, friendId);

        DBObject obj = coll.findOne(findObj);
        if (obj != null) {
            result = (String) obj.get(UserdbKey.MEMO.MEMO);
        }
        return result;
    }

}
