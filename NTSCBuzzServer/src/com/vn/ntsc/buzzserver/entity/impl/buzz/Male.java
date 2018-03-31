/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.buzz;

import eazycommon.util.Util;
import org.json.simple.JSONObject;


/**
 *
 * @author Admin
 */
public class Male extends User {

    public static final String hobbyKey = "hobby";
    public String hobby;

    @Override
    public void getUpdateUser(JSONObject obj) {
        super.getUpdateUser(obj);
        String str = Util.getStringParam(obj, hobbyKey);
        if (str != null) {
            hobby = str;
        }
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject(); //To change body of generated methods, choose Tools | Templates.
        if (hobby != null) {
            jo.put(hobbyKey, hobby);
        }

        return jo;

    }

}
