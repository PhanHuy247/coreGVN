/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class GetUserOnline {
    public String api;
    public int skip;
    public int take;

    public GetUserOnline() {
        this.api = API.GET_USER_ONLINE;
    }
    
    public GetUserOnline(String s) throws Exception{
        JSONObject jo = (JSONObject)(new JSONParser().parse(s));
        api = (String)(jo.get(ParamKey.API_NAME));
        skip = ((Long)(jo.get(ParamKey.SKIP))).intValue();
        take = ((Long)(jo.get(ParamKey.TAKE))).intValue();
    }
    
}
