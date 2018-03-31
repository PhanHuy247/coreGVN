/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.notification;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class PushNotificationSetting implements IEntity{

    private static final String notiBuzzKey = "buzz_notification";
    public Integer notiBuzz;

    private static final String andgAlertKey = "eazy_notification";
    public Integer andgAlert;

    private static final String chatKey = "chat_notification";
    public Integer chat;
    
    private static final String notiCheckOutKey = "noti_chk_out";
    public Integer notiCheckOut;
    
    private static final String notiLikeKey = "noti_like";
    public Integer notiLike;

    public PushNotificationSetting() {
        this.notiBuzz = 1;
        this.andgAlert = 1;
        this.chat = 0;
        this.notiCheckOut = 1;
        this.notiLike = 1;
    }

    public PushNotificationSetting(NotificationSetting setting){
        this.notiBuzz = setting.notiBuzz;
        this.andgAlert = setting.andgAlert;
        this.chat = setting.chat;
        this.notiCheckOut = setting.notiCheckOut;
        this.notiLike = setting.notiLike;
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
        if (this.notiCheckOut != null){
            jo.put(notiCheckOutKey, notiCheckOut);
        }
        if (this.notiLike != null){
            jo.put(notiLikeKey, notiLike);
        }
        
        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

}
