/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc;

import java.io.FileInputStream;
import java.util.Properties;
import eazycommon.util.Util;

/**
 *
 * @author tuannxv00804
 */
public class Config {

    //Configuration for SocketServer
    public static String ChatServerIP = "localhost";
    public static int ChatServerPort = 9118;
    public static int WebSocketPort = 9115;
    //Configuration for ChatLogService
    public static String ChatLogServer_IP = "localhost";
    public static int ChatLogServer_Port = 9116;
    
    //Configuration for Main Server
    public static String MainServer_IP = "localhost";
    public static int MainServer_Port = 9119;  
    
    //Configuration for UMS Server
    public static String UMSServer_IP = "localhost";
    public static int UMSServer_Port = 8090;
    //Configuration for JPNS
    public static String JPNSServer = "localhost";
    public static int JPNSPort = 3221;
    //Configuration for AuthPool
    public static int IdleThreadLatency = 10; //ms
    public static final String ServerName = "server";
    /*
     * Đối với authen thì clientSocket sẽ được sống đúng theo công thức: AuthenTime = MaxTimeToLive * MessageIO.ReadSocketLatency * SleepTime
     * Đối với UserConnection thì clientSocket sẽ chết ngay nếu server không thể gửi heartBeatSignal tới nó.
     */
    public static int MaxTimeToLive = 200; //AuthenticationTime = MaxTimeToLive * MessageIO.ReadSocketLatency * SleepTime

    //Configuration for WorkerFactory
    public static int AuthThreadNumber = 5;
    public static int WorkerThreadNumber = 10;
    public static int LoggingThreadNumber = 2;
    public static boolean IsDebug = true;
    public static final long OneYear = (long) 365 * 24 * 60 * 60 * 1000;
    
    public static int SOCKET_TIMEOUT = 180;
    
    public static void initConfig() {
        try {
            Properties prop = new Properties();
            prop.load( new FileInputStream( "Config.properties" ) );

            ChatServerIP = prop.getProperty( "ChatServerIP" ).trim();
            ChatServerPort = Integer.parseInt( prop.getProperty( "ChatServerPort" ).trim() );
            WebSocketPort = Integer.parseInt( prop.getProperty( "WebSocketPort" ).trim() );

            ChatLogServer_IP = prop.getProperty( "ChatLogServer_IP" ).trim();
            ChatLogServer_Port = Integer.parseInt(prop.getProperty("ChatLogServer_Port" ).trim());

            JPNSServer = prop.getProperty( "JPNSServer" ).trim();
            JPNSPort = Integer.parseInt( prop.getProperty( "JPNSPort" ).trim() );
            
            UMSServer_IP = prop.getProperty( "UMSServer_IP" ).trim();
            UMSServer_Port = Integer.parseInt( prop.getProperty( "UMSServer_Port" ).trim() );            

            MainServer_IP = prop.getProperty( "MainServer_IP" ).trim();
            MainServer_Port = Integer.parseInt( prop.getProperty( "MainServer_Port" ).trim() );            
            
            IdleThreadLatency = Integer.parseInt( prop.getProperty( "IdleThreadLatency" ).trim() );
//            ServerName = prop.getProperty( "ServerName" ).trim();

            MaxTimeToLive = Integer.parseInt( prop.getProperty( "MaxTimeToLive" ).trim() );

            AuthThreadNumber = Integer.parseInt( prop.getProperty( "AuthThreadNumber" ).trim() );
            WorkerThreadNumber = Integer.parseInt( prop.getProperty( "WorkerThreadNumber" ).trim() );
            LoggingThreadNumber = Integer.parseInt( prop.getProperty( "LoggingThreadNumber" ).trim() );
            
            IsDebug = Boolean.parseBoolean( prop.getProperty( "IsDebug" ).trim() );
            
            SOCKET_TIMEOUT = Integer.parseInt( prop.getProperty( "SOCKET_TIMEOUT" ).trim() );

        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
    }
}
