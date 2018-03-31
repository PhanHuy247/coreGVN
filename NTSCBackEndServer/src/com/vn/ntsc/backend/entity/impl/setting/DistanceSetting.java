/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.setting;

import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class DistanceSetting implements IEntity{

    private static final String nearKey = "near";
    public double near;
    
    private static final String cityKey = "city";
    public double city;
    
    private static final String stageKey = "state";
    public double stage;
    
    private static final String countryKey = "country";
    public double country;
    
    private static final String localBuzzKey = "local_buzz";
    public double localBuzz;  
    
    private static final Map<String, Integer> keys = new TreeMap<String, Integer>();
    static{
        keys.put(nearKey, 4);
        keys.put(cityKey, 5);
//        keys.put(stageKey, 6);
        keys.put(countryKey, 7);
        keys.put(localBuzzKey, 8);
    }
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        jo.put(nearKey, this.near);
        jo.put(cityKey, this.city);
//        jo.put(stageKey, this.stage);
        jo.put(countryKey, this.country);
        jo.put(localBuzzKey, this.localBuzz);   
        
        return jo;
    }

    public static int validate(JSONObject obj){
        for(Map.Entry<String, Integer> pair : keys.entrySet()){
            try{
                Double d = Util.getDoubleParam(obj, pair.getKey());
                if(d == null || d <= 0 || d > 100000)
                    return pair.getValue();
            }catch(Exception ex){
                return pair.getValue();
            }
        }
        return 0;
    }
    
    public static DistanceSetting createDistanceSetting (JSONObject obj){
        DistanceSetting ds = new DistanceSetting();
        Double near = Util.getDoubleParam(obj, nearKey);
        ds.near = near;

        Double city = Util.getDoubleParam(obj, cityKey);
        ds.city = city;

        ds.stage = 0.0;

        Double country = Util.getDoubleParam(obj, countryKey);
        ds.country = country;

        Double localBuzz = Util.getDoubleParam(obj, localBuzzKey);
        ds.localBuzz = localBuzz;
            
        return ds;
    }
    
    
}
