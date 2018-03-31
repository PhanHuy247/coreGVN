/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author Admin
 */
public class GetCallWaitingData implements IEntity {

    public static final String videoCallWaitingKey = "video_call_waiting";
    public Boolean videoCall;

    public static final String voiceCallWaitingKey = "voice_call_waiting";
    public Boolean voiceCall;
    
    //Linh add #5227
    private static final String callRequestSettingKey = "call_request_setting";
    public Integer callRequestSetting;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (videoCall != null) {
            jo.put(videoCallWaitingKey, videoCall);
        }
        if (voiceCall != null) {
            jo.put(voiceCallWaitingKey, voiceCall);
        }
        if (callRequestSetting != null) {
            jo.put(callRequestSettingKey, callRequestSetting);
        }
        return jo;
    }

}
