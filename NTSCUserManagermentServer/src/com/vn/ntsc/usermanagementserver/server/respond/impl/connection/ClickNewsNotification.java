/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.AutoNewsNotifyDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Administrator
 */
public class ClickNewsNotification implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond respond = new EntityRespond();
        try {
            String pushId = (String) obj.get("push_id");
            String userId = (String) obj.get("user_id");
            //String notiId = (String) obj.get("noti_id");
            boolean check = AutoNewsNotifyDAO.checkIfExistClickedUser(userId, pushId);
            if(check){
                AutoNewsNotifyDAO.addClickedUser(userId, pushId);
            }
            NotificationDAO.updateSeenForNewsNotificationClickByPushId(pushId, userId);
            respond = new EntityRespond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
