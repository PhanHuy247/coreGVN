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
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.constant.ErrorCode;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author RuAc0n
 */
public class LogBlockDAO {

    private static DBCollection coll;
    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.LOG_BLOCK_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
           
        }
    }

    public static boolean addLog(String checkId, String partnerId, Date time, int type,  String ip) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject addObj = new BasicDBObject(LogdbKey.LOG_BLOCK.REQUEST_ID, checkId);
            addObj.append(LogdbKey.LOG_BLOCK.PARTNER_ID, partnerId);
            addObj.append(LogdbKey.LOG_BLOCK.TYPE, type);
            addObj.append(LogdbKey.LOG_BLOCK.TIME, DateFormat.format(time));
            addObj.append(LogdbKey.LOG_BLOCK.IP, ip);
            coll.insert(addObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
