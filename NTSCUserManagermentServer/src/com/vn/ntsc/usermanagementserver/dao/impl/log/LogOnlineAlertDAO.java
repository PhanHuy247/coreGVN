/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl.log;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author RuAc0n
 */
public class LogOnlineAlertDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.LOG_ONLINE_ALERT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
           
        }
    }

    public static boolean addLog(String checkId, String partnerId, Date time, int alertType, int alertFre, String ip) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject addObj = new BasicDBObject(LogdbKey.LOG_ONLINE_ALERT.REQUEST_ID, checkId);
            addObj.append(LogdbKey.LOG_ONLINE_ALERT.PARTNER_ID, partnerId);
            addObj.append(LogdbKey.LOG_ONLINE_ALERT.ALERT_TYPE, alertType);
            addObj.append(LogdbKey.LOG_ONLINE_ALERT.ALERT_FREQUENCY, alertFre);
            addObj.append(LogdbKey.LOG_ONLINE_ALERT.TIME, DateFormat.format(time));
            addObj.append(LogdbKey.LOG_ONLINE_ALERT.IP, ip);
            coll.insert(addObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
