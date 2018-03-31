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
public class LogCallData implements IEntity {

    public static final String CALL_TYPE_Key = "call_type";
    public Integer callType;
    public static final String PARTNER_ID_Key = "partner_id";
    public String partnerId;
    public static final String START_TIME_Key = "start_time";
    public String startTime;
    public static final String DURATION_Key = "duration";
    public Long duration;
    public static final String PARTNER_RESPOND_Key = "partner_respond";
    public Integer partnerRes;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (callType != null) {
            jo.put(CALL_TYPE_Key, callType);
        }
        if (partnerId != null) {
            jo.put(PARTNER_ID_Key, partnerId);
        }
        if (startTime != null) {
            jo.put(START_TIME_Key, startTime);
        }
        if (duration != null) {
            jo.put(DURATION_Key, duration);
        }
        if (partnerRes != null) {
            jo.put(PARTNER_RESPOND_Key, partnerRes);
        }

        return jo;
    }

}
