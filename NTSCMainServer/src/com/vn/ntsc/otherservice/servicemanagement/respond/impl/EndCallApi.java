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
public class EndCallApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {

        Respond result;
        try {
            String callSip = Util.getStringParam(request.reqObj, "call_sip");
            Integer partnerRespondStr = Util.getLongParam(request.reqObj, "finish_type").intValue();
            Util.addDebugLog("=======finish_type=======" + partnerRespondStr);
            Integer partnerRespond;
            Integer duration = Util.getLongParam(request.reqObj, "bill_seconds").intValue();
            Util.addDebugLog("EndCallApi " + request.reqObj.toJSONString());
            switch (partnerRespondStr) {
                case Constant.END_CALL_VALUE.BUSY:
                    partnerRespond = Constant.PARTNER_ANSWER_VALUE.PARTNER_BUSY;
                    break;
                case Constant.END_CALL_VALUE.CANCEL:
                    partnerRespond = Constant.PARTNER_ANSWER_VALUE.PARTNER_REFUSE;
                    break;
                case Constant.END_CALL_VALUE.END_CALL_BY_MALE:
                case Constant.END_CALL_VALUE.END_CALL_BY_FEMALE:
                case Constant.END_CALL_VALUE.END_CALL_BY_NOT_ENOUGH_POINT:
                case Constant.END_CALL_VALUE.OTHERS:
                    partnerRespond = Constant.PARTNER_ANSWER_VALUE.PARTNER_ANSWER;
                    break;
                default:
                    result = new Respond(ErrorCode.WRONG_DATA_FORMAT);
                    return result;
            }

            if (Util.validateString(callSip) && Util.validate(duration)) {
                //if (partnerRespondStr!=null){
                    LogCallDAO.updateEndCall(callSip, partnerRespond, duration, partnerRespondStr);
                    result = new Respond(ErrorCode.SUCCESS);
                //}
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
