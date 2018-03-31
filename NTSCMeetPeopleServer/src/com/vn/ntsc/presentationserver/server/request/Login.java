/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author tuannxv00804
 */
public class Login {
    public String api;
    
    public String email;

    public Login() {
    }
    
    public Login(String s) throws Exception{
        JSONObject jo = (JSONObject)(new JSONParser().parse(s));
        api = (String)(jo.get(ParamKey.API_NAME));
        email = (String)(jo.get(ParamKey.USER_ID));
    }

    public Login(String api, String email) {
        this.api = api;
        this.email = email;
    }

}
