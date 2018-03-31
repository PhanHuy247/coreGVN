/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.gift;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateGiftImageApi implements IApiAdapter {

    private static final int img_error = 4;

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String giftId = Util.getStringParam(obj, ParamKey.GIFT_ID);
            if (giftId == null || giftId.isEmpty()) {
                return null;
            }
            String img = Util.getStringParam(obj, ParamKey.IMAGE);
            if (img == null || img.isEmpty()) {
                return new Respond(img_error);
            }
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
