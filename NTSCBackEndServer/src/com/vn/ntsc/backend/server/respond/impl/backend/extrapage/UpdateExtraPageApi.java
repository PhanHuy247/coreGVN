/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.extrapage;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.Format;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.ExtraPageDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateExtraPageApi implements IApiAdapter {

    private static final Pattern pattern = Pattern.compile(Format.URL_REGEX);
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(ParamKey.TITLE, 4);
        keys.put(ParamKey.URL, 5);
    }

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            String title = Util.getStringParam(obj, ParamKey.TITLE);
            String url = Util.getStringParam(obj, ParamKey.URL);
            if (id == null || id.isEmpty()) {
                return null;
            }

            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                respond = new Respond(error);
                return respond;
            }
            if (!validate(url)) {
                return new Respond(keys.get(ParamKey.URL));
            }
            ExtraPageDAO.update(id, title, url);
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    private static boolean validate(String string) {
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
