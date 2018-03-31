/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.gift;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.gift.GiftCategoryDAO;
import com.vn.ntsc.backend.dao.gift.GiftDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class DeleteGiftApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String giftId = Util.getStringParam(obj, ParamKey.GIFT_ID);
            if (giftId == null || giftId.isEmpty()) {
                return null;
            }
            GiftDAO.deleteGift(giftId);
            String catId = GiftDAO.getCategoryId(giftId);
            GiftCategoryDAO.updateGiftNumber(catId, -1);
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
