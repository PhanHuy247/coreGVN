/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.buzz;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author DuongLTD
 */
public class Comment implements Comparable<Comment>, IEntity {

    private static final String cmtIdKey = "cmt_id";
    public String cmtId;

    private static final String userIdKey = "user_id";
    public String userId;

    private static final String usernameKey = "user_name";
    public String userName;

    private static final String cmtValKey = "cmt_val";
    public String cmtVal;

    private static final String cmtTimeKey = "cmt_time";
    public String cmtTime;

    private static final String subCommentNumberKey = "sub_comment_number";
    public Integer subCommentNumber;
    
    private static final String subCommentKey = "sub_comment";
    public List<SubComment> subComment = new ArrayList<>();  
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.cmtId != null) {
            jo.put(cmtIdKey, this.cmtId);
        }
        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.userName != null) {
            jo.put(usernameKey, this.userName);
        }
        if (this.cmtVal != null) {
            jo.put(cmtValKey, this.cmtVal);
        }
        if (this.cmtTime != null) {
            jo.put(cmtTimeKey, this.cmtTime);
        }
        if(this.subCommentNumber != null)
            jo.put(subCommentNumberKey, this.subCommentNumber);
//        jo.put(subCommentNumberKey, 10);
        
        if (this.subComment != null) {
            JSONArray arr = new JSONArray();
            for (SubComment subC : this.subComment) {
                arr.add(subC.toJsonObject());
            }
            jo.put(subCommentKey, arr);
        } 
        return jo;
    }
    
    @Override
    public int compareTo(Comment o) {
        return this.cmtTime.compareTo(o.cmtTime);
    }
}
