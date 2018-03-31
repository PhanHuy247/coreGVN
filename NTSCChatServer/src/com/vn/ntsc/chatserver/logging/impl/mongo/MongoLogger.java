/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.logging.impl.mongo;

import com.vn.ntsc.chatserver.logging.impl.mongo.runner.LoggingThread;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.vn.ntsc.Config;
import com.vn.ntsc.chatserver.logging.ILogger;
import com.vn.ntsc.chatserver.pojos.message.Message;

/**
 *
 * @author tuannxv00804
 */
public class MongoLogger implements ILogger{

    private static final int LoggingThreadNumber = Config.LoggingThreadNumber;
    public static final ConcurrentLinkedQueue<Message> Container = new ConcurrentLinkedQueue<>();
    
    public MongoLogger(){
    }
    
    @Override
    public void log( Message msg ) {
        Container.add( msg );
    }

    @Override
    public void startService() {
        for( int i = 0; i < LoggingThreadNumber; i++ ){
            LoggingThread lt = new LoggingThread();
            lt.start();
            
        }
        
        
    }
    
}
