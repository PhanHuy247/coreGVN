/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;

/**
 *
 * @author HuyDX
 */
public class News implements IEntity{
    
    private static final String idKey = "news_id";
    private String id;
    private static final String titleKey = "title";
    private String title;
    private static final String updateDateKey = "update_date";
    private String updateDate;
    private static final String bannerIdKey = "banner_id";
    private String bannerId;
    private static final String contentKey = "content";
    private String content;
    private static final String isShownKey = "is_shown";
    private Boolean isShown;
    private static final String fromKey = "from";
    private String from;
    private static final String toKey = "to";
    private String to;
    private static final String device_typeKey = "device_type";
    private Integer device_type;
    private static final String targetGenderKey = "target_gender";
    private Integer targetGender;
    private static final String statusKey = "status";
    private Integer status;
    //Linh 10/17/2016 #4959
    private static final String isPurchasedKey = "is_purchase";
    private Boolean isPurchased;
    private static final String haveEmailKey = "have_email";
    private Boolean haveEmail;
    
    private static final String registerFromKey = "register_from";
    private String registerFrom;
    private static final String registerToKey = "register_to";
    private String registerTo;
    private static final String registerTypeKey = "register_type";
    private Long registerType;
    private static final String userTypeKey = "user_type";
    private Long userType;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        
        if (this.id != null)
            jo.put(idKey, this.id);
        if (this.title != null)
            jo.put(titleKey, this.title);
        if (this.updateDate != null)
            jo.put(updateDateKey, this.updateDate);        
        if (this.bannerId != null)
            jo.put(bannerIdKey, this.bannerId);
        if (this.content != null)
            jo.put(contentKey, this.content );
        if (this.isShown != null)
            jo.put(isShownKey, this.isShown);
        if (this.from != null)
            jo.put(fromKey, this.from);        
        if (this.to != null)
            jo.put(toKey, this.to);
        if (this.device_type != null)
            jo.put(device_typeKey, this.device_type );        
        if (this.targetGender != null)
            jo.put(targetGenderKey, this.targetGender);
        if (this.status!=null)
            jo.put(statusKey, this.status);
        if (this.isPurchased != null)
            jo.put(isPurchasedKey, this.isPurchased);
        // LongLT 19Sep2016 /////////////////////////// END #4597
        if (this.haveEmail != null)
            jo.put(haveEmailKey, this.haveEmail);
        // Linh 10/17/2016 #4959
        if (this.registerType != null){
            jo.put(registerTypeKey, this.registerTypeKey);
        }
        if (this.registerFrom != null){
            jo.put(registerFromKey, this.registerFrom);
        }
        if (this.registerTo != null){
            jo.put(registerToKey, this.registerTo);
        }
        if (this.userType != null){
            jo.put(userTypeKey, userType);
        }
        return jo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsShown() {
        return isShown;
    }

    public void setIsShown(Boolean isShown) {
        this.isShown = isShown;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getDevice_type() {
        return device_type;
    }

    public void setDevice_type(Integer device_type) {
        this.device_type = device_type;
    }

    public Integer getTargetGender() {
        return targetGender;
    }

    public void setTargetGender(Integer targetGender) {
        this.targetGender = targetGender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getHaveEmail() {
        return haveEmail;
    }

    public void setHaveEmail(Boolean haveEmail) {
        this.haveEmail = haveEmail;
    }

    public Boolean getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(Boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public String getRegisterFrom() {
        return registerFrom;
    }

    public void setRegisterFrom(String registerFrom) {
        this.registerFrom = registerFrom;
    }

    public String getRegisterTo() {
        return registerTo;
    }

    public void setRegisterTo(String registerTo) {
        this.registerTo = registerTo;
    }

    public Long getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Long registerType) {
        this.registerType = registerType;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    
 
}
