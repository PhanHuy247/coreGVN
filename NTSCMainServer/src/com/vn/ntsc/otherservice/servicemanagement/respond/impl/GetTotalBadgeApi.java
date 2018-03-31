/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import com.vn.ntsc.otherservice.entity.IEntity;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class GetTotalBadgeApi implements IEntity{
     private static final String totalKey = "total_badge";
    public Long total;
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.total != null) {
            jo.put(totalKey, this.total);
        }
        return jo;
    }

    public GetTotalBadgeApi(Long total) {
        this.total = total;
    }
}
