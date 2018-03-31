/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntsc.tool.constants;

import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author HuyDX
 */
public class Config {
    
    public static String DB_SERVER = "localhost";  
    /**
     * INSERT_TYPE
     *  1: repeat one day 
     *  2: automatically repeat all day in year.
     */
    public static int INSERT_TYPE = 1;
    public static int DAYS_TO_REPEAT = 10;
    public static int DEFAULT_YEAR = 2010;
    public static int DEFAULT_MONTH = 1;
    public static int DEFAULT_DAY = 1;
    
    public static int NUMBER_OF_YEAR = 1;
    public static int NUMBER_OF_MONTHS = 12;
    public static int NUMBER_OF_DAYS = 28;
    
    public static String VIDEO_DELETE_TIME = "20100101010100"; // yyyyMMddHHmmss
    public static int MOCKUP_TYPE = 1;
    
    public static void initConfig(){
        try{
            FileInputStream fis = new FileInputStream( "Config_tool.properties" );
            Properties prop = new Properties();
            prop.load( fis );

            DB_SERVER = prop.getProperty("DB_SERVER").trim();            
            
            INSERT_TYPE = Integer.parseInt(prop.getProperty("INSERT_TYPE").trim());
            DAYS_TO_REPEAT = Integer.parseInt(prop.getProperty("DAYS_TO_REPEAT").trim());

            DEFAULT_YEAR = Integer.parseInt(prop.getProperty("DEFAULT_YEAR").trim());
            DEFAULT_MONTH = Integer.parseInt(prop.getProperty("DEFAULT_MONTH").trim());
            DEFAULT_DAY = Integer.parseInt(prop.getProperty("DEFAULT_DAY").trim());

            NUMBER_OF_YEAR = Integer.parseInt(prop.getProperty("NUMBER_OF_YEAR").trim());
            NUMBER_OF_MONTHS = Integer.parseInt(prop.getProperty("NUMBER_OF_MONTHS").trim());
            NUMBER_OF_DAYS = Integer.parseInt(prop.getProperty("NUMBER_OF_DAYS").trim());
            
            VIDEO_DELETE_TIME = prop.getProperty("VIDEO_DELETE_TIME", "20100101010100").trim();
            MOCKUP_TYPE = Integer.parseInt(prop.getProperty("MOCKUP_TYPE").trim());

        } catch( Exception ex ) {
            ex.printStackTrace();

        }
    }
}
