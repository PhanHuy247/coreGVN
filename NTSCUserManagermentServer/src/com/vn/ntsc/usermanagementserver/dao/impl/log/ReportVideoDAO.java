/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;

/**
 *
 * @author hoangnh
 */
public class ReportVideoDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.VIDEO_REPORT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    
    public static boolean addReportVideo(String videoId, String reporterId, int type, String ip, String userId, Date time,Integer videoType) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(LogdbKey.VIDEO_REPORT.VIDEO_ID, videoId);
            DBCursor cur = coll.find(findObj);
            while(cur.hasNext()){
                BasicDBObject obj = (BasicDBObject) cur.next();
                String rptId = obj.getString(LogdbKey.VIDEO_REPORT.REPORT_ID);
                Integer reportType = obj.getInt(LogdbKey.VIDEO_REPORT.REPORT_TYPE);
                if (rptId.equals(reporterId) && type == reportType) {
                    return false;
                }
            }
            findObj.append(LogdbKey.VIDEO_REPORT.REPORT_ID, reporterId);
            findObj.append(LogdbKey.VIDEO_REPORT.REPORT_TYPE, type);
            findObj.append(LogdbKey.VIDEO_REPORT.REPORT_TIME, DateFormat.format(time));
            findObj.append(LogdbKey.VIDEO_REPORT.REPORT_IP, ip);
            findObj.append(LogdbKey.VIDEO_REPORT.USER_ID, userId);
            findObj.append(LogdbKey.VIDEO_REPORT.VIDEO_TYPE, videoType);
            coll.insert(findObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
