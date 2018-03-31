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
import com.vn.ntsc.otherservice.servicemanagement.MixService;

/**
 *
 * @author tuannxv00804
 */
public class StickerAdapter implements IServiceAdapter{
    public static final BuyStickerByPoint buyStickerByPoint = new BuyStickerByPoint();

    @Override
    public String callService(Request request) {
        MixService ms = new MixService();
        String result = ms.callApi(request);
        return result;
    }

    
    
    public static class BuyStickerByPoint implements IServiceAdapter{
        @Override
        public String callService( Request request ) {
            try{
                MixService ms = new MixService();            
                String result = ms.callApi(request);
                Long point;
                try{
                    JSONObject jo = (JSONObject) new JSONParser().parse( result );
                    Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                    if(code != ErrorCode.SUCCESS){
                        return result;
                    }
                    point = (Long) jo.get(ParamKey.DATA);
                    request.reqObj.put(ParamKey.POINT,point);                
                }catch(Exception ex){
                    Util.addErrorLog(ex);
                }
                String umsStr = InterCommunicator.sendRequest( request.toJson(), Config.UMSServerIP, Config.UMSPort );
                try {
                    JSONObject jo = (JSONObject) new JSONParser().parse( umsStr );
                    //DuongLTD
                    Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                    if(code != ErrorCode.SUCCESS){
                        return umsStr;
                    }
                    //
                    JSONObject json = (JSONObject) jo.get(ParamKey.DATA);
    //                point = (Long) ().get(ParamKey.POINT);
                    request.put(ParamKey.API_NAME, API.DOWNLOAD_STICKER_CATEGORY);
                    Long pointL = (Long) json.get(ParamKey.POINT);
                    request.reqObj.put(ParamKey.POINT, pointL);
                    result = ms.callApi(request);
                } catch( Exception ex ) {
                    Util.addErrorLog(ex);                 
                }
                return result;
            }catch(Exception ex){
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }
    }

}
