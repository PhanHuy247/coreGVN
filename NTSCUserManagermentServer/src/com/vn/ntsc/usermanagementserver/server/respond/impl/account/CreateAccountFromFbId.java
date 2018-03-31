/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class CreateAccountFromFbId implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String fbId = Util.getStringParam(obj, "fb_id");
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String email = Util.getStringParam(obj, ParamKey.EMAIL);
            String originalPassword = Util.getStringParam(obj, ParamKey.ORIGINAL_PASSWORD);
            String password = Util.getStringParam(obj, "pwd");
            
            if (!Util.validateEmail(email)) {
                result.code = ErrorCode.INVALID_EMAIL;
                return result;
            }
            //HUNGDT change check unique email
            if (UserDAO.checkEmail(email, userId)) {
                result.code = ErrorCode.EMAIL_REGISTED;
                return result;
            }
            if(UserDAO.checkFbIdAndEmail(fbId,userId)){
                boolean isChangePass = UserDAO.addEmailAndPassForFbId(userId, email, password, originalPassword, time);
                if (isChangePass) {
                    result.code = ErrorCode.SUCCESS;
                }
            }
            
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        }
        return result;
    }
    
}
