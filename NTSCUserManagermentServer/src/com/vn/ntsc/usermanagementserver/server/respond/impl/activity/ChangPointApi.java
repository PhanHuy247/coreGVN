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
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionResult;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.LogValue;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ChangPointApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            Long point = Util.getLongParam(obj, ParamKey.POINT);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            String partnerId = Util.getStringParam(obj, ParamKey.RECIEVER);
            ActionType actionType = LogValue.getKey(type.intValue());
            
            ActionResult respond = null;
            switch (actionType) {
                case video_call:
                    if (partnerId == null) {
                        break;
                    }
                    respond = ActionManager.doCall(userId, actionType, partnerId, time, ip, point.intValue());
                    break;
                case voice_call:
                    if (partnerId == null) {
                        break;
                    }
                    respond = ActionManager.doCall(userId, actionType, partnerId, time, ip, point.intValue());
                    break;
                case trade_point_to_money:
                    respond = ActionManager.doTradeToMoney(userId, ip, time, point.intValue());
                    break;
                default:
                    break;
            }
            if (respond == null) {
                return result;
            }
            Point pnt = new Point(respond.point);
            result = new EntityRespond(respond.code, pnt);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
