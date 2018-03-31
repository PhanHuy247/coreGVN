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
import com.vn.ntsc.dao.impl.StickerDAO;
import com.vn.ntsc.dao.impl.UserStickerDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.StickerCategory;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class StickerCategoryDetailApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            String catId = Util.getStringParam(request.reqObj, ParamKey.CATEGORY_ID);
            String languge = Util.getStringParam(request.reqObj, ParamKey.LANGUAGE);
            StickerCategory category = StickerCategoryDAO.get(catId, languge);
            String downloadTime = UserStickerDAO.getDownloadTime(userId, catId);
            if (downloadTime != null) {
                category.isDown = 1;
                category.downloadTime = downloadTime;
            } else {
                category.isDown = 0;
            }
            List<String> list = StickerDAO.getListSticker(catId);
            category.listSticker = list;
            result = new EntityRespond(ErrorCode.SUCCESS, category);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
