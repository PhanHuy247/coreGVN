/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.result;

import eazycommon.constant.ParamKey;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;
import vn.com.ntsc.staticfileserver.server.respond.Respond;

/**
 *
 * @author hoangnh
 */
public class VersionRespond<T> extends Respond{
    public List<T> data;
    public Integer version;

    public VersionRespond() {
        super();
    }
    
    public VersionRespond(int code , List<T> data, Integer version) {
        super(code);
        this.data = data;
        this.version = version;
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
        if (this.version != null)
            jo.put(ParamKey.VERSION, version);

        return jo;
    }
}
