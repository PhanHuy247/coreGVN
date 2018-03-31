/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.exception.EazyException;
import com.vn.ntsc.jpns.dao.impl.Notification;
import com.vn.ntsc.jpns.server.DeactivateUserManager;

/**
 *
 * @author DuongLTD
 */
public class NotificationDAO {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
//            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.NOTIFICATION_COLLECTION);
            db = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            coll = db.getCollection(UserdbKey.NOTIFICATION_COLLECTION);
            coll.createIndex(new BasicDBObject(UserdbKey.NOTIFICATION.TIME, 1));
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    private static final long A_YEAR_MILISECONS = 365 * 24 * 3600 * 1000;
    
    public static String addNotification(String toUserId, Notification noti, long time, int type) throws EazyException {
        try {
            BasicDBObject insertObj = new BasicDBObject(UserdbKey.NOTIFICATION.TO_USER_ID, toUserId);
            insertObj.append(UserdbKey.NOTIFICATION.TIME, time);
            insertObj.append(UserdbKey.NOTIFICATION.TYPE, type);
            if (noti.fromNotiUserId != null) {
                insertObj.append(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID, noti.fromNotiUserId);
            }
            if (noti.notiUserName != null) {
                insertObj.append(UserdbKey.NOTIFICATION.NOTI_USER_NAME, noti.notiUserName);
            }
            if (noti.notiBuzzId != null) {
                insertObj.append(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID, noti.notiBuzzId);
            }
            if (noti.notiBuzzOwnerId != null) {
                insertObj.append(UserdbKey.NOTIFICATION.NOTI_BUZZ_OWNER_ID, noti.notiBuzzOwnerId);
            }
            if (noti.notiBuzzOwnerName != null) {
                insertObj.append(UserdbKey.NOTIFICATION.NOTI_BUZZ_OWNER_NAME, noti.notiBuzzOwnerName);
            }
            if (noti.point != null) {
                insertObj.append(UserdbKey.NOTIFICATION.POINT, noti.point);
            }
            if (noti.notiImageId != null) {
                insertObj.append(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID, noti.notiImageId);
            }
            if (noti.content != null) {
                insertObj.append(UserdbKey.NOTIFICATION.CONTENT, noti.content);
            }
            if (noti.url != null) {
                insertObj.append(UserdbKey.NOTIFICATION.URL, noti.url);
            }
            if (noti.notiCommentId != null) {
                insertObj.append(UserdbKey.NOTIFICATION.NOTI_COMMENT_ID, noti.notiCommentId);
            }
            if (noti.notiCommentOwnerId != null) {
                insertObj.append(UserdbKey.NOTIFICATION.NOTI_COMMENT_OWNER_ID, noti.notiCommentOwnerId);
            }
            if (noti.notiCommentOwnerName != null) {
                insertObj.append(UserdbKey.NOTIFICATION.NOTI_COMMENT_OWNER_NAME, noti.notiCommentOwnerName);
            }

//            coll.insert(insertObj, new WriteConcern("", 0, true, false));
            coll.insert(insertObj);
            return insertObj.get(UserdbKey.NOTIFICATION.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    // Add LongLT 30Aug2016
    public static int getNotificationNumberByType(String userId, int type, List<String> blockUsers) {
        int result = 0;
        try {

            BasicDBObject query = new BasicDBObject();

            query.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId);
            BasicDBObject gt = new BasicDBObject("$ne", 1);
            query.append(UserdbKey.NOTIFICATION.IS_READ, gt);
            Util.addDebugLog("getNotificationNumberByType obj ");

            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                Integer typeN = obj.getInt(UserdbKey.NOTIFICATION.TYPE);
                Util.addDebugLog("getNotificationNumberByType obj " + obj.toString());
                if (typeN == type) {
                    String notiUserId = obj.getString(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID);
                    if (notiUserId == null || (!DeactivateUserManager.isDeactivateUser(notiUserId) && !blockUsers.contains(notiUserId))) {
                        String buzzOwnerId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_OWNER_ID);
                        if (buzzOwnerId == null || (!DeactivateUserManager.isDeactivateUser(buzzOwnerId) && !blockUsers.contains(buzzOwnerId))) {
                            result++;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            result = 0;
        }
        return result;
    }

    public static int getNotificationNumber(String userId, Long startTime, List<String> blockUser) {
        int result = 0;
        try {
            BasicDBObject query = new BasicDBObject();
            if (startTime == null) {
                startTime = Util.currentTime() - A_YEAR_MILISECONS;
            }
            BasicDBObject gt = new BasicDBObject("$gt", startTime);
            query.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId).append(UserdbKey.NOTIFICATION.TIME, gt);
            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String notiUserId = obj.getString(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID);
                if (notiUserId == null || (!DeactivateUserManager.isDeactivateUser(notiUserId) && !blockUser.contains(notiUserId))) {
                    String buzzOwnerId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_OWNER_ID);
                    if (buzzOwnerId == null || (!DeactivateUserManager.isDeactivateUser(buzzOwnerId) && !blockUser.contains(buzzOwnerId))) {
                        result++;
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            result = 0;
        }
        return result;
    }

}
