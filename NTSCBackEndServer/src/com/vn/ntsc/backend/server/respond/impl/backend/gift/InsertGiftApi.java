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
import com.vn.ntsc.backend.dao.gift.GiftCategoryDAO;
import com.vn.ntsc.backend.dao.gift.GiftDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class InsertGiftApi implements IApiAdapter {

    private static final int price_error = 4;
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(ParamKey.IMAGE, 7);
        keys.put(ParamKey.ENGLISH_NAME, 5);
        keys.put(ParamKey.JAPANESE_NAME, 6);
    }

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {

//            String catId = Util.getStringParam(obj, ParamKey.category_id);
            String catId = GiftCategoryDAO.ID;
            Long pri = Util.getLongParam(obj, ParamKey.GIFT_PRICE);
            String giftInf = Util.getStringParam(obj, "gift_inf");
            String enGiftName = Util.getStringParam(obj, ParamKey.ENGLISH_NAME);
            String jpGiftName = Util.getStringParam(obj, ParamKey.JAPANESE_NAME);
//            if (catId == null || catId.isEmpty()) {
//                return null;
//            }
            if (pri == null || pri < 0) {
                respond = new EntityRespond(price_error);
                return respond;
            }
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                respond = new EntityRespond(error);
                return respond;
            }
            String giftId = Util.getStringParam(obj, ParamKey.ID);
            int order = GiftDAO.getMaxOrder(catId);
            GiftDAO.insertGift(giftId, catId, pri.doubleValue(), giftInf, enGiftName, jpGiftName, order + 1);
            GiftCategoryDAO.updateGiftNumber(catId, 1);
            respond = new EntityRespond(ErrorCode.SUCCESS, new InsertData(giftId));
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
