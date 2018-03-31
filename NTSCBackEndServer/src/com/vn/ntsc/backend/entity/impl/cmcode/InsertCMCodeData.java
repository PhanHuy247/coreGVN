/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.cmcode;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class InsertCMCodeData implements IEntity {

    private static final String cmCodeIdKey = "cm_code_id";
    public String cmCodeId;

    public InsertCMCodeData(String cmCodeId) {
        this.cmCodeId = cmCodeId;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (this.cmCodeId != null) {
            jo.put(cmCodeIdKey, this.cmCodeId);
        }

        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

}
