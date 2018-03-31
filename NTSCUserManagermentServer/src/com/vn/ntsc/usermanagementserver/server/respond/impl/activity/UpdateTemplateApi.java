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
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.UserTemplateDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.UserTemplate;

/**
 *
 * @author RuAc0n
 */
public class UpdateTemplateApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String id = Util.getStringParam(obj, "template_id");
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String templateContent = Util.getStringParam(obj, "template_content");
            String templateTitle = Util.getStringParam(obj, "template_title");
            UserTemplate template = new UserTemplate(null, userId, templateContent, templateTitle);
            UserTemplateDAO.update(id, template);                   
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
