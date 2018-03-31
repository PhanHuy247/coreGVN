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
public class RestoreStickerByPointApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            String catId = Util.getStringParam(request.reqObj, ParamKey.CATEGORY_ID);
//            Long point = Util.getLongParam(request, ParamKey.POINT);
            boolean isDownload = UserStickerDAO.isDownload(userId, catId);
            if (!isDownload) {
                result.code = ErrorCode.NOT_DOWNLOAD;
            } else {
                List<String> listSticker = StickerDAO.getListSticker(catId);
                DownloadStickerCategoryData data = new DownloadStickerCategoryData(listSticker, catId);
                result = new EntityRespond(ErrorCode.SUCCESS, data);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
