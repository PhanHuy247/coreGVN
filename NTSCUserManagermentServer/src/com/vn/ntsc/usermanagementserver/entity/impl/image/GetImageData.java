/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.image;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class GetImageData implements IEntity {

    private static final String autoApprovedKey = "is_app";
    public Long autoApproved;

    public GetImageData(Long autoApproved) {
        this.autoApproved = autoApproved;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.autoApproved != null) {
            jo.put(autoApprovedKey, this.autoApproved);
        }

        return jo;
    }

}
