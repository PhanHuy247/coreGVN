/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

import java.util.LinkedList;
import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author tuannxv00804
 */
public class GetUsersPresentation {
    public String api;
    public LinkedList<String> llUserEmail;
    public String userId;
    
    public GetUsersPresentation(){
        
    }

    public GetUsersPresentation(String s) throws Exception{
        JSONObject jo = (JSONObject)(new JSONParser().parse(s));
        api = (String)(jo.get(ParamKey.API_NAME));
        JSONArray jarr = (JSONArray)(jo.get(ParamKey.LIST_EMAIL));
        llUserEmail = new LinkedList<String>();
        
        for(int i = 0; i < jarr.size(); i++){
            llUserEmail.add((String)(jarr.get(i)));
        }
        
        userId = (String)(jo.get(ParamKey.USER_ID));
    }
    
    public GetUsersPresentation(LinkedList<String> llUserEmail, String userId) {
        this.api = API.GET_USER_PRESENTATION;
        this.llUserEmail = llUserEmail;
        this.userId = userId;
    }
    
    
}
