/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.buzz;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author DuongLTD
 */
public class SubComment implements  Comparable<SubComment>, IEntity {

    private static final String usernameKey = "user_name";
    public String userName;
    
    private static final String subCommentIdKey = "sub_comment_id";
    public String subCommentId;

    private static final String userIdKey = "user_id";
    public String userId;

    private static final String valueKey = "value";
    public String value;

    private static final String timeKey = "time";
    public String time;
    
    public String commentId;
    
    public Integer flag;

    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
        if(this.userName != null)
            jo.put(usernameKey, this.userName);
        if(this.subCommentId != null)
            jo.put(subCommentIdKey, this.subCommentId);
        if(this.userId != null)
            jo.put(userIdKey, this.userId);
        if(this.value != null)
            jo.put(valueKey, this.value);
        if(this.time != null)
            jo.put(timeKey, this.time);
        
        return jo;
    }

    @Override
    public int compareTo(SubComment o) {
        return this.time.compareTo(o.time);
    }
}
