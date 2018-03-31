/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.video;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class FileInfo implements IEntity{
    
    public String userId;
    public String fileId;
    public Integer fileType;
    public Integer fileStatus;
    public Integer flag;
    public Long uploadTime;
    public Long reviewTime;
    public Integer reportFlag;
    
    @Override
    public JSONObject toJsonObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
