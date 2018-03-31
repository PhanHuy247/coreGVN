/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.sticker;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class StickerCategory implements IEntity{
    
    private static final String idKey = "cat_id";
    public String id;    
    
    private static final String enCatNameKey = "en_name";
    public String enCatName;

    private static final String jpCatNameKey = "jp_name";
    public String jpCatName;   

    private static final String enCatDesKey = "en_des";
    public String enCatDes;

    private static final String jpCatDesKey = "jp_des";
    public String jpCatDes;    
    
    private static final String categoryPriceKey = "price";
    public Integer categoryPrice;

    private static final String categoryNumberKey = "stk_num";
    public Integer categoryNumber;

    private static final String liveTimeKey = "live_time";
    public Integer liveTime;    
 
    private static final String catTypeKey = "type";
    public Integer catType;    

    private static final String newFlagKey = "new_flag";
    public Integer newFlag;    
    
    private static final String publicFlagKey = "public_flag";
    public Integer publicFlag;    
    
    private static final String appleIdKey = "apple_id";
    public String appleId;    
    
    private static final String googleIdKey = "google_id";
    public String googleId;    
    
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.appleId != null)
            jo.put(appleIdKey, this.appleId);
        if(this.googleId != null)
            jo.put(googleIdKey, this.googleId);               
        if(this.categoryPrice != null)
            jo.put(categoryPriceKey, this.categoryPrice);        
        if(this.catType != null)
            jo.put(catTypeKey, this.catType);
        if(this.liveTime != null)
            jo.put(liveTimeKey, this.liveTime);        
        if(this.categoryNumber != null)
            jo.put(categoryNumberKey, this.categoryNumber);
        else
            jo.put(categoryNumberKey, 0);
        if(this.enCatName != null)
            jo.put(enCatNameKey, this.enCatName);
        if(this.jpCatName != null)
            jo.put(jpCatNameKey, jpCatName);
        if(this.enCatDes != null)
            jo.put(enCatDesKey, this.enCatDes);
        if(this.jpCatDes != null)
            jo.put(jpCatDesKey, jpCatDes); 
        if(this.newFlag != null)
            jo.put(newFlagKey, this.newFlag);
        if(this.publicFlag != null)
            jo.put(publicFlagKey, this.publicFlag);
        return jo;
    }
}
