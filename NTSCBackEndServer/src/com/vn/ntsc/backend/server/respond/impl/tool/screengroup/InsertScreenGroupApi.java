/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.screengroup;

import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.RoleDAO;
import com.vn.ntsc.backend.dao.admin.RoleGroupDAO;
import com.vn.ntsc.backend.dao.screen.ScreenGroupDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import com.vn.ntsc.backend.server.rolegroupmanagement.RoleGroupManager;

/**
 *
 * @author RuAc0n
 */
public class InsertScreenGroupApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String title = Util.getStringParam(obj, ParamKey.TITLE);
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            String name = Util.getStringParam(obj, ParamKey.NAME);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            String id = ScreenGroupDAO.insert(title, flag.intValue(), name, order.intValue());
            String adminRoleId = RoleDAO.getRoleId(Constant.FLAG.ON);
            RoleGroupDAO.update(id, adminRoleId);
            List<String> listGroup = RoleGroupManager.getListGroup(adminRoleId);
            if (listGroup == null) {
                listGroup = new ArrayList<>();
            }
            listGroup.add(id);
            RoleGroupManager.add(adminRoleId, listGroup);
            respond = new EntityRespond(ErrorCode.SUCCESS, new InsertData(id));
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode(), null);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
