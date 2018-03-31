/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatlogserver;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import eazycommon.util.Util;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import com.vn.ntsc.Config;

/**
 *
 * @author tuannxv00804
 */
public class Server {

    public static void run() {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server( Config.ChatLogServer_Port );
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(6000);
        ExecutorThreadPool pool = new ExecutorThreadPool(10, 200, 300000, TimeUnit.MILLISECONDS, queue);
        server.setThreadPool(pool);
        ChatLogHandler handler = new ChatLogHandler();
        server.setHandler( handler );
        
        try{
            server.start();
            server.join();
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
    }
}
