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
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class DeleteStickerVer2Api implements IApiAdapter {

    private static final int public_category_error = 4;

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String stickerId = Util.getStringParam(obj, ParamKey.ID);
            if (stickerId == null || stickerId.isEmpty()) {
                return null;
            }
            String catId = StickerDAO.getCategoryId(stickerId);
            if (catId == null) {
                return null;
            }
            int publicFlag = StickerCategoryDAO.getPublicFlag(catId);
            if (publicFlag == 1) {
                return new Respond(public_category_error);
            }
            StickerDAO.delete(stickerId);
            StickerCategoryDAO.updateNumber(catId, -1);
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
