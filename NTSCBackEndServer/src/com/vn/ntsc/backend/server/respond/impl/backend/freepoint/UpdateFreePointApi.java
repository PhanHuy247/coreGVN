/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.freepoint;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.FreePointDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class UpdateFreePointApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            if (id == null || id.isEmpty()) {
                return null;
            }
            String name = Util.getStringParam(obj, "free_point_name");
            Long number = Util.getLongParam(obj, "free_point_number");
            if(name == null || number == null){
                return respond;
            }
            int code = FreePointDAO.validate(id, name, number.intValue());
            if(code != ErrorCode.SUCCESS){
                respond.code = code;
                return respond;
            }
            FreePointDAO.update(id, number.intValue(), name);
            respond.code = ErrorCode.SUCCESS;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
