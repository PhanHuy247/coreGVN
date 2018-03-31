/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.freepoint;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class FreePoint implements IEntity{
    private static final String idKey = "id";
    public String id;

    private static final String freePointNameKey = "free_point_name";
    public String name;
    
    private static final String freePointNumberKey = "free_point_number";
    public int number;
    
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
        if(this.id != null)
            jo.put(idKey, this.id);
        jo.put(freePointNameKey, this.name);
        jo.put(freePointNumberKey, this.number);
        
        return jo;
    }
    
    public FreePoint(String id, String name, int number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public FreePoint(String name, int number) {
        this.name = name;
        this.number = number;
    }    
    
    public FreePoint() {
    }

    
    
}
