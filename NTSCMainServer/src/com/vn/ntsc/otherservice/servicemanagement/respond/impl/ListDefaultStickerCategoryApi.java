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
import com.vn.ntsc.dao.impl.StickerCategoryDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.StickerCategory;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListDefaultStickerCategoryApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            Long skip = Util.getLongParam(request.reqObj, ParamKey.SKIP);
            Long take = Util.getLongParam(request.reqObj, ParamKey.TAKE);
            String languge = Util.getStringParam(request.reqObj, ParamKey.LANGUAGE);
            List<StickerCategory> list = StickerCategoryDAO.listDefaultCategory(languge, skip.intValue(), take.intValue());
            result = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
