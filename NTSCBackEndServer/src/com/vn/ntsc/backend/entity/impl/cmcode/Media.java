/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.cmcode;

import com.vn.ntsc.backend.entity.IEntity;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class Media implements IEntity{
    private static final String idKey = "media_id";
    public String id;

    private static final String afficiateIdKey = "aff_id";
    public String afficiateId;
    
    private static final String mediaNameKey = "media_name";
    public String mediaName;

    private static final String mediaUrlKey = "media_url";
    public String mediaUrl;

    private static final String flagKey = "flag";
    public Integer flag;
    
    private static final String cmCodeListKey = "code_lst";
    public List<CMCode> cmCodeList;

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.afficiateId != null)
            jo.put(afficiateIdKey, this.afficiateId);
        if(this.mediaName != null)
            jo.put(mediaNameKey, this.mediaName);
        if(this.mediaUrl != null)
            jo.put(mediaUrlKey, this.mediaUrl);
        if(this.flag != null)
            jo.put(flagKey, this.flag);
        if(cmCodeList != null){
            JSONArray arr = new JSONArray();
            for(int i = 0; i < this.cmCodeList.size(); i++){
                CMCode so = this.cmCodeList.get(i);
                arr.add(so.toJsonObject());
            }
            jo.put(cmCodeListKey, arr);
        }
        return jo;
    }

    public Media(String id, String afficateId, String mediaName, String mediaUrl, Integer flag) {
        this.id = id;
        this.afficiateId = afficateId;
        this.mediaName = mediaName;
        this.mediaUrl = mediaUrl;
        this.flag = flag;
    }
    
}
