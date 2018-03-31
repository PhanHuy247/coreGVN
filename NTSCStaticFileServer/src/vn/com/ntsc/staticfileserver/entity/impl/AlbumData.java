/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author hoangnh
 */
public class AlbumData implements IEntity{
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
    public FileData imageList;
    
    private static final String timeKey = "time";
    public Long time;
    
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
        if (this.imageList != null){
            jo.put(imageListKey, this.imageList.toJsonObject());
        }
        if(this.time != null)
            jo.put(timeKey, this.time);
        if(this.numberImage != null)
            jo.put(numberImageKey, numberImage);
        return jo;
    }
}
