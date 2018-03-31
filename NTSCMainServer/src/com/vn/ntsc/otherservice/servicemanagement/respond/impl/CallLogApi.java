/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.LogCallDAO;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.statistic.UserCounter;

/**
 *
 * @author RuAc0n
 */
public class CallLogApi implements IApiAdapter{

    private static final int VOICE_TYPE = 1;
    private static final int VIDEO_TYPE = 2;
    
    private static final String SVOICE = "SVOICE"; //start voice call
    private static final String EVOICE = "EVOICE"; //end voice call
    private static final String SVIDEO = "SVIDEO"; //start video call
    private static final String EVIDEO = "EVIDEO";  //end video call
    private static final String CMD = "CMD";  //end video call
    
    @Override
    public Respond execute(Request request) {
        Respond result = new Respond();
        try {
            String messType = Util.getStringParam(request.reqObj, ParamKey.MSG_TYPE);
            if (messType.equals(SVIDEO) || messType.equals(SVOICE)) {
                startCall(request, messType);
            } else if(messType.equals(CMD)) {
                addDuration(request);
            } else {
                endCall(request);
            }
            result = new Respond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }

    private void startCall(Request request, String messType) {
        try {
            int type = VIDEO_TYPE;
            if (messType.equals(SVOICE)) {
                type = VOICE_TYPE;
            }
            UserCounter.countCall(type);
            long startTime = Util.getLongParam(request.reqObj, ParamKey.TIME);
            Date time = new Date(startTime);
            String reqId = Util.getStringParam(request.reqObj, ParamKey.FROM_USER_ID);
            String partnerId = Util.getStringParam(request.reqObj, ParamKey.TOUSERID);
            String ip = Util.getStringParam(request.reqObj, ParamKey.IP);
            String callId = Util.getStringParam(request.reqObj, ParamKey.MESSAGE_ID);
            if(UserDAO.checkUser(reqId) && UserDAO.checkUser(partnerId))
                LogCallDAO.addStartCallLog(reqId, partnerId, callId, time, ip, type);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
        }
    }

    private void endCall(Request request) {
        try {
            long endTime = Util.getLongParam(request.reqObj, ParamKey.TIME);
            Date time = new Date(endTime);
            String content = Util.getStringParam(request.reqObj, ParamKey.CONTENT);
            String callId = null;
            Integer duration = null;
            String partner_respond = "";
            try {
                String[] eles = content.split("\\|");
//                for(int i = 0; i < eles.length; i++) {
//                }
                if (eles.length >= 3) {
                    partner_respond = eles[0];
                    callId = eles[1];
                    duration = Integer.parseInt(eles[2]);
                }
            } catch (Exception ex) {
               
            }
            if (callId != null && duration != null) {
                LogCallDAO.addEndCallLog(callId, time, duration, partner_respond);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }    
    
    private void addDuration(Request request) {
        try {

            long endTime = Util.getLongParam(request.reqObj, ParamKey.TIME);
            Date time = new Date(endTime);
            String content = Util.getStringParam(request.reqObj, ParamKey.CONTENT);
            String callId = null;
            Integer duration = null;
            try {
                String[] eles = content.split("\\|");
//                for(int i = 0; i < eles.length; i++) {
//                }
                if (eles.length >= 3) {
                    callId = eles[1];
                    duration = Integer.parseInt(eles[2]);
                }
            } catch (Exception ex) {
               Util.addErrorLog(ex);
            }
            if (callId != null && duration != null) {
                LogCallDAO.addDurationLog(callId, time, duration);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }    
}
