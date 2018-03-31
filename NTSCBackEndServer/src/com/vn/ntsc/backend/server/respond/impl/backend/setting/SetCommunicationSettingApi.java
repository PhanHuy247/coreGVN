/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.setting;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.DAOKeys;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.setting.CommunicationSettingDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.CommunicationSettingData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class SetCommunicationSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
//            CommunicationSettingData data = validate(obj);
            CommunicationSettingData data = new CommunicationSettingData(ErrorCode.SUCCESS);
            if (data.code != ErrorCode.SUCCESS) {
                respond = new EntityRespond(data.code, data);
                return respond;
            }
            CommunicationSettingDAO.update(obj);
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    private CommunicationSettingData validate(JSONObject obj) {
        int plusCode = 3;
        int min = -1000000000;
        int max = 1000000000;
        for (String type : DAOKeys.communication_type_list) {
            JSONObject dataObj = (JSONObject) obj.get(type);
            for(int i = 0; i < DAOKeys.pairs_list.size(); i++){
                String key = DAOKeys.pairs_list.get(i);
                JSONObject value = (JSONObject) dataObj.get(key);
                if (value == null) {
                    return new CommunicationSettingData(calculate(i, 4, 3, plusCode), type);
                } else {
                    if(!key.equals(DAOKeys.female_female)){
                     JSONObject caller = (JSONObject) value.get(SettingdbKey.COMMUNICATION_SETTING.CALLER);
                     Long callerPoint = (Long) caller.get(DAOKeys.value);
                     if(!isValid(callerPoint, min, max))
                        return new CommunicationSettingData(calculate(i, 4, 3, plusCode), type);
                     JSONObject potentialCaller = (JSONObject) value.get(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_CALLER);
                     Long potentialCallerPoint = (Long) potentialCaller.get(DAOKeys.value);
                     if(!isValid(potentialCallerPoint, min, max))
                        return new CommunicationSettingData(calculate(i, 4, 2, plusCode), type);
                     JSONObject receiver = (JSONObject) value.get(SettingdbKey.COMMUNICATION_SETTING.RECEIVER);
                     Long receiverPoint = (Long) receiver.get(DAOKeys.value);
                     if(!isValid(receiverPoint, min, max))
                        return new CommunicationSettingData(calculate(i, 4, 1, plusCode), type);
                     JSONObject potentialReceiver = (JSONObject) value.get(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_RECEIVER);
                     Long potentialReceiverPoint = (Long) potentialReceiver.get(DAOKeys.value);
                     if(!isValid(potentialReceiverPoint, min, max))
                        return new CommunicationSettingData(calculate(i, 4, 0, plusCode), type);
                    }else{
                        JSONObject caller = (JSONObject) value.get(SettingdbKey.COMMUNICATION_SETTING.CALLER);
                        Long callerPoint = (Long) caller.get(DAOKeys.value);
                        if(!isValid(callerPoint, min, max))
                            return new CommunicationSettingData(calculate(i, 2, 1, plusCode), type);
                        JSONObject receiver = (JSONObject) value.get(SettingdbKey.COMMUNICATION_SETTING.RECEIVER);
                        Long receiverPoint = (Long) receiver.get(DAOKeys.value);
                        if(!isValid(receiverPoint, min, max))
                           return new CommunicationSettingData(calculate(i, 2, 0, plusCode), type);
                    }
                }
            }
        }
        return new CommunicationSettingData(ErrorCode.SUCCESS);
    }

    private int calculate(int number, int mutiple, int minus, int plus) {
        return (number + 1) * mutiple - minus + plus;
    }

    private boolean isValid(Long number, int min, int max) {
        return number != null && number >= min && number <= max;
    }

}
