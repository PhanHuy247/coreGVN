/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.session;

import java.util.Collection;
import java.util.Iterator;
import eazycommon.constant.Constant;
import eazycommon.util.Util;
import vn.com.ntsc.staticfileserver.Config;

/**
 *
 * @author tuannxv00804
 */
public class SessionCollector extends Thread {

    public static void startCleaningService() {
        SessionCollector g = new SessionCollector();
        g.start();
    }

    @Override
    public void run() {
        while( true ) {
            try {
                Collection<Session> ss = SessionManager.getAllSession();
                Iterator<Session> iter = ss.iterator();
                while( iter.hasNext() ){
                    Session s = iter.next();
                    if( s.timeToLive < Config.SESSION_TIMEOUT ){
                        s.timeToLive++;
                    }else{
                        SessionManager.removeSession( s.token );
                    }
                }
                Thread.sleep( Constant.A_MINUTE );
            } catch( Exception ex ) {
                Util.addErrorLog(ex);                
               
            }
        }
    }
}
