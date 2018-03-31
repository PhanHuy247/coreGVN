/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class StampData implements IEntity {

    private static final String idKey = "id";
    public String id;

    private static final String codeKey = "code";
    public Long code;

    public StampData(String id) {
        this.id = id;
    }

    public StampData(String id, Long stickerCode) {
        this.id = id;
        this.code = stickerCode;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (id != null) {
            jo.put(idKey, id);
        }
        if (code != null) {
            jo.put(codeKey, code);
        }

        return jo;
    }

}
