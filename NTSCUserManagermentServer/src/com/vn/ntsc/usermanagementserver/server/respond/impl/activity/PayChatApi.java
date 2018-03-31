/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionResult;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.ContactTrackingDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.dao.impl.PointExchangedByChatDAO;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class PayChatApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String partnerId = Util.getStringParam(obj, ParamKey.PARTNER_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            Long originalTime = Util.getLongParam(obj, ParamKey.TIME);
//            String messageId = Util.getStringParam(obj, ParamKey.MESSAGE_ID);
//            int beforeExchangedPoint = UserInforManager.getPoint(userId);
            ActionType actionType = ActionType.chat;
            if (type == 2) {
                actionType = ActionType.wink;
            }
            if (type == -1) {
                actionType = ActionType.payback;
            }
            ContactTrackingDAO.update(userId, partnerId);
            ContactTrackingDAO.update(partnerId, userId);
            ActionResult actionResult = ActionManager.doAction(actionType, userId, partnerId, new Date(originalTime), null, null, ip);

            /**
             * Add point difference to Point_Exchanged_By_Chat Collection, for later reconfigure if upload file/image failed.
             */
            Point point = new Point(actionResult.point);
//            int afterExchangedPoint = point.point;
//            int pointDiffer = beforeExchangedPoint - afterExchangedPoint;
//            PointExchangedByChatDAO.addDocument(messageId, userId, pointDiffer);
            
            result =  new EntityRespond(actionResult.code, point);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }

}
