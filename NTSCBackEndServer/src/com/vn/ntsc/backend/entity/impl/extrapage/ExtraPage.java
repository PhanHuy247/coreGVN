/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.extrapage;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class ExtraPage implements IEntity{
    private static final String idKey = "id";
    public String id;

    private static final String titleKey = "title";
    public String title;
    
    private static final String urlKey = "url";
    public String url;
    
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.title != null)
            jo.put(titleKey, this.title);
        if(this.url != null)
            jo.put(urlKey, this.url);
        
        return jo;
    }
    
    public ExtraPage(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public ExtraPage() {
    }

    
    
}
