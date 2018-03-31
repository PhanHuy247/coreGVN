/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.audio;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class Audio implements IEntity{
    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String userNameKey = "user_name";
    public String username;
    
    private static final String emailKey = "email";
    public String email;  
    
    private static final String audioIdKey = "audio_id";    
    public String audioId;

    private static final String audioStatusKey = "audio_status";    
    public Long audioStatus;
    
   
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
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.audioId != null) {
            jo.put(audioIdKey, this.audioId);
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
        if(this.audioStatus != null)
            jo.put(audioStatusKey, this.audioStatus);
         if(this.thumbNail != null)
            jo.put(thumbNailKey, this.thumbNail);
        return jo;
    }
    
}
