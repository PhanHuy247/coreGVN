/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.gift;

import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.gift.GiftCategoryDAO;
import com.vn.ntsc.backend.dao.gift.GiftDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class OrderGiftApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            List<String> listId = Util.getListString(obj, ParamKey.LIST_ID);
            if (type == null || listId.isEmpty()) {
                return null;
            }
            if (type == 1) {
                for (int i = 0; i < listId.size(); i++) {
                    String catId = listId.get(i);
                    GiftCategoryDAO.updateOrder(catId, i + 1);
                }
            } else if (type == 2) {
                for (int i = 0; i < listId.size(); i++) {
                    String giftId = listId.get(i);
                    GiftDAO.updateOrder(giftId, i + 1);
                }
            }
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
