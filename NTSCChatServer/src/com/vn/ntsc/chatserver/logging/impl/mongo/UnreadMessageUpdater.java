/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.logging.impl.mongo;

import java.util.concurrent.ConcurrentLinkedQueue;
import com.vn.ntsc.chatserver.logging.impl.mongo.runner.UpdateLastMessageThread;
import com.vn.ntsc.chatserver.logging.pojos.UpdateUnreadPackage;
import com.vn.ntsc.chatserver.pojos.user.UnreadMessageManager;

/**
 *
 * @author duongltd
 */
public class UnreadMessageUpdater{

    private static final ConcurrentLinkedQueue<UpdateUnreadPackage> container = new ConcurrentLinkedQueue<>();
    
    public UnreadMessageUpdater(){
    }
    
    public static void add( String userId ) {
        UpdateUnreadPackage inf = new UpdateUnreadPackage(userId, UnreadMessageManager.getHashMapUnreadMessage(userId));
        container.add( inf );
    }
    
    public static UpdateUnreadPackage poll(){
        return container.poll();
    }

    public void startService() {
        UpdateLastMessageThread ulmt = new UpdateLastMessageThread();
        ulmt.start();
    }
    
}
