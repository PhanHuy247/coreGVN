/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.automessage;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author HuyDX
 */
public class AutoMessagePackage implements IEntity{

    private static final String userIdKey = "user_id";
    public String userId;
    private static final String contentKey = "content";
    public String content;
    
    public AutoMessagePackage(String userId, String content){
        this.userId = userId;
        this.content = content;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        
        if (this.content != null) {
            jo.put(contentKey, this.content);
        }
        
        return jo;
    }
    
}
