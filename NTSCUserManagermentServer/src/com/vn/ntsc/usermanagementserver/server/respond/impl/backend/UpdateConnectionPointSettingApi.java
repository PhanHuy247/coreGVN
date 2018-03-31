/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.backend;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.mongokey.DAOKeys;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;

/**
 *
 * @author RuAc0n
 */
public class UpdateConnectionPointSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            for(String key : DAOKeys.connection_type_list){
                JSONObject value = (JSONObject) obj.get(key);
                if(value != null){
                    ActionManager.connectionPointSetting.put(key, value);
                }
            }
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
