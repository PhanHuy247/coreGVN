/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.result;

import java.util.List;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;

/**
 *
 * @author RuAc0n
 * @param <T>
 */
public class ListEntityRespond<T> extends Respond {
    
    public List<T> data;

    public ListEntityRespond() {
        super();
    }
    
    public ListEntityRespond(int code , List<T> data) {
        super(code);
        this.data = data;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject();
        if (this.data != null) {
            JSONArray arr = new JSONArray();
            for (T dt : this.data) {
                arr.add(((IEntity)dt).toJsonObject());
            }
            jo.put(ParamKey.DATA, arr);
        }

        return jo;
    }
}
