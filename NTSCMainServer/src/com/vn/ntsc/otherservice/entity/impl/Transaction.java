/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.entity.impl;

import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class Transaction implements IEntity{
    private static final String userIdKey = "user_id";
    public String userId;

    private static final String pointKey = "point";
    public Integer point;

    private static final String timeKey = "time";
    public String time;

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(userId != null)
            jo.put(userIdKey, this.userId);
        if(this.point != null)
            jo.put(pointKey, this.point);
        if(this.time != null)
            jo.put(timeKey, this.time);

        return jo;
    }

}
