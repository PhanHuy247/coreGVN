/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.screengroup;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.screen.ScreenGroupDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class UpdateScreenGroupApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            String title = Util.getStringParam(obj, ParamKey.TITLE);
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            String name = Util.getStringParam(obj, ParamKey.NAME);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            ScreenGroupDAO.update(id, title, flag.intValue(), name, order.intValue());
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
