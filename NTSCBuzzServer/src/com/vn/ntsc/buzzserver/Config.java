/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver;

import java.io.FileInputStream;
import java.util.Properties;
import eazycommon.util.Util;

/**
 *
 * @author DUONGLTD
 */
public class Config {

    public static String MAIN_IP = "localhost";
    public static int MAIN_PORT = 9119;
    public static int PORT = 9115;
    public static String DB_SERVER = "localhost";
//    public static String DB_SERVER = "192.168.6.226";
    public static int DB_PORT = 27017; 
    
    public static void initConfig(){
        try{
            FileInputStream fis = new FileInputStream( "Config.properties" );
            Properties prop = new Properties();
            prop.load( fis );

            MAIN_IP = prop.getProperty("MAIN_IP").trim();
            MAIN_PORT = Integer.parseInt(prop.getProperty("MAIN_PORT").trim());
            PORT = Integer.parseInt(prop.getProperty("PORT").trim());

            DB_SERVER = prop.getProperty("DB_SERVER").trim();
            DB_PORT = Integer.parseInt(prop.getProperty("DB_PORT").trim());

        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }

}
