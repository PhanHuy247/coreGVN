/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.Core;
import com.vn.ntsc.jpns.dao.pojos.Device;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;

/**
 *
 * @author Administrator
 */
public class LogoutAPI implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        String userid = (String) obj.get(Device.UserID);
        String deviceToken = (String) obj.get(Device.DeviceToken);
        int deviceType = ((Long) obj.get(Device.DeviceType)).intValue();
        String username = (String) obj.get(Device.Username);
        String applicationId = (String) obj.get("application_id");
        String voip_notify_token = (String) obj.get("voip_notify_token");

        Device u = new Device(userid, username, deviceType, deviceToken, applicationId, voip_notify_token);
        Core.dao.remove(u);
    }
    
}
