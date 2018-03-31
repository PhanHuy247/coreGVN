/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.servicemanagement;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.respond.APIManager;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class MixService {
    private static final String BadResquestMessage;
    static{
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE,ErrorCode.WRONG_DATA_FORMAT);
        BadResquestMessage = obj.toJSONString();
    }
    
    public String callApi( Request request ){
        if(request == null){
            return BadResquestMessage;
        }
        String api = (String) request.getParamValue(ParamKey.API_NAME);
        if( api == null ){
            return BadResquestMessage;
        }
        IApiAdapter adapter = APIManager.getApi(api);
        if(adapter == null)
            return BadResquestMessage;
        Respond respond = adapter.execute(request);
        if(respond == null)
            return BadResquestMessage;
        return  respond.toString();
    }

}
