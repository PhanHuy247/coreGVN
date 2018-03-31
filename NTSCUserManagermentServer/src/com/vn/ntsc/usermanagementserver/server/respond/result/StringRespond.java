/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.result;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class StringRespond extends Respond {

    public String data;

    public StringRespond(int code , String str) {
        super(code);
        this.data = str;
    }

    public StringRespond(int code) {
        super(code);
    }

    public StringRespond() {
        super();
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject();

        if (this.data != null) {
            jo.put(ParamKey.DATA, this.data);
        }

        return jo;
    }
}
