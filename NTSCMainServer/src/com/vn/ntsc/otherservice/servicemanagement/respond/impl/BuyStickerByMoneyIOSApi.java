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
import com.vn.ntsc.dao.impl.BuyStickerLogDAO;
import com.vn.ntsc.dao.impl.StickerCategoryDAO;
import com.vn.ntsc.dao.impl.StickerDAO;
import com.vn.ntsc.dao.impl.UserStickerDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.DownloadStickerCategoryData;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.common.Helper;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;


/**
 *
 * @author RuAc0n
 */
public class BuyStickerByMoneyIOSApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        EntityRespond result = new EntityRespond();
        try {
            String receipt = Util.getStringParam(request.reqObj, "receipt");
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            String catId = Util.getStringParam(request.reqObj, ParamKey.CATEGORY_ID);
            String respondReceipt = Helper.getReceipt(receipt, Constant.IOS_PURCHASE_PRODUCTION_URL);
            JSONObject receiptJson = (JSONObject) new JSONParser().parse(respondReceipt);
            Long status = (Long) receiptJson.get("status");

            if (status == ErrorCode.SUCCESS) {
                JSONObject receiptObj = (JSONObject) receiptJson.get("receipt");
                String identifier = (String) receiptObj.get("unique_identifier");
                String tranId = (String) receiptObj.get("transaction_id");
                String purchaseDate = (String) receiptObj.get("purchase_date");
//                SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_PURCHASE_FORMAT);
                long purchaseTime = DateFormat.parse_yyyy_MM_dd_HH_mm_ss(purchaseDate).getTime();
                int catType = StickerCategoryDAO.getType(catId);
                UserStickerDAO.addDownLoadList(userId, catId, catType);
                StickerCategoryDAO.increaseDownloadNumber(catId);
                List<String> listSticker = StickerDAO.getListSticker(catId);
                BuyStickerLogDAO.add(userId, DateFormat.format(Util.getGMTTime()), identifier, tranId, Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION, purchaseTime);
                DownloadStickerCategoryData data = new DownloadStickerCategoryData(listSticker, catId);
                result = new EntityRespond(ErrorCode.SUCCESS, data);

            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
