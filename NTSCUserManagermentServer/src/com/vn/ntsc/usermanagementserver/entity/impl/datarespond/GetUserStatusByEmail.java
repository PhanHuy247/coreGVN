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
public class GetUserStatusByEmail implements IEntity {
    
    private static final String userStatusKey = "user_status";
    public Integer userStatus;
    
    private static final String emailKey = "email";
    public String email;
    
    public GetUserStatusByEmail(){
    }

    public GetUserStatusByEmail(String email, Integer userStatus) {
        this.userStatus = userStatus;
        this.email = email;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(email != null){
            jo.put(emailKey, email);
        }
        if(userStatus != null){
            jo.put(userStatusKey, userStatus);
        }
        return jo;
    }
}
