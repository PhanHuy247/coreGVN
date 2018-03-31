/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.admin;

import eazycommon.constant.ErrorCode;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.admin.AdminSettingDAO;
import com.vn.ntsc.backend.entity.impl.admin.AdminSetting;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetAdminSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            AdminSetting setting = AdminSettingDAO.get();
            respond = new EntityRespond(ErrorCode.SUCCESS, setting);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
