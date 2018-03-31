/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.vn.ntsc.usermanagementserver.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationInfor;
import eazycommon.constant.FilesAndFolders;

/**
 *
 * @author DuongLTD
 */
public class NotificationDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.NOTIFICATION_COLLECTION);
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
            if (noti.point != null && noti.point != 0) {
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

    public static List<String> addNotifications(List<String> toUserIds, Notification noti, long time, int type) throws EazyException {
        List<String> ids = new ArrayList<>();
        try {
            List<DBObject> insertObjs = new ArrayList<>();
            if (toUserIds != null && !toUserIds.isEmpty()) {
                for (String toUserId : toUserIds) {
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
                    if (noti.pushId != null) {
                        insertObj.append(UserdbKey.NOTIFICATION.PUSH_ID, noti.pushId);
                    }

                    insertObjs.add(insertObj);
                }

            }
//            coll.insert(insertObjs, new WriteConcern("", 0, true, false));
            coll.insert(insertObjs);
            for (DBObject obj : insertObjs) {
                ids.add(obj.get(UserdbKey.NOTIFICATION.ID).toString());
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return ids;
    }

    // Add LongLT 30Aug2016
    public static boolean updateIntegerField(ObjectId userid, String key, int value) throws EazyException {
        boolean result = false;
        try {
            //search by email

            BasicDBObject query = new BasicDBObject("_id", userid);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(key, value);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateIntegerListField(String userid, String key, int value) throws EazyException {
        boolean result = false;
        try {
            //search by email

            BasicDBObject query = new BasicDBObject("to_user_id", userid);
            //command add verification code
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                BasicDBObject obj = (BasicDBObject) cursor.next();
                BasicDBObject addition = new BasicDBObject(key, value);
                BasicDBObject command = new BasicDBObject("$set", addition);
                coll.update(obj, command);
            }
           
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeNotification(ObjectId id) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject removeObj = new BasicDBObject(UserdbKey.NOTIFICATION.ID, id);
            coll.remove(removeObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getNotificationNumber(String userId, Long startTime, List<String> blockUser) {
        int result = 0;
        boolean firstRowStatus = true;
        try {
            BasicDBObject query = new BasicDBObject();
            if (startTime == null) {
                startTime = Util.currentTime() - A_YEAR_MILISECONS;
            }
//            BasicDBObject gt = new BasicDBObject("$gt", startTime);
//            query.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId).append(UserdbKey.NOTIFICATION.TIME, gt);
            // Edit LongLT 30Aug2016
            query.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId);
            BasicDBObject gt = new BasicDBObject("$ne", 1);
//            query.append(UserdbKey.NOTIFICATION.IS_READ, gt);
            query.append(UserdbKey.NOTIFICATION.IS_SEEN, gt);

            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();

                Integer typeN = obj.getInt(UserdbKey.NOTIFICATION.TYPE);
                if ((typeN == Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI)
                        || (typeN == Constant.NOTIFICATION_TYPE_VALUE.AUTO_NEWS)
                        || (typeN == Constant.NOTIFICATION_TYPE_VALUE.DAILY_BONUS_NOTI)//thanhdd add #5257
                        ) {
                    continue;
                }

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
    
    public static int getNotificationSeenNumber(String userId, Long startTime, List<String> blockUser) {
        int result = 0;
        boolean firstRowStatus = true;
        try {
            BasicDBObject query = new BasicDBObject();
            if (startTime == null) {
                startTime = Util.currentTime() - A_YEAR_MILISECONS;
            }
//            BasicDBObject gt = new BasicDBObject("$gt", startTime);
//            query.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId).append(UserdbKey.NOTIFICATION.TIME, gt);
            // Edit LongLT 30Aug2016
            query.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId);
            BasicDBObject gt = new BasicDBObject("$ne", 1);
            query.append(UserdbKey.NOTIFICATION.IS_SEEN, gt);
            Util.addDebugLog("query------------------------------------"+query);
            Util.addDebugLog("userId------------------------------------"+userId);
            DBCursor cursor = coll.find(query);
            Util.addDebugLog("cursor------------------------------------"+cursor.size());
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();

                Integer typeN = obj.getInt(UserdbKey.NOTIFICATION.TYPE);
                if ((typeN == Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI)
                        || (typeN == Constant.NOTIFICATION_TYPE_VALUE.AUTO_NEWS)
                        || (typeN == Constant.NOTIFICATION_TYPE_VALUE.DAILY_BONUS_NOTI)//thanhdd add #5257
                        ) {
                    continue;
                }

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

    public static String getLikeNotification(String userId, String ownerId, String buzzId, int type) {
        String result = null;
        try {
            BasicDBObject query = new BasicDBObject();
            query.append(UserdbKey.NOTIFICATION.TO_USER_ID, ownerId);
            query.append(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID, userId);
            query.append(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID, buzzId);
            query.append(UserdbKey.NOTIFICATION.TYPE, type);

            BasicDBObject obj = (BasicDBObject) coll.findOne(query);
            if (obj != null) {
                result = obj.getString(UserdbKey.NOTIFICATION.ID);
            }
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        return result;
    }

    public static void remakeLikeNotification(String id, long time) {
        try {
            ObjectId oid = new ObjectId(id);

            BasicDBObject query = new BasicDBObject(UserdbKey.NOTIFICATION.ID, oid);

            BasicDBObject obj = (BasicDBObject) coll.findOne(query);
            Util.addDebugLog("++++++++++++++++++++++++++++++++++++++ obj " + obj.toString());

            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.NOTIFICATION.TIME, time);
            updateQuery.append(UserdbKey.NOTIFICATION.IS_READ, 0);

            BasicDBObject command = new BasicDBObject("$set", updateQuery);

            coll.update(query, command);
            Util.addDebugLog("++++++++++++++++++++++++++++++++++++++ SUCCSESS");
        } catch (Exception e) {
            Util.addErrorLog(e);
        }
    }

    public static int getNotificationNumberByType(String userId, int type, List<String> blockUsers, Long readTime) {
        int result = 0;
        try {

            BasicDBObject query = new BasicDBObject();
            if (readTime == null) {
                readTime = Util.currentTime() - A_YEAR_MILISECONS;
            }
//            query.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId);
            BasicDBObject gt = new BasicDBObject("$gt", readTime);
            query.append(UserdbKey.NOTIFICATION.TIME, gt);
            query.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId);
            BasicDBObject ne = new BasicDBObject("$ne", 1);
//            query.append(UserdbKey.NOTIFICATION.IS_READ, ne);
            query.append(UserdbKey.NOTIFICATION.IS_SEEN, ne);

            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                Integer typeN = obj.getInt(UserdbKey.NOTIFICATION.TYPE);
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

    // Edit LongLT
    public static List<Notification> getNotificationList(String userId, Long maxTime, int take, List<String> blockUser, int version) throws EazyException {
        List<Notification> result = new ArrayList<>();
        BasicDBObject lt = new BasicDBObject("$lt", maxTime);
        BasicDBObject query = new BasicDBObject(UserdbKey.NOTIFICATION.TO_USER_ID, userId).append(UserdbKey.NOTIFICATION.TIME, lt);
        BasicDBObject sort = new BasicDBObject(UserdbKey.NOTIFICATION.TIME, -1);
        //        BasicDBObject lt = new BasicDBObject("$ne", 1);
        //        BasicDBObject query = new BasicDBObject(UserdbKey.NOTIFICATION.TO_USER_ID, userId)
        //                .append(UserdbKey.NOTIFICATION.IS_READ, lt)
        ;
        DBCursor cursor = coll.find(query).sort(sort);

        boolean isValidNoti = true;
        boolean isValidToTake = true;

        while (cursor.hasNext()) {
            Notification noti = new Notification();
            BasicDBObject obj = (BasicDBObject) cursor.next();
            String notiUserId = (String) obj.get(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID);
            if (notiUserId != null && (!DeactivateUserManager.isDeactivateUser(notiUserId) && !blockUser.contains(notiUserId))) {
                Integer type;
                try {
                    type = obj.getInt(UserdbKey.NOTIFICATION.TYPE);
                } catch (Exception ex) {
                    continue;
                }
                long notiTime = obj.getLong(UserdbKey.NOTIFICATION.TIME);
                noti.time = DateFormat.format(notiTime);
                String notiId = obj.get(UserdbKey.NOTIFICATION.ID).toString();
                noti.notiId = notiId;
                noti.gender = UserDAO.getGenderFromUserId(notiUserId);
//                Integer isRead = (Integer) obj.get(UserdbKey.NOTIFICATION.IS_READ);
//                if(isRead == null){
//                    noti.isRead = 0;
//                    BasicDBObject con1 = new BasicDBObject();
//                    con1.append(UserdbKey.NOTIFICATION.IS_READ, 0);
//                    BasicDBObject con2 = new BasicDBObject();
//                    con2.append("$set", con1);
//                    BasicDBObject con3 = new BasicDBObject();
//                    con3.append(UserdbKey.NOTIFICATION.ID, new ObjectId(notiId));
//                    coll.update(con3, con2, true, false);
//                }
//                else{
//                    noti.isRead = isRead;
//                }
                Util.addDebugLog("Test notiId " + notiId);
                switch (type) {
                    case Constant.NOTIFICATION_TYPE_VALUE.FAVORITED_UNLOCK_NOTI:
                        noti.fromNotiUserId = notiUserId;
                        noti.notiType = type;
//                        result.add(noti);
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI: {
                        noti.fromNotiUserId = notiUserId;
                        noti.notiType = type;
                        String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                        noti.notiUserName = notiUserName;
                        String notiBuzz = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                        noti.notiBuzzId = notiBuzz;
                        addImageAvatarBuzz(notiUserId,noti,notiBuzz);
//                        result.add(noti);
//                        isValidNoti = false;
                        break;
                    }
                    case Constant.NOTIFICATION_TYPE_VALUE.COMMENT_MY_BUZZ_NOTI: {
                        noti.fromNotiUserId = notiUserId;
                        noti.notiType = type;
                        String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                        noti.notiUserName = notiUserName;
                        String notiBuzz = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                        noti.notiBuzzId = notiBuzz;
                        addImageAvatarBuzz(notiUserId,noti,notiBuzz);
//                        result.add(noti);
                        break;
                    }
                    case Constant.NOTIFICATION_TYPE_VALUE.ONLINE_ALERT_NOT: {
                        noti.notiType = type;
                        noti.fromNotiUserId = notiUserId;
                        String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                        noti.notiUserName = notiUserName;
                        String avatarId = UserDAO.getAvatarId(notiUserId);
                        if (avatarId != null) {
                            noti.avataId = avatarId;
                            String url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + ThumbnailDAO.getImageUrl(avatarId);
                            if(!url.contains("null"))
                            noti.avataUrl = url;
                        }
//                        result.add(noti);
                        break;
                    }
                    //thanhdd edit #5257
                    case Constant.NOTIFICATION_TYPE_VALUE.DAILY_BONUS_NOTI:
//                        noti.fromNotiUserId = userId;
//                        noti.notiType = type;
//                        Integer point = obj.getInt(UserdbKey.NOTIFICATION.POINT);
//                        noti.point = point;
                        isValidNoti = false;
//                        result.add(noti);
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI: {
                        noti.notiType = type;
                        noti.fromNotiUserId = notiUserId;
                        String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                        noti.notiUserName = notiUserName;
                        String notiBuzz = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                        noti.notiBuzzId = notiBuzz;
//                        result.add(noti);
                        addImageAvatarBuzz(notiUserId,noti,notiBuzz);
                        break;
                    }
                    case Constant.NOTIFICATION_TYPE_VALUE.SHARE_MUSIC: {
                        noti.notiType = type;
                        noti.fromNotiUserId = notiUserId;
                        String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                        noti.notiUserName = notiUserName;
                        String notiBuzz = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                        noti.notiBuzzId = notiBuzz;
//                        result.add(noti);
                        addImageAvatarBuzz(notiUserId,noti,notiBuzz);
                        break;
                    }
                    case Constant.NOTIFICATION_TYPE_VALUE.APPROVED_BUZZ_NOTI: {
                        noti.fromNotiUserId = userId;
                        noti.notiType = type;
                        String buzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                        noti.notiBuzzId = buzzId;
                        String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
                        noti.notiImageId = imageId;
//                        result.add(noti);
                        break;
                    }
                    case Constant.NOTIFICATION_TYPE_VALUE.APPROVED_BACKSTAGE_NOTI: {
                        noti.fromNotiUserId = userId;
                        noti.notiType = type;
                        String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
                        noti.notiImageId = imageId;
//                        result.add(noti);
                        break;
                    }
                    case Constant.NOTIFICATION_TYPE_VALUE.DENIED_BUZZ_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
                            //                    String buzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                            //                    noti.notiBuzzId = buzzId;
                            //                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
                            //                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.DENIED_BACKSTAGE_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
                            //                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
                            //                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.APPROVED_TEXT_BUZZ_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
                            noti.notiBuzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                            //                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
                            //                    noti.notiImageId = imageId;
//                            result.add(noti);
                            addImageAvatarBuzz(notiUserId,noti,noti.notiBuzzId);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.DENIED_TEXT_BUZZ_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
//                        noti.notiBuzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                            String buzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                            addImageAvatarBuzz(notiUserId,noti,buzzId);
//                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
//                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.APPROVED_COMMENT_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
                            noti.notiBuzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                            //                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
                            //                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.DENIED_COMMENT_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
                            noti.notiBuzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                            //                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
                            //                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.APPROVED_SUB_COMMENT_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
                            noti.notiBuzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                            //                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
                            //                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.DENIED_SUB_COMMENT_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
                            noti.notiBuzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                            //                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
                            //                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.APPROVED_USER_INFOR_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
//                        noti.notiBuzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
//                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
//                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.DENIED_A_PART_OF_USER_INFO_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
//                        noti.notiBuzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
//                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
//                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.DENIED_USER_INFO_NOTI:
                        if (version > 2) {
                            noti.fromNotiUserId = userId;
                            noti.notiType = type;
//                        noti.notiBuzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
//                    String imageId = obj.getString(UserdbKey.NOTIFICATION.NOTI_IMAGE_ID);
//                    noti.notiImageId = imageId;
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.FREE_PAGE_NOTI:
                        noti.fromNotiUserId = userId;
                        noti.notiType = type;
                        String content = obj.getString(UserdbKey.NOTIFICATION.CONTENT);
                        String url = obj.getString(UserdbKey.NOTIFICATION.URL);
                        String pushId = obj.getString(UserdbKey.NOTIFICATION.PUSH_ID);
                        noti.url = url;
                        noti.content = content;
                        noti.pushId = pushId;
//                        result.add(noti);
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT:
                        if (version != 1) {
//                            noti.fromNotiUserId = userId;
                            noti.notiCommentOwnerId = userId;
                            noti.notiType = type;
                            String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                            noti.notiUserName = notiUserName;
                            String commentId = obj.getString(UserdbKey.NOTIFICATION.NOTI_COMMENT_ID);
                            noti.notiCommentId = commentId;
                            String buzzId = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                            noti.notiBuzzId = buzzId;
                            addImageAvatarBuzz(notiUserId,noti,buzzId);
//                            result.add(noti);
                        }
                        break;
                    case Constant.NOTIFICATION_TYPE_VALUE.TAG_FROM_BUZZ_NOTI: {
                        noti.notiType = type;
                        noti.fromNotiUserId = notiUserId;
                        String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                        noti.notiUserName = notiUserName;
                        String notiBuzz = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                        noti.notiBuzzId = notiBuzz;
//                        result.add(noti);
                        addImageAvatarBuzz(notiUserId,noti,notiBuzz);
                        break;
                    }
                    default:
                        isValidNoti = false;
                        break;
                }

                noti.isRead = Util.getIntValue(obj.get(UserdbKey.NOTIFICATION.IS_READ));
                noti.isSeen = Util.getIntValue(obj.get(UserdbKey.NOTIFICATION.IS_SEEN));

                Util.addDebugLog("Test for isValidNoti " + isValidNoti);
                if (isValidNoti) {
//                    result.add(noti);
//                    updateIntegerField(obj.getObjectId("_id"), UserdbKey.NOTIFICATION.IS_SEEN, 1);
                    if (isValidToTake) {
                        result.add(noti);
                    }
                }

                isValidNoti = true;
            }
            if (result.size() >= take) {
                isValidToTake = false;
                break;
            }
        }
        return result;
    }

    public static void addImageAvatarBuzz(String notiUserId, Notification noti,String notiBuzz) throws EazyException {
        String avatarId = UserDAO.getAvatarId(notiUserId);
        if (avatarId != null) {
            noti.avataId = avatarId;
            String url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + ThumbnailDAO.getImageUrl(avatarId);
            if(!url.contains("null"))
                noti.avataUrl = url;
        }
        Integer typeBuzz = BuzzDetailDAO.getType(notiBuzz);
        if (typeBuzz == null) {
            return;
        }
        if (typeBuzz == Constant.BUZZ_TYPE_VALUE.TEXT_STATUS) {
            String text = BuzzDetailDAO.getText(notiBuzz);
            if(text != null)
                noti.text = text;
        }
        if (typeBuzz == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS) {
            String imageId = BuzzDetailDAO.getImageId(notiBuzz);
            String text = BuzzDetailDAO.getText(notiBuzz);
            String url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + ThumbnailDAO.getImageUrl(imageId);
            if(!url.contains("null"))
                noti.thumnailUrl = url;
            if(text != null)
                noti.text = text;
        }
        noti.buzzType = typeBuzz;
    }

    public static List<IEntity> getNotificationListByType(String userId, Long maxTime, int take, List<String> blockList, int type) throws EazyException {
        List<IEntity> result = new ArrayList<>();
        List<ObjectId> setReadList = new ArrayList<>();// LongLT
        BasicDBObject lt = new BasicDBObject("$lt", maxTime);
//        BasicDBObject query = new BasicDBObject(UserdbKey.NOTIFICATION.TO_USER_ID, userId).append(UserdbKey.NOTIFICATION.TIME, lt);
//        BasicDBObject lt = new BasicDBObject("$ne", 1);
        BasicDBObject query = new BasicDBObject(UserdbKey.NOTIFICATION.TO_USER_ID, userId); //                .append(UserdbKey.NOTIFICATION.IS_READ, lt)
        query.append(UserdbKey.NOTIFICATION.TIME, lt);
        query.append(UserdbKey.NOTIFICATION.TYPE, type);

        BasicDBObject sort = new BasicDBObject(UserdbKey.NOTIFICATION.TIME, -1);
        DBCursor cursor = coll.find(query).sort(sort);
        while (cursor.hasNext()) {
            Notification noti = new Notification();
            BasicDBObject obj = (BasicDBObject) cursor.next();
//            Integer typeN = obj.getInt(UserdbKey.NOTIFICATION.TYPE);
            String notiId = obj.get(UserdbKey.NOTIFICATION.ID).toString();
            Integer isSeen = Util.getIntValue(obj.get(UserdbKey.NOTIFICATION.IS_SEEN));
            Integer isRead = Util.getIntValue(obj.get(UserdbKey.NOTIFICATION.IS_READ));
            noti.notiId = notiId;
//            if (typeN == type) {
//                Util.addDebugLog("News Type ok " + typeN);
            String notiUserId = obj.getString(UserdbKey.NOTIFICATION.TO_USER_ID);
            String fromID = obj.getString(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID);
            if (notiUserId == null || (!DeactivateUserManager.isDeactivateUser(notiUserId) && !blockList.contains(notiUserId))) {
                noti.notiId = notiId;
                noti.fromNotiUserId = fromID;
                noti.notiType = type;
                String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                noti.notiUserName = notiUserName;
                String notiBuzz = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                noti.notiBuzzId = notiBuzz;
                String notiCommentId = (String) obj.get(UserdbKey.NOTIFICATION.NOTI_COMMENT_ID);
                noti.notiCommentId = notiCommentId;
                long notiTime = obj.getLong(UserdbKey.NOTIFICATION.TIME);
                noti.time = DateFormat.format(notiTime);
                noti.content = obj.getString(UserdbKey.NOTIFICATION.CONTENT);
                noti.url = obj.getString(UserdbKey.NOTIFICATION.URL);
                if (obj.get(UserdbKey.NOTIFICATION.PUSH_ID) != null) { //thanhdd edit
                    noti.pushId = obj.get(UserdbKey.NOTIFICATION.PUSH_ID).toString();
                }
                noti.isRead = isRead;
                noti.isSeen = isSeen;

                result.add(noti);
                setReadList.add(obj.getObjectId("_id"));// LongLT
//                }
            }
            if (result.size() >= take) {
                break;
            }
        }
        for (int i = 0; i < setReadList.size(); i++) {
            updateIntegerField(setReadList.get(i), UserdbKey.NOTIFICATION.IS_READ, 1);
        }
        return result;
    }

    public static void updateSeenForNewsNotificationClickByPushId(String notiId, String userId) {
        BasicDBObject queryObject = new BasicDBObject();
        queryObject.append(UserdbKey.NOTIFICATION.PUSH_ID, notiId);
        queryObject.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId);
        BasicDBObject updateObject = new BasicDBObject(UserdbKey.NOTIFICATION.IS_SEEN, 1);
        BasicDBObject command = new BasicDBObject("$set", updateObject);
        coll.update(queryObject, command);
    }

    public static void updateSeenForNewsNotificationClickByNotiId(String notiId, String userId) {
        BasicDBObject queryObject = new BasicDBObject();
        queryObject.append(UserdbKey.NOTIFICATION.ID, new ObjectId(notiId));
        queryObject.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId);
        BasicDBObject updateObject = new BasicDBObject(UserdbKey.NOTIFICATION.IS_SEEN, 1);
        updateObject.append(UserdbKey.NOTIFICATION.IS_READ, 1);
        BasicDBObject command = new BasicDBObject("$set", updateObject);
        coll.update(queryObject, command);
    }

    // Add LongLT 30Aug2016
    public static List<IEntity> getNotificationListByType(String userId, int take, List<String> blockList, int type) throws EazyException {
        List<IEntity> result = new ArrayList<>();
        List<ObjectId> setReadList = new ArrayList<>();
        BasicDBObject lt = new BasicDBObject("$ne", 1);
        BasicDBObject query = new BasicDBObject(UserdbKey.NOTIFICATION.TO_USER_ID, userId).append(UserdbKey.NOTIFICATION.IS_READ, lt);
        BasicDBObject sort = new BasicDBObject(UserdbKey.NOTIFICATION.TIME, -1);
        DBCursor cursor = coll.find(query).sort(sort);
        while (cursor.hasNext()) {
            Notification noti = new Notification();
            BasicDBObject obj = (BasicDBObject) cursor.next();
            Integer typeN = obj.getInt(UserdbKey.NOTIFICATION.TYPE);
            Util.addDebugLog("News Type " + type);
            if (typeN == type) {
                Util.addDebugLog("News Type ok " + typeN);
                String notiUserId = obj.getString(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID);
                if (notiUserId == null || (!DeactivateUserManager.isDeactivateUser(notiUserId) && !blockList.contains(notiUserId))) {
                    noti.fromNotiUserId = notiUserId;
                    noti.notiType = type;
                    String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                    noti.notiUserName = notiUserName;
                    String notiBuzz = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                    noti.notiBuzzId = notiBuzz;
                    String notiCommentId = (String) obj.get(UserdbKey.NOTIFICATION.NOTI_COMMENT_ID);
                    noti.notiCommentId = notiCommentId;
                    long notiTime = obj.getLong(UserdbKey.NOTIFICATION.TIME);
                    noti.time = DateFormat.format(notiTime);
                    noti.content = obj.getString(UserdbKey.NOTIFICATION.CONTENT);
                    noti.url = obj.getString(UserdbKey.NOTIFICATION.URL);
                    result.add(noti);
                    setReadList.add(obj.getObjectId("_id"));
                }
            }
            if (result.size() >= take) {
                break;
            }
        }
        for (int i = 0; i < setReadList.size(); i++) {
            updateIntegerField(setReadList.get(i), UserdbKey.NOTIFICATION.IS_READ, 1);
        }
        return result;
    }

    public static List<IEntity> getNewsNotificationListByType(String userId, Long maxTime, int take, List<String> blockList, int type) {
        List<IEntity> result = new ArrayList<>();
        BasicDBObject lt = new BasicDBObject("$lt", maxTime);
        BasicDBObject query = new BasicDBObject(UserdbKey.NOTIFICATION.TO_USER_ID, userId).append(UserdbKey.NOTIFICATION.TIME, lt);
        BasicDBObject sort = new BasicDBObject(UserdbKey.NOTIFICATION.TIME, -1);
        DBCursor cursor = coll.find(query).sort(sort);
        while (cursor.hasNext()) {
            Notification noti = new Notification();
            BasicDBObject obj = (BasicDBObject) cursor.next();
            Integer typeN = obj.getInt(UserdbKey.NOTIFICATION.TYPE);
            Util.addDebugLog("News Type " + type);
            if (typeN == type) {
                Util.addDebugLog("News Type ok " + typeN);
                String notiUserId = obj.getString(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID);
                if (notiUserId == null || (!DeactivateUserManager.isDeactivateUser(notiUserId) && !blockList.contains(notiUserId))) {
                    noti.fromNotiUserId = notiUserId;
                    noti.notiType = type;
                    String notiUserName = obj.getString(UserdbKey.NOTIFICATION.NOTI_USER_NAME);
                    noti.notiUserName = notiUserName;
                    String notiBuzz = obj.getString(UserdbKey.NOTIFICATION.NOTI_BUZZ_ID);
                    noti.notiBuzzId = notiBuzz;
                    String notiCommentId = (String) obj.get(UserdbKey.NOTIFICATION.NOTI_COMMENT_ID);
                    noti.notiCommentId = notiCommentId;
                    long notiTime = obj.getLong(UserdbKey.NOTIFICATION.TIME);
                    noti.time = DateFormat.format(notiTime);
                    noti.content = obj.getString(UserdbKey.NOTIFICATION.CONTENT);
                    noti.url = obj.getString(UserdbKey.NOTIFICATION.URL);
                    result.add(noti);
                }
            }
            if (result.size() >= take) {
                break;
            }
        }
        return result;
    }

//    public static void remove() {
//        try {
//            DBCursor cur = coll.find();
//            while (cur.hasNext()) {
//                long time = Util.currentTime();
//                DBObject obj = cur.next();
//                Long notiTime = (Long) obj.get(UserdbKey.NOTIFICATION.TIME);
//                if (((time - notiTime) / Constant.TIME_PER_DAY) > Constant.SIX_WEEKS) {
//                    coll.remove(obj);
//                }
//            }
//        } catch (Exception ex) {
//            Util.addErrorLog(ex);
//        }
//    }
    public static boolean removeByIds(List<String> ids) {
        boolean result = false;
        try {
            List<ObjectId> obIds = new ArrayList<>();
            for (String id : ids) {
                if (ObjectId.isValid(id)) {
                    obIds.add(new ObjectId(id));
                }
            }
            DBObject query = QueryBuilder.start(UserdbKey.NOTIFICATION.ID).in(obIds).get();
            coll.remove(query);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static Map<String, NotificationInfor> getAll() {
        Map<String, NotificationInfor> result = new ConcurrentHashMap<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                ObjectId id = (ObjectId) dbO.get(UserdbKey.NOTIFICATION.ID);
                Long time = (Long) dbO.get(UserdbKey.NOTIFICATION.TIME);
                if (time != null) {
                    NotificationInfor infor = new NotificationInfor(id.toString(), time);
                    result.put(id.toString(), infor);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static void notiSeen(String userId, String pushId) {
        BasicDBObject find = new BasicDBObject();
        find.append(UserdbKey.NOTIFICATION.TO_USER_ID, userId);
        find.append(UserdbKey.NOTIFICATION.PUSH_ID, pushId);
        BasicDBObject query = new BasicDBObject(UserdbKey.NOTIFICATION.IS_SEEN, new Integer(1));
        BasicDBObject command = new BasicDBObject("$set", query);
        coll.update(find, command);
    }

}
