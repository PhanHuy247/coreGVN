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
import com.vn.ntsc.otherservice.entity.impl.GetStickerShopInforData;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetStickerShopInforApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            List<String> downloadList = UserStickerDAO.getDownLoadList(userId);
            List<String> listNewSeen = UserStickerDAO.getNewList(userId);
            List<String> newList = StickerCategoryDAO.getListNew();
            int haveNew = 0;
            for (String str : newList) {
                if (!listNewSeen.contains(str)) {
                    haveNew = 1;
                    break;
                }
            }
            GetStickerShopInforData data = new GetStickerShopInforData(downloadList, haveNew);
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
