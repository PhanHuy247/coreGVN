/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server;

import com.vn.ntsc.backend.Config;
import eazycommon.util.Util;

/**
 *
 * @author tuannxv00804
 */
public class Server {
    
    public static void start(){
        org.eclipse.jetty.server.Server s = new org.eclipse.jetty.server.Server( Config.PORT );
        s.setHandler( new Handler() );
        try{
            s.start();
            s.join();
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
           
        }
    }
    
}
