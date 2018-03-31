/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionResult;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class BuyStickerApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.ID);
            Long point = Util.getLongParam(obj, ParamKey.POINT);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            ActionResult actionResult = ActionManager.doAction(ActionType.buy_sticker, userId, null, time, point.intValue(), null, ip);
            Point pnt = new Point(actionResult.point);
            result =  new EntityRespond(actionResult.code, pnt);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
