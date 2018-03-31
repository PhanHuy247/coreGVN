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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.OnlineAlert;

/**
 *
 * @author RuAc0n
 */
public class OnlineAlertDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.ONLINE_ALERT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static boolean addAlert(String userId, String arlertId, int limitTime, int alertType) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.ONLINE_ALERT.USER_ID, userId);
            findObj.append(UserdbKey.ONLINE_ALERT.ALERT_USER_ID, arlertId);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                BasicDBObject updateObj = new BasicDBObject(UserdbKey.ONLINE_ALERT.MAX_ALERT_NUMBER, limitTime);
                updateObj.append(UserdbKey.ONLINE_ALERT.ALERT_TYPE, alertType);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            } else {
                findObj.append(UserdbKey.ONLINE_ALERT.MAX_ALERT_NUMBER, limitTime);
                findObj.append(UserdbKey.ONLINE_ALERT.ALERT_TYPE, alertType);
                coll.insert(findObj);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static OnlineAlert getOnlineAlert(String userId, String arlertId) throws EazyException {
        OnlineAlert result = new OnlineAlert();
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.ONLINE_ALERT.USER_ID, userId);
            findObj.append(UserdbKey.ONLINE_ALERT.ALERT_USER_ID, arlertId);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                Integer altNum = (Integer) obj.get(UserdbKey.ONLINE_ALERT.MAX_ALERT_NUMBER);
                if (altNum == -1) {
                    result = new OnlineAlert();
                } else {
                    result = new OnlineAlert(altNum, 1);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateAlert(String userId, String arlertId, Date time) throws EazyException {
        boolean result = false;
        try {
            increaseAlertNumber(userId, arlertId);
            String nowDateString = DateFormat.format(time);
            updateAlertTime(userId, arlertId, nowDateString);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateAlertTime(String userId, String arlertId, String dateAlert) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.ONLINE_ALERT.USER_ID, userId);
            findObj.append(UserdbKey.ONLINE_ALERT.ALERT_USER_ID, arlertId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.ONLINE_ALERT.LASTEST_ALERT_DATE, dateAlert);
            BasicDBObject setObject = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean increaseAlertNumber(String userId, String arlertId) throws EazyException {
        boolean result = false;
        try {
            //search by id
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.ONLINE_ALERT.USER_ID, userId);
            updateQuery.append(UserdbKey.ONLINE_ALERT.ALERT_USER_ID, arlertId);
            //update command
            BasicDBObject obj = new BasicDBObject(UserdbKey.ONLINE_ALERT.LASTEST_ALERT_NUMBER, 1);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeAlert(String userId, String arlertId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.ONLINE_ALERT.USER_ID, userId);
            findObj.append(UserdbKey.ONLINE_ALERT.ALERT_USER_ID, arlertId);
            coll.remove(findObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkAlert(String userId, String arlertId) throws EazyException {
        boolean result = false;
        if (arlertId == null || arlertId.isEmpty()){
            return result;
        }
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.ONLINE_ALERT.USER_ID, userId);
            findObj.append(UserdbKey.ONLINE_ALERT.ALERT_USER_ID, arlertId);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                Integer max = (Integer) obj.get(UserdbKey.ONLINE_ALERT.MAX_ALERT_NUMBER);
                if (max != -1) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, Integer> getListAlertNotification(String userId, Date time) throws EazyException {
        Map<String, Integer> result = new TreeMap<String, Integer>();
        try {
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.ONLINE_ALERT.USER_ID, userId);
            DBCursor cur = coll.find(updateQuery);
            String nowDateString = DateFormat.format(time);
            Date now = DateFormat.parse_yyyyMMdd(nowDateString);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Integer type = obj.getInt(UserdbKey.ONLINE_ALERT.ALERT_TYPE);
                String alertId = obj.getString(UserdbKey.ONLINE_ALERT.ALERT_USER_ID);
                Integer max = obj.getInt(UserdbKey.ONLINE_ALERT.MAX_ALERT_NUMBER);
                String lastDateString = obj.getString(UserdbKey.ONLINE_ALERT.LASTEST_ALERT_DATE);
//                if (lastDateString == null) {
//                    result.put(alertId, type);
//                } else {
//                    Date lastDate = DateFormat.parse_yyyyMMdd(lastDateString);
//                    Integer currentTime = obj.getInt(UserdbKey.ONLINE_ALERT.LASTEST_ALERT_NUMBER);
//                    if (currentTime == null) {
//                        currentTime = 0;
//                    }
//                    if (now.after(lastDate) || max == null || max == 0) {
//                        if (now.after(lastDate)) {
//                            currentTime = 0;
//                        }
//                        if (max == null || max == 0 || currentTime < max) {
//                            result.put(alertId, type);
//                        }
//                    } else if (now.compareTo(lastDate) == 0) {
//                        if (currentTime < max) {
//                            result.put(alertId, type);
//                        }
//                    }
//                }
                result.put(alertId, type);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getAlertList(String userId) throws EazyException {
        List<String> result = new ArrayList<String>();
        try {
            BasicDBObject findObj = new BasicDBObject(UserdbKey.ONLINE_ALERT.ALERT_USER_ID, userId);
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                int number = (Integer) obj.get(UserdbKey.ONLINE_ALERT.MAX_ALERT_NUMBER);
                String alertId = (String) obj.get(UserdbKey.ONLINE_ALERT.USER_ID);
                if (number >= 0 ) {
                    result.add(alertId);
                }
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
