/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ParamKey;
import eazycommon.constant.ErrorCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class DownloadStickerApi implements IApiAdapter {

    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        InputStreamReader isr = new InputStreamReader(request.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        try {
            String inputString = reader.readLine();
            JSONObject req = Util.toJSONObject(inputString);
            String stickerCat = Util.getStringParam(req, ParamKey.CATEGORY_ID);
            String token = request.getToken();
            JSONObject data = Util.toJSONObject(Helper.getListSticker(token, stickerCat, request.getApiName()));
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                respond = Helper.createDataToDownSticker(data);
            } else {
                respond.code = code.intValue();
            }
        } catch (IOException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
