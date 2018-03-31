/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class ReviewSubCommentData implements IEntity {

    private static final String buzzOwnerNameKey = "buzz_owner_name";
    public String buzzOwnerName;
    
    private static final String commentOwnerNameKey = "comment_owner_name";
    public String commentOwnerName;
    
    private static final String notificationListKey = "notification_list";
    public List<String> notificationList;
    
    private static final String isNotiReviewSubCommentKey = "is_noti_review_sub_comment";
    public Integer isNotiReviewSubComment;
    
    public ReviewSubCommentData(String buzzOwnerName, String commentOwnerName, List<String> notificationList, int isNotiReviewSubComment) {
        this.buzzOwnerName = buzzOwnerName;
        this.commentOwnerName = commentOwnerName;
        this.notificationList = notificationList;
        this.isNotiReviewSubComment = isNotiReviewSubComment;
    }
    

    @Override
    public JSONObject toJsonObject() {
        
        JSONObject jo = new JSONObject();
        if (buzzOwnerName != null) {
            jo.put(buzzOwnerNameKey, buzzOwnerName);
        }
        if(isNotiReviewSubComment != null )
            jo.put(isNotiReviewSubCommentKey, isNotiReviewSubComment);
        if(commentOwnerName != null )
            jo.put(commentOwnerNameKey, commentOwnerName);
        if(notificationList != null ){
            JSONArray arr = new JSONArray();
            for(String id : notificationList){
                arr.add(id);
            }
            jo.put(notificationListKey, arr);
        }
        
        return jo;
    }

}
