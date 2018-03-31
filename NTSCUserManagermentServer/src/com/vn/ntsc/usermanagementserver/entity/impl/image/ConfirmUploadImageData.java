/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.image;

import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class ConfirmUploadImageData implements IEntity {

    private static final String autoApprovedKey = "is_app";
    public Long autoApproved;

    private static final String imageTypeKey = "img_type";
    public Integer imageType;

    private static final String isAvaKey = "is_ava";
    public Long isAva;
    
    public int point;

    public ConfirmUploadImageData(Long autoApproved, Integer imageType, Long isAva, int point) {
        this.autoApproved = autoApproved;
        this.imageType = imageType;
        this.isAva = isAva;
        this.point = point;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.autoApproved != null) {
            jo.put(autoApprovedKey, this.autoApproved);
        }
        if (this.isAva != null) {
            jo.put(isAvaKey, this.isAva);
        }
        if (this.imageType != null) {
            jo.put(imageTypeKey, this.imageType);
        }
        jo.put(ParamKey.POINT, point ); 
        return jo;
    }
}
