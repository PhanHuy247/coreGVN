/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.AutoQANotifyDAO;
import com.vn.ntsc.backend.entity.impl.automessage.Message;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoQANotify;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.server.automessage.MessageContainer;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.AutoMessageCommon;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author Administrator
 */
public class InsertQANotifyApi implements IApiAdapter {
    
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(ParamKey.URL, 4);
        keys.put(ParamKey.CONTENT, 5);
        keys.put(ParamKey.TIME, 6);
    }
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long isPurchase = Util.getLongParam(obj, ParamKey.IS_PURCHASE);
            if(isPurchase != null && isPurchase == 2)
                obj.remove(ParamKey.IS_PURCHASE);
            String url = Util.getStringParam(obj, ParamKey.URL);
            String message = Util.getStringParam(obj, ParamKey.CONTENT);
            String time = Util.getStringParam(obj, ParamKey.TIME);
            String ip = Util.getStringParam(obj, ParamKey.IP);

            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                respond = new EntityRespond(error);
                return respond;
            }
            if (!AutoMessageCommon.validate(url)) {
                return new EntityRespond(keys.get(ParamKey.URL));
            }
            if (!AutoMessageCommon.vertifyDate(time)) {
                return new EntityRespond(keys.get(ParamKey.TIME));
            }
            
            AutoMessageCommon.getQuery(obj);
            String id = AutoQANotifyDAO.insert(url, message, ip, time, obj.toJSONString());
            // create message
            Message msg = new AutoQANotify(message, url, ip, id, time, obj, Constant.FLAG.ON);
            MessageContainer.put(msg);
            respond = new EntityRespond(ErrorCode.SUCCESS, new InsertData(id));
            System.out.println("xong insert");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
