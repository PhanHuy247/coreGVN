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
 * @author Administrator
 */
public class FileUrl implements IEntity{
    public static final String fileIdKey = "file_id";
    private String fileId;
    
    public static final String thumbnailKey = "thumbnail_url";
    private String thumbnail;
    
    //add by Huy 201709Oct
    public static final String infoThumbnailKey = "thumbnail_info";
    private String thumbnailInfo; 
    public static final String infoOriginalKey = "original_info";
    private String originalInfo; 
    
    public static final String originalUrlKey = "original_url";
    private String originalUrl;
    
    public static final String fileUrlKey = "file_url";
    private String fileUrl;
    
    public static final String infoFileKey = "file_info";
    private String fileInfo;

    public FileUrl(String imageId, String thumbnail, String originalUrl) {
        this.fileId = imageId;
        this.thumbnail = thumbnail;
        this.originalUrl = originalUrl;
    }

    public FileUrl(String fileId, String thumbnail, String originalUrl, String fileUrl) {
        this.fileId = fileId;
        this.thumbnail = thumbnail;
        this.originalUrl = originalUrl;
        this.fileUrl = fileUrl;
    }

    public FileUrl(String fileId, String thumbnail, String originalUrl,String thumbnailInfo, String originalInfo) {
        this.fileId = fileId;
        this.thumbnail = thumbnail;
        this.thumbnailInfo = thumbnailInfo;
        this.originalInfo = originalInfo;
        this.originalUrl = originalUrl;
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

    public FileUrl(String fileId, String originalUrl) {
        this.fileId = fileId;
        this.originalUrl = originalUrl;
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
        if (fileUrl != null){
            jo.put(fileUrlKey, fileUrl);
        }
        if (fileInfo != null){
            jo.put(infoFileKey, fileInfo);
        }
        return jo;
    }
}
