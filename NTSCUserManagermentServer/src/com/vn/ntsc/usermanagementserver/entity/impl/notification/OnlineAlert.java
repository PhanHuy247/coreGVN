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
public class OnlineAlert implements IEntity{

    private static final String alertNumberKey = "alt_fre";
    public Integer alertNumber;

    private static final String isAlertKey = "is_alt";
    public Integer isAlert;

    public OnlineAlert() {
        this.isAlert = 0;
    }

    public OnlineAlert(int alertNumber, int isAlert) {
        this.alertNumber = alertNumber;
        this.isAlert = isAlert;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.alertNumber != null) {
            jo.put(alertNumberKey, this.alertNumber);
        }
        if (this.isAlert != null) {
            jo.put(isAlertKey, this.isAlert);
        }

        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

}
