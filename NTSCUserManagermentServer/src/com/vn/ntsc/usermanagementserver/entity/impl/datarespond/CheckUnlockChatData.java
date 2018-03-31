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
public class CheckUnlockChatData implements IEntity {

    private static final String isUnlckKey = "is_unlck";
    public Long is_unlck;

    private static final String pointKey = "point";
    public Long point;
    
    private static final String priceKey = "price";
    public Integer price;   
    
    private static final String receivedPointKey = "received_point";
    public Integer receivedPoint;     

    public CheckUnlockChatData(Long is_unlck, Long point, Integer price, Integer receivedPoint) {
        this.is_unlck = is_unlck;
        this.point = point;
        this.price = price;
        this.receivedPoint = receivedPoint;
    }

    public CheckUnlockChatData(Long point, Integer price) {
        this.point = point;
        this.price = price;
    }
    
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.is_unlck != null) {
            jo.put(isUnlckKey, this.is_unlck);
        }
        if (this.point != null) {
            jo.put(pointKey, point);
        }
        if (this.price != null) {
            jo.put(priceKey, price);
        }
        if (this.receivedPoint != null) {
            jo.put(receivedPointKey, receivedPoint);
        }
        return jo;
    }
}
