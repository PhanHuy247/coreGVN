/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author duyetpt
 */
public class PushFromNewsAdapter implements IServiceAdapter {

    @Override
    public String callService(Request request) {
        try {
            Util.addDebugLog("vao PushFromBackEndNewsAdapter");
            String umsString = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            JSONObject obj = (JSONObject) new JSONParser().parse(umsString);
            Long code = (Long) obj.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                JSONArray friendList = (JSONArray) obj.get(ParamKey.DATA);
                if (friendList != null && !friendList.isEmpty()) {
                    request.reqObj.put(ParamKey.FRIEND_LIST, friendList);
                    request.put(ParamKey.API_NAME, API.PUSH_NOTIFICATION);
                    InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return ResponseMessage.SuccessMessage;
    }
}
