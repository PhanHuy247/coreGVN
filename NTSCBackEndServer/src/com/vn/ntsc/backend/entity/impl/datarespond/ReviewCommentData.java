/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

public class ReviewCommentData implements IEntity {

    private static final String commentorKey = "commentor";
    public String commentor;
    
    private static final String buzzOwnerIdKey = "buzz_owner_id";
    public String buzzOwnerId;
    
    private static final String buzzIdKey = "buzz_id";
    public String buzzId;

    public ReviewCommentData(String commentor, String buzzOwnerId, String buzzId) {
        this.commentor = commentor;
        this.buzzOwnerId = buzzOwnerId;
        this.buzzId = buzzId;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.commentor != null) {
            jo.put(commentorKey, this.commentor);
        }
        if (this.buzzOwnerId != null) {
            jo.put(buzzOwnerIdKey, this.buzzOwnerId);
        }
        if (this.buzzId != null) {
            jo.put(buzzIdKey, this.buzzId);
        }
        return jo;
    }
}
