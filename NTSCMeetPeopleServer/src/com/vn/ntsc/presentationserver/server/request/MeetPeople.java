/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

import java.util.ArrayList;
import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author tuannxv00804
 */
public class MeetPeople {

    public String api;
    public int lowerAge;
    public int upperAge;
    public Double lon;
    public Double lat;
    public int distance;
    public int skip;
    public int take;
    public String email;
    public Integer gender;
    public ArrayList<Long> location;
    public boolean isNewLogin;
    public int sortType;
    public int filter;
    public String body_type;
    public int is_avatar;
    public boolean is_interacted;
    public boolean isCheck;
    public boolean isCheckFemale;

    public MeetPeople() {
    }

    public MeetPeople(String s) throws Exception {
        try {
            JSONObject jo = (JSONObject) (new JSONParser().parse(s));
            api = (String) (jo.get(ParamKey.API_NAME));
            lowerAge = ((Long) (jo.get("lower_age"))).intValue();
            upperAge = ((Long) (jo.get("upper_age"))).intValue();
            if (jo.get("body_type") != null) {
                body_type = (String) jo.get("body_type");
            }
            isNewLogin = (Boolean) (jo.get("is_new_login"));
            
//            isCheck = (Boolean) (jo.get("isCheck"));
//            isCheckFemale = (Boolean) (jo.get("isCheckFemale"));
            
            if (jo.get("is_avatar") != null) {
                is_avatar = ((Long) (jo.get("is_avatar"))).intValue();
            } else {
                is_avatar = -1;
            }
            filter = ((Long) (jo.get("filter"))).intValue();
            location = (ArrayList<Long>) (jo.get(ParamKey.REGION));
            Object LON = (jo.get(ParamKey.LONGITUDE));
            if (LON != null) {
                lon = Double.parseDouble(LON.toString());
            } else {
                lon = null;
            }
            Object LAT = (jo.get(ParamKey.LATITUDE));
            if (LAT != null) {
                lat = Double.parseDouble(LAT.toString());
            } else {
                lat = null;
            }
            distance = ((Long) (jo.get(ParamKey.DISTANCE))).intValue();
            skip = ((Long) (jo.get(ParamKey.SKIP))).intValue();
            take = ((Long) (jo.get(ParamKey.TAKE))).intValue();
            email = (String) (jo.get(ParamKey.USER_ID));
            sortType = ((Long) (jo.get("sort_type"))).intValue();
            is_interacted = (Boolean) (jo.get("is_interacted"));
            Long gen = (Long) (jo.get(ParamKey.GENDER));
            if (gen != null){
                this.gender = gen.intValue();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public MeetPeople(int lowerAge, int upperAge, int filter, boolean isNewLogin,
            double lon, double lat, int distance, int skip, int take, String email, int sortType, String body_type, int is_avatar) {
        this.api = API.MEET_PEOPLE;
        this.isNewLogin = isNewLogin;
        this.filter = filter;
        this.lowerAge = lowerAge;
        this.upperAge = upperAge;
        this.lon = lon;
        this.lat = lat;
        this.distance = distance;
        this.skip = skip;
        this.take = take;
        this.email = email;
        this.sortType = sortType;
        this.body_type = body_type;
        this.is_avatar = is_avatar;
    }

}
