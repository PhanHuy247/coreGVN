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
 * @author HuyDX
 */
public class PointExchangedByChatData implements IEntity{

    private static final String messageIdKey = "msg_id";
    private String messageId;
    private static final String userIdKey = "user_id";
    private String userId;
    private static final String pointDifferKey = "point_differ";
    private Integer pointDiffer;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (userId != null) {
            jo.put(userIdKey, userId);
        }
        if (pointDiffer != null) {
            jo.put(pointDifferKey, pointDiffer);
        }
        if (messageId != null) {
            jo.put(messageIdKey, messageId);
        }  
        return jo;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPointDiffer() {
        return pointDiffer;
    }

    public void setPointDiffer(int pointDiffer) {
        this.pointDiffer = pointDiffer;
    }
    
}
