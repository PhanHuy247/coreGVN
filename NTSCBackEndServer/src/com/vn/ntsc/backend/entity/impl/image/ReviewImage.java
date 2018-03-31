/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.image;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class ReviewImage implements IEntity {

    private static final String notifyKey = "notify";
    public int notify;

    private static final String avatarKey = "ava";
    public int avatar;

    private static final String buzzIdKey = "buzz_id";
    public String buzzId;
    
    private static final String imageIdKey = "img_id";
    public String imageId;
    
    private static final String userIdKey = "user_id";
    public String userId;

    private static final String ipKey = "ip";
    public String ip;    
    
    private static final String userDenyKey = "user_deny";
    public String userDeny;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.imageId != null) {
            jo.put(imageIdKey, this.imageId);
        }
        if (buzzId != null) {
            jo.put(buzzIdKey, this.buzzId);
        }
        jo.put(notifyKey, this.notify);
        
        jo.put(avatarKey, this.avatar);

        if(this.ip != null)
            jo.put(ipKey, this.ip);
        
        if(this.userDeny != null)
            jo.put(userDenyKey, this.userDeny);
        
        return jo;
    }

}
