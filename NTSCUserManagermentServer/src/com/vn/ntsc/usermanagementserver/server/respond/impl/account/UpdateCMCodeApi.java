/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.InvitationCodeDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;

/**
 *
 * @author RuAc0n
 */
public class UpdateCMCodeApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String reciverCode = Util.getStringParam(obj, "rec_code");
            String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
            String receiverId = InvitationCodeDAO.getUserId(reciverCode);

            if (receiverId != null && cmCode != null) {
                UserDAO.updateCmCode(receiverId, cmCode);
            }
            result.code = ErrorCode.SUCCESS;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);            
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
}
