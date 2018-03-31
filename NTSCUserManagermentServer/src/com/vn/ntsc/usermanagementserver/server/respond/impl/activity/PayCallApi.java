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
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.ContactTrackingDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class PayCallApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.CALLER_ID);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            String partnerId = Util.getStringParam(obj, ParamKey.RECIEVER);
            //String ip = Util.getStringParam(obj, ParamKey.IP);
            String ip = Util.getStringParam(obj, "user_ip");

            Long minusPoint = Util.getLongParam(obj, ParamKey.POINT);
            if (userId == null || type == null || partnerId == null || ip == null || minusPoint == null) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
//            int minusPoint = 0;
            if (type != Constant.CALL_TYPE_VALUE.VOICE_CALL && type != Constant.CALL_TYPE_VALUE.VIDEO_CALL) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            ActionType actionType = ActionType.video_call;
            if (type == Constant.CALL_TYPE_VALUE.VOICE_CALL) {
                actionType = ActionType.voice_call;
            }
            ContactTrackingDAO.update(userId, partnerId);
            ContactTrackingDAO.update(partnerId, userId);
            ActionResult actionResult = ActionManager.doAction(actionType, userId, partnerId, time, minusPoint.intValue(), null, ip);
            result = new EntityRespond(actionResult.code, new Point(UserInforManager.getPoint(userId)));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
