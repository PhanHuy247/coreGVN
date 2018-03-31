/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.NotificationSetting;

/**
 *
 * @author DuongLTD
 */
public class NotificationSettingDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.NOTIFICATION_SETTING_COLLECTION);
        } catch (Exception ex) {
            //Future logging here

        }
    }

    public static boolean addNotificationSetting(String userId, int notiBuzz, int andgAlert, int notiChat, int notiCheck, int notiLike) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject insertObj = new BasicDBObject(UserdbKey.NOTIFICATION_SETTING.ID, id);
            insertObj.append(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ, notiBuzz);
            insertObj.append(UserdbKey.NOTIFICATION_SETTING.EAZY_ALERT, andgAlert);
            insertObj.append(UserdbKey.NOTIFICATION_SETTING.NOTI_CHAT, notiChat);
            insertObj.append(UserdbKey.NOTIFICATION_SETTING.NOTI_CHECK_OUT, notiCheck);
            insertObj.append(UserdbKey.NOTIFICATION_SETTING.NOTI_LIKE, notiLike);
            
            coll.save(insertObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static NotificationSetting getNotificationSetting(String userId) {
        Util.addDebugLog("Test final day  nay");
        NotificationSetting result = null;
        try{
        result = new NotificationSetting();
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.NOTIFICATION_SETTING.ID, id);
        DBObject obj = coll.findOne(findObj);
        if(obj == null){
            Util.addDebugLog("NotificationSetting null $$$$$$$$$$");
            return result;
        }
        else {
//            Integer notiCheckOut = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_CHECK_OUT);
//            result.notiCheckOut = notiCheckOut;
            Integer notiChat = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_CHAT);
            result.chat = notiChat;
            Integer andGAlert = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.EAZY_ALERT);
            result.andgAlert = andGAlert;
            Integer notiBuzz = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ);
            result.notiBuzz = notiBuzz;
            Integer notiLike = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_LIKE);
            result.notiLike = notiLike;
            return result;
        }
        }
        catch(Exception e){
            Util.addErrorLog(e);
        }
        return result;
    }

    // Add LongLT 24Aug2016
    public static List<String> listUserNotNotifcation(List<String> listUser, int type) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            List<ObjectId> listId = new ArrayList<>();
            for (int i = 0; i < listUser.size(); i++) {
                listId.add(new ObjectId(listUser.get(i)));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBCursor cur = coll.find(query);
            if (cur != null && cur.size() > 0) {
                while (cur.hasNext()) {
                    DBObject obj = cur.next();
                    if (type == Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI) {
                        Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ);
                        if (check == Constant.FLAG.OFF) {
                            ObjectId oId = (ObjectId) obj.get(UserdbKey.NOTIFICATION_SETTING.ID);
                            result.add(oId.toString());
                        }
                    } else {
                        Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.EAZY_ALERT);
                        if (check == Constant.FLAG.OFF) {
                            ObjectId oId = (ObjectId) obj.get(UserdbKey.NOTIFICATION_SETTING.ID);
                            result.add(oId.toString());
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

    public static List<DBObject> getUserNotificationSettings(List<String> listUser, int type) throws EazyException {
        List<DBObject> result = new ArrayList<>();

        List<ObjectId> listId = new ArrayList<>();
        for (int i = 0; i < listUser.size(); i++) {
            listId.add(new ObjectId(listUser.get(i)));
        }
        DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
        DBCursor cur = coll.find(query);
        if (cur != null && cur.size() > 0) {
            while (cur.hasNext()) {
                DBObject obj = cur.next();
                result.add(obj);
            }
        }

        return result;
    }

    public static boolean checkUserNotification(String userId, int type) throws EazyException {
        boolean result = true;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.NOTIFICATION_SETTING.ID, id);
            BasicDBObject obj = (BasicDBObject) coll.findOne(findObj);
            if (obj != null) {
                
                Integer checkAlert = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.EAZY_ALERT);
                if (checkAlert != null && checkAlert == Constant.FLAG.OFF) {
                    result = false;
                    return result;
                }
                
//                if (type == Constant.NOTIFICATION_TYPE_VALUE.COMMENT_MY_BUZZ_NOTI 
//                        || type == Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI || type == Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT) {
//                    Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ);
//                    if (check != null && check == Constant.FLAG.OFF) {
//                        result = false;
//                    }
//                } else 
                if (type == Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI
                        || type == Constant.NOTIFICATION_TYPE_VALUE.FAVORITED_UNLOCK_NOTI) {
                    Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_LIKE);
                    if (check != null && check == Constant.FLAG.OFF) {
                        result = false;
                    }
                }else if (type == Constant.NOTIFICATION_TYPE_VALUE.CHAT){
                    Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_CHAT);
                    if (check != null && check == -1) {
                        result = false;
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void listUserNotification(List<String> input, int type) throws EazyException {
        try {
            List<ObjectId> listId = new ArrayList<>();
            for (String id : input) {
                listId.add(new ObjectId(id));
            }
            DBObject query = QueryBuilder.start(UserdbKey.NOTIFICATION_SETTING.ID).in(listId).get();
            DBCursor friendCursor = coll.find(query);
            while (friendCursor.hasNext()) {
                DBObject obj = friendCursor.next();
                String id = obj.get(UserdbKey.NOTIFICATION_SETTING.ID).toString();
                Util.addDebugLog("==========UserNotification ID:"+id);
                if (type == Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI) {
                    //               if (type == Constant.LIKE_MY_BUZZ_NOTI || type == Constant.COMMENT_MY_BUZZ_NOTI ) {
                    //noti_buzz user only for favorite
                    //HUNGDT
                    Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ);
                    Util.addDebugLog("==========UserNotification check :"+check);
                    //Long LT approve comment
//                    if (check == Constant.FLAG.OFF) {
//                        input.remove(id);
//                        Util.addDebugLog("==========Remove :"+id);
//                    }
                } else {
                    //kawano
//                     Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.EAZY_ALERT);
//                     if (check == Constant.FLAG.OFF) {
//                         input.remove(id);
//                     }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

}
