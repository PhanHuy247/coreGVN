/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.workers;

import com.vn.ntsc.jpns.Config;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author hoangnh
 */
public class BuzzNotificationContainer {
    private static final ConcurrentLinkedQueue<BuzzNotificationPackage> listNotificationRequest = new ConcurrentLinkedQueue<>();
    
    public static synchronized void add( BuzzNotificationPackage pk ){
        listNotificationRequest.add( pk );
    }
    
    public static synchronized BuzzNotificationPackage poll(){
        return listNotificationRequest.poll();
    }
    
    public static void run(){
        for( int i = 0; i < Config.RunnerNumber; i++ ){
            SendBuzzRunner r = new SendBuzzRunner();
            r.start();
        }
    }
}
