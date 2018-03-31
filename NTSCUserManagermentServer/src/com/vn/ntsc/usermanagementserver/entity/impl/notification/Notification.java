/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.notification;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class Notification implements IEntity{
    
    private static final String notiIdKey = "noti_id";
    public String notiId;
    private static final String notiTypeKey = "noti_type";
    public Integer notiType;

    private static final String notiUserNameKey = "noti_user_name";
    public String notiUserName;

    private static final String notiUserIdKey = "noti_user_id";
    public String fromNotiUserId;

    private static final String notiBuzzOwnerIdKey = "noti_buzz_owner_id";
    public String notiBuzzOwnerId;

    private static final String notiBuzzIdKey = "noti_buzz_id";
    public String notiBuzzId;

    private static final String notiBuzzOwnerNameKey = "noti_buzz_owner_name";
    public String notiBuzzOwnerName;

    private static final String pointKey = "point";
    public Integer point;

    private static final String timeKey = "time";
    public String time;
    
    private static final String notiImageIdKey = "noti_image_id";
    public String notiImageId;
    
    private static final String notiFileIdKey = "noti_file_id";
    public String notiFileId;
    
    private static final String contentKey = "content";
    public String content;
    
    private static final String urlKey = "url";
    public String url;
    
    private static final String pushIdKey = "push_id";
    public String pushId;

    private static final String notiCommentIdKey = "noti_comment_id";
    public String notiCommentId;
    
    private static final String notiCommentOwnerIdKey = "noti_comment_owner_id";
    public String notiCommentOwnerId;  
    
    private static final String notiCommentOwnerNameKey = "noti_comment_owner_name";
    public String notiCommentOwnerName;    
    
    private static final String isReadKey = "is_read";
    public Integer isRead;
    
    private static final String isSeenKey = "is_seen";
    public Integer isSeen;
    
    private static final String textKey = "text_buzz";
    public String text;
    
    private static final String thumnailUrlKey = "thumnail_url";
    public String thumnailUrl;
    
    private static final String avatarKey = "ava_id";
    public String avataId;
    
    private static final String avatarUrlKey = "ava_url";
    public String avataUrl;
    
    private static final String buzzTypeKey = "buzz_type";
    public Integer buzzType;
    
    private static final String genderKey = "gender";
    public Integer gender;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(this.notiId != null){
            jo.put(notiIdKey, this.notiId);
        }
        if (this.notiType != null) {
            jo.put(notiTypeKey, this.notiType);
        }
        if (this.fromNotiUserId != null) {
            jo.put(notiUserIdKey, this.fromNotiUserId);
        }
        if (this.notiUserName != null) {
            jo.put(notiUserNameKey, this.notiUserName);
        }
        if (this.notiBuzzOwnerId != null) {
            jo.put(notiBuzzOwnerIdKey, this.notiBuzzOwnerId);
        }
        if (this.notiBuzzId != null) {
            jo.put(notiBuzzIdKey, this.notiBuzzId);
        }
        if (this.notiBuzzOwnerName != null) {
            jo.put(notiBuzzOwnerNameKey, this.notiBuzzOwnerName);
        }
        if (this.point != null) {
            jo.put(pointKey, this.point);
        }
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if(this.notiImageId != null){
            jo.put(notiImageIdKey, this.notiImageId);
        }
        if(this.notiFileId != null){
            jo.put(notiFileIdKey, this.notiFileId);
        }
        if(this.content != null){
            jo.put(contentKey, this.content);
        }
        if(this.url != null){
            jo.put(urlKey, this.url);
        }
        if(this.pushId != null){
            jo.put(pushIdKey, this.pushId);
        }
        if(this.notiCommentId != null){
            jo.put(notiCommentIdKey, this.notiCommentId);
        }
        if(this.notiCommentOwnerId != null){
            jo.put(notiCommentOwnerIdKey, this.notiCommentOwnerId);
        }
        if(this.notiCommentOwnerName != null){
            jo.put(notiCommentOwnerNameKey, this.notiCommentOwnerName);
        }
        if(this.isRead != null){
            jo.put(isReadKey, this.isRead);
        }
        if(this.isSeen != null){
            jo.put(isSeenKey, this.isSeen);
        }
        if(this.text != null){
            jo.put(textKey, this.text);
        }
        if(this.thumnailUrl != null){
            jo.put(thumnailUrlKey, this.thumnailUrl);
        }
        if(this.avataId != null){
            jo.put(avatarKey, this.avataId);
        }
        if(this.avataUrl != null){
            jo.put(avatarUrlKey, this.avataUrl);
        }
        if(this.buzzType != null){
            jo.put(buzzTypeKey, this.buzzType);
        }
        if(this.gender != null){
            jo.put(genderKey, this.gender);
        }
        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

}
