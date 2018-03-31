/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.entity.impl;

import org.json.simple.JSONObject;


/**
 *
 * @author hungdt
 */
public class AutoNewsNotify {

    private static final String urlKey = "url";
    public String url;
    
    private static final String clickUserNumberKey = "click_user_number";
    public Integer clickUserNumber;
    
    private static final String ratioClickedUserKey = "ratio_clicked_user";
    public Double ratioClickedUser;
    
    private static final String idKey = "id";
    public String id;
    private static final String timeKey = "time";
    public String time;
    private static final String contentKey = "content"; 
    public String content;
    private static final String flagKey = "flag";
    public Integer flag;
    private static final String receiverNumberKey = "receiver_number";
    public Integer receiverNumber;
    
    public String ip;
    
    private static final String queryKey = "query";
    public JSONObject query;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getClickUserNumber() {
        return clickUserNumber;
    }

    public void setClickUserNumber(Integer clickUserNumber) {
        this.clickUserNumber = clickUserNumber;
    }

    public Double getRatioClickedUser() {
        return ratioClickedUser;
    }

    public void setRatioClickedUser(Double ratioClickedUser) {
        this.ratioClickedUser = ratioClickedUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(Integer receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public JSONObject getQuery() {
        return query;
    }

    public void setQuery(JSONObject query) {
        this.query = query;
    }
    
    public AutoNewsNotify() {
    }
    

    
    public AutoNewsNotify( String id,  String content, String url, String time, JSONObject query, Integer receiverNumber) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.time = time;
        this.query = query;
        this.receiverNumber = receiverNumber;
    }
    
      public AutoNewsNotify( String id, String ip,  String content, String url, String time) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.time = time;
        this.ip = ip;
    }
      

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
        if(this.url != null)
            jo.put(urlKey, this.url);
        if(this.clickUserNumber != null)
            jo.put(clickUserNumberKey, this.clickUserNumber);
        if(this.ratioClickedUser != null)
            jo.put(ratioClickedUserKey, this.ratioClickedUser);
        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.time != null)
            jo.put(timeKey, this.time);
        if(this.content != null)
            jo.put(contentKey, this.content);
        if(this.flag != null){
            jo.put(flagKey, this.flag);
        }
        if(this.receiverNumber != null){
            jo.put(receiverNumberKey, this.receiverNumber);
        }
        if(this.query != null){
            jo.put(queryKey, this.query);
        }
        return jo;
    }    
    
}
