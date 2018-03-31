/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.sticker;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.sticker.StickerCategoryDAO;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;

/**
 *
 * @author RuAc0n
 */
public class UpdateStickerCategoryVer2Api implements IApiAdapter {

    private static final int type_error = 4;
    private static final int price_error = 5;
    private static final int new_flag_error = 11;
    private static final int live_time_error = 12;
    private static Map<String, Integer> keys = new TreeMap<String, Integer>();

    static {
        keys.put(ParamKey.ENGLISH_NAME, 6);
        keys.put(ParamKey.JAPANESE_NAME, 7);
        keys.put(ParamKey.ENGLISH_DESCRIPTION, 8);
        keys.put(ParamKey.JAPANESE_DESCRIPTION, 9);
    }

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String catId = Util.getStringParam(obj, ParamKey.ID);
//            int catType = StickerCategoryDAO.getCategoryType(catId);
            if (catId == null || catId.isEmpty()) {
                return null;
            }
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            if (type == null || type < 0 || type > 2) {
                return new Respond(type_error);
            }
            Long price = Util.getLongParam(obj, ParamKey.PRICE);
            if (price == null || price < 0) {
                return new Respond(price_error);
            }
            if (type <= 1 && price != 0) {
                return new Respond(price_error);
            }
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                return new Respond(error);
            }
            Long buyTime = Util.getLongParam(obj, "live_time");
            if ((type == 2 || type == 4) && (buyTime == null || buyTime < 0 || buyTime > 1000)) {
                return new Respond(live_time_error);
            }
            Long newFlag = Util.getLongParam(obj, "new_flag");
            if (newFlag == null || newFlag < 0 || newFlag > 1) {
                return new Respond(new_flag_error);
            }
            String enCatName = Util.getStringParam(obj, ParamKey.ENGLISH_NAME);
            String jpCatName = Util.getStringParam(obj, ParamKey.JAPANESE_NAME);
            String enDes = Util.getStringParam(obj, ParamKey.ENGLISH_DESCRIPTION);
            String jpDes = Util.getStringParam(obj, ParamKey.JAPANESE_DESCRIPTION);

            StickerCategoryDAO.updateCategoryVer2(catId, enCatName, jpCatName, enDes, jpDes,
                    newFlag.intValue(), buyTime.intValue(), type.intValue(), price.intValue());
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
