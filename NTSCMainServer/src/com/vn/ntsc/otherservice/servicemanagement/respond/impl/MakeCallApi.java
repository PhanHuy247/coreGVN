/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import com.vn.ntsc.dao.impl.LogCallDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class MakeCallApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        Respond result;
        try {
            String call_sip = Util.getStringParam(request.reqObj, "call_sip");
            String from = Util.getStringParam(request.reqObj, "from");
            String to = Util.getStringParam(request.reqObj, "to");
            Integer type = Util.getLongParam(request.reqObj, "type").intValue();
            Util.addDebugLog("MakeCallApi " + request.reqObj.toJSONString());
            if (Util.validateString(call_sip, from, to) && Util.validate(type)) {
                String ip = Util.getStringParam(request.reqObj, ParamKey.IP);
                Date startTime = Util.getGMTTime();
                Date endTime = startTime;
                if (type == Constant.CALL_TYPE_VALUE.VOICE_CALL) {
                    type = 1;
                } else if (type == Constant.CALL_TYPE_VALUE.VIDEO_CALL) {
                    type = 2;
                }
                result = new Respond(ErrorCode.SUCCESS);
                LogCallDAO.addLog(call_sip, from, to, startTime, endTime, ip, type, 0, Constant.PARTNER_ANSWER_VALUE.PARTNER_REFUSE);
            } else {
                result = new Respond(ErrorCode.WRONG_DATA_FORMAT);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = new EntityRespond(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
