/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import java.util.Date;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Administrator
 */
public class DeleteNotificationApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Util.addDebugLog("Test obj from DeleteNotificationAPi "+obj.toJSONString());
        EntityRespond respond = new EntityRespond();
        try {
            String notiId = (String) obj.get("noti_id");
            if(notiId == null){
                respond = new EntityRespond(ErrorCode.NO_CONTENT);
            }
            else{
                NotificationDAO.removeNotification(new ObjectId(notiId));
                respond = new EntityRespond(ErrorCode.SUCCESS);
//                respond = new EntityRespond(ErrorCode.UNKNOWN_ERROR);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
