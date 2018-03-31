/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.admin;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class AdminSetting implements IEntity{
    
    private static final String timeZoneKey = "time_zone";
    public Integer timezone;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        
        if(this.timezone != null)
            jo.put(timeZoneKey, this.timezone);
                    
        return jo;
    }
    public Map toMap() {
        Map map = new HashMap();
        
        if(this.timezone != null)
            map.put(timeZoneKey, this.timezone);
                    
        return map;
    }

    public AdminSetting(Integer timezone) {
        this.timezone = timezone;
    }
    
}
