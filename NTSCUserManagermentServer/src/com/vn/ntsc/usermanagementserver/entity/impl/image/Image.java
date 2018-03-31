/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.image;

import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author DuongLTD
 */
public class Image implements IEntity {

    public String userId;
    public Integer reportNumber;
    public String imageId;
    public Integer imageType;
    public Integer imageStatus;
    public Integer avatarFlag;
    public Integer appFlag;
    public Integer flag;
    public Long uploadTime;
    public Integer appearFlag;
    public Integer deniedFlag;
    public Integer reportFlag;
    public Long reportTime;
    public Integer gender;

    public Image(String userId, String imageId) {
        this.userId = userId;
        this.imageId = imageId;
    }

    public Image(String userId, String imageId, Integer imageType, Integer imageStatus, Integer avatarFlag, Integer appFlag, Integer flag, Long uploadTime, Integer appearFlag, Integer gender) {
        this.userId = userId;
        this.imageId = imageId;
        this.imageType = imageType;
        this.imageStatus = imageStatus;
        this.avatarFlag = avatarFlag;
        this.appFlag = appFlag;
        this.flag = flag;
        this.uploadTime = uploadTime;
        this.appearFlag = appearFlag;
        this.gender = gender;
    }

    public Image(String userId, String imageId, Integer imageType, Integer imageStatus, Integer avatarFlag, Integer appFlag,
            Integer flag, Long timeUpload, Integer appearFlag) {
        this.userId = userId;
        this.imageId = imageId;
        this.imageType = imageType;
        this.imageStatus = imageStatus;
        this.avatarFlag = avatarFlag;
        this.appFlag = appFlag;
        this.flag = flag;
        this.uploadTime = timeUpload;
        this.appearFlag = appearFlag;
    }

    public Image(String userId, String imageId, Integer imageType, Integer imageStatus, Integer avatarFlag, Integer appFlag,
            Integer flag, Long timeUpload, Integer appearFlag, Integer reportFlag, Long reportTime) {
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
    
    
    public JSONObject toJsonObject() {
        return null;
    }

}
