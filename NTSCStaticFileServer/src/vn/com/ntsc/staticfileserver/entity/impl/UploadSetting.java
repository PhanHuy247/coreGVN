/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author hoangnh
 */
public class UploadSetting implements IEntity{
    
    private static final String fileSizeKey = "max_file_size";
    public Integer fileSize;
    
    private static final String totalFileSizeKey = "total_file_size";
    public Integer totalFileSize;
    
    private static final String imageNumberKey = "max_image_number";
    public Integer imageNumber;
    
    private static final String videoNumberKey = "max_video_number";
    public Integer videoNumber;
    
    private static final String audioNumberKey = "max_audio_number";
    public Integer audioNumber;
    
    private static final String totalFileUploadKey = "total_file_upload";
    public Integer totalFileUpload;
    
    private static final String defaultAudioImgKey = "default_audio_img";
    public String defaultAudioImg;
    
    private static final String maxLengthBuzzKey = "max_length_buzz";
    public Integer maxLengthBuzz;
    
    private static final String maxFilePerAlbumKey = "max_file_per_album";
    public Integer maxFilePerAlbum;
    
    private static final String shareHasDeletedImgKey = "share_has_deleted_img";
    public String shareHasDeletedImg;
    
    private static final String defaultAudioImgIdKey = "default_audio_img_id";
    public String defaultAudioImgId;
    
    private static final String shareHasDeletedImgIdKey = "share_has_deleted_img_id";
    public String shareHasDeletedImgId;
    
    public static UploadSetting createUploadSetting (Integer maxFileSize, Integer totalFileSize, Integer imageFileNumber, Integer videoFileNumber, Integer audioFileNumber, Integer totalFileUpload, Integer maxLengthBuzz, String imgId, Integer maxFilePerAlbum, String deletedShareImg, String defaultAudioImgId, String shareHasDeletedImgId){
        UploadSetting uploadSetting = new UploadSetting();
        
        uploadSetting.fileSize = maxFileSize;
        uploadSetting.totalFileSize = totalFileSize;
        uploadSetting.imageNumber = imageFileNumber;
        uploadSetting.videoNumber = videoFileNumber;
        uploadSetting.audioNumber = audioFileNumber;
        uploadSetting.totalFileUpload = totalFileUpload;
        uploadSetting.maxLengthBuzz = maxLengthBuzz;
        uploadSetting.defaultAudioImg = imgId;
        uploadSetting.maxFilePerAlbum = maxFilePerAlbum;
        uploadSetting.shareHasDeletedImg = deletedShareImg;
        uploadSetting.defaultAudioImgId = defaultAudioImgId;
        uploadSetting.shareHasDeletedImgId = shareHasDeletedImgId;
        
        return uploadSetting;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(fileSize != null)
            jo.put(fileSizeKey, this.fileSize);
        if(totalFileSize != null)
            jo.put(totalFileSizeKey, this.totalFileSize);
        if(imageNumber != null)
            jo.put(imageNumberKey, this.imageNumber);
        if(videoNumber != null)
            jo.put(videoNumberKey, this.videoNumber);
        if(audioNumber != null)
            jo.put(audioNumberKey, this.audioNumber);
        if(totalFileUpload != null)
            jo.put(totalFileUploadKey, this.totalFileUpload);
        if(defaultAudioImg != null)
            jo.put(defaultAudioImgKey, this.defaultAudioImg);
        if(maxLengthBuzz != null)
            jo.put(maxLengthBuzzKey, this.maxLengthBuzz);
        if(maxFilePerAlbum != null)
            jo.put(maxFilePerAlbumKey, this.maxFilePerAlbum);
        if(shareHasDeletedImg != null)
            jo.put(shareHasDeletedImgKey, this.shareHasDeletedImg);
        if(defaultAudioImgId != null)
            jo.put(defaultAudioImgIdKey, this.defaultAudioImgId);
        if(shareHasDeletedImgId != null)
            jo.put(shareHasDeletedImgIdKey, this.shareHasDeletedImgId);
        return jo;
    }
    
}
