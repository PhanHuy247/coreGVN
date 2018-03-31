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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.MixService;

/**
 *
 * @author RuAc0n
 */
public class SettingAdapter {

    public static final ConfirmPurchase confirmPurchase = new ConfirmPurchase();
    public static final SettingMis settingMis = new SettingMis();

    private static class SettingMis implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                MixService ss = new MixService();
                return ss.callApi(request);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }
    }

    private static class ConfirmPurchase implements IServiceAdapter {

        @Override
        public String callService(Request request) {

            try {
                MixService ss = new MixService();
                String result = ss.callApi(request);
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    Long point = (Long) jsonResult.get(ParamKey.DATA);
                    request.reqObj.put(ParamKey.POINT, point);
                    String umsString = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                    return umsString;
                }
                return result;
            } catch (ParseException ex) {
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }
    }

}
