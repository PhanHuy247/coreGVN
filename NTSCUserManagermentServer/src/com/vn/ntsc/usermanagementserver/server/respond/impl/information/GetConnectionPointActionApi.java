/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetConnectionPointActionData;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetConnectionPointActionApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            String friendId = Util.getStringParam(obj, ParamKey.FRDID);
            if(imageId != null && !imageId.isEmpty()){
                friendId = ImageDAO.getUserId(imageId);
            }
            if (friendId == null) {
                return result;
            }if(friendId.equals(userId)){
                GetConnectionPointActionData data = new GetConnectionPointActionData();
                result =  new EntityRespond(ErrorCode.SUCCESS, data);
            }
            Integer saveImagePrice = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.save_image), userId, friendId).senderPrice;
            Integer chatPrice = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.chat), userId, friendId).senderPrice;
            Integer unlockBackstagePrice = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.unlock_backstage), userId, friendId).senderPrice;
            Integer unlockBackstageBonusPrice = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.unlock_backstage), userId, friendId).senderPrice;
            Integer commentPrice = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.comment_buzz), userId, friendId).senderPrice;
            GetConnectionPointActionData data = new GetConnectionPointActionData(saveImagePrice, chatPrice, unlockBackstagePrice, unlockBackstageBonusPrice, commentPrice);
            result =  new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
