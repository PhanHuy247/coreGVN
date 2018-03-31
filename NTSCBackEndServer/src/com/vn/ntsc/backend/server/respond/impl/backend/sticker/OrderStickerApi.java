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
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.sticker.StickerCategoryDAO;
import com.vn.ntsc.backend.dao.sticker.StickerDAO;

/**
 *
 * @author RuAc0n
 */
public class OrderStickerApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
//            Long type = Util.getLongParam(obj, ParamKey.type);
            List<String> listId = Util.getListString(obj, ParamKey.LIST_ID);
            if (listId.isEmpty()) {
                return null;
            }
            String catId = StickerDAO.getCategoryId(listId.get(0));
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
