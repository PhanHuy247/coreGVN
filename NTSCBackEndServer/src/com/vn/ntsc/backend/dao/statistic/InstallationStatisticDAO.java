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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.statistic.InstallationStatistic;

/**
 *
 * @author DuongLTD
 */
public class InstallationStatisticDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getStatisticDB().getCollection(StatisticdbKey.INSTALLATION_STATISTIC_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static List<InstallationStatistic> getMonthStatistic( long from, long to, long addTime, long fromTime, long toTime) throws EazyException {
        List<InstallationStatistic> result = new ArrayList<>();
        try {
            BasicDBObject findObject = new BasicDBObject();
            BasicDBList ands = new BasicDBList();
            BasicDBObject gte = new BasicDBObject("$gte", from);
            BasicDBObject lte = new BasicDBObject("$lte", to);
            ands.add(new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.HOUR, lte));
            ands.add(new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.HOUR, gte));
            findObject.append("$and", ands);
            BasicDBObject sortObj = new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.HOUR, 1);
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            int size = cursor.size();
            int count = 0;
            InstallationStatistic stObj = new InstallationStatistic();
            Set<String> iosUnique = new HashSet<>();
            Set<String> androidUnique = new HashSet<>();
            while (cursor.hasNext()) {
                DBObject dbO = cursor.next();
                count++;

                long serverTime = (long) dbO.get(StatisticdbKey.INSTALLATION_STATISTIC.HOUR);
                long clientTime = serverTime + addTime;
                if (clientTime >= fromTime && clientTime <= toTime) {
                    String clientDay = DateFormat.format_yyyyMMdd(new Date(clientTime));
                    if (stObj.time == null || !stObj.time.equals(clientDay)) {
                        if (stObj.time != null) {
                            stObj.totalTime = stObj.androidInstallationTimes + stObj.iosInstallationTimes;
                            stObj.totalUniqueNumber = stObj.androidUniqueNumber + stObj.androidUniqueNumber;
                            result.add(stObj);
                        }

                        stObj = new InstallationStatistic();
                        iosUnique.clear();
                        androidUnique.clear();

                        stObj.time = clientDay;

                    }
                    Integer uniqueNumberType = (Integer) dbO.get(StatisticdbKey.INSTALLATION_STATISTIC.UNIQUE_NUMBER_TYPE);
                    Integer type = (Integer) dbO.get(StatisticdbKey.INSTALLATION_STATISTIC.DEVICE_TYPE);
                    String unique = (String) dbO.get(StatisticdbKey.INSTALLATION_STATISTIC.UNIQUE_NUMBER);
                    if (type == Constant.DEVICE_TYPE.IOS) {
                        stObj.iosInstallationTimes++;
                        if(uniqueNumberType == null  || uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.NEW){
                            if(unique != null && !iosUnique.contains(unique)){
                                stObj.iosUniqueNumber ++;
                                iosUnique.add(unique);
                            }
                        }
                        if(uniqueNumberType != null){
                            if(uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.EMPTY){
                                stObj.blankIosUniqueNumber ++;
                            }else if(uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.DUPLICATE){
                                stObj.duplicateIosUniqueNumber ++;
                            }
                        }
                    } else {
                        stObj.androidInstallationTimes++;
                        if(uniqueNumberType == null  || uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.NEW){
                            if(unique != null && !androidUnique.contains(unique)){
                                stObj.androidUniqueNumber ++;
                                androidUnique.add(unique);
                            }
                        }
                        if(uniqueNumberType != null){
                            if(uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.EMPTY){
                                stObj.blankAndroidUniqueNumber ++;
                            }else if(uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.DUPLICATE){
                                stObj.duplicateAndroidUniqueNumber ++;
                            }
                        }
                    }
                }
                if (count == size && stObj.time != null) {
                    stObj.totalTime = stObj.androidInstallationTimes + stObj.iosInstallationTimes;
                    stObj.totalUniqueNumber = stObj.androidUniqueNumber + stObj.androidUniqueNumber;
                    result.add(stObj);
                }

            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<InstallationStatistic> getDayStatistic(long from, long to, long addTime, long fromTime, long toTime) throws EazyException {
        List<InstallationStatistic> result = new ArrayList<>();
        try {
            BasicDBObject findObject = new BasicDBObject();
            BasicDBList ands = new BasicDBList();
            BasicDBObject gte = new BasicDBObject("$gte", from);
            BasicDBObject lte = new BasicDBObject("$lte", to);
            ands.add(new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.HOUR, lte));
            ands.add(new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.HOUR, gte));
            findObject.append("$and", ands);
            BasicDBObject sortObj = new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.HOUR, 1);
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            int size = cursor.size();
            int count = 0;
            InstallationStatistic stObj = new InstallationStatistic();
            Set<String> iosUnique = new HashSet<>();
            Set<String> androidUnique = new HashSet<>();
            while (cursor.hasNext()) {
                DBObject dbO = cursor.next();
                count++;

                long serverTime = (long) dbO.get(StatisticdbKey.INSTALLATION_STATISTIC.HOUR);
                long clientTime = serverTime + addTime;
                if (clientTime >= fromTime && clientTime <= toTime) {
                    String clientDay = DateFormat.format_yyyyMMddHH(new Date(clientTime));
                    if (stObj.time == null || !stObj.time.equals(clientDay)) {
                        if (stObj.time != null) {
                            stObj.totalTime = stObj.androidInstallationTimes + stObj.iosInstallationTimes;
                            stObj.totalUniqueNumber = stObj.androidUniqueNumber + stObj.androidUniqueNumber;
                            result.add(stObj);
                        }

                        stObj = new InstallationStatistic();
                        iosUnique.clear();
                        androidUnique.clear();
                        
                        stObj.time = clientDay;
                    }
                    Integer uniqueNumberType = (Integer) dbO.get(StatisticdbKey.INSTALLATION_STATISTIC.UNIQUE_NUMBER_TYPE);
                    Integer type = (Integer) dbO.get(StatisticdbKey.INSTALLATION_STATISTIC.DEVICE_TYPE);
                    String unique = (String) dbO.get(StatisticdbKey.INSTALLATION_STATISTIC.UNIQUE_NUMBER);
                    if (type == Constant.DEVICE_TYPE.IOS) {
                        stObj.iosInstallationTimes++;
                        if(uniqueNumberType == null  || uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.NEW){
                            if(unique != null && !iosUnique.contains(unique)){
                                stObj.iosUniqueNumber ++;
                                iosUnique.add(unique);
                            }
                        }
                        if(uniqueNumberType != null){
                            if(uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.EMPTY){
                                stObj.blankIosUniqueNumber ++;
                            }else if(uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.DUPLICATE){
                                stObj.duplicateIosUniqueNumber ++;
                            }
                        }
                    } else {
                        stObj.androidInstallationTimes++;
                        if(uniqueNumberType == null  || uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.NEW){
                            if(unique != null && !androidUnique.contains(unique)){
                                stObj.androidUniqueNumber ++;
                                androidUnique.add(unique);
                            }
                        }
                        if(uniqueNumberType != null){
                            if(uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.EMPTY){
                                stObj.blankAndroidUniqueNumber ++;
                            }else if(uniqueNumberType == Constant.UNIQUE_NUMBER_TYPE.DUPLICATE){
                                stObj.duplicateAndroidUniqueNumber ++;
                            }
                        }
                    }
                }
                if (count == size && stObj.time != null) {
                    stObj.totalTime = stObj.androidInstallationTimes + stObj.iosInstallationTimes;
                    stObj.totalUniqueNumber = stObj.androidUniqueNumber + stObj.androidUniqueNumber;
                    result.add(stObj);
                }

            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
