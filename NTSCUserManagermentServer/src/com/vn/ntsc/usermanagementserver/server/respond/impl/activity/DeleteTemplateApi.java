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
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.UserTemplateDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.UserTemplate;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class DeleteTemplateApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String id = Util.getStringParam(obj, "template_id");
            UserTemplateDAO.remove(id);
            UserTemplate template = new UserTemplate(id);                   
            result.code = ErrorCode.SUCCESS;
            result.data = template;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
