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
public class AlbumImageData implements Comparable<AlbumImageData>, IEntity{

    private static final String albumIdKey = "album_id";
    public String albumId;
    
    private static final String imageIdKey = "image_id";
    public String imageId;
    
    private static final String timeKey = "time";
    public Long time;
    
    private static final String albumNameKey = "album_name";
    public String albumName;
    
    private static final String albumDesKey = "album_des";
    public String albumDes;
    
    private static final String privacyKey = "privacy";
    public Integer privacy;
    
    private static final String numberImageKey = "number_image";
    public Integer numberImage;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(this.albumId != null)
            jo.put(albumIdKey, this.albumId);
        if(this.imageId != null)
            jo.put(imageIdKey, this.imageId);
        if(this.time != null)
            jo.put(timeKey, this.time);
        if(this.albumName != null)
            jo.put(albumNameKey, this.albumName);
        if(this.albumDes != null)
            jo.put(albumDesKey, this.albumDes);
        if(this.privacy != null)
            jo.put(privacyKey, this.privacy);
        if(this.numberImage != null)
            jo.put(numberImageKey, this.numberImage);
        return jo;
        
    }
    
    @Override
    public int compareTo(AlbumImageData obj) {
        if(this.time > obj.time)
            return -1;
        else if(this.time < obj.time)
            return 1;
        else
            return 0;
    }

}
