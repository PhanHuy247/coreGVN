/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.log;

import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.OneObjectLog;
import com.vn.ntsc.backend.server.respond.impl.csv.HeaderCreator;
import com.vn.ntsc.backend.server.respond.impl.csv.Headers;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeSwitch;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeValue;

/**
 *
 * @author RuAc0n
 */
public class LogWinkBomb extends OneObjectLog implements IEntity{

    private static final List<String> headers = new ArrayList<String>();
    private static final List<String> japaneseHeader = new ArrayList<String>();
    private static final List<String> englishHeader = new ArrayList<String>();
    private static final JSONObject jsonEnglishType = new JSONObject();
    private static final JSONObject jsonJapaneseType = new JSONObject();    
    
    private static final String idKey = "id";
    @TypeSwitch (value = ParamKey.NEXT, header = ParamKey.NEXT)
    public String id;    
  
    private static final String bombNumberKey = "bomb_number";
    @TypeSwitch(header = Headers.number_person)
    public Integer bombNumber;

    private static final String pointKey = "point";
    @TypeSwitch(header = Headers.points)    
    public Integer point;    
    
    private static final String messageKey = "message";
    @TypeSwitch(header = Headers.message_content)    
    public String message;

    private static final String timeKey = "time";
    @TypeSwitch (value = ParamKey.TIME, header = Headers.wink_bomb_time)
    public String time;   
    
    static{
        initHeader();
        initType();
    }
   
    private static void initType(){
        
//        jsonEnglishType = new JSONObject();
//        jsonJapaneseType = new JSONObject();
        
        JSONObject value = new JSONObject();
        //user type
        value.putAll(TypeValue.en_user_type);
        jsonEnglishType.put(ParamKey.USER_TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_user_type);
        jsonJapaneseType.put(ParamKey.USER_TYPE, value);
        
    }
    
    private static void initHeader(){
        
//        japaneseHeader = new ArrayList<String>();
//        englishHeader = new ArrayList<String>();
                
        List<String> keys = new ArrayList<String>();
        keys.add(Headers.number);
        keys.add(Headers.user_id);
        keys.add(Headers.user_name);
        keys.add(Headers.user_type);
        keys.add(Headers.email);
        keys.add(Headers.group);
        keys.add(Headers.cm_code);
        keys.add(Headers.ip);
        keys.add(Headers.number_person);
        keys.add(Headers.points);
        keys.add(Headers.message_content);
        keys.add(Headers.wink_bomb_time);
        headers.addAll(keys);
//        headers = keys;
        
        HeaderCreator.createHeader(japaneseHeader, englishHeader, keys);
      
    }     
    
    public LogWinkBomb() {
    }    
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.id != null) {
            jo.put(idKey, this.id);
        }
        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.email != null) {
            jo.put(emailKey, this.email);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (this.bombNumber != null) {
            jo.put(bombNumberKey, this.bombNumber);
        }
        if (this.point != null) {
            jo.put(pointKey, this.point);
        }
        if (this.message != null) {
            jo.put(messageKey, this.message);
        }
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if(this.cmCode != null){
            jo.put(cmCodeKey, this.cmCode);
        }
        if(this.ip != null){
            jo.put(ipKey, ip);
        }
        if(this.userType != null)
            jo.put(userTypeKey, userType);
        return jo;
    }
    
    @Override
    public List<String> getHeaders(Integer type) {
        if(type != null && type == 1)
            return englishHeader;
        else
            return japaneseHeader;
    }
    
    // GET LIST KEY OF SUBCLASS
    @Override
    public List<String> getKeys() {
        return headers;
    }    
    // get user type follow english or japanese
    @Override
    public JSONObject getJsonType(Integer type) {
        if(type != null && type == 1)
            return jsonEnglishType;
        else
            return jsonJapaneseType;
    }    
}
