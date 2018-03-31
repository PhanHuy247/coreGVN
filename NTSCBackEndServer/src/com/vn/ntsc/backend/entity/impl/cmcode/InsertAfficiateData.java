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
public class InsertAfficiateData implements IEntity{

    private static final String affIdKey = "aff_id";
    public String affId;

    public InsertAfficiateData(String affId) {
        this.affId = affId;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (this.affId != null) {
            jo.put(affIdKey, this.affId);
        }

        return jo;
    }

}
