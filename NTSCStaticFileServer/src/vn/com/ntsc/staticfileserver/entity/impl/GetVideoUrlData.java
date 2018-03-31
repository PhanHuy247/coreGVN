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
 * @author RuAc0n
 */
public class GetVideoUrlData implements IEntity {

    private static final String urlKey = "url";
    public String url;

    public GetVideoUrlData(String url) {
        this.url = url;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (url != null) {
            jo.put(urlKey, url);
        }

        return jo;
    }

}
