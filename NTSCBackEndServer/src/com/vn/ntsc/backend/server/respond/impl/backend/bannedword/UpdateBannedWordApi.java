/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.bannedword;

import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.BannedWordDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import eazycommon.util.Util;
import java.util.Date;

/**
 *
 * @author RuAc0n
 */
public class UpdateBannedWordApi implements IApiAdapter {

    private static final int flag_error = 5;
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put("word", 4);
    }

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            if (id == null || id.isEmpty()) {
                return null;
            }
            String word = Util.getStringParam(obj, "word");
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            if (flag == null) {
                return new Respond(flag_error);
            }
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                return new Respond(error);
            }
            Date now = new Date();
            BannedWordDAO.update(id, word, flag.intValue(), now);
            BannedWordDAO.changeVersion(1);
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
