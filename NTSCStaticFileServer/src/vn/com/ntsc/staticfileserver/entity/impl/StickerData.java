/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author hoangnh
 */
public class StickerData implements IEntity{
    
    private static final String idKey = "stk_id";
    public String id;
    
    private static final String codekey = "code";
    public Long code;
    
    private static final String urlKey = "stk_url";
    public String url;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.code != null)
            jo.put(codekey, this.code);
        if(this.url != null)
            jo.put(urlKey, url);
        return jo;
    }
    
}
