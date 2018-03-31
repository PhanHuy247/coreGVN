/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import com.vn.ntsc.usermanagementserver.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class PublicFile implements IEntity, Comparable<PublicFile>{
    
    private static final String imageIdKey = "file_id";
    public String imageId;

    private static final String buzzIdKey = "buzz_id";
    public String buzzId;
    
    private static final String coverIdKey = "cover_id";
    public String coverId;
    
    private static final String timeKey = "time";
    public Long time;
    
    private static final String fileTypeKey = "file_type";
    public String fileType;
    
    public PublicFile(String imageId, String buzzId, Long time) {
        this.imageId = imageId;
        this.buzzId = buzzId;
        if(time != null){
            this.time = time;
        }else{
            this.time = 0L;
        }
    }
    
    public PublicFile(String imageId, String buzzId, String coverId, Long time) {
        this.imageId = imageId;
        this.buzzId = buzzId;
        this.coverId = coverId;
        if(time != null){
            this.time = time;
        }else{
            this.time = 0L;
        }
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.imageId != null) {
            jo.put(imageIdKey, this.imageId);
        }
        if (this.buzzId != null) {
            jo.put(buzzIdKey, this.buzzId);
        }
        if (this.coverId != null) {
            jo.put(coverIdKey, this.coverId);
        }
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if (this.fileType != null){
            jo.put(fileTypeKey, this.fileType);
        }
        return jo;
    }

    @Override
    public int compareTo(PublicFile obj) {
        if(this.time == null && obj.time == null){
            return 0;
        }else if(this.time == null && obj.time != null){
            return 1;
        }else if(this.time != null && obj.time == null){
            return -1;
        }else if(this.time > obj.time){
            return -1;
        }else if(this.time < obj.time){
            return 1;
        }else
            return 0;
    }
    
}
