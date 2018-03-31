/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.user;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author DuongLTD
 */
public class ReviewingUser implements IEntity {

    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String userNameKey = "user_name";
    public String userName;

    private static final String genderKey = "gender";
    public Integer gender;    

    private static final String aboutKey = "about";
    public String about;
    
    private static final String hobbyKey = "hobby";
    public String hobby;
    
    private static final String typeOfManKey = "type_of_man";
    public String typeOfMan;
    
    private static final String fetishKey = "fetish";
    public String fetish;
    
    private static final String timeKey = "time";
    public String time;

    public ReviewingUser() {
//        userId = "userId";
//        userName = "username";
//        int i = new Random().nextInt();
//        gender = i%2;
//        if(gender == Constant.FEMALE){
//            about = "about";
//            hobby = "hobby";
//        }else{
//            about = "about";
//            typeOfMan = "typeOfMan";
//            fetish = "fetish";
//        }
//        time = "20151231101010";
    }
    
    public void setField(String field, String value){
        switch (field) {
            case aboutKey:
                this.about = value;
                break;
            case typeOfManKey:
                this.typeOfMan = value;
                break;
            case hobbyKey:
                this.hobby = value;
                break;
            case fetishKey:
                this.fetish = value;
                break;
        }
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.gender != null) {
            jo.put(genderKey, this.gender);
        }
        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (this.about != null) {
            jo.put(aboutKey, this.about);
        }
        if (this.hobby != null) {
            jo.put(hobbyKey, this.hobby);
        }
        if (this.typeOfMan != null) {
            jo.put(typeOfManKey, this.typeOfMan);
        }
        if (this.fetish != null) {
            jo.put(fetishKey, this.fetish);
        }
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        return jo;
    }
    
}
