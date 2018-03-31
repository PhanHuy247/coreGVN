/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.workers;

import java.util.concurrent.ConcurrentLinkedQueue;
import com.vn.ntsc.jpns.Config;

/**
 *
 * @author tuannxv00804
 */
public class MsgContainer{
    
    private static final ConcurrentLinkedQueue<ReadyPackage> llmsg = new ConcurrentLinkedQueue<>();
    
    //Test code.
//    static{
//        ReadyPackage p1 = new ReadyPackage();
//        p1.msg = MsgUtil.iosPayload( API.noti_checked_profile, "tuanthitluoc", null );
//        p1.deviceToken = "8fb159324d3995db9b18bc6b6fcf54a85023f60eecc269f0861c03aca64a191b";
//        p1.deviceType = 0;
//
//        llmsg.add( p1 );
//    }
    
    public static synchronized void add( ReadyPackage pk ){
        llmsg.add( pk );
    }
    
    public static synchronized ReadyPackage poll(){
        return llmsg.poll();
    }
    
    public static void run(){
        
        for( int i = 0; i < Config.RunnerNumber; i++ ){
            Runner r = new Runner();
            r.start();
        }
        
    }
    
}
