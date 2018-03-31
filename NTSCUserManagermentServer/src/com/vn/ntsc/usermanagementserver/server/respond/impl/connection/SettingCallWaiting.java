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
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author Admin
 */
public class SettingCallWaiting implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond respond = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Boolean voice_call_setting = (Boolean) obj.get(ParamKey.VOICE_CALL_WAITING);
            Boolean video_call_setting = (Boolean) obj.get(ParamKey.VIDEO_CALL_WAITING);
            //Linh add #5227
            Long call_request_setting = (Long) obj.get(ParamKey.CALL_REQUEST_SETTING);
            
            if (UserDAO.isExists(userId)) {
                boolean result = false;
                if(call_request_setting != null){
                    result = UserDAO.updateCallSetting(userId, video_call_setting, voice_call_setting, call_request_setting.intValue());
                } else {
                    result = UserDAO.updateCallSetting(userId, video_call_setting, voice_call_setting);
                }
                

                if (result) {
                    respond.code = ErrorCode.SUCCESS;
                }
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        }

        return respond;
    }

}
