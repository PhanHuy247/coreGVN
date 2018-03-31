/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.server.emailpool.EmailInfor;
import com.vn.ntsc.usermanagementserver.server.emailpool.EmailSender;
import com.vn.ntsc.usermanagementserver.server.emailpool.QueueEmailManager;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
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
public class ForgotPasswordApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String email = Util.getStringParam(obj, ParamKey.EMAIL).toLowerCase();
            String applicationId = "1";
            if (obj.get("application") != null && Util.getLongParam(obj, "application") != null) {
                applicationId = Util.getLongParam(obj, "application").toString();
            }
            
            boolean isExistMail = UserDAO.checkEmailMultiapp(email, null, applicationId);
            if (isExistMail) {
                String code = Util.createVerificationCode();
                boolean isAddCode = UserDAO.updateVerificationCode(email, code);
                if (isAddCode) {
                    EmailInfor emailInfor = new EmailInfor(email, code, Constant.EMAIL_TYPE_VALUE.FORGOT_PASS_EMAIL, null);
                    QueueEmailManager.addEmail(emailInfor);
                    EmailSender.startSendingEmail();
                    result.code = ErrorCode.SUCCESS;
                }
            }else {
                result.code = ErrorCode.EMAIL_NOT_FOUND;
                return result;
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
