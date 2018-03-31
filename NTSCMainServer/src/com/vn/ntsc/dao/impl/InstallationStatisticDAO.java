/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author DuongLTD
 */
public class InstallationStatisticDAO {

    private static DBCollection coll;


    static{
         try{
             coll = DBLoader.getStatisticDB().getCollection( StatisticdbKey.INSTALLATION_STATISTIC_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean insert(long hour , int type, String uniqueNumber, int uniqueNumberType) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject insertObject = new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.HOUR, hour);
            insertObject.append(StatisticdbKey.INSTALLATION_STATISTIC.DEVICE_TYPE, type);
            insertObject.append(StatisticdbKey.INSTALLATION_STATISTIC.UNIQUE_NUMBER_TYPE, uniqueNumberType);
            if(uniqueNumber != null && !uniqueNumber.isEmpty())
                insertObject.append(StatisticdbKey.INSTALLATION_STATISTIC.UNIQUE_NUMBER, uniqueNumber);
            coll.insert(insertObject);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean isUniqueNumberExists(int type, String uniqueNumber) throws EazyException {
        if(uniqueNumber == null || uniqueNumber.isEmpty())
            return true;
        try{
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.DEVICE_TYPE, type);
            findObj.append(StatisticdbKey.INSTALLATION_STATISTIC.UNIQUE_NUMBER, uniqueNumber);
            DBObject obj = coll.findOne(findObj);
            return obj != null;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    public static int getNumberUniqueNumber(int type, String uniqueNumber) throws EazyException {
        try{
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.DEVICE_TYPE, type);
            findObj.append(StatisticdbKey.INSTALLATION_STATISTIC.UNIQUE_NUMBER, uniqueNumber);
            return coll.find().count();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
     
    private static final BasicDBObject sortByHourDesc = new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.HOUR, -1);
    
    public static long getTimeUniqueInstallApplication(int type, String uniqueNumber) throws EazyException {
        try{
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.INSTALLATION_STATISTIC.DEVICE_TYPE, type);
            findObj.append(StatisticdbKey.INSTALLATION_STATISTIC.UNIQUE_NUMBER, uniqueNumber);
            DBCursor cursor = coll.find(findObj).sort(sortByHourDesc).limit(1);
            if(cursor.hasNext()){
                DBObject obj = cursor.next();
                if(obj != null){
                    Long time =  (Long) obj.get(StatisticdbKey.INSTALLATION_STATISTIC.HOUR);
                    return time == null ? Util.currentTime() : time;
                }else{
                    return Util.currentTime();
                }
            }
            return Util.currentTime();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
     
    
}
