/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class ListSimpleUserData implements IEntity {

    private static final String llKey = "list";
    public List<SimpleUser> ll;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.ll != null) {
            JSONArray arr = new JSONArray();
            for (SimpleUser user : this.ll) {
                arr.add(user.toJsonObject());
            }
            jo.put(llKey, arr);
        }
        return jo;
    }

    public ListSimpleUserData( Map<String, IEntity> map) {
        List<SimpleUser> listData = new ArrayList<>();
        for (Map.Entry<String, IEntity> pair : map.entrySet()) {
            IEntity user = pair.getValue();
            User u = (User) user;
            String userId = u.userId;
            String username = u.username;
            String cmCode = u.cmCode;
            Long userType = u.userType;
            String email = u.email;
            listData.add(new SimpleUser(userId, username, email, userType, cmCode, u.videoCall, u.voiceCall));
        }
        this.ll = listData;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }
}
