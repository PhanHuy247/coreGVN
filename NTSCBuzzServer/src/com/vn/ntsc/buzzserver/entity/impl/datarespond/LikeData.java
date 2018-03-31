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
public class LikeData implements IEntity {

    private static final String buzzIdKey = "buzz_id";
    public String buzz_id;

    private static final String timeKey = "time";
    public Long time;

    private static final String buzzOwnerKey = "buzz_owner_id";
    public String buzz_owner_id;

    private static final String isNotiKey = "is_noti";
    public Integer isNoti;
    
    public LikeData(){
        
    }

    public LikeData(String buzzId,Long time, String buzzOwner, Integer isNoti) {
        this.buzz_id = buzzId;
        this.time = time;
        this.buzz_owner_id = buzzOwner;
        this.isNoti = isNoti;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (buzz_id != null) {
            jo.put(buzzIdKey, buzz_id);
        }
        if (time != null) {
            jo.put(timeKey, time);
        }
        if (buzz_owner_id != null) {
            jo.put(buzzOwnerKey, buzz_owner_id);
        }
        if(isNoti != null)
            jo.put(isNotiKey, isNoti);

        return jo;
    }

}
