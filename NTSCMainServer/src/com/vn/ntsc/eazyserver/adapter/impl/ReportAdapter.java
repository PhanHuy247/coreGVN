/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.eazyserver.adapter.impl;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author RuAc0n
 */
public class ReportAdapter implements IServiceAdapter {

    @Override
    public String callService(Request request) {
        try{
            String result = InterCommunicator.sendRequest(request.toJson(),Config.UMSServerIP, Config.UMSPort);
            JSONObject obj = (JSONObject) new JSONParser().parse(result);
            Long code = (Long) obj.get(ParamKey.ERROR_CODE);
            if(code == ErrorCode.SUCCESS){
                JSONObject dataObj = (JSONObject) obj.get(ParamKey.DATA);
                if(dataObj != null){
                    String userId = (String) dataObj.get(ParamKey.USER_ID);
                    request.put(ParamKey.USER_ID, userId);
                    Long isAppear = (Long) dataObj.get("is_appear");
                    String buzzId = (String) dataObj.get(ParamKey.BUZZ_ID);
                    if(isAppear == Constant.FLAG.OFF && buzzId != null){
                        request.put(ParamKey.BUZZ_ID, buzzId);
                        InterCommunicator.sendRequest(request.toJson(),Config.BuzzServerIP, Config.BuzzServerPort);
                    }
                    Long isAva = (Long) dataObj.get(ParamKey.IS_AVATAR);
                    if(isAva != null && isAva == Constant.FLAG.ON){
                        request.put(ParamKey.API_NAME, API.UPDATE_USER_INFOR);
                        request.put(ParamKey.IMAGE_ID, Constant.NO_AVATAR_STRING_VALUE);
                        InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                    }
                    dataObj.remove(ParamKey.BUZZ_ID);
                    dataObj.remove(ParamKey.IS_AVATAR);
                }
            }
            return result;
        }catch(Exception ex){
            Util.addErrorLog(ex);             
            return ResponseMessage.UnknownError;
        }
    }
}
