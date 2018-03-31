/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.entity.impl.buzz;

import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.entity.IEntity;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;

/**
 *
 * @author DuongLTD
 */
public class SubComment implements  Comparable<SubComment>, IEntity {


    private static final String subCommentIdKey = "sub_comment_id";
    public String subCommentId;

    private static final String userIdKey = "user_id";
    public String userId;

    private static final String valueKey = "value";
    public String value;

    private static final String timeKey = "time";
    public String time;
    
    private static final String canDeleteKey = "can_delete";
    public Integer canDelete = 0;
    
    public String buzzId;
    
    public Integer flag;
    
    private static final String isAppKey = "is_app";
    public Integer isApp;
    
    private static final String commentNumberKey = "comment_number";
    public Integer commentNumber;
    
    private static final String allSubCommentNumberKey = "all_sub_comment_number";
    public Integer allSubCommentNumber;
    
    private static final String buzzParIdKey = "buzz_parent_id";
    public String buzzParId;
    
    public static final String listUserCmtKey = "list_user_cmt";
    public List<String> listUserCmt = new ArrayList<>();
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
        if(this.subCommentId != null)
            jo.put(subCommentIdKey, this.subCommentId);
        if(this.userId != null)
            jo.put(userIdKey, this.userId);
        if(this.value != null)
            jo.put(valueKey, this.value);
        if(this.time != null)
            jo.put(timeKey, this.time);
        if(this.isApp != null)
            jo.put(isAppKey, this.isApp);
        if(this.allSubCommentNumber != null)
            jo.put(allSubCommentNumberKey, this.allSubCommentNumber);
        if(this.commentNumber != null)
            jo.put(commentNumberKey, this.commentNumber);
        if(this.buzzParId != null)
            jo.put(buzzParIdKey, this.buzzParId);
        jo.put(canDeleteKey, canDelete);
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
    public int compareTo(SubComment o) {
        //asc sort
        return this.time.compareTo(o.time);
    }
}
