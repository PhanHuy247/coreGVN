/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import com.vn.ntsc.otherservice.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class FileUrl implements IEntity{
    public static final String fileIdKey = "file_id";
    private String fileId;
    
    public static final String thumbnailKey = "thumbnail_url";
    private String thumbnail;
    
    public static final String originalUrlKey = "original_url";
    private String originalUrl;
    
     //add by Huy 201709Oct
    public static final String infoThumbnailKey = "thumbnail_info";
    private String thumbnailInfo; 
    public static final String infoOriginalKey = "original_info";
    private String originalInfo; 
    
    public static final String fileUrlKey = "file_url";
    private String fileUrl;
    
    public static final String infoFileKey = "file_info";
    private String fileInfo;
    
    
    public static final String textValKey = "text_val";
    private String textVal;
    
    public static final String fileDurationKey = "file_duration";
    private String fileDuration;
    public FileUrl(String imageId, String thumbnail, String originalUrl) {
        this.fileId = imageId;
        this.thumbnail = thumbnail;
        this.originalUrl = originalUrl;
    }    

    public FileUrl(String fileId, String thumbnail, String originalUrl, String thumbnailInfo, String originalInfo) {
        this.fileId = fileId;
        this.thumbnail = thumbnail;
        this.originalUrl = originalUrl;
        this.thumbnailInfo = thumbnailInfo;
        this.originalInfo = originalInfo;
    }
    
    public FileUrl(String fileId, String fileUrl, String fileInfo, String thumbnail, String thumbnailInfo, String originalUrl, String originalInfo) {
        this.fileId = fileId;
        this.thumbnail = thumbnail;
        this.thumbnailInfo = thumbnailInfo;
        this.originalInfo = originalInfo;
        this.originalUrl = originalUrl;
        this.fileUrl = fileUrl;
        this.fileInfo = fileInfo;
    }

    public FileUrl() {
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (fileId != null){
            jo.put(fileIdKey, fileId);
        }
        if (thumbnail != null){
            jo.put(thumbnailKey, thumbnail);
        }
        if (originalUrl != null){
            jo.put(originalUrlKey, originalUrl);
        }
        if (thumbnailInfo != null){
            jo.put(infoThumbnailKey, thumbnailInfo);
        }
        if (originalInfo != null){
            jo.put(infoOriginalKey, originalInfo);
        }
        if (textVal != null){
            jo.put(textValKey, textVal);
        }
        if (fileUrl != null){
            jo.put(fileUrlKey, fileUrl);
        }
        if (fileInfo != null){
            jo.put(infoFileKey, fileInfo);
        }
        if (fileDuration != null){
            jo.put(fileDurationKey, fileDuration);
        }
        return jo;
    }
    
    public static FileUrl fromJSONOject(JSONObject obj){
        FileUrl img = new FileUrl();
        String id = (String) obj.get(fileIdKey);
        if (id != null){
            img.fileId = id;
        }
        String thumbnail = (String) obj.get(thumbnailKey);
        if (thumbnail != null){
            img.thumbnail = thumbnail;
        }
        String originalUrl = (String) obj.get(originalUrlKey);
        if (originalUrl != null){
            img.originalUrl = originalUrl;
        }
        String thumnailInfo = (String)obj.get(infoThumbnailKey);
        if (thumnailInfo != null){
            img.thumbnailInfo = thumnailInfo;
        }
        String originalInfo = (String)obj.get(infoOriginalKey);
        if (originalInfo != null){
            img.originalInfo = originalInfo;
        }
        String fileUrl = (String) obj.get(fileUrlKey);
        if (fileUrl != null){
            img.fileUrl = fileUrl;
        }
        String fileInfo = (String)obj.get(infoFileKey);
        if (fileInfo != null){
            img.fileInfo = fileInfo;
        }
        return img;
    }

    public String getImageId() {
        return fileId;
    }

    public void setImageId(String imageId) {
        this.fileId = imageId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getTextVal() {
        return textVal;
    }

    public void setTextVal(String textVal) {
        this.textVal = textVal;
    }

    public String getThumbnailInfo() {
        return thumbnailInfo;
    }

    public void setThumbnailInfo(String thumbnailInfo) {
        this.thumbnailInfo = thumbnailInfo;
    }

    public String getOriginalInfo() {
        return originalInfo;
    }

    public void setOriginalInfo(String originalInfo) {
        this.originalInfo = originalInfo;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
}
