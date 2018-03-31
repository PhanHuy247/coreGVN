/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl.log;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.pointaction.LogPoint;

/**
 *
 * @author RuAc0n
 */
public class LogPointDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.LOG_POINT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addLog(LogPoint log) throws EazyException {
//        System.out.println("add log "  );
        boolean result = false;
        try {
            BasicDBObject addObj = new BasicDBObject(LogdbKey.LOG_POINT.USER_ID, log.userId);
            addObj.append(LogdbKey.LOG_POINT.TYPE, log.type);
            if(log.partnerId != null)
                addObj.append(LogdbKey.LOG_POINT.PARTNER_ID, log.partnerId);
            if(log.saleType != null)
                addObj.append(LogdbKey.LOG_POINT.SALE_TYPE, log.saleType);
            if(log.freePointType != null)
                addObj.append(LogdbKey.LOG_POINT.FREE_POINT_TYPE, log.freePointType);
            if(log.partnerId != null)
                addObj.append(LogdbKey.LOG_POINT.PARTNER_ID, log.partnerId);
            addObj.append(LogdbKey.LOG_POINT.POINT, log.point);
            addObj.append(LogdbKey.LOG_POINT.BEFORE_POINT, log.beforePoin);
            addObj.append(LogdbKey.LOG_POINT.AFTER_POINT, log.afterPoint); 
            addObj.append(LogdbKey.LOG_POINT.TIME, DateFormat.format(log.time));
            addObj.append(LogdbKey.LOG_POINT.ORIGINAL_TIME, log.time.getTime());
            addObj.append(LogdbKey.LOG_POINT.ADDED_TO_TOTAL, 1);
            if(log.ip != null)
                addObj.append(LogdbKey.LOG_POINT.IP, log.ip);
            coll.insert(addObj);
            if(log.point>0){
                UserDAO.increaseFieldList(log.userId, UserdbKey.USER.TOTAL_POINT, log.point);
            }            
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
}
