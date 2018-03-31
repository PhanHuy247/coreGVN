/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.admin;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class Admin implements IEntity{
    private static final String idKey = "user_id";
    public String id;

    private static final String emailKey = "email";
    public String email;
    
    private static final String nameKey = "name";
    public String name;

    private static final String roleIdKey = "role_id";
    public String roleId;    
    
    private static final String flagKey = "flag";
    public Integer flag;

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.email != null)
            jo.put(emailKey, this.email);
        if(this.name != null)
            jo.put(nameKey, this.name);
        if(this.roleId != null)
            jo.put(roleIdKey, this.roleId);
        if(this.flag != null)
            jo.put(flagKey, this.flag);
        return jo;
    }
 
    public Admin(String id, String email, String roleId, String name, Integer flag) {
        this.id = id;
        this.email = email;
        this.roleId = roleId;
        this.name = name;
        this.flag = flag;
    }

    public Admin() {
    }

    
    
}
