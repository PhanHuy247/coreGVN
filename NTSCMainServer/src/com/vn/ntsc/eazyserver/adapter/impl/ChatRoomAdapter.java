/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author RuAc0n
 */
public class ChatRoomAdapter implements IServiceAdapter {

    public static final ChatRoomMis chatRoomMis = new ChatRoomMis();    
    
    @Override
    public String callService(Request request) {
        try{
            String line = InterCommunicator.sendRequest( request.toJson(), Config.ChatServerIP, Config.ChatServerPort );        
            JSONObject returnObj = new JSONObject();
            JSONObject dataObj = (JSONObject) new JSONParser().parse(line);
            returnObj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
            returnObj.put(ParamKey.DATA, dataObj);
            return returnObj.toJSONString();
        }catch(Exception ex){
            Util.addErrorLog(ex);             
            return ResponseMessage.UnknownError;
        }
    }

    public static class ChatRoomMis implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            return ResponseMessage.SuccessMessage;
        }

    }    
}
