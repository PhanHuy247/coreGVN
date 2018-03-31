/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.datarespond;

import java.util.Collection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class AddSubCommentData implements IEntity {

    private static final String buzzIdKey = "buzz_id";
    public String buzzId;

    private static final String timeKey = "time";
    public Long time;

    private static final String cmtIdKey = "cmt_id";
    public String commentId;
    
    private static final String subCommentIdKey = "sub_comment_id";
    public String subCommentId;
    
    private static final String buzzOwnerKey = "buzz_owner_id";
    public String buzzOwner;
    
    private static final String commentOwnerKey = "comment_owner_id";
    public String commentOwner;
    
    private static final String isApprovedKey = "is_app";
    public Integer isApp;
    
    private static final String notificationListKey = "notification_list";
    public Collection<String> notificationList;
    
    private static final String cmtValueKey = "cmt_value";
    public String cmtValue;
    
    private static final String cmtUserIdKey = "cmt_user_id";
    public String cmtUserId;
    
    private static final String cmtTimeKey = "cmt_time";
    public String cmtTime;

    public AddSubCommentData(String buzzId, Long time, String commentId, String subCommentId, Collection<String> notificationList,
            String buzzOwner, String commentOwner, int isApp,String cmtValue, String cmtUserId, String cmtTime) {
        this.buzzId = buzzId;
        this.time = time;
        this.commentId = commentId;
        this.subCommentId = subCommentId;
        this.notificationList = notificationList;
        this.buzzOwner = buzzOwner;
        this.commentOwner = commentOwner;
        this.isApp = isApp;
        this.cmtValue = cmtValue;
        this.cmtUserId = cmtUserId;
        this.cmtTime = cmtTime;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (buzzId != null) {
            jo.put(buzzIdKey, buzzId);
        }
        if (commentId != null) {
            jo.put(cmtIdKey, commentId);
        }
        if (time != null) {
            jo.put(timeKey, time);
        }
        if (commentOwner != null) {
            jo.put(commentOwnerKey, commentOwner);
        }
        if (buzzOwner != null) {
            jo.put(buzzOwnerKey, buzzOwner);
        }
        if (subCommentId != null) {
            jo.put(subCommentIdKey, subCommentId);
        }
        if (isApp != null) {
            jo.put(isApprovedKey, isApp);
        }
        if (cmtValue != null) {
            jo.put(cmtValueKey, cmtValue);
        }
        if (cmtUserId != null) {
            jo.put(cmtUserIdKey, cmtUserId);
        }
        if (cmtTime != null) {
            jo.put(cmtTimeKey, cmtTime);
        }
        if (notificationList != null) {
            JSONArray arr = new JSONArray();
            for(String  str : notificationList){
                arr.add(str);
            }
            jo.put(notificationListKey, arr);
        }
        return jo;
    }

}
