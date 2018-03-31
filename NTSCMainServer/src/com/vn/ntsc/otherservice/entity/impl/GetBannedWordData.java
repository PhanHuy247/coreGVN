/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class GetBannedWordData implements IEntity {

    private static final String versionKey = "version";
    public Integer version;

    private static final String llKey = "list";
    public List<String> ll;

    public GetBannedWordData(Integer version, List<String> list) {
        this.ll = list;
        this.version = version;
    }    
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.version != null) {
            jo.put(versionKey, this.version);
        }
        if (this.ll != null) {
            JSONArray arr = new JSONArray();
            for (String str : this.ll) {
                arr.add(str);
            }
            jo.put(llKey, arr);
        }
        return jo;
    }
}
