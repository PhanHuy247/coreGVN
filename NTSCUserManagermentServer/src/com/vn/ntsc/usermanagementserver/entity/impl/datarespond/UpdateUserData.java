/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;

/**
 *
 * @author RuAc0n
 */
public class UpdateUserData implements IEntity {

    public Boolean isVerify;    
    public Boolean isRemoveSession;    
    
    public Integer finishRegister;
    public User user;

    private static final String addPointKey = "add_point";
    public Integer addPoint;  
    
    private static final String isReviewedKey = "is_reviewed";
    public Boolean isReviewed;
    
    private static final String isNotiKey = "is_noti";
    public Integer isNoti;
    
    public UpdateUserData(User user) {
        this.user = user;
    }

    public UpdateUserData(int removeSession, User user, Integer isNoti) {
        this.finishRegister = removeSession;
        this.user = user;
        this.isNoti = isNoti;
    }
    
    public UpdateUserData(int removeSession, Boolean isVerify, User user) {
        this.finishRegister = removeSession;
        this.user = user;
        this.isVerify = isVerify;
    }
    public UpdateUserData(int removeSession, Boolean isVerify, boolean isRemoveSession, User user) {
        this.finishRegister = removeSession;
        this.user = user;
        this.isRemoveSession = isRemoveSession;
        this.isVerify = isVerify;
    }
    public UpdateUserData(int removeSession) {
        this.finishRegister = removeSession;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject json = user.toJsonObject();
        if (finishRegister != null) {
            json.put(ParamKey.IS_FINISH_REGISTER, this.finishRegister);
        } 
        if(isVerify != null)
            json.put(ParamKey.IS_VERIFY, isVerify);
        if(isRemoveSession != null)
            json.put(ParamKey.IS_REMOVE_SESSION, isRemoveSession);
        if (this.addPoint != null) {
            json.put(addPointKey, addPoint);
        }
        if (this.isReviewed != null) {
            json.put(isReviewedKey, isReviewed);
        }
        if (this.isNoti != null) {
            json.put(isNotiKey, isNoti);
        }
        return json;
    }

}
