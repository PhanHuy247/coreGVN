/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.statistic;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.statistic.TransactionStatistics;

/**
 *
 * @author DuongLTD
 */
public class TransactionStatisticDAO {

    private static DBCollection coll;
    private static final String MINUTE_SECOND = "0000";
    private static final String BITCAH_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_MONEY;
    private static final String BITCAH_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_TIMES;

    private static final String CREDIT_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_MONEY;
    private static final String CREDIT_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_TIMES;

    private static final String POINT_BACK_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_MONEY;
    private static final String POINT_BACK_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_TIMES;

    private static final String C_CHECK_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_MONEY;
    private static final String C_CHECK_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_TIMES;

    private static final String CONBONI_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_MONEY;
    private static final String CONBONI_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_TIMES;

    static {
        try {
            coll = DBManager.getStatisticDB().getCollection(StatisticdbKey.TRANSACTION_STATISTIC_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    // LongLT  8/8/2016
    public static boolean update(String day, String hour, double money, int type) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, day);
            BasicDBObject checkerObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.HOUR, hour);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", checkerObj);
            findObj.append(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String moneyField;
                String timeField;
                if (type == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION) {
                    moneyField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.IOS_MONEY;
                    timeField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.IOS_TIMES;
                } else if (type == Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION) {
                    moneyField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_MONEY;
                    timeField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_TIMES;
                } else {
                    moneyField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_MONEY;
                    timeField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_TIMES;
                }
                BasicDBObject updateObj = new BasicDBObject(moneyField, money);
                updateObj.append(timeField, 1);

                BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
                coll.update(findObj, incObj);
            } else {
                BasicDBObject updateQuery = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, day);
                BasicDBObject checkerElement = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.HOUR, hour);
                if (type == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.IOS_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.IOS_TIMES, 1);
                } else if (type == Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_TIMES, 1);
                } else {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_TIMES, 1);
                }
                BasicDBObject checker = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR, checkerElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", checker);
                coll.update(updateQuery, updateCommand, true, false);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    //HUNGDT add
    public static boolean updateNew(String day, String hour, double money, int type) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, day);
            BasicDBObject checkerObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.HOUR, hour);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", checkerObj);
            findObj.append(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR, elemMatch);
            DBObject obj = coll.findOne(findObj);
            Util.addDebugLog("updateNew  " + type);
            if (obj != null) {
                String moneyField = null;
                String timeField = null;
                if (type == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION) {
                    moneyField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.IOS_MONEY;
                    timeField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.IOS_TIMES;
                } else if (type == Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION) {
                    moneyField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_MONEY;
                    timeField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_TIMES;
                } else if (type == 3) {
                    moneyField = BITCAH_MONEY_FIELD;
                    timeField = BITCAH_TIME_FIELD;
                } else if (type == 2) {
                    moneyField = CREDIT_MONEY_FIELD;
                    timeField = CREDIT_TIME_FIELD;
                } else if (type == 5) {
                    moneyField = POINT_BACK_MONEY_FIELD;
                    timeField = POINT_BACK_TIME_FIELD;
                } else if (type == 6) {
                    moneyField = C_CHECK_MONEY_FIELD;
                    timeField = C_CHECK_TIME_FIELD;
                } else if (type == 4) {
                    moneyField = CONBONI_MONEY_FIELD;
                    timeField = CONBONI_TIME_FIELD;
                }
                if (moneyField != null && timeField != null) {
                    BasicDBObject updateObj = new BasicDBObject(moneyField, money);
                    updateObj.append(timeField, 1);

                    BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
                    coll.update(findObj, incObj);
                }
            } else {
                BasicDBObject updateQuery = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, day);
                BasicDBObject checkerElement = new BasicDBObject();
                if (type == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.IOS_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.IOS_TIMES, 1);
                } else if (type == Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_TIMES, 1);
                } else if (type == 3) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_TIMES, 1);
                } else if (type == 2) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_TIMES, 1);
                } else if (type == 5) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_TIMES, 1);
                } else if (type == 6) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_TIMES, 1);
                } else if (type == 4) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_TIMES, 1);
                }
                Util.addDebugLog("updateNew checkerElement " + checkerElement.toString());
                if (!checkerElement.isEmpty()) {
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.HOUR, hour);
                    BasicDBObject checker = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR, checkerElement);
                    BasicDBObject updateCommand = new BasicDBObject("$push", checker);
                    coll.update(updateQuery, updateCommand, true, false);
                }
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

//    public static List<BaseEntity> getStatistic(String month) throws DaoException {
//        List<BaseEntity> result= new ArrayList<BaseEntity>();
//        try{
//            DBObject dbO = coll.findOne(new BasicDBObject(StaticdbKey.TRANSACTION_STATISTIC.MONTH, month));
//            if(dbO != null){
//                BasicDBList listDay = (BasicDBList) dbO.get(StaticdbKey.TRANSACTION_STATISTIC.LIST_DAY);
//                if(listDay != null){
//                    for(int i = 0; i < listDay.size(); i++){
//
//                        TransactionStatistics stObj = new TransactionStatistics();
//                        BasicDBObject obj = (BasicDBObject)listDay.get(i);
//                        String hour = obj.getString(StaticdbKey.TRANSACTION_STATISTIC.DAY);
//                        stObj.time = hour + MINUTE_SECOND;
//                        Double iosMoney = obj.getDouble(StaticdbKey.TRANSACTION_STATISTIC.IOS_MONEY);
//                        stObj.iosMoney = iosMoney;
//                        Double androidMoney = obj.getDouble(StaticdbKey.TRANSACTION_STATISTIC.ANDROID_MONEY);
//                        stObj.androidMoney = androidMoney;
//                        Integer iosTime = obj.getInt(StaticdbKey.TRANSACTION_STATISTIC.IOS_TIMES);
//                        stObj.iosTimes= iosTime;
//                        Integer androidTime = obj.getInt(StaticdbKey.TRANSACTION_STATISTIC.ANDROID_TIMES);
//                        stObj.androidTimes = androidTime;
//                        result.add(stObj);
//                    }
//                }
//            }
//        }catch(MongoException ex){
//            throw new DaoException(ErrorCode.UNKNOWN_ERROR);
//        }catch(Exception ex){
//
//            throw new DaoException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }
    public static List<IEntity> getStatistic(String fromDay, String toDay, long addTime, long fromTime, long toTime) throws EazyException {
        List<IEntity> result = new ArrayList<>();
        try {
            BasicDBObject findObject = new BasicDBObject();
            BasicDBList ands = new BasicDBList();
            BasicDBObject gte = new BasicDBObject("$gte", fromDay);
            BasicDBObject lte = new BasicDBObject("$lte", toDay);
            ands.add(new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, lte));
            ands.add(new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, gte));
            findObject.append("$and", ands);
            BasicDBObject sortObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, 1);
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            int size = cursor.size();
            int count = 0;
            TransactionStatistics stObj = new TransactionStatistics();
            while (cursor.hasNext()) {
                DBObject dbO = cursor.next();
                count++;
                BasicDBList listHour = (BasicDBList) dbO.get(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR);
                if (listHour != null) {
                    for (int i = 0; i < listHour.size(); i++) {
                        DBObject obj = (DBObject) listHour.get(i);

                        String serverHour = (String) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.HOUR) + MINUTE_SECOND;
                        long serverTime = DateFormat.parse(serverHour).getTime();
                        long clientTime = serverTime + addTime;
                        Util.addDebugLog("serverHour " + serverHour + " serverTime " + serverTime + " clientTime " + clientTime);
                        if (clientTime >= fromTime && clientTime <= toTime) {
                            String clientDay = DateFormat.format_yyyyMMdd(new Date(clientTime));
                            Double iosMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.IOS_MONEY);
                            Double androidMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_MONEY);
                            Double amazonMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_MONEY);
                            Integer iosTime = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.IOS_TIMES);
                            Integer androidTime = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_TIMES);
                            Integer amazonTime = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_TIMES);
                            Double biscashMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_MONEY);
                            Double creditMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_MONEY);
                            Double pointBackMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_MONEY);
                            Double cCheckMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_MONEY);
                            Double conboniMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_MONEY);
                            Integer biscashTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_TIMES);
                            Integer creditTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_TIMES);
                            Integer pointBackTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_TIMES);
                            Integer cCheckTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_TIMES);
                            Integer conboniTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_TIMES);
                            Util.addDebugLog("stObj.time " + stObj.time + " clientDay " + clientDay);
                            if (stObj.time == null || !stObj.time.equals(clientDay)) {
                                Util.addDebugLog("stObj.time " + stObj.time + " clientDay " + clientDay);
                                if (stObj.time != null) {
                                    result.add(stObj);
                                }
                                stObj = new TransactionStatistics();

                                stObj.time = clientDay;
                                if (iosMoney != null) {
                                    stObj.iosMoney = iosMoney;
                                }
                                if (androidMoney != null) {
                                    stObj.androidMoney = androidMoney;
                                }
                                if (amazonMoney != null) {
                                    stObj.amazonMoney = amazonMoney;
                                }
                                if (iosTime != null) {
                                    stObj.iosTimes = iosTime;
                                }
                                if (androidTime != null) {
                                    stObj.androidTimes = androidTime;
                                }
                                if (amazonTime != null) {
                                    stObj.amazoneTimes = amazonTime;
                                }
                                if (biscashMoney != null) {
                                    stObj.biscashMoney = biscashMoney;
                                }
                                if (creditMoney != null) {
                                    stObj.creditMoney = creditMoney;
                                }
                                if (pointBackMoney != null) {
                                    stObj.pointBackMoney = pointBackMoney;
                                }
                                if (cCheckMoney != null) {
                                    stObj.cCheckMoney = cCheckMoney;
                                }
                                if (conboniMoney != null) {
                                    stObj.conboniMoney = conboniMoney;
                                }
                                if (biscashTimes != null) {
                                    stObj.biscashTimes = biscashTimes;
                                }
                                if (creditTimes != null) {
                                    stObj.creditTimes = creditTimes;
                                }
                                if (pointBackTimes != null) {
                                    stObj.pointBackTimes = pointBackTimes;
                                }
                                if (cCheckTimes != null) {
                                    stObj.cCheckTimes = cCheckTimes;
                                }
                                if (conboniTimes != null) {
                                    stObj.conboniTimes = conboniTimes;
                                }
                            } else {
                                Util.addDebugLog("stObj.time " + stObj.time + " clientDay " + clientDay);
                                if (iosMoney != null) {
                                    stObj.iosMoney += iosMoney;
                                }
                                if (androidMoney != null) {
                                    stObj.androidMoney += androidMoney;
                                }
                                if (amazonMoney != null) {
                                    stObj.amazonMoney += amazonMoney;
                                }
                                if (iosTime != null) {
                                    stObj.iosTimes += iosTime;
                                }
                                if (androidTime != null) {
                                    stObj.androidTimes += androidTime;
                                }
                                if (amazonTime != null) {
                                    stObj.amazoneTimes += amazonTime;
                                }
                                if (biscashMoney != null) {
                                    stObj.biscashMoney += biscashMoney;
                                }
                                if (creditMoney != null) {
                                    stObj.creditMoney += creditMoney;
                                }
                                if (pointBackMoney != null) {
                                    stObj.pointBackMoney += pointBackMoney;
                                }
                                if (cCheckMoney != null) {
                                    stObj.cCheckMoney += cCheckMoney;
                                }
                                if (conboniMoney != null) {
                                    stObj.conboniMoney += conboniMoney;
                                }
                                if (biscashTimes != null) {
                                    stObj.biscashTimes += biscashTimes;
                                }
                                if (creditTimes != null) {
                                    stObj.creditTimes += creditTimes;
                                }
                                if (pointBackTimes != null) {
                                    stObj.pointBackTimes += pointBackTimes;
                                }
                                if (cCheckTimes != null) {
                                    stObj.cCheckTimes += cCheckTimes;
                                }
                                if (conboniTimes != null) {
                                    stObj.conboniTimes += conboniTimes;
                                }
                            }
                        }
                        if (count == size && i == listHour.size() - 1 && stObj.time != null) {
                            result.add(stObj);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<IEntity> getHourStatistic(String fromDay, String toDay, long addTime, long fromTime, long toTime) throws EazyException {
        List<IEntity> result = new ArrayList<>();
        try {
            BasicDBObject findObject = new BasicDBObject();
            BasicDBList ands = new BasicDBList();
            BasicDBObject gte = new BasicDBObject("$gte", fromDay);
            BasicDBObject lte = new BasicDBObject("$lte", toDay);
            ands.add(new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, lte));
            ands.add(new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, gte));
            findObject.append("$and", ands);
            BasicDBObject sortObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, 1);
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            int size = cursor.size();
            int count = 0;
            TransactionStatistics stObj = new TransactionStatistics();
            while (cursor.hasNext()) {
                DBObject dbO = cursor.next();
                count++;
                BasicDBList listHour = (BasicDBList) dbO.get(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR);
                if (listHour != null) {
                    for (int i = 0; i < listHour.size(); i++) {
                        DBObject obj = (DBObject) listHour.get(i);

                        String serverHour = (String) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.HOUR) + MINUTE_SECOND;
                        long serverTime = DateFormat.parse(serverHour).getTime();
                        long clientTime = serverTime + addTime;
                        Util.addDebugLog("serverHour " + serverHour + " serverTime " + serverTime + " clientTime " + clientTime);
                        if (clientTime >= fromTime && clientTime <= toTime) {
                            String clientDay = DateFormat.format_yyyyMMddHH(new Date(clientTime));
                            Double iosMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.IOS_MONEY);
                            Double androidMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_MONEY);
                            Double amazonMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_MONEY);
                            Integer iosTime = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.IOS_TIMES);
                            Integer androidTime = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_TIMES);
                            Integer amazonTime = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_TIMES);
                            Double biscashMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_MONEY);
                            Double creditMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_MONEY);
                            Double pointBackMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_MONEY);
                            Double cCheckMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_MONEY);
                            Double conboniMoney = (Double) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_MONEY);
                            Integer biscashTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_TIMES);
                            Integer creditTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_TIMES);
                            Integer pointBackTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_TIMES);
                            Integer cCheckTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_TIMES);
                            Integer conboniTimes = (Integer) obj.get(StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_TIMES);
                            if (stObj.time == null || !stObj.time.equals(clientDay)) {
                                Util.addDebugLog("stObj.time1 " + stObj.time + " clientDay1 " + clientDay);
                                if (stObj.time != null) {
                                    result.add(stObj);
                                }
                                stObj = new TransactionStatistics();

                                stObj.time = clientDay;
                                if (iosMoney != null) {
                                    stObj.iosMoney = iosMoney;
                                }
                                if (androidMoney != null) {
                                    stObj.androidMoney = androidMoney;
                                }
                                if (amazonMoney != null) {
                                    stObj.amazonMoney = amazonMoney;
                                }
                                if (iosTime != null) {
                                    stObj.iosTimes = iosTime;
                                }
                                if (androidTime != null) {
                                    stObj.androidTimes = androidTime;
                                }
                                if (amazonTime != null) {
                                    stObj.amazoneTimes = amazonTime;
                                }
                                if (biscashMoney != null) {
                                    stObj.biscashMoney = biscashMoney;
                                }
                                if (creditMoney != null) {
                                    stObj.creditMoney = creditMoney;
                                }
                                if (pointBackMoney != null) {
                                    stObj.pointBackMoney = pointBackMoney;
                                }
                                if (cCheckMoney != null) {
                                    stObj.cCheckMoney = cCheckMoney;
                                }
                                if (conboniMoney != null) {
                                    stObj.conboniMoney = conboniMoney;
                                }
                                if (biscashTimes != null) {
                                    stObj.biscashTimes = biscashTimes;
                                }
                                if (creditTimes != null) {
                                    stObj.creditTimes = creditTimes;
                                }
                                if (pointBackTimes != null) {
                                    stObj.pointBackTimes = pointBackTimes;
                                }
                                if (cCheckTimes != null) {
                                    stObj.cCheckTimes = cCheckTimes;
                                }
                                if (conboniTimes != null) {
                                    stObj.conboniTimes = conboniTimes;
                                }
                            } else {
                                Util.addDebugLog("stObj.time2 " + stObj.time + " clientDay2 " + clientDay);
                                if (iosMoney != null) {
                                    stObj.iosMoney += iosMoney;
                                }
                                if (androidMoney != null) {
                                    stObj.androidMoney += androidMoney;
                                }
                                if (amazonMoney != null) {
                                    stObj.amazonMoney += amazonMoney;
                                }
                                if (iosTime != null) {
                                    stObj.iosTimes += iosTime;
                                }
                                if (androidTime != null) {
                                    stObj.androidTimes += androidTime;
                                }
                                if (amazonTime != null) {
                                    stObj.amazoneTimes += amazonTime;
                                }
                                if (biscashMoney != null) {
                                    stObj.biscashMoney += biscashMoney;
                                }
                                if (creditMoney != null) {
                                    stObj.creditMoney += creditMoney;
                                }
                                if (pointBackMoney != null) {
                                    stObj.pointBackMoney += pointBackMoney;
                                }
                                if (cCheckMoney != null) {
                                    stObj.cCheckMoney += cCheckMoney;
                                }
                                if (conboniMoney != null) {
                                    stObj.conboniMoney += conboniMoney;
                                }
                                if (biscashTimes != null) {
                                    stObj.biscashTimes += biscashTimes;
                                }
                                if (creditTimes != null) {
                                    stObj.creditTimes += creditTimes;
                                }
                                if (pointBackTimes != null) {
                                    stObj.pointBackTimes += pointBackTimes;
                                }
                                if (cCheckTimes != null) {
                                    stObj.cCheckTimes += cCheckTimes;
                                }
                                if (conboniTimes != null) {
                                    stObj.conboniTimes += conboniTimes;
                                }
                            }
                        }
                        if (count == size && i == listHour.size() - 1 && stObj.time != null && clientTime >= fromTime && clientTime <= toTime) {
                            result.add(stObj);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
