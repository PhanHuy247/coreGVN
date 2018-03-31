/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver;

import eazycommon.CommonCore;
import eazycommon.util.Util;
import com.vn.ntsc.presentationserver.meetpeople.IMeetPeopleProcessor;
import com.vn.ntsc.presentationserver.meetpeople.dao.DatabaseLoader;
import com.vn.ntsc.presentationserver.meetpeople.impl.MeetPeopleProcessor;
import com.vn.ntsc.presentationserver.server.Main;

/**
 *
 * @author tuannxv00804
 */
public class Core {
    
    public static IMeetPeopleProcessor meetPeopleProcessor;

    public static void main(String[] args) {
        CommonCore.init();
        MeetPeopleProcessor.init();
        DatabaseLoader.init();
        Util.addInfoLog("Start service Meetpeople");
        meetPeopleProcessor = new MeetPeopleProcessor();
        Main.run();
    }
}
