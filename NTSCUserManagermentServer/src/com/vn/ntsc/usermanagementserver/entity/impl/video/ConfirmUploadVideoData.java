/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.video;

import com.vn.ntsc.usermanagementserver.entity.IEntity;
import eazycommon.constant.ParamKey;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class ConfirmUploadVideoData implements IEntity{
    private static final String autoApprovedKey = "is_app";
    public Integer autoApproved;

    private static final String imageTypeKey = "img_type";
    public Integer imageType;

    public int point;
    
    private static final String listFavKey = "fvt_lst";
    public List<String> lstFav;
    
    private static final String userNameKey = "user_name";
    public String userName;
    
    private static final String streamIdKey = "stream_id";
    public String streamId;
    
    private static final String listTagKey = "lst_tag";
    public List<String> lstTag;
    
    public ConfirmUploadVideoData(){
        
    }
    
    public ConfirmUploadVideoData(Integer auto){
        this.autoApproved = auto;
    }

    public ConfirmUploadVideoData(Integer autoApproved, Integer imageType, int point) {
        this.autoApproved = autoApproved;
        this.imageType = imageType;
        this.point = point;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.autoApproved != null) {
            jo.put(autoApprovedKey, this.autoApproved);
        }
        if (this.imageType != null) {
            jo.put(imageTypeKey, this.imageType);
        }
        if(this.lstFav != null){
            jo.put(listFavKey, this.lstFav);
        }
        if(this.userName != null){
            jo.put(userNameKey, this.userName);
        }
        if(this.streamId != null){
            jo.put(streamIdKey, this.streamId);
        }
        if(this.lstTag != null){
            jo.put(listTagKey, this.lstTag);
        }
        jo.put(ParamKey.POINT, point ); 
        return jo;
    }
}
