/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server;

import eazycommon.util.Util;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import eazycommon.CommonConfig;
import com.vn.ntsc.usermanagementserver.Config;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

/**
 *
 * @author DuongLTD
 */
public class Server {
    
    public static void run() {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(Config.port);
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(60 * CommonConfig.JETTY_MAX_REQUEST);
        ExecutorThreadPool pool = new ExecutorThreadPool(10, CommonConfig.JETTY_MAX_THREAD, 300000, TimeUnit.MILLISECONDS, queue);
        server.setThreadPool(pool);
        Handler handler = new Handler();
        server.setHandler(handler);
        try {
//            Config.initConfig();
            server.start();
            server.join();
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
        }
    }
}
