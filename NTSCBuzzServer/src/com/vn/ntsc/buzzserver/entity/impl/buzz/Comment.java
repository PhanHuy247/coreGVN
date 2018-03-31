/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.entity.impl.buzz;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.entity.IEntity;

/**
 *
 * @author DuongLTD
 */
public class Comment implements  Comparable<Comment>, IEntity {
    private static final String isDelKey = "can_delete";
    public Integer isDel;

    private static final String cmtIdKey = "cmt_id";
    public String cmtId;

    private static final String userIdKey = "user_id";
    public String userId;

    private static final String cmtValKey = "cmt_val";
    public String cmtVal;

    private static final String cmtTimeKey = "cmt_time";
    public String cmtTime;
    
    private static final String subCommentNumberKey = "sub_comment_number";
    public Integer subCommentNumber;
    
    private static final String commentNumberKey = "comment_number";
    public Integer commentNumber;
    
    private static final String allSubCommentNumberKey = "all_sub_comment_number";
    public Integer allSubCommentNumber;

    private static final String subCommentKey = "sub_comment";
    public List<SubComment> subComment = new ArrayList<>();    
    
    public int flag;
    
    private static final String isAppKey = "is_app";
    public Integer isApp;
    
    private static final String buzzParIdKey = "buzz_parent_id";
    public String buzzParId;
    
    public static final String listUserCmtKey = "list_user_cmt";
    public List<String> listUserCmt = new ArrayList<>();
    

    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.isDel != null)
            jo.put(isDelKey, this.isDel);
        if(this.cmtId != null)
            jo.put(cmtIdKey, this.cmtId);
        if(this.userId != null)
            jo.put(userIdKey, this.userId);
        if(this.cmtVal != null)
            jo.put(cmtValKey, this.cmtVal);
        if(this.cmtTime != null)
            jo.put(cmtTimeKey, this.cmtTime);
        if(this.subCommentNumber != null)
            jo.put(subCommentNumberKey, this.subCommentNumber);
        if(this.allSubCommentNumber != null)
            jo.put(allSubCommentNumberKey, this.allSubCommentNumber);
        if(this.commentNumber != null)
            jo.put(commentNumberKey, this.commentNumber);
        if(this.isApp != null)
            jo.put(isAppKey, this.isApp);
        if(this.buzzParId != null)
            jo.put(buzzParIdKey, this.buzzParId);
        if (this.subComment != null) {
            JSONArray arr = new JSONArray();
            for (SubComment subC : this.subComment) {
                arr.add(subC.toJsonObject());
            }
            jo.put(subCommentKey, arr);
        } 
        if (this.listUserCmt != null) {
            JSONArray arr = new JSONArray();
            for (String userId : this.listUserCmt) {
                arr.add(userId);
            }
            jo.put(listUserCmtKey, arr);
        } 
        return jo;
    }

    @Override
    public int compareTo(Comment o) {
        return this.cmtTime.compareTo(o.cmtTime);
    }
}
