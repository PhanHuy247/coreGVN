/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.screen;

import com.vn.ntsc.backend.entity.IEntity;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class ScreenGroup implements IEntity{
    
    private static final String idKey = "id";
    public String id;
    
    private static final String titleKey = "title";
    public String title;
    
    private static final String flagKey = "flag";
    public Integer flag;
    
    private static final String nameKey = "name";
    public String name;
    
    private static final String orderKey = "order";
    public Integer order;
    
    private static final String rolesKey = "roles";
    public List<String> roles;
    
    private static final String screensKey = "screens";
    public List<IEntity> screens;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        
        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.title != null)
            jo.put(titleKey, this.title);
        if(this.flag != null)
            jo.put(flagKey, this.flag);
        if(this.name != null)
            jo.put(nameKey, name);
        if(this.order != null)
            jo.put(orderKey, order);
        if(this.roles != null){
            JSONArray arr = new JSONArray();
            for(String str : this.roles)
                arr.add(str);
            jo.put(rolesKey, arr);
        }
        if(this.screens != null){
            JSONArray arr = new JSONArray();
            for(IEntity scr : this.screens)
               arr.add(scr.toJsonObject());
            jo.put(screensKey, arr);
        }
        
        return jo;
    }

    public ScreenGroup(String id, String title, Integer flag, String name, Integer order) {
        this.id = id;
        this.title = title;
        this.flag = flag;
        this.name = name;
        this.order = order;
    }
}
