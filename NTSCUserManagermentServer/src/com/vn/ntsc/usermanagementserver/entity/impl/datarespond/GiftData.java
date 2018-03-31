/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class GiftData implements IEntity {

    private static final String giftIdKey = "gift_id";
    public String giftId;

    private static final String giftNuberKey = "gift_num";
    public Integer giftNumber;

    public GiftData(String giftId, Integer giftNumber) {
        this.giftId = giftId;
        this.giftNumber = giftNumber;
    }

    
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(giftId != null){
            jo.put(giftIdKey, giftId);
        }
        if(giftNumber != null){
            jo.put(giftNuberKey, giftNumber);
        }
        return jo;
    }
}
