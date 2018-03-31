/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import java.util.Date;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.Setting;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;

/**
 *
 * @author hoangnh
 */
public class UpdateOtherSettingApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Respond result = new Respond();
        try {
            if(request.getMaxLengthBuzz() != null){
                Setting.max_length_buzz = request.getMaxLengthBuzz();
            }
            
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
