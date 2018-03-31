/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.notification;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class GetNotificationSettingData implements IEntity{

    private static final String settiingKey = "setting";
    public NotificationSetting setting;

    private static final String favouristListKey = "fav_lst";
    public List<String> favouristList;

    public GetNotificationSettingData(NotificationSetting setting, List<String> favouristList) {
        this.setting = setting;
        this.favouristList = favouristList;
    }

    public GetNotificationSettingData(NotificationSetting setting) {
        this.setting = setting;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.setting != null) {
            jo.put(settiingKey, this.setting.toJsonObject());
        }
        if (this.favouristList != null) {
            JSONArray arr = new JSONArray();
            for (String str : favouristList) {
                arr.add(str);
            }
            jo.put(favouristListKey, arr);
        }

        return jo;
    }

}
