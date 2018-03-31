/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;

/**
 *
 * @author RuAc0n
 */
public class SetNotificationSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long notiBuzz = Util.getLongParam(obj, "noti_buzz");
            Long notiChat = Util.getLongParam(obj, "chat");
            Long andGAlert = Util.getLongParam(obj, "andg_alt");
//            Long notiCheck = Util.getLongParam(obj, "noti_chk_out");
            Long notiLike = Util.getLongParam(obj, "noti_like");
//            int notiInt = 0;
//            if (notiLike != null) {
//                notiInt = notiLike.intValue();
//            }
            NotificationSettingDAO.addNotificationSetting(userId,1, andGAlert.intValue(), notiChat.intValue(), 0, notiLike.intValue());
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
