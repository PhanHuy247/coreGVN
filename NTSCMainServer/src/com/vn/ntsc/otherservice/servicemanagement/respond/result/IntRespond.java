/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.result;

import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class IntRespond extends Respond {

    public Integer data;

    public IntRespond(int code , Integer number) {
        super(code);
        this.data = number;
    }

    public IntRespond(int code) {
        super(code);
    }

    public IntRespond() {
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
