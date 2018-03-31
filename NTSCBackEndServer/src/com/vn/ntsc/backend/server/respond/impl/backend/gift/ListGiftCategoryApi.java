/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.gift;

import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.gift.GiftCategoryDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.gift.GiftDAO;
import com.vn.ntsc.backend.entity.impl.gift.GiftCategory;

/**
 *
 * @author RuAc0n
 */
public class ListGiftCategoryApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try {
            List<IEntity> catList = GiftCategoryDAO.getAllCategory();
            for(IEntity entity : catList){
                GiftCategory cat = (GiftCategory) entity;
                 List<IEntity> giftList = GiftDAO.getListGift(cat.id);
                 cat.giftNumber = giftList.size();
            }
            respond = new ListEntityRespond(ErrorCode.SUCCESS, catList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new ListEntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
