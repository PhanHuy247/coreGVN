/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.image;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class PublicImage implements IEntity{

    private static final String imageIdKey = "img_id";
    public String imageId;

    private static final String buzzIdKey = "buzz_id";
    public String buzzId;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.imageId != null) {
            jo.put(imageIdKey, this.imageId);
        }
        if (this.buzzId != null) {
            jo.put(buzzIdKey, this.buzzId);
        }

        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

    public PublicImage(String imageId, String buzzId) {
        this.imageId = imageId;
        this.buzzId = buzzId;
    }

}
