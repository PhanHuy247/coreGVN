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
import com.vn.ntsc.backend.entity.impl.log.LogCall;

/**
 *
 * @author RuAc0n
 */
public class LogCallDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.LOG_CALL_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static SizedListData listLog(List<String> listRequestId, List<String> listPartnerId, String fromTime, String toTime,
            Long fromDuration, Long toDuration, Long callType, Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (listRequestId != null) {
                BasicDBObject inObj = new BasicDBObject("$in", listRequestId);
                findObject.append(LogdbKey.LOG_CALL.REQUEST_ID, inObj);
            }
            if (listPartnerId != null) {
                BasicDBObject inObj = new BasicDBObject("$in", listPartnerId);
                findObject.append(LogdbKey.LOG_CALL.PARTNER_ID, inObj);
            }
            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(LogdbKey.LOG_CALL.START_TIME, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_CALL.START_TIME, gte);
                findObject.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObject.append(LogdbKey.LOG_CALL.START_TIME, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObject.append(LogdbKey.LOG_CALL.START_TIME, lte);
                }
            }
            if (fromDuration != null && toDuration != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromDuration);
                BasicDBObject lte = new BasicDBObject("$lte", toDuration);
                ands[0] = new BasicDBObject(LogdbKey.LOG_CALL.DURATION, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_CALL.DURATION, gte);
                findObject.append("$and", ands);
            } else {
                if (fromDuration != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromDuration);
                    findObject.append(LogdbKey.LOG_CALL.DURATION, gte);
                }
                if (toDuration != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toDuration);
                    findObject.append(LogdbKey.LOG_CALL.DURATION, lte);
                }
            }

            if (callType != null) {
                findObject.append(LogdbKey.LOG_CALL.CALL_TYPE, callType);
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.LOG_CALL.START_TIME, or);
            } else if (sort == 2) {
                sortObj.append(LogdbKey.LOG_CALL.DURATION, or);
            }
            DBCursor cursor = null;
            if (findObject.isEmpty()) {
                cursor = coll.find().sort(sortObj);
            } else {
                cursor = coll.find(findObject).sort(sortObj);
            }
            int size = cursor.size();
            if (!isCsv) {
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            }
            List<IEntity> list = new ArrayList<IEntity>();
            while (cursor.hasNext()) {
                LogCall log = new LogCall();
                BasicDBObject dbO = (BasicDBObject) cursor.next();

                String uId = dbO.getString(LogdbKey.LOG_CALL.REQUEST_ID);
                log.reqId = uId;
                String partnerId = dbO.getString(LogdbKey.LOG_CALL.PARTNER_ID);
                log.partnerId = partnerId;
                Integer type = dbO.getInt(LogdbKey.LOG_CALL.CALL_TYPE);
                log.callType = type;
                String time = dbO.getString(LogdbKey.LOG_CALL.START_TIME);
                log.startTime = time;
                String ip = dbO.getString(LogdbKey.LOG_CALL.IP);
                log.ip = ip;
                Integer duration = dbO.getInt(LogdbKey.LOG_CALL.DURATION);
                log.duration = duration.longValue();
                Integer partnerResponsed = dbO.getInt(LogdbKey.LOG_CALL.PARTNER_RESPOND);
                log.partnerResponsed = partnerResponsed;
                Integer finish_type = null;
                if (dbO.getString(LogdbKey.LOG_CALL.FINISH_TYPE) != null) {
                    finish_type = Integer.parseInt(dbO.getString(LogdbKey.LOG_CALL.FINISH_TYPE));
                    log.finishType = finish_type;
                }
                list.add(log);
            }
            result = new SizedListData(size, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
