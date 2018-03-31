/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.gift;

import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.gift.GiftDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateGiftApi implements IApiAdapter {

    private static final int price_error = 4;
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(ParamKey.ENGLISH_NAME, 5);
        keys.put(ParamKey.JAPANESE_NAME, 6);
    }

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String giftId = Util.getStringParam(obj, ParamKey.GIFT_ID);
            Long pri = Util.getLongParam(obj, ParamKey.GIFT_PRICE);
            String giftInf = Util.getStringParam(obj, "gift_inf");
            String enGiftName = Util.getStringParam(obj, ParamKey.ENGLISH_NAME);
            String jpGiftName = Util.getStringParam(obj, ParamKey.JAPANESE_NAME);
            if (giftId == null || giftId.isEmpty()) {
                return null;
            }

            if (pri == null || pri < 0) {
                respond = new Respond(price_error);
                return respond;
            }
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                respond = new Respond(error);
                return respond;
            }

            GiftDAO.updateGift(giftId, pri.doubleValue(), giftInf, enGiftName, jpGiftName);
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
