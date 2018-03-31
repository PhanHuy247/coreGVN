/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dbentity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.entity.impl.video.Video;

/**
 *
 * @author Phan Huy
 */
public class VideoDB {
     private static final String userIdKey = "user_id";
    private String userId;

    private static final String videoIdKey = "video_id";
    private String videoId;

    private static final String videoTypeKey = "video_type";
    private Integer videoType;    
 
    private static final String videoStatusKey = "status";
    private Integer videoStatus;    
    
    private static final String flagKey = "flag";
    private Integer flag;    
    
    private static final String uploadTimeKey = "upload_time";
    private Long uploadTime;
    
    private static final String reviewTimeKey = "review_time";
    private Long reviewTime;
    
    public static VideoDB fromVideoEntity(Video entity) {
        VideoDB result = new VideoDB();
        result.setVideoId(entity.videoId);
        result.setFlag(entity.flag);
        result.setUserId(entity.userId);
        result.setVideoType(entity.videoType);
        result.setVideoStatus(entity.videoStatus);
        result.setUploadTime(entity.uploadTime);
        result.setReviewTime(entity.reviewTime);
        return result;
    }
    
    public DBObject toDBObject() {
        DBObject dbObject = new BasicDBObject();
        dbObject.put(userIdKey, this.userId);
        dbObject.put(videoIdKey, this.videoId);
        dbObject.put(flagKey, this.flag);
        dbObject.put(videoStatusKey, this.videoStatus);
        dbObject.put(videoTypeKey, this.videoType);
        dbObject.put(uploadTimeKey, this.uploadTime);
        return dbObject;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Integer getVideoType() {
        return videoType;
    }

    public void setVideoType(Integer videoType) {
        this.videoType = videoType;
    }

    public Integer getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(Integer videoStatus) {
        this.videoStatus = videoStatus;
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
