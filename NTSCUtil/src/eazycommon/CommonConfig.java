/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon;

import java.io.FileInputStream;
import java.util.Properties;
import eazycommon.util.Util;
/**
 *
 * @author RuAc0n
 */
public class CommonConfig {

    public static String DB_SERVER = "localhost";
    public static int DB_PORT = 27017;
    
    public static String LOG_LEVEL = "ERROR";
    public static String LOG_FILE = "logger.log";
    public static String LOG_PATTERN = "[%p] %m%n";
    
    public static int CONNECTION_PER_HOST = 1500;
    
    public static int JETTY_MAX_THREAD = 200;
    public static int JETTY_MAX_REQUEST = 100;

    public static void initConfig() {
        try {
            FileInputStream fis = new FileInputStream("Config.properties");
            Properties prop = new Properties();
            prop.load(fis);

            DB_SERVER = prop.getProperty("DB_SERVER").trim();
            DB_PORT = Integer.parseInt(prop.getProperty("DB_PORT").trim());
            
            LOG_LEVEL = prop.getProperty("LOG_LEVEL", "ERROR").trim();
            LOG_FILE = prop.getProperty("LOG_FILE").trim();
            LOG_PATTERN = prop.getProperty("LOG_PATTERN", "[%p] %m%n").trim();
            
            CONNECTION_PER_HOST = Integer.parseInt(prop.getProperty("CONNECTION_PER_HOST", "1500").trim());
            
            try{
                String maxThread = prop.getProperty("JETTY_MAX_THREAD", "200");
                JETTY_MAX_THREAD = Integer.parseInt(maxThread);
            }catch(Exception ex){
            }
            
            try{
                String maxRequest = prop.getProperty("JETTY_MAX_REQUEST", "100");
                JETTY_MAX_REQUEST = Integer.parseInt(maxRequest);
            }catch(Exception ex){
            }
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
           
        }
    }
}
