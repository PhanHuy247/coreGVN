/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import com.vn.ntsc.Config;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author RuAc0n
 */
public class AutoMessageAdapter implements IServiceAdapter {

    @Override
    public String callService(Request request) {
        try {
            String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            JSONObject umsJson;
            try {
                umsJson = (JSONObject) new JSONParser().parse(umsStr);
            } catch (ParseException ex) {
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
            Long code = (Long) umsJson.get(ParamKey.ERROR_CODE);
            if (code != ErrorCode.SUCCESS) {
                return umsStr;
            }
            String uId = (String) ((JSONObject) umsJson.get(ParamKey.DATA)).get(ParamKey.USER_ID);
            JSONArray llUser = (JSONArray) ((JSONObject) umsJson.get(ParamKey.DATA)).get(ParamKey.LIST_USER);
            String content = (String) request.getParamValue(ParamKey.CONTENT);
            content = content.replaceAll("\"", "&dq");
            content = content.replaceAll("\\\\", "&bs");
            content = content.replaceAll("\n", "&nl");
            if (llUser != null && !llUser.isEmpty()) {
                request.put(ParamKey.API_NAME, API.AUTO_MESSAGE);
                request.put(ParamKey.USER_ID, uId);
                request.put(ParamKey.CONTENT, content);
                request.reqObj.put(ParamKey.FRIEND_LIST, llUser);
                InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            }
            return ResponseMessage.SuccessMessage;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return ResponseMessage.UnknownError;
        }
    }

}
