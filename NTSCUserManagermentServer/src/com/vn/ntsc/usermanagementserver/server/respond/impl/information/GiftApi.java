/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionResult;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GiftApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long giftPri = Util.getLongParam(obj, ParamKey.GIFT_PRICE);
            String reciverId = Util.getStringParam(obj, ParamKey.RECEIVER_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            if (UserDAO.checkUser(reciverId)) {
                if (!BlockUserManager.isBlock(userId, reciverId)) {
                    ActionResult actionResult = ActionManager.doAction(ActionType.send_gift, userId, reciverId, time, giftPri.intValue(), null, ip);
                    Point point = new Point(actionResult.point);
                    result = new EntityRespond(actionResult.code, point);
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
