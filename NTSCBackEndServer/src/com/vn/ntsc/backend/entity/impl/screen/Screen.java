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
public class Screen implements IEntity{
    
    private static final String idKey = "id";
    public String id;
    
    private static final String titleKey = "title";
    public String title;
    
    private static final String flagKey = "flag";
    public Integer flag;
    
    private static final String pathKey = "path";
    public String path;
    
    private static final String nameKey = "name";
    public String name;

    private static final String groupIdKey = "group_id";
    public String groupId;    
    
    private static final String controllerKey = "controller";
    public String controller;
    
    private static final String orderKey = "order";
    public Integer order;
    
    private static final String listApiKey = "lst_api";
    public List<String> listApi;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        
        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.title != null)
            jo.put(titleKey, this.title);
        if(this.flag != null)
            jo.put(flagKey, this.flag);
        if(this.path != null)
            jo.put(pathKey, path);
        if(this.name != null)
            jo.put(nameKey, name);
        if(this.groupId != null)
            jo.put(groupIdKey, groupId);
        if(this.controller != null)
            jo.put(controllerKey, controller);
        if(this.order != null)
            jo.put(orderKey, order);
        if(this.listApi != null){
            JSONArray arr = new JSONArray();
            for(String str : this.listApi){
                arr.add(str);
            }
            jo.put(listApiKey, arr);
        }
        
        return jo;
    }

    public Screen(String id, String title, Integer flag, String name, String path, String groupId, String controller, Integer order) {
        this.id = id;
        this.title = title;
        this.flag = flag;
        this.name = name;
        this.path = path;
        this.groupId = groupId;
        this.controller = controller;
        this.order = order;
    }

}
