/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.sticker;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.sticker.StickerCategoryDAO;
import com.vn.ntsc.backend.dao.sticker.StickerDAO;
import com.vn.ntsc.backend.entity.impl.sticker.InsertStickerData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class InsertStickerApi implements IApiAdapter {

    private static final int img_error = 4;

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String img = Util.getStringParam(obj, ParamKey.IMAGE);
            if (img == null || img.isEmpty()) {
                return new EntityRespond(img_error);
            }
            String catId = Util.getStringParam(obj, ParamKey.CATEGORY_ID);
            Long code = Util.getLongParam(obj, "stk_code");
            String id = Util.getStringParam(obj, ParamKey.ID);
            if (catId == null || catId.isEmpty()) {
                return null;
            }
            int order = StickerDAO.getMaxOrder(catId) + 1;
            StickerDAO.insert(id, catId, code, order);
            StickerCategoryDAO.updateNumber(catId, 1);
            InsertStickerData data = new InsertStickerData(id, code);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
