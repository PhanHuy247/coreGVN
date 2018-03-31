/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns;

import eazycommon.CommonCore;
import eazycommon.util.Util;
import com.vn.ntsc.jpns.dao.IDAO;
import com.vn.ntsc.jpns.dao.impl.MongoDAO;
import com.vn.ntsc.jpns.server.Server;
import com.vn.ntsc.jpns.server.workers.BuzzNotificationContainer;
import com.vn.ntsc.jpns.server.workers.MsgContainer;
import com.vn.ntsc.jpns.server.workers.SendBuzzRunner;

/**
 *
 * @author duongltd
 */
public class Core {

    public static IDAO dao;

    public static void main(String[] args) {
        CommonCore.init();

        Config.initConfig();

        dao = new MongoDAO();

        MsgContainer.run();
        BuzzNotificationContainer.run();
        
        Util.addInfoLog("Start service JPNS");

        Server.start();
    }

}
