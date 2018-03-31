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
public class LogCall extends TwoObjectLog implements IEntity{

    private static final List<String> headers = new ArrayList<String>();
    private static final List<String> japaneseHeader = new ArrayList<String>();
    private static final List<String> englishHeader = new ArrayList<String>();
    private static final JSONObject jsonEnglishType = new JSONObject();
    private static final JSONObject jsonJapaneseType = new JSONObject();     
    
    private static final String reqIdKey = "req_id";
    @TypeSwitch(header = Headers.call_user_id)
    public String reqId;

    private static final String reqUserNameKey = "req_user_name";
    @TypeSwitch(header = Headers.call_user_name)
    public String reqUserName;

    private static final String reqUserTypeKey = "req_user_type";
    @TypeSwitch(value = ParamKey.USER_TYPE, header = Headers.call_user_type)
    public Integer reqUserType;    
    
    private static final String reqEmailKey = "req_email";
    @TypeSwitch(header = Headers.call_user_email)    
    public String reqEmail;
    
    @TypeSwitch(header = Headers.call_user_group)    
    public String reqGroup;
    
    private static final String reqCmCodeKey = "req_cm_code";
    @TypeSwitch(header = Headers.call_user_cm_code)    
    public String reqCmCode;    
    
    private static final String ipKey = "ip";
    @TypeSwitch(header = Headers.call_user_ip)    
    public String ip;    

    private static final String partnerIdKey = "partner_id";
    @TypeSwitch(header = Headers.called_user_id)    
    public String partnerId;

    private static final String partnerUserNameKey = "partner_user_name";
    @TypeSwitch(header = Headers.called_user_name)     
    public String partnerUserName;

    private static final String partnerUserTypeKey = "partner_user_type";
    @TypeSwitch(value = ParamKey.USER_TYPE, header = Headers.called_user_type)
    public Integer partnerUserType;     
    
    private static final String partnerEmailKey = "partner_email";
    @TypeSwitch(header = Headers.called_user_email)     
    public String partnerEmail;    
    
    @TypeSwitch(header = Headers.called_user_group)     
    public String partnerGroup;
    
    private static final String partnerCmCodeKey = "partner_cm_code";
    @TypeSwitch(header = Headers.called_user_cm_code)     
    public String partnerCmCode;    
 
    private static final String startTimetimeKey = "start_time";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.call_start_time)    
    public String startTime;
 
    private static final String durationKey = "duration";
    @TypeSwitch(header = Headers.call_duration)     
    public Long duration;    
    
    private static final String callTypeKey = "call_type";
    @TypeSwitch(value = ParamKey.TYPE, header = Headers.call_type)     
    public Integer callType;
    
    //Thanhdd add #5124
    private static final String partnerResponsedKey = "partner_respond";
    @TypeSwitch(value = ParamKey.PARTNER_RESPONSED, header = Headers.partner_respond)
    public Integer partnerResponsed;
    
    private static final String finishTypeKey = "finish_type";
    @TypeSwitch(value = ParamKey.FINISH_TYPE, header = Headers.finish_type)
    public Integer finishType;
    static{
        initHeader();
        initType();
    }
   
    private static void initType(){
        
//        jsonEnglishType = new JSONObject();
//        jsonJapaneseType = new JSONObject();
//        
        JSONObject value = new JSONObject();
        //user type
        value.putAll(TypeValue.en_user_type);
        jsonEnglishType.put(ParamKey.USER_TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_user_type);
        jsonJapaneseType.put(ParamKey.USER_TYPE, value);
        
        //block type
        value = new JSONObject();
        value.putAll(TypeValue.en_call_type);
        jsonEnglishType.put(ParamKey.TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_call_type);
        jsonJapaneseType.put(ParamKey.TYPE, value);        
        
    }
    
    private static void initHeader(){
        
//        japaneseHeader = new ArrayList<String>();
//        englishHeader = new ArrayList<String>();
                
        List<String> keys = new ArrayList<String>();
        keys.add(Headers.number);
        keys.add(Headers.call_user_id);
        keys.add(Headers.call_user_name);
        keys.add(Headers.call_user_type);
        keys.add(Headers.call_user_email);
        keys.add(Headers.call_user_group);
        keys.add(Headers.call_user_cm_code);
        keys.add(Headers.call_user_ip);
        keys.add(Headers.called_user_id);
        keys.add(Headers.called_user_name);
        keys.add(Headers.called_user_type);
        keys.add(Headers.called_user_email);
        keys.add(Headers.called_user_group);
        keys.add(Headers.called_user_cm_code);
        keys.add(Headers.call_start_time);
        keys.add(Headers.call_duration);
        keys.add(Headers.call_type);
        keys.add(Headers.partner_respond);//thanhdd add
        keys.add(Headers.finish_type);//thanhdd add
        headers.addAll(keys);
        
//        headers = keys;
        
        HeaderCreator.createHeader(japaneseHeader, englishHeader, keys);
      
    }    
    
    public LogCall() {
    }
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.reqId != null) {
            jo.put(reqIdKey, this.reqId);
        }
        if (this.reqEmail != null) {
            jo.put(reqEmailKey, this.reqEmail);
        }
        if (this.startTime != null) {
            jo.put(startTimetimeKey, this.startTime);
        }
        if (this.reqUserName != null) {
            jo.put(reqUserNameKey, this.reqUserName);
        }
        if(this.reqCmCode != null){
            jo.put(reqCmCodeKey, this.reqCmCode);
        }
        if (this.partnerEmail != null) {
            jo.put(partnerEmailKey, this.partnerEmail);
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
        if (this.callType != null) {
            jo.put(callTypeKey, this.callType);
        }
        //thanhdd add
        if (this.partnerResponsed != null) {
            jo.put(partnerResponsedKey, this.partnerResponsed);
        }
         if (this.finishType != null) {
            jo.put(finishTypeKey, this.finishType);
        }
        //end
        if(this.ip != null){
            jo.put(ipKey, ip);
        }
        if(this.reqUserType != null)
            jo.put(reqUserTypeKey, this.reqUserType);
        if(this.partnerUserType != null)
            jo.put(partnerUserTypeKey, this.partnerUserType);
        if(this.duration != null)
            jo.put(durationKey, this.duration);
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
