/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.cmcode;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class CMCode implements IEntity{
    private static final String idKey = "cm_code_id";
    public String id;

    private static final String afficiateIdKey = "aff_id";
    public String afficiateId;

    private static final String afficiateNameKey = "aff_name";
    public String affName;    
    
    private static final String mediaIdKey = "media_id";
    public String  mediaId;

    private static final String mediaNameKey = "media_name";
    public String  mediaName;    
    
    private static final String cmcodeKey = "cm_code";
    public String cmCode;
    
    private static final String moneyKey = "money";
    public Double money;    

    private static final String typeKey = "type";
    public Integer type;    
    
    private static final String redirectUrlKey = "redirect_url";
    public String redirectUrl;
    
    private static final String regUrlKey = "reg_url";
    public String regUrl;
    
    private static final String saleUrlKey = "pur_url";
    public String saleUrl;
    
    private static final String desKey = "des";
    public String des;
    
    private static final String flagKey = "flag";
    public Integer flag;

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.mediaId != null)
            jo.put(mediaIdKey, this.mediaId);
        if(this.mediaName != null)
            jo.put(mediaNameKey, this.mediaName);        
        if(this.afficiateId != null)
            jo.put(afficiateIdKey, this.afficiateId);
        if(this.affName != null)
            jo.put(afficiateNameKey, this.affName);        
        if(this.cmCode != null)
            jo.put(cmcodeKey, this.cmCode);
        if(this.money != null)
            jo.put(moneyKey, this.money);
        if(this.type != null)
            jo.put(typeKey, this.type);
        if(this.redirectUrl != null)
            jo.put(redirectUrlKey, this.redirectUrl);
        if(this.regUrl != null)
            jo.put(regUrlKey, this.regUrl);
        if(this.saleUrl != null)
            jo.put(saleUrlKey, this.saleUrl);
        if(this.des != null)
            jo.put(desKey, this.des);        
        if(this.flag != null)
            jo.put(flagKey, this.flag);
        return jo;
    }    

    public CMCode(String id, String afficiateId, String mediaId, String cmCode, 
            Double money, Integer type, String regUrl, String saleUrl, String redirectUrl, String des, Integer flag) {
        this.id = id;
        this.afficiateId = afficiateId;
        this.mediaId = mediaId;
        this.cmCode = cmCode;
        this.money = money;
        this.type = type;
        this.redirectUrl = redirectUrl;
        this.regUrl = regUrl;
        this.saleUrl = saleUrl;
        this.des = des;
        this.flag = flag;
    }

    public CMCode(String id, String afficiateId, String mediaId, String cmCode, Double money, Integer type, Integer flag) {
        this.id = id;
        this.afficiateId = afficiateId;
        this.mediaId = mediaId;
        this.cmCode = cmCode;
        this.money = money;
        this.type = type;
        this.flag = flag;
    }

    public CMCode() {
    }
    
}
