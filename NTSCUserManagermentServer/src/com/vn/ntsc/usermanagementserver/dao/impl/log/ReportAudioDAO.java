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
public class ReportAudioDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.AUDIO_REPORT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    
    public static boolean addReportAudio(String audioId, String reporterId, int type, String ip, String userId, Date time,Integer audioType) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(LogdbKey.AUDIO_REPORT.AUDIO_ID, audioId);
            DBCursor cur = coll.find(findObj);
            while(cur.hasNext()){
                BasicDBObject obj = (BasicDBObject) cur.next();
                String rptId = obj.getString(LogdbKey.AUDIO_REPORT.REPORT_ID);
                Integer reportType = obj.getInt(LogdbKey.AUDIO_REPORT.REPORT_TYPE);
                if (rptId.equals(reporterId) && type == reportType) {
                    return false;
                }
            }
            findObj.append(LogdbKey.AUDIO_REPORT.REPORT_ID, reporterId);
            findObj.append(LogdbKey.AUDIO_REPORT.REPORT_TYPE, type);
            findObj.append(LogdbKey.AUDIO_REPORT.REPORT_TIME, DateFormat.format(time));
            findObj.append(LogdbKey.AUDIO_REPORT.REPORT_IP, ip);
            findObj.append(LogdbKey.AUDIO_REPORT.USER_ID, userId);
            findObj.append(LogdbKey.AUDIO_REPORT.AUDIO_TYPE, audioType);
            coll.insert(findObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
