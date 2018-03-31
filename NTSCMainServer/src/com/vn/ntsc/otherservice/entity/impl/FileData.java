/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import com.vn.ntsc.otherservice.entity.IEntity;
import static com.vn.ntsc.otherservice.entity.impl.FileUrl.fileIdKey;
import static com.vn.ntsc.otherservice.entity.impl.FileUrl.fileUrlKey;
import static com.vn.ntsc.otherservice.entity.impl.FileUrl.infoFileKey;
import static com.vn.ntsc.otherservice.entity.impl.FileUrl.infoOriginalKey;
import static com.vn.ntsc.otherservice.entity.impl.FileUrl.infoThumbnailKey;
import static com.vn.ntsc.otherservice.entity.impl.FileUrl.originalUrlKey;
import static com.vn.ntsc.otherservice.entity.impl.FileUrl.thumbnailKey;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;

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
        return jo;
    }
    
    public static FileData fromJSONOject(JSONObject obj){
        FileData img = new FileData();
        String id = (String) obj.get(fileIdKey);
        if (id != null){
            img.fileId = id;
        }
        String thumbnail = (String) obj.get(thumbnailUrlKey);
        if (thumbnail != null){
            img.thumbnailUrl = thumbnail;
        }
        Double thumbnailWidth = (Double) obj.get(thumbnailWidthKey);
        if (thumbnail != null){
            img.thumbnailWidth = thumbnailWidth;
        }
        Double thumbnailHeight = (Double) obj.get(thumbnailHeightKey);
        if (thumbnail != null){
            img.thumbnailHeight = thumbnailHeight;
        }
        String originalUrl = (String) obj.get(originalUrlKey);
        if (originalUrl != null){
            img.originalUrl = originalUrl;
        }
        Double originalWidth = (Double)obj.get(originalWidthKey);
        if (originalWidth != null){
            img.originalWidth = originalWidth;
        }
        Double originalHeight = (Double)obj.get(originalHeightKey);
        if (originalHeight != null){
            img.originalHeight = originalHeight;
        }
        Long fileDuration = (Long)obj.get(fileDurationKey);
        if (fileDuration != null){
            img.fileDuration = fileDuration.intValue();
        }
        String fileUrl = (String) obj.get(fileUrlKey);
        if (fileUrl != null){
            img.fileUrl = fileUrl;
        }
        return img;
    }
    
}
