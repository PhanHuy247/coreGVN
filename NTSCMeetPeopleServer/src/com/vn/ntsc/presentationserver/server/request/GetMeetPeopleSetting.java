/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Administrator
 */
public class GetMeetPeopleSetting {
    
    public String api;
    public String email;

    public GetMeetPeopleSetting() {
    }

    public GetMeetPeopleSetting(String s, String t) throws Exception{
        JSONObject jo = (JSONObject)(new JSONParser().parse(s));
        api = (String)(jo.get(ParamKey.API_NAME));
        email = (String)(jo.get(ParamKey.USER_ID));
    }
    
    public GetMeetPeopleSetting(String email) {
        this.api = API.GET_MEET_PEOPLE_SETTING;
        this.email = email;
    }
    
}
