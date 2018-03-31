/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import eazycommon.CommonConfig;
import eazycommon.util.Util;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import com.vn.ntsc.presentationserver.Config;

/**
 *
 * @author duongltd
 */
public class Main {
    
    public static void run() {
        try{
            Server server = new Server( Config.ServerPort );
            LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(60 * CommonConfig.JETTY_MAX_REQUEST);
            ExecutorThreadPool pool = new ExecutorThreadPool(10, CommonConfig.JETTY_MAX_THREAD, 300000, TimeUnit.MILLISECONDS, queue);
            server.setThreadPool(pool);
            PresentationHandler handler = new PresentationHandler();
            server.setHandler( handler );
            Util.addInfoLog("Run service Meetpeople");
            server.start();
            server.join();
        } catch( Exception ex ) {
            Util.addErrorLog(ex);             
        }
    }
}
