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
import com.vn.ntsc.dao.impl.UserStickerDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.StickerCategory;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class StickerCategoryListApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            Long skip = Util.getLongParam(request.reqObj, ParamKey.SKIP);
            Long take = Util.getLongParam(request.reqObj, ParamKey.TAKE);
            Long type = Util.getLongParam(request.reqObj, ParamKey.TYPE);
            String languge = Util.getStringParam(request.reqObj, ParamKey.LANGUAGE);
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            if (type == 2) {
                List<String> newList = StickerCategoryDAO.getListNew();
                UserStickerDAO.addNewListSeen(newList, userId);
            }
            List<StickerCategory> list = StickerCategoryDAO.list(type, languge, skip.intValue(), take.intValue());
            result = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
