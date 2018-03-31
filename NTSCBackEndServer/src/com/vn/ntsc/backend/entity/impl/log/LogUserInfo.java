/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.log;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author Administrator
 */
public class LogUserInfo implements IEntity{
    
    public static final String userIdKey = "user_id";
    public String userId;
    
    public static final String userNameKey = "user_name";
    public String userName;
    
    public static final String appIdKey = "application_id";
    public Integer appId;
    
    public static final String userTypeKey = "user_type";
    public Integer userType;
    
    public static final String genderKey = "gender";
    public Integer gender;
    
    public static final String timeKey = "time";
    public Long time;
    public String timeStr;
    
    public static final String aboutKey = "about";
    public String about;
    
    public static final String aboutFlagKey = "about_flag";
    public Integer aboutFlag;
    
    public static final String fetishKey = "fetish";
    public String fetish;
    
    public static final String fetishFlagKey = "fetish_flag";
    public Integer fetishFlag;
    
    public static final String hobbyKey = "hobby";
    public String hobby;
    
    public static final String hobbyFlagKey = "hobby_flag";
    public Integer hobbyFlag;
    
    public static final String typeOfManKey = "type_of_man";
    public String typeOfMan;
    
    public static final String typeOfManFlagKey = "type_of_man_flag";
    public Integer typeOfManFlag;
    
    public static final String userDenyKey = "user_deny";
    public String user_deny;
    
    public static final String userDenyNameKey = "user_deny_name";
    public String user_deny_name;
    
    public static final String appFlagKey = "app_flag";
    public Integer app_flag;
    

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (userId != null){
            jo.put(userIdKey, userId);
        }
        if (userName != null){
            jo.put(userNameKey, userName);
        }
        if (appId != null){
            jo.put(appIdKey, appId);
        }
        if (userType != null){
            jo.put(userTypeKey, userType);
        }
        if (gender != null){
            jo.put(genderKey, gender);
        }
        if (time != null){
            jo.put(timeKey, timeStr);
        }
        if (aboutFlag != null){
            jo.put(aboutFlagKey, aboutFlag);
        }
        if (about != null){
            jo.put(aboutKey, about);
        }
        if (fetishFlag != null){
            jo.put(fetishFlagKey, fetishFlag);
        }
        if (fetish != null){
            jo.put(fetishKey, fetish);
        }
        if (typeOfManFlag != null){
            jo.put(typeOfManFlagKey, typeOfManFlag);
        }
        if (typeOfMan != null){
            jo.put(typeOfManKey, typeOfMan);
        }
        if (hobbyFlag != null){
            jo.put(hobbyFlagKey, hobbyFlag);
        }
        if (hobby != null){
            jo.put(hobbyKey, hobby);
        }
        if (user_deny != null){
            jo.put(userDenyKey, user_deny);
        }
        
        if (user_deny_name != null){
            jo.put(userDenyNameKey, user_deny_name);
        }
        
        if (app_flag != null){
            jo.put(appFlagKey, app_flag);
        }
        
        return jo;
    }
    
}
