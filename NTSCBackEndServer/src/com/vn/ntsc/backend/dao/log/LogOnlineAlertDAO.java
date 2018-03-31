/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
import com.vn.ntsc.backend.entity.impl.log.LogOnlineAlert;

/**
 *
 * @author RuAc0n
 */
public class LogOnlineAlertDAO {

    private static DBCollection coll;
    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.LOG_ONLINE_ALERT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static SizedListData listLog(List<String> listCheckId, List<String> listPartnerId, String fromTime, String toTime,
            Long type, Long frequency, Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (type != null) {
                findObject.append(LogdbKey.LOG_ONLINE_ALERT.ALERT_TYPE, type.intValue());
            }
            if (frequency != null) {
                findObject.append(LogdbKey.LOG_ONLINE_ALERT.ALERT_FREQUENCY, frequency.intValue());
            }
            if (listCheckId != null) {
                BasicDBObject inObj = new BasicDBObject("$in", listCheckId);
                findObject.append(LogdbKey.LOG_ONLINE_ALERT.REQUEST_ID, inObj);
            }
            if (listPartnerId != null) {
                BasicDBObject inObj = new BasicDBObject("$in", listPartnerId);
                findObject.append(LogdbKey.LOG_ONLINE_ALERT.PARTNER_ID, inObj);
            }
            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(LogdbKey.LOG_ONLINE_ALERT.TIME, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_ONLINE_ALERT.TIME, gte);
                findObject.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObject.append(LogdbKey.LOG_ONLINE_ALERT.TIME, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObject.append(LogdbKey.LOG_ONLINE_ALERT.TIME, lte);
                }
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.LOG_ONLINE_ALERT.TIME, or);
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
                LogOnlineAlert log = new LogOnlineAlert();
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String uId = dbO.getString(LogdbKey.LOG_ONLINE_ALERT.REQUEST_ID);
                log.reqId = uId;
                Integer tpe = dbO.getInt(LogdbKey.LOG_ONLINE_ALERT.ALERT_TYPE);
                log.alertType = tpe;
                Integer fre = dbO.getInt(LogdbKey.LOG_ONLINE_ALERT.ALERT_FREQUENCY);
                log.alertFrequency = fre;
                String partnerId = dbO.getString(LogdbKey.LOG_ONLINE_ALERT.PARTNER_ID);
                log.partnerId = partnerId;
                String time = dbO.getString(LogdbKey.LOG_ONLINE_ALERT.TIME);
                log.time = time;
                String ip = dbO.getString(LogdbKey.LOG_ONLINE_ALERT.IP);
                log.ip = ip;

                list.add(log);
            }
            result= new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
