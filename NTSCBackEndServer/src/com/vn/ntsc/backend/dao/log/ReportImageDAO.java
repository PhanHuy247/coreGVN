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
import com.vn.ntsc.backend.entity.impl.log.LogImageReport;

/**
 *
 * @author DuongLTD
 */
public class ReportImageDAO {

    private static DBCollection coll;
    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.IMAGE_REPORT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static SizedListData listLog(String imageId, List<String> listRequestId, List<String> listPartnerId, String fromTime, String toTime, Long reportType, Long imageType,
            Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if(imageId != null){
                findObject.append(LogdbKey.IMAGE_REPORT.IMAGE_ID, imageId);
            }else{ 
                if (listRequestId != null) {
                    BasicDBObject inObj = new BasicDBObject("$in", listRequestId);
                    findObject.append(LogdbKey.IMAGE_REPORT.REPORT_ID, inObj);
                }
                if (listPartnerId != null) {
                    BasicDBObject inObj = new BasicDBObject("$in", listPartnerId);
                    findObject.append(LogdbKey.IMAGE_REPORT.USER_ID, inObj);
                }
                if (fromTime != null && toTime != null) {
                    BasicDBObject[] ands = new BasicDBObject[2];
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    BasicDBObject lte = new BasicDBObject("$lt", toTime);
                    ands[0] = new BasicDBObject(LogdbKey.IMAGE_REPORT.REPORT_TIME, lte);
                    ands[1] = new BasicDBObject(LogdbKey.IMAGE_REPORT.REPORT_TIME, gte);
                    findObject.append("$and", ands);
                } else {
                    if (fromTime != null) {
                        BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                        findObject.append(LogdbKey.IMAGE_REPORT.REPORT_TIME, gte);
                    }
                    if (toTime != null) {
                        BasicDBObject lte = new BasicDBObject("$lte", toTime);
                        findObject.append(LogdbKey.IMAGE_REPORT.REPORT_TIME, lte);
                    }
                }
                if(reportType != null)
                    findObject.append(LogdbKey.IMAGE_REPORT.REPORT_TYPE, reportType);
                if(imageType != null)
                    findObject.append(LogdbKey.IMAGE_REPORT.IMAGE_TYPE, imageType);
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.IMAGE_REPORT.REPORT_TIME, or);
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
                String iId = dbO.getString(LogdbKey.IMAGE_REPORT.IMAGE_ID);
                String partnerIdId = (String)dbO.getString(LogdbKey.IMAGE_REPORT.USER_ID);
                LogImageReport log = new LogImageReport();
                log.imageId = iId;
                log.partnerId = partnerIdId;
                String reqId = dbO.getString(LogdbKey.IMAGE_REPORT.REPORT_ID);
                log.reqId = reqId;
                String time = dbO.getString(LogdbKey.IMAGE_REPORT.REPORT_TIME);
                log.time = time;
                Integer type = dbO.getInt(LogdbKey.IMAGE_REPORT.REPORT_TYPE);
                log.reportType = type;
                String ip = dbO.getString(LogdbKey.IMAGE_REPORT.REPORT_IP);
                log.ip = ip;           
                Long tpe = dbO.getLong(LogdbKey.IMAGE_REPORT.IMAGE_TYPE);
                log.imageType = tpe;
                
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
