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
public class BuzzFileData implements IEntity{
    
    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String buzzIdKey = "buzz_id";
    public String buzzId;
    
    private static final String fileIdKey = "file_id";
    public String fileId;
    
    private static final String buzzTypeKey = "buzz_type";
    public Integer buzzType;
    
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(this.userId != null)
            jo.put(userIdKey, this.userId);
        if(this.buzzId != null)
            jo.put(buzzIdKey, this.buzzId);
        if(this.buzzType != null)
            jo.put(buzzTypeKey, this.buzzType);
        if(this.fileId != null)
            jo.put(fileIdKey, this.fileId);
        return jo;
    }
    
}
