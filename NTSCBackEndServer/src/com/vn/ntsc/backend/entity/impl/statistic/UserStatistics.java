/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.statistic;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class UserStatistics implements IEntity, Comparable<UserStatistics> {

    private static final String timeKey = "time";
    public String time;

    private static final String onlineUserIosKey = "onl_user_ios";
    public Integer onlineNumberIos;
    
    private static final String onlineFemaleUserIosKey = "onl_female_user_ios";
    public Integer onlineFemaleNumberIos;
    
    private static final String onlineMaleUserIosKey = "onl_male_user_ios";
    public Integer onlineMaleNumberIos;

    private static final String onlineUserAndroidKey = "onl_user_android";
    public Integer onlineNumberAndroid;
    
    private static final String onlineFemaleUserAndroidKey = "onl_female_user_android";
    public Integer onlineFemaleNumberAndroid;
    
    private static final String onlineMaleUserAndroidKey = "onl_male_user_android";
    public Integer onlineMaleNumberAndroid;

    private static final String newUserIosKey = "new_user_ios";
    public Integer newUserIosNumber;
    
    private static final String newFemaleUserIosKey = "new_female_user_ios";
    public Integer newFemaleUserIosNumber;
    
    private static final String newMaleUserIosKey = "new_male_user_ios";
    public Integer newMaleUserIosNumber;

    private static final String newUserAndroidKey = "new_user_android";
    public Integer newUserAndroidNumber;
    
    private static final String newFemaleUserAndroidKey = "new_female_user_android";
    public Integer newFemaleUserAndroidNumber;
    
    private static final String newMaleUserAndroidKey = "new_male_user_android";
    public Integer newMaleUserAndroidNumber;
    
    private static final String newUserWebKey = "new_user_web";
    public Integer newUserWebNumber;
    
    private static final String newFemaleUserWebKey = "new_female_user_web";
    public Integer newFemaleUserWebNumber;
    
    private static final String newMaleUserWebKey = "new_male_user_web";
    public Integer newMaleUserWebNumber;

    private static final String activeUserIosKey = "act_user_ios";
    public Integer activeUserIosNumber;

    private static final String activeUserAndroidKey = "act_user_android";
    public Integer activeUserAndroidNumber;

    private static final String totalUserKey = "total_user";
    public Integer totalUserNumber;
    
    private static final String videoCallKey = "video_call_number";
    public Integer videoCallNumber;
    private static final String voiceCallKey = "voice_call_number";
    public Integer voiceCallNumber;
    
    public InstallationStatistic installationStatistic;
      

    public UserStatistics(){
        time = null;
        this.onlineNumberIos = 0;
        this.onlineFemaleNumberIos = 0;
        this.onlineMaleNumberIos = 0;
        
        this.onlineNumberAndroid = 0;
        this.onlineFemaleNumberAndroid = 0;
        this.onlineMaleNumberAndroid = 0;
        
        this.newUserIosNumber = 0;
        this.newFemaleUserIosNumber = 0;
        this.newMaleUserIosNumber = 0;
        
        this.newUserAndroidNumber = 0;
        this.newFemaleUserAndroidNumber = 0;
        this.newMaleUserAndroidNumber = 0;
        
        this.newUserWebNumber = 0;
        this.newFemaleUserWebNumber = 0;
        this.newMaleUserWebNumber = 0;
        
        this.activeUserIosNumber = 0;
        this.activeUserAndroidNumber = 0;
        
        this.totalUserNumber = 0;
        
        this.videoCallNumber = 0;
        this.voiceCallNumber = 0;
        
        this.installationStatistic = new InstallationStatistic();
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        //
        if (this.onlineNumberIos != null) {
            jo.put(onlineUserIosKey, this.onlineNumberIos);
        }
        if (this.onlineFemaleNumberIos != null) {
            jo.put(onlineFemaleUserIosKey, this.onlineFemaleNumberIos);
        }
        if (this.onlineMaleNumberIos != null) {
            jo.put(onlineMaleUserIosKey, this.onlineMaleNumberIos);
        }
        //
        if (this.onlineNumberAndroid != null) {
            jo.put(onlineUserAndroidKey, this.onlineNumberAndroid);
        }
        if (this.onlineFemaleNumberAndroid != null) {
            jo.put(onlineFemaleUserAndroidKey, this.onlineFemaleNumberAndroid);
        }
        if (this.onlineMaleNumberAndroid != null) {
            jo.put(onlineMaleUserAndroidKey, this.onlineMaleNumberAndroid);
        }
        //
        if (this.newUserIosNumber != null) {
            jo.put(newUserIosKey, this.newUserIosNumber);
        }
        if (this.newFemaleUserIosNumber != null) {
            jo.put(newFemaleUserIosKey, this.newFemaleUserIosNumber);
        }
        if (this.newMaleUserIosNumber != null) {
            jo.put(newMaleUserIosKey, this.newMaleUserIosNumber);
        }
        //
        if (this.newUserAndroidNumber != null) {
            jo.put(newUserAndroidKey, this.newUserAndroidNumber);
        }
        if (this.newFemaleUserAndroidNumber != null) {
            jo.put(newFemaleUserAndroidKey, this.newFemaleUserAndroidNumber);
        }
        if (this.newMaleUserAndroidNumber != null) {
            jo.put(newMaleUserAndroidKey, this.newMaleUserAndroidNumber);
        }
        //
        if (this.newUserWebNumber != null) {
            jo.put(newUserWebKey, this.newUserWebNumber);
        }
        if (this.newFemaleUserWebNumber != null) {
            jo.put(newFemaleUserWebKey, this.newFemaleUserWebNumber);
        }
        if (this.newMaleUserWebNumber != null) {
            jo.put(newMaleUserWebKey, this.newMaleUserWebNumber);
        }
        //
        if (this.activeUserIosNumber != null) {
            jo.put(activeUserIosKey, this.activeUserIosNumber);
        }
        if (this.activeUserAndroidNumber != null) {
            jo.put(activeUserAndroidKey, this.activeUserAndroidNumber);
        }
        //
        if (this.totalUserNumber != null) {
            jo.put(totalUserKey, this.totalUserNumber);
        }
        
        if (this.videoCallNumber != null) {
            jo.put(videoCallKey, this.videoCallNumber);
        }
        if (this.voiceCallNumber != null) {
            jo.put(voiceCallKey, this.voiceCallNumber);
        }
        //
        if(this.installationStatistic != null){
            jo.putAll(this.installationStatistic.toJsonObject());
        }
        return jo;
    }

    @Override
    public int compareTo(UserStatistics o) {
        return this.time.compareTo(o.time);
    }

}
