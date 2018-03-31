/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.cmcode;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class InsertMediaData implements IEntity {

    private static final String mediaIdKey = "media_id";
    public String mediaId;

    public InsertMediaData(String mediaId) {
        this.mediaId = mediaId;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (this.mediaId != null) {
            jo.put(mediaIdKey, this.mediaId);
        }

        return jo;
    }
}
