/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.log;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogUserReport;


/**
 *
 * @author DuongLTD
 */
public class ReportUserDAO {

    private static DBCollection coll;
    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.USER_REPORT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static SizedListData listLog(List<String> listRequestId, List<String> listPatnerId,String fromTime, String toTime, Long reportType,
            Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (listRequestId != null) {
                BasicDBObject inObj = new BasicDBObject("$in", listRequestId);
                findObject.append(LogdbKey.USER_REPORT.REPORT_ID, inObj);
            }
            if(listPatnerId != null){
                BasicDBObject inObj = new BasicDBObject("$in", listPatnerId);
                findObject.append(LogdbKey.USER_REPORT.USER_ID, inObj);
            }
            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(LogdbKey.USER_REPORT.REPORT_TIME, lte);
                ands[1] = new BasicDBObject(LogdbKey.USER_REPORT.REPORT_TIME, gte);
                findObject.append("$and", ands);
            }else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObject.append(LogdbKey.USER_REPORT.REPORT_TIME, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObject.append(LogdbKey.USER_REPORT.REPORT_TIME, lte);
                }
            }
            if(reportType != null)
                findObject.append(LogdbKey.USER_REPORT.REPORT_TYPE, reportType);
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.USER_REPORT.REPORT_TIME, or);
            }
            DBCursor cursor = null;
            if (findObject.isEmpty()) {
                cursor = coll.find().sort(sortObj);
            } else {
                cursor = coll.find(findObject).sort(sortObj);
            }
            int size = cursor.size();
            if(!isCsv)
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            List<IEntity> list = new ArrayList<IEntity>();
            while (cursor.hasNext()) {
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String uId = dbO.getString(LogdbKey.USER_REPORT.USER_ID);
                LogUserReport log = new LogUserReport();
                log.partnerId = uId;
                String reqId = dbO.getString(LogdbKey.USER_REPORT.REPORT_ID);
                log.reqId = reqId;
                String time = dbO.getString(LogdbKey.USER_REPORT.REPORT_TIME);
                log.time = time;
                Integer type = dbO.getInt(LogdbKey.USER_REPORT.REPORT_TYPE);
                log.reportType = type;
                String ip = dbO.getString(LogdbKey.USER_REPORT.REPORT_IP);
                log.ip = ip;                    
                list.add(log);                
            }
            result = new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
}
