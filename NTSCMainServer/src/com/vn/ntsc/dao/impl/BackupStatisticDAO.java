/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.Constant;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.Set;
import java.util.TreeSet;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;


/**
 *
 * @author DuongLTD
 */
public class BackupStatisticDAO {

    private static DBCollection coll;

    static{
         try{
             coll = DBLoader.getStatisticDB().getCollection( StatisticdbKey.BACKUP_STATISTIC_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
           
        }
    }   

    public static String updateMonthStatistic(String day, Set<String> activeIosSet, Set<String> activeAndroidSet) throws EazyException {
        String result = null;
        try{
            //serch object
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.BACKUP_STATISTIC.STATISTIC_TYPE, Constant.STATISTIC_USER_TYPE.MONTH_USER_STATISTIC);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBObject updateObj = new BasicDBObject(StatisticdbKey.BACKUP_STATISTIC.LASTEST_DAY, day);
                if(activeIosSet != null && activeAndroidSet != null){
                    BasicDBList activeAndroidList = new BasicDBList();
                    activeAndroidList.addAll(activeAndroidSet);
                    updateObj.append(StatisticdbKey.BACKUP_STATISTIC.ACTIVE_ANDROID_LIST, activeAndroidList);

                    BasicDBList activeIosList = new BasicDBList();
                    activeIosList.addAll(activeIosList);
                    updateObj.append(StatisticdbKey.BACKUP_STATISTIC.ACTIVE_IOS_LIST, activeIosList);
                }
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }else{
                findObj.append(StatisticdbKey.BACKUP_STATISTIC.LASTEST_DAY, day);
                if(activeAndroidSet != null && activeIosSet != null){
                    BasicDBList activeAndroidList = new BasicDBList();
                    activeAndroidList.addAll(activeAndroidSet);
                    findObj.append(StatisticdbKey.BACKUP_STATISTIC.ACTIVE_ANDROID_LIST, activeAndroidList);

                    BasicDBList activeIosList = new BasicDBList();
                    activeIosList.addAll(activeIosList);
                    findObj.append(StatisticdbKey.BACKUP_STATISTIC.ACTIVE_IOS_LIST, activeIosList);
                }
                coll.insert(findObj);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String getLastestDay() throws EazyException {
        String result = null;
        try{
            //serch object
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.BACKUP_STATISTIC.STATISTIC_TYPE, Constant.STATISTIC_USER_TYPE.MONTH_USER_STATISTIC);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                String lastestDay = (String) obj.get(StatisticdbKey.BACKUP_STATISTIC.LASTEST_DAY);
                result = lastestDay;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Set<String> getActiveUserSet(int statisticType, int deviceType) throws EazyException {
        Set<String> result = new TreeSet<>();
        try{
            //serch object
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.BACKUP_STATISTIC.STATISTIC_TYPE, statisticType);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBList list;
                if(deviceType == Constant.DEVICE_TYPE.IOS){
                    list = (BasicDBList) obj.get(StatisticdbKey.BACKUP_STATISTIC.ACTIVE_IOS_LIST);
                }else{
                    list = (BasicDBList) obj.get(StatisticdbKey.BACKUP_STATISTIC.ACTIVE_ANDROID_LIST);
                }
                for (Object list1 : list) {
                    String userId = (String) list1;
                    result.add(userId);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
}
