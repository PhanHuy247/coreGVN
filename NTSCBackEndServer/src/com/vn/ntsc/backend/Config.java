/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend;

import eazycommon.util.Util;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author tuannxv00804
 */
public class Config {

    public static String DB_SERVER = "localhost";
    public static int DB_PORT = 27017;

    public static int PORT = 9120;

    public static String ChatServerIP = "localhost";
    public static int ChatServerPort = 9116;

    public static String MainServerIp = "localhost";
    public static int MainServerPort = 3221;
    
    public static String SERVER_ADDRESS = "http://192.168.6.239:80/";
    public static String STREAMING_HOST = "http://192.168.6.222:81/";


    public static void initConfig() {
        try {
            FileInputStream fis = new FileInputStream("Config.properties");
            Properties prop = new Properties();
            prop.load(fis);

            PORT = Integer.parseInt(prop.getProperty("PORT").trim());
            DB_SERVER = prop.getProperty("DB_SERVER").trim();
            DB_PORT = Integer.parseInt(prop.getProperty("DB_PORT").trim());

            ChatServerIP = prop.getProperty("ChatServerIP").trim();
            ChatServerPort = Integer.parseInt(prop.getProperty("ChatServerPort").trim());
            
            MainServerIp = prop.getProperty("MainServerIp").trim();
            MainServerPort = Integer.parseInt(prop.getProperty("MainServerPort").trim());
            
            SERVER_ADDRESS = prop.getProperty("SERVER_ADDRESS").trim();
            STREAMING_HOST = prop.getProperty("STREAMING_HOST").trim();

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

}
