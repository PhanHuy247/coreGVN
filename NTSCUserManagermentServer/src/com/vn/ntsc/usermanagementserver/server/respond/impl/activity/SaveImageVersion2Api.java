/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionResult;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.SaveImageData;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class SaveImageVersion2Api implements IApiAdapter {

    @Override
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
            ActionResult actionResult = ActionManager.doAction(ActionType.save_image, userId, partnerId, time, null, null, ip);
            ConnectionPrice price = ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.save_image), userId, partnerId);
            SaveImageData data = new SaveImageData(actionResult.point, 0 - price.senderPrice );
            result =  new EntityRespond(actionResult.code, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
