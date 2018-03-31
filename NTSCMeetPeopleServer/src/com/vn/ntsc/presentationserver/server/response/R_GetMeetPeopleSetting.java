/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.response;

import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.QuerySetting;

/**
 *
 * @author Administrator
 */
public class R_GetMeetPeopleSetting {
    public int code;
    public QuerySetting setting;

    public R_GetMeetPeopleSetting() {
        code = 0;
        setting = new QuerySetting();
    }
    
    public String toJson(){
        JSONObject jo = new JSONObject();
        jo.put(ParamKey.ERROR_CODE, code);
        jo.put(ParamKey.DATA, setting.toJsonObject());
        return jo.toJSONString();
    }
    
}
