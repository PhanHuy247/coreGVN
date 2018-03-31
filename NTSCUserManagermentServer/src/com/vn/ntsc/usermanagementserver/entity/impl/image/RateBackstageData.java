/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.image;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class RateBackstageData implements IEntity{

    private static final String rateKey = "rate";
    public Double rate;

    public RateBackstageData(Double rate) {
        this.rate = rate;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.rate != null) {
            jo.put(rateKey, this.rate);
        }

        return jo;
    }

}
