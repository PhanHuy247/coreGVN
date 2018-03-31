/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl.log;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.Date;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.constant.ErrorCode;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author DuongLTD
 */
public class ReportUserDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.USER_REPORT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addReportUser(String userId, String reporterId, int type, String ip, Date time) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(LogdbKey.USER_REPORT.USER_ID, userId);
            DBCursor cur = coll.find(findObj);
            while(cur.hasNext()){
                BasicDBObject obj = (BasicDBObject) cur.next();
                String rptId = obj.getString(LogdbKey.USER_REPORT.REPORT_ID);
                Integer reportType = obj.getInt(LogdbKey.USER_REPORT.REPORT_TYPE);
                if (rptId.equals(reporterId) && type == reportType) {
                            return false;
                }
            }
            findObj.append(LogdbKey.USER_REPORT.REPORT_ID, reporterId);
            findObj.append(LogdbKey.USER_REPORT.REPORT_TYPE, type);
            findObj.append(LogdbKey.USER_REPORT.REPORT_TIME, DateFormat.format(time));
            findObj.append(LogdbKey.USER_REPORT.REPORT_IP, ip);
            coll.insert(findObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
