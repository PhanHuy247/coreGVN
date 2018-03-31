/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.datarespond;

import com.vn.ntsc.buzzserver.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class ShareNumberData implements IEntity{

    private static final String shareNumberKey = "share_number";
    public Integer shareNumber;

    public ShareNumberData(Integer shareNumber) {
        this.shareNumber = shareNumber;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (shareNumber != null) {
            jo.put(shareNumberKey, shareNumber);
        }

        return jo;
    }
    
}
