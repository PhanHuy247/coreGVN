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
import eazycommon.constant.Format;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.cmcode.CMCodeDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateCMCodeApi implements IApiAdapter {

    private static final String REGEX = "^([0-9a-z]([0-9a-z-_－])+)$";
    private static final int type_error = 6;
    private static final int money_error = 7;
    private static final int flag_error = 8;
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(ParamKey.CM_CODE, 4);
        keys.put(ParamKey.REGISTER_URL, 5);
        keys.put("pur_url", 9);
        keys.put("redirect_url", 10);
    }

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String cmCodeId = Util.getStringParam(obj, "cm_code_id");
            String affId = Util.getStringParam(obj, ParamKey.AFFICIATE_ID);
            String mediaId = Util.getStringParam(obj, "media_id");
            String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            String regUrl = Util.getStringParam(obj, ParamKey.REGISTER_URL);
            String purUrl = Util.getStringParam(obj, "pur_url");
            String redirectUrl = Util.getStringParam(obj, "redirect_url");
            String des = Util.getStringParam(obj, ParamKey.DESCRIPTION);
            Double money = Util.getDoubleParam(obj, "money");
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            if (cmCodeId == null || affId == null || affId.isEmpty() || mediaId == null || mediaId.isEmpty()) {
                return null;
            }
            if (type == null || type < 0) {
                return new Respond(type_error);
            }
            if (flag == null || flag < 0) {
                return new Respond(flag_error);
            }
            if (money == null || money < 0) {
                return new Respond(money_error);
            }
            int error = validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                return new Respond(error);
            }
            CMCodeDAO.update(cmCodeId, affId, mediaId, cmCode, type.intValue(), money, regUrl, purUrl, redirectUrl, des, flag.intValue());
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
                if ((str == null || str.trim().isEmpty()) && !pair.getKey().equals("pur_url") && !pair.getKey().equals("redirect_url")) {
                    return pair.getValue();
                }
                if (pair.getKey().equals(ParamKey.CM_CODE)) {
                    if (!validate(str, REGEX)) {
                        return pair.getValue();
                    }
                } else if (pair.getKey().equals(ParamKey.REGISTER_URL) || pair.getKey().equals("pur_url") || pair.getKey().equals("redirect_url")) {
                    if (str != null && !validate(str, Format.URL_REGEX)) {
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
