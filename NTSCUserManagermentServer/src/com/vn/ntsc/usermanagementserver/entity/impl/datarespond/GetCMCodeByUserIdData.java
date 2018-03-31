/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author Rua
 */
public class GetCMCodeByUserIdData implements IEntity {
    private static final String cmCodeKey = "cm_code";
    public String cmCode;
    
    public GetCMCodeByUserIdData(){
    }

    public GetCMCodeByUserIdData(String cmCode) {
        this.cmCode = cmCode;
    }
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(cmCode != null){
            jo.put(cmCodeKey, cmCode);
        }
        return jo;
    }
}
