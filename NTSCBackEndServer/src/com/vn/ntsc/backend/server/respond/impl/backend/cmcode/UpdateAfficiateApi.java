/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.cmcode;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.cmcode.AfficiateDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateAfficiateApi implements IApiAdapter {

    private static final String REGEX = "^([0-9a-zA-Z]+)$";
    public static  final String REGEX_EMAIL = "^((([0-9a-zA-Z]((\\.(?!\\.))|[-!#\\$%&'\\*\\+/=\\?\\^`\\{\\}\\|~\\w])*)(?<=[0-9a-zA-Z])@))((\\[(\\d{1,3}\\.){3}\\d{1,3}\\])|(([0-9a-zA-Z]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,6}))$";
    private static final int flag_error = 8;
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put("aff_name", 4);
        keys.put("aff_email", 5);
        keys.put("aff_pwd", 6);
        keys.put("aff_login_id", 7);
    }

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String affId = Util.getStringParam(obj, ParamKey.AFFICIATE_ID);
            String affName = Util.getStringParam(obj, "aff_name");
            String affEmail = Util.getStringParam(obj, "aff_email");
            String affPassword = Util.getStringParam(obj, "aff_pwd");
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            String affLoginId = Util.getStringParam(obj, "aff_login_id");
            if (affId == null || affId.isEmpty()) {
                return null;
            }
            if (flag == null) {
                return new Respond(flag_error);
            }
            int error = validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                return new Respond(error);
            }
            AfficiateDAO.update(affId, affName, affPassword, affEmail, flag.intValue(), affLoginId);
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    private static int validate(JSONObject obj, Map<String, Integer> keys) {
        for (Map.Entry<String, Integer> pair : keys.entrySet()) {
            try {
                String str = Util.getStringParam(obj, pair.getKey());
                if (str == null || str.trim().isEmpty()) {
                    return pair.getValue();
                }
                if (pair.getKey().equals("aff_login_id") || pair.getKey().equals("aff_pwd")) {
                    if (!validate(str, REGEX)) {
                        return pair.getValue();
                    }
                } else if (pair.getKey().equals("aff_email")) {
                    if (!validate(str, REGEX_EMAIL)) {
                        return pair.getValue();
                    }
                }

            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return pair.getValue();
            }
        }
        return 0;
    }

    private static boolean validate(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
