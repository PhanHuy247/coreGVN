/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.init;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import com.vn.ntsc.backend.dao.admin.RoleDAO;
import com.vn.ntsc.backend.dao.admin.RoleGroupDAO;
import com.vn.ntsc.backend.dao.screen.ScreenDAO;
import com.vn.ntsc.backend.dao.screen.ScreenGroupDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.screen.ScreenGroup;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.AdminStringDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.InitData;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.admin.AdminSettingDAO;
import com.vn.ntsc.backend.entity.impl.admin.AdminSetting;

/**
 *
 * @author RuAc0n
 */
public class InitApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            List<IEntity> roles = RoleDAO.getAll(null);
            List<IEntity> screenGroups = ScreenGroupDAO.getAll(new Long(Constant.FLAG.ON));
            for (IEntity entity : screenGroups) {
                String groupId = ((ScreenGroup) entity).id;
                List<String> groupRole = RoleGroupDAO.listRole(groupId);
                ((ScreenGroup) entity).roles = groupRole;
                List<IEntity> screens = ScreenDAO.getScreenByGroupId(groupId);
                ((ScreenGroup) entity).screens = screens;
            }
            String string = AdminStringDAO.get();
            AdminSetting setting = AdminSettingDAO.get();
            InitData data = new InitData(roles, screenGroups, string, setting);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
