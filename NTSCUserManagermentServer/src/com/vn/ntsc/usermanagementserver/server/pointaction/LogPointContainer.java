/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.pointaction;

import java.util.LinkedList;

/**
 *
 * @author DuongLTD
 */
public class LogPointContainer{
    
    public static final LinkedList<LogPoint> ll = new LinkedList<>();
    
    public static synchronized void add( LogPoint lp ){
        ll.add( lp );
    }
    
    public static synchronized LogPoint poll(){
        return ll.poll();
    }
    
    public static void run(){
        for(int i =0; i < 2; i++){
            LogPointRunner r = new LogPointRunner();
            r.start();
        } 
    }
    
}
