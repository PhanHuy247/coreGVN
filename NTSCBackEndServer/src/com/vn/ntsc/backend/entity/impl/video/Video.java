/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.video;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class Video implements IEntity {
    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String userNameKey = "user_name";
    public String username;
    
    private static final String emailKey = "email";
    public String email;  
    
    private static final String videoIdKey = "video_id";    
    public String videoId;

    private static final String videoStatusKey = "video_status";    
    public Long videoStatus;
    
    public Long uploadTime;

    private static final String uploadTimeKey = "upload_time"; 
    public String uploadTimeStr;
      
    public Long reviewTime;
    
    private static final String reviewTimeKey = "review_time";  
    public String reviewTimeStr;
    
    private static final String flagKey = "flag";
    public String flag;
    
    private static final String urlKey = "url";
    public String url;
    
    private static final String thumbNailKey = "thumb_nail";
    public String thumbNail;
    
    private static final String reportNumberKey = "report_number";
    public Integer reportNumber;
    
    private static final String reportTimeKey = "report_time";  
    public String reportTimeStr;
    
    private static final String fileTypeKey = "file_type";
    public String fileType;
    
    private static final String privacyKey = "privacy";
    public Integer privacy;

    public Video() {
    }
    public Video(String userId, String username, String videoId, Long videoStatus, String uploadTimeStr, String reviewTimeStr, String url) {
        this.userId = userId;
        this.username = username;
        this.videoId = videoId;
        this.videoStatus = videoStatus;
        this.uploadTimeStr = uploadTimeStr;
        this.reviewTimeStr = reviewTimeStr;
        this.url = url;
    }

    public Video(String userId, String username, String email, String videoId, Long videoStatus, String uploadTimeStr, String reviewTimeStr, String url) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.videoId = videoId;
        this.videoStatus = videoStatus;
        this.uploadTimeStr = uploadTimeStr;
        this.reviewTimeStr = reviewTimeStr;
        this.url = url;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.videoId != null) {
            jo.put(videoIdKey, this.videoId);
        }
        if (username != null) {
            jo.put(userNameKey, this.username);
        }
        if (uploadTimeStr != null) {
            jo.put(uploadTimeKey, this.uploadTimeStr);
        } 
        if (reviewTimeStr != null) {
            jo.put(reviewTimeKey, this.reviewTimeStr);
        }
        if (url != null) {
            jo.put(urlKey, this.url);
        }
        if(email != null){
            jo.put(emailKey,this.email);
        }
        if(reportNumber!= null){
            jo.put(reportNumberKey, this.reportNumber);
        }
        if(reportTimeStr!= null){
            jo.put(reportTimeKey, this.reportTimeStr);
        }
        if(this.videoStatus != null)
            jo.put(videoStatusKey, this.videoStatus);
        if(this.thumbNail != null)
            jo.put(thumbNailKey, this.thumbNail);
        if(this.fileType != null){
            jo.put(fileTypeKey, this.fileType);
        }
        if(this.privacy != null){
            jo.put(privacyKey, this.privacy);
        }
        return jo;
    }
    
}