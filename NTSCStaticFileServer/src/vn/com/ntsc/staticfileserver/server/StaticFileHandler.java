/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server;

import vn.com.ntsc.staticfileserver.server.session.SessionManager;
import vn.com.ntsc.staticfileserver.server.session.Session;
import vn.com.ntsc.staticfileserver.server.respond.APIManager;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.Constant;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eazycommon.constant.API;
import eazycommon.util.Util;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class StaticFileHandler extends AbstractHandler {

    private static final String FALSE_RESULT = String.valueOf(false);

    @Override
    public void handle(String string, org.eclipse.jetty.server.Request request, HttpServletRequest hsr, HttpServletResponse response) throws IOException, ServletException {
        request.setHandled(true);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        Util.addDebugLog("DEFAULT STRING " + string);
        Request requestObj;
        byte[] result;

        Date time = new Date();
        try {
            time = Util.getGMTTime();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
//        Util.addDebugLog("LongLT ============================= StaticFileHandler contentType " + request.getContentType());
        if (request.getContentType() != null && request.getContentType().contains("multipart")) {
            Integer maxFileSize = Setting.max_file_size;
            if(maxFileSize == 0){
                maxFileSize = 1;
            }
            Integer totalFileSize = Setting.total_file_size;
            if(totalFileSize == 0){
                totalFileSize = 1;
            }
            
            request.setAttribute(org.eclipse.jetty.server.Request.__MULTIPART_CONFIG_ELEMENT, new MultipartConfigElement(
                    System.getProperty("java.io.tmpdir"), maxFileSize*1000, totalFileSize*1000, Setting.file_size_threshold));
            Collection<Part> partList = null;
            try{
                partList = hsr.getParts();
            }catch(Exception ex){
                Util.addErrorLog(ex);
                Respond respond = new Respond(ErrorCode.MAX_FILE_SIZE);
                dataBack(response, respond.toJsonObject().toJSONString(), time);
                return ;
            }
            
            Util.addDebugLog("LongLT ============================= StaticFileHandler partList.size() " + partList.size());
            
            if (partList.size() > 0) {
                Respond respond;
                String api = hsr.getParameter(ParamKey.API_NAME);
                String andGrespondString = checkToken(hsr.getParameter(ParamKey.TOKEN_STRING));
                Util.addDebugLog("andGrespondString=============================="+andGrespondString);
                AndGRespond andGRespond = AndGRespond.initRespond(andGrespondString);
                if(andGRespond.getResult().equals("true")){
                    requestObj = new Request();
                    requestObj.parts = partList;
                    IApiAdapter adapter;
                    Util.addDebugLog("api===="+api);
                    if(api == null || api.equals(API.ADD_STATUS) || api.equals(API.ADD_BUZZ)){
                        adapter = APIManager.getApi(API.ADD_BUZZ);
                        respond = adapter.execute(requestObj, time);
                    }else if(api.equals(API.UPLOAD_FILE)){
                        adapter = APIManager.getApi(API.UPLOAD_FILE);
                        respond = adapter.execute(requestObj, time);
                    }else if(api.equals(API.UPLOAD_STREAM_FILE)){
                        adapter = APIManager.getApi(API.UPLOAD_STREAM_FILE);
                        respond = adapter.execute(requestObj, time);
                    }else if(api.equals(API.ADD_ALBUM_IMAGE)){
                        adapter = APIManager.getApi(API.ADD_ALBUM_IMAGE);
                        respond = adapter.execute(requestObj, time);
                    }else if(api.equals(API.ADD_ALBUM_AND_IMAGE)){
                        adapter = APIManager.getApi(API.ADD_ALBUM_AND_IMAGE);
                        respond = adapter.execute(requestObj, time);
                    }else if(api.equals(API.ADD_EMOJI_CAT)){
                        adapter = APIManager.getApi(API.ADD_EMOJI_CAT);
                        respond = adapter.execute(requestObj, time);
                    }else if(api.equals(API.EDIT_EMOJI_CAT)){
                        adapter = APIManager.getApi(API.EDIT_EMOJI_CAT);
                        respond = adapter.execute(requestObj, time);
                    }else if(api.equals(API.ADD_EMOJI)){
                        adapter = APIManager.getApi(API.ADD_EMOJI);
                        respond = adapter.execute(requestObj, time);
                    }else if(api.equals(API.EDIT_EMOJI)){
                        adapter = APIManager.getApi(API.EDIT_EMOJI);
                        respond = adapter.execute(requestObj, time);
                    }else{
                        respond = new Respond(ErrorCode.WRONG_DATA_FORMAT);
                    }
                }else{
                    if(andGRespond.getNewToken() == null || andGRespond.getNewToken() == ""){
                        respond = new Respond(ErrorCode.TOKEN_NOT_EXIST_DB);
                    }else{
                        respond = new EntityRespond(ErrorCode.INVALID_TOKEN, andGRespond.getNewToken(),null);
                    }
                }
                Util.addDebugLog("respond : " + respond.toJsonObject().toJSONString());
                dataBack(response, respond.toJsonObject().toJSONString(), time);
                return ;
            }
        }else{
            String inputString = string.substring(1);

            if (string == null) {
                sendBadRequestNotification(response, time);
                return;
            }

            if (string.length() > 1) {
                requestObj = Request.initRequest(inputString, request.getInputStream(), hsr);
                Util.addDebugLog("STF.handle() --> inputString : " + inputString);
            } else {
                InputStreamReader isr = new InputStreamReader(request.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                inputString = br.readLine();
                Util.addDebugLog("STF.handle() --> inputString : " + inputString);
                requestObj = Request.initRequest(request.getInputStream(), inputString, hsr);
            }

            if (requestObj == null) {
                sendBadRequestNotification(response, time);
                return;
            }
            String api = requestObj.getApiName();
            if (api == null || api.isEmpty()) {
                sendBadRequestNotification(response, time);
                return;
            }
            String token = requestObj.getToken();
            if (token == null || token.isEmpty()) {
                if (api.equals(API.INSERT_GIFT)
                        || api.equals(API.UPDATE_GIFT_IMAGE)
                        || api.equals(API.UPDATE_STICKER_CATEGORY_IMAGE)
                        || api.equals(API.UPDATE_STICKER_IMAGE)
                        || api.equals(API.INSERT_STICKER)
                        || api.equals(API.UPLOAD_NEWS_BANNER)
                        || api.equals(API.LOAD_FILE)
                        || api.equals(API.LOAD_IMAGE)
                        || api.equals(API.LOAD_IMAGE_WITH_SIZE)
                        || api.equals(API.LOAD_STICKER_PACKAGE)
                        || api.equals(API.LOAD_LIST_IMAGE)
                        || api.equals(API.LOAD_LIST_VIDEO)
                        || api.equals(API.LOAD_LIST_AUDIO)
                        || api.equals(API.LOAD_LIST_FILE)
                        || api.equals(API.LOAD_FILE_DATA)
                        || api.equals(API.INSERT_STICKER_CATEGORY)
                        || api.equals(API.GET_UPLOAD_SETTING)
                        || api.equals(API.LIST_EMOJI)
                        || api.equals(API.LIST_EMOJI_CAT)
                        || api.equals(API.LIST_UPDATED_EMOJI_CAT)
                        || api.equals(API.LIST_UPDATED_STICKER_CAT)
                        || api.equals(API.GET_ALL_GIFT)
                        || api.equals(API.GET_ALL_GIFT_CATEGORY)) {
                    IApiAdapter adapter = APIManager.getApi(api);
                    if (adapter != null) {
                        Respond respond = adapter.execute(requestObj, time);
                        if (respond != null) {
                            result = respond.toByte();
                        } else {
                            result = ResponseMessage.BadResquestMessage.getBytes();
                        }
                    } else {
                        result = ResponseMessage.BadResquestMessage.getBytes();
                    }
                    if (result != null) {
                        response.setHeader("Content-Length", String.valueOf(result.length));
                        DataOutputStream dos = new DataOutputStream(response.getOutputStream());
                        dos.write(result);
                        dos.flush();
                        dos.close();
                        return;
                    } else {
                        sendBadRequestNotification(response, time);
                        return;
                    }
                }
                sendBadRequestNotification(response, time);
            } else {
                AndGRespond andGRespond;
                Session session = SessionManager.getSession(token);
                if (session == null) {
                    String andGrespondString = checkToken(token);
                    andGRespond = AndGRespond.initRespond(andGrespondString);
                    if (andGRespond.getResult().equals(FALSE_RESULT)) {
                        if (api.equals(API.UPLOAD_FILE_BY_CHAT)
                                || api.equals(API.UPLOAD_IMAGE_BY_CHAT)) {
                            Helper.returnFailedUploadPoint(requestObj.getMessageId(), requestObj.getIp());
                        }

                        JSONObject obj = new JSONObject();
                        obj.put(ParamKey.ERROR_CODE, ErrorCode.INVALID_TOKEN);
                        String str = obj.toJSONString();
                        response.setHeader("Content-Length", String.valueOf(str.getBytes().length));
                        DataOutputStream dos = new DataOutputStream(response.getOutputStream());
                        dos.write(str.getBytes());
                        dos.flush();
                        dos.close();
                        return;
                    } else {
                        String userId = andGRespond.getUserId();
                        session = new Session(token, userId);
                        SessionManager.putSession(session);
                        requestObj.setUserId(userId);
                    }
                }
                session.timeToLive = 0;
                requestObj.setUserId(session.userID);
                if(api.equals(API.UPDATE_STREAM_URL)
                        || api.equals(API.LIST_STICKER_URL)
                        || api.equals(API.EMOJI_DATA)
                        || api.equals(API.EMOJI_URL)
                        || api.equals(API.LIST_EMOJI_CAT)
                        || api.equals(API.LIST_EMOJI)
                        || api.equals(API.LIST_UPDATED_EMOJI_CAT)){
                    IApiAdapter adapter = APIManager.getApi(api);
                    Respond respond = adapter.execute(requestObj, time);
                    dataBack(response, respond.toJsonObject().toJSONString(), time);
                    return ;
                }else{
                    IApiAdapter adapter = APIManager.getApi(api);
                    if (adapter != null) {
                        Respond respond = adapter.execute(requestObj, time);
                        if (respond != null) {
                            result = respond.toByte();
                        } else {
                            result = ResponseMessage.BadResquestMessage.getBytes();
                        }
                    } else {
                        result = ResponseMessage.BadResquestMessage.getBytes();
                    }
                    if (result != null) {
                        response.setHeader("Content-Length", String.valueOf(result.length));
                        String contentType = "image/jpg";
                        if (api.equals(API.LOAD_FILE)) {
                            contentType = "video/mp4";
                        }
                        response.setHeader("Content-Type", contentType);
                        try (DataOutputStream dos = new DataOutputStream(response.getOutputStream())) {
                            dos.write(result);
                            dos.flush();
                        }
                    } else {
                        sendBadRequestNotification(response, time);
                    }
                }
            }
        }
    }

    private void sendBadRequestNotification(HttpServletResponse response, Date time) {
        try {
            response.setHeader("Content-Length", String.valueOf(ResponseMessage.BadResquestMessage.getBytes().length));
            DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            Util.addDebugLog("respond: " + ResponseMessage.BadResquestMessage);
            dos.write(ResponseMessage.BadResquestMessage.getBytes());
            dos.flush();
            dos.close();
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    private String checkToken(String token) {
        String result = " ";
        StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(Config.ANDG_IP);
        url.append(":");
        url.append(Config.ANDG_PORT);
        url.append("/");
        url.append("checktoken");
        url.append("=");
        url.append(token);
        result = sendGetRequest(url.toString());
        return result;

    }

    private String sendGetRequest(String url) {
        String result = " ";
        JSONObject jsonOject = new JSONObject();
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            result = buf.readLine();

        } catch (Exception ex) {
            jsonOject.put(ParamKey.ERROR_CODE, ErrorCode.UNKNOWN_ERROR);
            result = jsonOject.toJSONString();
        }
        return result;
    }
    
    private static final String UTF8Stamp = "text/plain;charset=UTF-8";
    private void dataBack(HttpServletResponse response, String message, Date time) {
        try {
            if (message == null || message.isEmpty()) {
                message = ResponseMessage.UnknownError;
            }
            Util.addDebugLog("Respond: " + message);
            response.setContentType(UTF8Stamp);
            try (OutputStream out = response.getOutputStream()) {
                out.write(message.getBytes());
                out.flush();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
}
