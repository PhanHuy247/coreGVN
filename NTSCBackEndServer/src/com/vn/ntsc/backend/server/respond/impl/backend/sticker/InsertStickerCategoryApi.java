/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.sticker;

import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.sticker.StickerCategoryDAO;
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
public class InsertStickerCategoryApi implements IApiAdapter {

    private static final int type_error = 4;
    private static final int price_error = 5;
    private static Map<String, Integer> keys = new TreeMap<String, Integer>();

    static {
        keys.put(ParamKey.ENGLISH_NAME, 6);
        keys.put(ParamKey.JAPANESE_NAME, 7);
        keys.put(ParamKey.ENGLISH_DESCRIPTION, 8);
        keys.put(ParamKey.JAPANESE_DESCRIPTION, 9);
        keys.put(ParamKey.IMAGE, 10);
    }

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            if (type == null || type < 0) {
                return new EntityRespond(type_error);
            }
            Long price = Util.getLongParam(obj, ParamKey.PRICE);
            if (price == null || price < 0) {
                return new EntityRespond(price_error);
            }
            if (type == 1 && price != 0) {
                return new EntityRespond(price_error);
            }
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                respond = new EntityRespond(error);
                return respond;
            }
            String enCatName = Util.getStringParam(obj, ParamKey.ENGLISH_NAME);
            String jpCatName = Util.getStringParam(obj, ParamKey.JAPANESE_NAME);
            String enDes = Util.getStringParam(obj, ParamKey.ENGLISH_DESCRIPTION);
            String jpDes = Util.getStringParam(obj, ParamKey.JAPANESE_DESCRIPTION);

            String catId = Util.getStringParam(obj, ParamKey.ID);

            int order = StickerCategoryDAO.getMaxOrder() + 1;
            String id = StickerCategoryDAO.insert(catId, enCatName, jpCatName, price.intValue(), type.intValue(), enDes, jpDes, order);
            respond = new EntityRespond(ErrorCode.SUCCESS, new InsertData(id));
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
