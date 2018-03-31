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
public class LogCheckOutDAO {

    private static DBCollection coll;
    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.LOG_CHECK_OUT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            //Future logging here
           
        }
    }

    public static boolean addLog(String checkId, String partnerId, Date time, String ip) throws EazyException {
        boolean result = false;
        if (checkId == null || checkId.isEmpty()){
            return result;
        }
        try {
            BasicDBObject addObj = new BasicDBObject(LogdbKey.LOG_CHECK_OUT.REQUEST_ID, checkId);
            addObj.append(LogdbKey.LOG_CHECK_OUT.PARTNER_ID, partnerId);
            addObj.append(LogdbKey.LOG_CHECK_OUT.TIME, DateFormat.format(time));
            addObj.append(LogdbKey.LOG_CHECK_OUT.IP, ip);
            coll.insert(addObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
