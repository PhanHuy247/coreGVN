/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

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
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author hungdt
 */
public class NotificationSettingDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getUserDB().getCollection(UserdbKey.NOTIFICATION_SETTING_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
        }
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
                        || type == Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI || type == Constant.NOTIFICATION_TYPE_VALUE.REPLY_COMMENT) {
                    Integer check = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ);
                    if (check != null && check == Constant.FLAG.OFF) {
                        result = false;
                    }
                }
                if(type == Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI 
                        || type == Constant.NOTIFICATION_TYPE_VALUE.FAVORITED_UNLOCK_NOTI){
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

}
