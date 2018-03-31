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
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.TransactionStatistics;

/**
 *
 * @author duyetpt
 */
public class LogSalePointDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.SALE_POINT_LOG_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
        }
    }

    public static void addLog(String userid, int point, double money, int type, Date time) throws EazyException {
        try {
            BasicDBObject update = new BasicDBObject(LogdbKey.SALE_POINT_LOG.USER_ID, userid);
            update.append(LogdbKey.SALE_POINT_LOG.POINT, point)
                    .append(LogdbKey.SALE_POINT_LOG.TYPE, type)
                    .append(LogdbKey.SALE_POINT_LOG.MONEY, money)
                    .append(LogdbKey.SALE_POINT_LOG.TIME, time.getTime());

            coll.insert(update);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void getStatistic(String userId, Date fromDate, Date toDate, TransactionStatistics statistic) throws EazyException {
        try {
            BasicDBObject find = new BasicDBObject();
            BasicDBList ands = new BasicDBList();
            if (userId != null) {
                BasicDBObject userObj = new BasicDBObject(LogdbKey.SALE_POINT_LOG.USER_ID, userId);
                ands.add(userObj);
            }
            if (toDate != null) {
                long toTime = toDate.getTime() + Constant.A_DAY - Constant.JST_TIME_RAW_OFF_SET;
                BasicDBObject gtObj = new BasicDBObject(LogdbKey.SALE_POINT_LOG.TIME, new BasicDBObject("$lt", toTime));
                ands.add(gtObj);
            }
            if (fromDate != null) {
                long fromTime = fromDate.getTime() - Constant.JST_TIME_RAW_OFF_SET;
                BasicDBObject ltObj = new BasicDBObject(LogdbKey.SALE_POINT_LOG.TIME, new BasicDBObject("$gte", fromTime));
                ands.add(ltObj);
            }

            if (!ands.isEmpty()) {
                find.append("$and", ands);
            }
            DBCursor cursor = coll.find(find);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                Integer type  = (Integer) obj.get(LogdbKey.SALE_POINT_LOG.TYPE);
                Double money = (Double) obj.get(LogdbKey.SALE_POINT_LOG.MONEY);
                if(type == Constant.PAYMENT_TYPE.BISCASH){
                    statistic.biscashTimes ++;
                    statistic.biscashMoney += money;
                }else if(type == Constant.PAYMENT_TYPE.CREDIT){
                    statistic.creditTimes ++;
                    statistic.creditMoney += money;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

}

