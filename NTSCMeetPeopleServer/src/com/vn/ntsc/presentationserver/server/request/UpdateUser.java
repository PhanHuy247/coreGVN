/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;


import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author tuannxv00804
 */
public class UpdateUser {
    public String api;
    public Integer callWaiting;
    public Integer age;
    public Double lon;
    public Double lat;
    public Boolean isOnline;
    public String email;
    public String username;
    public String avataID;
    public Integer location;
    public String bir;
    public Integer bodyType;
    public Boolean isVideo;
    public Boolean isVoice;
    
    public UpdateUser(){
        
    }
    
    public UpdateUser(String s) throws Exception{
        JSONObject jo = (JSONObject)(new JSONParser().parse(s));
        api = (String)jo.get(ParamKey.API_NAME);
        
        Long loc = (Long) jo.get(ParamKey.REGION);
        if(loc != null)
            location = loc.intValue();
        else 
            location = null;
        isVideo = (Boolean)(jo.get(ParamKey.VIDEO_CALL_WAITING));
        isVoice = (Boolean)(jo.get(ParamKey.VOICE_CALL_WAITING));
        
        String birthday = ((String)(jo.get(ParamKey.BIRTHDAY)));
        if(birthday != null){
            bir = birthday;
        }
        
        Long AGE = ((Long)(jo.get("age")));
        if(AGE != null){
            age = AGE.intValue();
        }
        
        else age = null;
        Object LON = (jo.get(ParamKey.LONGITUDE));
        if(LON != null)
            lon = Double.parseDouble(LON.toString());
        else
            lon = null;
        Object LAT = (jo.get(ParamKey.LATITUDE));
        if(LAT != null)
            lat = Double.parseDouble(LAT.toString());
        else
            lat = null;
        isOnline = (Boolean)(jo.get(ParamKey.IS_ONLINE));
        email = (String)(jo.get(ParamKey.USER_ID));
        username = (String)(jo.get(ParamKey.USER_NAME));
        avataID = (String)(jo.get(ParamKey.IMAGE_ID));
        Long bodyTypeObj = (Long)(jo.get(ParamKey.BODY_TYPE));
        if (bodyTypeObj != null){
            bodyType = bodyTypeObj.intValue();
        }
    }

    public UpdateUser( int callWaiting, int age, double lon, double lat, boolean isOnline, String email, String username, String avataID, String bir) {
        this.api = API.REGISTER;
        this.callWaiting = callWaiting;
        this.age = age;
        this.lon = lon;
        this.lat = lat;
        this.isOnline = isOnline;
        this.email = email;
        this.username = username;
        this.avataID = avataID;
        this.bir = bir;
    }
    
}
