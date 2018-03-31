/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.datarespond;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 * @param <T>
 */
public class UserOnlineData<T> implements IEntity {

    private static final String totalKey = "total";
    public Integer total;
    
    private static final String femaleKey = "female";
    public Integer female;
    
    private static final String maleKey = "male";
    public Integer male;
    
    private static final String voiceCallKey = "voice_call";
    public Integer voiceCall;
    
    private static final String videoCallKey = "video_call";
    public Integer videoCall;

    private static final String llKey = "list";
    public List<T> ll;

    public UserOnlineData() {
        this.total = 0;
        this.ll = new ArrayList<>();
    }
        
    public UserOnlineData(Integer total, Integer female, Integer male, Integer voiceCall, Integer videoCall, List<T> ll) {
        this.total = total;
        this.female = female;
        this.male = male;
        this.voiceCall = voiceCall; 
        this.videoCall = videoCall;
        this.ll = ll;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.total != null) {
            jo.put(totalKey, this.total);
//            jo.put(totalKey, 1000000);
        }
        if (this.female != null) {
            jo.put(femaleKey, this.female);
//            jo.put(femaleKey, 500000);
        }
        if (this.male != null) {
            jo.put(maleKey, this.male);
//            jo.put(maleKey, 500000);
        }
        if (this.videoCall != null) {
            jo.put(videoCallKey, this.videoCall);
//            jo.put(videoCallKey, 1000000);
        }
        if (this.voiceCall != null) {
            jo.put(voiceCallKey, this.voiceCall);
//            jo.put(voiceCallKey, 2000000);
        }
        if (this.ll != null) {
            JSONArray arr = new JSONArray();
            for (T entity : this.ll) {
                arr.add(((IEntity) entity).toJsonObject());
            }
            jo.put(llKey, arr);
        }
        return jo;
    }
}
