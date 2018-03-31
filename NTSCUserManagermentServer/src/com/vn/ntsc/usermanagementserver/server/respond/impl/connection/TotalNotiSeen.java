/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;
import java.util.Date;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class TotalNotiSeen implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond respond = new EntityRespond();
         try {
            String userId = (String) obj.get(ParamKey.USER_ID);
            NotificationDAO.updateIntegerListField(userId, UserdbKey.NOTIFICATION.IS_SEEN, 1);
            respond = new EntityRespond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
