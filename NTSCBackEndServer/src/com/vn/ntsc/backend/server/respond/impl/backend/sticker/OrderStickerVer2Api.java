/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.sticker;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.sticker.StickerCategoryDAO;
import com.vn.ntsc.backend.dao.sticker.StickerDAO;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class OrderStickerVer2Api implements IApiAdapter {

    private static final int public_category_error = 4;

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
//            Long type = Util.getLongParam(obj, ParamKey.type);
            String catId = Util.getStringParam(obj, ParamKey.CATEGORY_ID);
            if (catId == null || catId.isEmpty()) {
                return null;
            }
            int publicFlag = StickerCategoryDAO.getPublicFlag(catId);
            if (publicFlag == 1) {
                respond = new Respond(public_category_error);
            }
            List<String> listId = Util.getListString(obj, ParamKey.LIST_ID);
            if (listId.isEmpty()) {
                return null;
            }
            for (int i = 0; i < listId.size(); i++) {
                String id = listId.get(i);
                StickerDAO.updateOrder(id, i + 1);
            }
            StickerCategoryDAO.updateVersion(catId);
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
