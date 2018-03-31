/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author DuongLTD
 */
public class Handler extends AbstractHandler {

    @Override
    public void handle( String string, Request request, HttpServletRequest hsr, HttpServletResponse response ) throws IOException, ServletException {
        request.setHandled( true );
        response.setContentType("text/plain;charset=UTF-8");
        
        InputStreamReader isr = new InputStreamReader( request.getInputStream() );
        BufferedReader reader = new BufferedReader( isr );
        string = reader.readLine();
        Date time = new Date();
        try{
            time = Util.getGMTTime();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
        Util.addDebugLog("request : " + string);
        System.out.println("request : " + string);
        JSONParser parser = new JSONParser();
        JSONObject jo = null;
        try {
            jo = (JSONObject) parser.parse( string );
        } catch( ParseException ex ) {
            Util.addErrorLog(ex);            
           
            jo = null;
        }
        if( jo == null ) {
            dataBack(ResponseMessage.BadResquestMessage, response, time, null);
        }

        String api = null;
        try {
            api = (String) jo.get( ParamKey.API_NAME );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
            api = null;
        }
        if( api == null ) {
            dataBack(ResponseMessage.BadResquestMessage, response, time, jo);
        }
        
        if(api.equals(API.GET_STATIC_PAGE)
                || api.equals(API.UPDATE_STATIC_PAGE)
                || api.equals(API.REGISTER_BY_ADMIN)
                || api.equals(API.UPDATE_USER_INF_BY_ADMIN)){
            String output = null;
            PermissionChecker checker = new PermissionChecker(jo);
            if(checker.validate()){
                output = ResponseMessage.SuccessMessage;
            }else{
                output = ResponseMessage.PermissionDenied;
            }
            
            dataBack(output, response, time, jo);
            return;
        }

        IApiAdapter toolApi = APIManager.getToolApi( api );
        if(toolApi != null){
            Respond result = toolApi.execute( jo );
            String output = null;
            if(result == null)
                output = ResponseMessage.BadResquestMessage;
            else
                output = result.toJsonObject().toString();

            dataBack(output, response, time, jo);
            return;
        }


        IApiAdapter normalApi = APIManager.getApi(api);
        if(normalApi != null){
            if(api.equals(API.CLICK_NEWS_NOTIFICATION)){
                Respond result = normalApi.execute( jo );
            }
            else{
                String output = null;
                PermissionChecker checker = new PermissionChecker(jo);
                if(checker.validate()){
                    Respond result = normalApi.execute( jo );
                    if(result == null)
                        output = ResponseMessage.BadResquestMessage;
                    else
                        output = result.toJsonObject().toString();
                }else{
                    output = ResponseMessage.PermissionDenied;
                }
            
                dataBack(output, response, time, jo);
                return;
            }
            
        }
        
        dataBack(ResponseMessage.BadResquestMessage, response, time, jo);
    }

    private void dataBack( String result, HttpServletResponse response, Date time, JSONObject jo) {
        try{
            try (OutputStream out = response.getOutputStream()) {
                if( result == null ){
                    out.write(ResponseMessage.UnknownError.getBytes());
                    out.flush();
                    out.close();
                    Util.addDebugLog("result = null");
                    return;
                }
//                if(jo != null && !jo.containsKey(ParamKey.CSV)){
                Util.addDebugLog("Respond : " + result);
//                }
                out.write( result.getBytes());
                out.flush();
            }
        } catch( IOException ex ) {
            Util.addErrorLog(ex);            
           
        }
    }    
    
}
