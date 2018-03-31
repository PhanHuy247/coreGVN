/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.Setting;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class UpdateUploadSetting implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        Respond result = new Respond();
        try {
            String imgId = Util.getStringParam(obj, "share_has_deleted_img");
            Setting.share_has_deleted_img = imgId;
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
