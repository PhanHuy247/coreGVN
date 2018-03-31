/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.user;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author Admin
 */
public class ConnectionInfor implements IEntity{

    private static final String jobKey = "job";   // out
    public Long job;
    
    private static final String isFavouristKey = "is_fav";   // out
    public Long isFavourist;

    private static final String requestIdKey = "rqt_id"; //out
    public String requestId;

    private static final String chatPointKey = "chat_point";
    public Integer chatPoint;
    
    private static final String viewImagePointKey = "view_image_point";
    public Integer viewImagePoint;
    
    private static final String viewImageTimeKey = "view_image_time";
    public Integer viewImageTime;
    
    private static final String watchVideoPointKey = "watch_video_point";
    public Integer watchVideoPoint;    
    
    private static final String watchVideoTimeKey = "watch_video_time";
    public Integer watchVideoTime;    
    
    private static final String listenAudioPointKey = "listen_audio_point";
    public Integer listenAudioPoint;
    
    private static final String listenAudioTimeKey = "listen_audio_time";
    public Integer listenAudioTime;    
    
    private static final String commentBuzzPointKey = "comment_buzz_point";
    public Integer commentBuzzPoint;
    
    private static final String subCommentPointKey = "sub_comment_point";
    public Integer subCommentPoint;
    
    private static final String isContactedKey = "is_contacted";
    public boolean isContacted;
    
    //khanhdd 06/01/2017
    private static final String memoKey = "memo";
    public String memo;
    
    public static final String isAlertKey = "is_alt";
    public Integer isAlert;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.isFavourist != null) {
            jo.put(isFavouristKey, isFavourist);
        }
        if (this.job != null) {
            jo.put(jobKey, job);
        }

        if (this.requestId != null) {
            jo.put(requestIdKey, requestId);
        }
        
        if (this.chatPoint != null) {
            jo.put(chatPointKey, chatPoint);
        }
        if (this.viewImagePoint != null) {
            jo.put(viewImagePointKey, viewImagePoint);
        }
        if (this.viewImageTime != null) {
            jo.put(viewImageTimeKey, viewImageTime);
        }
        
        if (this.watchVideoPoint != null) {
            jo.put(watchVideoPointKey, watchVideoPoint);
        }
        if (this.watchVideoTime != null) {
            jo.put(watchVideoTimeKey, watchVideoTime);
        }
        
        if (this.listenAudioPoint != null) {
            jo.put(listenAudioPointKey, listenAudioPoint);
        }
        if (this.listenAudioTime != null) {
            jo.put(listenAudioTimeKey, listenAudioTime);
        }
        if (this.commentBuzzPoint != null) {
            jo.put(commentBuzzPointKey, commentBuzzPoint);
        }
        if (this.subCommentPoint != null) {
            jo.put(subCommentPointKey, subCommentPoint);
        }
        if (this.memo != null) {
            jo.put(memoKey, memo);
        }
        jo.put(isContactedKey, isContacted);
        if(isAlert != null){
            jo.put(isAlertKey, isAlert);
        }
        return jo;
    }

}
