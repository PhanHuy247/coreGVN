/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

import java.util.ArrayList;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Administrator
 */
public class UpdateDistance {
    public String api;
    public ArrayList<Double> distance;

    public UpdateDistance() {
    }

    public UpdateDistance(String request) throws Exception{
        JSONObject obj = (JSONObject) new JSONParser().parse(request);
        this.api = (String) obj.get(ParamKey.API_NAME);
        JSONArray arr = (JSONArray) obj.get(ParamKey.DISTANCE);
        distance = new ArrayList<Double>();
        if(arr != null && !arr.isEmpty()){
            for (Object arr1 : arr) {
                distance.add(Double.parseDouble(arr1.toString()));
            }
        }
    }    
    
    public UpdateDistance(String api, ArrayList<Double> distance) {
        this.api = api;
        this.distance = distance;
    }
    
}
