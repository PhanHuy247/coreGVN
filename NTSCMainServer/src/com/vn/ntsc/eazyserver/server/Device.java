/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.server;

import java.util.LinkedList;

/**
 *
 * @author hungdt
 */
public class Device {
    
    public static final String UserID = "userid";
    public static final String Username = "username";
    public static final String DeviceType = "deviceType";
    public static final String DeviceToken = "deviceToken";
     public static final String DeviceID = "deviceid";
     public static final String CheckID = "checkid";

    public String userid;
    public String username;
    public int deviceType;
    public String deviceToken;
    public String deviceid;
    public String checkid;

    public Device(){
        
    }
    
    public Device( String userid, String username, int deviceType, String deviceToken, String deviceid, String checkid ){
        this.userid = userid;
        this.username = username;
        this.deviceType = deviceType;
        this.deviceToken = deviceToken;
        this.deviceid = deviceid;
        this.checkid = checkid;
        
    }
    
    @Override
    public String toString() {
        return "User{" + "userid=" + userid + ", deviceType=" + deviceType + ", deviceToken=" + deviceToken  + ", deviceid=" + deviceid +'}';
    }
    
    
}
