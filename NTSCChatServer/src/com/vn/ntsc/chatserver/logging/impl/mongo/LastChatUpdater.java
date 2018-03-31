/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.logging.impl.mongo;

import java.util.concurrent.ConcurrentLinkedQueue;
import com.vn.ntsc.chatserver.logging.impl.mongo.runner.UpdateUnreadMessageThread;
import com.vn.ntsc.chatserver.logging.pojos.LastChatPackage;
import com.vn.ntsc.chatserver.pojos.message.Message;

/**
 *
 * @author duongltd
 */
public class LastChatUpdater{

    private static final ConcurrentLinkedQueue<LastChatPackage> container = new ConcurrentLinkedQueue<>();
    
    public LastChatUpdater(){
    }
    
    public static void add( String userId, String friendId, Message msg ) {
        LastChatPackage p = new LastChatPackage(userId, friendId, msg);
        container.add( p );
    }
    
    public static LastChatPackage poll(){
        return container.poll();
    }

    public void startService() {
        UpdateUnreadMessageThread uumt = new UpdateUnreadMessageThread();
        uumt.start();
    }
    
}
