/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.automessage.extend;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.impl.automessage.Message;

/**
 *
 * @author HuyDX
 */
public class LoginBonusMessage extends Message {
    
    private static final String login_bonus_times_key = "login_bonus_times";
    private Integer login_bonus_times;
    
    private static final String message_type_key = "sender";
    private String message_type;
    
    private static final String gender_key = "gender";
    private Integer gender;
    
    public LoginBonusMessage(){
    }
    public LoginBonusMessage(String id, Integer login_bonus_times, String message_type, String content, Integer gender) {
        this.id=id;
        this.login_bonus_times = login_bonus_times;
        this.message_type = message_type;
        this.content = content;
        this.gender = gender;
    }
    public LoginBonusMessage(Integer login_bonus_times, String message_type, String content, Integer gender) {
        this.login_bonus_times = login_bonus_times;
        this.message_type = message_type;
        this.content = content;
        this.gender = gender;
    }

    public Integer getLogin_bonus_times() {
        return login_bonus_times;
    }

    public void setLogin_bonus_times(Integer login_bonus_times) {
        this.login_bonus_times = login_bonus_times;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject();
        if (this.login_bonus_times!=null)
            jo.put(login_bonus_times_key, login_bonus_times);
        if (this.message_type!=null)
            jo.put(message_type_key, message_type);
        if (this.gender!=null)
            jo.put(gender_key, gender);
        
        return jo;            
    }
      
    
    
}
