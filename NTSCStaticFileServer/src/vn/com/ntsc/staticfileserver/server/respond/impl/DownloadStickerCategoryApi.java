/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ErrorCode;
import java.util.Date;
import eazycommon.constant.API;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class DownloadStickerCategoryApi implements IApiAdapter {

    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        String stickerCat = request.getStickerCatId();
        try {
            String token = request.getToken();
            JSONObject data = Util.toJSONObject(Helper.getListSticker(token, stickerCat, API.DOWNLOAD_STICKER_CATEGORY));
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                respond = Helper.createDataToDownSticker(data);
            } else {
                respond.code = code.intValue();
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
