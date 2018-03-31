/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.sticker;

import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.sticker.StickerCategoryDAO;
import com.vn.ntsc.backend.dao.sticker.StickerDAO;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class UpdateStickerImageApi implements IApiAdapter {

    private static final int img_error = 4;

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            if (id == null || id.isEmpty()) {
                return null;
            }
//            if(!StickerCategoryDAO.checkCategory(id))
//                return null;
            String img = Util.getStringParam(obj, ParamKey.IMAGE);
            if (img == null || img.isEmpty()) {
                return new Respond(img_error);
            }
            String api = Util.getStringParam(obj, ParamKey.API_NAME);
            if(api.equals(API.UPDATE_STICKER_CATEGORY_IMAGE)){
                StickerCategoryDAO.updateVersion(id);
            }else{
                String catId = StickerDAO.getCategoryId(id);
                StickerCategoryDAO.updateVersion(catId);
            }
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
