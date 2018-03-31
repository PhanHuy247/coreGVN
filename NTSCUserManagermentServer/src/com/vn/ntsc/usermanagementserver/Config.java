/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver;

import eazycommon.util.Util;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author RuAc0n
 */
public class Config {

    //public static String andGIp = "192.168.6.226";
    public static String andGIp = "localhost";
    public static int andGPort = 9119;
    public static int port = 8090;
    
    public static String EMAIL = "support@eazy-app.com";
    public static String EMAIL_PASSWORD = "q4w1jrg9pka567ai";
    public static String EMAIL_HOST = "10.202.3.100";
    public static String EMAIL_PORT = "587";
    
    public static int MAX_USER_TEMPLATE_NUMBER = 20;
   
    public static String INVITATION_URL = "http://" + andGIp + ":" + andGPort + "/ivt_code=";
    public static String HOME_PAGE_URL = "http://" + andGIp + ":" + andGPort + "/id=";     
    public static String STREAMING_HOST = "http://10.64.100.22:81/";
    public static void initConfig() {
        try {
            FileInputStream fis = new FileInputStream("Config.properties");
            Properties prop = new Properties();
            prop.load(fis);

            andGIp = prop.getProperty("andGIp").trim();
            andGPort = Integer.parseInt(prop.getProperty("andGPort"));
            port = Integer.parseInt(prop.getProperty("port").trim());

            EMAIL = prop.getProperty("EMAIL").trim();
            EMAIL_PASSWORD = prop.getProperty("EMAIL_PASSWORD").trim();
            EMAIL_HOST = prop.getProperty("EMAIL_HOST").trim();
            EMAIL_PORT = prop.getProperty("EMAIL_PORT").trim();
            
            INVITATION_URL = "http://" + andGIp + ":" + andGPort + "/ivt_code=";
            HOME_PAGE_URL = "http://" + andGIp + ":" + andGPort + "/id="; 
            
            MAX_USER_TEMPLATE_NUMBER = Integer.parseInt(prop.getProperty("MAX_USER_TEMPLATE_NUMBER").trim());
            STREAMING_HOST = prop.getProperty("STREAMING_HOST").trim();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
           
        }
    }
       
}
