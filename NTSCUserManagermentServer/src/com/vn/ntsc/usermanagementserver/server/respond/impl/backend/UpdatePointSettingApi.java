/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.backend;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;

/**
 *
 * @author RuAc0n
 */
public class UpdatePointSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            ActionManager.update(obj);
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
