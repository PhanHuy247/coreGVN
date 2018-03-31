/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.buzz;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author DuongLTD
 */
public class ReviewingBuzz implements Comparable<ReviewingBuzz>, IEntity {

    private static final String buzzIdKey = "buzz_id";
    public String buzzId;

    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String userNameKey = "user_name";
    public String userName;

    private static final String buzzValKey = "buzz_val";
    public String buzzVal;    

    private static final String buzzTimeKey = "buzz_time";
    public String buzzTime;

    public ReviewingBuzz() {
//        buzzId = "buzzId";
//        userId = "userId";
//        userName = "username";
//        buzzVal = "buzzValue";
//        buzzTime = "20151231101010";
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.buzzId != null) {
            jo.put(buzzIdKey, this.buzzId);
        }
        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (this.buzzVal != null) {
            jo.put(buzzValKey, this.buzzVal);
        }
        if (this.buzzTime != null) {
            jo.put(buzzTimeKey, this.buzzTime);
        }
        return jo;
    }
    
    @Override
    public int compareTo(ReviewingBuzz o) {
        return this.buzzTime.compareTo(o.buzzTime);
    }
}
