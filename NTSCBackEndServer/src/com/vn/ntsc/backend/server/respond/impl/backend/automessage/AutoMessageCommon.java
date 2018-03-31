/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.automessage;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import eazycommon.constant.Format;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public class AutoMessageCommon {
    private static final Pattern pattern = Pattern.compile(Format.URL_REGEX);
    public static boolean validate(String string) {
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }    
    
    public static boolean vertifyDate(String time) {
        Date date = null;
        try {
            date = DateFormat.parse_yyyyMMddHHmm(time);
            if (date.getTime() < Util.getGMTTime().getTime()) {
                return false;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return false;
        }
        return true;
    }

    public static void getQuery(JSONObject obj) {
        obj.remove(ParamKey.CONTENT);
        obj.remove(ParamKey.API_NAME);
        obj.remove(ParamKey.TIME);
        obj.remove(ParamKey.URL);
        obj.remove(ParamKey.SENDER);
        obj.remove(ParamKey.USER_ID);
    }

}
