/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class AddSubCommentData implements IEntity {

    private static final String buzzOwnerNameKey = "buzz_owner_name";
    public String buzzOwnerName;
    
    private static final String commentOwnerNameKey = "comment_owner_name";
    public String commentOwnerName;
    
    private static final String notificationListKey = "notification_list";
    public List<String> notificationList;
    
    public Long point;
    
    public Integer isNoti;

    public AddSubCommentData(String buzzOwnerName, String commentOwnerName, List<String> notificationList, Long point, int isNoti) {
        this.buzzOwnerName = buzzOwnerName;
        this.commentOwnerName = commentOwnerName;
        this.notificationList = notificationList;
        this.point = point;
        this.isNoti = isNoti;
    }
    

    @Override
    public JSONObject toJsonObject() {
        
        JSONObject jo = new JSONObject();
        if (buzzOwnerName != null) {
            jo.put(buzzOwnerNameKey, buzzOwnerName);
        }
        if(commentOwnerName != null )
            jo.put(commentOwnerNameKey, commentOwnerName);
        if(notificationList != null ){
            JSONArray arr = new JSONArray();
            for(String id : notificationList){
                arr.add(id);
            }
            jo.put(notificationListKey, arr);
        }
        
        jo.put(ParamKey.POINT, point);
        jo.put(ParamKey.IS_NOTI, isNoti);
        return jo;
    }

}
