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
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class SaveImageApi implements IApiAdapter {

    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            if (imageId == null || imageId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            String ip = Util.getStringParam(obj, ParamKey.IP);
            String partnerId = ImageDAO.getUserId(imageId);
            if (partnerId == null) {
                return result;
            }
            ActionResult actionResult = ActionManager.doExchangeOldVersion(userId, ActionType.save_image, partnerId, ActionType.save_image_bonus, time, ip);
            Point point = new Point(actionResult.point);
            result =  new EntityRespond(actionResult.code, point);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
