/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver;

import com.vn.ntsc.usermanagementserver.server.systemaccount.SystemAccountCreator;
import com.vn.ntsc.usermanagementserver.server.systemaccount.SystemAccountManager;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import eazycommon.CommonCore;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.pointaction.LogPointContainer;
import com.vn.ntsc.usermanagementserver.server.unlockpool.UnlockCleaner;
import com.vn.ntsc.usermanagementserver.server.userinformanager.CheckoutManager;
import com.vn.ntsc.usermanagementserver.server.userinformanager.MyFootPrintManager;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;
import com.vn.ntsc.usermanagementserver.server.worker.ImageGenderUpdater;

/**
 *
 * @author DUONGLTD
 */
public class Core {

    public static void main(String[] args) {
        long start1 = System.nanoTime();
        CommonCore.init();
        Util.getDuration("CommonCore", start1);
        long start2 = System.nanoTime();
        Config.initConfig();
        Util.getDuration("Config", start2);
        long start3 = System.nanoTime();
        DatabaseLoader.init();
        Util.getDuration("DatabaseLoader", start3);
        long start4 = System.nanoTime();
        UnlockCleaner.startUnlockCleaner();
        Util.getDuration("UnlockCleaner", start4);
        long start5 = System.nanoTime();
        CheckoutManager.init();
        Util.getDuration("CheckoutManager", start5);
        long start6 = System.nanoTime();
        MyFootPrintManager.init();
        Util.getDuration("MyFootPrintManager", start6);
        long start7 = System.nanoTime();
        BlockUserManager.init();
        Util.getDuration("BlockUserManager", start7);
        long start8 = System.nanoTime();
        DeactivateUserManager.init();
        Util.getDuration("DeactivateUserManager", start8);
        long start9 = System.nanoTime();
        NotificationCleaner.startNotificationCleaner();
//        ImageGenderUpdater.startImageGenderUpdater();
        SystemAccountCreator.addAdministratorAccount();
        Util.getDuration("NotificationCleaner", start9);
        long start10 = System.nanoTime();
        SystemAccountManager.init();
        Util.getDuration("SystemAccountManager", start10);
        long start11 = System.nanoTime();
        UserInforManager.init();
        Util.getDuration("UserInforManager", start11);
        long start12 = System.nanoTime();
        LogPointContainer.run();
        Util.getDuration("LogPointContainer", start12);
        Util.addInfoLog("Start service UMS");
        com.vn.ntsc.usermanagementserver.server.Server.run();
    }

}
