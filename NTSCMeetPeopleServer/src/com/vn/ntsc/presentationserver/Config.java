/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver;

import java.io.FileInputStream;
import java.util.Properties;
import eazycommon.util.Util;

public class Config {
    
    public static int ServerPort = 2612;

     static {
        
        try {
            FileInputStream fis = new FileInputStream( "Config.properties" );
            Properties prop = new Properties();
            prop.load( fis );

            ServerPort = Integer.parseInt(prop.getProperty("ServerPort").trim());

        } catch( Exception ex ) {
            Util.addErrorLog(ex);
           
        }

    }
 
    public static void initConfig(){
        // TODO 
    }
}
