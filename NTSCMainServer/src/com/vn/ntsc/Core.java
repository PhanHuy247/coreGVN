/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import eazycommon.CommonConfig;
import eazycommon.CommonCore;
import eazycommon.util.Util;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.eazyserver.server.DefaultHandler;
import com.vn.ntsc.eazyserver.server.Runner;
import com.vn.ntsc.eazyserver.server.session.GabageCollector;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.servicemanagement.respond.common.PublicKeyManager;
import com.vn.ntsc.otherservice.statistic.StatisticThread;

/**
 *
 * @author RuAc0n
 */
public class Core {

    public static void main(String[] args) {
        Config.initConfig();
        run();
    }

    private static void run() {
        CommonCore.init();
        DBLoader.init();
        SessionManager.init();
        PublicKeyManager.init();
        GabageCollector.startCleaningService();
        StatisticThread.startService();
//        StickerCleaner.startStickerCleaner();
        Runner.startService();
        Util.addInfoLog("Start service Main");
        Server server = new Server(Config.ServerPort);
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(60 * CommonConfig.JETTY_MAX_REQUEST);
        ExecutorThreadPool pool = new ExecutorThreadPool(10, CommonConfig.JETTY_MAX_THREAD, 300000, TimeUnit.MILLISECONDS, queue);
        server.setThreadPool(pool);
        DefaultHandler handler = new DefaultHandler();
        server.setHandler(handler);

        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
}
