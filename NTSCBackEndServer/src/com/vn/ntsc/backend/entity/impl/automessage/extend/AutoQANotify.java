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
 * @author Administrator
 */
public class AutoQANotify extends Message{
    
    private static final String urlKey = "url";
    public String url;
    
    private static final String clickUserNumberKey = "click_user_number";
    public Integer clickUserNumber;
    
    private static final String ratioClickedUserKey = "ratio_clicked_user";
    public Double ratioClickedUser;
    
    
    public AutoQANotify(String content, String url, String ip,  String id, String time, JSONObject query, int flag) {
        super(id, time, content, query, flag);
        this.url = url;
        this.ip = ip;
    }

    public AutoQANotify(String content, String url, String id, String time, int flag, Integer receiverNumber) {
        super(id, time, content, flag, receiverNumber);
        this.url = url;
    }
    
    public AutoQANotify( String id,  String content, String url, String time, JSONObject query, Integer receiverNumber) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.time = time;
        this.query = query;
        this.receiverNumber = receiverNumber;
    }
    
    public void update(String content, String url, String time) {
        super.update(content, time);
        this.url = url;
    }

    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = super.toJsonObject();
        if(this.url != null)
            jo.put(urlKey, this.url);
        if(this.clickUserNumber != null)
            jo.put(clickUserNumberKey, this.clickUserNumber);
        if(this.ratioClickedUser != null)
            jo.put(ratioClickedUserKey, this.ratioClickedUser);
        return jo;
    }    
}
