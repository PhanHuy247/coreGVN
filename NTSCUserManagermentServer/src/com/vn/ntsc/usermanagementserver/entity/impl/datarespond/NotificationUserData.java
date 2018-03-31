/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class NotificationUserData implements IEntity {

    private static final String userNameKey = "user_name";
    public String user_name;

    private static final String listEmailKey = "ListEmail";
    public List<String> ListEmail;

    public NotificationUserData(String user_name, List<String> ListEmail) {
        this.user_name = user_name;
        this.ListEmail = ListEmail;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.user_name != null) {
            jo.put(userNameKey, this.user_name);
        }
        if (this.ListEmail != null) {
            JSONArray arr = new JSONArray();
            for (String ListEmail1 : this.ListEmail) {
                arr.add(ListEmail1);
            }
            jo.put(listEmailKey, arr);
        }
        return jo;
    }
}
