/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import eazycommon.util.Util;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.Setting;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class UpdateOtherSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        Respond result = new Respond();
        try {
            Long comment = Util.getLongParam(obj, "auto_approved_comment");
            Setting.auto_approve_comment = comment.intValue();
            Long buzz = Util.getLongParam(obj, "auto_approved_buzz");
            Setting.auto_approve_buzz = buzz.intValue();
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
}
