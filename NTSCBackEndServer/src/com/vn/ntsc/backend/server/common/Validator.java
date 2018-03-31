/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.common;

import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.util.Util;

/**
 *
 * @author tuannxv00804
 */
public class Validator {
    
    public static int validate(JSONObject obj, Map<String, Integer> keys){
        for(Map.Entry<String, Integer> pair : keys.entrySet()){
            try{
                Object value = obj.get(pair.getKey());
                if(value == null)
                    return pair.getValue();
                if(value instanceof String){
                    if(value.toString().isEmpty())
                        return pair.getValue();
                }
            }catch(Exception ex){
                Util.addErrorLog(ex);                
                return pair.getValue();
            }
        }
        return 0;
    }
}