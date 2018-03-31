/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.setting;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class NotificationSetting implements IEntity{

    private static final String notiBuzzKey = "noti_buzz";
    public Integer notiBuzz;

    private static final String andgAlertKey = "andg_alt";
    public Integer andgAlert;

    private static final String chatKey = "chat";
    public Integer chat;

    private static final String notiCheckOutKey = "noti_chk_out";
    public Integer notiCheckOut;
    
    private static final String notiLikeKey = "noti_like";
    public Integer notiLike;
    
    private static final String resetPasswordKey = "reset_pwd";
    public Integer resetPassword;

    public NotificationSetting() {
        this.notiBuzz = 1;
        this.andgAlert = 1;
        this.chat = 0;
        this.notiCheckOut = 1;
        this.notiLike = 1;
        this.resetPassword = 1;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.notiBuzz != null) {
            jo.put(notiBuzzKey, this.notiBuzz);
        }
        if (this.andgAlert != null) {
            jo.put(andgAlertKey, this.andgAlert);
        }
        if (this.chat != null) {
            jo.put(chatKey, this.chat);
        }
        if (this.notiCheckOut != null) {
            jo.put(notiCheckOutKey, this.notiCheckOut);
        }
        if (this.notiLike != null) {
            jo.put(notiLikeKey, this.notiLike);
        }
        if (this.resetPassword != null) {
            jo.put(resetPasswordKey, this.resetPassword);
        }
        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

}
