/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity;

import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public abstract class TwoObjectLog {
   
//    protected static List<String> headers;
//    protected static List<String> japaneseHeader;
//    protected static List<String> englishHeader;
//    protected static JSONObject jsonEnglishType;
//    protected static JSONObject jsonJapaneseType;
// 
//    public List<String> getKeys() {
//        return headers;
//    }    
//    
//    public List<String> getHeaders(Integer type) {
//        if(type != null && type == 1)
//            return englishHeader;
//        else
//            return japaneseHeader;
//    }
//
//    public JSONObject getJsonType(Integer type) {
//        if(type != null && type == 1)
//            return jsonEnglishType;
//        else
//            return jsonJapaneseType;
//    }     
    public abstract List<String> getKeys();   
    
    public abstract List<String> getHeaders(Integer type);

    public abstract JSONObject getJsonType(Integer type);
    
    public TwoObjectLog() {
    }
    
    public abstract String getReqId();

    public abstract void setReqeusetInfor(String reqUserName, Integer reqUserType, String reqEmail, String reqCmCode);

    public abstract String getPartnerId();

    public abstract void setPartnerInfor(String partnerUserName, Integer partnerUserType, String partnerEmail, String partnerCmCode);

}
