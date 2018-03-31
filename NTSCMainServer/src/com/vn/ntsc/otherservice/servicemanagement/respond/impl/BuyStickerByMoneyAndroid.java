/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import eazycommon.util.Util;
import eazycommon.util.DateFormat;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.dao.impl.ApplicationDAO;
import com.vn.ntsc.dao.impl.BuyStickerLogDAO;
import com.vn.ntsc.dao.impl.StickerCategoryDAO;
import com.vn.ntsc.dao.impl.StickerDAO;
import com.vn.ntsc.dao.impl.UserStickerDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.DownloadStickerCategoryData;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.common.Helper;
import com.vn.ntsc.otherservice.servicemanagement.respond.common.PublicKeyManager;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;

;

/**
 *
 * @author RuAc0n
 */
public class BuyStickerByMoneyAndroid implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        EntityRespond result = new EntityRespond();
        try {
            String signature = Util.getStringParam(request.reqObj, "signature");
            String signedData = Util.getStringParam(request.reqObj, "pur_data");
            String catId = Util.getStringParam(request.reqObj, ParamKey.CATEGORY_ID);
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            String applicationId = "1";
            if (request.reqObj.get("application") != null && Util.getLongParam(request.reqObj, ParamKey.APPLICATION) != null) {
                applicationId = Util.getLongParam(request.reqObj, ParamKey.APPLICATION).toString();
            }
            JSONObject sigJson = (JSONObject) new JSONParser().parse(signedData);
            String orderId = (String) sigJson.get("orderId");
            //HUNGDT add Multiapp #6374
            String uniqueName = ApplicationDAO.getUniqueNameById(applicationId);
            String publicKey = PublicKeyManager.getPublicKey(uniqueName);
            if (Helper.verifyGooglePurchase(signedData, signature, publicKey)) {
                Long purchaseTime = (Long) sigJson.get("purchaseTime");
                int catType = StickerCategoryDAO.getType(catId);
                UserStickerDAO.addDownLoadList(userId, catId, catType);
                StickerCategoryDAO.increaseDownloadNumber(catId);
                BuyStickerLogDAO.add(userId, DateFormat.format(Util.getGMTTime()), orderId, null, Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION, purchaseTime);
                List<String> listSticker = StickerDAO.getListSticker(catId);
                DownloadStickerCategoryData data = new DownloadStickerCategoryData(listSticker, catId);
                result = new EntityRespond(ErrorCode.SUCCESS, data);
            } else {
                result.code = ErrorCode.UNKNOWN_ERROR;
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
