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
public class ReportImageData implements IEntity {

    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String isAppearKey = "is_appear";
    public Integer isAppear;

    private static final String buzzIdKey = "buzz_id";
    public String buzzId;    
    
    private static final String isAvaKey = "is_ava";
    public Integer isAva;
    
    
    public ReportImageData(String userId, Integer isAppear, String buzzId, Integer isAva) {
        this.userId = userId;
        this.isAppear = isAppear;
        this.buzzId = buzzId;
        this.isAva = isAva;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        
        if (this.isAppear != null) {
            jo.put(isAppearKey, this.isAppear);
        }
        if (this.buzzId != null) {
            jo.put(buzzIdKey, this.buzzId);
        }
        if (this.isAva != null) {
            jo.put(isAvaKey, this.isAva);
        }

        return jo;
    }

}
