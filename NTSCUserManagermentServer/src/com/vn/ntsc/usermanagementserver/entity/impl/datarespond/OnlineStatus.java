/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import com.vn.ntsc.usermanagementserver.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class OnlineStatus implements IEntity {

    public static final String isOnlineKey = "is_online";
    public Boolean isOnline;

    public OnlineStatus(Boolean isOnline) {
        this.isOnline = isOnline;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (isOnline != null){
            jo.put(isOnlineKey, this.isOnline);
        }
        return jo;
    }
    
}
