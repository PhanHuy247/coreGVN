/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.AutoNotifyDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class ClickPushNotificationApi implements IApiAdapter{
    

    @Override
    public Respond execute(Request request) {
        Respond result = new Respond();
        try {
            String userId = (String) request.getParamValue(ParamKey.USER_ID);
            String pushId = (String) request.getParamValue("push_id");
            if(pushId != null && !pushId.isEmpty() && userId != null && !userId.isEmpty()){
                AutoNotifyDAO.updateClickedUser(pushId, userId);
            }
            result = new Respond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }

}
