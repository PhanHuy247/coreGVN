/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.setting;

import com.vn.ntsc.backend.dao.setting.UploadSettingDAO;
import com.vn.ntsc.backend.entity.impl.setting.UploadSetting;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class SetUploadSetting implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            UploadSetting uploadSetting = UploadSetting.createUploadSetting(obj);
            if(uploadSetting.imageNumber > uploadSetting.totalFileUpload || uploadSetting.videoNumber > uploadSetting.totalFileUpload || uploadSetting.audioNumber > uploadSetting.totalFileUpload){
                respond = new Respond(ErrorCode.MAX_FILE_NUMBER);
            }else{
                UploadSettingDAO.updateUploadSetting(uploadSetting);
                respond = new Respond(ErrorCode.SUCCESS);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
