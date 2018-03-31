/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.buzz;

import com.vn.ntsc.buzzserver.entity.IEntity;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class PrioritizeUserBuzzSetting implements IEntity{

    private static final String listBuzzIdKey = "list_buzz_id";
    public List<String> listBuzzId;
    
    private static final String listUserIdKey = "list_user_id";
    public List<String> listUserId;
    
    private static final String takeBuzzKey = "take_buzz";
    public Integer takeBuzz;
    
    private static final String skipBuzzKey = "skip_buzz";
    public Integer skipBuzz;

    public PrioritizeUserBuzzSetting(List<String> listBuzzId, List<String> listUserId, Integer takeBuzz, Integer skipBuzz) {
        this.listBuzzId = listBuzzId;
        this.listUserId = listUserId;
        this.takeBuzz = takeBuzz;
        this.skipBuzz = skipBuzz;
    }

    public PrioritizeUserBuzzSetting() {
    }
    
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(listBuzzId != null)
            jo.put(listBuzzIdKey, this.listBuzzId);
        if(listUserId != null)
            jo.put(listUserIdKey, this.listUserId);
        if(takeBuzz != null)
            jo.put(takeBuzzKey, this.takeBuzz);
        if(skipBuzz != null)
            jo.put(skipBuzzKey, this.skipBuzz);
        return jo;
    }
    
}
