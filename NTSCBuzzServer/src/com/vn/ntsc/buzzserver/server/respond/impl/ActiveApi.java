/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.server.respond.impl;

import eazycommon.util.Util;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.blacklist.BlackListManager;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class ActiveApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            BlackListManager.remove(userId);
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
}
