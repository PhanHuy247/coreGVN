/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;


import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.LogCallDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class StartCallApi implements IApiAdapter{

    @Override
    public Respond execute(Request request) {
        Respond result;
        try {
            String call_sip = Util.getStringParam(request.reqObj, "call_sip");
            Util.addDebugLog("StartCallApi " + request.reqObj.toJSONString());
            if (Util.validateString(call_sip)){
                //update CallLog
                LogCallDAO.updateStartCall(call_sip, Constant.PARTNER_ANSWER_VALUE.PARTNER_ANSWER);
                result = new Respond(ErrorCode.SUCCESS);
            }
            else 
                result = new Respond(ErrorCode.WRONG_DATA_FORMAT);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = new EntityRespond(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
}