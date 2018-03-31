/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author hoangnh
 */
public class FileData implements IEntity{
    
    private static final String fileIdKey = ParamKey.FILE_ID;
    public String fileId;
    private static final String thumbnailUrlKey = ParamKey.THUMBNAIL_URL;
    public String thumbnailUrl;
    private static final String thumbnailWidthKey = ParamKey.THUMBNAIL_WIDTH;
    public Double thumbnailWidth;
    private static final String thumbnailHeightKey = ParamKey.THUMBNAIL_HEIGHT;
    public Double thumbnailHeight;
    private static final String originalUrlKey = ParamKey.ORIGINAL_URL;
    public String originalUrl;
    private static final String originalWidthKey = ParamKey.ORIGINAL_WIDTH;
    public Double originalWidth;
    private static final String originalHeightKey = ParamKey.ORIGINAL_HEIGHT;
    public Double originalHeight;
    public static final String fileDurationKey = ParamKey.FILE_DURATION;
    public Integer fileDuration;
    private static final String fileUrlKey = ParamKey.FILE_URL;
    public String fileUrl;
    public static final String fileTypeKey = ParamKey.FILE_TYPE;
    public String fileType;
    
    public FileData(){
        
    }
    
    //for image
    public FileData(String fileId, String thumbnailUrl, Double thumbnailWidth, Double thumbnailHeight, String originalUrl, Double originalWidth, Double originalHeight){
        this.fileId = fileId;
        this.thumbnailUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + thumbnailUrl;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
        this.originalUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.ORIGINAL_IMAGE + originalUrl;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }
    
    //for video - audio
    public FileData(String fileId, String fileUrl, String thumbnailUrl, Double thumbnailWidth, Double thumbnailHeight, String originalUrl, Double originalWidth, Double originalHeight, Integer fileLength){
        this.fileId = fileId;
        this.fileUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE + fileUrl;
        this.thumbnailUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + thumbnailUrl;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
        this.originalUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.ORIGINAL_IMAGE + originalUrl;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.fileDuration = fileLength;
    }
    
    //for audio without img
    public FileData(String type, String fileId, String fileUrl, String thumbnailUrl, Double thumbnailWidth, Double thumbnailHeight, String originalUrl, Double originalWidth, Double originalHeight, Integer fileLength){
        this.fileId = fileId;
        this.fileUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE + fileUrl;
        if(type.equals("audio")){
            this.thumbnailUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.ORIGINAL_IMAGE + thumbnailUrl;
        }else{
            this.thumbnailUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + thumbnailUrl;
        }
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
        this.originalUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.ORIGINAL_IMAGE + originalUrl;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.fileDuration = fileLength;
    }
    
    //for gift
    public FileData(String fileId, String fileUrl){
        this.fileId = fileId;
        this.fileUrl = fileUrl;
    }
    
    public void addFileInfo(String fileId){
        this.fileId = fileId;
    }
    
    public void addOriginalInfo(FileData file){
        this.originalUrl = file.originalUrl;
        this.originalWidth = file.originalWidth;
        this.originalHeight = file.originalHeight;
    }
    
    public void addThumbnailInfo(FileData file){
        this.thumbnailUrl = file.thumbnailUrl;
        this.thumbnailWidth = file.thumbnailWidth;
        this.thumbnailHeight = file.thumbnailHeight;
    }
    
    public void addFileProperty(FileData file){
        this.fileUrl = file.fileUrl;
        this.fileDuration = file.fileDuration;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (fileId != null) {
            jo.put(fileIdKey, fileId);
        }
        if (thumbnailUrl != null){
            jo.put(thumbnailUrlKey, thumbnailUrl);
        }
        if (thumbnailWidth != null){
            jo.put(thumbnailWidthKey, thumbnailWidth);
        }
        if (thumbnailHeight != null){
            jo.put(thumbnailHeightKey, thumbnailHeight);
        }
        if (originalUrl != null){
            jo.put(originalUrlKey, originalUrl);
        }
        if (originalWidth != null){
            jo.put(originalWidthKey, originalWidth);
        }
        if (originalHeight != null){
            jo.put(originalHeightKey, originalHeight);
        }
        if (fileDuration != null)
            jo.put(fileDurationKey, fileDuration);
        if (fileUrl != null){
            jo.put(fileUrlKey, fileUrl);
        }
        if(fileType != null){
            jo.put(fileTypeKey, fileType);
        }
        return jo;
    }
    
}
