/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import org.json.simple.JSONObject;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;

/**
 *
 * @author tuannxv00804
 */
public class TokenAdapter implements IServiceAdapter {

    @Override
    public String callService( Request request ) {
        if( request.token == null ){
            return ResponseMessage.BadResquestMessage;
        }
        
        Session session = SessionManager.getSession( request.token );
        if( session == null ){
            return ResponseMessage.InvailidTokenMessage;
        }
        
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
        JSONObject data = new JSONObject();
        data.put(ParamKey.TOKEN_STRING, request.token);
        obj.put(ParamKey.DATA, data);
        
        return obj.toJSONString();
    }
    
}
