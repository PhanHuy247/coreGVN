/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author duyetpt
 */
public class GetAgeVerificationStatusData implements IEntity {

    public Integer ageStatus;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (ageStatus != null) {
            jo.put("age_status", ageStatus);
        }
        return jo;
    }

}
