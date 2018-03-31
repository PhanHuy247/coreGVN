/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.response;

import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class R_UpdateUser {
    public boolean isSuccess;

    public R_UpdateUser() {
        this.isSuccess = false;
    }

    public R_UpdateUser(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    
    
    
    public String toJson(){
        JSONObject jo = new JSONObject();
        
        jo.put("isSuccess", isSuccess);
        return jo.toJSONString();
    }
}
