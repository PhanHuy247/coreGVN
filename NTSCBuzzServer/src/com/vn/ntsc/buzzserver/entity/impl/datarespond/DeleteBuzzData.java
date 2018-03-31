/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class DeleteBuzzData implements IEntity {

    private static final String buzzTypeKey = "buzz_type";
    public Long buzz_type;

    private static final String buzzValKey = "buzz_val";
    public String buzz_val;
    
    private static final String isStatusKey = "is_status";
    public Long is_status;
    
    private static final String fileIdKey = "file_id";
    public String file_id;
    
    private static final String buzzIdKey = "buzz_id";
    public String buzzId;
    
    private static final String userIdKey = "user_id";
    public String userId;

    public DeleteBuzzData(Long buzzType, String buzzValue, Long is_status) {
        this.buzz_type = buzzType;
        this.buzz_val = buzzValue;
        this.is_status = is_status;
    }

    public DeleteBuzzData(Long buzz_type, String buzz_val, Long is_status, String file_id,String buzzId, String userId) {
        this.buzz_type = buzz_type;
        this.buzz_val = buzz_val;
        this.is_status = is_status;
        this.file_id = file_id;
        this.buzzId = buzzId;
        this.userId = userId;
    }
    
    

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (buzz_type != null) {
            jo.put(buzzTypeKey, buzz_type);
        }
        if (buzz_val != null) {
            jo.put(buzzValKey, buzz_val);
        }
        if (is_status != null) {
            jo.put(isStatusKey, is_status);
        }      
        if (file_id != null) {
            jo.put(fileIdKey, file_id);
        }  
        if (buzzId != null) {
            jo.put(buzzIdKey, buzzId);
        }  
        if (userId != null) {
            jo.put(userIdKey, userId);
        }  
        return jo;
    }

}
