/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dbentity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;

/**
 *
 * @author DuongLTD
 */
public class ImageDB {

    private static final String userIdKey = "user_id";
    private String userId;

    private static final String imageIdKey = "img_id";
    private String imageId;

    private static final String imageTypeKey = "img_type";
    private Integer imageType;    
 
    private static final String imageStatusKey = "status";
    private Integer imageStatus;    
    
    private static final String avatarFlagKey = "ava_flag";
    private Integer avatarFlag;
    
    private static final String appFlagKey = "app_flag";
    private Integer appFlag;
    
    private static final String flagKey = "flag";
    private Integer flag;    
    
    private static final String uploadTimeKey = "upload_time";
    private Long uploadTime;

    private static final String appearFlagKey = "appear_flag";
    public Integer appearFlag;
    
    private static final String deniedFlagKey = "denied_flag";
    public Integer deniedFlag;
    
    private static final String genderKey = "gender";
    public Integer gender;
    
//    private static final String reportFlagKey = "report_flag";
    public Integer reportFlag;
    
//    private static final String reportTimeKey = "report_time";
    public Long reportTime;    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String id) {
        this.userId = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public static ImageDB fromImageEntity(Image entity) {
        ImageDB result = new ImageDB();
        result.setImageId(entity.imageId);
        result.setFlag(entity.flag);
        result.setUserId(entity.userId);
        result.setImageType(entity.imageType);
        result.setImageStatus(entity.imageStatus);
        result.setAvatarFlag(entity.avatarFlag);
        result.setAppFlag(entity.appFlag);
        result.setUploadTime(entity.uploadTime);
        result.reportFlag = (entity.reportFlag);
        result.reportTime = (entity.reportTime);
        result.appearFlag = (entity.appearFlag);
        result.deniedFlag = (entity.deniedFlag);
        result.gender = entity.gender;
        return result;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }


    public DBObject toDBObject() {
        DBObject dbObject = new BasicDBObject();
        dbObject.put(userIdKey, this.userId);
        dbObject.put(imageIdKey, this.imageId);
        dbObject.put(flagKey, this.flag);
        dbObject.put(imageTypeKey, this.imageType);
        dbObject.put(imageStatusKey, this.imageStatus);
        dbObject.put(avatarFlagKey, this.avatarFlag);
        dbObject.put(appFlagKey, this.appFlag);
        dbObject.put(deniedFlagKey, this.deniedFlag);
        dbObject.put(uploadTimeKey, this.uploadTime);
        dbObject.put (appearFlagKey, this.appearFlag);
        dbObject.put (genderKey, this.gender);
//        dbObject.put()
        return dbObject;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Integer getImageType() {
        return imageType;
    }

    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    public Integer getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(Integer imageStatus) {
        this.imageStatus = imageStatus;
    }

    public Integer getAvatarFlag() {
        return avatarFlag;
    }

    public void setAvatarFlag(Integer avatarFlag) {
        this.avatarFlag = avatarFlag;
    }
    public Integer getAppFlag() {
        return appFlag;
    }

    public void setAppFlag(Integer appFlag) {
        this.appFlag = appFlag;
    }
}
