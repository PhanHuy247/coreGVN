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
public class SendGiftData implements IEntity {

    private static final String buzzIdKey = "buzz_id";
    public String buzz_id;

    private static final String timeKey = "time";
    public Long time;

    private static final String userIdKey = "user_id";
    public String user_id;

    public SendGiftData(String buzzId, Long time, String user_id) {
        this.buzz_id = buzzId;
        this.time = time;
        this.user_id = user_id;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (buzz_id != null) {
            jo.put(buzzIdKey, buzz_id);
        }
        if (time != null) {
            jo.put(timeKey, time);
        }
        if (user_id != null) {
            jo.put(userIdKey, user_id);
        }

        return jo;
    }

}
