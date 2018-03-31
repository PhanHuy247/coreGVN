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
public class AddFavouristData implements IEntity {

    private static final String isUnlckKey = "is_unlck";
    Long is_unlck;

    private static final String isNotiKey = "is_noti";
    Integer isNoti;

    public AddFavouristData(Long is_unlck, Integer isNoti) {
        this.is_unlck = is_unlck;
        this.isNoti = isNoti;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.is_unlck != null) {
            jo.put(isUnlckKey, this.is_unlck);
        }

        if (this.isNoti != null) {
            jo.put(isNotiKey, isNoti);
        }

        return jo;
    }
}
