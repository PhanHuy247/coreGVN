/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.automessage.loginbonus;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.LoginBonusMessageDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class ListLoginBonusMessageApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        Respond response = new Respond();
        try {
            int skip = Util.getLongParam(obj, ParamKey.SKIP).intValue();
            int take = Util.getLongParam(obj, ParamKey.TAKE).intValue();
            SizedListData data = LoginBonusMessageDAO.list(skip, take);
            response = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex){
            Util.addErrorLog(ex);
        }
        return response;
    }
    
}
