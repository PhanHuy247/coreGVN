/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import eazycommon.exception.EazyException;
import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author ElvisHsu
 */
public class SetCreaUserInfoApi implements IApiAdapter{
        public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String creaId = Util.getStringParam(obj, ParamKey.CREA_ID);
            String creaPass = Util.getStringParam(obj, ParamKey.CREA_PASS);
            UserDAO.updateCreaUserInfo(userId, creaId, creaPass);
            result = new Respond(ErrorCode.SUCCESS);
            
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    
}
