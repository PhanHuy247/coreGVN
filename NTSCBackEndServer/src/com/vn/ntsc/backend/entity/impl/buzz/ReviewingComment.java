/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.buzz;

import java.util.List;
import eazycommon.constant.Constant;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author DuongLTD
 */
public class ReviewingComment implements Comparable<ReviewingComment>, IEntity {

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
    
//    private static final String reviewingSubCommentNumberKey = "reviewing_sub_comment_number";
//    public Integer reviewingSubCommentNumber;
//    
//    private static final String reviewingFlagKey = "is_approved";
//    public Integer reviewFlag;

    private static final String subCommentKey = "sub_comment";
    public List<ReviewingSubComment> subComments;
    
    public ReviewingComment() {
//        buzzId = "buzzId";
//        userId = "userId";
//        userName = "username";
//        commentId = "commentId";
//        commentValue = "buzzValue";
//        commentTime = "20151231101010";
//        int i = new Random().nextInt();
//        int number = i%2 == 0? 5 : 0;
//        i = new Random().nextInt();
//        reviewFlag = i%2 ==0 ? 0 : 1;
//        reviewingSubCommentNumber = number;
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
//        if (this.reviewingSubCommentNumber != null) {
//            jo.put(reviewingSubCommentNumberKey, this.reviewingSubCommentNumber);
//        }
//        if (this.reviewFlag != null) {
//            boolean isApproved = reviewFlag == Constant.YES;
//            jo.put(reviewingFlagKey, isApproved);
//        }
        if(this.subComments != null){
            JSONArray array = new JSONArray();
            for(ReviewingSubComment reviewingSubComment : subComments){
                array.add(reviewingSubComment.toJsonObject());
            }
            jo.put(subCommentKey, array);
        }
        return jo;
    }
    
    @Override
    public int compareTo(ReviewingComment o) {
        return this.commentTime.compareTo(o.commentTime);
    }
}
