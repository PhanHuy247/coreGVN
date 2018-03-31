/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.user;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class SimpleUser implements IEntity {

    private static final String userIdKey = "user_id";
    public String userId;

    private static final String userNameKey = "user_name";
    public String userName;

    private static final String emailKey = "email";
    public String email;

    private static final String userTypeKey = "user_type";
    public Long userType;

    private static final String cmCodeKey = "cm_code";
    public String cmCode;
    
    private static final String videoCallKey = "video_call";
    public Boolean videoCall;
    
    private static final String voiceCallKey = "voice_call";
    public Boolean voiceCall;

    public SimpleUser(String userId, String userName, String email, Long userType, String cmCode, Boolean videoCall, Boolean voiceCall) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.userType = userType;
        this.cmCode = cmCode;
        this.videoCall = videoCall;
        this.voiceCall = voiceCall;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (email != null) {
            jo.put(emailKey, this.email);
        }
        if (userType != null) {
            jo.put(userTypeKey, this.userType);
        }
        if (cmCode != null) {
            jo.put(cmCodeKey, this.cmCode);
        }
        if (videoCall != null) {
            jo.put(videoCallKey, this.videoCall);
        }
        if (voiceCall != null) {
            jo.put(voiceCallKey, this.voiceCall);
        }

        return jo;
    }

}
