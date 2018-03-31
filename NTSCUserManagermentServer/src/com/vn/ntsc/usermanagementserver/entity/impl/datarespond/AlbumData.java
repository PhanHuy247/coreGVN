/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import com.vn.ntsc.usermanagementserver.entity.IEntity;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class AlbumData implements Comparable<AlbumData>, IEntity{
    
    private static final String albumIdKey = "album_id";
    public String albumId;
    
    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String albumNameKey = "album_name";
    public String albumName;
    
    private static final String albumDesKey = "album_des";
    public String albumDes;
    
    private static final String privacyKey = "privacy";
    public Integer privacy;
    
    private static final String imageListKey = "image_list";
    public List<AlbumImageData> imageList;
    
    private static final String timeKey = "time";
    public Long time;

    public AlbumData(String albumId, String albumName, String albumDes, Integer privacy) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumDes = albumDes;
        this.privacy = privacy;
    }

    public AlbumData() {
    }
    
    private static final String imageIdKey = "image_id";
    public String imageId;
    
    private static final String numberAlbumKey = "number_album";
    public Integer numberAlbum;
    
     private static final String numberImageKey = "number_image";
    public Integer numberImage;
    
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(this.albumId != null)
            jo.put(albumIdKey, this.albumId);
        if(this.userId != null)
            jo.put(userIdKey, this.userId);
        if(this.albumName != null)
            jo.put(albumNameKey, this.albumName);
        if(this.albumDes != null)
            jo.put(albumDesKey, this.albumDes);
        if(this.privacy != null)
            jo.put(privacyKey, this.privacy);
        if(this.time != null)
            jo.put(timeKey, this.time);
        if(this.numberAlbum != null)
            jo.put(numberAlbumKey, this.numberAlbum);
        if(this.numberImage != null)
            jo.put(numberImageKey, this.numberImage);
        if (this.imageList != null){
            JSONArray arr = new JSONArray();
            for (AlbumImageData image : this.imageList){
                arr.add(image.toJsonObject());
            }
            jo.put(imageListKey, arr);
        }
        if(this.imageId != null)
            jo.put(imageIdKey, this.imageId);
        return jo;
    }

    @Override
    public int compareTo(AlbumData obj) {
        if(this.time > obj.time)
            return -1;
        else if(this.time < obj.time)
            return 1;
        else
            return 0;
    }
    
}
