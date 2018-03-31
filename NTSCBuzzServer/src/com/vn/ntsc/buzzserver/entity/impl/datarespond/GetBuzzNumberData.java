/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class GetBuzzNumberData implements IEntity {
    
    private static final String buzzNumberKey = "buzz_number";
    public Integer buzzNumber;

    public GetBuzzNumberData(Integer buzzNumber) {
        this.buzzNumber = buzzNumber;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (buzzNumber != null) {
            jo.put(buzzNumberKey, buzzNumber);
        }

        return jo;
    }
}
