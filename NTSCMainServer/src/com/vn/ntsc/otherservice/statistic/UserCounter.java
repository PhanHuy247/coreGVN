/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.statistic;

import java.util.Set;
import java.util.TreeSet;
import eazycommon.constant.Constant;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.BackupStatisticDAO;

/**
 *
 * @author RuAc0n
 */
public class UserCounter {
    
    public static Set<String> hourActiveIosSet = new TreeSet<>();
    public static Set<String> hourActiveAndroidSet = new TreeSet<>();
    
    public static Set<String> dayActiveIosSet = new TreeSet<>();
    public static Set<String> dayActiveAndroidSet = new TreeSet<>();
    
//    public static Set<String> monthActiveMaleSet = new TreeSet<String>();
//    public static Set<String> monthActiveFemaleSet = new TreeSet<String>();
    
    public static int hourLoginIosCounter = 0;
    public static int hourLoginIosFemaleCounter = 0;
    public static int hourLoginIosMaleCounter = 0;
    
    public static int hourLoginAndroidCounter = 0;
    public static int hourLoginAndroidFemaleCounter = 0;
    public static int hourLoginAndroidMaleCounter = 0;
    
    public static int dayLoginIosCounter = 0;
    public static int dayLoginIosFemaleCounter = 0;
    public static int dayLoginIosMaleCounter = 0;
    
    public static int dayLoginAndroidCounter = 0;
    public static int dayLoginAndroidFemaleCounter = 0;
    public static int dayLoginAndroidMaleCounter = 0;
    
    public static int hourRegisterIosCounter = 0;
    public static int hourRegisterIosFemaleCounter = 0;
    public static int hourRegisterIosMaleCounter = 0;
    
    public static int hourRegisterAndroidCounter = 0;
    public static int hourRegisterAndroidFemaleCounter = 0;
    public static int hourRegisterAndroidMaleCounter = 0;
    
    public static int hourRegisterWebCounter = 0;
    public static int hourRegisterWebFemaleCounter = 0;
    public static int hourRegisterWebMaleCounter = 0;
    
    public static int dayRegisterIosCounter = 0;
    public static int dayRegisterIosFemaleCounter = 0;
    public static int dayRegisterIosMaleCounter = 0;
    
    public static int dayRegisterAndroidCounter = 0;
    public static int dayRegisterAndroidFemaleCounter = 0;
    public static int dayRegisterAndroidMaleCounter = 0;
    
    public static int dayRegisterWebCounter = 0;
    public static int dayRegisterWebFemaleCounter = 0;
    public static int dayRegisterWebMaleCounter = 0;
    
    public static int hourVideoCall = 0;
    public static int hourVoiceCall = 0;
    
    public static int dayVideoCall = 0;
    public static int dayVoiceCall = 0;
    
    
    public static void countCall(int callType){
        if(callType == Constant.CALL_TYPE_VALUE.VIDEO_CALL){
            hourVideoCall ++;
            dayVideoCall ++;
        }else{
            hourVoiceCall ++;
            dayVoiceCall ++;
        }
    }
//    private static BackupStatisticDAO backupDao = new BackupStatisticDAO();    
    
    public static void count (String userId, int deviceType, int gender, boolean isRegister){
        if(deviceType == Constant.DEVICE_TYPE.IOS){
            hourLoginIosCounter ++;
            dayLoginIosCounter ++;
            hourActiveIosSet.add(userId);
            dayActiveIosSet.add(userId);
            if(gender == Constant.GENDER.FEMALE){
                hourLoginIosFemaleCounter ++;
                dayLoginIosFemaleCounter ++;
            }else{
                hourLoginIosMaleCounter ++;
                dayLoginIosMaleCounter ++;
            }
//            monthActiveMaleSet.add(userId);
        }else{
            hourLoginAndroidCounter ++;
            dayLoginAndroidCounter ++;
            hourActiveAndroidSet.add(userId);
            dayActiveAndroidSet.add(userId);
            if(gender == Constant.GENDER.FEMALE){
                hourLoginAndroidFemaleCounter ++;
                dayLoginAndroidFemaleCounter ++;
            }else{
                hourLoginAndroidMaleCounter ++;
                dayLoginAndroidMaleCounter ++;
            }
//            monthActiveFemaleSet.add(userId);
        }
        
        if(isRegister){
            if(deviceType == Constant.DEVICE_TYPE.IOS){
                hourRegisterIosCounter ++;
                dayRegisterIosCounter ++;
                if(gender == Constant.GENDER.FEMALE){
                    dayRegisterIosFemaleCounter ++;
                    hourRegisterIosFemaleCounter ++;
                }else{
                    hourRegisterIosMaleCounter ++;
                    dayRegisterIosMaleCounter ++;
                }
            }else if(deviceType == Constant.DEVICE_TYPE.ANDROID){
                hourRegisterAndroidCounter ++;
                dayRegisterAndroidCounter ++;
                if(gender == Constant.GENDER.FEMALE){
                    hourRegisterAndroidFemaleCounter ++;
                    dayRegisterAndroidFemaleCounter ++;
                }else{
                    hourRegisterAndroidMaleCounter ++;
                    dayRegisterAndroidMaleCounter ++;
                }
            }else{
                hourRegisterWebCounter ++;
                dayRegisterWebCounter ++;
                if(gender == Constant.GENDER.FEMALE){
                    hourRegisterWebFemaleCounter ++;
                    dayRegisterWebFemaleCounter ++;
                }else{
                    hourRegisterWebMaleCounter ++;
                    dayRegisterWebMaleCounter ++;
                }
            }
        }
    }
    
    public static void initInformation(){
        try{
//            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_FORMAT);
            String dateString = DateFormat.format(Util.getGMTTime());
            String currentDay = dateString.substring(0, 8);
            String lastestDay = BackupStatisticDAO.getLastestDay();
            if(lastestDay != null && currentDay.equals(lastestDay)){
                dayActiveIosSet = BackupStatisticDAO.getActiveUserSet(Constant.STATISTIC_USER_TYPE.MONTH_USER_STATISTIC, Constant.DEVICE_TYPE.IOS);
                dayActiveAndroidSet = BackupStatisticDAO.getActiveUserSet(Constant.STATISTIC_USER_TYPE.MONTH_USER_STATISTIC, Constant.DEVICE_TYPE.ANDROID);
            }
            
//            String currentMonth = dateString.substring(0, 6);
//            String lastestMonth = backupDao.getLastestMonth();
//            if(lastestMonth != null && currentMonth.equals(lastestMonth)){
//                monthActiveMaleSet = backupDao.getActiveUserSet(Constant.YEAR_USER_STATISTIC, Constant.MALE);
//                monthActiveFemaleSet = backupDao.getActiveUserSet(Constant.YEAR_USER_STATISTIC, Constant.FEMALE);
//            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
    }
    
}
