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
public class AutoMessage {
    public String api;
    public int number;
    
    public AutoMessage() {
        this.api = API.AUTO_MESSAGE;
        number = 0;
    }
    
    public AutoMessage(String s) throws Exception{
        JSONObject jo = (JSONObject)(new JSONParser().parse(s));
        api = (String)(jo.get(ParamKey.API_NAME));
        number = ((Long)(jo.get("num_user"))).intValue();
    }
    public AutoMessage(int number){
        this.api = API.AUTO_MESSAGE;
        this.number = number;
    }
    
}
