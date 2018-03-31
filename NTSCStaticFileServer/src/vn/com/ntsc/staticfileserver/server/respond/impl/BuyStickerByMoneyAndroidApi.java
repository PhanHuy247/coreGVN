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
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;

/**
 *
 * @author RuAc0n
 */
public class BuyStickerByMoneyAndroidApi implements IApiAdapter {

    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        InputStreamReader isr = new InputStreamReader(request.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        try {
            String inputString = reader.readLine();
            JSONObject req = Util.toJSONObject(inputString);
            String stickerCat = Util.getStringParam(req, ParamKey.CATEGORY_ID);
            String token = request.getToken();
            String signature = Util.getStringParam(req, "signature");
            String purchaseData = Util.getStringParam(req, "pur_data");
            JSONObject json = new JSONObject();
            json.put(ParamKey.TOKEN_STRING, token);
            json.put("signature", signature);
            json.put("pur_data", purchaseData);
            json.put(ParamKey.CATEGORY_ID, stickerCat);
            JSONObject data = Util.toJSONObject(Helper.requestAndG(json.toJSONString()));
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
