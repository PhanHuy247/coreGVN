/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter;

import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.dao.impl.UserTokenDAO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import eazycommon.constant.Constant;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import eazycommon.exception.EazyException;

/**
 *
 * @author tuannxv00804
 */
public class MinistryOfInternalAffairs {
    
    private static final String CheckTokenString = "checktoken=";
    private static final Pattern CheckTokenPattern = Pattern.compile( CheckTokenString );
    
    private static final String API1_Result_True = "result=true&user_id=%s&send_read_message=%s&application_type=%s&device_id=%s";
    private static final String API1_Result_False = "result=false&new_token=%s";
    private static final String API2_Result_False = "result=false";
    
    public static String execute( String requestStr ) throws EazyException{
        Matcher matcher = CheckTokenPattern.matcher( requestStr );
        if( matcher.find() ){
            /**
             * API No 1: CheckToken.
             */
            int end = matcher.end();
            String token = requestStr.substring( end );
            Session session = SessionManager.getSession( token );
            if( session != null ){
                return String.format(API1_Result_True, session.userID, String.valueOf(isSendReadMessage(session)),String.valueOf(session.applicationType),session.deviceId);
//                return API1_Result_True + session.userID;
            }else{
                //case session null , token expire, return new token
                // boolean useToken can use token ?
                //if useToken = false,cann't use token, need create token new
                //if useToken = true , can use token
                Boolean useToken = false;
                //even session has token expire
                //get session expire in cache
                Session ss = SessionManager.getSessionHasToken(token);
                if (ss == null) {
                    //if cache hasn't
                    //get session expire in datatabases with token
                    ss = UserTokenDAO.getInfoSession(token);
                    if (ss == null) {
                        //get session expire in datatabases with old_token
                        ss = UserTokenDAO.getInfoSessionOldToken(token);
                        useToken = true;
                    }
                } else {
                    useToken = true;
                }
                if (ss != null && ss.userID != null) {
                    Session sessionNew = null;
                    if (!useToken) {
                        //create 1 session new if cannot use token
                        sessionNew = new Session(ss.userID, true, ss.applicationVersion, ss.applicationType, ss.deviceId, token);
                        SessionManager.putSession(sessionNew);
                        UserSessionDAO.add(sessionNew);
                        UserTokenDAO.add(sessionNew);
                    } else {
                        //use session 
                        sessionNew = ss;
                    }
                    return String.format(API1_Result_False, sessionNew.token);
                }else{
                    return String.format(API2_Result_False);
                }
            }
            
        }
        
        return API1_Result_False;
    }
    
    private static final String SEND_READ_MESSAGE_ANDROID_VERSION = "1.16";
    public static boolean isSendReadMessage(Session session){
        if(session.applicationType == null  || session.applicationType != Constant.APPLICATION_TYPE.ANDROID_APPLICATION)
            return true;
        String applicationVersion = session.applicationVersion;
        if(applicationVersion != null && !applicationVersion.isEmpty()){
            String usableVersion = SEND_READ_MESSAGE_ANDROID_VERSION;
            String [] usalbeVersionElements = usableVersion.split("\\.");
            String [] applicationVersionElements = applicationVersion.split("\\.");
            if(usalbeVersionElements.length <= applicationVersionElements.length){
                for(int i = 0; i < usalbeVersionElements.length ; i++){
                    String usalbeVersionElement = "."  + usalbeVersionElements[i];
                    String applicationVersionElement = "."  + applicationVersionElements[i];
                    try{
                        double usableVersionE = Double.parseDouble(usalbeVersionElement);
                        double applicationVersionE = Double.parseDouble(applicationVersionElement);
                        if(applicationVersionE < usableVersionE){
                            return false;
                        }else if(applicationVersionE > usableVersionE){
                            return true;
                        }
                    }catch(NumberFormatException ex){
                        int compare = applicationVersionElement.compareTo(usalbeVersionElement);
                        if(compare < 0){
                            return false;
                        }else if(compare > 0){
                            return true;
                        }
                    }
                }
                return true;
            }else{
                for(int i = 0; i < applicationVersionElements.length ; i++){
                    String usalbeVersionElement = "." + usalbeVersionElements[i];
                    String applicationVersionElement = "." + applicationVersionElements[i];
                    try{
                        double usableVersionE = Double.parseDouble(usalbeVersionElement);
                        double applicationVersionE = Double.parseDouble(applicationVersionElement);
                        if(applicationVersionE < usableVersionE){
                            return false;
                        }else if(applicationVersionE > usableVersionE){
                            return true;
                        }
                    }catch(NumberFormatException ex){
                        int compare = applicationVersionElement.compareTo(usalbeVersionElement);
                        if(compare < 0){
                            return false;
                        }else if(compare > 0){
                            return true;
                        }
                    }
                }
                return false;
            }
        }else{
            return true;
        }
    }  
    
}
