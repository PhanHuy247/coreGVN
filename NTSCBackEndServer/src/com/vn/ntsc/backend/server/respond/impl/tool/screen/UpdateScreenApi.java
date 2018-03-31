/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.screen;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.backend.dao.screen.ScreenDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class UpdateScreenApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            String title = Util.getStringParam(obj, ParamKey.TITLE);
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            String name = Util.getStringParam(obj, ParamKey.NAME);
            String path = Util.getStringParam(obj, "path");
            String groupId = Util.getStringParam(obj, "group_id");
            String controller = Util.getStringParam(obj, "controller");
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            ScreenDAO.update(id, name, path, title, flag.intValue(), groupId, controller, order.intValue());
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
