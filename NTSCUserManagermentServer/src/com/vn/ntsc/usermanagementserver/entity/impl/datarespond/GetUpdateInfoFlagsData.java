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
 * @author duyetpt
 */
public class GetUpdateInfoFlagsData implements IEntity {

    public static final String updateEmailFlagKey = "update_email_flag";
    public Integer updateEmailFlag;

    public static final String finishRegisterFlagKey = "finish_register_flag";
    public Integer finishRegisterFlag;

    private static final String verificationFlagKey = "verification_flag";
    public Integer verificationFlag;
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (updateEmailFlag != null) {
            jo.put(updateEmailFlagKey, updateEmailFlag);
        }
        if (finishRegisterFlag != null) {
            jo.put(finishRegisterFlagKey, finishRegisterFlag);
        }
        if (verificationFlag != null) {
            jo.put(verificationFlagKey, verificationFlag);
        }
        return jo;
    }

}
