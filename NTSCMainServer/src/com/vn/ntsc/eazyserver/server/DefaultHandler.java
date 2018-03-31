/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.AdjustCMCodeDAO;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.dao.impl.UserTokenDAO;
import com.vn.ntsc.eazyserver.adapter.AdapterManager;
import com.vn.ntsc.eazyserver.adapter.CMCodeProcessor;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.MinistryOfInternalAffairs;
import com.vn.ntsc.eazyserver.adapter.impl.UmsMis;
import com.vn.ntsc.eazyserver.adapter.impl.UserManagermentServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.backend.IServiceBackendAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.servicemanagement.respond.APIManager;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.StringRespond;

/**
 *
 * @author duongltd
 */
public class DefaultHandler extends AbstractHandler {

    private static final String UTF8Stamp = "text/plain;charset=UTF-8";
    private static final String SPILIT_CHARACTER = "=";

    /**
     * NOTICE: Session is initialized and added in login or authentication
     * process.
     *
     * @param string
     * @param rqst
     * @param hsr
     * @param response
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    @Override
    public void handle(String string, org.eclipse.jetty.server.Request rqst, HttpServletRequest hsr, HttpServletResponse response) throws IOException, ServletException {
        Util.addDebugLog("DefaultHandler START");
        Util.addDebugLog("DefaultHandler " + string);
        try {
            Date time = Util.getGMTTime();
            rqst.setHandled(true);
            Util.addDebugLog("DefaultHandler setHandled");

            response = addHeaders(response);
//            response.addHeader("Access-Control-Allow-Origin", "*");
//            response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
//            response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
            Util.addDebugLog("DefaultHandler addHeaders");

            //<editor-fold desc="Internal Servers Communication">
            //HUNGDT add 16-04-2016
            if (string != null) {
                if (startWith(string, "tracker_id")) {
                    installTracker(string, time);
                    Util.addDebugLog("DefaultHandler installTracker");
                } // END
                else if (startWith(string, ParamKey.CM_CODE)) {
                    CMCodeProcessor.execute(string.substring(1), response, rqst);
                    Util.addDebugLog("DefaultHandler CMCodeProcessor.execute");
                    return;
                } else if (startWith(string, "checktoken")) {
                    checkToken(response, string);
                    Util.addDebugLog("DefaultHandler CMCodeProcessor.execute");
                    return;
                } else if (startWith(string, ParamKey.ID)) {
                    updateCmCode(string, rqst);
                    sendRedirectWhenStartWithID(string, rqst, response);
                    Util.addDebugLog("DefaultHandler updateCmCode");
                    return;
                } else if (startWith(string, ParamKey.UNIQUE_NUMBER)) {
                    updateInstallCmCode(string, rqst, response);
                    Util.addDebugLog("DefaultHandler updateInstallCmCode");
                    return;
                }
            }

            long startRequest = System.currentTimeMillis();            
            
            Request request = initRequest(rqst, time, startRequest, hsr, response);
            
            if(request == null){
                return;
            }

            String api = request.api;
            if (api == null) {
                dataBack(response, ResponseMessage.BadResquestMessage, time);
                Util.addDebugLog("DefaultHandler BadResquestMessage");
                return;
            }
           
            
            Util.addDebugLog("DefaultHandler --------------->"+api);
            String token = request.token;

            if (token == null || token.isEmpty() || api.equals(API.LOG_OUT)) {
                callNullTokenAPI(api, time, request, response, startRequest);
                Util.addDebugLog("DefaultHandler callNullTokenAPI");
                return;
            }

            Session session = SessionManager.getSession(token);
            
//            if(api.equals(API.CHANGE_TOKEN)){
//                
//                JSONObject jsonResult = new JSONObject();
//                String applicationVersion = (String) request.getParamValue("application_version");
//                String userid = (String) request.getParamValue("user_id");
//                Long applicationTypeL = (Long) request.getParamValue("applicaton_type");
//                
//                int applicationType = Constant.APPLICATION_TYPE.ANDROID_APPLICATION;
//                if (applicationTypeL != null) {
//                    applicationType = applicationTypeL.intValue();
//                } else {
//                    Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
//                    if (deviceType == Constant.DEVICE_TYPE.IOS) {
//                        applicationType = Constant.APPLICATION_TYPE.IOS_ENTERPRISE_APPLICATION;
//                    }
//                }
//                if(session == null){
//                    Session sessionNew = new Session(userid, true, applicationVersion, applicationType);
//                    SessionManager.putSession(sessionNew);
//                    UserSessionDAO.add(sessionNew);
//                    jsonResult.put("token", sessionNew.token);
//                    jsonResult.put("is_change", true);
//                }else{
//                    jsonResult.put("token", token);
//                    jsonResult.put("is_change", false);
//                }
//                jsonResult.put("user_id", userid);
//                StringRespond resultResponse = new StringRespond(ErrorCode.SUCCESS,jsonResult.toJSONString());
//                dataBack(response, resultResponse.toString(), time);
//                return;
//            }
            if (session == null) {
                responseInvalidSessionError(token, response, time, startRequest, request);
                Util.addDebugLog("DefaultHandler responseInvalidSessionError");
                return;
            } else if (!session.normalUser) {
                if (!api.equals(API.UPDATE_USER_INFOR)
                        && !api.equals(API.GET_USER_INFOR)
                        && !api.equals(API.CONFIRM_UPLOAD_IMAGE)
                        && !api.equals(API.GET_UPDATE_INFO_FLAGS)) { //
                    responseInvalidUserError(token, response, time, startRequest, request, api, session);
                    Util.addDebugLog("DefaultHandler responseInvalidUserError");
                    return;
                }
            }
            if(api.equals(API.EXPIRE_TOKEN)){
                Util.addDebugLog("EXPIRE_TOKEN========================================="+token);
                session.resetExpire();
                Util.addDebugLog("session==================================="+session.sessionExpire);
                Util.addDebugLog("Util.currentTime()==================================="+Util.currentTime());
                return;
            }
//            session.timeToLive = 0;
            session.resetExpire();
            Util.addDebugLog("session==================================="+session.sessionExpire);
            Util.addDebugLog("Util.currentTime()==================================="+Util.currentTime());
            request.put(ParamKey.USER_ID, session.userID);
            String result = "";
            if(api.equals(API.TOTAL_BADGE) || api.equals(API.TOTAL_NOTI_SEEN)){
                 IApiAdapter adapter = APIManager.getApi(api);
                if (adapter == null) {
                    responseBadRequestError(response, time, startRequest, request);
                }
             
                result = adapter.execute(request).toString();
            }else{
                result = callService(api, response, time, startRequest, request, session);
            }
            Util.addDebugLog("Return: " + result);

            if (api.equals(API.RESET_PASSWORD)) {
                changePassInBackend(result, request, token);
            }
            Util.addDebugLog("DefaultHandler changePassInBackend");

            dataBack(response, result, time);
            logSlowRequest(startRequest, request, "Authen");
            Util.addDebugLog("DefaultHandler END");

        } catch (EazyException | IOException | ParseException ex) {
            Util.addErrorLog(ex);
            dataBack(response, ResponseMessage.UnknownError, Util.getGMTTime());
        }
    }

    public void changePassInBackend(String result, Request request, String token) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(result);
        Long code = (Long) json.get("code");
        if (code == 0) {
            String userId = (String) request.getParamValue("id");
            UmsMis.changePassInBackend(token, userId);
        }
    }

    private String callService(String api, HttpServletResponse response, Date time, long startRequest, Request request, Session session) {
        String result;
        IServiceAdapter adapter = AdapterManager.getAdapter(api);
        if (adapter == null) {
            responseBadRequestError(response, time, startRequest, request);
        }
        if (adapter instanceof IServiceBackendAdapter) {
            if (session.type == null) { // token is not belong administrator
                responseUnknowntError(response, time, startRequest, request);
            }
        }
        result = adapter.callService(request);
        return result == null ? "" : result;
    }

    private void installTracker(String string, Date time) throws EazyException {
        String[] adjustElement = string.substring(1).split("/");
        String tracker_id = Util.getStringValue(adjustElement[0], "tracker_id", SPILIT_CHARACTER);
        String label = Util.getStringValue(adjustElement[3], "label", SPILIT_CHARACTER);
        String deviceType = null;
        String deviceId = null;
        if (adjustElement.length >= 2) {
            deviceType = Util.getStringValue(adjustElement[1], "deviceType", SPILIT_CHARACTER);
            deviceId = Util.getStringValue(adjustElement[2], "deviceId", SPILIT_CHARACTER);
        }
        int flag = 0;

        // LongLT 13Sep2016 /////////////////////////// START #4484
        String adid = Util.getStringValue(adjustElement[4], "adid", SPILIT_CHARACTER);
        deviceId = adid;
        // LongLT 13Sep2016 /////////////////////////// END #4484

        boolean result = AdjustCMCodeDAO.installTracker(tracker_id, deviceType, deviceId, time, flag, label);
        Util.addDebugLog("AdjustCMCodeDAO.installTracker " + result);
    }

    private void checkToken(HttpServletResponse response, String string) throws IOException, EazyException {
        response.setContentType(UTF8Stamp);
        OutputStream out = response.getOutputStream();
        string = string.substring(1);
        String result = MinistryOfInternalAffairs.execute(string);
        out.write(result.getBytes());
        out.flush();
        out.close();
    }

    private void updateCmCode(String string, org.eclipse.jetty.server.Request rqst) {
        String userAgent = rqst.getHeader(ParamKey.USER_AGENT);
        String[] userCodeElement = string.substring(1).split("&");

        String cmCode = getCMCode(rqst);

        if (cmCode != null && !cmCode.isEmpty()) {
            String originalUserCode = userCodeElement[0];
            String userCode = Util.getStringValue(originalUserCode, ParamKey.ID, SPILIT_CHARACTER);
            int type = Constant.DEVICE_TYPE.IOS;
            if (userAgent.contains(Constant.DEVICE_NAME.ANDROID_DEVICE)) {
                type = Constant.DEVICE_TYPE.ANDROID;
            }

            UserManagermentServiceAdapter.updateCmCode(userCode, cmCode, type);
        }
    }

    private String getCMCode(org.eclipse.jetty.server.Request rqst) {
        String code = null;
        Cookie[] cookies = rqst.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie ck : cookies) {
                if (ck.getName().equals(Constant.COOKIES_NAME)) {
                    String value = ck.getValue();
                    if (value != null && !value.isEmpty()) {
                        code = Util.getStringValue(value, ParamKey.CM_CODE, SPILIT_CHARACTER);
                    }
                }
            }
        }
        return code;
    }

    private void sendRedirectWhenStartWithID(String string, org.eclipse.jetty.server.Request rqst, HttpServletResponse response) throws IOException {
        String userAgent = rqst.getHeader(ParamKey.USER_AGENT);
        int applicationType = getApplicationType(string);

        Cookie cokie = new Cookie(Constant.COOKIES_NAME, "");
        response.addCookie(cokie);

        sendRedirect(userAgent, applicationType, response);
    }

    private void sendRedirect(String userAgent, int applicationType, HttpServletResponse response) throws IOException {
        if (userAgent.contains(Constant.DEVICE_NAME.MAC_DEVICE)) {
            if (applicationType == Constant.APPLICATION_TYPE.IOS_PRODUCTION_APPLICATION) {
                response.sendRedirect(Config.REDIRECT_IOS_APP);
            } else {
                response.sendRedirect(Config.REDIRECT_IOS_APP_ENTERPRISE);
            }
        } else if (userAgent.contains(Constant.DEVICE_NAME.ANDROID_DEVICE)) {
            response.sendRedirect(Config.REDIRECT_ANDROID_APP);
        }
    }

    private int getApplicationType(String string) {
        int applicationType = 0;

        String[] userCodeElement = string.substring(1).split("&");
        if (userCodeElement.length > 1) {
            try {
                String applicationTypeString = Util.getStringValue(userCodeElement[1], "application_type", SPILIT_CHARACTER);
                applicationType = Integer.parseInt(applicationTypeString);
            } catch (NumberFormatException ex) {
            }
        } else {
            applicationType = Constant.APPLICATION_TYPE.IOS_ENTERPRISE_APPLICATION;
        }

        return applicationType;
    }

    private boolean startWith(String string, String key) {
        boolean result = false;
        if (string != null && string.length() > 1) {
            result = string.substring(1).startsWith(key);
        }
        return result;
    }

    private void updateInstallCmCode(String string, org.eclipse.jetty.server.Request rqst, HttpServletResponse response) throws IOException {
        //                Util.addDebugLog("unique String : " + string);
        String cmCode = null;
        String userAgent = rqst.getHeader(ParamKey.USER_AGENT);
        Cookie[] cookies = rqst.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie ck : cookies) {
                if (ck.getName().equals(Constant.COOKIES_NAME)) {
                    String value = ck.getValue();
                    if (value != null && !value.isEmpty()) {
                        cmCode = Util.getStringValue(value, ParamKey.CM_CODE, SPILIT_CHARACTER);
                    }
                }
            }
        }
        int applicationType = Constant.APPLICATION_TYPE.IOS_ENTERPRISE_APPLICATION;
        String[] uniqueElement = string.substring(1).split("&");
        if (uniqueElement.length > 1) {
            try {
                String applicationTypeString = Util.getStringValue(uniqueElement[1], "applicatioin_type", SPILIT_CHARACTER);
                applicationType = Integer.parseInt(applicationTypeString);
            } catch (NumberFormatException ex) {
            }
        }
//                System.out.println("cmcode unique : " + cmCode   );
        if (cmCode != null && !cmCode.isEmpty()) {
            String originalUnique = uniqueElement[0];
//                    String uniqueNumber = Util.getStringValue(string.substring(1), ParamKey.UNIQUE_NUMBER, Constant.SPILIT_CHARACTER);
            String uniqueNumber = Util.getStringValue(originalUnique, ParamKey.UNIQUE_NUMBER, SPILIT_CHARACTER);
//                    System.out.println("unique : " + uniqueNumber   );
            int type = Constant.DEVICE_TYPE.IOS;
            if (userAgent.contains(Constant.DEVICE_NAME.ANDROID_DEVICE)) {
                type = Constant.DEVICE_TYPE.ANDROID;
            }
            UserManagermentServiceAdapter.updateInstallCmCode(uniqueNumber, cmCode, type);
        }
        sendRedirect(userAgent, applicationType, response);
    }

    private Request initRequest(org.eclipse.jetty.server.Request rqst, Date time, long startRequest, HttpServletRequest hsr, HttpServletResponse response) {
        Request request;
        try {

            String userAgent = rqst.getHeader(ParamKey.USER_AGENT);
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
            }
            BufferedReader reader;
            String inputString;
            //                Util.addDebugLog("userAgent : " + userAgent);
            InputStreamReader isr = new InputStreamReader(rqst.getInputStream());
            reader = new BufferedReader(isr);
            inputString = reader.readLine();
            isr.close();
            reader.close();
            if (inputString == null) {
                Util.addDebugLog("Client request1 : " + inputString);
                return null;
            }
            request = Request.initRequest(inputString);
            long initRequest = System.currentTimeMillis();
            if (initRequest - startRequest > 2000) {
                Util.addInfoLog("Init request slow : " + inputString);
            }
            if (request == null) {
                Util.addDebugLog("Client request2 : " + inputString);
                dataBack(response, ResponseMessage.BadResquestMessage, time);
                return null;
            }
            request.userAgent = userAgent;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            dataBack(response, ResponseMessage.BadResquestMessage, time);
            return null;
        }

        if (!request.contain(ParamKey.IP)) {
            request.put(ParamKey.IP, Util.getClientIpAddr(hsr));
        }

        Util.addDebugLog("Client request3 : " + request.toString());
        return request;
    }

    private void callNullTokenAPI(String api, Date time, Request request, HttpServletResponse response, long startRequest) {
        if (api.equals(API.LOGIN) // Adding 
                || api.equals(API.LOG_OUT)
                || api.equals(API.FORGOT_PASSWORD)
                || api.equals(API.REGISTER_VERSION_2)
                || api.equals(API.REGISTER) // session
                || api.equals(API.CHANGE_PASS_CASE_FORGOT) //here
                || api.equals(API.LOGIN_ADMINISTRATOR)
                || api.equals(API.INIT)
                || api.equals(API.STATIC_PAGE)
                || api.equals(API.LOGIN_TOOL)
                || api.equals(API.CALL_LOG)
                || api.equals(API.CLICK_PUSH_NOTIFICATION)
                || api.equals(API.GET_BANNED_WORD)
                || api.equals(API.CALL_PAYMENT)
                || api.equals(API.PUSH_NOTIFICATION_FROM_BACK_END)
                || api.equals(API.PUSH_NOTIFICATION)
                || api.equals(API.GET_INFOR_FOR_APPLICATION)
                || api.equals(API.GET_CM_CODE_BY_USER_ID)
                || api.equals(API.GET_USER_BY_REGISTER_TIME)
                || api.equals(API.INSTALL_APPLICATION)
                || api.equals(API.GET_USER_STATUS_BY_EMAIL)
                || api.equals(API.MAKE_CALL)
                || api.equals(API.START_CALL)
                || api.equals(API.END_CALL)
                || api.equals(API.CLICK_NOTI_NOTIFICATION)                
                || api.equals(API.GET_BUZZ)                
                || api.equals(API.LIST_SUB_COMMENT)
                || api.equals(API.GET_BUZZ_DETAIL)
                || api.equals(API.ADD_NUMBER_OF_SHARE)
                || api.equals(API.ADD_NUMBER_OF_VIEW)
                || api.equals(API.LIST_COMMENT)
                || api.equals(API.LOAD_IMAGE)
                || api.equals(API.GET_USER_INFOR)
                || api.equals(API.GET_BASIC_INFOR)
                || api.equals(API.LIST_PUBLIC_IMAGE)
                || api.equals(API.LIST_PUBLIC_VIDEO)
                || api.equals(API.LIST_PUBLIC_FILE)
                || api.equals(API.CHECK_ONLINE_STATUS_BY_DEVICE)                
                || api.equals(API.MEET_PEOPLE)                
                || api.equals(API.SEARCH_BY_NAME)                
                || api.equals(API.UNREGISTER_NOTI_TOKEN)
                || api.equals(API.LOAD_ALBUM)
                || api.equals(API.LOAD_ALBUM_IMAGE)
                || api.equals(API.START_STREAM_SERVER)
                || api.equals(API.GET_UPLOAD_SETTING)
                || api.equals(API.CREATE_ACCOUNT_FROM_FBID)
                || api.equals(API.LIST_EMOJI)
                || api.equals(API.LIST_EMOJI_CAT)
                || api.equals(API.LIST_UPDATED_EMOJI_CAT)
                || api.equals(API.LIST_UPDATED_STICKER_CAT)
                || api.equals(API.GET_ALL_GIFT)
                || api.equals(API.GET_ALL_GIFT_CATEGORY)) {
            IServiceAdapter adapter = AdapterManager.getAdapter(api);
            if (adapter != null) {
                String result = adapter.callService(request);
                dataBack(response, result, time);
            } else {
                dataBack(response, ResponseMessage.BadResquestMessage, time);
            }
            logSlowRequest(startRequest, request, "Unauthen");
//                    return;
        } else {
            //DuongLTD
            logSlowRequest(startRequest, request, "Unauthen");
            dataBack(response, ResponseMessage.TOKENNOTEXISTDB, time);
//                    return;
        }
    }

    private HttpServletResponse addHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        return response;
    }

    private void responseInvalidSessionError(String token, HttpServletResponse response, Date time, long startRequest, Request request) throws EazyException {
        Util.addDebugLog("DefaultHandler -> Session is invalid, token = " + token);
        Boolean useToken = false;
        Session ss = SessionManager.getSessionHasToken(token);
        if(ss == null){
            ss = UserTokenDAO.getInfoSession(token);
            if(ss == null){
                ss = UserTokenDAO.getInfoSessionOldToken(token);
                useToken = true;
            }
        }else{
            useToken = true;
        }
        if(ss != null && ss.userID != null){
            Session sessionNew = null;
            if(!useToken){
                sessionNew = new Session(ss.userID, true, ss.applicationVersion, ss.applicationType,ss.deviceId,token);
                SessionManager.putSession(sessionNew);
                UserSessionDAO.add(sessionNew);
                UserTokenDAO.add(sessionNew);
            }else{
                sessionNew = ss;
            }
//            UserTokenDAO.update(token, sessionNew.token);
            JSONObject obj = new JSONObject();
            obj.put(ParamKey.ERROR_CODE, ErrorCode.INVALID_TOKEN);
            obj.put("new_token", sessionNew.token);
            //DuongLTD
            dataBack(response, obj.toJSONString(), time);
            obj.put("token", token);
            obj.put("user_id", ss.userID);
            obj.put("api", API.CHANGE_TOKEN_WEBSOCKET);
            InterCommunicator.sendRequest( obj.toJSONString(), Config.ChatServerIP, Config.ChatServerPort );
            logSlowRequest(startRequest, request, "Authen");
        }else{
            dataBack(response, ResponseMessage.TOKENNOTEXISTDB, time);
        }
    }

    private void responseInvalidUserError(String token, HttpServletResponse response, Date time, long startRequest, Request request, String api, Session session) {
        Util.addDebugLog("DefaultHandler -> invalid userId: " + session.userID);
        response.setContentType(UTF8Stamp);
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.INVALID_ACCOUNT);
        if (api.equals(API.CHECK_TOKEN)) {
            JSONObject data = new JSONObject();
            data.put(ParamKey.TOKEN_STRING, token);
            obj.put(ParamKey.DATA, data);
        }
        dataBack(response, obj.toJSONString(), time);
        logSlowRequest(startRequest, request, "Authen");
    }

    private void responseBadRequestError(HttpServletResponse response, Date time, long startRequest, Request request) {
        dataBack(response, ResponseMessage.BadResquestMessage, time);
        logSlowRequest(startRequest, request, "Authen");
    }

    private void responseUnknowntError(HttpServletResponse response, Date time, long startRequest, Request request) {
        dataBack(response, ResponseMessage.UnknownError, time);
        logSlowRequest(startRequest, request, "Authen");
    }

    private void logSlowRequest(long startRequest, Request request, String msg) {
        long authenRequest = System.currentTimeMillis();
        if (authenRequest - startRequest > 10000) {
            Util.addInfoLog(msg + " request slow : " + request.toString());
        }
    }

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
