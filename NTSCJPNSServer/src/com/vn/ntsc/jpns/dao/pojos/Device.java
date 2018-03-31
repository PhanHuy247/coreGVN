/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.dao.pojos;

/**
 *
 * @author tuannxv00804
 */
public class Device {

    public static final String UserID = "userid";
    public static final String Username = "username";
    public static final String DeviceType = "deviceType";
    public static final String DeviceToken = "deviceToken";
    public static final String IosApplicationType = "iosApplicationType";
    public static final String DeviceID = "deviceid";
    public static final String CheckID = "checkid";
    public static final String ApplicationID = "applicationid";
    public static final String Voip_notify_token = "voip_notify_token";

    public String userid;
    public String username;
    public int deviceType;
    public String deviceToken;
    public Integer iosApplicationType;
    public String deviceId;
    public String checkid;
    public String applicationId;
    public String voip_notify_token;

    public Device() {

    }

    public Device(String userid, String username, int deviceType, String deviceToken, String applicationId, String voip_notify_token) {

        this.userid = userid;
        this.username = username;
        this.deviceType = deviceType;
        this.deviceToken = deviceToken;
        this.applicationId = applicationId;
        this.voip_notify_token = voip_notify_token;

    }

    public Device(String userid, String username, int deviceType, String deviceToken, Integer iosApplicationType, String deviceId, String checkid, String applicationId, String voip_notify_token) {
        this.userid = userid;
        this.username = username;
        this.deviceType = deviceType;
        this.deviceToken = deviceToken;
        this.iosApplicationType = iosApplicationType;
        this.deviceId = deviceId;
        this.checkid = checkid;
        this.applicationId = applicationId;
        this.voip_notify_token = voip_notify_token;
    }

    @Override
    public String toString() {
        return "User{" + "userid=" + userid + ", deviceType=" + deviceType + ", deviceToken=" + deviceToken + '}';
    }

}
