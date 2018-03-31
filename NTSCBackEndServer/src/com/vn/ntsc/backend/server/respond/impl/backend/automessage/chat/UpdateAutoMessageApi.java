/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.automessage.chat;

import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.AutoMessageChatDAO;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoMessage;
import com.vn.ntsc.backend.server.automessage.MessageContainer;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.AutoMessageCommon;

/**
 *
 * @author RuAc0n
 */
public class UpdateAutoMessageApi implements IApiAdapter {

    private static final Map<String, Integer> keys = new TreeMap<String, Integer>();

    static {
        keys.put(ParamKey.SENDER, 4);
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
            String sender = (String) obj.get(ParamKey.SENDER);
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                res = new Respond(error);
                return res;
            }
            if (!AutoMessageCommon.vertifyDate(time)) {
                return new Respond(keys.get(ParamKey.TIME));
            }
            
            AutoMessageChatDAO.update(id, sender, message, time, sender);
            
            AutoMessage msg = (AutoMessage) MessageContainer.getMessage(id);
            msg.update(message, time, sender);
            MessageContainer.put(msg);
            res = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        }
        return res;
    }
}
