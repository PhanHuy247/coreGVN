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
import com.vn.ntsc.backend.dao.cmcode.MediaDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateMediaApi implements IApiAdapter {

    private static final Pattern pattern = Pattern.compile(Format.URL_REGEX);
    private static final int flag_error = 6;
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put("media_name", 4);
        keys.put("media_url", 5);
    }

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String affId = Util.getStringParam(obj, ParamKey.AFFICIATE_ID);
            String mediaId = Util.getStringParam(obj, "media_id");
            String mediaName = Util.getStringParam(obj, "media_name");
            String mediaUrl = Util.getStringParam(obj, "media_url");
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            if (affId == null || affId.isEmpty()) {
                return null;
            }
            if (flag == null) {
                return new Respond(flag_error);
            }
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                return new Respond(error);
            }
            if (!validate(mediaUrl)) {
                return new Respond(keys.get("media_url"));
            }
            MediaDAO.update(mediaId, affId, mediaName, mediaUrl, flag.intValue());
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
