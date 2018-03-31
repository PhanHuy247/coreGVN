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
public class ReportImageDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.IMAGE_REPORT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addReportImage(String imageId, String reporterId, int type, int imgType, String ip, String userId, Date time) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(LogdbKey.IMAGE_REPORT.IMAGE_ID, imageId);
            DBCursor cur = coll.find(findObj);
            while(cur.hasNext()){
                BasicDBObject obj = (BasicDBObject) cur.next();
                String rptId = obj.getString(LogdbKey.IMAGE_REPORT.REPORT_ID);
                Integer reportType = obj.getInt(LogdbKey.IMAGE_REPORT.REPORT_TYPE);
                if (rptId.equals(reporterId) && type == reportType) {
                            return false;
                }
            }
            findObj.append(LogdbKey.IMAGE_REPORT.REPORT_ID, reporterId);
            findObj.append(LogdbKey.IMAGE_REPORT.REPORT_TYPE, type);
            findObj.append(LogdbKey.IMAGE_REPORT.REPORT_TIME, DateFormat.format(time));
            findObj.append(LogdbKey.IMAGE_REPORT.REPORT_IP, ip);
            findObj.append(LogdbKey.IMAGE_REPORT.USER_ID, userId);
            findObj.append(LogdbKey.IMAGE_REPORT.IMAGE_TYPE, imgType);
            coll.insert(findObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
