/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
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
public class MixServiceAdapter implements IServiceAdapter {
//    public static InsertSticker insertSticker = new InsertSticker();
//    public static InsertStickerCategory insertStickerCat = new InsertStickerCategory();
    public static CallAdapter callAdapter = new CallAdapter();

    @Override
    public String callService(Request request) {
        MixService ms = new MixService();
        return ms.callApi(request);
    }      
 
    public static class CallAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            MixService ss = new MixService();
            result = ss.callApi(request);
            try {
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
                    return result;
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }    
}
