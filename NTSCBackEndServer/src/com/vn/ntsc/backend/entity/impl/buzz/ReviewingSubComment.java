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
public class ReviewingSubComment implements Comparable<ReviewingSubComment>, IEntity {

    private static final String buzzIdKey = "buzz_id";
    public String buzzId;
    
    private static final String subCommentIdKey = "sub_comment_id";
    public String subCommentId;

    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String userNameKey = "user_name";
    public String userName;

    private static final String commentIdKey = "comment_id";
    public String commentId;    
    
    private static final String valueKey = "value";
    public String value;    

    private static final String timeKey = "time";
    public String time;

    public ReviewingSubComment() {
//        buzzId = "buzzId";
//        userId = "userId";
//        userName = "username";
//        commentId = "commentId";
//        subCommentId = "subcommentId";
//        value = "buzzValue";
//        time = "20151231101010";
    }      
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.buzzId != null) {
            jo.put(buzzIdKey, this.buzzId);
        }
        if (this.subCommentId != null) {
            jo.put(subCommentIdKey, this.subCommentId);
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
        if (this.value != null) {
            jo.put(valueKey, this.value);
        }
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        return jo;
    }
    
    @Override
    public int compareTo(ReviewingSubComment o) {
        return this.time.compareTo(o.time);
    }
}
