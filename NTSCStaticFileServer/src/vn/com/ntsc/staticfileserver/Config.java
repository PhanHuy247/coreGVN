/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver;

import java.io.FileInputStream;
import java.util.Properties;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class Config {
    public static int PORT = 9117;
    public static String ANDG_IP = "localhost";
    public static int ANDG_PORT = 9119;
    public static String STREAMING_HOST = "http://10.64.100.22:81/";
    public static int SESSION_TIMEOUT = 20; // 20 minutes
    
    //public static boolean DELETED_VIDEO_TEST_MODE = false;
    //public static String DELETED_VIDEO_TEST_DATE="20090101";
    
    //Configuration for UMS Server
    public static String UMSServer_IP = "localhost";
    public static int UMSServer_Port = 8090;
    
    //Configuration for Chat Server
    public static String ChatServer_IP="localhost";
    public static int ChatServer_Port = 9116;
    
    public static String MainServer_IP="localhost";
    public static int MainServer_Port = 9119;
    public static int VIDEO_EXPIRED_MONTHS = 60;
    
    public static void initConfig(){
        try{
            FileInputStream fis = new FileInputStream( "Config.properties" );
            Properties prop = new Properties();
            prop.load( fis );

            ANDG_IP = prop.getProperty("ANDG_IP", "localhost").trim();
            ANDG_PORT = Integer.parseInt(prop.getProperty("ANDG_PORT", "9119").trim());
            PORT = Integer.parseInt(prop.getProperty("PORT", "9117").trim());

            SESSION_TIMEOUT = Integer.parseInt(prop.getProperty("SESSION_TIMEOUT", "60").trim());
            STREAMING_HOST = prop.getProperty("STREAMING_HOST", "http://10.64.100.22:81/").trim();
            
            UMSServer_IP = prop.getProperty("UMSServer_IP", "localhost").trim();
            UMSServer_Port = Integer.parseInt(prop.getProperty("UMSServer_Port", "8090").trim());
            
            ChatServer_IP = prop.getProperty ("ChatServer_IP", "localhost").trim();
            ChatServer_Port = Integer.parseInt(prop.getProperty("ChatServer_Port", "9116").trim());
            
            MainServer_IP = prop.getProperty ("MainServer_IP", "localhost").trim();
            MainServer_Port = Integer.parseInt(prop.getProperty("MainServer_Port", "9119").trim());
            
            VIDEO_EXPIRED_MONTHS = Integer.parseInt(prop.getProperty("VIDEO_EXPIRED_MONTHS", "60").trim());
            //DELETED_VIDEO_TEST_MODE = Boolean.parseBoolean(prop.getProperty("DELETED_VIDEO_TEST_MODE", "false").trim());
            //DELETED_VIDEO_TEST_DATE = prop.getProperty("DELETED_VIDEO_TEST_DATE","20090101").trim();
            
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
           
        }
    }

}
