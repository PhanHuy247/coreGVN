/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.automessage;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class Message implements IEntity{

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

    public Message() {
    }
    
    public Message(String id, String time, String message, JSONObject query, int flag) {
        this.id = id;
        this.time = time;
        this.content = message;
        this.flag = flag;
        this.query = query;
    }

    public Message(String id, String time, String msg, int flag, Integer receiverNumber) {
        this.id = id;
        this.time = time;
        this.content = msg;
        this.flag = flag;
        this.receiverNumber = receiverNumber;
    }

    
    // encode các kí tự đặc biệt
    protected void encodeMsg(){
        
        this.content = this.content.replace("{","&ob");
        this.content = this.content.replaceAll("}", "&cb");
        this.content = this.content.replaceAll(";", "&sc");
        this.content = this.content.replace("\\", "&bs");
        this.content = this.content.replaceAll("\"", "&dg");
        this.content = this.content.replaceAll("\n", "&nl");
        
    }  

    protected void update(String content, String time) {
        this.content = content;
        this.time = time;
    }    
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

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
