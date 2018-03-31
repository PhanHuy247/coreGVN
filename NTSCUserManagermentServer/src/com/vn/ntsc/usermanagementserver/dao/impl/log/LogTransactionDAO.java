/*
 * To change this template, choose Tools | Templates
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
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.TransactionStatistics;

/**
 *
 * @author RuAc0n
 */
public class LogTransactionDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getCashDB().getCollection(CashdbKey.TRANSACTION_LOG_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void getStatistic(String userId, Date fromDate, Date toDate, TransactionStatistics statistic) throws EazyException {
        try {
            BasicDBObject find = new BasicDBObject();
            BasicDBList ands = new BasicDBList();
            if (userId != null) {
                BasicDBObject userObj = new BasicDBObject(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
                ands.add(userObj);
            }
            if (toDate != null) {
                long toTime = toDate.getTime() + Constant.A_DAY - Constant.JST_TIME_RAW_OFF_SET;
                BasicDBObject gtObj = new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, new BasicDBObject("$lt", toTime));
                ands.add(gtObj);
            }
            if (fromDate != null) {
                long fromTime = fromDate.getTime() - Constant.JST_TIME_RAW_OFF_SET;
                BasicDBObject ltObj = new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, new BasicDBObject("$gte", fromTime));
                ands.add(ltObj);
            }

            if (!ands.isEmpty()) {
                find.append("$and", ands);
            }
            DBCursor cursor = coll.find(find);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                Integer type = (Integer) obj.get(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE);
                Double money = (Double) obj.get(CashdbKey.TRANSACTION_LOG.PRICE);
                if (type == Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION) {
                    statistic.androidTimes++;
                    statistic.androidMoney += money;
                } else {
                    statistic.iosTimes++;
                    statistic.iosMoney += money;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    //ThanhĐ add
    public static double getTotalPrice(String userId) throws EazyException {
        double result = 0;
        try {
            DBObject findObject = new BasicDBObject(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            DBObject sortObj = new BasicDBObject(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE, -1);
            DBCursor cur = coll.find(findObject).sort(sortObj);
            if (cur != null && cur.size() != 0) {
                DBObject obj = cur.next();
                Double totalPrice = (Double) obj.get(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE);
                if (totalPrice != null) {
                    result = totalPrice;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    // LongLT 8/8/2016
    public static boolean addTransaction(String userId, String time, int point, double price, double totalMoney,
            int productionType, long timePurchase, String ip) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject insertObj = new BasicDBObject();
            insertObj.append(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE, totalMoney);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE, productionType);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRICE, price);
            insertObj.append(CashdbKey.TRANSACTION_LOG.POINT, point);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TIME, time);
            insertObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, true);
            insertObj.append(CashdbKey.TRANSACTION_LOG.ADD_BY_ADMIN_FLAG, false);
            //insertObj.append(CashdbKey.TRANSACTION_LOG.ADD_BY_ADMIN_FLAG, addByAdmin);

            insertObj.append(CashdbKey.TRANSACTION_LOG.IP, ip);
            coll.insert(insertObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
