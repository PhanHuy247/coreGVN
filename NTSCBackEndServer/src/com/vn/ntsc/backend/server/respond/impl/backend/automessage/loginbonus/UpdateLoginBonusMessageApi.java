/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.automessage.loginbonus;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.LoginBonusMessageDAO;
import com.vn.ntsc.backend.entity.impl.automessage.extend.LoginBonusMessage;
import com.vn.ntsc.backend.server.automessage.MessageContainer;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class UpdateLoginBonusMessageApi implements IApiAdapter{
    
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(SettingdbKey.LOGIN_BONUS_MESSAGE.LOGIN_BONUS_TIMES, 4);
        keys.put(SettingdbKey.LOGIN_BONUS_MESSAGE.SENDER, 5);
        keys.put(SettingdbKey.LOGIN_BONUS_MESSAGE.CONTENT, 6);
    }
    
    @Override
    public Respond execute(JSONObject obj) {
        Respond response = new Respond();
        try {            
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                response = new EntityRespond(error);
                return response;
            }
            
            String id = Util.getStringParam(obj, "id");
            Integer login_bonus_times = Util.getLongParam(obj, "login_bonus_times").intValue();
            String content = Util.getStringParam(obj, "content");                    
            String sender = Util.getStringParam(obj, "sender");
            Long genderL = Util.getLongParam(obj, "gender");
            Integer gender = genderL==null? null: genderL.intValue();
            LoginBonusMessage message = new LoginBonusMessage(id, login_bonus_times, sender, content, gender);
            LoginBonusMessageDAO.update(message);
            
            message.time = DateFormat.format_yyyyMMddHHmm(Util.currentTime());
            MessageContainer.put(message);
            
            response = new Respond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return response;
    }
}
