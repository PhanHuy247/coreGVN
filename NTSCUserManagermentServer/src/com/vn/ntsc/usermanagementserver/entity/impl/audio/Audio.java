/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.audio;

import com.vn.ntsc.usermanagementserver.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class Audio implements IEntity{
    
    public String userId;
    public String audioId;
    public Integer audioType;
    public Integer audioStatus;
    public Integer flag;
    public Long uploadTime;
    public Long reviewTime;
    public Integer reportFlag;
    
    public Audio(){
        
    }

    public Audio(String userId, String audioId, Integer audioType, Integer audioStatus, Integer flag, Long uploadTime) {
        this.userId = userId;
        this.audioId = audioId;
        this.audioType = audioType;
        this.audioStatus = audioStatus;
        this.flag = flag;
        this.uploadTime = uploadTime;
    }

    public Audio(String userId, String audioId, Integer audioType, Integer audioStatus, Integer flag) {
        this.userId = userId;
        this.audioId = audioId;
        this.audioType = audioType;
        this.audioStatus = audioStatus;
        this.flag = flag;
    }
    
    public Audio(String userId, String audioId, Integer flag, Integer reportFlag){
        this.userId = userId;
        this.audioId = audioId;
        this.flag = flag;
        this.reportFlag = reportFlag;
    }
    
    @Override
    public JSONObject toJsonObject() {
        return null;
    }
    
}
