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
public class SystemAccount implements IEntity{
    
    private static final String idKey = "id";
    public String id;

    private static final String nameKey = "name";
    public String name;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        
        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.name != null)
            jo.put(nameKey, name);
                    
        return jo;
    }

    public SystemAccount(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
}
