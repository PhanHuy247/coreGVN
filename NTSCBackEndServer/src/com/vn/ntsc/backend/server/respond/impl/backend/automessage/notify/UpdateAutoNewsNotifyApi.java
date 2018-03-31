/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify;

import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.AutoNewsNotifyDAO;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoNewsNotify;
import com.vn.ntsc.backend.server.automessage.MessageContainer;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.AutoMessageCommon;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author hungdt
 */
public class UpdateAutoNewsNotifyApi implements IApiAdapter {
    
    private static final Map<String, Integer> keys = new TreeMap<String, Integer>();

    static {
        keys.put(ParamKey.URL, 4);
        keys.put(ParamKey.CONTENT, 5);
        keys.put(ParamKey.TIME, 6);
    }
    @Override
    public Respond execute(JSONObject obj) {
        Respond res = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            String message = Util.getStringParam(obj, ParamKey.CONTENT);
            String time = Util.getStringParam(obj, ParamKey.TIME);
            if (id == null || id.isEmpty()) {
                return res;
            }
            String url = (String) obj.get(ParamKey.URL);
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                res = new EntityRespond(error);
                return res;
            }
            if (!AutoMessageCommon.validate(url)) {
                return new EntityRespond(keys.get(ParamKey.URL));
            }
            if (!AutoMessageCommon.vertifyDate(time)) {
                return new EntityRespond(keys.get(ParamKey.TIME));
            }
            
            AutoNewsNotifyDAO.update(id, url, message, time);
            
            AutoNewsNotify msg = (AutoNewsNotify) MessageContainer.getMessage(id);
            msg.update(message, url, time);
            MessageContainer.put(msg);
            res = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        }
        return res;
    }
}
