/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

public class ReplacedReviewCommentData implements IEntity {

    private static final String commentIDKey = "comment_id";
    public String commentID;
    
    private static final String replacedStringKey = "replaced_string";
    public String replacedString;

    public ReplacedReviewCommentData(String commentID, String content) {
        this.commentID = commentID;
        this.replacedString = content;        
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.commentID != null) {
            jo.put(commentIDKey, this.commentID);
        }
        if (this.replacedString != null) {
            jo.put(replacedStringKey, this.replacedString);
        }
   
        return jo;
    }
}
