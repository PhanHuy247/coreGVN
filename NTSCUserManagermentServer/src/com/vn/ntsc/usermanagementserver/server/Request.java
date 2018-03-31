/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server;

import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import eazycommon.util.Util;

/**
 *
 * @author tuannxv00804
 */
public class Request {
    public JSONObject reqObj;
    public String api;
    public String token;
    public String userAgent;
    
    public Request(){}
    
    public static Request initRequest( String requestStr ){
        try{
            JSONParser parser = new JSONParser();
            JSONObject jo = (JSONObject) parser.parse( requestStr );
            
            Request r = new Request();
            r.api = (String) jo.get( ParamKey.API_NAME );
            r.token = (String) jo.get( ParamKey.TOKEN_STRING );
            r.reqObj = jo;
            
            return r;
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
            return null;
        }
    }
    
    public void put( String key, String value ){
        this.reqObj.put( key, value );
    }
    
    public boolean contain( String key ){
        return this.reqObj.containsKey(key);
    }    
    
    public Object getParamValue( String key ){
        return this.reqObj.get( key );
    }
    
    public String toJson(){
        return reqObj.toJSONString();
    }
    
    @Override
    public String toString(){
        return toJson();
    }
    
    public static void main( String[] args ) {
        JSONObject jo = new JSONObject();
        jo.put( "name", "tuan" );
        jo.put( "age", 123 );
        String str = jo.toJSONString();
        
        Request r = new Request();
        r.reqObj = jo;
    }
}