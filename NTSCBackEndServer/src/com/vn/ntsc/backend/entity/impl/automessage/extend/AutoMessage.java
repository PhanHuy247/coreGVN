/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.automessage.extend;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.impl.automessage.Message;

/**
 *
 * @author RuAc0n
 */
public class AutoMessage extends Message{

    private static final String senderKey = "sender";
    public String sender;
    
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = super.toJsonObject();
        if(this.sender != null)
            jo.put(senderKey, this.sender);
        return jo;
    }

    public AutoMessage(String content, String sender, String id, String time, JSONObject query, int flag, String ip) {
        super(id, time, content, query, flag);
        this.sender = sender;
        encodeMsg();
    }

    public AutoMessage(String content, String sender, String id, String time, int flag, Integer receiverNumber, String ip) {
        super(id, time, content, flag, receiverNumber);
        this.sender = sender;
        encodeMsg();
    }

    public void update(String content, String time, String sender) {
        super.update(content, time); //To change body of generated methods, choose Tools | Templates.
        this.sender = sender;
        encodeMsg();
    }
    
    
}
