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
public class LogDeactivateDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.LOG_DEACTIVATE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addLog(String userId, String comment, Date time, String ip) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject addObj = new BasicDBObject(LogdbKey.LOG_DEACTIVATE.USER_ID, userId);
            addObj.append(LogdbKey.LOG_LOGIN.TIME, DateFormat.format(time));
            addObj.append(LogdbKey.LOG_LOGIN.IP, ip);
            addObj.append(LogdbKey.LOG_DEACTIVATE.COMMENT, comment);
            coll.insert(addObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
