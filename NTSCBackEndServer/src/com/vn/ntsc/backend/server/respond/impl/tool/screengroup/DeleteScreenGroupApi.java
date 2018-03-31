/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.screengroup;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.RoleGroupDAO;
import com.vn.ntsc.backend.dao.screen.ScreenAPIDAO;
import com.vn.ntsc.backend.dao.screen.ScreenDAO;
import com.vn.ntsc.backend.dao.screen.ScreenGroupDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.rolegroupmanagement.RoleGroupManager;
import com.vn.ntsc.backend.server.rolegroupmanagement.ScreenAPIManager;

/**
 *
 * @author RuAc0n
 */
public class DeleteScreenGroupApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            ScreenGroupDAO.remove(id);
            RoleGroupDAO.removeByGroup(id);
            RoleGroupManager.reset();
            List<String> list = ScreenDAO.removeByGroupId(id);
            if (!list.isEmpty()) {
                ScreenAPIDAO.removeByScreen(list);
                ScreenAPIManager.reset();
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
