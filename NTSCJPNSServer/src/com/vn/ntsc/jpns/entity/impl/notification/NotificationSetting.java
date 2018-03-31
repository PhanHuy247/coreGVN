/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.entity.impl.notification;

import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.dao.IEntity;

/**
 *
 * @author Administrator
 */
public class NotificationSetting implements IEntity {

    private static final String notiBuzzKey = "noti_buzz";
    public Integer notiBuzz = 1;

    private static final String andgAlertKey = "andg_alt";
    public Integer andgAlert = 1;

    private static final String chatKey = "chat";
    public Integer chat = 0;

    private static final String notiCheckOutKey = "noti_chk_out";
    public Integer notiCheckOut = 1;

    private static final String notiLikeKey = "noti_like";
    public Integer notiLike = 1;

    private static final String resetPasswordKey = "reset_pwd";
    public Integer resetPassword = 1;

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
