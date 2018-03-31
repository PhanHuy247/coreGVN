/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.response;

import org.json.simple.JSONObject;

/**
 *
 * @author tuannxv00804
 */
public class R_RemoveUser {
    public boolean isSuccess;
    
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
        jo.put("isSuccess", isSuccess);
        return jo;
    }
}
