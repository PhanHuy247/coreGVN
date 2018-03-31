/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople.pojos.entity;

import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import com.vn.ntsc.presentationserver.meetpeople.pojos.Constants;

/**
 *
 * @author Administrator
 */
public class QuerySetting {
    
    public ArrayList<Long> showme;
    public int interest;
    public int lower_age;
    public int upper_age;
    public ArrayList<Long> ethnics;
    public ArrayList<Long> location;
    public int distance;
    public String email;

    public QuerySetting() {
        showme = new ArrayList<>();
        for(int i = 0; i < Constants.MAX_SHOW_ME; i ++){
            showme.add((long)i);
        }
        interest = -1;
        lower_age = 18;
        upper_age = 120;
        ethnics = new ArrayList<>();
        location = new ArrayList<>();
        distance = 4;
    }
    
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
        jo.put("show_me", showme);
//        jo.put(Constant.INTERESTED_IN, interest);
        jo.put("lower_age", lower_age);
        jo.put("upper_age", upper_age);
//        jo.put(Constant.ETHNICITY, ethnics);
        jo.put(ParamKey.DISTANCE, distance);
        jo.put(ParamKey.REGION, location);
        return jo;
    }

    public QuerySetting(ArrayList<Long> showme, Integer interest, int lower_age, int upper_age, ArrayList<Long> ethnics, int distance, ArrayList<Long> location) {
        this.showme = showme;
        this.interest = -1;
        this.lower_age = lower_age;
        this.upper_age = upper_age;
        if(ethnics == null) ethnics = new ArrayList<Long>();
        if(location == null) location = new ArrayList<Long>();        
        this.ethnics = ethnics;
        this.distance = distance;
        this.location = location;
    }    
    
    public String toString(){
        return "showme = " + showme + "\n" +
               "interest = " + interest + "\n" + 
               "lower_age = " + lower_age + "\n" +
               "upper_age = " + upper_age + "\n" +
               "ethnics = " + ethnics + "\n" + 
               "distance = " + distance + "\n"+
                "location = " + location;
                
    }
}
