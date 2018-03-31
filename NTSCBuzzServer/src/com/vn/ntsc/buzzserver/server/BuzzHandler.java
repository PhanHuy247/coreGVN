/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server;

import com.vn.ntsc.buzzserver.server.respond.APIManager;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author DUONGLTD
 */
public class BuzzHandler extends AbstractHandler{

    @Override
    public void handle(String string, Request request, HttpServletRequest hsr, HttpServletResponse response) throws IOException, ServletException {
        request.setHandled(true);
        response.setContentType("text/plain;charset=UTF-8");
        OutputStream out = response.getOutputStream();
//        String inputString  = string.substring(1);
        Date time = Util.getGMTTime();
        InputStreamReader isr = new InputStreamReader( request.getInputStream() );
        BufferedReader reader = new BufferedReader( isr );
        String inputString = reader.readLine();
        
        Util.addDebugLog("BuzzHandler.handle() --> inputString : "+ inputString);
        String outputString = null;
        if (inputString != null) {
            try{
                JSONObject json = (JSONObject) new JSONParser().parse(inputString);
                String api = Util.getStringParam(json, ParamKey.API_NAME);
                if(api != null){
                    IApiAdapter adapter = APIManager.getApi(api);
                    if(adapter != null){
                        Respond res = adapter.execute(json, time.getTime());
                        outputString = res.toString();
                    }
                }
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
        }
        if(outputString == null)
            outputString = ResponseMessage.BadResquestMessage;
        Util.addDebugLog("BuzzHandler.handle() --> outString " + outputString);            
        out.write(outputString.getBytes());
        out.flush();
        out.close(); 
    }
}
