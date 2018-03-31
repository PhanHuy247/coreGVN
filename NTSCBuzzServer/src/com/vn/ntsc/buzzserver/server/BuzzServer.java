/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server;

import org.eclipse.jetty.server.Server;
import com.vn.ntsc.buzzserver.Config;
import eazycommon.util.Util;

public class BuzzServer {

    public static void run() {
        Server server = new Server(Config.PORT);
        BuzzHandler buzzHandler = new BuzzHandler();
        server.setHandler(buzzHandler);
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
        }
         
    }
}
