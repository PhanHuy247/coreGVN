/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Phan Huy
 */
public class TotalNotiSeen implements IApiAdapter{

    @Override
    public Respond execute(Request request) {
         EntityRespond result = new EntityRespond();
         try {
            JSONObject jsonObject = request.reqObj;
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    result = new EntityRespond(code.intValue());
                }else
                    result = new EntityRespond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
