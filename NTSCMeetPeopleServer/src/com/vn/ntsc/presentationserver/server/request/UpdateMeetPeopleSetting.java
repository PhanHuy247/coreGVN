/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

import java.util.ArrayList;
import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author tuannxv00804
 */
public class UpdateMeetPeopleSetting {
    public String api;
    public String email;
    public ArrayList<Long> showme;
//    public int interest;
    public int lower_age;
    public int upper_age;
    public ArrayList<Long> ethnics;
    public ArrayList<Long> location;
    public int distance;
    
    
    public UpdateMeetPeopleSetting(String s) throws Exception{
        JSONObject jo = (JSONObject)((new JSONParser()).parse(s));
        
        api = (String)(jo.get(ParamKey.API_NAME));
        email = (String)(jo.get(ParamKey.USER_ID));
        showme = ((ArrayList<Long>)(jo.get("show_me")));
//        interest = -1;
        lower_age = ((Long)(jo.get("lower_age"))).intValue();
        upper_age = ((Long)(jo.get("upper_age"))).intValue();
//        ethnics = (ArrayList<Long>)(jo.get(Constant.ETHNICITY));
        location = (ArrayList<Long>)(jo.get(ParamKey.REGION));
        distance = ((Long)(jo.get(ParamKey.DISTANCE))).intValue();
    }

    public UpdateMeetPeopleSetting(String email, ArrayList<Long> showme, int lower_age, int upper_age, ArrayList<Long> ethnics, int distance) {
        this.api = API.UPDATE_MEET_PEOPLE_SETTING;
        this.email = email;
        this.showme = showme;
//        this.interest = -1;
        this.lower_age = lower_age;
        this.upper_age = upper_age;
//        this.ethnics = ethnics;
        this.distance = distance;
    }

    
}
