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
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.UserTemplateDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.UserTemplate;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListTemplateApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            List<UserTemplate> templates = UserTemplateDAO.list(userId);
            result = new ListEntityRespond(ErrorCode.SUCCESS ,  templates);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
