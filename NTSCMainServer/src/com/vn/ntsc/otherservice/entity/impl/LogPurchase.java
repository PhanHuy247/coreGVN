/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.entity.impl;

import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class LogPurchase implements IEntity{

    private static final String transactionIdKey = "transaction_id";
    public String transactionId;

    public LogPurchase(String transactionId){
        this.transactionId = transactionId;
    }
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(transactionId != null)
            jo.put(transactionIdKey, transactionId);


        return jo;
    }

}
