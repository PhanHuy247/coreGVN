/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

public class ReviewUserData implements IEntity {
    
    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String reviewResultKey = "review_result";
    public Integer reviewResult;

    public ReviewUserData(String userId, Integer reviewResult) {
        this.userId = userId;
        this.reviewResult = reviewResult;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.reviewResult != null) {
            jo.put(reviewResultKey, this.reviewResult);
        }
        return jo;
    }
}
