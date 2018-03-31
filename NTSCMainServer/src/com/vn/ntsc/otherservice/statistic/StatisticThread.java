/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.statistic;

import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.BackupStatisticDAO;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.dao.impl.UserDailyStatisticDAO;
import com.vn.ntsc.dao.impl.UserMonthStatisticDAO;


/**
 *
 * @author tuannxv00804
 */
public class StatisticThread extends Thread {
    
    public static void startService() {
        UserCounter.initInformation();
        StatisticThread g = new StatisticThread();
        g.start();
    }

    @Override
    public void run() {
//        
//        int monthLastestNumberMaleUser = 0;
//        int monthLastestNumberFemaleUser = 0;
//        
//        String lastestMonth = "";
        String lastestDay = null;

        try {
            lastestDay = BackupStatisticDAO.getLastestDay();
        } catch (EazyException ex) {
            Util.addErrorLog(ex);            
           
        }
        while( true ) {
            try {
                Date d = Util.getGMTTime();
                int minus = d.getMinutes();
                 if( minus >= 55){
//                if( count > 0 && minus >= 55){
                    String dateString = DateFormat.format(d);
                    int currentUser = UserDAO.countUser();

                    //update day
                    String currentDay = dateString.substring(0,8);
                    
                    // user day
                    int hourNumberActiveIos = UserCounter.hourActiveIosSet.size();
                    int hourNumberActiveAndroid = UserCounter.hourActiveAndroidSet.size();
                    UserDailyStatisticDAO.update(currentDay, dateString,
                            UserCounter.hourLoginIosCounter, UserCounter.hourLoginIosFemaleCounter, UserCounter.hourLoginIosMaleCounter,
                            UserCounter.hourLoginAndroidCounter,UserCounter.hourLoginAndroidFemaleCounter, UserCounter.hourLoginAndroidMaleCounter,
                            UserCounter.hourRegisterIosCounter, UserCounter.hourRegisterIosFemaleCounter, UserCounter.hourRegisterIosMaleCounter,
                            UserCounter.hourRegisterAndroidCounter, UserCounter.hourRegisterAndroidFemaleCounter, UserCounter.hourRegisterAndroidMaleCounter,
                            UserCounter.hourRegisterWebCounter, UserCounter.hourRegisterWebFemaleCounter, UserCounter.hourRegisterWebMaleCounter,
                            hourNumberActiveIos, hourNumberActiveAndroid, currentUser, UserCounter.hourVideoCall, UserCounter.hourVoiceCall);
                    UserCounter.hourActiveIosSet.clear();
                    UserCounter.hourActiveAndroidSet.clear();
                    
                    UserCounter.hourLoginAndroidCounter = 0;
                    UserCounter.hourLoginAndroidFemaleCounter = 0;
                    UserCounter.hourLoginAndroidMaleCounter = 0;
                    
                    UserCounter.hourLoginIosCounter = 0;
                    UserCounter.hourLoginIosFemaleCounter = 0;
                    UserCounter.hourLoginIosMaleCounter = 0;
                    
                    UserCounter.hourRegisterIosCounter = 0;
                    UserCounter.hourRegisterIosFemaleCounter = 0;
                    UserCounter.hourRegisterIosMaleCounter = 0;
                    
                    UserCounter.hourRegisterAndroidCounter = 0;
                    UserCounter.hourRegisterAndroidFemaleCounter = 0;
                    UserCounter.hourRegisterAndroidMaleCounter = 0;
                    
                    UserCounter.hourRegisterWebCounter = 0;
                    UserCounter.hourRegisterWebFemaleCounter = 0;
                    UserCounter.hourRegisterWebMaleCounter = 0;
                    
                    UserCounter.hourVideoCall = 0;
                    UserCounter.hourVoiceCall = 0;

                    
                    //update month
                    String currentMonth = dateString.substring(0,6);
                    int dayNumberActiveIos = UserCounter.dayActiveIosSet.size();
                    int dayNumberActiveAndroid = UserCounter.dayActiveAndroidSet.size();
                    UserMonthStatisticDAO.update(currentMonth, currentDay,
                            UserCounter.dayLoginIosCounter, UserCounter.dayLoginIosFemaleCounter, UserCounter.dayLoginIosMaleCounter,
                            UserCounter.dayLoginAndroidCounter,UserCounter.dayLoginAndroidFemaleCounter, UserCounter.dayLoginAndroidMaleCounter,
                            UserCounter.dayRegisterIosCounter, UserCounter.dayRegisterIosFemaleCounter, UserCounter.dayRegisterIosMaleCounter,
                            UserCounter.dayRegisterAndroidCounter, UserCounter.dayRegisterAndroidFemaleCounter, UserCounter.dayRegisterAndroidMaleCounter,
                            UserCounter.dayRegisterWebCounter, UserCounter.dayRegisterWebFemaleCounter, UserCounter.dayRegisterWebMaleCounter,
                            dayNumberActiveIos, dayNumberActiveAndroid, currentUser,
                            UserCounter.dayVideoCall, UserCounter.dayVoiceCall);
                    if(lastestDay == null || currentDay.compareTo(lastestDay) > 0){
                        BackupStatisticDAO.updateMonthStatistic(currentDay, UserCounter.dayActiveIosSet, UserCounter.dayActiveAndroidSet);
                        UserCounter.dayActiveAndroidSet.clear();
                        UserCounter.dayActiveIosSet.clear();
                        
                        UserCounter.dayLoginAndroidCounter = 0;
                        UserCounter.dayLoginAndroidFemaleCounter = 0;
                        UserCounter.dayLoginAndroidMaleCounter = 0;
                        
                        UserCounter.dayLoginIosCounter = 0;
                        UserCounter.dayLoginIosFemaleCounter = 0;
                        UserCounter.dayLoginIosMaleCounter = 0;
                        
                        UserCounter.dayRegisterAndroidCounter = 0;
                        UserCounter.dayRegisterAndroidFemaleCounter = 0;
                        UserCounter.dayRegisterAndroidMaleCounter = 0;
                        
                        UserCounter.dayRegisterIosCounter = 0;
                        UserCounter.dayRegisterIosFemaleCounter = 0;
                        UserCounter.dayRegisterIosMaleCounter = 0;
                        
                        UserCounter.dayRegisterWebCounter = 0;
                        UserCounter.dayRegisterWebFemaleCounter = 0;
                        UserCounter.dayRegisterWebMaleCounter = 0;
                        
                        UserCounter.dayVideoCall = 0;
                        UserCounter.dayVoiceCall = 0;
                        lastestDay = currentDay;
                    }else{ 
                        BackupStatisticDAO.updateMonthStatistic(currentDay,UserCounter.dayActiveIosSet, UserCounter.dayActiveAndroidSet);
                    }
                }
                Thread.sleep(Constant.FIVE_MINUTES);
            } catch( Exception ex ) {
                Util.addErrorLog(ex);                
               
            }
        }
    }

}
