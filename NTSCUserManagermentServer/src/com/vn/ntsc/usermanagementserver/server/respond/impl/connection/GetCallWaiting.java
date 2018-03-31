/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetCallWaitingData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Admin
 */
public class GetCallWaiting implements IApiAdapter {

    public Respond execute(JSONObject obj, Date time) {
        EntityRespond respond = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            User user = UserDAO.getUserInfor(userId);
            GetCallWaitingData data = new GetCallWaitingData();
            if (user.videoCall == null) {
                data.videoCall = true;
            } else {
                data.videoCall = user.videoCall;
            }
            if (user.voiceCall == null) {
                data.voiceCall = true;
            } else {
                data.voiceCall = user.voiceCall;
            }
            if (user.callRequestSetting == null) {
                data.callRequestSetting = 0;
            } else {
                data.callRequestSetting = user.callRequestSetting;
            }
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
