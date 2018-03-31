/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.setting;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.setting.OtherSettingDAO;
import com.vn.ntsc.backend.entity.impl.setting.OtherSetting;
import com.vn.ntsc.backend.server.Setting;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class SetOtherSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            int error = OtherSetting.validate(obj);
            if (error != ErrorCode.SUCCESS) {
                respond = new Respond(error);
                return respond;
            }
            OtherSetting os = OtherSetting.createOtherSetting(obj);
            Setting.AUTO_HIDE_REPORTED_IMAGE = os.autoHideReportedImage;
            Setting.AUTO_HIDE_REPORTED_VIDEO = os.autoHideReportedVideo;
            OtherSettingDAO.update(os);
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
