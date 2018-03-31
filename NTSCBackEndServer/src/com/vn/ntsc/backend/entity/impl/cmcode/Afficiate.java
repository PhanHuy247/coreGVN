/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.cmcode;

import com.vn.ntsc.backend.entity.IEntity;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class Afficiate implements IEntity{
    private static final String idKey = "aff_id";
    public String id;

    private static final String shopNameKey = "aff_name";
    public String shopName;
    
    private static final String shopPassKey = "aff_pwd";
    public String shopPass;

    private static final String shopEmailKey = "aff_email";
    public String shopEmail;

    private static final String flagKey = "flag";
    public Integer flag;
    
    private static final String affLoginIdKey = "aff_login_id";
    public String affLoginId;
    
    private static final String mediaListKey = "media_lst";
    public List<Media> mediaList;

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.shopName != null)
            jo.put(shopNameKey, this.shopName);
        if(this.shopPass != null)
            jo.put(shopPassKey, this.shopPass);
        if(this.shopEmail != null)
            jo.put(shopEmailKey, this.shopEmail);
        if(this.flag != null)
            jo.put(flagKey, this.flag);
        if(this.affLoginId != null){
            jo.put(affLoginIdKey, this.affLoginId);
        }
        if(mediaList != null){
            JSONArray arr = new JSONArray();
            for(int i = 0; i < this.mediaList.size(); i++){
                Media so = this.mediaList.get(i);
                arr.add(so.toJsonObject());
            }
            jo.put(mediaListKey, arr);
        }
        return jo;
    }   

    public Afficiate(String id, String shopName, String shopPass, String shopEmail, Integer flag, String affLoginId) {
        this.id = id;
        this.shopName = shopName;
        this.shopPass = shopPass;
        this.shopEmail = shopEmail;
        this.flag = flag;
        this.affLoginId = affLoginId;
        this.mediaList = new ArrayList<Media>();
    }
    
}
