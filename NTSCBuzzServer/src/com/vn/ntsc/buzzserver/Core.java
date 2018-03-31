/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver;

import eazycommon.CommonCore;
import eazycommon.util.Util;
import com.vn.ntsc.buzzserver.blacklist.BlackListManager;
import com.vn.ntsc.buzzserver.dao.DAO;

/**
 *
 * @author DUONGLTD
 */
public class Core {
    
    public static void main( String[] args ) {
        CommonCore.init();
        Config.initConfig();
        DAO.init();
        BlackListManager.init();
        Util.addInfoLog("Start service Buzz Timeline");
        com.vn.ntsc.buzzserver.server.BuzzServer.run();
    }
        
}
