/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.server.session;

import java.util.UUID;
import eazycommon.util.Util;
import com.vn.ntsc.Config;
import eazycommon.constant.Constant;
import eazycommon.util.DateFormat;

/**
 *
 * @author tuannxv00804
 */
public class Session {
    private static final long SESSION_TIME_OUT_MILLISECONDS = (long)Config.SessionTimeOut * 60 * 1000;
    private static final long WEB_SESSION_TIME_OUT_MILLISECONDS = (long)Config.WebSessionTimeOut * Constant.A_DAY;
    
    public String token;
    public String oldToken;
    public String userID;
    // if type != null, this is token of administrator
    public String type;
    
    public int timeToLive;
    public int serverCode;
    public boolean normalUser;
    public boolean changeSettingToken = false;
    public String applicationVersion;
    public Integer applicationType;
    public String deviceId;
    public boolean inUse = true;
    public long sessionExpire = 0;

    public Session() {
    }
    
    public Session( String userID, boolean normalUser, String applicationVersion, Integer applicationType, String deviceId){
        this.token = UUID.randomUUID().toString();
        this.userID = userID;

        this.timeToLive = 0;
        this.serverCode = 0;
        this.type = null;
        this.normalUser = normalUser;
        this.applicationVersion = applicationVersion;
        this.applicationType = applicationType;
        this.deviceId = deviceId;
        this.sessionExpire = Util.currentTime() + SESSION_TIME_OUT_MILLISECONDS;
    }
    
    public Session( String userID, boolean normalUser, String applicationVersion, Integer applicationType, String deviceId,String oldToken){
        this.token = UUID.randomUUID().toString();
        this.oldToken = oldToken;
        this.userID = userID;

        this.timeToLive = 0;
        this.serverCode = 0;
        this.type = null;
        this.normalUser = normalUser;
        this.applicationVersion = applicationVersion;
        this.applicationType = applicationType;
        this.deviceId = deviceId;
        this.sessionExpire = Util.currentTime() + SESSION_TIME_OUT_MILLISECONDS;
    }
    
    public Session( String token, String userID, boolean normalUser, String applicationVersion, Integer applicationType){
        this.token = token;
        this.userID = userID;

        this.timeToLive = 0;
        this.serverCode = 0;
        this.type = null;
        this.normalUser = normalUser;
        this.applicationVersion = applicationVersion;
        this.applicationType = applicationType;
    }

    public Session( String token ,String userID, String type){
        this.token = token;
        this.userID = userID;
        this.type = type;

        this.timeToLive = 0;
        this.serverCode = 0;
    }
    
    public Session(String userID, String type ){
        this.token = UUID.randomUUID().toString();
        this.userID = userID;
        this.type = type;
        this.timeToLive = 0;
        this.serverCode = 0;
        this.normalUser = true;
        this.sessionExpire = Util.currentTime() + SESSION_TIME_OUT_MILLISECONDS;
    }    
    
    public void resetExpire(){
        if (this.applicationType != null && this.applicationType == Constant.APPLICATION_TYPE.WEB_APPLICATION){
            this.sessionExpire = Util.currentTime() + WEB_SESSION_TIME_OUT_MILLISECONDS;
        }
        else {
            this.sessionExpire = Util.currentTime() + SESSION_TIME_OUT_MILLISECONDS;
        }
    }
}
