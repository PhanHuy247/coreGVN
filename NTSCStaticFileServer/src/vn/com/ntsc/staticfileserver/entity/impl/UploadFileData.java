/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class UploadFileData implements IEntity {

    private static final String fileIdKey = "file_id";
    public String file_id;
    
    private static final String imageIdKey = "image_id";
    public String image_id;
    
    private static final String isApprovedKey = "is_app";
    public Long is_app;
    
    public UploadFileData(String file_id) {
        this.file_id = file_id;
    }
    
    public UploadFileData(String file_id, String image_id) {
        this.file_id = file_id;
        this.image_id = image_id;
    }
    
    public UploadFileData(String file_id, Long is_app) {
        this.file_id = file_id;
        this.is_app = is_app;
    }
    
    public UploadFileData(String file_id, String image_id, Long is_app) {
        this.file_id = file_id;
        this.image_id = image_id;
        this.is_app = is_app;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (file_id != null) {
            jo.put(fileIdKey, file_id);
        }
        if (image_id != null){
            jo.put(imageIdKey, image_id);
        }
        if (is_app != null){
            jo.put(isApprovedKey, is_app);
        }

        return jo;
    }

}
