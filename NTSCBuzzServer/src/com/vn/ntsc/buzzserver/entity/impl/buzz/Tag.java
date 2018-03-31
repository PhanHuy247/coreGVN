/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.buzz;

import com.vn.ntsc.buzzserver.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class Tag implements IEntity{
    
    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String userNameKey = "user_name";
    public String userName;
    
    private static final String flagKey = "flag";
    public Integer flag;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(this.userId != null)
            jo.put(userIdKey, this.userId);
        if(this.userName != null)
            jo.put(userNameKey, this.userName);
        if(this.flag != null)
            jo.put(flagKey, this.flag);
        return  jo;
    }
    
}
