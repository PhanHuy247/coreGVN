/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.user.extend;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.impl.user.User;

/**
 *
 * @author Admin
 */
public class Male extends User {

    private static final String hobbyKey = "hobby";
    public String hobby;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject(); //To change body of generated methods, choose Tools | Templates.
        if (hobby != null) {
            jo.put(hobbyKey, hobby);
        }

        return jo;

    }

}
