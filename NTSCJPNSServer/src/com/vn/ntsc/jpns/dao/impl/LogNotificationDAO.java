/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import java.util.Date;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class LogNotificationDAO {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = CommonDAO.mongo.getDB(LogdbKey.DB_NAME);
            coll = db.getCollection(LogdbKey.LOG_NOTIFICATION_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static boolean addLog(String toUserId, int type, String fromUserId, Date time, String ip) {
        boolean result = false;
        try {
            BasicDBObject addObj = new BasicDBObject(LogdbKey.LOG_NOTIFICATION.TO_USER_ID, toUserId);
            addObj.append(LogdbKey.LOG_NOTIFICATION.TYPE, type);
            if (fromUserId != null) {
                addObj.append(LogdbKey.LOG_NOTIFICATION.FROM_USER_ID, fromUserId);
            }
            addObj.append(LogdbKey.LOG_NOTIFICATION.TIME, DateFormat.format(time));
            if (ip != null) {
                addObj.append(LogdbKey.LOG_NOTIFICATION.IP, ip);
            }
            coll.insert(addObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return result;
    }

}
