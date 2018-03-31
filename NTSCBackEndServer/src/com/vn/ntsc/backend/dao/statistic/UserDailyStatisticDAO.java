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
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.statistic.UserStatistics;

/**
 *
 * @author DuongLTD
 */
public class UserDailyStatisticDAO {

    private static DBCollection coll;
    
    static{
         try{
             coll = DBManager.getStatisticDB().getCollection( StatisticdbKey.USER_DAILY_STATISTIC_COLLECTION );
             coll.createIndex(new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.DAY, 1));
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    } 
    
    public static List<UserStatistics> getDayStatistic(String fromDay, String toDay, long addTime, long fromTime, long toTime) throws EazyException {
        List<UserStatistics> result= new ArrayList<>();
        try{
            BasicDBObject findObject = new BasicDBObject();
            BasicDBList ands = new BasicDBList();
            BasicDBObject gte = new BasicDBObject("$gte", fromDay);
            BasicDBObject lte = new BasicDBObject("$lte", toDay);
            ands.add(new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, lte));
            ands.add(new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, gte));
            findObject.append("$and", ands);
            BasicDBObject sortObj = new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.DAY, 1);
            DBCursor cursor = coll.find(findObject).sort(sortObj);            
            while(cursor.hasNext()){
                DBObject dbO = cursor.next();
                BasicDBList listHour = (BasicDBList) dbO.get(StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR);
                if(listHour != null){
                    for(int i = 0; i < listHour.size(); i++){
                        
                        UserStatistics stObj = new UserStatistics();
                        DBObject obj = (DBObject) listHour.get(i);
                        String serverHour = (String) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.HOUR);
                        long serverTime = DateFormat.parse(serverHour).getTime();
                        long clientTime = serverTime + addTime; 
                        if(clientTime >= fromTime && clientTime <= toTime){
                            stObj.time = DateFormat.format_yyyyMMddHH(new Date(clientTime));
                            Integer onlNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_IOS);
                            stObj.onlineNumberIos = onlNumIos;
                            Integer onlFemaleNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_IOS);
                            stObj.onlineFemaleNumberIos = onlFemaleNumIos;
                            Integer onlMaleNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_IOS);
                            stObj.onlineMaleNumberIos = onlMaleNumIos;
                            Integer onlNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_ANDROID);
                            stObj.onlineNumberAndroid = onlNumAndroid;
                            Integer onlFemaleNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_ANDROID);
                            stObj.onlineFemaleNumberAndroid = onlFemaleNumAndroid;
                            Integer onlMaleNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_ANDROID);
                            stObj.onlineMaleNumberAndroid = onlMaleNumAndroid;
                            Integer newUserIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_IOS);
                            stObj.newUserIosNumber = newUserIos;
                            Integer newUserAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_ANDROID);
                            stObj.newUserAndroidNumber = newUserAndroid;
                            Integer newUserWeb = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_WEB);
                            stObj.newUserWebNumber = newUserWeb;
                            Integer actNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_IOS);
                            stObj.activeUserIosNumber = actNumIos;
                            Integer actNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_ANDROID);
                            stObj.activeUserAndroidNumber = actNumAndroid;
                            Integer total = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.TOTAL_USER);
                            stObj.totalUserNumber = total;
                            Integer videoCall = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.VIDEO_CALL);
                            stObj.videoCallNumber = videoCall;
                            Integer voiceCall = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.VOICE_CALL);
                            stObj.voiceCallNumber = voiceCall;

                            result.add(stObj);
                        }
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<UserStatistics> getMonthStatistic(String fromDay, String toDay, long addTime, long fromTime, long toTime) throws EazyException {
        List<UserStatistics> result= new ArrayList<>();
        try{
            BasicDBObject findObject = new BasicDBObject();
            BasicDBList ands = new BasicDBList();
            BasicDBObject gte = new BasicDBObject("$gte", fromDay);
            BasicDBObject lte = new BasicDBObject("$lte", toDay);
            ands.add(new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.DAY, lte));
            ands.add(new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.DAY, gte));
            findObject.append("$and", ands);
            BasicDBObject sortObj = new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.DAY, 1);
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            int size = cursor.size();
            int count = 0;
            UserStatistics stObj = new UserStatistics();
            while(cursor.hasNext()){
                DBObject dbO = cursor.next();
                count++; 
                BasicDBList listHour = (BasicDBList) dbO.get(StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR);
                if(listHour != null){
                    for(int i = 0; i < listHour.size(); i++){
                        
                        DBObject obj = (DBObject) listHour.get(i);
                        String serverHour = (String) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.HOUR);
                        long serverTime = DateFormat.parse(serverHour).getTime();
                        long clientTime = serverTime + addTime; 
                        if(clientTime >= fromTime && clientTime <= toTime){
                            String clientDay =  DateFormat.format_yyyyMMdd(new Date(clientTime));
                            if(stObj.time == null || !stObj.time.equals(clientDay)){
                                if(stObj.time != null)
                                    result.add(stObj);
                                
                                stObj = new UserStatistics();
                                
                                stObj.time = clientDay;
                                
                                Integer onlNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_IOS);
                                if(onlNumIos != null)
                                    stObj.onlineNumberIos = onlNumIos;
                                Integer onlFemaleNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_IOS);
                                if(onlFemaleNumIos != null)
                                    stObj.onlineFemaleNumberIos = onlFemaleNumIos;
                                Integer onlMaleNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_IOS);
                                if(onlMaleNumIos != null)
                                    stObj.onlineMaleNumberIos = onlMaleNumIos;
                                
                                Integer onlNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_ANDROID);
                                if(onlNumAndroid != null)
                                    stObj.onlineNumberAndroid = onlNumAndroid;
                                Integer onlFemaleNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_ANDROID);
                                if(onlFemaleNumAndroid != null)
                                    stObj.onlineFemaleNumberAndroid = onlFemaleNumAndroid;
                                Integer onlMaleNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_ANDROID);
                                if(onlMaleNumAndroid != null)
                                    stObj.onlineMaleNumberAndroid = onlMaleNumAndroid;
                                
                                Integer newUserIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_IOS);
                                if(newUserIos != null)
                                    stObj.newUserIosNumber = newUserIos;
                                Integer newFemaleUserIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_IOS);
                                if(newFemaleUserIos != null)
                                    stObj.newFemaleUserIosNumber = newFemaleUserIos;
                                Integer newMaleUserIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_IOS);
                                if(newMaleUserIos != null)
                                    stObj.newMaleUserIosNumber = newMaleUserIos;
                                
                                Integer newUserAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_ANDROID);
                                if(newUserAndroid != null)
                                    stObj.newUserAndroidNumber = newUserAndroid;
                                Integer newFemlaeUserAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_ANDROID);
                                if(newFemlaeUserAndroid != null)
                                    stObj.newFemaleUserAndroidNumber = newFemlaeUserAndroid;
                                Integer newMaleUserAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_ANDROID);
                                if(newMaleUserAndroid != null)
                                    stObj.newMaleUserAndroidNumber = newMaleUserAndroid;
                                
                                Integer newUserWeb = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_WEB);
                                if(newUserWeb != null)
                                    stObj.newUserWebNumber = newUserWeb;
                                Integer newFemaleUserWeb = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_WEB);
                                if(newFemaleUserWeb != null)
                                    stObj.newFemaleUserWebNumber = newFemaleUserWeb;
                                Integer newMaleUserWeb = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_WEB);
                                if(newMaleUserWeb != null)
                                    stObj.newMaleUserWebNumber = newMaleUserWeb;
                                
                                Integer actNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_IOS);
                                if(actNumIos != null)
                                    stObj.activeUserIosNumber = actNumIos;
                                Integer actNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_ANDROID);
                                if(actNumAndroid != null)
                                    stObj.activeUserAndroidNumber = actNumAndroid;
                                
                                Integer total = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.TOTAL_USER);
                                if(total != null)    
                                    stObj.totalUserNumber = total;    
                                
                                Integer voiceCall = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.VOICE_CALL);
                                if(voiceCall != null)    
                                    stObj.voiceCallNumber = voiceCall;                                 
                                Integer videoCall = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.VIDEO_CALL);
                                if(videoCall != null)    
                                    stObj.videoCallNumber = videoCall;                                 
                            }else{
                                Integer onlNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_IOS);
                                if(onlNumIos != null)
                                    stObj.onlineNumberIos += onlNumIos;
                                Integer onlFemaleNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_IOS);
                                if(onlFemaleNumIos != null)
                                    stObj.onlineFemaleNumberIos += onlFemaleNumIos;
                                Integer onlMaleNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_IOS);
                                if(onlMaleNumIos != null)
                                    stObj.onlineMaleNumberIos += onlMaleNumIos;
                                
                                Integer onlNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_ANDROID);
                                if(onlNumAndroid != null)
                                    stObj.onlineNumberAndroid += onlNumAndroid;
                                Integer onlFemaleNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_ANDROID);
                                if(onlFemaleNumAndroid != null)
                                    stObj.onlineFemaleNumberAndroid += onlFemaleNumAndroid;
                                Integer onlMaleNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_ANDROID);
                                if(onlMaleNumAndroid != null)
                                    stObj.onlineMaleNumberAndroid += onlMaleNumAndroid;
                                
                                Integer newUserIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_IOS);
                                if(newUserIos != null)
                                    stObj.newUserIosNumber += newUserIos;
                                Integer newFemaleUserIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_IOS);
                                if(newFemaleUserIos != null)
                                    stObj.newFemaleUserIosNumber += newFemaleUserIos;
                                Integer newMaleUserIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_IOS);
                                if(newMaleUserIos != null)
                                    stObj.newMaleUserIosNumber += newMaleUserIos;
                                
                                Integer newUserAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_ANDROID);
                                if(newUserAndroid != null)
                                    stObj.newUserAndroidNumber += newUserAndroid;
                                Integer newFemaleUserAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_ANDROID);
                                if(newFemaleUserAndroid != null)
                                    stObj.newFemaleUserAndroidNumber += newFemaleUserAndroid;
                                Integer newMaleUserAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_ANDROID);
                                if(newMaleUserAndroid != null)
                                    stObj.newMaleUserAndroidNumber += newMaleUserAndroid;
                                
                                Integer newUseWeb = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_WEB);
                                if(newUseWeb != null)
                                    stObj.newUserWebNumber += newUseWeb;
                                Integer newFemaleUseWeb = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_WEB);
                                if(newFemaleUseWeb != null)
                                    stObj.newFemaleUserWebNumber += newFemaleUseWeb;
                                Integer newMaleUseWeb = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_WEB);
                                if(newMaleUseWeb != null)
                                    stObj.newMaleUserWebNumber += newMaleUseWeb;
                                
                                Integer actNumIos = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_IOS);
                                if(actNumIos != null)
                                    stObj.activeUserIosNumber += actNumIos;
                                Integer actNumAndroid = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_ANDROID);
                                if(actNumAndroid != null)
                                    stObj.activeUserAndroidNumber += actNumAndroid;
                                
                                Integer total = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.TOTAL_USER);
                                if(total != null)    
                                    stObj.totalUserNumber = total;    
                                
                                Integer videoCall = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.VIDEO_CALL);
                                if(videoCall != null)    
                                    stObj.videoCallNumber = videoCall;                            
                                Integer voiceCall = (Integer) obj.get(StatisticdbKey.USER_DAILY_STATISTIC.VOICE_CALL);
                                if(voiceCall != null)    
                                    stObj.voiceCallNumber = voiceCall;                            
                            }    
                        }
                        if(count == size && i == listHour.size() -1 && stObj.time != null){
                            result.add(stObj);
                        }                        

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
