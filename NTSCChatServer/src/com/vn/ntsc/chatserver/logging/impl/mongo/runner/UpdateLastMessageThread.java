/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.logging.impl.mongo.runner;

import eazycommon.util.Util;
import com.vn.ntsc.Config;
import com.vn.ntsc.chatserver.logging.impl.mongo.LastChatUpdater;
import com.vn.ntsc.chatserver.logging.impl.mongo.UnreadMessageUpdater;
import com.vn.ntsc.chatserver.logging.pojos.LastChatPackage;
import com.vn.ntsc.chatserver.logging.pojos.UpdateUnreadPackage;
import com.vn.ntsc.dao.impl.LastChatDAO;
import com.vn.ntsc.dao.impl.UnreadMessageDAO;

/**
 *
 * @author tuannxv00804
 */
public class UpdateLastMessageThread extends Thread {

    public static final int IdleThreadLatency = Config.IdleThreadLatency;

    public UpdateLastMessageThread() {
    }
    
    @Override
    public void run() {
        while( true ) {
            LastChatPackage p = LastChatUpdater.poll();
            if( p != null ){
                LastChatDAO.putLastChatList(p.userId, p.friendId, p.msg);
            }else{
                sleep( IdleThreadLatency );
            }
        }
    }
    
    private void sleep( int delay ) {
        try {
            Thread.sleep( delay );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
    }
       
}
