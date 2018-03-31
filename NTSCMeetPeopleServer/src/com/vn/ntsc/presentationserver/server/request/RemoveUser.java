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
 * @author tuannxv00804
 */
public class RemoveUser {
    public String api;
    
    public String email;

    public RemoveUser() {
    }
    
    public RemoveUser(String s, int number) throws Exception{
        JSONObject jo = (JSONObject)(new JSONParser().parse(s));
        api = (String)(jo.get(ParamKey.API_NAME));
        email = (String)(jo.get(ParamKey.USER_ID));
    }

    public RemoveUser(String api, String email) {
        this.api = api;
        this.email = email;
    }

    public RemoveUser(String email) {
        api = API.DEACTIVATE;
        this.email = email;
    }

}
