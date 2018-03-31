/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.exception.EazyException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.impl.ImageDAO;
import vn.com.ntsc.staticfileserver.entity.impl.UploadSetting;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.Setting;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author hoangnh
 */
public class GetUploadSetting implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        EntityRespond respond = new EntityRespond();
        try {
            UploadSetting up = new UploadSetting();
            String url = ImageDAO.getImageUrl(Setting.default_audio_img);
            String deletedShareUrl = ImageDAO.getImageUrl(Setting.share_has_deleted_img);
            url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.ORIGINAL_IMAGE + url ;
            deletedShareUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.ORIGINAL_IMAGE + deletedShareUrl ;
            up = UploadSetting.createUploadSetting(Setting.max_file_size, 
                    Setting.total_file_size, 
                    Setting.max_image_number, 
                    Setting.max_video_number, 
                    Setting.max_audio_number, 
                    Setting.total_file_upload, 
                    Setting.max_length_buzz, 
                    url, 
                    Setting.max_file_per_album, 
                    deletedShareUrl,
                    Setting.default_audio_img,
                    Setting.share_has_deleted_img);
            respond = new EntityRespond(ErrorCode.SUCCESS, up);
        } catch (EazyException ex) {
            Logger.getLogger(GetUploadSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respond;
    }
    
}
