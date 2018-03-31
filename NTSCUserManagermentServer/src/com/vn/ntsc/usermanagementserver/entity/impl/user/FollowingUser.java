/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.user;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import eazycommon.util.DateFormat;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import static com.vn.ntsc.usermanagementserver.entity.impl.user.User.isAlertKey;

/**
 *
 * @author duyetpt
 */
public class FollowingUser implements IEntity {

    public Long fvtTime;
    public Long checkoutTime;
    public static final String USER_ID = "user_id";
    public String userId;
    public static final String AVATAR_ID = "ava_id";
    public String avatarId;
    public static final String USERNAME = "user_name";
    public String userName;
    public static final String GENDER = "gender";
    public Integer gender;
    public static final String AGE = "age";
    public Integer age;
    public static final String ABOUT = "about";
    String about;
    private static final String REGION = "region";
    public Integer region;
    public static final String JOB = "job";
    public Integer job;
    public static final String isAlertKey = "is_alt";
    public Integer isAlert;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (userId != null) {
            jo.put(USER_ID, userId);
        }
        if (avatarId != null) {
            jo.put(AVATAR_ID, avatarId);
        }
        if (userName != null) {
            jo.put(USERNAME, userName);
        }
        if (gender != null) {
            jo.put(GENDER, gender);
        }
        if (age != null) {
            jo.put(AGE, age);
        }
        if (about != null) {
            jo.put(ABOUT, about);
        }
        if (fvtTime != null) {
            jo.put("time", fvtTime);
        }
        if(checkoutTime != null){
            jo.put("check_out_time", DateFormat.format(checkoutTime));
        }
        if(region != null){
            jo.put(REGION, region);
        }
        if(job != null){
            jo.put(JOB, job);
        }
        if(isAlert != null){
            jo.put(isAlertKey, isAlert);
        }
        return jo;
    }

}
