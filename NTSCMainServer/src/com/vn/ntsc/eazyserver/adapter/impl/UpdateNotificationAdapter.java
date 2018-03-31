/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author RuAc0n
 */
public class UpdateNotificationAdapter implements IServiceAdapter {

    public static final Unregister unregister = new Unregister();

    @Override
    public String callService(Request request) {
        JSONObject obj = new JSONObject();
        try {
            String notiToken = (String) request.getParamValue(ParamKey.NOTIFY_TOKEN);
            Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
            if (notiToken == null || notiToken.isEmpty() || deviceType == null) {
                obj.put(ParamKey.ERROR_CODE, ErrorCode.UNKNOWN_ERROR);
                return obj.toJSONString();
            }
            request.put(ParamKey.NOFI_DEVICE_TOKEN, notiToken);
            request.reqObj.put(ParamKey.NOFI_DEVICE_TYPE, deviceType);
            InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
            obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
            return obj.toJSONString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return ResponseMessage.UnknownError;
        }
    }

    private static class Unregister implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            JSONObject obj = new JSONObject();
            try {
                String notiToken = (String) request.getParamValue(ParamKey.NOTIFY_TOKEN);
                if (notiToken == null || notiToken.isEmpty()) {
                    obj.put(ParamKey.ERROR_CODE, ErrorCode.UNKNOWN_ERROR);
                    return obj.toJSONString();
                }
                request.put(ParamKey.NOFI_DEVICE_TOKEN, notiToken);
                InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
                return obj.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                obj.put(ParamKey.ERROR_CODE, ErrorCode.UNKNOWN_ERROR);
                return ResponseMessage.UnknownError;
            }
        }
    }
}
