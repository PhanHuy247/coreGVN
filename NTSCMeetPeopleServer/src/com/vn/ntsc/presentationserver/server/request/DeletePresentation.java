/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

import java.util.ArrayList;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author tuannxv00804
 */
public class DeletePresentation {
    public String api;
    
    public ArrayList<String> list;

    public DeletePresentation() {
    }
    
    public DeletePresentation(String s) throws Exception{
        JSONObject jo = (JSONObject)(new JSONParser().parse(s));
        api = (String)(jo.get(ParamKey.API_NAME));
        list = (ArrayList<String>)(jo.get(ParamKey.LIST_USER));
    }
}
