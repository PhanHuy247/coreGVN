/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.result;

import java.util.List;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class ListStringRespond extends Respond {
    
    public List<String> data;

    public ListStringRespond() {
        super();
    }
    
    public ListStringRespond(int code , List<String> data) {
        super(code);
        this.data = data;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject();
        if (this.data != null) {
            JSONArray arr = new JSONArray();
            for (String dt : this.data) {
                arr.add(dt);
            }
            jo.put(ParamKey.DATA, arr);
        }

        return jo;
    }
}
