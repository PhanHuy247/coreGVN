/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server;

import eazycommon.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author DuongLTD
 */
public class Handler extends AbstractHandler {

    public void handle(String string, Request request, HttpServletRequest hsr, HttpServletResponse response) throws IOException, ServletException {
        request.setHandled(true);
        response.setContentType("text/plain;charset=UTF-8");
        OutputStream out = response.getOutputStream();
//        String inputString  = string.substring(1);
        InputStreamReader isr = new InputStreamReader(request.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        String inputString = reader.readLine();
        Date time = Util.getGMTTime();
//        System.out.println(time.toString() + " UMSHandler.handle() --> inputString : " + inputString);
        Util.addDebugLog("UMSHandler.handle() --> inputString : " + inputString);  
        String outputString = null;
        if (inputString != null) {
            try{
                JSONObject json = (JSONObject) new JSONParser().parse(inputString);
                String api = Util.getStringParam(json, ParamKey.API_NAME);
                Util.addDebugLog("UMSHandler.handle() --> api : " + api);  
                if(api != null){
                    IApiAdapter adapter = APIManager.getApi(api);
                    if(adapter != null){
                        Respond res = adapter.execute(json, time);
                        outputString = res.toString();
                    }
                }
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
        }
        if(outputString == null)
            outputString = ResponseMessage.BadResquestMessage;
        Util.addDebugLog("UMSHandler.handle() --> outString " + outputString);            
        out.write(outputString.getBytes());
        out.flush();
        out.close();        
    }

//    private static final String BadRequestMessage;
//
//    static {
//        JSONObject obj = new JSONObject();
//        obj.put(ParamKey.ERROR_CODE, ErrorCode.WRONG_DATA_FORMAT);
//        BadRequestMessage = obj.toJSONString();
//    }
}
