/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class AddPurchaseByAdminApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        Util.addInfoLog("AddPurchaseByAdminApi execute");
        Util.addInfoLog("Param :" + obj.toJSONString());
        try {
            String userId = Util.getStringParam(obj, ParamKey.ID);
            Long point = Util.getLongParam(obj, ParamKey.POINT);
            if (point > 0) {
                UserInforManager.increasePoint(userId, point.intValue());
                UserInforManager.setHavePurchased(userId);                
            } else {
                UserInforManager.decreasePoint(userId, 0 - point.intValue());
            }
                   
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
