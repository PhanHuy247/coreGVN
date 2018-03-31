/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.setting;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 * 
 * @author namhv
 */
public class OtherSettingApp implements IEntity{
    
    private static final String applicationIdKey = "application_id";
    public Integer applicationId;
    private static final String forceUpdatingKey = "force_updating";
    public Boolean forceUpdating;
    private static final String urlWebKey = "url_web";
    public String urlWeb;
    private static final String appVersionKey = "app_version";
    public String appVersion;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (this.applicationId != null) {
            jo.put(applicationIdKey, this.applicationId);
        }
        if (this.forceUpdating != null) {
            jo.put(forceUpdatingKey, this.forceUpdating);
        }
        if (this.urlWeb != null) {
            jo.put(urlWebKey, this.urlWeb);
        }
        if (this.appVersion != null) {
            jo.put(appVersionKey, this.appVersion);
        }
        return jo;
    }

    
}
