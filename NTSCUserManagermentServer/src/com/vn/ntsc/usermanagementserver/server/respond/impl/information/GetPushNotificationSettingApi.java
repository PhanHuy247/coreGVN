/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.NotificationSetting;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.PushNotificationSetting;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetPushNotificationSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            NotificationSetting notiSetting = NotificationSettingDAO.getNotificationSetting(userId);
            PushNotificationSetting setting = new PushNotificationSetting(notiSetting);
            result = new EntityRespond(ErrorCode.SUCCESS, setting);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
