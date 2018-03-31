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
 * @author RuAc0n
 */
public class DeleteBuzzData implements IEntity {

    private static final String isAvaKey = "is_ava";
    Integer is_ava;
    
    public DeleteBuzzData(Integer is_ava) {
        this.is_ava = is_ava;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.is_ava != null) {
            jo.put(isAvaKey, this.is_ava);
        }
        return jo;
    }
}
