/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.adminstring;

import eazycommon.constant.ErrorCode;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.AdminStringDAO;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class SetStringApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String str = Util.getStringParam(obj, "str");
            AdminStringDAO.insert(str);
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
