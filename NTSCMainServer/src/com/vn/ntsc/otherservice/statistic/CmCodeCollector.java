/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.statistic;

import eazycommon.util.Util;
import eazycommon.util.DateFormat;
import com.vn.ntsc.dao.impl.CMCodeDAO;
import com.vn.ntsc.dao.impl.CMCodeStatisticDAO;
import com.vn.ntsc.dao.impl.InstallationStatisticDAO;


/**
 *
 * @author RuAc0n
 */
public class CmCodeCollector {

//    private static SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_FORMAT);

    public static void loginLog(String cmCode, int type) {
        try {
            if (cmCode != null) {
                String dateTime = DateFormat.format(Util.getGMTTime());
                String day = dateTime.substring(0, 8);
                CMCodeStatisticDAO.loginUpdate(day, cmCode, type);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static void registerLog(String cmCode, int type) {
        try {
            if (cmCode != null) {
                String dateTime = DateFormat.format(Util.getGMTTime());
                String day = dateTime.substring(0, 8);
                CMCodeStatisticDAO.registerUpdate(day, cmCode, type);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    
    private static final long TWO_MINUTE = 2L * 60 *1000;
            
    public static void installLog(String cmCode, int type, String uniqueNumber) {
        try {
            if (cmCode != null && uniqueNumber != null && !uniqueNumber.isEmpty()) {
                long time = Util.getGMTTime().getTime();
                String dateTime = DateFormat.format(time);
                String day = dateTime.substring(0, 8);
                CMCodeStatisticDAO.installUpdate(day, cmCode, type, time);
                
                int numberUnique = InstallationStatisticDAO.getNumberUniqueNumber(type, uniqueNumber);

                if(numberUnique == 0){
                    CMCodeStatisticDAO.installUpdate(day, cmCode, type, uniqueNumber, time);
                }else if (numberUnique == 1){
                    long uniqueInstallTime = InstallationStatisticDAO.getTimeUniqueInstallApplication(type, uniqueNumber);
                    if( Util.currentTime() - uniqueInstallTime <= TWO_MINUTE){
                        CMCodeStatisticDAO.installUpdate(day, cmCode, type, uniqueNumber, time);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static void purchaseLog(String cmCode, int type) {
        try {
            if (cmCode != null) {
                Double money = CMCodeDAO.getMoney(cmCode);
                if (money != null) {
                    String dateTime = DateFormat.format(Util.getGMTTime());
                    String day = dateTime.substring(0, 8);
                    CMCodeStatisticDAO.purchaseUpdate(day, cmCode, type, money);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
}
