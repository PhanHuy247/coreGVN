/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity;

import java.util.List;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.server.respond.impl.csv.Headers;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeSwitch;

/**
 *
 * @author RuAc0n
 */
public abstract class OneObjectLog {

    protected static final String userIdKey = "user_id";
    @TypeSwitch(header = Headers.user_id)
    public String userId;

    protected static final String userNameKey = "user_name";
    @TypeSwitch(header = Headers.user_name)
    public String userName;

    protected static final String userTypeKey = "user_type";
    @TypeSwitch(value = ParamKey.USER_TYPE, header = Headers.user_type)
    public Integer userType;

    protected static final String emailKey = "email";
    @TypeSwitch(header = Headers.email)
    public String email;

    @TypeSwitch(header = Headers.group)
    public String group;

    protected static final String cmCodeKey = "cm_code";
    @TypeSwitch(header = Headers.cm_code)
    public String cmCode;

    protected static final String ipKey = "ip";
    @TypeSwitch(header = Headers.ip)
    public String ip;

    protected static final String partnerIdKey = "partner_id";
    @TypeSwitch(value = ParamKey.NEXT, header = ParamKey.NEXT)
    public String partnerId;

    protected static final String partnerNameKey = "partner_name";
    @TypeSwitch(value = ParamKey.NEXT, header = ParamKey.NEXT)
    public String partnerName;

    protected static final String application_idKey = "application_id";
    @TypeSwitch(value = ParamKey.APPLICATION_ID, header = Headers.status)
    public String application_id;

    protected static final String applicationNameKey = "application_name";
    @TypeSwitch(header = ParamKey.APPLICATION_NAME)
    public String applicationName;

    // nhung list nay se duoc nhung thang subclass su dung va put value into ti
//    protected List<String> headers;
//    protected List<String> japaneseHeader;
//    protected List<String> englishHeader;
//    protected JSONObject jsonEnglishType;
//    protected JSONObject jsonJapaneseType;
//    public List<String> getHeaders(Integer type) {
//        if(type != null && type == 1)
//            return englishHeader;
//        else
//            return japaneseHeader;
//    }
    public abstract List<String> getHeaders(Integer type);

    // GET LIST KEY OF SUBCLASS
//    public List<String> getKeys() {
//        return headers;
//    }    
    public abstract List<String> getKeys();

    // get user type follow english or japanese
//    public JSONObject getJsonType(Integer type) {
//        if(type != null && type == 1)
//            return jsonEnglishType;
//        else
//            return jsonJapaneseType;
//    }     

    public abstract JSONObject getJsonType(Integer type);

    public OneObjectLog() {
    }

}
