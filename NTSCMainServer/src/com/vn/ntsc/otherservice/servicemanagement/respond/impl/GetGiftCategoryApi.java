/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.GiftDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.Gift;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetGiftCategoryApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String catId = Util.getStringParam(request.reqObj, ParamKey.CATEGORY_ID);
            String language = Util.getStringParam(request.reqObj, ParamKey.LANGUAGE);
            if (catId == null || catId.isEmpty()) {
                return result;
            }
            List<Gift> giftList = GiftDAO.getListGiftInfor(catId, language);
            result = new ListEntityRespond(ErrorCode.SUCCESS, giftList);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
