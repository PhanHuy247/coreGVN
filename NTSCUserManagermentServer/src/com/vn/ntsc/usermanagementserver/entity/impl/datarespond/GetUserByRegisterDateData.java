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
 * @author Rua
 */
public class GetUserByRegisterDateData implements IEntity {
    private static final String cmCodeKey = "cm_code";
    public String cmCode;
    
    private static final String userIdKey = "user_id";
    public String userId;
    
    public GetUserByRegisterDateData(){
    }

    public GetUserByRegisterDateData(String userId, String cmCode) {
        this.cmCode = cmCode;
        this.userId = userId;
    }
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(userId != null){
            jo.put(userIdKey, userId);
        }
        if(cmCode != null){
            jo.put(cmCodeKey, cmCode);
        }
        return jo;
    }
}
