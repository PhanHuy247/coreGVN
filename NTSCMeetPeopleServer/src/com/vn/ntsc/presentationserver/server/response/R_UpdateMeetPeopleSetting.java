/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.response;

import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class R_UpdateMeetPeopleSetting {
    public int code;
    public boolean isSuccess;
    public R_UpdateMeetPeopleSetting(){
        this.code = 0;
        this.isSuccess = true;
    }
    public R_UpdateMeetPeopleSetting(int code, boolean isSuccess) {
        this.code = code;
        this.isSuccess = isSuccess;
    }
    
    public String toJson(){
        JSONObject jo = new JSONObject();
        jo.put(ParamKey.ERROR_CODE, code);
        jo.put("isSuccess", isSuccess);
        return jo.toJSONString();
    }
    
}
