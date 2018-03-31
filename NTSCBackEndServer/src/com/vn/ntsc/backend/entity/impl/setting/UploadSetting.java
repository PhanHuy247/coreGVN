/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.setting;

import com.mongodb.DBObject;
import com.vn.ntsc.backend.entity.IEntity;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

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
    
    private static final String maxFilePerAlbumKey = "max_file_per_album";
    public Integer maxFilePerAlbum;
    
    private static final String shareHasDeletedImgKey = "share_has_deleted_img";
    public String shareHasDeletedImg;
    
    private static final String maxLengthBuzzKey = "max_length_buzz";
    public Integer maxLengthBuzz;
    
    public static UploadSetting createUploadSetting (JSONObject obj){
        UploadSetting uploadSetting = new UploadSetting();
        
        Long maxFileSize = Util.getLongParam(obj, fileSizeKey);
        uploadSetting.fileSize = maxFileSize.intValue();
        
        Long totalFileSize = Util.getLongParam(obj, totalFileSizeKey);
        uploadSetting.totalFileSize = totalFileSize.intValue();
        
        Long imageFileNumber = Util.getLongParam(obj, imageNumberKey);
        uploadSetting.imageNumber = imageFileNumber.intValue();
        
        Long videoFileNumber = Util.getLongParam(obj, videoNumberKey);
        uploadSetting.videoNumber = videoFileNumber.intValue();
        
        Long audioFileNumber = Util.getLongParam(obj, audioNumberKey);
        uploadSetting.audioNumber = audioFileNumber.intValue();
        
        Long totalFileUpload = Util.getLongParam(obj, totalFileUploadKey);
        uploadSetting.totalFileUpload = totalFileUpload.intValue();
        
        String imgId = Util.getStringParam(obj, defaultAudioImgKey);
        uploadSetting.defaultAudioImg = imgId;
        
        Long maxFilePerAlbum = Util.getLongParam(obj, maxFilePerAlbumKey);
        uploadSetting.maxFilePerAlbum = maxFilePerAlbum.intValue();
        
        String shareImgId = Util.getStringParam(obj, shareHasDeletedImgKey);
        uploadSetting.shareHasDeletedImg = shareImgId;
        
        Long maxLengthBuzz = Util.getLongParam(obj, maxLengthBuzzKey);
        uploadSetting.maxLengthBuzz = maxLengthBuzz.intValue();
        
        return uploadSetting;
    }
    
    public static UploadSetting createUploadSetting (DBObject obj){
        UploadSetting uploadSetting = new UploadSetting();
        
        Integer maxFileSize = (Integer) obj.get(fileSizeKey);
        uploadSetting.fileSize = maxFileSize;
        
        Integer totalFileSize = (Integer) obj.get(totalFileSizeKey);
        uploadSetting.totalFileSize = totalFileSize;
        
        Integer imageFileNumber = (Integer) obj.get(imageNumberKey);
        uploadSetting.imageNumber = imageFileNumber;
        
        Integer videoFileNumber = (Integer) obj.get(videoNumberKey);
        uploadSetting.videoNumber = videoFileNumber;
        
        Integer audioFileNumber = (Integer) obj.get(audioNumberKey);
        uploadSetting.audioNumber = audioFileNumber;
        
        Integer totalFileUpload = (Integer) obj.get(totalFileUploadKey);
        uploadSetting.totalFileUpload = totalFileUpload;
        
        String imgId = (String) obj.get(defaultAudioImgKey);
        uploadSetting.defaultAudioImg = imgId;
        
        Integer maxFilePerAlbum = (Integer) obj.get(maxFilePerAlbumKey);
        uploadSetting.maxFilePerAlbum = maxFilePerAlbum;
        
        String shareImgId = (String) obj.get(shareHasDeletedImgKey);
        uploadSetting.shareHasDeletedImg = shareImgId;
        
        Integer maxLengthBuzz = (Integer) obj.get(maxLengthBuzzKey);
        uploadSetting.maxLengthBuzz = maxLengthBuzz;
        
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
        if(maxFilePerAlbum != null)
            jo.put(maxFilePerAlbumKey, this.maxFilePerAlbum);
        if(shareHasDeletedImg != null)
            jo.put(shareHasDeletedImgKey, this.shareHasDeletedImg);
        if(maxLengthBuzz != null)
            jo.put(maxLengthBuzzKey, this.maxLengthBuzz);
        return jo;
    }
    
}
