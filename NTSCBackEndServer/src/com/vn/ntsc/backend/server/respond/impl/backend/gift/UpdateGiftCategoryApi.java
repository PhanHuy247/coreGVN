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
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateGiftCategoryApi implements IApiAdapter {

    private static Map<String, Integer> keys = new TreeMap<String, Integer>();

    static {
        keys.put(ParamKey.ENGLISH_NAME, 4);
        keys.put(ParamKey.JAPANESE_NAME, 5);
    }

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String catId = Util.getStringParam(obj, ParamKey.CATEGORY_ID);
            if (catId == null || catId.isEmpty()) {
                return null;
            }
            String enCatName = Util.getStringParam(obj, ParamKey.ENGLISH_NAME);
            String jpCatName = Util.getStringParam(obj, ParamKey.JAPANESE_NAME);

            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                respond = new Respond(error);
                return respond;
            }
            GiftCategoryDAO.updateCategory(catId, enCatName, jpCatName);
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
