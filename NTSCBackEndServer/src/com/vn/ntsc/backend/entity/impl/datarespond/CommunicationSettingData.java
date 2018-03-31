/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class CommunicationSettingData implements IEntity {

    public int code;
    
    private static final String tabKey = "tab";
    public String tab;

    public CommunicationSettingData(int code, String tab) {
        this.code = code;
        this.tab = tab;
    }

    public CommunicationSettingData(int code) {
        this.code = code;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.tab != null) {
            jo.put(tabKey, this.tab);
        }

        return jo;
    }
}
