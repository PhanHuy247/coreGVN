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
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.setting.OtherSetting;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author RuAc0n
 */
public class UpdateOtherSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            OtherSetting os = OtherSetting.createOtherSetting(obj);
            Setting.BACKSTAGE_TIME = os.unlockBackstageTime;
            Setting.VIEW_IMAGE_TIME = os.unlockViewImageTime;
            Setting.LISTEN_AUDIO_TIME = os.unlockListenAudioTime;
            Setting.WATCH_VIDEO_TIME = os.unlockWatchVideoTime;
            Setting.AUTO_HIDE_REPORTED_IMAGE = os.autoHideReportedImage;
            Setting.AUTO_APPROVE_REVIEW_USER = os.autoApproveUserInfo;
            Setting.AUTO_HIDE_REPORTED_VIDEO = os.autoHideReportedVideo;
            Setting.AUTO_HIDE_REPORTED_AUDIO = os.autoHideReportedAudio;
            Util.addDebugLog("Setting.AUTO_HIDE_REPORTED_IMAGE======="+Setting.AUTO_HIDE_REPORTED_IMAGE);
            Util.addDebugLog("Setting.AUTO_HIDE_REPORTED_VIDEO======="+Setting.AUTO_HIDE_REPORTED_VIDEO);
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
}
