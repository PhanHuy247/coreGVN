/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.server;

import eazycommon.CommonCore;
import eazycommon.util.Util;
import org.eclipse.jetty.server.Server;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.gabagecollector.GabageCollector;
import vn.com.ntsc.staticfileserver.server.session.SessionCollector;
import vn.com.ntsc.staticfileserver.server.videocollector.VideoCollector;
/**
 *
 * @author RuAc0n
 */
public class StaticFileServer {

    public static void main( String[] args ) {
        run();
    }

    private static void run() {
        CommonCore.init();
        Config.initConfig();
        DAO.init();
        SessionCollector.startCleaningService();
        GabageCollector.startGabageCollector();
        VideoCollector.startVideoCollector();
        Util.addInfoLog("Start service STF");
        Server server = new Server( Config.PORT );
        StaticFileHandler StfHandler = new StaticFileHandler();
        server.setHandler( StfHandler );
        try{
            server.start();
            server.join();
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
           
        }
    }
}
