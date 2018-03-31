/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.admin;

import eazycommon.constant.ErrorCode;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.AdminSettingDAO;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class SetAdminSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            Long timezone = Util.getLongParam(obj, "time_zone");
            AdminSettingDAO.insert(timezone.intValue());
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
