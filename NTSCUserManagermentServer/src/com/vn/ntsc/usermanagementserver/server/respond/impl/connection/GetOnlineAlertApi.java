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
import com.vn.ntsc.usermanagementserver.entity.impl.notification.OnlineAlert;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetOnlineAlertApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            boolean check = UserDAO.checkUser(friendId);
            if (check) {
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    OnlineAlert ola = OnlineAlertDAO.getOnlineAlert(friendId, userId);
                    result = new EntityRespond(ErrorCode.SUCCESS, ola);
                } else {
                    result.code = ErrorCode.BLOCK_USER;
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
