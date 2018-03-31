/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.jpns.entity.impl.notification.NotificationSetting;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;

/**
 *
 * @author Administrator
 */
public class NotificationSettingDAO {
    private static DBCollection coll;
    private static DB db;
    static {
        try {
            db = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            coll = db.getCollection(UserdbKey.NOTIFICATION_SETTING_COLLECTION);
        } catch (Exception ex) {
            //Future logging here

        }
    }
    
    public static NotificationSetting getNotificationSetting(String userId) {
        Util.addDebugLog("Teset toUserId "+userId);
        NotificationSetting result = null;
        try{
        result = new NotificationSetting();
        BasicDBObject findObj = new BasicDBObject(UserdbKey.NOTIFICATION_SETTING.ID, new ObjectId(userId));
        Util.addDebugLog("Test findObj "+findObj.toString());
        DBObject obj = coll.findOne(findObj);
        if(obj == null){
            Util.addDebugLog("NotificationSetting null $$$$$$$$$$");
            return result;
        }
        else {
            Integer notiCheckOut = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_CHECK_OUT);
            result.notiCheckOut = notiCheckOut;
            Integer notiChat = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_CHAT);
            result.chat = notiChat;
            Integer andGAlert = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.EAZY_ALERT);
            result.andgAlert = andGAlert;
            Integer notiBuzz = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ);
            result.notiBuzz = notiBuzz;
            Integer notiLike = 1;
            if(obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_LIKE) != null) {
               notiLike = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_LIKE);
            }
            result.notiLike = notiLike;
            return result;
        }
        }
        catch(Exception e){
            Util.addErrorLog(e);
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
                
                if (type == Constant.NOTIFICATION_TYPE_VALUE.COMMENT_MY_BUZZ_NOTI 
                        || type == Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI || type == Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT 
                        || type == Constant.NOTIFICATION_TYPE_VALUE.TAG_FROM_BUZZ_NOTI
                        || type == Constant.NOTIFICATION_TYPE_VALUE.SHARE_MUSIC) {
                    Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ);
                    if (check != null && check == Constant.FLAG.OFF) {
                        result = false;
                    }
                }
                if(type == Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI 
                        || type == Constant.NOTIFICATION_TYPE_VALUE.FAVORITED_UNLOCK_NOTI || type == Constant.NOTIFICATION_TYPE_VALUE.NOTI_NEW_LIVESTREAM_FROM_FAVORIST){
                        
                    Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_LIKE);
                    if (check != null && check == Constant.FLAG.OFF) {
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
    
    public static boolean hasNotificationSetting(String userId){
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.NOTIFICATION_SETTING.ID, id);
        BasicDBObject obj = (BasicDBObject) coll.findOne(findObj);
        if(obj == null){
            return false;
        }else{
            return true;
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
}
