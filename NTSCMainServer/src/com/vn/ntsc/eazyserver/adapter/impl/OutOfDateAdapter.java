/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author RuAc0n
 */
public class OutOfDateAdapter implements IServiceAdapter{
    
    private static final String Respond;
    static{
        JSONObject jo = new JSONObject();
        jo.put( ParamKey.ERROR_CODE, ErrorCode.OUT_OF_DATE_API );
        Respond = jo.toJSONString();
    }

    @Override
    public String callService(Request request) {
        return Respond;
    }
    
}
