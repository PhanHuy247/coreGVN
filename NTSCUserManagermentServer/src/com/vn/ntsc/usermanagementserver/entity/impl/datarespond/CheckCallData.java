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
public class CheckCallData implements IEntity {

    private static final String callerPointKey = "caller_point";
    public Integer callerPoint;

    private static final String receiverPointKey = "receiver_point";
    public Integer receiverPoint;

    private static final String isPurchaseKey = "is_purchase";
    public Integer isPurchase;

    private static final String canSendRequestKey = "can_send_request";
    public Integer canSendRequest = 0;

    public CheckCallData(Integer callerPoint, Integer receiverPoint, Integer isPurchase) {
        this.callerPoint = callerPoint;
        this.receiverPoint = receiverPoint;
        this.isPurchase = isPurchase;
    }

    public CheckCallData(){
    }
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(callerPoint != null){
            jo.put(callerPointKey, callerPoint);
        }
        if(receiverPoint != null){
            jo.put(receiverPointKey, receiverPoint);
        }
        if(isPurchase != null){
            jo.put(isPurchaseKey, isPurchase);
        }
        if(canSendRequest != null){
            jo.put(canSendRequestKey, canSendRequest);
        }
        return jo;
    }
}
