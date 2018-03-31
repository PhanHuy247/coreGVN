/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import java.util.Date;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.Setting;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;

/**
 *
 * @author hoangnh
 */
public class UpdateUploadSetting implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Respond result = new Respond();
        try {
            Setting.max_file_size = request.getFileSize();
            Setting.total_file_size = request.getTotalFileSize();
            Setting.max_image_number = request.getImageNumber();
            Setting.max_video_number = request.getVideoNumber();
            Setting.max_audio_number = request.getAudioNumber();
            Setting.total_file_upload = request.getTotalFileUpload();
            Setting.max_file_per_album = request.getMaxFilePerAlbum();
            Setting.max_length_buzz = request.getMaxLengthBuzz();
            if(request.getDefaultAudioImg() != null && !request.getDefaultAudioImg().equals("")){
                Setting.default_audio_img = request.getDefaultAudioImg();
            }
            if(request.getShareHasDeletedImg()!= null && !request.getShareHasDeletedImg().equals("")){
                Setting.share_has_deleted_img = request.getShareHasDeletedImg();
            }
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
