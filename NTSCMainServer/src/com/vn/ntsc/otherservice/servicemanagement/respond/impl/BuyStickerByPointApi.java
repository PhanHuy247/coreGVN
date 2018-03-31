/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.dao.impl.StickerCategoryDAO;
import com.vn.ntsc.dao.impl.UserStickerDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.IntRespond;

/**
 *
 * @author RuAc0n
 */
public class BuyStickerByPointApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        IntRespond result = new IntRespond();
        try {
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            String catId = Util.getStringParam(request.reqObj, ParamKey.CATEGORY_ID);
            boolean isDownload = UserStickerDAO.isDownload(userId, catId);
            JSONObject respond = new JSONObject();
            respond.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
            if (isDownload) {
                result = new IntRespond(ErrorCode.SUCCESS, 0);
            } else {
                int point = StickerCategoryDAO.getPoint(catId);
                result = new IntRespond(ErrorCode.SUCCESS, point);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
