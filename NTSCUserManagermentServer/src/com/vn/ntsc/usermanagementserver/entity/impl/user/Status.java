/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.user;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class Status implements IEntity{

    private static final String userIdKey = "user_id";
    public String userId;

    private static final String statusKey = "status";
    public String status;

    public Status(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.status != null) {
            jo.put(statusKey, this.status);
        }

        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

}
