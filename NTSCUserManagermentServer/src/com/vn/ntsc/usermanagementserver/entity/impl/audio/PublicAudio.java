/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.audio;

import com.vn.ntsc.usermanagementserver.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class PublicAudio implements IEntity{

    private static final String audioIdKey = "audio_id";
    public String audioId;
    
    private static final String coverIdKey = "cover_id";
    public String coverId;
    
    private static final String buzzIdKey = "buzz_id";
    public String buzzId;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.coverId != null) {
            jo.put(coverIdKey, this.coverId);
        }
        if (this.audioId != null) {
            jo.put(audioIdKey, this.audioId);
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

    public PublicAudio(String audioId, String buzzId,String coverId) {
        this.audioId = audioId;
        this.buzzId = buzzId;
        this.coverId = coverId;
    }
    
}
