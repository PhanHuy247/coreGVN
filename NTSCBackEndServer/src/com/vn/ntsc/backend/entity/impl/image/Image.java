/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.image;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author DuongLTD
 */
public class Image implements IEntity {

    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String userNameKey = "user_name";
    public String username;

    private static final String reportNumberKey = "report_number";
    public Integer reportNumber;    
    
    private static final String imageIdKey = "img_id";    
    public String imageId;

    private static final String imageTypeKey = "img_type";    
    public Integer imageType;

    private static final String imageStatusKey = "img_stt";    
    public Integer imageStatus;
    
    private static final String avatarFlagKey = "ava_flag";    
    public Integer avatarFlag;
    
    private static final String appFlagKey = "app_flag";    
    public Integer appFlag;
    
    public Integer deniedFlag;
 
    private static final String flagKey = "flag";    
    public Integer flag;
   
    public Long uploadTime;

    private static final String uploadTimeKey = "upl_time"; 
    public String uploadTimeStr;
      
    public Long reviewTime;
    
    private static final String reviewTimeKey = "review_time";  
    public String reviewTimeStr;
    
    private static final String appearFlagKey = "appear_flag"; 
    public Integer appearFlag;
    
    private static final String reportFlagKey = "report_flag";
    public Integer reportFlag;
    
    public Long reportTime;
    
    private static final String reportTimeKey = "report_time";  
    public String reportTimeStr;
    //thanhdd add 14/2/2017
    private static final String genderKey = "gender";
    public Long gender;
    // namhv #8001 
    private static final String userDenyKey = "user_deny";
    public String userDeny;
    private static final String userDenyNameKey = "user_deny_name";
    public String userDenyName;
    
    private static final String typeKey = "type";    
    public Long type;

    public Image(String userId, String imageId, Integer imageType, Integer imageStatus, Integer avatarFlag, Integer appFlag, Integer flag, Long timeUpload, Long reviewTime) {
        this.userId = userId;
        this.imageId = imageId;
        this.imageType = imageType;
        this.imageStatus = imageStatus;
        this.avatarFlag = avatarFlag;
        this.appFlag = appFlag;
        this.flag = flag;
        this.uploadTime = timeUpload;
        this.reviewTime = reviewTime;
    }

    public Image(String userId, String imageId, Integer imageType, Integer imageStatus, Integer avatarFlag, Integer flag,
            Long timeUpload, Integer appearFlag, Integer reportFlag, Long reportTime) {
        this.userId = userId;
        this.imageId = imageId;
        this.imageType = imageType;
        this.imageStatus = imageStatus;
        this.avatarFlag = avatarFlag;
        this.appFlag = appFlag;
        this.flag = flag;
        this.uploadTime = timeUpload;
        this.appearFlag = appearFlag;
        this.reportFlag = reportFlag;
        this.reportTime = reportTime;
    }    
    
    public Image() {
    }

    
    @Override
    public JSONObject toJsonObject() {
        
        JSONObject jo = new JSONObject();
        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.imageId != null) {
            jo.put(imageIdKey, this.imageId);
        }
        if (username != null) {
            jo.put(userNameKey, this.username);
        }
        if(this.imageType != null)
            jo.put(imageTypeKey, this.imageType);

        if(this.uploadTimeStr != null)
            jo.put(uploadTimeKey, this.uploadTimeStr);

        if(this.reviewTimeStr != null)
            jo.put(reviewTimeKey, this.reviewTimeStr);
        
        if(this.imageStatus != null)
            jo.put(imageStatusKey, this.imageStatus);
        
        if(reportNumber!= null)
            jo.put(reportNumberKey, this.reportNumber);
        
        if(appearFlag!= null)
            jo.put(appearFlagKey, this.appearFlag);
        
        if(reportFlag!= null)
            jo.put(reportFlagKey, this.reportFlag);
        
        if(reportTimeStr!= null)
            jo.put(reportTimeKey, this.reportTimeStr);
        
        if(gender!= null)
            jo.put(genderKey, this.gender);
        // namhv #8001
        if(userDeny != null && !"".equals(userDeny)){
            jo.put(userDenyKey, this.userDeny);
        }
            
        if(userDenyName != null){
            jo.put(userDenyNameKey, this.userDenyName);
        }
        
        if(type != null){
            jo.put(typeKey, this.type);
        }
        
        return jo;
    }

}
