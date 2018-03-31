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
public class ReviewUserData implements IEntity {
    
    private static final String isNotiKey = "is_noti";
    public Integer isNoti;
    
    public ReviewUserData( Integer isNoti) {
        this.isNoti = isNoti;
    }

    @Override
    public JSONObject toJsonObject() {
        
        JSONObject jo = new JSONObject();

        if (isNoti != null) {
            jo.put(isNotiKey, isNoti);
        }
        return jo;
    }

}
