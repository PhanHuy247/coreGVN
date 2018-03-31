/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.CMCodeDAO;

/**
 *
 * @author Rua
 */
public class CMCodeProcessor {
    
    public static final String cmCodeString = "cm_code=";
    public static final Pattern cmCodePattern = Pattern.compile(cmCodeString);
    
    public static void execute(String urlString, HttpServletResponse response, org.eclipse.jetty.server.Request rqst) {
        try {
            Matcher matcher = cmCodePattern.matcher(urlString);
            if (matcher.find()) {
                int end = matcher.end();
                String cmCode = urlString.substring(end);
                if (cmCode != null && !cmCode.isEmpty()) {
                    StringBuilder urlBuilder = new StringBuilder();
                    String dbUrl = CMCodeDAO.getRedirectUrl(cmCode);
                    if(dbUrl != null){
                        urlBuilder.append(dbUrl);
                        String parameter = rqst.getUri().toString();
                        String [] parameters = parameter.split("\\?");
                        if(parameters != null && parameters.length == 2){
                            if(dbUrl.contains("?")){
                                if(!dbUrl.endsWith("?")){
                                    if(!dbUrl.endsWith("&")){
                                        urlBuilder.append("&");
                                    }
                                }
                            }else{
                                urlBuilder.append("?");
                            }
                            urlBuilder.append(parameters[1]);
                        }
                    }else{
                        String userAgent = rqst.getHeader(ParamKey.USER_AGENT);
                        if (userAgent.contains(Constant.DEVICE_NAME.MAC_DEVICE)) {
                            urlBuilder.append(Config.REDIRECT_IOS_7_STORE);
                            urlBuilder.append("?");
                        } else{
                            urlBuilder.append(Config.REDIRECT_ANDROID_STORE);
                            urlBuilder.append("&referrer=");
                        }
                        String parameter = rqst.getUri().toString();
                        String [] parameters = parameter.split("\\?");
                        if(parameters != null && parameters.length == 2){
                            urlBuilder.append(parameters[1]);
                        }
                    }

                    String redirectUrl = urlBuilder.toString();
                    if (!redirectUrl.isEmpty()) {
                        Cookie cokie = new Cookie(Constant.COOKIES_NAME, urlString);
                        response.addCookie(cokie);
                        response.sendRedirect(redirectUrl);
                    }
                }
                
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
}
