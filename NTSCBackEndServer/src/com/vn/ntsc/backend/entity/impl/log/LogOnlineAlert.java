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
import com.vn.ntsc.backend.entity.TwoObjectLog;
import com.vn.ntsc.backend.server.respond.impl.csv.HeaderCreator;
import com.vn.ntsc.backend.server.respond.impl.csv.Headers;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeSwitch;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeValue;

/**
 *
 * @author RuAc0n
 */
public class LogOnlineAlert extends TwoObjectLog implements IEntity{

    private static final List<String> headers = new ArrayList<String>();
    private static final List<String> japaneseHeader = new ArrayList<String>();
    private static final List<String> englishHeader = new ArrayList<String>();
    private static final JSONObject jsonEnglishType = new JSONObject();
    private static final JSONObject jsonJapaneseType = new JSONObject();     
    
    private static final String reqIdKey = "req_id";
    @TypeSwitch(header = Headers.alert_user_id)    
    public String reqId;

    private static final String reqUserNameKey = "req_user_name";
    @TypeSwitch(header = Headers.alert_user_name)     
    public String reqUserName;

    private static final String reqUserTypeKey = "req_user_type";
    @TypeSwitch (value = ParamKey.USER_TYPE, header = Headers.alert_user_type)
    public Integer reqUserType;    
    
    private static final String reqEmailKey = "req_email";
    @TypeSwitch(header = Headers.alert_user_email)     
    public String reqEmail;

    @TypeSwitch(header = Headers.alert_user_group)     
    public String reqGroup;
    
    private static final String reqCmCodeKey = "req_cm_code";
    @TypeSwitch(header = Headers.alert_user_cm_code)     
    public String reqCmCode; 
    
    private static final String ipKey = "ip";
    @TypeSwitch(header = Headers.alert_user_ip)     
    public String ip;        
    
    private static final String partnerIdKey = "partner_id";
    @TypeSwitch(header = Headers.alerted_user_id)     
    public String partnerId;

    private static final String partnerUserNameKey = "partner_user_name";
    @TypeSwitch(header = Headers.alerted_user_name)     
    public String partnerUserName;

    private static final String partnerUserTypeKey = "partner_user_type";     
    @TypeSwitch (value = ParamKey.USER_TYPE, header = Headers.alerted_user_type)
    public Integer partnerUserType;    
    
    private static final String partnerEmailKey = "partner_email";
    @TypeSwitch(header = Headers.alerted_user_email)     
    public String partnerEmail;

    @TypeSwitch(header = Headers.alerted_user_group)     
    public String partnerGroup;
    
    private static final String partnerCmCodeKey = "partner_cm_code";
    @TypeSwitch(header = Headers.alerted_user_cm_code)     
    public String partnerCmCode;     
    
    private static final String alertFrequencyKey = "alt_fre";
    @TypeSwitch (value = ParamKey.ALERT_FREQUENCY, header = Headers.alert_frequency)    
    public Integer alertFrequency;    
    
    private static final String alertTypeKey = "alt_type";
    @TypeSwitch (value = ParamKey.ALERT_TYPE,  header = Headers.alert_type)      
    public Integer alertType;

    private static final String timeKey = "time";
    @TypeSwitch (value = ParamKey.TIME, header = Headers.alert_time)    
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
        
        //alert frequency
        value = new JSONObject();
        value.putAll(TypeValue.en_alert_frequency);
        jsonEnglishType.put(ParamKey.ALERT_FREQUENCY, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_alert_frequency);
        jsonJapaneseType.put(ParamKey.ALERT_FREQUENCY, value);        
        
        //alert type
        value = new JSONObject();
        value.putAll(TypeValue.en_alert_type);
        jsonEnglishType.put(ParamKey.ALERT_TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_alert_type);
        jsonJapaneseType.put(ParamKey.ALERT_TYPE, value);         
    }
    
    private static void initHeader(){
        
//        japaneseHeader = new ArrayList<String>();
//        englishHeader = new ArrayList<String>();
                
        List<String> keys = new ArrayList<String>();
        keys.add(Headers.number);
        keys.add(Headers.alert_user_id);
        keys.add(Headers.alert_user_name);
        keys.add(Headers.alert_user_type);
        keys.add(Headers.alert_user_email);
        keys.add(Headers.alert_user_group);
        keys.add(Headers.alert_user_cm_code);
        keys.add(Headers.alert_user_ip);
        keys.add(Headers.alerted_user_id);
        keys.add(Headers.alerted_user_name);
        keys.add(Headers.alerted_user_type);
        keys.add(Headers.alerted_user_email);
        keys.add(Headers.alerted_user_group);
        keys.add(Headers.alerted_user_cm_code);
        keys.add(Headers.alert_frequency);
        keys.add(Headers.alert_type);
        keys.add(Headers.alert_time); 
        headers.addAll(keys);
        
//        headers = keys;
        
        HeaderCreator.createHeader(japaneseHeader, englishHeader, keys);
      
    }    
    
    public LogOnlineAlert() {
    }
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.reqId != null) {
            jo.put(reqIdKey, this.reqId);
        }
        if (this.reqEmail != null) {
            jo.put(reqEmailKey, this.reqEmail);
        }
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if (this.reqUserName != null) {
            jo.put(reqUserNameKey, this.reqUserName);
        }
        if (this.partnerEmail != null) {
            jo.put(partnerEmailKey, this.partnerEmail);
        }
        if(this.reqCmCode != null){
            jo.put(reqCmCodeKey, this.reqCmCode);
        }        
        if (this.partnerUserName != null) {
            jo.put(partnerUserNameKey, this.partnerUserName);
        }
        if (this.partnerId != null) {
            jo.put(partnerIdKey, this.partnerId);
        }
        if(this.partnerCmCode != null){
            jo.put(partnerCmCodeKey, this.partnerCmCode);
        }        
        if (this.alertType != null) {
            jo.put(alertTypeKey, this.alertType);
        }
        if (this.alertFrequency != null) {
            jo.put(alertFrequencyKey, this.alertFrequency);
        }
        if(this.ip != null){
            jo.put(ipKey, ip);
        }
        if(this.reqUserType != null)
            jo.put(reqUserTypeKey, this.reqUserType);
        if(this.partnerUserType != null)
            jo.put(partnerUserTypeKey, this.partnerUserType);        
        return jo;
    }

    @Override
    public String getReqId() {
        return this.reqId;
    }

    @Override
    public String getPartnerId() {
        return this.partnerId;
    }

    @Override
    public void setReqeusetInfor(String reqUserName, Integer reqUserType, String reqEmail, String reqCmCode) {
        this.reqUserName = reqUserName;
        this.reqUserType = reqUserType;
        this.reqEmail = reqEmail;
        this.reqCmCode = reqCmCode;
    }

    @Override
    public void setPartnerInfor(String partnerUserName, Integer partnerUserType, String partnerEmail, String partnerCmCode) {
        this.partnerUserName = partnerUserName;
        this.partnerUserType = partnerUserType;
        this.partnerEmail = partnerEmail;
        this.partnerCmCode = partnerCmCode;
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
