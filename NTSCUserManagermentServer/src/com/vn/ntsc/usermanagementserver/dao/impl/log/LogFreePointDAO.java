/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl.log;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LogFreePointData;

/**
 *
 * @author duyetpt
 */
public class LogFreePointDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.FREE_POINT_LOG_COLLECTION);
            
            coll.createIndex(new BasicDBObject(LogdbKey.FREE_POINT_LOG.USER_ID, 1));
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
        }
    }

    public static void addLog(String userid, int point, int type_id, Date time) throws EazyException {
        try {
            BasicDBObject update = new BasicDBObject(LogdbKey.FREE_POINT_LOG.USER_ID, userid);
            update.append(LogdbKey.FREE_POINT_LOG.POINT, point)
                    .append(LogdbKey.FREE_POINT_LOG.TYPE_ID, type_id)
                    .append(LogdbKey.FREE_POINT_LOG.TIME, time.getTime());

            coll.insert(update);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static List<LogFreePointData> getListLog(String userId, Date fromDate, Date toDate) throws EazyException {
        List<LogFreePointData> list = new ArrayList<LogFreePointData>();
        try {
            BasicDBObject find = new BasicDBObject();
            BasicDBList ands = new BasicDBList();
            if (userId != null) {
                find = new BasicDBObject(LogdbKey.FREE_POINT_LOG.USER_ID, userId);
            }
//            if (type_id != null) {
//                BasicDBObject typeObj = new BasicDBObject(LogdbKey.FREE_POINT_LOG.TYPE_ID, type_id);
//                ands.add(typeObj);
//            }
            if (toDate != null) {
                long toTime = toDate.getTime() + Constant.A_DAY - Constant.JST_TIME_RAW_OFF_SET;
                Date d = new Date(toTime);
                BasicDBObject gtObj = new BasicDBObject(LogdbKey.FREE_POINT_LOG.TIME, new BasicDBObject("$lt", toTime));
                ands.add(gtObj);
            }
            if (fromDate != null) {
                long fromTime = fromDate.getTime() - Constant.JST_TIME_RAW_OFF_SET;
                Date d = new Date(fromTime);
                BasicDBObject ltObj = new BasicDBObject(LogdbKey.FREE_POINT_LOG.TIME, new BasicDBObject("$gte", fromTime));
                ands.add(ltObj);
            }

            if (!ands.isEmpty()) {
                find.append("$and", ands);
            }
            DBCursor cursor = coll.find(find).sort(new BasicDBObject(LogdbKey.FREE_POINT_LOG.TIME, -1));
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                LogFreePointData log = new LogFreePointData();
                log.userId = (String) obj.get(LogdbKey.FREE_POINT_LOG.USER_ID);
                log.typeId = (Integer) obj.get(LogdbKey.FREE_POINT_LOG.TYPE_ID);
                log.point = (Integer) obj.get(LogdbKey.FREE_POINT_LOG.POINT);
                long time = (Long) obj.get(LogdbKey.FREE_POINT_LOG.TIME) + Constant.JST_TIME_RAW_OFF_SET;
                log.time = DateFormat.format(time);
                list.add(log);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return list;
    }
}

