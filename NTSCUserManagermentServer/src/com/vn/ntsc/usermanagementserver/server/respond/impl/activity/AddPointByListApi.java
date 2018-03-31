/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class AddPointByListApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            List<String> listIds = Util.getListString(obj, "list_ids");
            Long point = Util.getLongParam(obj, "added_point");
            if (point > 0) {
                for(String userId : listIds){
                    UserInforManager.increasePoint(userId, point.intValue());
                }
            } else {
                for(String userId : listIds){
                    UserInforManager.decreasePoint(userId, 0 - point.intValue());
                }
            }
                   
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
