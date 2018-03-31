/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.video;

import com.vn.ntsc.usermanagementserver.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class Video implements IEntity{
    
    public String userId;
    public String videoId;
    public Integer videoType;
    public Integer videoStatus;
    public Integer flag;
    public Long uploadTime;
    public Long reviewTime;
    public Integer reportFlag;
    
    public Video(){
        
    }

    public Video(String userId, String videoId, Integer videoType, Integer videoStatus, Integer flag, Long uploadTime) {
        this.userId = userId;
        this.videoId = videoId;
        this.videoType = videoType;
        this.videoStatus = videoStatus;
        this.flag = flag;
        this.uploadTime = uploadTime;
    }

    public Video(String userId, String videoId, Integer videoType, Integer videoStatus, Integer flag) {
        this.userId = userId;
        this.videoId = videoId;
        this.videoType = videoType;
        this.videoStatus = videoStatus;
        this.flag = flag;
    }
    
    public Video(String userId, String videoId, Integer flag, Integer reportFlag){
        this.userId = userId;
        this.videoId = videoId;
        this.flag = flag;
        this.reportFlag = reportFlag;
    }
    
    
    @Override
    public JSONObject toJsonObject() {
        return null;
    }
    
}
