/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.video;

import com.vn.ntsc.usermanagementserver.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class PublicVideo implements IEntity{

    private static final String videoIdKey = "video_id";
    public String videoId;

    private static final String buzzIdKey = "buzz_id";
    public String buzzId;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.videoId != null) {
            jo.put(videoIdKey, this.videoId);
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

    public PublicVideo(String videoId, String buzzId) {
        this.videoId = videoId;
        this.buzzId = buzzId;
    }
    
}
