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
public class PointSettingData implements IEntity{
    public JSONObject json;

    public PointSettingData(JSONObject json) {
        this.json = json;
    }

    @Override
    public JSONObject toJsonObject() {
        return this.json;
    }
}
