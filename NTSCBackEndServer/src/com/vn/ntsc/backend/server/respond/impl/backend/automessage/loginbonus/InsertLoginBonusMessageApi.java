/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.automessage.loginbonus;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.LoginBonusMessageDAO;
import com.vn.ntsc.backend.entity.impl.automessage.extend.LoginBonusMessage;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.server.automessage.MessageContainer;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class InsertLoginBonusMessageApi implements IApiAdapter{

    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(SettingdbKey.LOGIN_BONUS_MESSAGE.LOGIN_BONUS_TIMES, 4);
        keys.put(SettingdbKey.LOGIN_BONUS_MESSAGE.SENDER, 5);
        keys.put(SettingdbKey.LOGIN_BONUS_MESSAGE.CONTENT, 6);
    }
    
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond response = new EntityRespond();        
        try {
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                    response = new EntityRespond(error);
                    return response;
                }

            Integer login_bonus_times = Util.getLongParam(obj, SettingdbKey.LOGIN_BONUS_MESSAGE.LOGIN_BONUS_TIMES).intValue();
            if (login_bonus_times <= 0){
                response = new EntityRespond(4);
                return response;
            }

            String message_type = Util.getStringParam(obj, SettingdbKey.LOGIN_BONUS_MESSAGE.SENDER);
            String content = Util.getStringParam(obj, SettingdbKey.LOGIN_BONUS_MESSAGE.CONTENT);
            Long genderL = Util.getLongParam(obj, SettingdbKey.LOGIN_BONUS_MESSAGE.GENDER);
            Integer gender;
            gender = genderL==null? null : genderL.intValue();
                    
            LoginBonusMessage message = new LoginBonusMessage(login_bonus_times, message_type, content, gender);
            String _id = LoginBonusMessageDAO.insert(message);
            message.id=_id;
            message.time = DateFormat.format_yyyyMMddHHmm(Util.currentTime());            
            MessageContainer.put(message);

            response = new EntityRespond(ErrorCode.SUCCESS, new InsertData(_id));
        }
        catch (EazyException ex){
            Util.addErrorLog(ex);
            response = new EntityRespond(ex.getErrorCode());
        }
        catch (Exception ex){
            Util.addErrorLog(ex);
        }
        return response;
    }
    
}
