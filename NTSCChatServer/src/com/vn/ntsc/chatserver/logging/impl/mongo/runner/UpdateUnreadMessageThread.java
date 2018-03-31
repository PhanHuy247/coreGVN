/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.logging.impl.mongo.runner;

import eazycommon.util.Util;
import com.vn.ntsc.Config;
import com.vn.ntsc.chatserver.logging.impl.mongo.UnreadMessageUpdater;
import com.vn.ntsc.chatserver.logging.pojos.UpdateUnreadPackage;
import com.vn.ntsc.dao.impl.UnreadMessageDAO;

/**
 *
 * @author tuannxv00804
 */
public class UpdateUnreadMessageThread extends Thread {

    public static final int IdleThreadLatency = Config.IdleThreadLatency;

    public UpdateUnreadMessageThread() {
    }
    
    @Override
    public void run() {
        while( true ) {
            UpdateUnreadPackage inf = UnreadMessageUpdater.poll();
            if( inf != null ){
                UnreadMessageDAO.update(inf.userId, inf.unreadMessageMap);
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
