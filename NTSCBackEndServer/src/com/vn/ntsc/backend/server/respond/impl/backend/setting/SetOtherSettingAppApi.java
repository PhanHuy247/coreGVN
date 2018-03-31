/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.setting;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.setting.OtherSettingDAO;
import com.vn.ntsc.backend.entity.impl.setting.OtherSetting;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.setting.OtherSettingAppDAO;

/**
 *
 * @author Namhv
 */
public class SetOtherSettingAppApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
//            int error = OtherSetting.validate(obj);
//            if (error != ErrorCode.SUCCESS) {
//                respond = new Respond(error);
//                return respond;
//            }

            Long applicationId = (Long) obj.get(SettingdbKey.OTHER_SETTING_APP.APP_ID);
            Boolean force = (Boolean) obj.get(SettingdbKey.OTHER_SETTING_APP.FORCE_UPDATING);
            String urlWeb = (String) obj.get(SettingdbKey.OTHER_SETTING_APP.URL_WEB);
            String appVer = (String) obj.get(SettingdbKey.OTHER_SETTING_APP.APP_VERSION);
            OtherSettingAppDAO.updateOtherSettingByAppId(applicationId, force, urlWeb, appVer);
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
