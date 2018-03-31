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
import com.vn.ntsc.backend.entity.impl.automessage.Message;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoMessage;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.server.automessage.MessageContainer;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.AutoMessageCommon;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class InsertAutoMessageApi implements IApiAdapter {

    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(ParamKey.SENDER, 4);
        keys.put(ParamKey.CONTENT, 5);
        keys.put(ParamKey.TIME, 6);
    }    
    
    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            Long isPurchase = Util.getLongParam(obj, ParamKey.IS_PURCHASE);
            if(isPurchase != null && isPurchase == 2)
                obj.remove(ParamKey.IS_PURCHASE);
            String message = Util.getStringParam(obj, ParamKey.CONTENT);
            String time = Util.getStringParam(obj, ParamKey.TIME);
            String sender = Util.getStringParam(obj, ParamKey.SENDER);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                respond = new EntityRespond(error);
                return respond;
            }
            if (!AutoMessageCommon.vertifyDate(time)) {
                return new EntityRespond(keys.get(ParamKey.TIME));
            }
            
            AutoMessageCommon.getQuery(obj);
            String id = AutoMessageChatDAO.insert(sender, message, time, obj.toJSONString());
            Message msg = new AutoMessage(message, sender, id, time, obj, Constant.FLAG.ON, ip);
            MessageContainer.put(msg);
            respond = new EntityRespond(ErrorCode.SUCCESS, new InsertData(id));
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
