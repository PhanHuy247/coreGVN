/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.news;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author HuyDX
 */
public class News implements IEntity{
    
    private static final String idKey = "id";
    private String id;
    private static final String titleKey = "title";
    private String title;
    private static final String updateDateKey = "update_date";
    private Long updateDate;
    private static final String updateDateStrKey = "update_dateStr";
    private String updateDateStr;
    private static final String bannerIdKey = "banner_id";
    private String bannerId;
    private static final String contentKey = "content";
    private String content;
    private static final String isShownKey = "is_shown";
    private Boolean isShown;
    private static final String fromKey = "from";
    private Long from;
    private static final String fromStrKey = "fromStr";
    private String fromStr;    
    private static final String toKey = "to";
    private Long to;
    private static final String toStrKey = "toStr";
    private String toStr;    
    private static final String device_typeKey = "device_type";
    private Integer device_type;
    private static final String targetGenderKey = "target_gender";
    private Integer targetGender;
    private static final String statusKey = "status";
    private Integer status;
    private static final String registerFromKey = "register_from";
    private Long registerFrom;
    private static final String registerToKey = "register_to";
    private Long registerTo;
    private static final String registerFromStrKey = "register_from";
    private String registerFromStr;
    private static final String registerToStrKey = "register_to";
    private String registerToStr;
    private static final String registerTypeKey = "register_type";
    private Long registerType;
    private static final String userTypeKey = "user_type";
    private Long userType;
    // LongLT 19Sep2016 /////////////////////////// START #4597
    private static final String isPurchasedKey = "is_purchase";
    private static final String haveEmailKey = "have_email";
    
    private Boolean haveEmail;
    private Boolean isPurchased;

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
    // LongLT 19Sep2016 /////////////////////////// END #4597

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        
        if (this.id != null)
            jo.put(idKey, this.id);
        // LongLT 19Sep2016 /////////////////////////// START #4597
        if (this.isPurchased != null)
            jo.put(isPurchasedKey, this.isPurchased);
        // LongLT 19Sep2016 /////////////////////////// END #4597
        if (this.haveEmail != null)
            jo.put(haveEmailKey, this.haveEmail);
        if (this.title != null)
            jo.put(titleKey, this.title);
        if (this.updateDate != null)
            jo.put(updateDateKey, this.updateDate);        
        if (this.updateDateStr != null)
            jo.put(updateDateStrKey, this.updateDateStr);      
        if (this.bannerId != null)
            jo.put(bannerIdKey, this.bannerId);
        if (this.content != null)
            jo.put(contentKey, this.content );
        if (this.isShown != null)
            jo.put(isShownKey, this.isShown);
        if (this.from != null)
            jo.put(fromKey, this.from);        
        if (this.fromStr != null)
            jo.put(fromStrKey, this.fromStr);            
        if (this.to != null)
            jo.put(toKey, this.to);
        if (this.toStr != null)
            jo.put(toStrKey, this.toStr);
        if (this.device_type != null)
            jo.put(device_typeKey, this.device_type );        
        if (this.targetGender != null)
            jo.put(targetGenderKey, this.targetGender);
        if (this.status!=null)
            jo.put(statusKey, this.status);
        //Linh 10/5/2016
        if (this.registerFrom != null)
            jo.put(registerFromKey, registerFrom);
        if (this.registerTo != null)
            jo.put(registerToKey, registerTo);
        if (this.registerFromStr != null)
            jo.put(registerFromStrKey, registerFromStr);
        if (this.registerToStr != null)
            jo.put(registerToStrKey, registerToStr);
        if (this.registerType != null)
            jo.put(registerTypeKey, registerType);
        if (this.userType != null)
            jo.put(this.userTypeKey, this.userType);
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

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
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

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
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
    
    public String getFromStr(){
        return fromStr;
    }
    
    public void setFromStr(String fromStr){
        this.fromStr = fromStr;
    }
    
    public String getToStr(){
        return toStr;
    }
    
    public void setToStr(String toStr){
        this.toStr = toStr;
    }
    
    //Linh
    public Long getRegisterFrom() {
        return registerFrom;
    }

    public void setRegisterFrom(Long registerFrom) {
        this.registerFrom = registerFrom;
    }

    public Long getRegisterTo() {
        return registerTo;
    }

    public void setRegisterTo(Long registerTo) {
        this.registerTo = registerTo;
    }

    public String getRegisterFromStr() {
        return registerFromStr;
    }

    public void setRegisterFromStr(String registerFromStr) {
        this.registerFromStr = registerFromStr;
    }

    public String getRegisterToStr() {
        return registerToStr;
    }

    public void setRegisterToStr(String registerToStr) {
        this.registerToStr = registerToStr;
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

