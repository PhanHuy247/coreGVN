/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class ReviewCommentData implements IEntity {

    private static final String buzzOwnerNameKey = "buzz_owner_name";
    public String buzzOwnerName;

    private static final String isNotiCommentBuzzKey = "is_noti_comment_buzz";
    public Integer isNotiCommentBuzz;
    
    private static final String isNotiReviewCommentKey = "is_noti_review_comment";
    public Integer isNotiReviewComment;
    
    public ReviewCommentData( String buzzOwnerName, Integer isNotiCommentBuzz, Integer isNotiReviewComment) {
        this.buzzOwnerName = buzzOwnerName;
        this.isNotiCommentBuzz = isNotiCommentBuzz;
        this.isNotiReviewComment = isNotiReviewComment;
    }

    @Override
    public JSONObject toJsonObject() {
        
        JSONObject jo = new JSONObject();

        if (buzzOwnerName != null) {
            jo.put(buzzOwnerNameKey, buzzOwnerName);
        }
        if(isNotiCommentBuzz != null )
            jo.put(isNotiCommentBuzzKey, isNotiCommentBuzz);
        if(isNotiReviewComment != null )
            jo.put(isNotiReviewCommentKey, isNotiReviewComment);
        
        return jo;
    }

}
