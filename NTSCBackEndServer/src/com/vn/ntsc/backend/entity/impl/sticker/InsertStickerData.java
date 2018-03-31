/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.sticker;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class InsertStickerData implements IEntity {

    private static final String idKey = "id";
    public String id;

    private static final String codeKey = "code";
    public Long code;

    public InsertStickerData(String id, Long code) {
        this.id = id;
        this.code = code;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.id != null) {
            jo.put(idKey, this.id);
        }
        if (this.code != null) {
            jo.put(codeKey, this.code);
        }
        return jo;
    }

}
