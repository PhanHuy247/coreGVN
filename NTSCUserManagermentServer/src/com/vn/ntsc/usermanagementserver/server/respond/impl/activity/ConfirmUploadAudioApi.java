/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.dao.impl.AudioDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.VideoDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.audio.Audio;
import com.vn.ntsc.usermanagementserver.entity.impl.video.ConfirmUploadVideoData;
import com.vn.ntsc.usermanagementserver.entity.impl.video.Video;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class ConfirmUploadAudioApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String audioId = Util.getStringParam(obj, ParamKey.AUDIO_ID);
//            String category = Util.getStringParam(obj, "video_cat");
//            Long auto = Util.getLongParam(obj, "auto_approved_video");
            Long uploadTime = Util.getLongParam(obj, ParamKey.TIME);
            int audioType = Constant.AUDIO_TYPE_VALUE.AUDIO_PUBLIC;
            int status = Util.getIntParam(obj, "auto_approved_video");
            int flag = Constant.FLAG.ON;            
            //1. insert to image collection
            Audio audio = new Audio(userId, audioId, audioType, status, flag,uploadTime);
            AudioDAO.insertAudio(audio);

        
            //5. set result
//            int point = UserInforManager.getPoint(userId);
//            int point = 0;
            ConfirmUploadVideoData data = new ConfirmUploadVideoData(status);
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
