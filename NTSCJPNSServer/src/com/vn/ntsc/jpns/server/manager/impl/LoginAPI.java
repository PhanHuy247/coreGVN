/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import eazycommon.constant.Constant;
import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.Core;
import com.vn.ntsc.jpns.dao.impl.Notification;
import com.vn.ntsc.jpns.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.jpns.dao.pojos.Device;
import com.vn.ntsc.jpns.entity.impl.notification.NotificationSetting;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;
import org.bson.types.ObjectId;

/**
 *
 * @author Administrator
 */
public class LoginAPI implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        try {
            String userid = (String) obj.get(Device.UserID);
            String username = (String) obj.get(Device.Username);
            String deviceToken = (String) obj.get(Device.DeviceToken);
            int deviceType = ((Long) obj.get(Device.DeviceType)).intValue();
            Long iosApplicationTypeL = (Long) obj.get("applicaton_type");
            Integer iosApplicatonType = null;
            if (deviceType == Constant.DEVICE_TYPE.IOS) {
                iosApplicatonType = Constant.APPLICATION_TYPE.IOS_ENTERPRISE_APPLICATION;
                if (iosApplicationTypeL != null) {
                    iosApplicatonType = iosApplicationTypeL.intValue();
                }
            }
            String deviceId = (String) obj.get("device_id");

            //HUNGDT add 16-04-2016
            String checkid = null;
//            if (deviceType == 0) {
//                checkid = (String) jo.get("idfa");
//            } else if (deviceType == 1) {
//                checkid = (String) jo.get("gps_adid");
//
//            }
            // LongLT 13Sep2016 /////////////////////////// START #4484
//            if((checkid == null) || ("".equals(checkid))) {
            checkid = (String) obj.get("adid");
//            }
            // LongLT 13Sep2016 /////////////////////////// END #4484
            String applicationId = (String) obj.get("application_id");
            //HUNGDT add callkit
            String voip_notify_token = (String) obj.get("voip_notify_token");
            
            if(userid != null && ObjectId.isValid(userid)){
                Boolean hasNotiSetting = NotificationSettingDAO.hasNotificationSetting(userid);
                if(!hasNotiSetting){
                    NotificationSetting notiSetting = new NotificationSetting();
                    NotificationSettingDAO.addNotificationSetting(userid, notiSetting.notiBuzz, notiSetting.andgAlert, notiSetting.chat, notiSetting.notiCheckOut, notiSetting.notiLike);
                }
            }
            Device u = new Device(userid, username, deviceType, deviceToken, iosApplicatonType, deviceId, checkid, applicationId, voip_notify_token);
            if (deviceToken != null && !deviceToken.isEmpty()) {
                Core.dao.removeDuplicatedOrEmtyTokenDevice(u);
//                if (deviceType == Constant.DEVICE_TYPE.IOS) {
                    Core.dao.removeDuplicatedDeviceId(u);
//                }
                Core.dao.add(u);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
