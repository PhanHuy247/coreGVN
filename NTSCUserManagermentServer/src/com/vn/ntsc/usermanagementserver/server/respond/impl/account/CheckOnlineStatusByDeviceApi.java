/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.OnlineStatus;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class CheckOnlineStatusByDeviceApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond respond = new Respond();
        try {
            String deviceId = (String) obj.get(ParamKey.DEVICE_ID);
            boolean isUsed = UserDAO.isDeviceIdInUse(deviceId);
            if (isUsed){
                respond = new EntityRespond(ErrorCode.SUCCESS, new OnlineStatus(true));
            }
            else {
                respond = new EntityRespond(ErrorCode.SUCCESS, new OnlineStatus(false));
            }
        }
        catch (Exception e){
            Util.addErrorLog(e);
        }
        return respond;
    }
    
}
