/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.datarespond;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.admin.AdminSetting;

/**
 *
 * @author RuAc0n
 */
public class InitData implements IEntity{

    private static final String rolesKey = "roles";
    public List<IEntity> roles;

    private static final String screenGroupsKey = "scr_groups";
    public List<IEntity> screenGroups;

    private static final String stringKey = "string";
    public String string;
    
    public AdminSetting adminSetting;

    public InitData(List<IEntity> roles, List<IEntity> screenGroups, String string, AdminSetting setting) {
        this.roles = roles;
        this.screenGroups = screenGroups;
        this.string = string;
        this.adminSetting = setting;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.roles != null) {
            JSONArray arr = new JSONArray();
            for (IEntity entity : this.roles) {
                arr.add(entity.toJsonObject());
            }
            jo.put(rolesKey, arr);
        }

        if (this.screenGroups != null) {
            JSONArray arr = new JSONArray();
            for (IEntity entity : this.screenGroups) {
                arr.add(entity.toJsonObject());
            }
            jo.put(screenGroupsKey, arr);
        }
        if (string != null) {
            jo.put(stringKey, this.string);
        }
        if (adminSetting != null) {
            jo.putAll(adminSetting.toMap());
        }

        return jo;
    }
}
