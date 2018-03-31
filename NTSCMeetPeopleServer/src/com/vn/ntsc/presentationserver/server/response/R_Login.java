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
public class R_Login {
    public boolean isChange;

    public R_Login() {
        this.isChange = false;
    }

    public R_Login(boolean isSuccess) {
        this.isChange = isSuccess;
    }
    
    
    
    public String toJson(){
        JSONObject jo = new JSONObject();
        
        jo.put(ParamKey.IS_CHANGE, isChange);
        return jo.toJSONString();
    }
}
