/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.dao.statistic;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.statistic.UserStatistics;

/**
 *
 * @author DuongLTD
 */
public class UserMonthStatisticDAO {

    private static DBCollection coll;

    static{
         try{
             coll = DBManager.getStatisticDB().getCollection( StatisticdbKey.USER_MONTH_STATISTIC_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static boolean update(String month, String day,int onlineIos, int onlineAndroid, int newNumberIos, int newNumberAndroid,  
            int activeUserIos, int activeUserAndroid, int totalNumber) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.USER_MONTH_STATISTIC.MONTH, month);
            BasicDBObject dayObj = new BasicDBObject(StatisticdbKey.USER_MONTH_STATISTIC.DAY, day);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", dayObj);
            findObj.append(StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if(obj == null){
                BasicDBObject query = new BasicDBObject(StatisticdbKey.USER_MONTH_STATISTIC.MONTH, month);
                BasicDBObject updateObj = new BasicDBObject(StatisticdbKey.USER_MONTH_STATISTIC.DAY, day);
                updateObj.append(StatisticdbKey.USER_MONTH_STATISTIC.ONLINE_USER_IOS, onlineIos);
                updateObj.append(StatisticdbKey.USER_MONTH_STATISTIC.ONLINE_USER_ANDROID, onlineAndroid);
                updateObj.append(StatisticdbKey.USER_MONTH_STATISTIC.ACTIVE_USER_IOS, activeUserIos);
                updateObj.append(StatisticdbKey.USER_MONTH_STATISTIC.NEW_USER_IOS, newNumberIos);
                updateObj.append(StatisticdbKey.USER_MONTH_STATISTIC.TOTAL_USER, totalNumber);
                updateObj.append(StatisticdbKey.USER_MONTH_STATISTIC.ACTIVE_USER_ANDROID, activeUserAndroid);
                updateObj.append(StatisticdbKey.USER_MONTH_STATISTIC.NEW_USER_ANDROID, newNumberAndroid);
                BasicDBObject dObj = new BasicDBObject(StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY, updateObj);
                BasicDBObject pushObj = new BasicDBObject("$push", dObj);
                coll.update(query, pushObj, true, false);
            }else{
                
                String fieldOnlineIos = StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY + ".$." + StatisticdbKey.USER_MONTH_STATISTIC.ONLINE_USER_IOS;
                String fieldOnlineAndroid = StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY + ".$." + StatisticdbKey.USER_MONTH_STATISTIC.ONLINE_USER_ANDROID;
                
                String fieldActiveIos = StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY + ".$." + StatisticdbKey.USER_MONTH_STATISTIC.ACTIVE_USER_IOS;
                String fieldActiveAndroid = StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY + ".$." + StatisticdbKey.USER_MONTH_STATISTIC.ACTIVE_USER_ANDROID;
                
                String fieldNewIos = StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY + ".$." + StatisticdbKey.USER_MONTH_STATISTIC.NEW_USER_IOS;
                String fieldNewAndroid = StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY + ".$." + StatisticdbKey.USER_MONTH_STATISTIC.NEW_USER_ANDROID;
                
                String fieldTotalMale = StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY + ".$." + StatisticdbKey.USER_MONTH_STATISTIC.TOTAL_USER;
                
                BasicDBObject updateObj = new BasicDBObject(fieldActiveIos,activeUserIos);
                updateObj.append(fieldActiveAndroid, activeUserAndroid);
                updateObj.append(fieldNewIos, newNumberIos);
                updateObj.append(fieldNewAndroid, newNumberAndroid);
                updateObj.append(fieldOnlineIos, onlineIos);
                updateObj.append(fieldOnlineAndroid, onlineAndroid);           
                updateObj.append(fieldTotalMale, totalNumber);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                
                coll.update(findObj, setObj);
                
            }
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<IEntity> getStatistic(String month) throws EazyException {
        List<IEntity> result= new ArrayList<IEntity>();
        try{
            DBObject query = new BasicDBObject(StatisticdbKey.USER_MONTH_STATISTIC.MONTH, month);
            DBObject dbO = coll.findOne(query);
            if(dbO != null){
                BasicDBList listDay = (BasicDBList) dbO.get(StatisticdbKey.USER_MONTH_STATISTIC.LIST_DAY);
                if(listDay != null){
                    for(int i = 0; i < listDay.size(); i++){
                        
                        UserStatistics stObj = new UserStatistics();
                        BasicDBObject obj = (BasicDBObject) listDay.get(i);
                        String day = obj.getString(StatisticdbKey.USER_MONTH_STATISTIC.DAY);
                        stObj.time = day;
                        Integer onlineIos = obj.getInt(StatisticdbKey.USER_MONTH_STATISTIC.ONLINE_USER_IOS);
                        stObj.onlineNumberIos = onlineIos;
                        Integer onlineAndroid = obj.getInt(StatisticdbKey.USER_MONTH_STATISTIC.ONLINE_USER_ANDROID);
                        stObj.onlineNumberAndroid = onlineAndroid;
                        Integer newUserIos = obj.getInt(StatisticdbKey.USER_MONTH_STATISTIC.NEW_USER_IOS);
                        stObj.newUserIosNumber = newUserIos;
                        Integer newUserAndroid = obj.getInt(StatisticdbKey.USER_MONTH_STATISTIC.NEW_USER_ANDROID);
                        stObj.newUserAndroidNumber = newUserAndroid;
                        Integer actNumIos = obj.getInt(StatisticdbKey.USER_MONTH_STATISTIC.ACTIVE_USER_IOS);
                        stObj.activeUserIosNumber = actNumIos;
                        Integer actNumAndroid = obj.getInt(StatisticdbKey.USER_MONTH_STATISTIC.ACTIVE_USER_ANDROID);
                        stObj.activeUserAndroidNumber = actNumAndroid;
                        Integer total = obj.getInt(StatisticdbKey.USER_MONTH_STATISTIC.TOTAL_USER);
                        stObj.totalUserNumber = total;
                        
                        result.add(stObj);
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
}
