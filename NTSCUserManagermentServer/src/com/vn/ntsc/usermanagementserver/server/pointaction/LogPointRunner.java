/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.pointaction;

import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogPointDAO;


/**
 *
 * @author DuongLTD
 */
public class LogPointRunner extends Thread{
    
    public static void log( LogPoint log ){
        try{            
            if(log.point != 0)
                UserDAO.changePoint(log.userId, log.point);
            LogPointDAO.addLog(log);
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
           
        }
    }
    
    @Override
    public void run(){
        while( true ){
            try{
                LogPoint log = LogPointContainer.poll();
                if(log != null){
                    log(log);
                }else{
                    sleep();
                }
                
            } catch( Exception ex ) {
               
            }
        }
    }
    
    public static void sleep(){
        try{
            Thread.sleep( 5 );
        } catch( Exception ex ) {
           
        }
    }
}
