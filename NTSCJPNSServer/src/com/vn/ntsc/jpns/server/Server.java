/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import com.vn.ntsc.jpns.Config;
import eazycommon.util.Util;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

/**
 *
 * @author tuannxv00804
 */
public class Server {
    
    public static void start(){
        
        org.eclipse.jetty.server.Server s = new org.eclipse.jetty.server.Server( Config.JPNSPort );
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(6000);
        ExecutorThreadPool pool = new ExecutorThreadPool(10, 200, 300000, TimeUnit.MILLISECONDS, queue);
        s.setThreadPool(pool);
        s.setHandler( new Handler() );
        try{
            s.start();
            s.join();
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
        
    }
    
}
