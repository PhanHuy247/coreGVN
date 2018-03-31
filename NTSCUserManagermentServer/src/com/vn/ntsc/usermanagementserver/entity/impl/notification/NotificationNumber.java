/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.entity.impl.notification;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class NotificationNumber implements IEntity{
    private static final String notificationNumberKey = "noti_num";
    public int noti;

    public NotificationNumber(int noti) {
        this.noti = noti;
    }

    public NotificationNumber() {
        this.noti = 0;
    }
    
    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        json.put(notificationNumberKey, this.noti);
        return json;
    }
    
}
