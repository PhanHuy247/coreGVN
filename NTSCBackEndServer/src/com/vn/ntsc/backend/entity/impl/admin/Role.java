/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.admin;

import com.vn.ntsc.backend.entity.IEntity;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class Role implements IEntity{
    
    private static final String idKey = "id";
    public String id;

    private static final String nameKey = "name";
    public String name;
    
    private static final String listGroupKey = "lst_group";
    public List<String> listGroup;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        
        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.name != null)
            jo.put(nameKey, name);
        if(this.listGroup != null){
            JSONArray arr = new JSONArray();
            for(String str : this.listGroup)
                arr.add(str);
            jo.put(listGroupKey, arr);
        }
                    
        return jo;
    }

    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
}
