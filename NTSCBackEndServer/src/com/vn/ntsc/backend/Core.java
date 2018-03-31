/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend;

import com.vn.ntsc.backend.Config;
import com.vn.ntsc.backend.dao.DBManager;
import eazycommon.CommonCore;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.vn.ntsc.backend.dao.admin.BannedWordDAO;
import com.vn.ntsc.backend.dao.admin.ReplaceWordDAO;
import com.vn.ntsc.backend.dao.log.LogPointDAO;
import com.vn.ntsc.backend.dao.log.TransactionLogDAO;
import com.vn.ntsc.backend.server.DatabaseCreator;
import com.vn.ntsc.backend.server.Server;
import com.vn.ntsc.backend.server.automessage.MessageDeliver;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeValue;

/**
 *
 * @author tuannxv00804
 */
public class Core {

    public static void main(String[] args) {

        Config.initConfig();
        CommonCore.init();
        DBManager.initSetting();
//        DatabaseCreator.createSupperAdminAccount();
        MessageDeliver.startThread();
        TypeValue.init();
        Util.addInfoLog("Start service Backend");
//        addLogToTotalPoint();
//        addLogToTotalPurchase();
        Server.start();
       
        try {
            ReplaceWordDAO.updateDateTime("", "", new Date());
            BannedWordDAO.updateDateTimeBanner("", "", new Date());
        } catch (EazyException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void addLogToTotalPurchase() {

        Runnable t = new Runnable() {

            @Override
            public void run() {
                try {
//                    LogPointDAO.updateTotalPointToUser();
                    TransactionLogDAO.updateTotalPurchaseToUser();
                } catch (EazyException ex) {
                    Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        (new Thread(t)).start();        
    }

    private static void addLogToTotalPoint() {

        Runnable t = new Runnable() {

            @Override
            public void run() {
                try {
                    LogPointDAO.updateTotalPointToUser();
//                    TransactionLogDAO.updateTotalPurchaseToUser();
                } catch (EazyException ex) {
                    Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        (new Thread(t)).start();
       
    }

}
