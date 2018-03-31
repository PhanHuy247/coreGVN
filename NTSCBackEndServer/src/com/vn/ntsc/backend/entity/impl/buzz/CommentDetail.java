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
 * @author Namhv
 */
public class CommentDetail implements Comparable<CommentDetail>, IEntity{
    private static final String buzzIdKey = "buzz_id";
    public String buzzId;

    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String userNameKey = "user_name";
    public String userName;

    private static final String commentIdKey = "comment_id";
    public String commentId;    
    
    private static final String commentValueKey = "comment_value";
    public String commentValue;    

    private static final String commentTimeKey = "comment_time";
    public String commentTime;  
    
    private static final String commentFlagKey = "cmt_flag";
    public Integer commentFlag;

    private static final String approveFlagKey = "approve_flag";
    public Integer approveFlag;
    
    private static final String isDelKey = "is_deleted";
    public Integer isDel;
    
    private static final String commentStatus ="comment_status";
    public Integer isCommentStatus;
    
    private static final String userDeny ="user_deny";
    public String isUserDeny;
    
    private static final String userDenyNameKey ="user_deny_name";
    public String isUserDenyName;
    
    @Override
    public int compareTo(CommentDetail o) {
        return this.commentTime.compareTo(o.commentTime);
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
        if (this.commentId != null) {
            jo.put(commentIdKey, this.commentId);
        }
        if (this.commentValue != null) {
            jo.put(commentValueKey, this.commentValue);
        }
        if (this.commentTime != null) {
            jo.put(commentTimeKey, this.commentTime);
        }
        if (this.isDel != null) {
            jo.put(isDelKey,this.isDel);
        }
        if (this.isCommentStatus != null) {
            jo.put(commentStatus,this.isCommentStatus);
        }
        if (this.isUserDeny != null) {
            jo.put(userDeny,this.isUserDeny);
        }
        if (this.isUserDenyName != null) {
            jo.put(userDenyNameKey,this.isUserDenyName);
        }
        if (this.approveFlag != null) {
            jo.put(approveFlagKey,this.approveFlag);
        }
        return jo;
    }

}
