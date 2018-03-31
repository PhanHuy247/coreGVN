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
public class LogDeactivate extends OneObjectLog implements IEntity {

    private static final List<String> headers = new ArrayList<>();
    private static final List<String> japaneseHeader = new ArrayList<>();
    private static final List<String> englishHeader = new ArrayList<>();
    private static final JSONObject jsonEnglishType = new JSONObject();
    private static final JSONObject jsonJapaneseType = new JSONObject();    
    
    private static final String timeKey = "time";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.deactivate_time)
    public String time;
    
    private static final String commentKey = "deactivate_comment";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.deactivate_comment)
    public String comment;

    static {
        initHeader();
        initType();
    }

    private static void initType() {

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

    private static void initHeader() {

//        japaneseHeader = new ArrayList<String>();
//        englishHeader = new ArrayList<String>();

        List<String> keys = new ArrayList<>();
        keys.add(Headers.number);
        keys.add(Headers.user_id);
        keys.add(Headers.user_name);
        keys.add(Headers.user_type);
        keys.add(Headers.email);
        keys.add(Headers.group);
        keys.add(Headers.cm_code);
        keys.add(Headers.deactivate_comment);
        keys.add(Headers.ip);
        keys.add(Headers.deactivate_time);

        
        headers.addAll(keys);

        HeaderCreator.createHeader(japaneseHeader, englishHeader, keys);

    }

    public LogDeactivate() {
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (this.email != null) {
            jo.put(emailKey, email);
        }
        if (this.cmCode != null) {
            jo.put(cmCodeKey, cmCode);
        }
        if (this.comment != null) {
            jo.put(commentKey, comment);
        }
        if (this.ip != null) {
            jo.put(ipKey, ip);
        }
        if (this.userType != null) {
            jo.put(userTypeKey, userType);
        }
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
