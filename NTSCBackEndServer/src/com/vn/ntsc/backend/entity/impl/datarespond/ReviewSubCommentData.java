/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.datarespond;

import java.util.Collection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

public class ReviewSubCommentData implements IEntity {

    private static final String subCommentorKey = "sub_commentor";
    public String subCommentor;
    
    private static final String buzzOwnerIdKey = "buzz_owner_id";
    public String buzzOwnerId;
    
    private static final String buzzIdKey = "buzz_id";
    public String buzzId;
    
    private static final String commentIdKey = "comment_id";
    public String commentId;
    
    private static final String commentOwnerIdKey = "comment_owner_id";
    public String commentOwnerId;
    
    private static final String notificationListKey = "notification_list";
    public Collection<String> notificationList;    

    public ReviewSubCommentData(String subCommentor, String buzzOwnerId, String buzzId, String commentId, String commentOwnerId, Collection<String> notificationList) {
        this.subCommentor = subCommentor;
        this.buzzOwnerId = buzzOwnerId;
        this.buzzId = buzzId;
        this.commentId = commentId;
        this.commentOwnerId = commentOwnerId;
        this.notificationList = notificationList;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.subCommentor != null) {
            jo.put(subCommentorKey, this.subCommentor);
        }
        if (this.buzzOwnerId != null) {
            jo.put(buzzOwnerIdKey, this.buzzOwnerId);
        }
        if (this.buzzId != null) {
            jo.put(buzzIdKey, this.buzzId);
        }
        if (this.commentId != null) {
            jo.put(commentIdKey, this.commentId);
        }
        if (this.commentOwnerId != null) {
            jo.put(commentOwnerIdKey, this.commentOwnerId);
        }
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
