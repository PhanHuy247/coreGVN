/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dbentity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.entity.impl.audio.Audio;
import com.vn.ntsc.usermanagementserver.entity.impl.video.Video;

/**
 *
 * @author Phan Huy
 */
public class AudioDB {
     private static final String userIdKey = "user_id";
    private String userId;

    private static final String audioIdKey = "audio_id";
    private String audioId;

    private static final String audioTypeKey = "audio_type";
    private Integer audioType;    
 
    private static final String audioStatusKey = "status";
    private Integer audioStatus;    
    
    private static final String flagKey = "flag";
    private Integer flag;    
    
    private static final String uploadTimeKey = "upload_time";
    private Long uploadTime;
    
    private static final String reviewTimeKey = "review_time";
    private Long reviewTime;
    
    public static AudioDB fromAudioEntity(Audio entity) {
        AudioDB result = new AudioDB();
        result.setAudioId(entity.audioId);
        result.setFlag(entity.flag);
        result.setUserId(entity.userId);
        result.setAudioType(entity.audioType);
        result.setAudioStatus(entity.audioStatus);
        result.setUploadTime(entity.uploadTime);
        result.setReviewTime(entity.reviewTime);
        return result;
    }
    
    public DBObject toDBObject() {
        DBObject dbObject = new BasicDBObject();
        dbObject.put(userIdKey, this.userId);
        dbObject.put(audioIdKey, this.audioId);
        dbObject.put(flagKey, this.flag);
        dbObject.put(audioStatusKey, this.audioStatus);
        dbObject.put(audioTypeKey, this.audioType);
        dbObject.put(uploadTimeKey, this.uploadTime);
        return dbObject;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }

    public Integer getAudioType() {
        return audioType;
    }

    public void setAudioType(Integer audioType) {
        this.audioType = audioType;
    }

    public Integer getAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(Integer audioStatus) {
        this.audioStatus = audioStatus;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Long reviewTime) {
        this.reviewTime = reviewTime;
    }
    
    
}
