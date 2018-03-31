/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.user;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.log.LogPointDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.user.Point;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class AddPointApi implements IApiAdapter {

    private static final int point_error = 4;

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            Long point = Util.getLongParam(obj, ParamKey.POINT);
            String adminName = Util.getStringParam(obj, ParamKey.ADMIN_NAME);// LongLT 19Sep2016 /////////////////////////// START #4295
            if (id == null || id.isEmpty()) {
                return null;
            }
            if (point == null) {
                return new Respond(point_error);
            }
            if (("".equals(adminName))|| (adminName == null)) {
                return new Respond(point_error);
            }
            if(point != 0 ){ 
                Point pnt = UserDAO.addPoint(id, point.intValue());
                // LongLT 19Sep2016 /////////////////////////// START #4295
//                LogPointDAO.addLog(id, 14, null, Util.getGMTTime(), null, pnt);
                LogPointDAO.addLog(id, 14, adminName, Util.getGMTTime(), null, pnt);
            }
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
