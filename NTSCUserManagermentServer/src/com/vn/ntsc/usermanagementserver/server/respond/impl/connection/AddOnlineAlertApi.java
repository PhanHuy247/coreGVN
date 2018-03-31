/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogOnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;

/**
 *
 * @author RuAc0n
 */
public class AddOnlineAlertApi implements IApiAdapter {

    private static final int ADD_ALERT = 1;
    private static final int REMOVE_ALERT = 0;
    
    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            Long alertMe = Util.getLongParam(obj, "is_alt");
            String ip = Util.getStringParam(obj, ParamKey.IP);
            Long number = Util.getLongParam(obj, ParamKey.ALERT_FREQUENCY);
            Long type = (long) 1;
            boolean check = UserDAO.checkUser(friendId);
            if (check && !friendId.equals(userId)) {
                if (alertMe == ADD_ALERT) {
                    if (!BlockUserManager.isBlock(userId, friendId)) {
                        OnlineAlertDAO.addAlert(friendId, userId, number.intValue(), type.intValue());
                        LogOnlineAlertDAO.addLog(userId, friendId, time, alertMe.intValue(), number.intValue(), ip);
                        result.code = ErrorCode.SUCCESS;
                    } else {
                        result.code = ErrorCode.BLOCK_USER;
                    }
                } else if (alertMe == REMOVE_ALERT) {
                    number = (long) -1;
                    OnlineAlertDAO.addAlert(friendId, userId, number.intValue(), type.intValue());
                    LogOnlineAlertDAO.addLog(userId, friendId, time, alertMe.intValue(), number.intValue(), ip);
                    result.code = ErrorCode.SUCCESS;
                }
            } else {
                result.code = ErrorCode.USER_NOT_EXIST;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
