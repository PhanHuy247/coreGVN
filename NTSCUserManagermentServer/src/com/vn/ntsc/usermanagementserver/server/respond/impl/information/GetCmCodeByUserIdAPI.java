/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetCMCodeByUserIdData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Rua
 */
public class GetCmCodeByUserIdAPI implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond respond = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String cmCode = UserDAO.getCMCode(userId);
            GetCMCodeByUserIdData data = new GetCMCodeByUserIdData(cmCode);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
