/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author DuongLTD
 */
public class UserDailyStatisticDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getStatisticDB().getCollection(StatisticdbKey.USER_DAILY_STATISTIC_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static boolean update(String day, String hour,
            int onlineNumberIos, int onlineNumberFemaleIos, int onlineNumberMaleIos,
            int onlineNumberAndroid, int onlineNumberFemaleAndroid, int onlineNumberMaleAndroid,
            int newNumberIos, int newNumberFemaleIos, int newNumberMaleIos,
            int newNumberAndroid, int newNumberFemaleAndroid, int newNumberMaleAndroid,
            int newNumberWeb, int newNumberFemaleWeb, int newNumberMaleWeb,
            int activeUserIos, int activeUserAndroid,
            int totalNumber,
            int videoCall, int voiceCall) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.DAY, day);
            BasicDBObject dayObj = new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.HOUR, hour);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", dayObj);
            findObj.append(StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                BasicDBObject query = new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.DAY, day);
                BasicDBObject updateObj = new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.HOUR, hour);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_IOS, onlineNumberIos);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_IOS, onlineNumberFemaleIos);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_IOS, onlineNumberMaleIos);
                
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_ANDROID, onlineNumberAndroid);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_ANDROID, onlineNumberFemaleAndroid);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_ANDROID, onlineNumberMaleAndroid);
                
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_IOS, activeUserIos);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_ANDROID, activeUserAndroid);
                
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_IOS, newNumberIos);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_IOS, newNumberFemaleIos);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_IOS, newNumberMaleIos);
                
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_ANDROID, newNumberAndroid);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_ANDROID, newNumberFemaleAndroid);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_ANDROID, newNumberMaleAndroid);
                
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_WEB, newNumberWeb);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_WEB, newNumberFemaleWeb);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_WEB, newNumberMaleWeb);
                
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.VIDEO_CALL, videoCall);
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.VOICE_CALL, voiceCall);
                
                updateObj.append(StatisticdbKey.USER_DAILY_STATISTIC.TOTAL_USER, totalNumber);
                
                BasicDBObject hObj = new BasicDBObject(StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR, updateObj);
                BasicDBObject pushObj = new BasicDBObject("$push", hObj);
                coll.update(query, pushObj, true, false);
            } else {

                String fieldOnlineIos = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_IOS;
                String fieldOnlineFemaleIos = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_IOS;
                String fieldOnlineMaleIos = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_IOS;
                
                String fieldOnlineAndroid = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_ANDROID;
                String fieldOnlineFemaleAndroid = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_FEMALE_ANDROID;
                String fieldOnlineMaleAndroid = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.ONLINE_USER_MALE_ANDROID;

                String fieldActiveIos = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_IOS;
                String fieldActiveAndroid = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.ACTIVE_USER_ANDROID;

                String fieldNewIos = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_IOS;
                String fieldNewFemaleIos = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_IOS;
                String fieldNewMaleIos = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_IOS;
                
                String fieldNewAndroid = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_ANDROID;
                String fieldNewFemaleAndroid = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_ANDROID;
                String fieldNewMaleAndroid = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_ANDROID;
                
                String fieldNewWeb = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_WEB;
                String fieldNewFemaleWeb = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_FEMALE_WEB;
                String fieldNewMaleWeb = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.NEW_USER_MALE_WEB;

                String fieldTotalMale = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.TOTAL_USER;
                
                String fieldVideoCall = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.VIDEO_CALL;
                String fieldVoiceCall = StatisticdbKey.USER_DAILY_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.USER_DAILY_STATISTIC.VOICE_CALL;

                BasicDBObject updateObj = new BasicDBObject(fieldActiveIos, activeUserIos);
                updateObj.append(fieldActiveAndroid, activeUserAndroid);
                
                updateObj.append(fieldNewIos, newNumberIos);
                updateObj.append(fieldNewFemaleIos, newNumberFemaleIos);
                updateObj.append(fieldNewMaleIos, newNumberMaleIos);
                
                updateObj.append(fieldNewAndroid, newNumberAndroid);
                updateObj.append(fieldNewFemaleAndroid, newNumberFemaleAndroid);
                updateObj.append(fieldNewMaleAndroid, newNumberMaleAndroid);

                updateObj.append(fieldNewWeb, newNumberWeb);
                updateObj.append(fieldNewFemaleWeb, newNumberFemaleWeb);
                updateObj.append(fieldNewMaleWeb, newNumberMaleWeb);
                
                updateObj.append(fieldOnlineIos, onlineNumberIos);
                updateObj.append(fieldOnlineFemaleIos, onlineNumberFemaleIos);
                updateObj.append(fieldOnlineMaleIos, onlineNumberMaleIos);
                
                updateObj.append(fieldOnlineAndroid, onlineNumberAndroid);
                updateObj.append(fieldOnlineFemaleAndroid, onlineNumberFemaleAndroid);
                updateObj.append(fieldOnlineMaleAndroid, onlineNumberMaleAndroid);
                
                updateObj.append(fieldTotalMale, totalNumber);
                
                updateObj.append(fieldVideoCall, videoCall);
                updateObj.append(fieldVoiceCall, voiceCall);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);

                coll.update(findObj, setObj);

            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    } 
    
}
