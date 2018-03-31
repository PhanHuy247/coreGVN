/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class ChangePasswordApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.USER_ID);
            String password = Util.getStringParam(obj, ParamKey.OLD_PASSWORD);
            String newPassword = Util.getStringParam(obj, ParamKey.NEW_PASSWORD);
            String originalPassword = Util.getStringParam(obj, ParamKey.ORIGINAL_PASSWORD);
            boolean isChangePass = UserDAO.changePassword(id, password, newPassword, originalPassword, time);
            if (isChangePass) {
                result.code = ErrorCode.SUCCESS;
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);            
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
}
