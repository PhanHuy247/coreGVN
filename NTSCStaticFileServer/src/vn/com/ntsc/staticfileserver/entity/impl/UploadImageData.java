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
public class UploadImageData implements IEntity {

    private static final String imageIdKey = "image_id";
    public String image_id;

    private static final String isApprovedKey = "is_app";
    public Long is_app;
    
    private static final String imageUrlKey = "image_url";
    public String image_url;

    public UploadImageData(String image_id) {
        this.image_id = image_id;
    }

    public UploadImageData(String image_id, Long is_app) {
        this.image_id = image_id;
        this.is_app = is_app;
    }
    
    public UploadImageData(String image_id, Long is_app, String image_url) {
        this.image_id = image_id;
        this.is_app = is_app;
        this.image_url = image_url;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (image_id != null) {
            jo.put(imageIdKey, image_id);
        }
        if (is_app != null) {
            jo.put(isApprovedKey, is_app);
        }
        if (image_url != null) {
            jo.put(imageUrlKey, image_url);
        }
        return jo;
    }

}
