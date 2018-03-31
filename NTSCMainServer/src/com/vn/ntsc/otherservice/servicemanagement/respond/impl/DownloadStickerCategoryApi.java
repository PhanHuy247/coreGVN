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
import com.vn.ntsc.otherservice.entity.impl.DownloadStickerCategoryData;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class DownloadStickerCategoryApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            String id = Util.getStringParam(request.reqObj, "sticker_cat_id");
            Long point = Util.getLongParam(request.reqObj, ParamKey.POINT);
            if (id == null) {
                id = StickerCategoryDAO.getDefaultCategoryId();
            }
            if (point != null ) { // api buy sticker by point
                int categoryType = StickerCategoryDAO.getType(id);
                UserStickerDAO.addDownLoadList(userId, id, categoryType);
                StickerCategoryDAO.increaseDownloadNumber(id);
            }
            List<String> listSticker = StickerDAO.getListSticker(id);
            DownloadStickerCategoryData data = new DownloadStickerCategoryData(listSticker, id, point);
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
