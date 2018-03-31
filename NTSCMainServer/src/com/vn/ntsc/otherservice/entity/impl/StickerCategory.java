/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.entity.impl;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class StickerCategory implements IEntity{

    private static final String idKey = "cat_id";
    public String id;

    private static final String categoryNameKey = "cat_name";
    public String categoryName;

    private static final String categoryDesKey = "cat_des";
    public String categoryDes;    
    
    private static final String enCatNameKey = "en_name";
    public String enCatName;

    private static final String jpCatNameKey = "jp_name";
    public String jpCatName;

    private static final String downloadTimeKey = "download_time";
    public String downloadTime;    
    
    private static final String categoryPriceKey = "cat_pri";
    public Integer categoryPrice;

    private static final String stickerNumberKey = "stk_num";
    public Integer stickerNumber;
    
    private static final String isDownKey = "is_down";
    public Integer isDown;

    private static final String liveTimeKey = "live_time";
    public Integer liveTime;    
 
    private static final String catTypeKey = "cat_type";
    public Integer catType;    
    
    private static final String listStickerCodeKey = "lst_stk_code";
    public List<String> listSticker;

    private static final String appleIdKey = "apple_id";
    public String appleId;    
    
    private static final String googleIdKey = "google_id";
    public String googleId;    
    
    private static final String newFlagKey = "new_flag";
    public Integer newFlag;
    
    private static final String versionKey = "version";
    public Integer version;
    
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.appleId != null)
            jo.put(appleIdKey, this.appleId);
        if(this.googleId != null)
            jo.put(googleIdKey, this.googleId);        
        if(this.categoryName != null)
            jo.put(categoryNameKey, this.categoryName);
        if(this.categoryDes != null)
            jo.put(categoryDesKey, this.categoryDes);        
        if(this.categoryPrice != null)
            jo.put(categoryPriceKey, this.categoryPrice);
        if(this.downloadTime != null)
            jo.put(downloadTimeKey, this.downloadTime);        
        if(this.catType != null)
            jo.put(catTypeKey, this.catType);
        if(this.liveTime != null)
            jo.put(liveTimeKey, this.liveTime);        
        if(this.stickerNumber != null)
            jo.put(stickerNumberKey, this.stickerNumber);
        if(this.enCatName != null)
            jo.put(enCatNameKey, this.enCatName);
        if(this.jpCatName != null)
            jo.put(jpCatNameKey, jpCatName);
        if(this.isDown != null)
            jo.put(isDownKey, this.isDown);
        if(this.newFlag != null)
            jo.put(newFlagKey, this.newFlag);        
        if(this.version != null)
            jo.put(versionKey, this.version);        
        if(this.listSticker != null){
            JSONArray arr = new JSONArray();
            for(String str : this.listSticker)
                arr.add(str);
            jo.put(listStickerCodeKey, arr);
        }
        return jo;
    }

}
